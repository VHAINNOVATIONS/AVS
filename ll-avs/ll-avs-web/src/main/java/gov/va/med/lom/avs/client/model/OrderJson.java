package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class OrderJson implements Serializable {

    private String type;
    private String date;
    private String text;
    public String getType() {
      return type;
    }
    public void setType(String type) {
      this.type = type;
    }
    public String getDate() {
      return date;
    }
    public void setDate(String date) {
      this.date = date;
    }
    public String getText() {
      return text;
    }
    public void setText(String text) {
      this.text = text;
    }
    
}
