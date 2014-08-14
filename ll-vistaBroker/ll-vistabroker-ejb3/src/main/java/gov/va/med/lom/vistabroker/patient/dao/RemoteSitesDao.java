package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.RemoteStation;

import java.util.ArrayList;
import java.util.List;

public class RemoteSitesDao extends BaseDao {
	
  // CONSTRUCTORS
  public RemoteSitesDao() {
    super();
  }
  
  public RemoteSitesDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
	// Returns a list of sites for which patient has had visits
  public List<RemoteStation> getRemoteSites(String dfn) throws Exception {
    List<RemoteStation> remoteSites = new ArrayList<RemoteStation>();
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWCIRN FACLIST");
  	List<String> list = lCall(dfn);
  	for(int i = 0; i < list.size(); i++) {
      RemoteStation remoteStation = new RemoteStation();
      String s = (String)list.get(i);
      String stationNo = StringUtils.piece(s, 1);
      remoteStation.setStationNo(stationNo);
      remoteStation.setStationName(StringUtils.piece(s, 2));
      String dt = StringUtils.piece(s, 3);
      if (!dt.equals("")) {
        remoteStation.setLastSeenDate(Double.valueOf(dt));
      }
      remoteStation.setLastEvent(StringUtils.piece(s, 4));
      remoteSites.add(remoteStation);
  	}
  	return remoteSites;
  }
  
}
