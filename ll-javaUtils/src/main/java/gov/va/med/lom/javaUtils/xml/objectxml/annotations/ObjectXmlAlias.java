package gov.va.med.lom.javaUtils.xml.objectxml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Annotation used to define a class or field value.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface ObjectXmlAlias {

  // The value of the class or field value
  public String value();
  public Class<?> impl() default Void.class;
  
}
