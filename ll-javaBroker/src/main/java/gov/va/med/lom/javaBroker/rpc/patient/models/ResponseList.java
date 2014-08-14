package gov.va.med.lom.javaBroker.rpc.patient.models;

public class ResponseList {
  
  private String[] names;
  private String[] values;
  
  public ResponseList() {
    names = null;
    values = null;
  }
  
  public String[] getNames() {
    return names;
  }
  
  public void setNames(String[] names) {
    this.names = names;
  }
  
  public String[] getValues() {
    return values;
  }
  
  public void setValues(String[] values) {
    this.values = values;
  }
}
