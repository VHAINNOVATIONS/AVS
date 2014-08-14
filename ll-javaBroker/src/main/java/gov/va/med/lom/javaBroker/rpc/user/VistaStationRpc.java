package gov.va.med.lom.javaBroker.rpc.user;


import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class VistaStationRpc extends AbstractRpc {
  
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
  
  // FIELDS
  private VistaStation station;
  
  // CONSTRUCTORS
  public VistaStationRpc() throws BrokerException {
    super();
  }
  
  public VistaStationRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }

  // PROPERTY ACCESSORS
  public VistaStation getStation() {
    return station;
  }
  
  // RPC API  
  public synchronized VistaStation getStation(String stationNo) throws BrokerException {
    if (setContext("ALS CLINICAL RPC")) {
      String[] params = {stationNo};
      String result = sCall("ALSI STATION NAME GET", params);
      if (result.length() > 0) {
        station = new VistaStation();
        String x = StringUtils.piece(result, 1);
        if (x.length() > 0) {
          station.setIen(x);
          station.setName(StringUtils.piece(result, 2));
        }
      }
      return station;
    } else
      throw getCreateContextException("XUS SIGNON");
  }

}
