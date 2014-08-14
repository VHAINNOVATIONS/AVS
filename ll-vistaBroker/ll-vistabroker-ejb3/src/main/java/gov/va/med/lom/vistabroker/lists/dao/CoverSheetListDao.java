package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.CoverSheetRpc;

import java.util.ArrayList;
import java.util.List;

public class CoverSheetListDao extends BaseDao {
	
  // CONSTRUCTORS
  public CoverSheetListDao() {
    super();
  }
  
  public CoverSheetListDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public synchronized List<CoverSheetRpc> getCoverSheetList() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWCV1 COVERSHEET LIST");
    List<String> list = lCall();
    List<CoverSheetRpc> coverSheetRpcs = new ArrayList<CoverSheetRpc>();
    for(String s : list) {
      CoverSheetRpc coverSheetRpc = new CoverSheetRpc();
      coverSheetRpc.setRpc(StringUtils.piece(s, 6));
      coverSheetRpc.setDatePiece(StringUtils.piece(s, 11));
      coverSheetRpc.setDateFormat(StringUtils.piece(s, 10));
      coverSheetRpc.setTextColor(StringUtils.piece(s, 9));
      coverSheetRpc.setStatus("Searching for " + StringUtils.piece(s, 2) + "...");
      coverSheetRpc.setParam1(StringUtils.piece(s, 12));
      coverSheetRpc.setId(StringUtils.piece(s, 1));
      coverSheetRpc.setQualifier(StringUtils.piece(s, 13));
      coverSheetRpc.setTabPos(StringUtils.piece(s, 14));
      coverSheetRpc.setPiece(StringUtils.piece(s, 15));
      coverSheetRpc.setDetail(StringUtils.piece(s, 16));
      coverSheetRpc.setIfn(StringUtils.piece(s, 17));
      coverSheetRpc.setMixedCase(StringUtils.strToBool(StringUtils.piece(s, 7), "1"));
      coverSheetRpc.setInvert(StringUtils.strToBool(StringUtils.piece(s, 8), "1"));
      coverSheetRpcs.add(coverSheetRpc);
    }
    return coverSheetRpcs;
  }
  
}
