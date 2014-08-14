package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.TemplateType;

import java.util.ArrayList;
import java.util.List;

public class TemplateDao extends BaseDao {
  
  // CONSTRUCTORS
  public TemplateDao() {
    super();
  }
  
  public TemplateDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API 
  public List<TemplateType> getTemplateRoots(String userDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU TEMPLATE GETROOTS");
    
    List<String> list = lCall(userDuz);
    List<TemplateType> templateTypeList = new ArrayList<TemplateType>();
    for (String s : list) {
      TemplateType templateType = new TemplateType();
      templateType.setIen(StringUtils.piece(s, 1));
      templateType.setCode(StringUtils.piece(s, 2));
      templateType.setTitle(StringUtils.piece(s, 3));
      templateTypeList.add(templateType);
    }
    return templateTypeList;
  }
  
  public int getTemplateAccess(String id, String userDuz, String encounterLocation) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU TEMPLATE ACCESS LEVEL");
    Object[] params = {id, userDuz, encounterLocation};
    return Integer.valueOf(sCall(params)).intValue();
  }
  
}
