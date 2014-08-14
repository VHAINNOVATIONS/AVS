package gov.va.med.lom.vistabroker.misc.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.misc.data.VistaStation;

public class VistaStationDao extends BaseDao {
  
/**
 * NAME: ALSI STATION NAME GET<br>
 * TAG: 3                                ROUTINE: ALTZAPI<br>
 * RETURN VALUE TYPE: SINGLE VALUE       AVAILABILITY: PUBLIC<br>
 * DESCRIPTION:<br>
 * This does a remote query on a station name for a given station number - not a station IEN.<br>
 * Station number in this case refers to an actual field (STATION NUMBER) and is not the internal entry number.<br>
 * INPUT PARAMETER: STATION                PARAMETER TYPE: LITERAL<br>
 * MAXIMUM DATA LENGTH: 8                REQUIRED: YES<br>
 * SEQUENCE NUMBER: 1<br>
 * RETURN PARAMETER DESCRIPTION:<br>
 * The return is in the following format:<br>
 *       RESULT = STATION IEN^STATION NAME<br>
 * If null, no station was found to match the input station number field.
 */
  
  // CONSTRUCTORS
  public VistaStationDao() {
    super();
  }
  
  public VistaStationDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API  
  public VistaStation getStation(String stationNo) throws Exception {
    VistaStation station = null;
    setDefaultContext("ALS CLINICAL RPC");
    setDefaultRpcName("ALSI STATION NAME GET");
    String[] params = {String.valueOf(stationNo)};
    String result = sCall(params);
    if (result.length() > 0) {
      station = new VistaStation();
      String x = StringUtils.piece(result, 1);
      if (x.length() > 0) {
        station.setIen(x);
        station.setName(StringUtils.piece(result, 2));
      }
    }
    return station;
  }

}
