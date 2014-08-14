package gov.va.med.lom.javaUtils.xml;

import gov.va.med.lom.javaUtils.misc.StringUtils;

import java.util.HashMap;
import java.text.SimpleDateFormat;

import javax.xml.rpc.NamespaceConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLDataTypes {
  
  // Named Constantss
  public static final String DEFAULT_NS_PREFIX = "lom";
  public static final String DEFAULT_NS_URI = "gov.va.med.lom.xml";

  // Static Members
  public static HashMap XSD_SOAP_TYPES_MAP = new HashMap();
  public static HashMap STANDARD_TYPES_MAP = new HashMap();
  public static HashMap XSD_SOAP_ARRAY_TYPES_MAP = new HashMap();
  public static HashMap PRIMITIVE_TYPES_MAP = new HashMap();
  
  /*
   * Static Initializer
   */
  static {
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_STRING.getLocalPart(), String.class);
    XSD_SOAP_TYPES_MAP.put("java.lang.String", String.class);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_BOOLEAN.getLocalPart(), Boolean.TYPE); 
    XSD_SOAP_TYPES_MAP.put("java.lang.Boolean", Boolean.TYPE);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_DECIMAL.getLocalPart(), java.math.BigDecimal.class); 
    XSD_SOAP_TYPES_MAP.put("java.math.BigDecimal", java.math.BigDecimal.class);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_FLOAT.getLocalPart(), Float.TYPE);
    XSD_SOAP_TYPES_MAP.put("java.lang.Float", Float.TYPE);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_DOUBLE.getLocalPart(), Double.TYPE);    
    XSD_SOAP_TYPES_MAP.put("java.lang.Double", Double.TYPE);    
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_INTEGER.getLocalPart(), java.math.BigInteger.class);
    XSD_SOAP_TYPES_MAP.put("java.math.BigInteger", java.math.BigInteger.class);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_INT.getLocalPart(), Integer.TYPE);
    XSD_SOAP_TYPES_MAP.put("java.lang.Integer", Integer.TYPE);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_LONG.getLocalPart(), Long.TYPE);  
    XSD_SOAP_TYPES_MAP.put("java.lang.Long", Long.TYPE);  
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_SHORT.getLocalPart(), Short.TYPE);
    XSD_SOAP_TYPES_MAP.put("java.lang.Short", Short.TYPE);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_BYTE.getLocalPart(), Byte.TYPE); 
    XSD_SOAP_TYPES_MAP.put("java.lang.Byte", Byte.TYPE);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_BASE64.getLocalPart(), byte[].class); 
    XSD_SOAP_TYPES_MAP.put("byte[]", byte[].class); 
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_DATE.getLocalPart(), java.util.Date.class); 
    XSD_SOAP_TYPES_MAP.put("java.util.Date", java.util.Date.class); 
    XSD_SOAP_TYPES_MAP.put("java.sql.Date", java.sql.Date.class);  
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_TIME.getLocalPart(), java.sql.Time.class); 
    XSD_SOAP_TYPES_MAP.put("java.sql.Time", java.sql.Time.class);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.XSD_DATETIME.getLocalPart(), java.util.Calendar.class);
    XSD_SOAP_TYPES_MAP.put("java.util.Calendar", java.util.Calendar.class);
    XSD_SOAP_TYPES_MAP.put("java.util.GregorianCalendar", java.util.Calendar.class);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.SOAP_MAP.getLocalPart(), java.util.HashMap.class);
    XSD_SOAP_TYPES_MAP.put("java.util.HashMap", java.util.HashMap.class);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.SOAP_VECTOR.getLocalPart(), java.util.Vector.class);
    XSD_SOAP_TYPES_MAP.put("java.util.Vector", java.util.Vector.class);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.SOAP_ELEMENT.getLocalPart(), org.w3c.dom.Element.class);
    XSD_SOAP_TYPES_MAP.put("org.w3c.dom.Element", org.w3c.dom.Element.class);
    XSD_SOAP_TYPES_MAP.put(org.apache.axis.Constants.SOAP_ARRAY.getLocalPart(), java.lang.reflect.Array.class);
    XSD_SOAP_TYPES_MAP.put("char", Character.TYPE);
    XSD_SOAP_TYPES_MAP.put("ArrayList", java.util.ArrayList.class);
    XSD_SOAP_TYPES_MAP.put("LinkedList", java.util.LinkedList.class);
    XSD_SOAP_TYPES_MAP.put("Hashtable", java.util.Hashtable.class);
    XSD_SOAP_TYPES_MAP.put("Properties", java.util.Properties.class);
    
    
    STANDARD_TYPES_MAP.put(String.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                       org.apache.axis.Constants.XSD_STRING.getLocalPart()});
    STANDARD_TYPES_MAP.put(Character.TYPE, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                         org.apache.axis.Constants.XSD_STRING.getLocalPart()});
    STANDARD_TYPES_MAP.put(Character.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                          org.apache.axis.Constants.XSD_STRING.getLocalPart()});    
    STANDARD_TYPES_MAP.put(Boolean.TYPE, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                       org.apache.axis.Constants.XSD_BOOLEAN.getLocalPart()});   
    STANDARD_TYPES_MAP.put(Boolean.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                        org.apache.axis.Constants.XSD_BOOLEAN.getLocalPart()});   
    STANDARD_TYPES_MAP.put(java.math.BigDecimal.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                                     org.apache.axis.Constants.XSD_DECIMAL.getLocalPart()});  
    STANDARD_TYPES_MAP.put(Float.TYPE, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                     org.apache.axis.Constants.XSD_FLOAT.getLocalPart()});  
    STANDARD_TYPES_MAP.put(Float.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                      org.apache.axis.Constants.XSD_FLOAT.getLocalPart()});
    STANDARD_TYPES_MAP.put(Double.TYPE, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                      org.apache.axis.Constants.XSD_DOUBLE.getLocalPart()}); 
    STANDARD_TYPES_MAP.put(Double.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                       org.apache.axis.Constants.XSD_DOUBLE.getLocalPart()});
    STANDARD_TYPES_MAP.put(java.math.BigInteger.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                                     org.apache.axis.Constants.XSD_INTEGER.getLocalPart()});  
    STANDARD_TYPES_MAP.put(Integer.TYPE, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                       org.apache.axis.Constants.XSD_INT.getLocalPart()});  
    STANDARD_TYPES_MAP.put(Integer.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                        org.apache.axis.Constants.XSD_INT.getLocalPart()}); 
    STANDARD_TYPES_MAP.put(Long.TYPE, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                    org.apache.axis.Constants.XSD_LONG.getLocalPart()});  
    STANDARD_TYPES_MAP.put(Long.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                     org.apache.axis.Constants.XSD_LONG.getLocalPart()});
    STANDARD_TYPES_MAP.put(Short.TYPE, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                     org.apache.axis.Constants.XSD_SHORT.getLocalPart()}); 
    STANDARD_TYPES_MAP.put(Short.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                      org.apache.axis.Constants.XSD_SHORT.getLocalPart()});
    STANDARD_TYPES_MAP.put(Byte.TYPE, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                    org.apache.axis.Constants.XSD_BYTE.getLocalPart()});  
    STANDARD_TYPES_MAP.put(Byte.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                     org.apache.axis.Constants.XSD_BYTE.getLocalPart()});
    STANDARD_TYPES_MAP.put(byte[].class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                       org.apache.axis.Constants.XSD_BASE64.getLocalPart()});    
    STANDARD_TYPES_MAP.put(java.util.Date.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                               org.apache.axis.Constants.XSD_DATE.getLocalPart()});  
    STANDARD_TYPES_MAP.put(java.sql.Date.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                              org.apache.axis.Constants.XSD_DATE.getLocalPart()});      
    STANDARD_TYPES_MAP.put(java.sql.Time.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                              org.apache.axis.Constants.XSD_TIME.getLocalPart()});      
    STANDARD_TYPES_MAP.put(java.util.Calendar.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                                   org.apache.axis.Constants.XSD_DATETIME.getLocalPart()});
    STANDARD_TYPES_MAP.put(java.util.GregorianCalendar.class, new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, 
                                                                            org.apache.axis.Constants.XSD_DATETIME.getLocalPart()});
    STANDARD_TYPES_MAP.put(java.util.HashMap.class, new String[] {org.apache.axis.Constants.NS_PREFIX_XMLSOAP, 
                                                                  org.apache.axis.Constants.SOAP_MAP.getLocalPart()});
    STANDARD_TYPES_MAP.put(java.util.Vector.class, new String[] {NamespaceConstants.NSPREFIX_SOAP_ENCODING, 
                                                                 org.apache.axis.Constants.SOAP_VECTOR.getLocalPart()});
    STANDARD_TYPES_MAP.put(org.w3c.dom.Element.class, new String[] {NamespaceConstants.NSPREFIX_SOAP_ENCODING, 
                                                                    org.apache.axis.Constants.SOAP_ELEMENT.getLocalPart()});
    STANDARD_TYPES_MAP.put(java.lang.reflect.Array.class, new String[] {NamespaceConstants.NSPREFIX_SOAP_ENCODING, 
                                                                        org.apache.axis.Constants.SOAP_ARRAY.getLocalPart()});
    STANDARD_TYPES_MAP.put(java.util.ArrayList.class, new String[] {DEFAULT_NS_PREFIX, "ArrayList"});
    STANDARD_TYPES_MAP.put(java.util.LinkedList.class, new String[] {DEFAULT_NS_PREFIX, "LinkedList"});
    STANDARD_TYPES_MAP.put(java.util.Hashtable.class, new String[] {DEFAULT_NS_PREFIX, "Hashtable"});
    STANDARD_TYPES_MAP.put(java.util.Properties.class, new String[] {DEFAULT_NS_PREFIX, "Properties"});
    
    
    XSD_SOAP_ARRAY_TYPES_MAP.put("boolean[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "boolean[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.lang.Boolean[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "boolean[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("byte[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "byte[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.lang.Byte[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "byte[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("char[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "char[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.lang.Char[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "char[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("short[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "short[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.lang.Short[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "short[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("int[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "int[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.lang.Integer[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "int[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("float[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "float[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.lang.Float[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "float[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("long[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "long[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.lang.Long[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "long[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("double[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "double[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.lang.Double[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "double[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.lang.String[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "string[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("void[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "void[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.util.Calendar[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "dateTime[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.util.GregorianCalendar[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "dateTime[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.util.Date[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "date[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.sql.Date[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "date[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.sql.Time[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "time[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.math.BigDecimal[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "decimal[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.math.BigInteger[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "integer[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("javax.xml.namespace.QName[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "QName[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.util.Vector[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "Vector[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.util.HashMap[]", new String[] {org.apache.axis.Constants.NS_PREFIX_XMLSOAP, "Map[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("org.w3c.dom.Element[]", new String[] {NamespaceConstants.NSPREFIX_SCHEMA_XSD, "Element[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.util.ArrayList[]", new String[] {DEFAULT_NS_PREFIX, "ArrayList[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.util.LinkedList[]", new String[] {DEFAULT_NS_PREFIX, "LinkedList[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.util.Hashtable[]", new String[] {DEFAULT_NS_PREFIX, "Hashtable[]"});
    XSD_SOAP_ARRAY_TYPES_MAP.put("java.util.Properties[]", new String[] {DEFAULT_NS_PREFIX, "Properties[]"});
    
    PRIMITIVE_TYPES_MAP.put("java.lang.String", String.class);
    PRIMITIVE_TYPES_MAP.put("char", Character.class);
    PRIMITIVE_TYPES_MAP.put("boolean", Boolean.class);   
    PRIMITIVE_TYPES_MAP.put("float", Float.class);  
    PRIMITIVE_TYPES_MAP.put("double", Double.class); 
    PRIMITIVE_TYPES_MAP.put("int", Integer.class);  
    PRIMITIVE_TYPES_MAP.put("long", Long.class);  
    PRIMITIVE_TYPES_MAP.put("short", Short.class); 
    PRIMITIVE_TYPES_MAP.put("byte", Byte.class);  
  }
  
  
  // Map from a standard XSD type to a Class
  public static Class getXsdTypeClass(String xsdType) {
    Class cls = (Class)XSD_SOAP_TYPES_MAP.get(xsdType);
    return cls;
  }  
  
  // Map from a primitive type to a Class
  public static Class getPrimitiveTypeClass(String type) {
    Class cls = (Class)PRIMITIVE_TYPES_MAP.get(type);
    return cls;
  }   
  
  // Map from a standard XSD type to a Class
  public static String[] getXsdSoapArrayInfo(String arrayType, String defaultNsPrefix) {
    int colonIndex = arrayType.indexOf(':');
    if (colonIndex > 0)
      arrayType.substring(colonIndex+1);
    String[] results = (String[])XSD_SOAP_ARRAY_TYPES_MAP.get(arrayType);
    if (results != null)
      return results;
    else {
     if (arrayType.indexOf('.') >= 0)
       arrayType = new StringBuffer(arrayType).substring(arrayType.lastIndexOf('.') + 1, arrayType.length());
     return new String[] {defaultNsPrefix, arrayType};
    }
  } 
  
  // If the object is one of the standard types, do mapping from Java type to XSD type
  /*
   * xsd:base64Binary   byte[]
   * xsd:boolean        boolean
   * xsd:byte           byte
   * xsd:dateTime       java.util.Calendar 
   * xsd:date           java.util.Date
   * xsd:date           java.sql.Date
   * xsd:time           java.sql.Time
   * xsd:decimal        java.math.BigDecimal
   * xsd:double         double
   * xsd:float          float
   * xsd:hexBinary      byte[]
   * xsd:int            int
   * xsd:integer        java.math.BigInteger
   * xsd:long           long
   * xsd:QName          javax.xml.namespace.QName
   * xsd:short          short
   * xsd:string         java.lang.String
   */
  public static Element getStandardObjectElement(Document document, Object object, String name) {
    String type = null;
    String value = null;
    if (object instanceof String) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_STRING.getLocalPart(); 
      value = StringUtils.escapeEntities(((String)object).toString());
    } else if (object instanceof Boolean) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_BOOLEAN.getLocalPart();
      value = ((Boolean)object).toString();
    } else if (object instanceof java.math.BigDecimal) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_DECIMAL.getLocalPart(); 
      value = ((java.math.BigDecimal)object).toString(); 
    } else if (object instanceof Float) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_FLOAT.getLocalPart();
      value = ((Float)object).toString();
    } else if (object instanceof Double) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_DOUBLE.getLocalPart();
      value = ((Double)object).toString();
    } else if (object instanceof java.math.BigInteger) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_INTEGER.getLocalPart();
      value = ((java.math.BigInteger)object).toString();
    } else if (object instanceof Integer) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_INT.getLocalPart();
      value = ((Integer)object).toString();
    } else if (object instanceof Long) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_LONG.getLocalPart(); 
      value = ((Long)object).toString();
    } else if (object instanceof Short) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_SHORT.getLocalPart();
      value = ((Short)object).toString();
    } else if (object instanceof Byte) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_BYTE.getLocalPart(); 
      value = ((Byte)object).toString();
    } else if (object instanceof byte[]) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_BASE64.getLocalPart();
      value = gov.va.med.lom.javaUtils.misc.Base64Encoder.toBase64String((byte[])object);
    } else if (object instanceof java.util.Date) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_DATE.getLocalPart();
      SimpleDateFormat ansiFormatter = new SimpleDateFormat("yyyy-MM-dd");
      value = ansiFormatter.format((java.util.Date)object);
    } else if (object instanceof java.sql.Date) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_DATE.getLocalPart();
      SimpleDateFormat ansiFormatter = new SimpleDateFormat("yyyy-MM-dd");
      value = ansiFormatter.format((java.sql.Date)object);   
    } else if (object instanceof java.sql.Time) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_TIME.getLocalPart();
      SimpleDateFormat ansiFormatter = new SimpleDateFormat("hh:mm:ss");
      value = ansiFormatter.format((java.sql.Time)object);  
    } else if (object instanceof java.util.Calendar) {
      type = NamespaceConstants.NSPREFIX_SCHEMA_XSD + ":" + org.apache.axis.Constants.XSD_DATETIME.getLocalPart();
      SimpleDateFormat ansiFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.S");  // "yyyy-MM-dd'T'hh:mm:ss.S'Z'"
      value = ansiFormatter.format(((java.util.Calendar)object).getTime());
    }
    if ((type == null) && (value == null) && (object != null))
      return null;
    else {
      Element childElement = document.createElement(name);
      if ((type == null) || (type.length() == 0)) {
        type = "anyType";
        childElement.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":null", "true");
      }
      childElement.setAttributeNS(NamespaceConstants.NSURI_SCHEMA_XSI, NamespaceConstants.NSPREFIX_SCHEMA_XSI + ":" + 
                                  org.apache.axis.Constants.ATTR_TYPE, type);
      if (value != null) {
        childElement.setTextContent(value);
      }
      return childElement;
    } 
  }  

  public static boolean isStandardType(String type) {
    Class cls = getXsdTypeClass(type);
    return cls != null;
  }  
  
  public static boolean isPrimitiveType(String type) {
    Class cls = getPrimitiveTypeClass(type);
    return cls != null;
  }  
  
  public static String[] getXsdSoapType(Class cls) {
    return (String[])STANDARD_TYPES_MAP.get(cls);
  }
  
  public static String[] getXsdSoapType(String type) {
    Class cls = null;
    try {
      cls = (Class)XSD_SOAP_TYPES_MAP.get(type);
      return (String[])STANDARD_TYPES_MAP.get(cls);
    } catch(Exception e) {
      return null;
    }
  }  
  
  public static void main(String[] args) {
  }
  
}
