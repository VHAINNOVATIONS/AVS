package gov.va.med.lom.javaBroker.rpc;

import java.util.Vector;
import java.util.List;

public class Params implements IParamType {
  
  // Private fields
  private Vector parameters;
  
  // Constructors
  public Params() {
      parameters = new Vector();
  }
  
  public Params(Params params) {
      this();
      for (int i=0; i < params.getCount(); i++) {
          ParamRecord paramRec = new ParamRecord();
          paramRec.setMult(params.getParameter(i).getMult());
          paramRec.setType(params.getParameter(i).getType());
          paramRec.setValue(params.getParameter(i).getValue());
          parameters.add(paramRec);
      }
  }
  
  // Public mutator methods
  public int getCount() {
      return parameters.size();
  }

  public ParamRecord getParameter(int index) {
      if (index >= parameters.size())
          while(parameters.size() <= index)
              parameters.add(null);
      if (parameters.get(index) == null) {
          // point it to new memory block
          parameters.set(index, new ParamRecord());
      }
      return (ParamRecord)parameters.get(index);
  }

  public ParamRecord setParameter(int index, ParamRecord param) {
      if (index >= parameters.size())
          while(parameters.size() <= index)
              parameters.add(null);      
      parameters.set(index, param);
      return (ParamRecord)parameters.get(index);
  }
  
  public ParamRecord addLiteral(String value) {      
      parameters.add(new ParamRecord(value, LITERAL));
      return (ParamRecord)parameters.get(parameters.size()-1);
  } 
  
  public ParamRecord addReference(String value) {      
    parameters.add(new ParamRecord(value, REFERENCE));
    return (ParamRecord)parameters.get(parameters.size()-1);
}  

  public ParamRecord addParameter(Object value, int type) {  
      parameters.add(new ParamRecord((String)value, type));
      return (ParamRecord)parameters.get(parameters.size()-1);
  }   
  
  public ParamRecord addList(List list) {   
      parameters.add(new ParamRecord(list));
      return (ParamRecord)parameters.get(parameters.size()-1);
  }  
  
  public ParamRecord addMult(Mult mult) {   
    parameters.add(new ParamRecord(mult));
    return (ParamRecord)parameters.get(parameters.size()-1);
  }     
  
  // Public methods
  public void clear() {
    parameters.clear();
  }  
}
