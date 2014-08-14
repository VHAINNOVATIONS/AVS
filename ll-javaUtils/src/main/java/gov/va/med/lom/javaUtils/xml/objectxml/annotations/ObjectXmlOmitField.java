package gov.va.med.lom.javaUtils.xml.objectxml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/*
 * Annotation that declares a field to be ommited. 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @ interface ObjectXmlOmitField {
  
}
