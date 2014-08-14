package gov.va.med.lom.javaUtils.xml;

import java.lang.reflect.*;
import java.util.*;
import java.beans.*;

import javax.xml.rpc.NamespaceConstants;
import javax.xml.XMLConstants;
import org.w3c.dom.*;
import org.apache.axis.Constants;

import gov.va.med.lom.javaUtils.xml.DOMDocument;
import gov.va.med.lom.javaUtils.misc.StringUtils;

public class BeanSerializer extends XMLDataTypes {
  
  /* PUBLIC METHODS */
  
  // Serialize an array of beans, with the given root element name, 
  // using the specified namespace prefix & uri
  public static DOMDocument serializeBeans(Object[] beans, String nsPrefix, String nsURI,
                                           String name, String[] ignoredFields) throws IntrospectionException,
                                                                                       IllegalAccessException {
    HashMap hashedIgnoredFields = new HashMap();
    if (ignoredFields != null) {
      for (int i = 0; i < ignoredFields.length; i++)
        hashedIgnoredFields.put(ignoredFields[i], ignoredFields[i]);
    }
    DOMDocument domDocument = createDomDocument(name, nsPrefix, nsURI);
    Document document = domDocument.getDocument();
    Element root = document.getDocumentElement();
    for (int i = 0; i < beans.length; i++) {
      // Parse the bean elements
      getBeanElements(document, root, null, nsPrefix, beans[i].getClass().getName(), beans[i], hashedIgnoredFields);
    }
    return domDocument;
  }
  
  // Serialize an array of beans, with the given root element name, 
  // using the default XML namespace prefix & uri
  public static DOMDocument serializeBeans(Object[] beans, String name, 
                                           String[] ignoredFields) throws IntrospectionException,
                                                                          IllegalAccessException {
    return serializeBeans(beans, DEFAULT_NS_PREFIX, DEFAULT_NS_URI, name, ignoredFields);
  }
  
  // Serialize the bean using the specified namespace prefix & uri
  public static DOMDocument serializeBean(Object bean, String nsPrefix, String nsURI, 
                                          String[] ignoredFields) throws IntrospectionException,
                                                                         IllegalAccessException {
    HashMap hashedIgnoredFields = new HashMap();
    if (ignoredFields != null) {
      for (int i = 0; i < ignoredFields.length; i++)
        hashedIgnoredFields.put(ignoredFields[i], ignoredFields[i]);
    } 
    // Use the class name as the name of the root element
    String className = bean.getClass().getName();
    // Remove a trailing semi-colon (;) if present (i.e. if the bean is an array)
    className = StringUtils.deleteTrailingChar(className, ';');
    StringBuffer objectName = new StringBuffer(className);
    objectName.delete(0, objectName.lastIndexOf(".") + 1);
    DOMDocument domDocument = createDomDocument(objectName.toString(), nsPrefix, nsURI);
    Document document = domDocument.getDocument();
    Element root = document.getDocumentElement();
    // Parse the bean elements
    getBeanElements(document, root, null, nsPrefix, className, bean, hashedIgnoredFields);
    return domDocument;
  }
  
  public static DOMDocument serializeBean(Object bean, String nsPrefix, 
                                          String nsURI) throws IntrospectionException,
                                                               IllegalAccessException {
    return serializeBean(bean, nsPrefix, nsURI, new String[] {});  
  }
  
  // Serialize the bean using the default namespace prefix & uri
  public static DOMDocument serializeBean(Object bean, String[] ignoredFields) throws IntrospectionException,
                                                                                      IllegalAccessException {
    return serializeBean(bean, DEFAULT_NS_PREFIX, DEFAULT_NS_URI, ignoredFields);
  }
  
  public static DOMDocument serializeBean(Object bean) throws IntrospectionException,
                                                              IllegalAccessException {
    return serializeBean(bean, null);
  }
  
