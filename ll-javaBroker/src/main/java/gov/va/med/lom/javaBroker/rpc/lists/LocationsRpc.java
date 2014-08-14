package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;
import java.util.List;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class LocationsRpc extends AbstractRpc {
	
  // FIELDS
  private LocationsList locationsList;

  // CONSTRUCTORS
  public LocationsRpc() throws BrokerException {
    super();
  }
  
  public LocationsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  //lists all active inpatient wards
  public synchronized LocationsList listAllWards() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQPT WARDS");
      locationsList = getItems(list, false);
      return locationsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized List<DivWardLocation> listAllByDiv() throws BrokerException {
    if (setContext("ALT INTRANET RPCS")) {
      List list = lCall("ALSI WARD LIST");
      return getWardsWithDiv(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized List<DivWardLocation> listAllForDiv(String div) throws BrokerException {
    if (setContext("ALT INTRANET RPCS")) {
      String[] params = {div};
      List list = lCall("ALSI WARDS FOR DIVISION",params);
      return getWardsWithDiv(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String getDivisionForWard(long ien) throws BrokerException {
	    if (setContext("ALT INTRANET RPCS")) {
	      String[] params = {String.valueOf(ien)};
	      return sCall("ALSI WARDS FOR DIVISION", params);
	    } else 
	      throw getCreateContextException("OR CPRS GUI CHART");
	  }
  
  //lists all hospital locations
  public synchronized LocationsList getSubSetOfLocations(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWU HOSPLOC", params);
      locationsList = getItems(list, false);
      return locationsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  //returns a list of locations (for use in a long list box)
  public synchronized LocationsList getSubSetOfInpatientLocations(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWU INPLOC", params);
      locationsList = getItems(list, false);
      return locationsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  //returns a list of locations (for use in a long list box)
  //Filtered by C, W, and Z types - i.e., Clinics, Wards, and "Other" type locations
  public synchronized LocationsList getSubSetOfNewLocations(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWU1 NEWLOC", params);
      locationsList = getItems(list, false);
      return locationsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  //returns a list of clinics
  public synchronized LocationsList listAllClinics() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQPT CLINICS");
      locationsList = getItems(list, false);
      return locationsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  //returns a list of clinics (for use in a long list box)
  public synchronized LocationsList getSubSetOfClinics(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWU CLINLOC", params);
      locationsList = getItems(list, false);
      return locationsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  // Parses the result list from VistA and returns array of patient list item objects
  private LocationsList getItems(ArrayList list, boolean mixedCase) {
    locationsList = new LocationsList();
    if (returnRpcResult) {
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      locationsList.setRpcResult(sb.toString().trim());
    }     
    Location[] locations = new Location[list.size()]; 
    for(int i=0; i < list.size(); i++) {
      Location location = new Location();
      String x = (String)list.get(i);
      if (returnRpcResult)
        location.setRpcResult(x);
      String ien = StringUtils.piece(x, 1);
      String name;
      if (mixedCase)
        name = StringUtils.mixedCase(StringUtils.piece(x, 2));
      else
        name = StringUtils.piece(x, 2);
      location.setIen(ien);
      location.setName(name);      
      locations[i] = location;
    }
    locationsList.setLocations(locations);
    return locationsList;    
  }  
  
  /**
   * Parses the result list from VistA and 
   * returns array of wards with the attached divisions
   * 
   */ 
  private List<DivWardLocation> getWardsWithDiv(List<String> rawList) {

	List<DivWardLocation> wards = new ArrayList<DivWardLocation>();
	
    for(String x : rawList){
      DivWardLocation ward = new DivWardLocation();
      ward.setIen(StringUtils.piece(x, 1));
      ward.setName(StringUtils.piece(x, 2));
      ward.setDivision(StringUtils.piece(x, 3));
      wards.add(ward);
    }
    
    return wards;    
  }  
}
