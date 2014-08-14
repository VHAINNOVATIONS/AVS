package gov.va.med.lom.javaUtils.xml;

import gov.va.med.lom.javaUtils.classloader.ClassLoaderStrategy;
import gov.va.med.lom.javaUtils.classloader.ClassLoaderUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import javax.xml.rpc.NamespaceConstants;

import org.w3c.dom.*;
import org.apache.axis.Constants;
import org.relaxng.datatype.DatatypeException;

import com.sun.msv.datatype.xsd.DatatypeFactory;
import com.sun.msv.datatype.xsd.XSDatatype;

public class BeanDeserializer extends XMLDataTypes {
  
  // Instance fields
  private ClassLoaderStrategy classLoaderStrategy;
  private String[] classLoaderArgs;
  private String[] classPackages;
  private String currentName;
  private String currentType;
  private Bean currentBean;
  private Node currentNode;
  private Node parentNode;
  private Node root;
  private Stack beansStack;

  // Inner class encapsulating a bean
  public class Bean {
    
    private BeanInfo beanInfo;
    private Object beanObject;
    private Properties propDesc;
    
    public Bean(String classType, ClassLoaderStrategy cls,
                String[] classLoaderArgs) throws Exception {
      // Get the no-arg constructor and create the bean
      try {
        Class classOfBean = BeanDeserializer.getClassOfBean((ClassLoader)cls, classPackages, classType);
        Constructor ct = classOfBean.getConstructor((Class[])null);
        beanObject = ct.newInstance((Object[])null);
        // Get an array of property descriptors
        beanInfo = Introspector.getBeanInfo(classOfBean);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        // load property descriptors into hashtable
        propDesc = new Properties();
        for (int i = 0; i < pds.length; i++) {
          propDesc.put(pds[i].getName(), pds[i]);
        }
      } catch(Exception e) {
        System.err.println("Exception creating bean: " + e.getMessage());
        e.printStackTrace();
        throw e;
      }
    }  
    
    public String getPropertyName(String name) {
      // Set the first letter of the property name to lower-case
      StringBuffer propertyName = new StringBuffer(name);
      char c = propertyName.charAt(0);
      if (c >= 'A' && c <= 'Z') {
        c -= 'A' - 'a';
        propertyName.setCharAt(0, c);
      }
      return propertyName.toString();
    }
    
    public PropertyDescriptor getPropertyDescriptor(String name) {
      return (PropertyDescriptor)propDesc.get(getPropertyName(name));
    }
    
    public Class getPropertyType(String name) {
      PropertyDescriptor pd = getPropertyDescriptor(name);
      return pd.getPropertyType();
    }
    
    public void setProperty(String name, Object object) {
      PropertyDescriptor pd = getPropertyDescriptor(name);
      if (pd != null) {
        Object[] args = {object};
        Object propertyValue = null;
        try {
          Method setter = pd.getWriteMethod();
          if (setter != null) {
            propertyValue = setter.invoke(beanObject, args);
          }
        } catch (Exception e) {
          System.err.println("Exception setting bean property: " + e.getMessage());
          System.err.println("Property name: " + name);
          System.err.println("Property value: " + propertyValue);
        }
      } 
    }
    
    public Object getBeanObject() {
      return beanObject;
    }
  }  
  
  // CONSTRUCTORS
  public BeanDeserializer(String strategy, String[] classLoaderArgs, String[] classPackages) throws Exception {
    beansStack = new Stack();
    this.classLoaderArgs = classLoaderArgs;
    this.classPackages = classPackages;
    try {
      // Create a class loader strategy
      classLoaderStrategy = ClassLoaderUtil.getClassLoader(strategy, classLoaderArgs);
    } catch(Exception e) {
      System.err.println("Exception creating class loader strategy: " + strategy);
      System.err.println(e.getMessage());
      throw e;
    }
  }  
  
