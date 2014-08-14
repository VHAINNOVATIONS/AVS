package gov.va.med.lom.vistabroker.util;

import gov.va.med.lom.javaUtils.xml.BeanSerializer;
import gov.va.med.lom.javaUtils.xml.DOMDocument;

public class BeanUtils {
	
  // VistA Namespace Prefix/URI
  public static final String VISTA_NS_PREFIX = "vista";                
  public static final String VISTA_NS_URI = "gov.va.med.vista";
  
  /*
   * BEAN SERIALIZATION
   */
  public static DOMDocument serializeBeans(Object[] beans, String nsPrefix, String nsURI, String root) throws Exception {
    DOMDocument domDoc = BeanSerializer.serializeBeans(beans, nsPrefix, nsURI, root, null);
    return domDoc;
  }
  
  public static DOMDocument serializeBeans(Object[] beans, String root) throws Exception {
    return serializeBeans(beans, VISTA_NS_PREFIX, VISTA_NS_URI, root);
  }
  
  public static DOMDocument serializeBean(Object bean, String nsPrefix, String nsURI) throws Exception {
    DOMDocument domDoc = BeanSerializer.serializeBean(bean, nsPrefix, nsURI, null);
    return domDoc;
  }
  
  public static DOMDocument serializeBean(Object bean) throws Exception {
    return serializeBean(bean, VISTA_NS_PREFIX, VISTA_NS_URI);
  }  
  
  public String toXML(Object[] beans, String nsPrefix, String nsURI,
                      String root, boolean includeXmlProlog) throws Exception {
    DOMDocument domDoc = serializeBeans(beans, nsPrefix, nsURI, root);
    return domDoc.serialize(includeXmlProlog);
  }
  
  public String toXML(Object[] beans, String root, boolean includeXmlProlog) throws Exception {
    return toXML(beans, VISTA_NS_PREFIX, VISTA_NS_URI, root, includeXmlProlog);
  }
  
  public String toXML(Object bean, String nsPrefix, String nsURI,
                      boolean includeXmlProlog) throws Exception {
    DOMDocument domDoc = serializeBean(bean, nsPrefix, nsURI);
    return domDoc.serialize("\n", "  ", includeXmlProlog);
  }  
  
  public String toXML(Object bean, boolean includeXmlProlog) throws Exception {
    return toXML(bean, VISTA_NS_PREFIX, VISTA_NS_URI, includeXmlProlog);
  }  


}
