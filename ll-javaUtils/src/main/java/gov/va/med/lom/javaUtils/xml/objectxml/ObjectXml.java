package gov.va.med.lom.javaUtils.xml.objectxml;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;
import java.beans.*;

import javax.xml.rpc.NamespaceConstants;
import javax.xml.XMLConstants;

import org.relaxng.datatype.DatatypeException;
import org.w3c.dom.*;
import org.apache.axis.Constants;

import com.sun.msv.datatype.xsd.DatatypeFactory;
import com.sun.msv.datatype.xsd.XSDatatype;

import gov.va.med.lom.javaUtils.xml.XMLDataTypes;
import gov.va.med.lom.javaUtils.xml.DOMDocument;
import gov.va.med.lom.javaUtils.classloader.ClassLoaderStrategy;
import gov.va.med.lom.javaUtils.classloader.ClassLoaderUtil;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.javaUtils.misc.DateUtils;

import gov.va.med.lom.javaUtils.xml.objectxml.annotations.*;

public class ObjectXml extends XMLDataTypes {
  
  // Instance fields
  private ClassLoaderStrategy classLoaderStrategy;
  private Document document;
  private String currentName;
  private String currentPackage;
  private String currentType;
  private Bean currentBean;
  private String rootPath;
  private String classPackage;
  private Node currentNode;
  private Node parentNode;
  private Node root;
  private Stack<Bean> beansStack;
  private Bean topLevelBean;
  
  private String nsPrefix;
  private String nsURI;
  private DOMDocument domDocument;
  private boolean includeXmlProlog;
  private boolean prettyPrint;
  private String lineSeperator;
  private String indentChars;
  private boolean lowerCase;
  private boolean firstLetterUpperCase;
  private boolean includeTypeInfo;
  private boolean includeNullValues;
  
  // Inner class encapsulating a bean
  public class Bean {
    
    private BeanInfo beanInfo;
    private Object beanObject;
    private Properties propDesc;
    
