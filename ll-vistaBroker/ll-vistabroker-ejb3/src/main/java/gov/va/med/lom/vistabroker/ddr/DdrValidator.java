package gov.va.med.lom.vistabroker.ddr;

import java.util.HashMap;
import java.util.List;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.security.ISecurityContext;

/*
 * This function allows the application to validate user input to
 * a field before filing data. The call uses the database server VAL^DIE
 * call.
 */

public class DdrValidator extends BaseDao {

  private String file;
  private String iens;
  private String field;
  private String value;
  
  
  // CONSTRUCTORS
  public DdrValidator(ISecurityContext securityContext) {
    super();
    setSecurityContext(securityContext);
  }
  
  public DdrValidator(BaseDao baseDao) {
    super(baseDao);
  }  
    
  // RPC API  
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
    
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("FILE", file);
    map.put("IENS", iens);
    map.put("FIELD", field);
    map.put("VALUE", value);
    Object[] params = {map};
    
    if ((getDefaultContext() == null) || !getDefaultContext().equals(DdrQuery.CAPRI_CONTEXT)) {
      setDefaultContext(DdrQuery.CAPRI_CONTEXT);
    }
    setDefaultRpcName("DDR VALIDATOR");
    String response = sCall(params);
    List<String> list = StringUtils.getStringList(response);
    for (String str : list) {
      if (str.startsWith("[ERRORS]")) {
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
