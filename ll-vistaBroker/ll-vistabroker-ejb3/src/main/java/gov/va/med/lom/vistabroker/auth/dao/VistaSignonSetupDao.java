package gov.va.med.lom.vistabroker.auth.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.auth.data.VistaServerInfo;
import gov.va.med.lom.vistabroker.dao.BaseDao;

import java.util.List;

public class VistaSignonSetupDao extends BaseDao {
  
  // CONSTRUCTORS
  public VistaSignonSetupDao() {
    super();
  }
  
  public VistaSignonSetupDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API  
  public VistaServerInfo getVistaServerInfo() throws Exception {
    setDefaultContext("ALSI VISTA BROKER");
    setDefaultRpcName("ALSI SERVER INFO");
    VistaServerInfo vistaServerInfo = new VistaServerInfo(); 
    List<String> list = lCall();
    if (list.size() >= 6) {
      vistaServerInfo.setServerAvailable(true);
      vistaServerInfo.setServer((String)list.get(0));
      vistaServerInfo.setVolume((String)list.get(1));
      vistaServerInfo.setUci((String)list.get(2));
      vistaServerInfo.setPort((String)list.get(3));
      vistaServerInfo.setDeviceLocked(StringUtils.strToBool((String)list.get(4), "1"));
      vistaServerInfo.setSignonRequired(StringUtils.strToBool((String)list.get(5), "0"));
      vistaServerInfo.setIntroMessage(getIntroMessage());
    } else
      vistaServerInfo.setServerAvailable(false);
    return vistaServerInfo;
  }
  
  public String getIntroMessage() throws Exception {
    setDefaultContext("ALSI VISTA BROKER");
    setDefaultRpcName("XUS INTRO MSG");
    return sCall();
  }
  
  public String getAVHelp(boolean mustEnterOldVC) throws Exception {
    setDefaultContext("ALSI VISTA BROKER");
    setDefaultRpcName("ALSI XUS AV HELP");
    String x = sCall();
    StringBuffer sb = new StringBuffer(x);
    sb.insert(0, "Enter a new verify code and then confirm it." + '\n');
    if (mustEnterOldVC)
      sb.insert(0, "Enter your current verify code first." + '\n');
    return sb.toString();
  }
  
}
