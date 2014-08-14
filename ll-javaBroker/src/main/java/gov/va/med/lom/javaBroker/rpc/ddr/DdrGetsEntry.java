package gov.va.med.lom.javaBroker.rpc.ddr;

import java.util.List;


import gov.va.med.lom.javaBroker.rpc.Params;
import gov.va.med.lom.javaBroker.rpc.Mult;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class DdrGetsEntry extends DdrQuery {

  private String file;
  private String iens;
  private String fields;
  private String flags;
  
  public DdrGetsEntry(RpcBroker rpcBroker) {
    super(rpcBroker);
  }
  
  public List<String> execute() throws Exception {
    if ((file == null) || (file.equals(""))) {
      throw new Exception("Must have a file");
    }
    if ((iens == null) || (iens.equals(""))) {
      throw new Exception("Must have IENS");
    }
    if ((fields == null) || (fields.equals(""))) {
      throw new Exception("Must have a field");
    }  
    Params params = new Params();
    Mult mult = new Mult();
    mult.setMultiple("\"FILE\"", file);
    mult.setMultiple("\"IENS\"", iens);
    mult.setMultiple("\"FIELDS\"", fields);
    if ((flags != null) && (!flags.equals(""))) {
      mult.setMultiple("\"FLAGS\"", flags);
    }
    params.addMult(mult); 
    String response = super.execute("DDR GETS ENTRY DATA", params);
    return StringUtils.getStringList(response);
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
  
  public String getFields() {
    return fields;
  }
  
  public void setFields(String fields) {
    this.fields = fields;
  }
  
  public String getFlags() {
    return flags;
  }
  
  public void setFlags(String flags) {
    this.flags = flags;
  }
  
}