    public Bean(String classPackage, String clazz, ClassLoaderStrategy cls, Bean topLevelBean) throws Exception {
      // Get the no-arg constructor and create the bean
      try {
        Class classOfBean = ObjectXml.getClassOfBean((ClassLoader)cls, classPackage + "." + clazz);
        Constructor ct = null;
        // check whether this class is an inner class
        if (classOfBean.getEnclosingClass() != null) { 
          ct = classOfBean.getConstructor(new Class[] {classOfBean.getEnclosingClass()});
          beanObject = ct.newInstance(new Object[] {topLevelBean.getBeanObject()});
        } else {
          ct = classOfBean.getConstructor((Class[])null);
          beanObject = ct.newInstance((Object[])null);
        }
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
  
  /* CONSTRUCTORS */
  public ObjectXml() throws Exception {
    nsPrefix = "oxml";
    nsURI = null;
    includeXmlProlog = true;
    prettyPrint = true;
    lineSeperator = "\n";
    indentChars = "  ";
    lowerCase = false;
    firstLetterUpperCase = false;
    includeTypeInfo = true;
    beansStack = new Stack<Bean>();
  }  
  
  /* STATIC ACCESSORY METHODS */
  public static Class getClassOfBean(ClassLoader classLoader, String clazz) {
    Class cls = null;
    try {
      cls = classLoader.loadClass(clazz);
    } catch(ClassNotFoundException cnfe) {
    }
    return cls;
  }   
  
  
  /* ACCESSOR METHODS */
  public String getNsPrefix() {
    return nsPrefix;
  }

  public void setNsPrefix(String nsPrefix) {
    this.nsPrefix = nsPrefix;
  }

  public String getNsURI() {
    return nsURI;
  }

  public void setNsURI(String nsURI) {
    this.nsURI = nsURI;
  }

  public String getClassPackage() {
    return this.classPackage;
  }

  public void setClassPackage(String classPackage) {
    this.classPackage = classPackage;
  }
  
  public DOMDocument getDomDocument() {
    return domDocument;
  }
  
  public boolean isFirstLetterUpperCase() {
    return firstLetterUpperCase;
  }

  public void setFirstLetterUpperCase(boolean firstLetterUpperCase) {
    this.firstLetterUpperCase = firstLetterUpperCase;
  }
  
  public boolean getIncludeNullValues() {
    return this.includeNullValues;
  }

  public void setIncludeNullValues(boolean includeNullValues) {
    this.includeNullValues = includeNullValues;
  }

  public boolean isIncludeTypeInfo() {
    return includeTypeInfo;
  }

  public void setIncludeTypeInfo(boolean includeTypeInfo) {
    this.includeTypeInfo = includeTypeInfo;
  }
  
  public boolean getIncludeXmlProlog() {
    return this.includeXmlProlog;
  }

  public void setIncludeXmlProlog(boolean includeXmlProlog) {
    this.includeXmlProlog = includeXmlProlog;
  }
  
  public String getIndentChars() {
    return indentChars;
  }

  public void setIndentChars(String indentChars) {
    this.indentChars = indentChars;
  }

  public String getLineSeperator() {
    return lineSeperator;
  }

  public void setLineSeperator(String lineSeperator) {
    this.lineSeperator = lineSeperator;
  }

  public boolean isLowerCase() {
    return lowerCase;
  }

  public void setLowerCase(boolean lowerCase) {
    this.lowerCase = lowerCase;
  }
  
  public boolean isPrettyPrint() {
    return prettyPrint;
  }

  public void setPrettyPrint(boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
  }

  public String getRootPath() {
    return this.rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  /* PUBLIC METHODS */
  // Serialize the bean using the specified namespace prefix & uri
  public String serialize(Object bean) throws IntrospectionException,
                                              IllegalAccessException {
    // Use the class name as the name of the root element
    String className = bean.getClass().getName();
    String rootElementName = null;
    if (bean.getClass().isAnnotationPresent(ObjectXmlAlias.class)) {
      AnnotatedElement annotatedElement = bean.getClass();      
      ObjectXmlAlias aliasAnnotation = annotatedElement.getAnnotation(ObjectXmlAlias.class);
      rootElementName = aliasAnnotation.value();
    }
    // Use the package name as the namespace URI
    Package pkg = bean.getClass().getPackage();
    nsURI = pkg.getName();
    // Remove a trailing semi-colon (;) if present (i.e. if the bean is an array)
    className = StringUtils.deleteTrailingChar(className, ';');
    StringBuffer sb = new StringBuffer(className);
    String objectName = sb.delete(0, sb.lastIndexOf(".") + 1).toString();
    domDocument = createDomDocument(objectName);
    document = domDocument.getDocument();
    Element root = document.getDocumentElement();
    // Parse the bean elements
    getBeanElements(root, rootElementName, className, bean);
    StringBuffer xml = new StringBuffer();
    if (prettyPrint)
      xml.append(domDocument.serialize(lineSeperator, indentChars, includeXmlProlog));
    else
      xml.append(domDocument.serialize(includeXmlProlog));
    if (!includeTypeInfo) {
      int index = xml.indexOf(root.getNodeName());
      xml.delete(index - 1, index + root.getNodeName().length() + 2);
      xml.delete(xml.length() - root.getNodeName().length()-4, xml.length());
    }
    return xml.toString();
  }
  
  public Object deserialize(Class rootClass, String xml) throws Exception {
    DOMDocument domDocument = new DOMDocument(xml);
    root = domDocument.getRootNode();
    if ((classPackage == null) || (rootPath == null))
      classLoaderStrategy = getClassLoaderStrategy(rootClass.getName());
    else {
      currentPackage = classPackage;
      classLoaderStrategy = ClassLoaderUtil.getClassLoader(ClassLoaderUtil.FILE_SYSTEM_CLASS_LOADER, 
                                                           new String[] {rootPath});
    }
    return deserialize(root, true, true);
  }
  
  public Object deserialize(String xml) throws Exception {
    DOMDocument domDocument = new DOMDocument(xml);
    root = domDocument.getRootNode();
    if ((classPackage == null) || (rootPath == null))
      classLoaderStrategy = getClassLoaderStrategy(root.lookupNamespaceURI(nsPrefix) + "." + root.getLocalName());
    else {
      currentPackage = classPackage;
      classLoaderStrategy = ClassLoaderUtil.getClassLoader(ClassLoaderUtil.FILE_SYSTEM_CLASS_LOADER, 
                                                           new String[] {rootPath});
    }    
    return deserialize(root, true, true);
  }
  
  /* PROTECTED METHODS */
  protected void getBeanElements(Element parentElement, String objectName,  
                                 String objectType, Object bean) throws IntrospectionException, 
                                                                        IllegalAccessException {
    if (objectName == null) {
      // Get just the class name by lopping off the package name
      StringBuffer sb = new StringBuffer(bean.getClass().getName());
      sb.delete(0, sb.lastIndexOf(".") + 1);
      objectName = sb.toString();
    }
    
    // Check if the bean is a standard Java object type or a byte[] (encoded as a base 64 array)
    Element element = getStandardObjectElement(document, bean, objectName);
    // If the body element object is null then the bean is not a standard Java object type
    if (element != null)  {
      if (includeNullValues || !element.getAttribute(NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                                                     org.apache.axis.Constants.ATTR_TYPE).equals("anyType")) {
        if (!includeTypeInfo) {
          element.removeAttribute(NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                                  org.apache.axis.Constants.ATTR_TYPE);
          element.removeAttribute(NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":null");
        }
        parentElement.appendChild(element);
      }
    } else {
      // Analyze the bean
      Class classOfBean = null;
      if (bean != null)
        classOfBean = bean.getClass();
      // If the object is an array, then serialize each of the beans in the array.
      if ((classOfBean != null) && (classOfBean.isArray())) {
        String[] arrayInfo = getXsdSoapArrayInfo(classOfBean.getCanonicalName(), nsPrefix);
        int arrayLen = Array.getLength(bean);
        StringBuffer arrayType = new StringBuffer(arrayInfo[1]);
        arrayType.insert(arrayType.indexOf("[]") + 1, arrayLen);
        if (objectName.charAt(objectName.length() - 1) == ';')
          objectName = new StringBuffer(objectName).deleteCharAt(objectName.length() - 1).toString();
          element  = document.createElement(objectName);
        parentElement.appendChild(element);
        // Get the bean objects from the array and serialize each
        for(int i=0; i < arrayLen; i++) {
          Object b = Array.get(bean, i);
          if (b != null) {
            String name = null;
            if (objectName.charAt(objectName.length()-1) == 's') {
              name = formatName(objectName.substring(0, objectName.length()-1));
            }
            getBeanElements(element, name, b.getClass().getName(), b);
          } else {
            // Array element is null, so don't include it and decrement the # elements in the array
            int index = arrayType.indexOf("[");
            arrayType.replace(index + 1, index + 2, String.valueOf(--arrayLen));
          }
          if (includeTypeInfo) {
            element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                Constants.ATTR_TYPE, NamespaceConstants.NSPREFIX_SOAP_ENCODING + ":Array");
            element.setAttributeNS(NamespaceConstants.NSURI_SOAP_ENCODING, NamespaceConstants.NSPREFIX_SOAP_ENCODING + ":" + 
                Constants.ATTR_ARRAY_TYPE, arrayInfo[0] + ":" + arrayType.toString());
          }
        }
      } else {
        int beanType = 0;
        String beanName = null;
        if (classOfBean != null) {
          if (classOfBean == Vector.class) {
            beanType = 1;
            beanName = "Vector";
          } else if (classOfBean == ArrayList.class) { 
            beanType = 2;  
            beanName = "ArrayList";
          } else if (classOfBean == LinkedList.class) {
            beanType = 3;
            beanName = "LinkedList";
          } else if (classOfBean == Hashtable.class) {
            beanType = 4;
            beanName = "Hashtable";
          } else if (classOfBean == Properties.class) {
            beanType = 5;
            beanName = "Properties";
          } else if ((classOfBean == HashMap.class) || 
                     (classOfBean == SortedMap.class)) {
            beanType = 6;
            beanName = "Map";
          }
        }
        if (beanType > 0) {
          String prefix = null;
          if ((beanType == 1) || (beanType == 5))
            prefix = NamespaceConstants.NSPREFIX_SOAP_ENCODING;
          if (beanType == 6)
            prefix = Constants.NS_PREFIX_XMLSOAP;
          else 
            prefix = DEFAULT_NS_PREFIX;
          element  = document.createElement(objectName);
          if (includeTypeInfo) {
            element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                                   Constants.ATTR_TYPE, prefix + ":" + beanName);
            if (bean == null)
              element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":null", "true");
          }
          parentElement.appendChild(element);
          if ((beanType >= 1) && (beanType <= 3)) {
            AbstractCollection collection = (AbstractCollection)bean; 
            // Get the bean objects from the vector and serialize each
            Iterator it = collection.iterator();
            while(it.hasNext()) {
              Object b = it.next();
              String name = null;
              if (b != null) {
                if (objectName.charAt(objectName.length()-1) == 's') {
                  name = formatName(objectName.substring(0, objectName.length()-1));
                } else
                  name = "item";
              }
              getBeanElements(element, name, b.getClass().getName(), b);
            }                
          } else if ((beanType == 4) || (beanType == 5)) {
            Hashtable hashtable = (Hashtable)bean; 
            // Get the bean objects from the hashtable or properties and serialize each
            Enumeration en = hashtable.keys();
            while(en.hasMoreElements()) {
              Object key = en.nextElement();
              String keyClassName = key.getClass().getName();
              Object value = hashtable.get(key);
              String beanClassName = null;
              if (value != null)
                beanClassName = value.getClass().getName();
              Element itemElement  = document.createElement("item");
              element.appendChild(itemElement);
              getBeanElements(itemElement, "key", keyClassName, key);
              getBeanElements(itemElement, "value", beanClassName, value);
            }                 
          } else if (beanType == 6) {
            Map map = null;
            if (classOfBean == HashMap.class)
              map = (HashMap)bean;
            else if (classOfBean == SortedMap.class)
              map = (SortedMap)bean;
            // Get the bean objects from the hashmap and serialize each
            Set set = map.keySet();
            Iterator it = set.iterator();
            while(it.hasNext()) {
              Object key = it.next();
              String keyClassName = key.getClass().getName();
              Object value = map.get(key);
              String beanClassName = null;
              if (value != null)
                beanClassName = value.getClass().getName();
              Element itemElement  = document.createElement("item");
              element.appendChild(itemElement);
              getBeanElements(itemElement, "key", keyClassName, key);
              getBeanElements(itemElement, "value", beanClassName, value);
            }               
          }
        } else {
          // Create a parent element for this bean's properties
          if (objectName.charAt(objectName.length() - 1) == ';')
            objectName = new StringBuffer(objectName).deleteCharAt(objectName.length() - 1).toString();
          objectName = formatName(objectName);
          element = document.createElement(objectName);
          parentElement.appendChild(element);
          if (includeTypeInfo) {
            StringBuffer className = new StringBuffer(objectType);
            element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                                   Constants.ATTR_TYPE, nsPrefix + ":" + className.delete(0, className.lastIndexOf(".") + 1).toString());
          }
          if (classOfBean != null) {
            // Get an array of property descriptors
            BeanInfo bi = Introspector.getBeanInfo(classOfBean);
            PropertyDescriptor[] pds = bi.getPropertyDescriptors();
            // For each property of the bean, get a SOAPBodyElement that
            // represents the individual property. Append that SOAPBodyElement
            // to the class name element of the SOAP body.
            for (int i = 0; i < pds.length; i++) {
              PropertyDescriptor pd = pds[i];
              getBeanElementProperties(element, bean, pd);
            }
          } else {
            if (includeTypeInfo)            
              element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":null", "true");
          }
        }
      }
    }
  }

  protected void getBeanElementProperties(Element element,Object bean,  
                                          PropertyDescriptor pd) throws IntrospectionException,
                                                                        IllegalAccessException {
    Element propertyElement = null;
    Class classOfProperty = pd.getPropertyType();
    Object[] argsNone = {};
    // If the property is "class" and the type is java.lang.Class.class then
    // this is the class of the bean, which we've already encoded.
    // In this special case, return null.
    if (!((pd.getName().charAt(0) == 'c') && pd.getName().equals("class")) && 
         !classOfProperty.equals(java.lang.Class.class)) {
      // Don't process property if it is in the list of fields to be ignored
      boolean omittedField = false;
      try {
        Field field = bean.getClass().getDeclaredField(pd.getName());
        omittedField = field.isAnnotationPresent(ObjectXmlOmitField.class);
      } catch(NoSuchFieldException nsfe) {}
      if (!omittedField) {
        String propertyName = formatName(pd.getName());
        // Hereafter, we're trying to create a representation of the property
        // based on the property's value.
        // The very first thing we need to do is get the value of the
        // property as an object. If we can't do that, we can't get any
        // representation of the property at all.
        Object propertyValue = null;
        try {
          Method getter = pd.getReadMethod();
          if (getter != null) {
            propertyValue = getter.invoke(bean, argsNone);
          }
        } catch (Exception ex) {
          // couldn't get value 
          System.err.println(ex.getMessage());
        }
        // See if this property's value is something we can represent as a
        // standard data type that can be converted to an xsd type,
        // or if it's something that must be represented as a JavaBean.
        if (propertyElement == null) {
          
          PropertyEditor propEditor = PropertyEditorManager.findEditor(classOfProperty);
          // If the property editor is not null, pass the property's
          // value to the PropertyEditor, and then ask the PropertyEditor
          // for the object and then attempt to convert it to a standard type.
          
          if ((propEditor != null) || (classOfProperty == java.util.Calendar.class) ||
              (classOfProperty == java.util.Date.class)) {
            // The object is a standard type 
            // (e.g. a wrapper around a primitive type, a String, or a Date or Calendar object)
            try {
              Object object;
              if ((classOfProperty == java.util.Calendar.class) ||
                  (classOfProperty == java.util.Date.class))
                object = propertyValue;
              else {
                propEditor.setValue(propertyValue);                
                object = propEditor.getValue();
              }
              Element childElement = getStandardObjectElement(document, object, propertyName);
              if (includeNullValues || !childElement.getAttribute(NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                                                                  org.apache.axis.Constants.ATTR_TYPE).equals("anyType")) {
                if (!includeTypeInfo) {
                  childElement.removeAttribute(NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                                               org.apache.axis.Constants.ATTR_TYPE);
                  childElement.removeAttribute(NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":null");
                }
                try {
                  Field field = bean.getClass().getDeclaredField(propertyName);
                  if (field.isAnnotationPresent(ObjectXmlAsAttribute.class)) {
                    String attrName = null;
                    if (includeTypeInfo)
                      attrName = nsPrefix + ":" + propertyName;
                    else
                      attrName = propertyName;
                    element.setAttribute(attrName, childElement.getFirstChild().getNodeValue());
                  } else if (field.isAnnotationPresent(ObjectXmlAsValue.class))
                    element.setTextContent(childElement.getFirstChild().getNodeValue());
                  else
                    element.appendChild(childElement);
                } catch(NoSuchFieldException nfse) {
                  element.appendChild(childElement);
                }
              }
            } catch (Exception e) {
            }
          } else {
            // The object is not an XSD-encoded datatype, it must be a JavaBean
            try {
              String propertyTypeName = null;
              if (propertyValue != null) {
                // get object type
                Class propertyType = pd.getPropertyType();
                propertyTypeName = propertyType.getName();
              } 
              getBeanElements(element, propertyName, propertyTypeName, propertyValue);
            } catch (Exception e) {
            }
          }
        }
      }
    }
  }  
  
  /* PRIVATE METHODS */
  private DOMDocument createDomDocument(String rootName) {
    DOMDocument domDocument = new DOMDocument(nsURI, rootName, null);
    Document document = domDocument.getDocument();
    Element root = document.getDocumentElement();
    if (includeTypeInfo) {
      root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" +nsPrefix, nsURI);
      root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" + 
                          NamespaceConstants.NSPREFIX_SCHEMA_XSD, NamespaceConstants.NSURI_SCHEMA_XSD);    
      root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" + 
                          NamespaceConstants.NSPREFIX_SCHEMA_XSI, NamespaceConstants.NSURI_SCHEMA_XSI);
      root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" + 
                          NamespaceConstants.NSPREFIX_SOAP_ENCODING, NamespaceConstants.NSURI_SOAP_ENCODING);
      root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" + 
                          Constants.NS_PREFIX_XMLSOAP, Constants.NS_URI_XMLSOAP);
    }
    return domDocument;
  }
  
  private ClassLoaderStrategy getClassLoaderStrategy(String fullyQualifiedClassName) throws Exception {
    try {
      // Get just the package name
      StringBuffer sb = new StringBuffer(fullyQualifiedClassName);
      sb.delete(sb.lastIndexOf("."), sb.length());
      currentPackage = sb.toString();
      // Retrieve the Java classpath from the system properties
      String cp = System.getProperty("java.class.path");
      String sepChar = System.getProperty("path.separator");
      String[] paths = StringUtils.pieceList(cp, sepChar.charAt(0));
      ClassLoaderStrategy cl = 
        ClassLoaderUtil.getClassLoader(ClassLoaderUtil.FILE_SYSTEM_CLASS_LOADER, new String[] {});
      // Iterate through paths until class with the specified name is found
      String classpath = StringUtils.replaceChar(currentPackage, '.', File.separatorChar);
      for (int i = 0; i < paths.length; i++) {
        Class[] classes = cl.getClasses(paths[i] + File.separatorChar + classpath, currentPackage);
        for (int j = 0; j < classes.length; j++) {
          if (classes[j].getName().equals(fullyQualifiedClassName)) {
            return ClassLoaderUtil.getClassLoader(ClassLoaderUtil.FILE_SYSTEM_CLASS_LOADER, new String[] {paths[i]});
          }
        }
      }
      throw new Exception("Class could not be found.");
    } catch(Exception e) {
      System.err.println("Exception creating class loader strategy.");
      System.err.println(e.getMessage());
      throw e;
    }
  }
  
  private String formatName(String name) {
    StringBuffer propertyName = new StringBuffer();
    if (lowerCase)
      propertyName.append(name.toLowerCase());
    else
      propertyName.append(name);
    char c = propertyName.charAt(0);
    if (firstLetterUpperCase) {
      if (c >= 'a' && c <= 'z') {
        c += 'A' - 'a';
      }
    } else {
      if (c >= 'A' && c <= 'Z') {
        c -= 'A' - 'a';
      }        
    }
    propertyName.setCharAt(0, c);
    return propertyName.toString();
  }
  
  private Object pushBeanOnStack(String classPackage, String clazz) {
    try {
      Bean bean = new Bean(classPackage, clazz, classLoaderStrategy, topLevelBean);
      parentNode = currentNode;
      currentBean = (Bean)beansStack.push(bean);
      return bean.getBeanObject();
    } catch(Exception e) {
      // bean could not be created
      System.err.println("bean could not be created: " + classPackage + "." + clazz);
      return null;
    }          
  }
  
  private Object popBeanOffStack() {
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
  
  private Object createObject(Node node, String name, String classPackage, 
                              String type, String value, boolean setProperty) {
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
    // convert null value to default
    if ((dt != null) && (value == null)) {
      Class objType = dt.getJavaObjectType();
      if (objType == String.class)
        value = "";
      else if ((objType == java.math.BigInteger.class) || (objType == Long.class) || (objType == Integer.class) ||
               (objType == Short.class) || (objType == Byte.class))
        value = "0";
      else if ((objType == java.math.BigDecimal.class) || (objType == Double.class) || (objType == Float.class))
        value = "0.0";
      else if (objType == Boolean.class)
        value = "false";
      else if (objType == java.util.Date.class)
        value = DateUtils.getCurrentDate();
      else if (objType == java.util.Calendar.class)
        value = DateUtils.getCurrentDateTime();
    }
    //  check whether the type was converted to an XSD datatype
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
      if (topLevelBean == null)
        topLevelBean = parentBean; 
      object = pushBeanOnStack(classPackage, type);
      // Check fields to see if this property is the 'value' for the object
      Field[] fields = object.getClass().getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        if (fields[i].isAnnotationPresent(ObjectXmlAsValue.class)) {
          try {
            StringBuffer fieldName = new StringBuffer(fields[i].getName());
            char c = fieldName.charAt(0);
            if (c >= 'a' && c <= 'z') {
              c += 'A' - 'a';
            }
            fieldName.setCharAt(0, c);
            fieldName.insert(0, "set");
            Method method = object.getClass().getMethod(fieldName.toString(), new Class[] {fields[i].getType()});
            method.invoke(object, value);
            break;
          } catch(Exception e) {
            System.err.println(e.getMessage());
          }
        }
      }
      NamedNodeMap nodeAttrs = node.getAttributes();
      // iterate attributes and set field values for property attributes
      for (int i = 0; i < nodeAttrs.getLength(); i++) {
        String nodePrefix = nodeAttrs.item(i).getPrefix();
        if (nodePrefix.equals(nsPrefix)) {
          String nodeName = nodeAttrs.item(i).getLocalName();
          String nodeValue = nodeAttrs.item(i).getNodeValue();
          try {
            Field field = object.getClass().getDeclaredField(nodeName);
            if (field.isAnnotationPresent(ObjectXmlAsAttribute.class)) {
              StringBuffer fieldName = new StringBuffer(field.getName());
              char c = fieldName.charAt(0);
              if (c >= 'a' && c <= 'z') {
                c += 'A' - 'a';
              }
              fieldName.setCharAt(0, c);
              fieldName.insert(0, "set");
              Method method = object.getClass().getMethod(fieldName.toString(), new Class[] {field.getType()});
              if (field.getType() == String.class)
                method.invoke(object, nodeValue);
              else if (field.getType() == Boolean.TYPE)
                method.invoke(object, StringUtils.strToBool(nodeValue, "true"));
              else if (field.getType() == Byte.TYPE)
                method.invoke(object, Byte.valueOf(nodeValue).byteValue());
              else if (field.getType() == Character.TYPE)
                method.invoke(object, Character.valueOf(nodeValue.charAt(0)));
              else if (field.getType() == Double.TYPE)
                method.invoke(object, Double.valueOf(nodeValue).doubleValue());
              else if (field.getType() == Float.TYPE)
                method.invoke(object, Float.valueOf(nodeValue).floatValue());
              else if (field.getType() == Integer.TYPE)
                method.invoke(object, Integer.valueOf(nodeValue).intValue());
              else if (field.getType() == Long.TYPE)
                method.invoke(object, Long.valueOf(nodeValue).longValue());
              else if (field.getType() == Short.TYPE)
                method.invoke(object, Short.valueOf(nodeValue).shortValue());
            }
          } catch(Exception e) {
            System.err.println(e.getMessage());
          }
        }
      }
    }
    if ((parentBean != null) && (setProperty)) {
      parentBean.setProperty(name, object);
    }
    return object;
  }
  
  private Object deserialize(Node node, boolean setProperty, boolean popBean) throws Exception {
    Object object = null;
    currentType = null;    
    currentNode = node;
    currentName = node.getNodeName();
    boolean isNull = false;
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
      else if (nodeName.equals(NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":null"))
        isNull = nodeValue.equals("true");
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
          arrayClass = getClassOfBean((ClassLoader)classLoaderStrategy, arrayType);
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
      if ((childNodes != null) && (childNodes.getLength() > 0)) {
        for (int i = 0; i < childNodes.getLength(); i++) {
          Node childNode = childNodes.item(i);
          if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            if (currentType != null)
              createObject(node, currentName, currentPackage, currentType, childNode.getNodeValue(), setProperty);
            parentNode = node;
            object = deserialize(childNode, true, true);
          } else if ((childNode.getNodeType() == Node.TEXT_NODE) && (currentType != null)) {
            object = createObject(node, currentName, currentPackage, currentType, childNode.getNodeValue(), setProperty);
          }
          currentType = null;
        }
      } else {
        if (!isNull)
          object = createObject(node, currentName, currentPackage, currentType, null, setProperty);
      }
      if (node.getParentNode() != parentNode) {
        parentNode = node.getParentNode();
        if (popBean) {
          Object bean = popBeanOffStack();
          if (bean != null)
            object = bean;
        }
      }
    }
    return object;
  }  
  
}