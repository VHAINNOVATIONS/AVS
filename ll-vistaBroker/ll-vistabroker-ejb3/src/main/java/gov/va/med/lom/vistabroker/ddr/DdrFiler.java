package gov.va.med.lom.vistabroker.ddr;

import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.security.ISecurityContext;

import java.util.List;
import java.util.ArrayList;

/*
 * Generic call to file edits into FM file.
 */

public class DdrFiler extends DdrQuery {

  private String operation;
  private String[] args;
  
  // CONSTRUCTORS
  public DdrFiler(ISecurityContext securityContext) {
    super(securityContext);
  }
  
  public DdrFiler(BaseDao baseDao) {
    super(baseDao);
  }  
  
  // RPC API  
  public String execute() throws Exception {
    if ((operation == null) || (operation.length() == 0)) {
      throw new Exception("Must have an operation.");
    }
    Object[] params = new Object[2];
    params[0] = operation;
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < args.length; i++) {
      list.add(args[i]);
    }
    params[1] = list;
    return execute("DDR FILER", params);
  }
  
  // Getter/Setter methods
  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public String[] getArgs() {
    return args;
  }

  public void setArgs(String[] args) {
    this.args = args;
  }
  
}
