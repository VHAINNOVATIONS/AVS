package gov.va.med.lom.javaUtils.html;

import java.lang.String;
import java.lang.StringBuffer;

/*
 * This class contains a String and adds a toHtml() method. 
 */
public class HtmlString implements java.io.Serializable {
  private String value;

  public HtmlString(String value) {
    this.value = value;
  }

  public HtmlString(StringBuffer buffer) {
    this.value = buffer.toString();
  }

  public String toString() {
    return this.value;
  }

  public String toHtml() {
    return this.value;
  }
}
