package gov.va.med.lom.javaBroker.rpc.ddr;

import java.util.List;
import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.Params;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

public class DdrFiler extends DdrQuery {

  private String operation;
  private String[] args;
  
  public DdrFiler(RpcBroker rpcBroker) {
    super(rpcBroker);
  }

  public String execute() throws Exception {
    if ((operation == null) || (operation.length() == 0)) {
      throw new Exception("Must have an operation.");
    }
    Params params = new Params();
    params.addLiteral(operation);
    List list = new ArrayList();
    for (int i = 0; i < args.length; i++) {
      list.add(args[i]);
    }
    params.addList(list);
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