  // STATIC ACCESSORY METHODS
  public static Class getClassOfBean(ClassLoader classLoader, String[] classesPackages, String classType) {
    Class cls = null;
    for (int i = 0; i < classesPackages.length; i++) {
      try {
        cls = classLoader.loadClass(classesPackages[i] + "." + classType);
        break;
      } catch(ClassNotFoundException cnfe) {
      }
    }
    return cls;
  }    
  
  // PUBLIC METHODS
  public Object deserialize(String xml) throws Exception {
    DOMDocument domDocument = new DOMDocument(xml);
    root = domDocument.getRootNode();
    return deserialize(root);
  }
  
  public Class getClassOfBean(String classType) throws ClassNotFoundException {
    return getClassOfBean((ClassLoader)classLoaderStrategy, classPackages, classType);
  }
  
  public Bean createBean(String classType) throws Exception {
    return new Bean(classType, classLoaderStrategy, classLoaderArgs);
  }  
  
  // PRIVATE METHODS
  private Object pushBeanOnStack(String type) {
    try {
      Bean bean = new Bean(type, classLoaderStrategy, classLoaderArgs);
      parentNode = currentNode;
      currentBean = (Bean)beansStack.push(bean);
      return bean.getBeanObject();
    } catch(Exception e) {
      // bean could not be created
      System.err.println("bean could not be created: " + type);
      return null;
    }          
  }
  
  private Object popBeanOffStack(Node node) {
    // pop the bean off the stack
    if ((parentNode != root) && (beansStack.size() > 0)) {
      currentBean = (Bean)beansStack.pop();
      return currentBean.getBeanObject();
    }
    return null;
  }
  
  private void setBeanProperty(String name, Object object) {
    Bean parentBean = null;
    if (beansStack.size() > 0) {
      parentBean = (Bean)beansStack.peek();
      parentBean.setProperty(name, object);
    }       
  }
  
  private Object createObject(String name, String type, String value, boolean setProperty) {
    Bean parentBean = null;
    if (beansStack.size() > 0)
      parentBean = (Bean)beansStack.peek();
    Object object = null;
    // param is either an XSD type or a bean,
    // check if it can be converted to an XSD type
    XSDatatype dt = null;
    try {
      dt = DatatypeFactory.getTypeByName(type);
    } catch(DatatypeException dte) {
      // the type is not a valid XSD data type
    }      
    // check whether the type was converted to an XSD datatype
    if ((dt != null) && dt.isValid(value, null)) {
      // create and return an XSD Java object (e.g. String, Integer, Boolean, etc)
      object = dt.createJavaObject(value, null);
      if (object instanceof java.util.Calendar) {
        // check that the object is truly a Calendar
        // because DatatypeFactory converts xsd:date
        // types to GregorianCalendar instead of Date.
        if (type.equals("date")) {
          java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
          try {
            object = df.parse(value);
          } catch(java.text.ParseException pe) {
            object = new java.util.Date();
          }
        }
      }
    } else {
      // Create a bean object
      object = pushBeanOnStack(type);
    }
    if ((parentBean != null) && setProperty) {
      parentBean.setProperty(name, object);
    }
    return object;
  }
  
  private Object deserialize(Node node) throws Exception {
    return deserialize(node, true, true);
  }
  