  /* PROTECTED METHODS */
  protected static void getBeanElements(Document document, Element parentElement, String objectName,  
                                        String nsPrefix, String objectType, Object bean,
                                        HashMap hashedIgnoredFields) throws IntrospectionException, 
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
      parentElement.appendChild(element);
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
          if (b != null)
            getBeanElements(document, element, null, arrayInfo[0], b.getClass().getName(), b, hashedIgnoredFields);
          else {
            // Array element is null, so don't include it and decrement the # elements in the array
            int index = arrayType.indexOf("[");
            arrayType.replace(index + 1, index + 2, String.valueOf(--arrayLen));
          }
          element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
              Constants.ATTR_TYPE, NamespaceConstants.NSPREFIX_SOAP_ENCODING + ":Array");
          element.setAttributeNS(NamespaceConstants.NSURI_SOAP_ENCODING, NamespaceConstants.NSPREFIX_SOAP_ENCODING + ":" + 
              Constants.ATTR_ARRAY_TYPE, arrayInfo[0] + ":" + arrayType.toString());                
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
          element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                                 Constants.ATTR_TYPE, prefix + ":" + beanName);
          if (bean == null)
            element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":null", "true");
          parentElement.appendChild(element);
          if ((beanType >= 1) && (beanType <= 3)) {
            AbstractCollection collection = (AbstractCollection)bean; 
            // Get the bean objects from the vector and serialize each
            Iterator it = collection.iterator();
            while(it.hasNext()) {
              Object b = it.next();
              String beanClassName = null;
              if (b != null)
                beanClassName = b.getClass().getName();
              getBeanElements(document, element, "item", nsPrefix, beanClassName, b, hashedIgnoredFields);
              
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
              getBeanElements(document, itemElement, "key", nsPrefix, keyClassName, key, hashedIgnoredFields);
              getBeanElements(document, itemElement, "value", nsPrefix, beanClassName, value, hashedIgnoredFields);
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
              getBeanElements(document, itemElement, "key", nsPrefix, keyClassName, key, hashedIgnoredFields);
              getBeanElements(document, itemElement, "value", nsPrefix, beanClassName, value, hashedIgnoredFields);
            }               
          }
        } else {
          // Create a parent element for this bean's properties
          if (objectName.charAt(objectName.length() - 1) == ';')
            objectName = new StringBuffer(objectName).deleteCharAt(objectName.length() - 1).toString();
          element = document.createElement(objectName);
          parentElement.appendChild(element);
          StringBuffer className = new StringBuffer(objectType);
          element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                                 Constants.ATTR_TYPE, nsPrefix + ":" + className.delete(0, className.lastIndexOf(".") + 1).toString());
          if (classOfBean != null) {
            // Get an array of property descriptors
            BeanInfo bi = Introspector.getBeanInfo(classOfBean);
            PropertyDescriptor[] pds = bi.getPropertyDescriptors();
            // For each property of the bean, get a SOAPBodyElement that
            // represents the individual property. Append that SOAPBodyElement
            // to the class name element of the SOAP body.
            for (int i = 0; i < pds.length; i++) {
              PropertyDescriptor pd = pds[i];
              getBeanElementProperties(document, element, nsPrefix, bean, pd, hashedIgnoredFields);
            }
          } else
            element.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":null", "true");
        }
      }
    }
  }

  protected static void getBeanElementProperties(Document document, Element element, String nsPrefix, Object bean,  
                                                 PropertyDescriptor pd, HashMap hashedIgnoredFields) throws IntrospectionException,
                                                                                                          IllegalAccessException {
    Element propertyElement = null;
    Class classOfProperty = pd.getPropertyType();
    Object[] argsNone = {};
    // If the property is "class" and the type is java.lang.Class.class then
    // this is the class of the bean, which we've already encoded.
    // In this special case, return null.
    if (!pd.getName().equals("class") && !classOfProperty.equals(java.lang.Class.class)) {
      // Don't process property if it is in the list of fields to be ignored
      if (hashedIgnoredFields.get(pd.getName()) == null) {
        // Set the first letter of the property name to upper-case
        StringBuffer propertyName = new StringBuffer(pd.getName());
        char c = propertyName.charAt(0);
        if (c >= 'a' && c <= 'z') {
          c += 'A' - 'a';
          propertyName.setCharAt(0, c);
        }
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
        } catch (InvocationTargetException ex) {
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
              Element childElement = getStandardObjectElement(document, object, propertyName.toString());
              element.appendChild(childElement);
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
              getBeanElements(document, element, propertyName.toString(), nsPrefix, 
                              propertyTypeName, propertyValue, hashedIgnoredFields);
            } catch (Exception e) {
            }
          }
        }
      }
    }
  }  
  
  /* PRIVATE METHODS */
  private static DOMDocument createDomDocument(String rootName, String nsPrefix, String nsURI) {
    DOMDocument domDocument = new DOMDocument(nsURI, nsPrefix + ":" + rootName, null);
    Document document = domDocument.getDocument();
    Element root = document.getDocumentElement();
    root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" +nsPrefix, nsURI);
    root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" + 
                        NamespaceConstants.NSPREFIX_SCHEMA_XSD, NamespaceConstants.NSURI_SCHEMA_XSD);    
    root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" + 
                        NamespaceConstants.NSPREFIX_SCHEMA_XSI, NamespaceConstants.NSURI_SCHEMA_XSI);
    root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" + 
                        NamespaceConstants.NSPREFIX_SOAP_ENCODING, NamespaceConstants.NSURI_SOAP_ENCODING);
    root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE + ":" + 
                        Constants.NS_PREFIX_XMLSOAP, Constants.NS_URI_XMLSOAP);
    return domDocument;
  }
  
}