package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

public class OrderField implements Serializable {

  private String iValue;
  private String eValue;
  
  public OrderField() {
    this.iValue = "";
    this.eValue = "";
  }
  
  public OrderField (String iValue, String eValue) {
    this.iValue = iValue;
    this.eValue = eValue;
  }

  public String getiValue() {
    return iValue;
  }

  public void setiValue(String iValue) {
    this.iValue = iValue;
  }

  public String geteValue() {
    return eValue;
  }

  public void seteValue(String eValue) {
    this.eValue = eValue;
  }

}
