package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class CoverSheetListRpc extends AbstractRpc {
	
  // FIELDS
  private CoverSheetRpcList coverSheetRpcList;
  
  // CONSTRUCTORS
  public CoverSheetListRpc() throws BrokerException {
    super();
  }
  
  public CoverSheetListRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized CoverSheetRpcList getCoverSheetList() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORWCV1 COVERSHEET LIST");
      CoverSheetRpc[] coverSheetRpcs = new CoverSheetRpc[list.size()];
      for(int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        coverSheetRpcs[i] = new CoverSheetRpc();
        coverSheetRpcs[i].setRpc(StringUtils.piece(x, 6));
        coverSheetRpcs[i].setDatePiece(StringUtils.piece(x, 11));
        coverSheetRpcs[i].setDateFormat(StringUtils.piece(x, 10));
        coverSheetRpcs[i].setTextColor(StringUtils.piece(x, 9));
        coverSheetRpcs[i].setStatus("Searching for " + StringUtils.piece(x, 2) + "...");
        coverSheetRpcs[i].setParam1(StringUtils.piece(x, 12));
        coverSheetRpcs[i].setId(StringUtils.piece(x, 1));
        coverSheetRpcs[i].setQualifier(StringUtils.piece(x, 13));
        coverSheetRpcs[i].setTabPos(StringUtils.piece(x, 14));
        coverSheetRpcs[i].setPiece(StringUtils.piece(x, 15));
        coverSheetRpcs[i].setDetail(StringUtils.piece(x, 16));
        coverSheetRpcs[i].setIfn(StringUtils.piece(x, 17));
        coverSheetRpcs[i].setMixedCase(StringUtils.strToBool(StringUtils.piece(x, 7), "1"));
        coverSheetRpcs[i].setInvert(StringUtils.strToBool(StringUtils.piece(x, 8), "1"));
        if (returnRpcResult)
          coverSheetRpcs[i].setRpcResult(x);
      }
      coverSheetRpcList = new CoverSheetRpcList();
      coverSheetRpcList.setCoverSheetRpcs(coverSheetRpcs);
      return coverSheetRpcList;
  } else 
    throw getCreateContextException("OR CPRS GUI CHART");
  }
  
}
