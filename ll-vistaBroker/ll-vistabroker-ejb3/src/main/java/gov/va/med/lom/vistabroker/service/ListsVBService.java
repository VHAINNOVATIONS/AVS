package gov.va.med.lom.vistabroker.service;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.lists.data.*;
import gov.va.med.lom.vistabroker.security.ISecurityContext;

import java.util.Date;

import javax.ejb.Remote;

@Remote
public interface ListsVBService extends Service {
  
  public abstract CollectionServiceResponse<Icd9Code> listICD9Codes(ISecurityContext securityContext, String dsix, String scr);
  public abstract CollectionServiceResponse<Icd9Code> listIcd9Lexicon(ISecurityContext securityContext, String lex, double dt);
  public abstract CollectionServiceResponse<CptCode> listCPTCodes(ISecurityContext securityContext, String val);
  public abstract CollectionServiceResponse<CptCode> listCptLexicon(ISecurityContext securityContext, String lex, double dt);
  public abstract ServiceResponse<String> lexiconToIcd9Code(ISecurityContext securityContext, String ien, double dt);
  public abstract ServiceResponse<String> lexiconToCptCode(ISecurityContext securityContext, String ien, double dt);
  public abstract CollectionServiceResponse<ListItem> listAllSpecialties(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<ListItem> listAllTeams(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<CoverSheetRpc> getCoverSheetList(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<ListItem> getAtomicTests(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getSpecimens(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getAllTests(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getChemTests(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getLabUsers(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getTestGroupsForLabUser(ISecurityContext securityContext, String userDuz);
  public abstract CollectionServiceResponse<ListItem> getATest(ISecurityContext securityContext, String testIen);
  public abstract CollectionServiceResponse<ListItem> getATestGroup(ISecurityContext securityContext, String testGroupIen, String userDuz);
  public abstract ServiceResponse<String> getTestInfo(ISecurityContext securityContext, String testIen);
  public abstract CollectionServiceResponse<ListItem> getLabReportLists(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<ListItem> listAllWards(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfLocations(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfInpatientLocations(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfNewLocations(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> listAllClinics(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfClinics(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getTitlesForClass(ISecurityContext securityContext, String classIen, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> subsetOfProgressNoteTitles(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> subSetOfDCSummaryTitles(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> subSetOfClinProcTitles(ISecurityContext securityContext, String startFrom, int direction, boolean idNoteTitlesOnly);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfOrderItems(ISecurityContext securityContext, String startFrom, int direction, String xRef);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfPersons(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfProviders(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfProvidersWithClass(ISecurityContext securityContext, String startFrom, int direction, Date dateTime);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfAttendings(ISecurityContext securityContext, String startFrom, int direction, Date dateTime, String docTypeIen, String docIen) throws Exception;
  public abstract CollectionServiceResponse<ListItem> listProvidersAll(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfUsers(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfUsersByType(ISecurityContext securityContext, String startFrom, int direction, String type);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfUsersWithClass(ISecurityContext securityContext, String startFrom, int direction, String dateTime);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfActiveAndInactivePersons(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<String> odForProcedures(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfProcedures(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> getServiceList(ISecurityContext securityContext, int purpose);
  public abstract CollectionServiceResponse<ListItem> getServiceListWithSynonyms(ISecurityContext securityContext, int purpose, String consultIen);
  public abstract CollectionServiceResponse<ListItem> getSubSetOfServices(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<ListItem> subSetOfStatus(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<ListItem> subSetOfUrgencies(ISecurityContext securityContext, String consultIen);
  
}