  private Object deserialize(Node node, boolean setProperty, boolean popBean) throws Exception {
    Object object = null;
    currentType = null;    
    currentNode = node;
    currentName = node.getNodeName();
    NamedNodeMap attrs = node.getAttributes();
    String arrayType = null;
    for (int i = 0; i < attrs.getLength(); i++) {
      String nodeName = attrs.item(i).getNodeName();
      String nodeValue = attrs.item(i).getNodeValue();
      if (nodeName.equals(NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                          Constants.ATTR_TYPE))
        currentType = new StringBuffer(nodeValue).delete(0, nodeValue.indexOf(':') + 1).toString();
      else if (nodeName.equals(NamespaceConstants.NSPREFIX_SOAP_ENCODING + ":" + 
                               Constants.ATTR_ARRAY_TYPE))
        arrayType = new StringBuffer(nodeValue).delete(0, nodeValue.indexOf(':') + 1).toString();
    }
    Class cls = null;
    if (currentType != null) 
      cls = getXsdTypeClass(currentType);
    // Handle array, Vector, ArrayList, LinkedList, Hashtable, 
    // Properties, and HashMap data types 
    if ((cls != null) && ((cls == java.lang.reflect.Array.class) || 
        (cls == Vector.class) || (cls == ArrayList.class) || 
        (cls == LinkedList.class) || (cls == Hashtable.class) || 
        (cls == Properties.class) || (cls == HashMap.class) || 
        (cls == SortedMap.class))) {
      parentNode = currentNode;
      String name = node.getNodeName();
      // Handle arrays
      if (cls == java.lang.reflect.Array.class) {
        int a = arrayType.indexOf("[");
        int b = arrayType.indexOf("]");
        String s = arrayType.substring(a+1, b);
        int arrayLen = Integer.valueOf(s).intValue();
        arrayType = arrayType.substring(0, a);
        // check if the array element is a standard Java class
        Class arrayClass = getXsdTypeClass(arrayType);
        // otherwise try to get the class of the bean
        if (arrayClass == null)
          arrayClass = getClassOfBean((ClassLoader)classLoaderStrategy, classPackages, arrayType);
        object = java.lang.reflect.Array.newInstance(arrayClass, arrayLen);
      } else {
        // Construct the list or map type
        Constructor ct = cls.getConstructor((Class[])null);
        object = ct.newInstance((Object[])null);
      }
      // deserialize the elements of the array, list, or map
      NodeList childNodes = node.getChildNodes();
      int arrayIndex = -1;
      Node childNode = null;
      Object nodeObj = null;
      for (int i = 0; i < childNodes.getLength(); i++) {
        childNode = childNodes.item(i);
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
          if ((cls == java.lang.reflect.Array.class) || 
              (cls == Vector.class) || (cls == ArrayList.class) || 
              (cls == LinkedList.class)) {
            nodeObj = deserialize(childNode, false, true);
            if (nodeObj != null) {
              if (cls == java.lang.reflect.Array.class) 
                java.lang.reflect.Array.set(object, ++arrayIndex, nodeObj);
              else 
                ((List)object).add(nodeObj);   
            }
          } else if ((cls == Hashtable.class) || 
                     (cls == Properties.class) || 
                     (cls == HashMap.class) || 
                     (cls == SortedMap.class)) {
            if (childNode.getLocalName().equals("item")) {
              NodeList htNodes = childNode.getChildNodes();
              if (htNodes.getLength() == 2) {
                Object hashKey = deserialize(htNodes.item(0), false, false); 
                Object hashValue = deserialize(htNodes.item(1), false, true); 
                ((Map)object).put(hashKey, hashValue);
              }
            }
          }
        }
      }
      setBeanProperty(name, object);          
    // Handle everything else (primitives & POJOs)  
    } else {
      // recurse on each of the child nodes
      NodeList childNodes = node.getChildNodes();
      if (childNodes != null) {
        for (int i = 0; i < childNodes.getLength(); i++) {
          Node childNode = childNodes.item(i);
          if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            if (currentType != null)
              createObject(currentName, currentType, childNode.getNodeValue(), setProperty);
            parentNode = node;
            object = deserialize(childNode, true, true);
          } else if ((childNode.getNodeType() == Node.TEXT_NODE) && (currentType != null)) {
            //if (currentType == null)
              //currentType = "string";
            object = createObject(currentName, currentType, childNode.getNodeValue(), setProperty);
          }
          currentType = null;
        }
      }
      if (node.getParentNode() != parentNode) {
        parentNode = node.getParentNode();
        if (popBean) {
          Object bean = popBeanOffStack(node);
          if (bean != null)
            object = bean;
        }
      }
    }
    return object;
  }
}
