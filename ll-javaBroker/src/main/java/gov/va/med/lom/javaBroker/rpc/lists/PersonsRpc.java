package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;
import java.util.Date;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class PersonsRpc extends AbstractRpc {
	
  // FIELDS
  private PersonsList personsList;

  // CONSTRUCTORS
  public PersonsRpc() throws BrokerException {
    super();
  }
  
  public PersonsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  // Returns a list of persons 
  public synchronized PersonsList getSubSetOfPersons(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWU NEWPERS", params);
      Person[] persons = getItems(list, false);
      personsList = new PersonsList();
      personsList.setPersons(persons);
      return personsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
   
  // Returns a list of providers (for use in a long list box)
  public synchronized PersonsList getSubSetOfProviders(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {    
      String[] params = {startFrom, String.valueOf(direction), "PROVIDER"};
      ArrayList list = lCall("ORWU NEWPERS", params);
      Person[] persons = getItems(list, false);
      personsList = new PersonsList();
      personsList.setPersons(persons);
      return personsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns a list of providers (for use in a long list box)
  public synchronized PersonsList getSubSetOfProvidersWithClass(String startFrom, int direction, Date dateTime) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      if (dateTime == null)
        dateTime = new Date();
      double fmDate = FMDateUtils.dateToFMDate(dateTime);
      String[] params = {startFrom, String.valueOf(direction), "PROVIDER", String.valueOf(fmDate)};
      ArrayList list = lCall("ORWU NEWPERS", params);
      Person[] persons = getItems(list, false);
      personsList = new PersonsList();
      personsList.setPersons(persons);
      return personsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns a list of providers
  public synchronized PersonsList listProvidersAll() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQPT PROVIDERS");
      Person[] persons = getItems(list, false);
      personsList = new PersonsList();
      personsList.setPersons(persons);
      return personsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  // Returns a list of users (for use in a long list box)
  public synchronized PersonsList getSubSetOfUsers(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction), ""};
      ArrayList list = lCall("ORWU NEWPERS", params);
      Person[] persons = getItems(list, false);
      personsList = new PersonsList();
      personsList.setPersons(persons);
      return personsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns a list of users (for use in a long list box)
  public synchronized PersonsList getSubSetOfUsersByType(String startFrom, int direction,
                                                         String type) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction), type};
      ArrayList list = lCall("ORWU NEWPERS", params);
      Person[] persons = getItems(list, false);
      personsList = new PersonsList();
      personsList.setPersons(persons);
      return personsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns a list of users (for use in a long list box)
  public synchronized PersonsList getSubSetOfUsersWithClass(String startFrom, int direction, 
                                               String dateTime) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction), "", dateTime};
      ArrayList list = lCall("ORWU NEWPERS", params);
      Person[] persons = getItems(list, false);
      personsList = new PersonsList();
      personsList.setPersons(persons);
      return personsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns a list of users (for use in a long list box)
  public synchronized PersonsList getSubSetOfActiveAndInactivePersons(String startFrom, 
                                                         int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction), "", "", "true"};
      ArrayList list = lCall("ORWU NEWPERS", params);
      Person[] persons = getItems(list, false);
      personsList = new PersonsList();
      personsList.setPersons(persons);
      return personsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns a list of attendings (for use in a long list box)
  public synchronized PersonsList getSubSetOfAttendings(String startFrom, int direction,
                                  Date dateTime, String docTypeIen, String docIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String fmDate = null;
      if (dateTime != null)
        fmDate = String.valueOf(FMDateUtils.dateToFMDate(dateTime));
      else
        fmDate = "";
      String[] params = {startFrom, String.valueOf(direction), fmDate, docTypeIen, docIen};
      ArrayList list = lCall("ORWU2 COSIGNER", params);
      Person[] persons = getItems(list, false);
      personsList = new PersonsList();
      personsList.setPersons(persons);
      return personsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Parses the result list from VistA and returns array of patient list item objects
  private Person[] getItems(ArrayList list, boolean mixedCase) {
    Person[] persons = new Person[list.size()]; 
    for(int i=0; i < list.size(); i++) {
      Person person = new Person();
      String x = (String)list.get(i);
      if (returnRpcResult)
        person.setRpcResult(x);
      String duz = StringUtils.piece(x, 1);
      String name;
      if (mixedCase)
        name = StringUtils.mixedCase(StringUtils.piece(x, 2));
      else
        name = StringUtils.piece(x, 2);
      person.setDuz(duz);
      person.setName(name);      
      persons[i] = person;
    }
    return persons;
  }  
}
