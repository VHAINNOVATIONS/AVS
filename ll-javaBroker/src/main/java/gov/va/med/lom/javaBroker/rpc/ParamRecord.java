package gov.va.med.lom.javaBroker.rpc;

import java.util.List;

public class ParamRecord implements IParamType{
	
  // Private fields
  private Mult mult;
  private List list;
  private String value;
  private int type;
  
  // Constructors
  public ParamRecord() {
    setType(UNDEFINED);
  }  
  
  public ParamRecord(String value, int type) {
  	setValue(value);
  	setType(type);
  }
  
  public ParamRecord(Mult mult) {
  	setMult(mult);
  	setType(MULT);
  }
  
  public ParamRecord(List list) {
    setList(list);
    setType(LIST);
  }  
  
  public Mult getMult() {
  	return mult;
  }

  public void setMult(Mult mult) {
  	this.mult = mult;
  }
  
  public List getList() {
    return list;
  }
  
  public void setList(List list) {
    this.list = list;
  }  
  
  public String getValue() {
  	return value;
  }

  public void setValue(String value) {
  	this.value = value;
  }

  public int getType() {
  	return type;
  }

  public void setType(int value) {
  	this.type = value;
  }  
}
