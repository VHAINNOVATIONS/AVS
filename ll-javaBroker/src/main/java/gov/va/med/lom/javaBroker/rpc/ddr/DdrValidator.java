package gov.va.med.lom.javaBroker.rpc.ddr;

import java.util.List;


import gov.va.med.lom.javaBroker.rpc.Params;
import gov.va.med.lom.javaBroker.rpc.Mult;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class DdrValidator {

  private RpcBroker rpcBroker;
  private String file;
  private String iens;
  private String field;
  private String value;
  
  public DdrValidator(RpcBroker rpcBroker) {
    this.rpcBroker = rpcBroker;
  }
  
  public String execute() throws Exception {
    if ((file == null) || (file.equals(""))) {
      throw new Exception("Must have a file");
    }
    if ((iens == null) || (iens.equals(""))) {
      throw new Exception("Must have IENS");
    }
    if ((field == null) || (field.equals(""))) {
      throw new Exception("Must have a field");
    }  
    if ((value == null) || (value.equals(""))) {
      throw new Exception("Must have a value");
    }    
    
    Params params = new Params();
    Mult mult = new Mult();
    mult.setMultiple("\"FILE\"", file);
    mult.setMultiple("\"IENS\"", iens);
    mult.setMultiple("\"FIELD\"", field);
    mult.setMultiple("\"VALUE\"", value);
    params.addMult(mult);
    
    String currentContext = rpcBroker.getCurrentContext();
    if (!currentContext.equals(DdrQuery.CAPRI_CONTEXT)) {
      if (!rpcBroker.createContext(DdrQuery.CAPRI_CONTEXT)) {
        throw new Exception("Unable to set CAPRI context.");
      }
    }
    String response = rpcBroker.call("DDR VALIDATOR", params);
    if (!currentContext.equals(DdrQuery.CAPRI_CONTEXT)) {
      if (!rpcBroker.createContext(currentContext)) {
        throw new Exception("Unable to set " + currentContext + " context.");
      }
    }    
    List<String> list = StringUtils.getStringList(response);
    for (String str : list) {
      if (str.startsWith("[BEGIN_diERRORS]")) {
        String theValue = null;
        for (String s : list) {
          if (s.startsWith("The value")) {
            theValue = s;
            break;
          }
        }
        if (theValue == null) {
          throw new Exception("Unexpected return value from VistA: " + response);
        }
        return theValue;
      }
    }
    return "OK";
  }

  // Getter/Setter Methods
  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getIens() {
    return iens;
  }

  public void setIens(String iens) {
    this.iens = iens;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
  
}
