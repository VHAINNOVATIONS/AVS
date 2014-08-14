package gov.va.med.lom.avs.service.impl;

import gov.va.med.lom.avs.dao.FacilityHealthFactorsDao;
import gov.va.med.lom.avs.dao.VhaSitesDao;
import gov.va.med.lom.avs.dao.MedDescriptionsDao;
import gov.va.med.lom.avs.dao.RemoteAllergiesDao;
import gov.va.med.lom.avs.dao.RemoteAppointmentsDao;
import gov.va.med.lom.avs.dao.EncounterCacheMongoDao;

import gov.va.med.lom.avs.dao.morphia.RemoteAllergiesDaoImpl;
import gov.va.med.lom.avs.dao.morphia.RemoteAppointmentsDaoImpl;
import gov.va.med.lom.avs.dao.morphia.MedDescriptionsDaoImpl;
import gov.va.med.lom.avs.dao.morphia.EncounterCacheMongoDaoImpl;

import gov.va.med.lom.avs.enumeration.DGroupSeq;
import gov.va.med.lom.avs.model.EncounterCacheMongo;
import gov.va.med.lom.avs.model.Encounter;
import gov.va.med.lom.avs.model.EncounterProvider;
import gov.va.med.lom.avs.model.RemoteAllergies;
import gov.va.med.lom.avs.model.RemoteAllergy;
import gov.va.med.lom.avs.model.RemoteAppointment;
import gov.va.med.lom.avs.model.RemoteAppointments;
import gov.va.med.lom.avs.model.BasicDemographics;
import gov.va.med.lom.avs.model.EncounterInfo;
import gov.va.med.lom.avs.model.FacilityHealthFactor;
import gov.va.med.lom.avs.model.FacilityPrefs;
import gov.va.med.lom.avs.model.HealthFactor;
import gov.va.med.lom.avs.model.MedDescription;
import gov.va.med.lom.avs.model.MedicationRdv;
import gov.va.med.lom.avs.model.Procedure;
import gov.va.med.lom.avs.model.PceData;
import gov.va.med.lom.avs.model.VhaSite;
import gov.va.med.lom.avs.service.BaseService;
import gov.va.med.lom.avs.service.SettingsService;
import gov.va.med.lom.avs.service.SheetService;
import gov.va.med.lom.avs.util.DBConnectionPool;
import gov.va.med.lom.avs.util.StringResources;
import gov.va.med.lom.avs.util.MemCache;
import gov.va.med.lom.avs.util.AvsUtils;

import gov.va.med.lom.foundation.service.response.BaseServiceResponse;
import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.foundation.util.Precondition;

import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.ddr.DdrLister;
import gov.va.med.lom.javaBroker.rpc.patient.AllergiesReactionsRpc;
import gov.va.med.lom.javaBroker.rpc.patient.AppointmentsRpc;
import gov.va.med.lom.javaBroker.rpc.patient.PatientSelectionRpc;
import gov.va.med.lom.javaBroker.rpc.patient.ReportsRpc;
import gov.va.med.lom.javaBroker.rpc.patient.models.AllergyReaction;
import gov.va.med.lom.javaBroker.rpc.patient.models.AppointmentsList;
import gov.va.med.lom.javaBroker.rpc.patient.models.PatientList;
import gov.va.med.lom.javaBroker.rpc.patient.models.PatientListItem;
import gov.va.med.lom.javaBroker.rpc.patient.models.AllergiesReactions;
import gov.va.med.lom.javaBroker.rpc.user.VistaRemoteSignonRpc;
import gov.va.med.lom.javaBroker.rpc.user.models.RemoteSignon;
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistaLinkUtils.VistaLinkUtils;

import gov.va.med.lom.vistabroker.patient.dao.ReportsDao;
import gov.va.med.lom.vistabroker.patient.data.AllergiesReactants;
import gov.va.med.lom.vistabroker.patient.data.AllergyReactant;
import gov.va.med.lom.vistabroker.patient.data.Appointment;
import gov.va.med.lom.vistabroker.patient.data.ClinicalReminder;
import gov.va.med.lom.vistabroker.patient.data.Demographics;
import gov.va.med.lom.vistabroker.patient.data.EncounterAppointment;
import gov.va.med.lom.vistabroker.patient.data.Medication;
import gov.va.med.lom.vistabroker.patient.data.OrderInfo;
import gov.va.med.lom.vistabroker.patient.data.OrderView;
import gov.va.med.lom.vistabroker.patient.data.Problem;
import gov.va.med.lom.vistabroker.patient.data.RemoteStation;
import gov.va.med.lom.vistabroker.patient.data.OrdersInfoList;
import gov.va.med.lom.vistabroker.patient.data.PatientInfo;
import gov.va.med.lom.vistabroker.patient.data.VitalSignMeasurement;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.service.DdrVBService;
import gov.va.med.lom.vistabroker.service.MiscVBService;
import gov.va.med.lom.vistabroker.util.FMDateUtils;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mongodb.morphia.query.UpdateOperations;
import org.bson.types.ObjectId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 

/**
 * AVS Service implementation
 */
@Stateless(name="gov.va.med.lom.avs.SheetService")
@Local(SheetService.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class SheetServiceImpl extends BaseServiceImpl implements BaseService, SheetService {

  // inner class encapsulating order description and sort key
  private class OrderDescription {
    private String sortKey;
    private String description;
    
    public OrderDescription(String sortKey, String description) {
      this.sortKey = sortKey;
      this.description = description;
    }
    public String getSortKey() {
      return sortKey;
    }
    public String getDescription() {
      return description;
    }
  }
  
  private static MemCache MEM_CACHE;
  
  public static boolean IS_DEMO;
  public static String DEMO_STATION_NO;
  public static String DEMO_PT_DFN;
  public static String DEMO_PT_NAME;
  public static double DEMO_PT_DOB_DM;
  public static String DEMO_PT_DOB;
  public static int DEMO_PT_AGE;
  public static String DEMO_PT_SSN;  
  public static String VISTA_SERVER;
  public static int VISTA_PORT;
  public static String VISTA_AV;
  public static String MIL_IMAGES_DIR;
  
  private static final long CACHE_DATA_EXPIRE = 24 * 60 * 60 * 24; // 1 day
  private static final long EXPIRED_REMOTE_MEDS = 24 * 60 * 60 * 24 * 30; // 30 days
  private static Hashtable<String, ISecurityContext> DDR_SEC_CTX_MAP 
      = new Hashtable<String, ISecurityContext>();
  private static final Hashtable<String, String> CONFIDENTIAL_ICD9_MAP
      = new Hashtable<String,String>();
  private static final List<String> LAB_EXCLUDE_START_LIST 
      = new ArrayList<String>();
  private static final List<String> LAB_EXCLUDE_INDEX_LIST
      = new ArrayList<String>();
  private static Hashtable<String, Hashtable<String, DGroupSeq>> DGROUP_MAP
      = new Hashtable<String, Hashtable<String, DGroupSeq>>();
  private static final long MSEC_HOUR = 60 * 60 * 1000;
  private static final int DB_MIN_POOL_SIZE = 5;
  private static final long DB_REAP_DELAY = 60000;
  private static final long DB_INACTIVE_TIMEOUT = 10 * 60 * 1000;
  private static final Pattern newOrderPattern = 
      Pattern.compile("^(\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2})\\s{2}New Order entered by\\s[\\w\\s,'-\\(\\)]+$");
  private static final Pattern discontinueEnteredPattern = 
      Pattern.compile("^(\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2})\\s{2}Discontinue entered by\\s[\\w\\s,'-\\(\\)]+$");
  
  static {
    
    MEM_CACHE = MemCache.createMemCache();
    
    ResourceBundle res = null;
    
    try {
      res = ResourceBundle.getBundle("gov.va.med.lom.avs.vista");
      VISTA_SERVER = res.getString("vista.vistaServer");
      VISTA_PORT = Integer.valueOf(res.getString("vista.vistaPort")).intValue();
      VISTA_AV = res.getString("vista.vistaAV");
    } catch(Exception e) {}
    
    try {
      res = ResourceBundle.getBundle("gov.va.med.lom.avs.avs");
      IS_DEMO = StringUtils.strToBool(res.getString("avs.demo"), "true");
      DEMO_STATION_NO = res.getString("avs.demo.stationNo");
      DEMO_PT_DFN = res.getString("avs.demo.ptDfn");
      DEMO_PT_NAME = res.getString("avs.demo.ptName");
      DEMO_PT_DOB_DM = Double.valueOf(res.getString("avs.demo.ptDobDm")).doubleValue();
      DEMO_PT_DOB = res.getString("avs.demo.ptDob");
      DEMO_PT_AGE = Integer.valueOf(res.getString("avs.demo.ptAge")).intValue();
      DEMO_PT_SSN = res.getString("avs.demo.ptSsn");      
    } catch(Exception e) {}
    
    try {
      res = ResourceBundle.getBundle("gov.va.med.lom.avs.mil");
      MIL_IMAGES_DIR = res.getString("mil.images");
    } catch(Exception e) {}
    
    CONFIDENTIAL_ICD9_MAP.put("292", "DRUG WITHDRAWAL");
    CONFIDENTIAL_ICD9_MAP.put("292.11", "DRUG PSYCH DISOR W DELUS");
    CONFIDENTIAL_ICD9_MAP.put("292.12", "DRUG PSY DIS W HALLUCIN");
    CONFIDENTIAL_ICD9_MAP.put("292.2", "PATHOLOGIC DRUG INTOX");
    CONFIDENTIAL_ICD9_MAP.put("292.81", "DRUG-INDUCED DELIRIUM");
    CONFIDENTIAL_ICD9_MAP.put("292.82", "DRUG PERSISTING DEMENTIA");
    CONFIDENTIAL_ICD9_MAP.put("292.83", "DRUG PERSIST AMNESTC DIS");
    CONFIDENTIAL_ICD9_MAP.put("292.84", "DRUG-INDUCED MOOD DISORD");
    CONFIDENTIAL_ICD9_MAP.put("292.89", "DRUG MENTAL DISORDER NEC");
    CONFIDENTIAL_ICD9_MAP.put("292.9", "DRUG MENTAL DISORDER NOS");
    
    CONFIDENTIAL_ICD9_MAP.put("291", "DELIRIUM TREMENS");
    CONFIDENTIAL_ICD9_MAP.put("291.1", "ALCOHOL AMNESTIC DISORDR");
    CONFIDENTIAL_ICD9_MAP.put("291.2", "ALCOHOL PERSIST DEMENTIA");
    CONFIDENTIAL_ICD9_MAP.put("291.21", "DEMENTIA ASSOC W/ETOH, MILD");
    CONFIDENTIAL_ICD9_MAP.put("291.22", "DEMENTIA ASSOC W/ETOH, MOD");
    CONFIDENTIAL_ICD9_MAP.put("291.23", "DEMENTIA ASSOC W/ETOH SEVERE");
    CONFIDENTIAL_ICD9_MAP.put("291.3", "ALCOH PSY DIS W HALLUCIN");
    CONFIDENTIAL_ICD9_MAP.put("291.4", "PATHOLOGIC ALCOHOL INTOX");
    CONFIDENTIAL_ICD9_MAP.put("291.5", "ALCOH PSYCH DIS W DELUS");
    CONFIDENTIAL_ICD9_MAP.put("291.8", "ALCOHOLIC PSYCHOSIS NEC");
    CONFIDENTIAL_ICD9_MAP.put("291.81", "ALCOHOL WITHDRAWAL");
    CONFIDENTIAL_ICD9_MAP.put("291.82", "ALCOH INDUCE SLEEP DISOR");
    CONFIDENTIAL_ICD9_MAP.put("291.89", "ALCOHOL MENTAL DISOR NEC");
    CONFIDENTIAL_ICD9_MAP.put("291.9", "ALCOHOL MENTAL DISOR NOS");
    CONFIDENTIAL_ICD9_MAP.put("303.01", "AC ALCOHOL INTOX-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("303.02", "AC ALCOHOL INTOX-EPISOD");
    CONFIDENTIAL_ICD9_MAP.put("303.03", "AC ALCOHOL INTOX-REMISS");
    CONFIDENTIAL_ICD9_MAP.put("303.91", "ALCOH DEP NEC/NOS-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("303.92", "ALCOH DEP NEC/NOS-EPISOD");
    CONFIDENTIAL_ICD9_MAP.put("303.93", "ALCOH DEP NEC/NOS-REMISS");
    CONFIDENTIAL_ICD9_MAP.put("304.01", "OPIOID DEPENDENCE CONT");
    CONFIDENTIAL_ICD9_MAP.put("304.02", "OPIOID DEPENDENCE EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("304.03", "OPIOID DEPENDENCE IN REMISSION");
    CONFIDENTIAL_ICD9_MAP.put("304.11", "SED,HYP,ANXIOLYT DEP-CON");
    CONFIDENTIAL_ICD9_MAP.put("304.12", "SED,HYP,ANXIOLYT DEP-EPI");
    CONFIDENTIAL_ICD9_MAP.put("304.13", "SED,HYP,ANXIOLYT DEP-REM");
    CONFIDENTIAL_ICD9_MAP.put("304.21", "COCAINE DEPEND-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("304.22", "COCAINE DEPEND-EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("304.23", "COCAINE DEPEND-REMISS");
    CONFIDENTIAL_ICD9_MAP.put("304.31", "CANNABIS DEPENDENCE CONTINUOUS");
    CONFIDENTIAL_ICD9_MAP.put("304.32", "CANNABIS DEPENDENCE EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("304.39", "OTHER CANNABIS DEPEND, NEC");
    CONFIDENTIAL_ICD9_MAP.put("304.41", "AMPHETAMINE DEPENDENCE CONT");
    CONFIDENTIAL_ICD9_MAP.put("304.42", "AMPHETAMINE DEPENDENCE EPISOD");
    CONFIDENTIAL_ICD9_MAP.put("304.43", "AMPHETAMINE DEPENDENCE IN REM");
    CONFIDENTIAL_ICD9_MAP.put("304.51", "HALLUCINOGEN DEPENDENCE CONT");
    CONFIDENTIAL_ICD9_MAP.put("304.52", "HALLUCINOGEN DEPENDENCE EPISOD");
    CONFIDENTIAL_ICD9_MAP.put("304.53", "HALLUCINOGEN DEPENDENCE IN REM");
    CONFIDENTIAL_ICD9_MAP.put("304.61", "DRUG DEPEND OTH-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("304.62", "DRUG DEPEND OTH-EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("304.63", "DRUG DEPEND OTH-IN REM");
    CONFIDENTIAL_ICD9_MAP.put("304.71", "OPIOID+OTHER DEP-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("304.72", "OPIOID+OTHER DEP-EPISOD");
    CONFIDENTIAL_ICD9_MAP.put("304.73", "OPIOID+OTHER DEP-REMISS");
    CONFIDENTIAL_ICD9_MAP.put("304.81", "COMB DRUG DEP NEC-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("304.82", "COMB DRUG DEP NEC-EPISOD");
    CONFIDENTIAL_ICD9_MAP.put("304.83", "COMB DRUG DEP NEC-REMISS");
    CONFIDENTIAL_ICD9_MAP.put("304.91", "UNSPEC DRUG DEPEND CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("304.92", "DRUG DEPEND NOS-EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("304.93", "DRUG DEPEND NOS-REMISS");
    CONFIDENTIAL_ICD9_MAP.put("305.00", "ALCOHOL ABUSE");
    CONFIDENTIAL_ICD9_MAP.put("305.01", "ALCOHOL ABUSE-CONTINUOUS");
    CONFIDENTIAL_ICD9_MAP.put("305.02", "ALCOHOL ABUSE-EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("305.03", "ALCOHOL ABUSE-IN REMISS");
    CONFIDENTIAL_ICD9_MAP.put("305.11", "TOBACCO ABUSE-CONTINUOUS");
    CONFIDENTIAL_ICD9_MAP.put("305.12", "TOBACCO ABUSE-EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("305.13", "TOBACCO ABUSE-IN REMISS");
    CONFIDENTIAL_ICD9_MAP.put("305.20", "CANNABIS ABUSE");
    CONFIDENTIAL_ICD9_MAP.put("305.21", "CANNABIS ABUSE-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("305.22", "CANNABIS ABUSE-EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("305.23", "CANNABIS ABUSE-IN REMISS");
    CONFIDENTIAL_ICD9_MAP.put("305.31", "HALLUCINOG ABUSE-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("305.32", "HALLUCINOG ABUSE-EPISOD");
    CONFIDENTIAL_ICD9_MAP.put("305.33", "HALLUCINOG ABUSE-REMISS");
    CONFIDENTIAL_ICD9_MAP.put("305.41", "SED,HYP,ANXIOLYTC AB-CON");
    CONFIDENTIAL_ICD9_MAP.put("305.42", "SED,HYP,ANXIOLYTC AB-EPI");
    CONFIDENTIAL_ICD9_MAP.put("305.43", "SED,HYP,ANXIOLYTC AB-REM");
    CONFIDENTIAL_ICD9_MAP.put("305.51", "OPIOID ABUSE CONTINUOUS");
    CONFIDENTIAL_ICD9_MAP.put("305.52", "OPIOID ABUSE EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("305.53", "OPIOID ABUSE IN REMISSION");
    CONFIDENTIAL_ICD9_MAP.put("305.61", "COCAINE ABUSE-CONTINUOUS");
    CONFIDENTIAL_ICD9_MAP.put("305.62", "COCAINE ABUSE-EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("305.63", "COCAINE ABUSE-IN REMISS");
    CONFIDENTIAL_ICD9_MAP.put("305.71", "AMPHETAMINE ABUSE-CONTINUOUS");
    CONFIDENTIAL_ICD9_MAP.put("305.72", "AMPHETAMINE ABUSE-EPISODIC");
    CONFIDENTIAL_ICD9_MAP.put("305.73", "AMPHETAMINE ABUSE-REMISS");
    CONFIDENTIAL_ICD9_MAP.put("305.81", "ANTIDEPRESS ABUSE-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("305.82", "ANTIDEPRESS ABUSE-EPISOD");
    CONFIDENTIAL_ICD9_MAP.put("305.83", "ANTIDEPRESS ABUSE-REMISS");
    CONFIDENTIAL_ICD9_MAP.put("305.91", "OTHER DRUG ABUSE-CONTIN");
    CONFIDENTIAL_ICD9_MAP.put("305.92", "OTHER DRUG ABUSE-EPISOD");
    CONFIDENTIAL_ICD9_MAP.put("305.93", "OTHER DRUG ABUSE-REMISS");

    CONFIDENTIAL_ICD9_MAP.put("42", "HIV WITH SPECIFIED INFECTIONS");
    CONFIDENTIAL_ICD9_MAP.put("42.1", "HIV CAUSING OTHER SPEC INF");
    CONFIDENTIAL_ICD9_MAP.put("42.2", "HIV WITH SPEC MALIGNANT NEO");
    CONFIDENTIAL_ICD9_MAP.put("42.9", "HIV-AIDS UNSPECIFIED");
    CONFIDENTIAL_ICD9_MAP.put("43", "HIV CAUSING LYMPHADENOPATHY");
    CONFIDENTIAL_ICD9_MAP.put("43.1", "HIV CAUSING SPEC DISEASE-CNS");
    CONFIDENTIAL_ICD9_MAP.put("43.2", "HIV CAUSE OTH IMMUNE DIS");
    CONFIDENTIAL_ICD9_MAP.put("43.3", "HIV CAUSE OTHER SPEC DISORDERS");
    CONFIDENTIAL_ICD9_MAP.put("43.9", "HIV-ARC, UNSPECIFIED");
    CONFIDENTIAL_ICD9_MAP.put("44", "OTHER HIV CAUSE SPEC ACUTE INF");
    CONFIDENTIAL_ICD9_MAP.put("44.9", "HIV INFECTION UNSPECIFIED");
    CONFIDENTIAL_ICD9_MAP.put("79.53", "HIV 2");
    CONFIDENTIAL_ICD9_MAP.put("795.8", "POSITIVE SERO/VIRAL HIV");

    CONFIDENTIAL_ICD9_MAP.put("282.5", "SICKLE-CELL TRAIT");
    CONFIDENTIAL_ICD9_MAP.put("282.61", "HB-SS DISEASE W/O CRISIS");
    CONFIDENTIAL_ICD9_MAP.put("282.62", "HB-SS DISEASE W CRISIS");
    CONFIDENTIAL_ICD9_MAP.put("282.63", "HB-SS/HB-C DIS W/O CRSIS");
    CONFIDENTIAL_ICD9_MAP.put("282.69", "HB-SS DIS NEC W CRISIS");
    CONFIDENTIAL_ICD9_MAP.put("282.6", "SICKLE CELL DISEASE NOS");
    CONFIDENTIAL_ICD9_MAP.put("V78.2", "SCREEN-SICKLE CELL DIS");
    CONFIDENTIAL_ICD9_MAP.put("282.44", "SICKLE-CELL THALASSEMIA");
    CONFIDENTIAL_ICD9_MAP.put("282.5", "SICKLE-CELL TRAIT HB-AS GENO");
    CONFIDENTIAL_ICD9_MAP.put("282.59", "SICKLE-CELL TRAIT, OTHER");
    CONFIDENTIAL_ICD9_MAP.put("282.65", "SICKLE-CELL DISEASE/HB-E");

    LAB_EXCLUDE_INDEX_LIST.add("SP LB");
    LAB_EXCLUDE_INDEX_LIST.add("SP ADDED BY ");
    LAB_EXCLUDE_INDEX_LIST.add("SP ONCE ");
    LAB_EXCLUDE_START_LIST.add("ZZ NON-FASTING ");
  }
  
  protected static final Log log = LogFactory.getLog(SheetServiceImpl.class);
  
  private static DBConnectionPool dbConnectionPool;

  @EJB
  private VhaSitesDao vhaSitesDao; 
  
  @EJB
  private PatientVBService patientRpcs;
  
  @EJB
  private DdrVBService ddrRPCs;
  
  @EJB
  private MiscVBService miscRpcs;
  
  @EJB
  private SettingsService settingsService;
  
  @EJB
  private FacilityHealthFactorsDao facilityHealthFactorsDao;
  
  private static final String DEMOGRAPHICS = "demographics";
  
  public SheetServiceImpl() {}

  public ServiceResponse<BasicDemographics> getBasicDemographics(ISecurityContext securityContext, EncounterInfo encounterInfo) {

    ServiceResponse<BasicDemographics> response = new ServiceResponse<BasicDemographics>();
    
    BasicDemographics basicDemographics = (BasicDemographics)MEM_CACHE.get(encounterInfo, DEMOGRAPHICS);
    if (basicDemographics != null) {
      response.setPayload(basicDemographics);
      return response;
    }
    
    basicDemographics = new BasicDemographics(securityContext.getDivision(), encounterInfo.getPatientDfn());
    response.setPayload(basicDemographics);

    ServiceResponse<PatientInfo> vistaResponse = this.patientRpcs
        .getPatientInfo(securityContext, encounterInfo.getPatientDfn());
    super.checkVistaExceptions(vistaResponse);
    PatientInfo patientInfo = vistaResponse.getPayload();

    Date patientDob = patientInfo.getDob();
    if (patientDob != null) {
      basicDemographics.setDob(!IS_DEMO ? FMDateUtils.dateToFMDate(patientDob) : DEMO_PT_DOB_DM);
    }
    basicDemographics.setDobStr(!IS_DEMO ? patientInfo.getDobStr() : DEMO_PT_DOB);
    basicDemographics.setDfn(patientInfo.getDfn());
    basicDemographics.setName(!IS_DEMO ? patientInfo.getName() : DEMO_PT_NAME);
    basicDemographics.setSex(patientInfo.getSex());
    basicDemographics.setSsn(patientInfo.getSsn());
    basicDemographics.setAge(!IS_DEMO ? patientInfo.getAge() : DEMO_PT_AGE);
    basicDemographics.setDeceasedDateStr(patientInfo.getDeceasedDateStr());
    basicDemographics.setVeteran(patientInfo.getVeteran());
    basicDemographics.setScPct(patientInfo.getScPct());
    basicDemographics.setLocation(patientInfo.getLocation());
    basicDemographics.setRoomBed(patientInfo.getRoomBed());
    basicDemographics.setInpatient(patientInfo.getInpatient());
    
    ServiceResponse<Demographics> vistaResponse2 = this.patientRpcs
        .getDemographics(securityContext, encounterInfo.getPatientDfn());
    super.checkVistaExceptions(vistaResponse2);
    Demographics demographics = vistaResponse2.getPayload();

    if (demographics != null) {
      basicDemographics.setPrimaryProvider(demographics.getPrimaryProvider());
      basicDemographics.setPrimaryTeam(demographics.getPrimaryTeam());
    }
    
    MEM_CACHE.put(encounterInfo, DEMOGRAPHICS, basicDemographics);
    
    return response;  
  }
  
  public ServiceResponse<LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>>> getTodaysOrders(
    ISecurityContext securityContext, EncounterInfo encounterInfo) {
    
    // create a map
    LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>> sortedOrders = 
        new LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>>();
    
    // Add dgroups with null placeholders to set the sort order
    DGroupSeq[] dGroupSeqs = DGroupSeq.values();
    for (int i = 0; i < dGroupSeqs.length; i++) {
      sortedOrders.put(dGroupSeqs[i], null);
    }
    
    // add the orders
    List<OrderInfo> ordersList = this.getRawOrders(securityContext, encounterInfo);
    for (OrderInfo orderInfo : ordersList) {
      DGroupSeq dGroupSeq = this.getDGroupFromOrderInfo(securityContext, orderInfo);
      // don't include discontinued (1), expired (7), discontinued/edit (12), cancelled (13), and lapsed (14) text and lab orders. 
      if ((dGroupSeq.equals(DGroupSeq.OTHER.getName()) || 
           dGroupSeq.equals(DGroupSeq.LAB.getName()))) {
        if (orderInfo.getStatusIen().equals("1") || orderInfo.getStatusIen().equals("7") || 
            orderInfo.getStatusIen().equals("12") || orderInfo.getStatusIen().equals("13") ||
            orderInfo.getStatusIen().equals("14")) {
          continue;
        }
      }
      
      // don't include cancelled orders that have been cancelled within two hours of the original order
      if (orderInfo.getStatusIen().equals("1") || orderInfo.getStatusIen().equals("13")) {
        
        Date newOrderDate = null;
        Date discontinuedDate = null;
        // Get order details to extract new order and cancel date/time
        String orderDetails = (String)MEM_CACHE.get(securityContext.getDivision(), orderInfo.getId(), "orderDetails");
        if (orderDetails == null) {
          orderDetails = this.patientRpcs.getOrderDetails(securityContext, orderInfo.getId(), encounterInfo.getPatientDfn()).getPayload();
          MEM_CACHE.put(securityContext.getDivision(), orderInfo.getId(), "orderDetails", orderDetails);
        }
        String[] lines = StringUtils.pieceList(orderDetails, '\n');
        for (int i = 0; i < lines.length; i++) {
          String s = lines[i];
          Matcher m = null; 
          if ((m = newOrderPattern.matcher(s)).matches()) {
            try {
              newOrderDate = DateUtils.convertDateStr(m.group(1), DateUtils.ENGLISH_SHORT_DATE_TIME_FORMAT);
            } catch(Exception e) {}
          } else if ((m = discontinueEnteredPattern.matcher(s)).matches()) {
            try {
              discontinuedDate = DateUtils.convertDateStr(m.group(1), DateUtils.ENGLISH_SHORT_DATE_TIME_FORMAT);
            } catch(Exception e) {}
          }
        }
        // don't include orders cancelled within a two hours
        if ((newOrderDate != null) && (discontinuedDate != null)) {
          long diff = discontinuedDate.getTime() - newOrderDate.getTime();
          if (diff <= (MSEC_HOUR*2)) {
            continue;
          }
        }
      }
      
      OrderDescription description = this.getOrderDescription(securityContext, orderInfo, dGroupSeq, encounterInfo);
      if (description != null) {
        SortedMap<String,List<String>> descriptions = sortedOrders.get(dGroupSeq);
        if (descriptions == null) {
          descriptions = new TreeMap<String,List<String>>();
          sortedOrders.put(dGroupSeq, descriptions);
        }
        List<String> descList = descriptions.get(description.getSortKey());
        if (descList == null) {
          descList = new ArrayList<String>();
          descriptions.put(description.getSortKey(), descList);
        }
        descList.add(description.getDescription());
      }
      
    }

    // remove any null entries
    LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>> mappedOrders = 
        new LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>>();
    Iterator<DGroupSeq> it = sortedOrders.keySet().iterator();
    while (it.hasNext()) {
      DGroupSeq dGroupSeq = it.next();
      SortedMap<String,List<String>> sublist = sortedOrders.get(dGroupSeq);
      if (sublist != null) {
        mappedOrders.put(dGroupSeq, sublist);
      }
    }
    
    ServiceResponse<LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>>> response = 
        new ServiceResponse<LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>>>();
    response.setPayload(mappedOrders);

    return response;  
  }
  
  private List<OrderInfo> getRawOrders(ISecurityContext securityContext, EncounterInfo encounterInfo) {

    ServiceResponse<OrderView> orderViewResponse = 
        this.patientRpcs.getOrderViewDefault(securityContext);
    super.checkVistaExceptions(orderViewResponse);
    OrderView orderView = orderViewResponse.getPayload();
    
    FacilityPrefs facilityPrefs = this.settingsService.getFacilityPrefs(securityContext.getDivision()).getPayload();

    List<OrderInfo> result = new ArrayList<OrderInfo>();
    for (Encounter encounter : encounterInfo.getEncounterCache().getEncounters()) {
      
      // parse out the time component of the encounter Fileman date/time
      String dtStr = StringUtils.piece(String.valueOf(encounter.getEncounterDatetime()), '.', 1);
      StringBuffer tmStr = new StringBuffer("0." + StringUtils.piece(String.valueOf(encounter.getEncounterDatetime()), '.', 2));
      if (tmStr.length() > 8) {
        String t = tmStr.substring(0, 8);
        tmStr.delete(0, tmStr.length());
        tmStr.append(t);
      }
      for (int i = 8 - tmStr.length(); i > 0; i--) {
        tmStr.append("0");
      }
      long tm = parseInterval(tmStr.toString());
      
      // set "from" date/time for order retrieval
      int from = facilityPrefs.getOrderTimeFrom() > 0 ? facilityPrefs.getOrderTimeFrom() : 60;
      int hours = from / 60;
      int minutes = from % 60;
      long l = parseInterval(String.format("0.%02d%02d%02d", hours, minutes, 0));
      String s = formatInterval(tm - l > 0 ? tm - l : 0);
      double d = Double.valueOf(dtStr) + Double.valueOf(s); 
      orderView.setFmTimeFrom(d); 
      
      // set "to" date/time for order retrieval
      int thru = facilityPrefs.getOrderTimeThru() > 0 ? facilityPrefs.getOrderTimeThru() : 180;
      hours = thru / 60;
      minutes = thru % 60;
      l = parseInterval(String.format("0.%02d%02d%02d", hours, minutes, 0));
      s = formatInterval(tm + l > 24 ? tm + l : 86399000);
      d = Double.valueOf(dtStr) + Double.valueOf(s); 
      orderView.setFmTimeThru(d);
      
      // get the list of orders
      ServiceResponse<OrdersInfoList> orderListResponse = 
          this.patientRpcs.getOrdersList3(securityContext, encounterInfo.getPatientDfn(), orderView, null, false);
      super.checkVistaExceptions(orderListResponse);
      OrdersInfoList ordersInfoList = orderListResponse.getPayload();
  
      /* *********************************** BEGIN DEMO/TEST ****************************************************/
      if (IS_DEMO && encounterInfo.getFacilityNo().equals(DEMO_STATION_NO) && encounterInfo.getPatientDfn().equals(DEMO_PT_DFN)) {
        if (ordersInfoList == null) {
          ordersInfoList = new OrdersInfoList();
        }
        List<OrderInfo> orders = ordersInfoList.getOrdersInfo();
        if (orders == null) {
          orders = new ArrayList<OrderInfo>();
        }
        List<OrderInfo> testOrders = new ArrayList<OrderInfo>();
        testOrders.add(new OrderInfo());
        testOrders.add(new OrderInfo());
        testOrders.add(new OrderInfo());
        int i = 0;
        for (OrderInfo orderInfo : testOrders) {
          i++;
          for (EncounterProvider provider : encounter.getProviders()) {
            orderInfo.setProviderDuz(provider.getProviderDuz());
          }
          switch (i) {
            case 1 : orderInfo.setId("42835992;1");
                     orderInfo.setDGroupSeq("90"); 
                     orderInfo.setDGroup(DGroupSeq.CONSULT.getName()); 
                     orderInfo.setText("RHEUMATOLOGY Consult");
                     orderInfo.setStatusIen("0");
                     orderInfo.setStatus("ACTIVE");
                     orderInfo.setStartTimeStr("10/01/2014@00:00");
                     break;
            case 2 : orderInfo.setId("42835993;1");
                     orderInfo.setDGroupSeq("80"); 
                     orderInfo.setDGroup(DGroupSeq.IMAGING.getName()); 
                     orderInfo.setText("HAND RIGHT 2 VIEW AXILARY BILATERAL EXAM");
                     orderInfo.setStatusIen("0");
                     orderInfo.setStatus("ACTIVE");
                     orderInfo.setStartTimeStr("10/01/2014@00:00");
                     break;
            case 3 : orderInfo.setId("42835994;1");
                     orderInfo.setDGroupSeq("80"); 
                     orderInfo.setDGroup(DGroupSeq.IMAGING.getName()); 
                     orderInfo.setText("HAND LEFT 2 VIEW AXILARY BILATERAL EXAM");
                     orderInfo.setStatusIen("0");
                     orderInfo.setStatus("ACTIVE");
                     orderInfo.setStartTimeStr("10/01/2014@00:00");
                     break;
          }
        }
        orders.addAll(testOrders);
      }
      /* *********************************** END DEMO/TEST ****************************************************/
      if (ordersInfoList == null) {
        return result;
      }
      List<OrderInfo> orders = ordersInfoList.getOrdersInfo();
      if (orders == null) {
        return result; 
      } else {
        Hashtable<String, String> providersHT = new Hashtable<String, String>();
        for (EncounterProvider provider : encounter.getProviders()) {
          providersHT.put(provider.getProviderDuz(), provider.getProviderDuz());
        }
        for (OrderInfo order : orders) {
          if ((order.getProviderDuz() != null) &&
              providersHT.containsKey(order.getProviderDuz())) {
            result.add(order);
          }
        }
      }
    }
    
    return result;
  }

  private DGroupSeq getDGroupFromOrderInfo(ISecurityContext securityContext, OrderInfo orderInfo) {
    
    Hashtable<String, DGroupSeq> dGroupMap = DGROUP_MAP.get(securityContext.getDivision());
    if (dGroupMap == null) {
      List<String> dgroup = (List<String>)this.patientRpcs.getDGroupMap(securityContext).getCollection();
      dGroupMap = new Hashtable<String, DGroupSeq>();
      for (String s : dgroup) {
        String values = StringUtils.piece(s, '=', 2);
        String ien = StringUtils.piece(values, 1);
        String item = StringUtils.piece(values, 2).toUpperCase();
        if (item.indexOf("LAB") >= 0) {
          dGroupMap.put(ien, DGroupSeq.LAB);
        } else if ((item.indexOf("MED") >= 0) || (item.indexOf("OTC") >= 0)) {
          dGroupMap.put(ien, DGroupSeq.MED);
        } else if (item.indexOf("IMAGING") >= 0) {
          dGroupMap.put(ien, DGroupSeq.IMAGING);
        } else if (item.indexOf("CONSULTS") >= 0) {
          dGroupMap.put(ien, DGroupSeq.CONSULT);          
        }  
      }
      DGROUP_MAP.put(securityContext.getDivision(), dGroupMap);
    }
    
    String seq = orderInfo.getDGroupSeq();
    
    DGroupSeq dGroup;
    if ((seq != null) && (dGroupMap.containsKey(seq))) {
      dGroup = dGroupMap.get(seq);
    } else {
      dGroup = DGroupSeq.OTHER;
    }

    return dGroup;
  }

  private OrderDescription getOrderDescription(ISecurityContext securityContext, OrderInfo orderInfo, 
      DGroupSeq dGroup, EncounterInfo encounterInfo) {
    
    OrderDescription description = null;

    String text = null;
    String fmDateStr = null;
    if (orderInfo != null) {
      switch (dGroup) {
        case MED:
          if (orderInfo.getText() != null) {
            String orderNum = StringUtils.piece(orderInfo.getId(), ';', 1);
            // returns: file #^status in external form^NDC^total # refills^remarks^release date/time^rx #^previous order #^forward order #
            String orderDetails = getMedOrderDetails(securityContext, orderNum, encounterInfo);
            String orderStatus = null; 
            if (orderDetails != null) {
              String file = StringUtils.piece(orderDetails, 1);
              if (file.equals("52")) {
                String status = StringUtils.piece(orderDetails, 2);
                
                if (status.equals("ACTIVE")) {
                  // check if last character of rx # is alpha
                  String rxNum = StringUtils.piece(orderDetails, 7);
                  int i = (int)rxNum.charAt(rxNum.length()-1);
                  // if alpha character then this is a renewed med
                  if ((i >= 65) && (i <= 90)) {
                    orderStatus = "RENEWED";
                  } else {
                    // check if this is either a new or edited med
                    String prevOrderNum = StringUtils.piece(orderDetails, 8);
                    String forwardOrderNum = StringUtils.piece(orderDetails, 9);
                    if (prevOrderNum.isEmpty() && forwardOrderNum.isEmpty()) {
                      orderStatus = "NEW ORDER";
                    } else if (!prevOrderNum.isEmpty()) {
                      orderStatus = "CHANGED";
                    } else {
                      orderStatus = status;
                    }
                  }
                } else {
                  String comment = StringUtils.piece(orderDetails, 5);
                  if (comment.startsWith("RENEW")) {
                    orderStatus = "RENEW";
                  } else {
                    orderStatus = status;
                  }
                }
              } else if (file.equals("52.41")) {
                String status = StringUtils.piece(orderDetails, 2);
                if (status.trim().isEmpty()) {
                  String reason = StringUtils.piece(orderDetails, 5);
                  if (!reason.trim().isEmpty()) {
                    if (reason.equals("N")) {
                      orderStatus = "NEW ORDER";
                    } else if (reason.equals("R")) {
                      orderStatus = "RENEW";
                    } else if (reason.equals("E")) {
                      orderStatus = "CHANGED";
                    } else if (reason.equals("F")) {
                      orderStatus = "REFILL";
                    } 
                  } else {
                    orderStatus = null;
                  }
                } else {
                  orderStatus = status;
                }
              }
            }
            String[] lines = orderInfo.getText().split("[\n\r]");
            text = lines[0];
            if (text.toUpperCase().startsWith("RENEW ")) {
              text = text.substring(5);
              orderStatus = "RENEW";
            } else if ( text.toUpperCase().startsWith("*")) {
              text = text.substring(1);
            } else if ( text.toUpperCase().startsWith("CHANGE ")) {
              if (text.toUpperCase().endsWith("MG")) {
                text = text.substring(0, text.lastIndexOf(' '));
              }
            }
            if ((orderStatus == null) || (orderStatus.trim().isEmpty()))  {
              orderStatus = getStringResource(securityContext.getDivision(), 
                  "orderStatusOther", encounterInfo.getPatientLanguage());
            } else if (orderStatus.equals("RENEW")) {
              orderStatus = getStringResource(securityContext.getDivision(), 
                  "orderStatusRenewed", encounterInfo.getPatientLanguage());
            } else if (orderStatus.startsWith("REFILL")) {
              orderStatus = getStringResource(securityContext.getDivision(), 
                  "orderStatusRefilled", encounterInfo.getPatientLanguage());
            } else if (orderStatus.startsWith("DISCONTINUED")) {
              orderStatus = getStringResource(securityContext.getDivision(), 
                  "orderStatusDiscontinued", encounterInfo.getPatientLanguage());
            } else if (orderStatus.equals("SUSPENDED")) {
              orderStatus = null; // don't include 
            } else if (orderStatus.startsWith("NEW ORDER")) {
              orderStatus = getStringResource(securityContext.getDivision(), 
                  "orderStatusNew", encounterInfo.getPatientLanguage());
            }
            description = new OrderDescription(orderStatus, text);
          }
          break;

        case LAB:
          if (orderInfo.getStatus().equalsIgnoreCase("active") || 
              orderInfo.getStatus().equalsIgnoreCase("pending")) {
            try {
              Double fmDate = FMDateUtils.dateTimeToFMDateTime(
                  DateUtils.toDate(orderInfo.getStartTimeStr(), 
                  DateUtils.ENGLISH_SHORT_DATE_TIME_FORMAT2));
              fmDateStr = String.valueOf(fmDate);
              fmDateStr = StringUtils.piece(fmDateStr, '.', 1);
            } catch(Exception e) {
              fmDateStr = getStringResource(securityContext.getDivision(), 
                  "unspecifiedDateTime", encounterInfo.getPatientLanguage());
            }
          } else {
            fmDateStr = null;
          }
          
          text = orderInfo.getText();
          for (String s : LAB_EXCLUDE_START_LIST) {
             if (text.toUpperCase().startsWith(s)) {
                text = text.substring(s.length()-1);
              }
          }
          for (String s : LAB_EXCLUDE_INDEX_LIST) {
            int index = text.toUpperCase().indexOf(s);
            if (index > 0) {
               text = text.substring(0, index);
             }
          }             
          description = new OrderDescription(fmDateStr, text);
          break;
          
        case IMAGING:
          text = orderInfo.getText();
          try {
            if (orderInfo.getStartTimeStr() != null) {
              fmDateStr = String.valueOf(FMDateUtils.dateTimeToFMDateTime(DateUtils.toDate(orderInfo.getStartTimeStr(), 
                  DateUtils.ENGLISH_SHORT_DATE_TIME_FORMAT2)));
              fmDateStr = StringUtils.piece(fmDateStr, '.', 1);
            } else {
              fmDateStr = getStringResource(securityContext.getDivision(), 
                  "unspecifiedDateTime", encounterInfo.getPatientLanguage());
            }
          } catch(Exception e) {}
          description = new OrderDescription(fmDateStr, text);
          break;
          
        case CONSULT:
          if (orderInfo.getText() != null) {
            text = orderInfo.getText().replace("Consultant's Choice", "")
                .replace("Cons ", "")
                .replace("*unsigned*", "");
            description = new OrderDescription("", text);            
          }
          break;

        case OTHER:
        default:
          if (orderInfo.getText() != null) {
            String sortKey = null;
            if (!orderInfo.getText().toUpperCase().startsWith("NON-VA")) {
              text = orderInfo.getText().replace(">> ", "")
                  .replace("RTC", getStringResource(securityContext.getDivision(), 
                  "returnToClinic", encounterInfo.getPatientLanguage()))
                       .replace("Consultant's Choice", "")
                       .replace("Cons ", "")
                       .replace("*unsigned*", "");
              sortKey = "";
            }
            description = new OrderDescription(sortKey, text);
          }
  
      }
    }

    if (description == null) {
      String[] lines = orderInfo.getText().split("[\n\r]");
      text = lines[0];
      try {
        if (orderInfo.getStartTimeStr() != null) {
          fmDateStr = String.valueOf(FMDateUtils.dateTimeToFMDateTime(DateUtils.toDate(orderInfo.getStartTimeStr(), 
              DateUtils.ENGLISH_SHORT_DATE_TIME_FORMAT2)));
        } else {
          fmDateStr = "";
        }    
      } catch(Exception e) {}
      description = new OrderDescription(fmDateStr, text);    
    } else if (description.getSortKey() == null) {
      description = null;
    }

    return description;
  }
  
  // return value: status^NDC
  private String getMedOrderDetails(ISecurityContext securityContext, String orderNum, EncounterInfo encounterInfo) {
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(encounterInfo);
    
    // first check pending outpatient orders file (#52.41) to see if med order is there
    CollectionServiceResponse<String> csr = ddrRPCs.execDdrLister(ddrSecCtx, "52.41", null, ".01;2;2E;13;22", "IP", 10, 
                                                 AvsUtils.adjustForNumericSearch(orderNum), orderNum, "B", null, null, null, null, null);
    List<String> list = (List<String>)csr.getCollection();
    if (list.size() > 0) {
      // find the exact match
      for (String result : list) {
        String placerOrderNum = StringUtils.piece(result, 2);   
        if (placerOrderNum.equals(orderNum)) {
          // return the file #^4th piece (order type in external form)^blank piece (no NDC available in this file)^ 
          // 5th piece (total # refills)^7th piece (reason)
          return "52.41^" + StringUtils.piece(list.get(0), 4) + "^^" + 
            StringUtils.piece(list.get(0), 5) + "^" + StringUtils.piece(list.get(0), 6);
        }
      }
    } else {
      // otherwise check prescription file (#52) to see if med order has already been processed by pharmacy
      csr = (CollectionServiceResponse<String>)MEM_CACHE.get(encounterInfo.getFacilityNo() + "^" + orderNum, "medOrderDetails");
      if (csr == null) {
        csr = ddrRPCs.execDdrLister(ddrSecCtx, "52", null, ".01;39.3;100;100E;27;9;12;31;39.4;39.5", "IP", 10,
                                    AvsUtils.adjustForNumericSearch(orderNum), orderNum, "APL", null, null, null, null, null);
        MEM_CACHE.put(encounterInfo.getFacilityNo() + "^" + orderNum, "medOrderDetails", csr);
      }
      list = (List<String>)csr.getCollection();
      if (list.size() > 0) {
        // find the exact match
        for (String result : list) {
          String placerOrderNum = StringUtils.piece(result, 3);
          if (placerOrderNum.equals(orderNum)) {
            // return file #^status in external form^NDC^# refills^remarks^release date/time^rx #^previous order #^forward order #
            return "52^" + StringUtils.piece(list.get(0), 5) + "^" +  StringUtils.piece(list.get(0), 6) + "^" +
            StringUtils.piece(list.get(0), 7) + "^" + StringUtils.piece(list.get(0), 8) + "^" + 
            StringUtils.piece(list.get(0), 9) + "^" + StringUtils.piece(list.get(0), 2) + "^" + 
            StringUtils.piece(list.get(0), 10) + "^" + StringUtils.piece(list.get(0), 11);
          }
        }
      }
    }
    
    return null;
  }
  
  public CollectionServiceResponse<LinkedHashMap<Double, LinkedHashMap<Double, Appointment>>> 
      getAppointments(ISecurityContext securityContext, EncounterInfo encounterInfo, Integer months) {
    
    double fmdatetime = miscRpcs.fmNow(securityContext).getPayload();
    // find earliest encounter date
    List<Encounter> enc = encounterInfo.getEncounterCache().getEncounters();
    if ((enc != null) && (enc.size() > 0)) {
      for (Encounter encounter : enc) {
        if (encounter.getEncounterDatetime() < fmdatetime) {
          fmdatetime = encounter.getEncounterDatetime();
        }
      }
    }
    
    // Range starts with beginning of next calendar day
    GregorianCalendar calendarDate = this.getDateFloor(fmdatetime);
    calendarDate.add(Calendar.DAY_OF_MONTH, 1);
    Date fromDate = calendarDate.getTime();

    // Range ends X months after today
    calendarDate.add(Calendar.MONTH, months);
    Date throughDate = calendarDate.getTime();

    // Retrieve future appointments from appointments file
    CollectionServiceResponse<Appointment> vistaResponse = 
        (CollectionServiceResponse<Appointment>)MEM_CACHE.get(encounterInfo, "appointments"); 
    if (vistaResponse == null) {
      vistaResponse = this.patientRpcs.getAppointments(securityContext, encounterInfo.getPatientDfn(), fromDate, throughDate);
      super.checkVistaExceptions(vistaResponse);
      MEM_CACHE.put(encounterInfo, "appointments", vistaResponse);
    }
    
    // get local scheduled appointments
    List<Appointment> tempList = (List<Appointment>)vistaResponse.getCollection();
    // initialize header for local appointments
    for (Appointment appt : tempList) {
      appt.setLocation(appt.getLocation().toUpperCase());
      appt.setHeader("%FACILITY_NAME%^" + encounterInfo.getFacilityNo());
    }    
    
    // get remote scheduled appointments
    List<Appointment> remoteAppointments = (List<Appointment>)
        this.getRemoteAppointments(securityContext, encounterInfo, months).getCollection();
    for (Appointment appt : remoteAppointments) {
      appt.setLocation(appt.getLocation().toUpperCase());
    }
    tempList.addAll(remoteAppointments);
    
    List<Appointment> scheduledAppts = new ArrayList<Appointment>();
    // filter out any "ZZ" and "-X" appointments, which should not be displayed
    for (Appointment appt : tempList) {
      if (!appt.getLocation().startsWith("ZZ") && !appt.getLocation().endsWith("-X")) {
        scheduledAppts.add(appt);
      }
    }    
    
    List<Appointment> recallAppts = new ArrayList<Appointment>();
    // Check recall reminders file for future recall appointments 
    CollectionServiceResponse<String> csr = (CollectionServiceResponse<String>)MEM_CACHE.get(encounterInfo, "recallAppointments");
    if (csr == null) {
      ISecurityContext ddrSecCtx = getServiceSecurityContext(encounterInfo);
      csr = ddrRPCs.execDdrLister(ddrSecCtx, "403.5", null, ".01;3E;4E;4.5E;5", "IP", null,
          AvsUtils.adjustForNumericSearch(encounterInfo.getPatientDfn()), encounterInfo.getPatientDfn(), "B", null, null, null, null, null);
      MEM_CACHE.put(encounterInfo, "recallAppointments", csr);
    }
    List<String> reminders = (List<String>)csr.getCollection();
    for (String reminder : reminders) {
      String dfn = StringUtils.piece(reminder, 2);
      if (!dfn.equals(encounterInfo.getPatientDfn())) {
        continue;
      }
      String clinic = StringUtils.piece(reminder, 5);
      if (!clinic.startsWith("ZZ") && !clinic.endsWith("-X")) {
        Appointment appt = new Appointment();
        double dt = 0;
        try {
          dt = Double.valueOf(StringUtils.piece(reminder, 6));
          appt.setFmDatetime(dt);
        } catch(Exception e) {}
        appt.setLocation(clinic);
        appt.setHeader("%FACILITY_NAME%^" + encounterInfo.getFacilityNo() + "^recall");
        recallAppts.add(appt);
      }
    }
    
    class AppointmentComparator implements Comparator<Appointment> {
      public int compare(Appointment appt1, Appointment appt2) {
        return (int)Math.round(appt1.getFmDatetime() - appt2.getFmDatetime());
      }
    }
    
    double fmFromDate = FMDateUtils.dateTimeToFMDateTime(fromDate);
    double fmThroughDate = FMDateUtils.dateTimeToFMDateTime(throughDate);
    
    // sort appointments by date
    Comparator<Appointment> comparator = new AppointmentComparator();    
    Collections.sort(scheduledAppts, comparator);
    Collections.sort(recallAppts, comparator);

    List<LinkedHashMap<Double, LinkedHashMap<Double, Appointment>>> apptsList = 
        new ArrayList<LinkedHashMap<Double, LinkedHashMap<Double, Appointment>>>();
    for (int i = 0; i < 2; i++) {
      List<Appointment> tempAppts = null;
      if (i == 0) {
        tempAppts = scheduledAppts;
      } else {
        tempAppts = recallAppts;
      }
      LinkedHashMap<Double, LinkedHashMap<Double, Appointment>> appts = 
          new LinkedHashMap<Double, LinkedHashMap<Double, Appointment>>();
      for (Appointment appt : tempAppts) {
        double apptDateTime = appt.getFmDatetime();
        if ((apptDateTime >= fmFromDate) && (apptDateTime <= fmThroughDate)) {
          double apptDate = Math.floor(apptDateTime);
          if (!appts.containsKey(apptDate)) {
            appts.put(apptDate, new LinkedHashMap<Double, Appointment>());
          }
          appts.get(apptDate).put(apptDateTime, appt);
        }
      }
      apptsList.add(appts);
    }

    CollectionServiceResponse<LinkedHashMap<Double, LinkedHashMap<Double, Appointment>>> response = 
        new CollectionServiceResponse<LinkedHashMap<Double, LinkedHashMap<Double, Appointment>>>();
    response.setCollection(apptsList);

    return response;
  }

  
  private CollectionServiceResponse<Appointment> getRemoteAppointments(ISecurityContext securityContext, 
      EncounterInfo encounterInfo, Integer months) {
    
    double fmdatetime = miscRpcs.fmNow(securityContext).getPayload();
    // find earliest encounter date
    List<Encounter> enc = encounterInfo.getEncounterCache().getEncounters();
    if ((enc != null) && (enc.size() > 0)) {
      for (Encounter encounter : enc) {
        if (encounter.getEncounterDatetime() < fmdatetime) {
          fmdatetime = encounter.getEncounterDatetime();
        }
      }
    }
    
    // Range starts with beginning of next calendar day
    GregorianCalendar calendarDate = this.getDateFloor(fmdatetime);
    calendarDate.add(Calendar.DAY_OF_MONTH, 1);
    Date fromDate = calendarDate.getTime();

    // Range ends X months after today
    calendarDate.add(Calendar.MONTH, months);
    Date throughDate = calendarDate.getTime();

    CollectionServiceResponse<Appointment> response = new CollectionServiceResponse<Appointment>();
    List<Appointment> appointments = null;
    
    // check mongodb for cached remote appointments
    RemoteAppointmentsDao remoteAppointmentsDao = null;
    remoteAppointmentsDao = new RemoteAppointmentsDaoImpl();
    RemoteAppointments remoteAppointments = 
        remoteAppointmentsDao.findByPatient(securityContext.getDivision(), encounterInfo.getPatientDfn());
    if (remoteAppointments != null) {
      // check date stamp to see if this record is over 1 day old
      try {
        Date date = DateUtils.convertAnsiDateTimeStr(remoteAppointments.getDate());
        if (new Date().getTime() - date.getTime() < CACHE_DATA_EXPIRE) {
          appointments = new ArrayList<Appointment>();
          response.setCollection(appointments);
          if (remoteAppointments.getAppointments() == null) {
            remoteAppointments.setAppointments(new ArrayList<RemoteAppointment>());
          }
          for (RemoteAppointment remoteAppointment : remoteAppointments.getAppointments()) {
            Appointment appt = new Appointment();
            Calendar cal = new GregorianCalendar();
            cal.setTime(remoteAppointment.getDatetime());
            appt.setDatetime(cal);
            appt.setDfn(encounterInfo.getPatientDfn());
            appt.setFmDatetime(remoteAppointment.getFmdatetime());
            appt.setId(remoteAppointment.getId());
            appt.setLocation(remoteAppointment.getLocation());
            // set the facility name in the header field
            appt.setHeader(remoteAppointment.getSite() + "^" + remoteAppointment.getStationNo() + "^scheduled");
            appointments.add(appt);
          }
        } else {
          remoteAppointmentsDao.delete(remoteAppointments);
          remoteAppointments = new RemoteAppointments();
        }        
      } catch(Exception e) {
        e.printStackTrace();
      }
    } else {
      remoteAppointments = new RemoteAppointments();
    }
    
    if (appointments != null) {
      return response;
    }
    
    remoteAppointments.setPatientDfn(encounterInfo.getPatientDfn());
    remoteAppointments.setStationNo(encounterInfo.getFacilityNo());
    List<RemoteAppointment> remoteAppts = new ArrayList<RemoteAppointment>();
    appointments = new ArrayList<Appointment>();
    response.setCollection(appointments);
    
    try {
      List<RemoteStation> remoteSites = 
          (List<RemoteStation>)this.patientRpcs.getRemoteSites(securityContext, encounterInfo.getPatientDfn()).getCollection();
      if (remoteSites.size() > 0) {
        // Get remote broker rpc instance
        RpcBroker rpcBroker = null;
        try {
          try {
            remoteAppointments.setDate(DateUtils.toAnsiDateTime(new Date()));
            rpcBroker = getRpcBroker();
            if (rpcBroker == null) {
              return response;
            }
            VistaRemoteSignonRpc vistaRemoteSignonRpc = new VistaRemoteSignonRpc(rpcBroker);
            for (RemoteStation remoteSite : remoteSites) {   
              VhaSite vhaSite = vhaSitesDao.findByStationAndProtocol(String.valueOf(remoteSite.getStationNo()), "VISTA");
              if (vhaSite != null) {
                RemoteSignon remoteSignon = null;
                try {
                  // Get remote connection
                  remoteSignon = vistaRemoteSignonRpc.getRemoteConnection(vhaSite.getHost(), vhaSite.getPort());
                  
                  // Get remote patient dfn
                  String remotePatientDfn = getRemotePatientDfn(remoteSignon.getRemoteBroker(), encounterInfo.getPatientSsn());
                  if (remotePatientDfn == null) {
                    continue;
                  }
                  
                  // Retrieve future appointments from appointments file
                  AppointmentsRpc appointmentsRpc = new AppointmentsRpc(remoteSignon.getRemoteBroker());
                  appointmentsRpc.setReturnRpcResult(true);
                  AppointmentsList appointmentsList = appointmentsRpc.getAppointments(remotePatientDfn, fromDate, throughDate); 
                  gov.va.med.lom.javaBroker.rpc.patient.models.Appointment[] apptsArr = appointmentsList.getAppointments();
                  for (int i = 0; i < apptsArr.length; i++) {
                    Appointment appt = new Appointment();
                    appt.setDatetime(apptsArr[i].getDatetime());
                    appt.setDfn(apptsArr[i].getDfn());
                    appt.setFmDatetime(apptsArr[i].getFmDatetime());
                    appt.setId(apptsArr[i].getId());
                    appt.setLocation(apptsArr[i].getLocation());
                    // set the facility name in the header field
                    appt.setHeader(remoteSite.getStationName() + "^" + remoteSite.getStationNo() + "^scheduled");
                    appointments.add(appt);
                    RemoteAppointment remoteAppointment = new RemoteAppointment();
                    remoteAppointment.setDatetime(apptsArr[i].getDatetime().getTime());
                    remoteAppointment.setPatientDfn(encounterInfo.getPatientDfn());
                    remoteAppointment.setFmdatetime(apptsArr[i].getFmDatetime());
                    remoteAppointment.setId(apptsArr[i].getId());
                    remoteAppointment.setLocation(apptsArr[i].getLocation());
                    remoteAppointment.setSite(remoteSite.getStationName());
                    remoteAppointment.setStationNo(remoteSite.getStationNo());
                    remoteAppts.add(remoteAppointment);
                  }
                  
                } catch (Exception e) {
                  log.error("Error getting remote appointments from Host: " + vhaSite.getHost() + ", Port: " + vhaSite.getPort(), e);
                } 
              }
            }
            remoteAppointments.setAppointments(remoteAppts);
            remoteAppointmentsDao.save(remoteAppointments);
          } catch(Exception e) {
            log.error("Error retrieving remote meds", e);
          }
        } finally {
          try {
            rpcBroker.disconnect();
          } catch(Exception e) {}
        }
      }
    } catch(Exception e) {
      log.error("Error retrieving remote appointments", e);
    }   
    return response;
  }

  public ServiceResponse<AllergiesReactants> getAllergies(ISecurityContext securityContext, EncounterInfo encounterInfo) {

    ServiceResponse<AllergiesReactants> allergyResponse = this.patientRpcs.getAllergiesReactants(securityContext, encounterInfo.getPatientDfn());
    super.checkVistaExceptions(allergyResponse);
    AllergiesReactants container = allergyResponse.getPayload();
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(encounterInfo);
    CollectionServiceResponse<String> csr = ddrRPCs.execDdrLister(ddrSecCtx, "120.8", null, ".02;3.1;20;.01", "IP", null, 
        AvsUtils.adjustForNumericSearch(encounterInfo.getPatientDfn()), encounterInfo.getPatientDfn(), "B", null, null, null, null, null);
    HashMap<String, String> arMap = new HashMap<String, String>();
    List<String> list = (List<String>)csr.getCollection();
    for (String s : list) {
      String dfn = StringUtils.piece(s, 5);
      if (!dfn.equals(encounterInfo.getPatientDfn())) {
        continue;
      }
      arMap.put(StringUtils.piece(s, 2).toUpperCase(), StringUtils.piece(s, 3) + "^" + StringUtils.piece(s, 4));
    }
    for (AllergyReactant ar : container.getAllergiesReactants()) {
      StringBuffer sb = new StringBuffer(ar.getAllergenReactant()); 
      sb.append("^") 
      .append(encounterInfo.getFacilityName())
      .append("^")
      .append(encounterInfo.getFacilityNo());
      if (arMap.containsKey(ar.getAllergenReactant().toUpperCase())) {
        sb.append("^")
        .append(arMap.get(ar.getAllergenReactant().toUpperCase()));
      } else {
        sb.append("^^");
      }
      ar.setAllergenReactant(sb.toString());
    }
    
    ServiceResponse<AllergiesReactants> response = new ServiceResponse<AllergiesReactants>();
    response.setPayload(container);
    
    return response;
  }
  
  public ServiceResponse<AllergiesReactants> getRemoteAllergies(ISecurityContext securityContext, 
      EncounterInfo encounterInfo) {
    
    ServiceResponse<AllergiesReactants> response = new ServiceResponse<AllergiesReactants>();
    AllergiesReactants ar = null;
    // check mongodb for cached remote allergies 
    RemoteAllergiesDao remoteAllergiesDao = null;
    try {
      remoteAllergiesDao = new RemoteAllergiesDaoImpl();
      RemoteAllergies remoteAllergies = remoteAllergiesDao.findByPatient(encounterInfo.getFacilityNo(), encounterInfo.getPatientDfn());
      if (remoteAllergies != null) {
         // check date stamp to see if this record is over 1 day old
        try {
          Date date = DateUtils.convertAnsiDateTimeStr(remoteAllergies.getDate());
          if (new Date().getTime() - date.getTime() < CACHE_DATA_EXPIRE) {
            if (remoteAllergies.getAllergies() == null) {
              remoteAllergies.setAllergies(new ArrayList<RemoteAllergy>());
            }
            ar = new AllergiesReactants();
            response.setPayload(ar);
            ar.setNoAllergyAssessment(remoteAllergies.isNoAllergyAssessment());
            ar.setNoKnownAllergies(remoteAllergies.isNoKnownAllergies());
            List<AllergyReactant> arList = new ArrayList<AllergyReactant>();
            ar.setAllergiesReactants(arList);
            for (RemoteAllergy remoteAllergy : remoteAllergies.getAllergies()) {
              AllergyReactant aRec = new AllergyReactant();
              arList.add(aRec);
              aRec.setAllergenReactant(remoteAllergy.getAllergenReactant() + "^" + remoteAllergy.getSite() + "^^" + remoteAllergy.getType());
              aRec.setSeverity(remoteAllergy.getSeverity());
              String[] arr = new String[remoteAllergy.getReactionsSymptoms().size()];
              remoteAllergy.getReactionsSymptoms().toArray(arr);
              aRec.setReactionsSymptoms(arr);
            }
          } else {
            remoteAllergiesDao.delete(remoteAllergies);
            remoteAllergies = new RemoteAllergies();
          }
        } catch(Exception e) {
          e.printStackTrace();
        }
      } else {
        remoteAllergies = new RemoteAllergies();
      }
      
      remoteAllergies.setStationNo(encounterInfo.getFacilityNo());
      remoteAllergies.setPatientDfn(encounterInfo.getPatientDfn());
      
      if (ar != null) {
        return response;
      }
      ar = new AllergiesReactants();
      response.setPayload(ar);
      List<RemoteAllergy> raList = new ArrayList<RemoteAllergy>();
      ar.setNoAllergyAssessment(true);
      ar.setNoKnownAllergies(true);
      
      List<RemoteStation> remoteSites = 
          (List<RemoteStation>)this.patientRpcs.getRemoteSites(securityContext, encounterInfo.getPatientDfn()).getCollection();
      if (remoteSites.size() > 0) {
        // Get remote broker rpc instance
        RpcBroker rpcBroker = null;
        try {
          try {
            remoteAllergies.setDate(DateUtils.toAnsiDateTime(new Date()));
            rpcBroker = getRpcBroker();
            if (rpcBroker == null) {
              return response;
            }
            VistaRemoteSignonRpc vistaRemoteSignonRpc = new VistaRemoteSignonRpc(rpcBroker);
            for (RemoteStation remoteSite : remoteSites) {
              if (!remoteSite.getStationNo().equals(securityContext.getDivision()) && 
                  !remoteSite.getStationNo().equals("-1")) {
                VhaSite vhaSite = vhaSitesDao.findByStationAndProtocol(String.valueOf(remoteSite.getStationNo()), "VISTA");
                if (vhaSite != null) {
                  RemoteSignon remoteSignon = null;
                  try {
                    // Get remote connection
                    remoteSignon = vistaRemoteSignonRpc.getRemoteConnection(vhaSite.getHost(), vhaSite.getPort());
                    
                    // Get remote patient dfn
                    String remotePatientDfn = getRemotePatientDfn(remoteSignon.getRemoteBroker(), encounterInfo.getPatientSsn());
                    if (remotePatientDfn == null) {
                      continue;
                    }
                    
                    // Get type for each allergy
                    HashMap<String, String> arMap = new HashMap<String, String>();
                    DdrLister query = new DdrLister(remoteSignon.getRemoteBroker());
                    query.setFile("120.8");
                    query.setFields(".02;3.1;20;.01");
                    query.setFlags("IP");
                    query.setXref("B");
                    query.setFrom(AvsUtils.adjustForNumericSearch(remotePatientDfn));
                    query.setPart(remotePatientDfn);    
                    List<String> results = query.execute();
                    for (String s : results) {
                      String dfn = StringUtils.piece(s, 5);
                      if (!dfn.equals(encounterInfo.getPatientDfn())) {
                        continue;
                      }
                      arMap.put(StringUtils.piece(s, 2).toUpperCase(), StringUtils.piece(s, 3) + "^" + StringUtils.piece(s, 4));
                    }
                    
                    // Get outpatient meds report
                    AllergiesReactionsRpc allergiesReactionRpc = new AllergiesReactionsRpc(remoteSignon.getRemoteBroker());
                    if (allergiesReactionRpc != null) {
                      allergiesReactionRpc.setReturnRpcResult(false);
                      AllergiesReactions allergiesReactions = allergiesReactionRpc.getAllergiesReactions(remotePatientDfn);
                      ar.setNoAllergyAssessment(ar.getNoAllergyAssessment() && allergiesReactions.getNoAllergyAssessment());
                      ar.setNoKnownAllergies(ar.getNoKnownAllergies() && allergiesReactions.getNoKnownAllergies());
                      AllergyReaction[] allergiesReactantsArray = allergiesReactions.getAllergiesReactions();
                      List<AllergyReactant> arList = new ArrayList<AllergyReactant>();
                      for (int i = 0; i < allergiesReactantsArray.length; i++) {
                        String type = null;
                        String verifiedDate = null;
                        if (arMap.containsKey(allergiesReactantsArray[i].getAllergy().toUpperCase())) {
                          String s = arMap.get(allergiesReactantsArray[i].getAllergy().toUpperCase());
                          type = StringUtils.piece(s, 1);
                          verifiedDate = StringUtils.piece(s, 2);
                        } else {
                          type = "";
                          verifiedDate = "";
                        }
                        AllergyReactant allergyReactant = new AllergyReactant();
                        allergyReactant.setAllergenReactant(allergiesReactantsArray[i].getAllergy() + "^" +
                            remoteSite.getStationName() + "^" + remoteSite.getStationNo() + "^" + type + "^" + verifiedDate);
                        allergyReactant.setReactionsSymptoms(allergiesReactantsArray[i].getReactionsSymptoms());
                        arList.add(allergyReactant);
                        RemoteAllergy remoteAllergy = new RemoteAllergy();
                        raList.add(remoteAllergy);
                        remoteAllergy.setAllergenReactant(allergiesReactantsArray[i].getAllergy());
                        remoteAllergy.setSite(remoteSite.getStationName());
                        remoteAllergy.setStationNo(remoteSite.getStationNo());
                        remoteAllergy.setSeverity(allergiesReactantsArray[i].getSeverity());
                        remoteAllergy.setReactionsSymptoms(Arrays.asList(allergiesReactantsArray[i].getReactionsSymptoms()));
                      }
                      ar.setAllergiesReactants(arList);
                    }
    
                  } catch (Exception e) {
                    log.error("Error getting remote allergies from Host: " + vhaSite.getHost() + ", Port: " + vhaSite.getPort(), e);
                  }
                }
              }
            }
            remoteAllergies.setNoKnownAllergies(ar.getNoAllergyAssessment());
            remoteAllergies.setNoAllergyAssessment(ar.getNoKnownAllergies());
            remoteAllergies.setAllergies(raList); 
            remoteAllergiesDao.save(remoteAllergies);
          } catch(Exception e) {
            log.error("Error retrieving remote allergies", e);
          }
        } finally {
          try {
            rpcBroker.disconnect();
          } catch(Exception e) {}          
        }
      }
    } catch(Exception e) {
      log.error("Error retrieving remote allergies", e);
    }   
    
    return response;
    
  }  
  
  public CollectionServiceResponse<Medication> getMedications(ISecurityContext securityContext, EncounterInfo encounterInfo) {
    
    CollectionServiceResponse<Medication> vistaResponse = this.patientRpcs.getMedications(securityContext, encounterInfo.getPatientDfn());
    super.checkVistaExceptions(vistaResponse);
    List<Medication> meds = new ArrayList<Medication>(vistaResponse.getCollection());
    HashMap<String, Medication> medsMap = new HashMap<String, Medication>();
    // filter out unwanted entries and construct meds map
    for (Medication med : meds) {
      // only include active outpatient meds
      String medStatus = med.getStatus().toUpperCase();
      if ((medStatus.startsWith("ACTIVE") || medStatus.startsWith("PENDING") || 
          (encounterInfo.getDocType().equals(EncounterInfo.PVS) && medStatus.startsWith("EXPIRED"))) &&
          !med.getType().equals("NV") &&
          !med.getName().equalsIgnoreCase("No Reported Usage Miscellaneous")) {
        String orderDetails = getMedOrderDetails(securityContext, med.getOrderId(), encounterInfo);
        String ndc = null;
        String totalNumRefills = null;
        String lastReleaseDate = null;
        if (orderDetails != null) {
          String file = StringUtils.piece(orderDetails, 1);
          if (file.equals("52")) {
            ndc = StringUtils.piece(orderDetails, 3);
            totalNumRefills = StringUtils.piece(orderDetails, 4);
            String released = StringUtils.piece(orderDetails, 6);
            if (!released.isEmpty()) {
              try {
                lastReleaseDate = DateUtils.formatDate(DateUtils.fmDateTimeToDateTime(Double.valueOf(released)).getTime(), "MMMM dd, yyyy");
              } catch(Exception e) {
                lastReleaseDate = "";
              }
            } else {
              lastReleaseDate = "";
            }
          } else if (file.equals("52.41")) {
            ndc = "";
            lastReleaseDate = "";
            totalNumRefills = StringUtils.piece(orderDetails, 4);
          }          
          // since the Medication object has no field for NDC or Last Release Date or Total # Refills, 
          // add these as the 2nd, 3rd, and 4th pieces respectively of the Pharm ID
          med.setPharmId(med.getPharmId() + "^" + ndc + "^" + lastReleaseDate + "^" + totalNumRefills);
        }
        medsMap.put(med.getOrderId(), med);
      }
    }
    
    // call cover sheet RPC to identify any "Clinic Meds" (IMO)
    try {
      RpcRequest req = RpcRequestFactory.getRpcRequest();
      req.setRpcContext("OR CPRS GUI CHART");
      req.setRpcName("ORWPS COVER");
      List<String> paramList = new ArrayList<String>();
      paramList.add(encounterInfo.getPatientDfn());
      req.setParams(paramList);
      RpcResponse rpcResponse = VistaLinkUtils.call(req, securityContext.getDivision(), null, securityContext.getUserId());
      String results = rpcResponse.getResults();
      ArrayList<String> list = StringUtils.getArrayList(results);
      for (int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if (StringUtils.piece(x, 5).equals("C")) {
          String orderId = StringUtils.piece(x, 3);
          Medication med = medsMap.get(orderId);
          if (med != null) {
            med.setType("C");
          }
        }
      }
    } catch(Exception e) {
    }
    
    // add any new med orders that haven't yet been processed by pharmacy
    // to do this we'll check the pending outpatient orders file (#52.41) 
    // to see if any med order is there. We'll also need to remove any meds 
    // from the list that were changed, held, or discontinued
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(encounterInfo);
    
    CollectionServiceResponse<String> csr = ddrRPCs.execDdrLister(ddrSecCtx, "52.41", null, ".01;1;2;2E;6;11E", "IP", 10, 
                  AvsUtils.adjustForNumericSearch(encounterInfo.getPatientDfn()), encounterInfo.getPatientDfn(), "P", null, null, null, null, null);
    List<String> list = (List<String>)csr.getCollection();
    if (list.size() > 0) {
      for (String result : list) {
        // make sure it's for the same patient
        String dfn = StringUtils.piece(result, 3);   
        if (encounterInfo.getPatientDfn().equals(dfn)) {
          String orderType = StringUtils.piece(result, 4);
          String name = StringUtils.piece(result, 7);
          // check if the unprocessed med order is already in the patient's med list
          String placerOrderNum = StringUtils.piece(result, 2); 
          if (medsMap.containsKey(placerOrderNum)) {
            // remove discontinued meds
            if (orderType.equals("DE") || orderType.equals("DC")) {
              medsMap.remove(placerOrderNum);
            }
          } else {
            // add the new med to the list
            if (orderType.equalsIgnoreCase("NW")) {
              // get the sig
              String ien = StringUtils.piece(result, 1); 
              CollectionServiceResponse<String> csr2 = 
                  ddrRPCs.execDdrLister(ddrSecCtx, "52.4124", "," + ien + ",", ".01", "IP", 10, 
                      null, null, "#", null, null, null, null, null);
              List<String> list2 = (List<String>)csr2.getCollection();
              StringBuffer sig = new StringBuffer();
              for (String s : list2) {
                sig.append(StringUtils.piece(s, 2));
              }
              Medication newMed = new Medication();
              newMed.setName(name);
              newMed.setOrderId(placerOrderNum);
              newMed.setPharmId(placerOrderNum);
              newMed.setStatus("Pending");
              newMed.setSig(sig.toString());
              medsMap.put(placerOrderNum, newMed);
            }
          }
        }
      }    
    }

    // Need to also check for new orders and add these in
    List<OrderInfo> ordersList = this.getRawOrders(securityContext, encounterInfo);
    for (OrderInfo orderInfo : ordersList) {
      DGroupSeq dGroupSeq = this.getDGroupFromOrderInfo(securityContext, orderInfo);
      if (dGroupSeq.getName().equalsIgnoreCase(DGroupSeq.MED.getName())) {
        if (orderInfo.getText() != null) {
          String orderNum = StringUtils.piece(orderInfo.getId(), ';', 1);
          // check if med is already in the list
          if (medsMap.containsKey(orderNum)) {
            continue;
          }
          String orderDetails = getMedOrderDetails(securityContext, orderNum, encounterInfo);
          String orderStatus = null;
          String ndc = null;
          String lastReleaseDate = "";
          String totalNumRefills = null;
          if (orderDetails != null) {
            
            String file = StringUtils.piece(orderDetails, 1);
            if (file.equals("52")) {
              String status = StringUtils.piece(orderDetails, 2);
              if (status.equals("ACTIVE")) {
                String remarks = StringUtils.piece(orderDetails, 5);
                if (!remarks.trim().isEmpty()) {
                  orderStatus = StringUtils.piece(remarks, ' ', 1);
                } else {
                  orderStatus = status;
                }
                totalNumRefills = StringUtils.piece(orderDetails, 4);
                String released = StringUtils.piece(orderDetails, 6);
                if (!released.isEmpty()) {
                  try {
                    lastReleaseDate = DateUtils.formatDate(DateUtils.fmDateTimeToDateTime(Double.valueOf(released)).getTime(), "MMMM dd, yyyy");
                  } catch(Exception e) {
                    lastReleaseDate = "";
                  }
                } else {
                  lastReleaseDate = "";
                }                
                
              } else {
                orderStatus = status;
              }
              ndc = StringUtils.piece(orderDetails, 3);
            } else if (file.equals("52.41")) {
              String status = StringUtils.piece(orderDetails, 1);
              totalNumRefills = StringUtils.piece(orderDetails, 4);
              if (status.trim().isEmpty()) {
                String reason = StringUtils.piece(orderDetails, 5);
                if (reason.trim().isEmpty()) {
                  orderStatus = null;
                } else {
                  orderStatus = status;
                }
              } else {
                orderStatus = status;
              }
            }
            ndc = "";
          }
          if ((orderStatus != null) && (orderStatus.contains("NEW ORDER") || orderStatus.contains("RENEWED"))) {
            String[] lines = orderInfo.getText().split("[\n\r]");
            String name = lines[0];
            StringBuffer sig = new StringBuffer();
            for (int i = 1; i < lines.length; i++) {
              sig.append(lines[i]);
              if (i < lines.length-1) {
                sig.append("<br/>");
              }
            }
            Medication newMed = new Medication();
            newMed.setName(name);
            newMed.setOrderId(orderInfo.getId());
            // since the Medication object has no field for NDC, Last Release Date, and Total # Refills, 
            // add these as the 2nd, 3rd, and 4th pieces of the Pharm ID
            newMed.setPharmId(orderInfo.getId() + "^" + ndc != null ? ndc : "" + "^" + lastReleaseDate + "^" + totalNumRefills);
            newMed.setStatus("New Order");
            newMed.setSig(sig.toString());
            medsMap.put(orderInfo.getId(), newMed);
          }
        }
      }
    }
    
    meds.clear();
    meds.addAll(medsMap.values());
    
    // the Medication class doesn't have its own comparator, so we define an external one (alphabetical by name)
    class MedicationComparator implements Comparator<Medication> {
      public int compare(Medication medA, Medication medB) {
        return medA.getName().compareToIgnoreCase(medB.getName());
      }
    }
    
    // sort meds by name
    Comparator<Medication> comparator = new MedicationComparator();    
    Collections.sort(meds, comparator);

    CollectionServiceResponse<Medication> response = new CollectionServiceResponse<Medication>();
    response.setCollection(meds);

    return response;
  }
  
  public CollectionServiceResponse<MedicationRdv> getNonVAMedicationsRdv(ISecurityContext securityContext, 
      EncounterInfo encounterInfo, Date startDate) {
    
    ServiceResponse<String> response = patientRpcs.getReportText(securityContext, encounterInfo.getPatientDfn(), startDate, null, 0, 
        ReportsDao.OTHER_MEDS_REQUEST_REPORT);
    
    if (BaseServiceResponse.hasErrorMessage(response)) {
      log.error("Error getting herbal/otc/non-va medications rdv");
    }    
    
    CollectionServiceResponse<MedicationRdv> csr = new CollectionServiceResponse<MedicationRdv>();
    List<MedicationRdv> list = new ArrayList<MedicationRdv>();
    csr.setCollection(list);
    
    if (response.getPayload().equals(""))
      return csr;
    
    String[] rdvMeds = StringUtils.pieceList(response.getPayload(), '\n');
    MedicationRdv rec = null;
    StringBuffer sig = new StringBuffer();
    StringBuffer comment = new StringBuffer();
    
    for (int i = 0; i < rdvMeds.length; i++) {
      
      String rdvMed = rdvMeds[i].trim();
      
      if (rdvMed.equals(""))
        continue;
      String[] flds = StringUtils.pieceList(rdvMed, '^');
      int fldNum = Integer.valueOf(flds[0]);
      switch(fldNum) {
        
        case 1: 
          if (rec != null) {
            rec.setSig(sig.toString().trim());
            rec.setComment(comment.toString());
            rec.setType("NV");
            list.add(rec);
          }
          rec = new MedicationRdv();
          rec.setOrderId(String.valueOf(i));
          rec.setOutpatient(true);
          rec.setNonVA(true);
          sig.delete(0, sig.length());
          comment.delete(0, comment.length());
          if (flds.length == 2) {
            String[] parts = StringUtils.pieceList(flds[1], ';');
            if (parts.length == 2) {
              rec.setStationName(parts[0]);
              rec.setStationNo(parts[1]);
            }
          } else if (!flds[1].equals("")) {
            rec.setStationName(flds[1]);
            rec.setStationNo(securityContext.getDivision());
          } else if (!flds[1].equals("")) {
            rec.setStationName("");
            rec.setStationNo(securityContext.getDivision());
          }
          break;
          
        case 2: 
          if (flds.length == 2) {
            if (flds[1].equalsIgnoreCase("No Reported Usage Miscellaneous")) {
              if (rec != null) {
                list.remove(rec);
              }
              continue;
            }
            rec.setName(flds[1]);
          }
          break;
        
        case 3: 
          if (flds.length == 2) {
            rec.setStatus(flds[1]);
          }
          break;
          
        case 4: 
          if (flds.length == 2) {
            rec.setStartDate(flds[1]);
          }
          break;          
          
        case 5: 
          if (flds.length == 2) {
            rec.setDateDocumented(flds[1]);
          }
          break;          
          
        case 6: 
          if (flds.length == 2) {
            rec.setDocumentor(flds[1]);
          }
          break;          
          
        case 7: 
          if (flds.length == 2) {
            rec.setStopDate(flds[1]);
          }
          break;           
          
        case 8: 
          if (flds.length == 2) {
            sig.append(!((sig == null) || sig.equals("")) ? "\n" : "");
            sig.append(flds[1]);
          }
          break;
          
        case 10: 
          if (flds.length == 2) {
            comment.append(!((comment == null) || comment.equals("")) ? "\n" : "");
            comment.append(flds[1]);
          }
          break;       
      }
    }
    if (rec != null) {
      rec.setSig(sig.toString().trim());
      rec.setComment(comment.toString());
      rec.setType("NV");
      list.add(rec);
    }
    
    return csr;    
  }
    
  public CollectionServiceResponse<MedicationRdv> getRemoteOutpatientMedicationsRdv(
      ISecurityContext securityContext, EncounterInfo encounterInfo, Date startDate) {
    
    CollectionServiceResponse<MedicationRdv> csr = new CollectionServiceResponse<MedicationRdv>();
    List<MedicationRdv> medsList = new ArrayList<MedicationRdv>();
    csr.setCollection(medsList);
    try {
      List<RemoteStation> remoteSites = 
          (List<RemoteStation>)this.patientRpcs.getRemoteSites(securityContext, encounterInfo.getPatientDfn()).getCollection();
      if (remoteSites.size() > 0) {
        // Get remote broker rpc instance
        RpcBroker rpcBroker = null;
        try {
          try {
            rpcBroker = getRpcBroker();
            if (rpcBroker == null) {
              return csr;
            }
            VistaRemoteSignonRpc vistaRemoteSignonRpc = new VistaRemoteSignonRpc(rpcBroker);
            for (RemoteStation remoteSite : remoteSites) {
              if (!remoteSite.getStationNo().equals(securityContext.getDivision()) && 
                  !remoteSite.getStationNo().equals("-1")) {
                VhaSite vhaSite = vhaSitesDao.findByStationAndProtocol(String.valueOf(remoteSite.getStationNo()), "VISTA");
                if (vhaSite != null) {
                  RemoteSignon remoteSignon = null;
                  try {
                    // Get remote connection
                    remoteSignon = vistaRemoteSignonRpc.getRemoteConnection(vhaSite.getHost(), vhaSite.getPort());
                    
                    // Get remote patient dfn
                    String remotePatientDfn = getRemotePatientDfn(remoteSignon.getRemoteBroker(), encounterInfo.getPatientSsn());
                    if (remotePatientDfn == null) {
                      continue;
                    }
                    
                    // Get outpatient meds report
                    ReportsRpc reportsRpc = new ReportsRpc(remoteSignon.getRemoteBroker());
                    if (reportsRpc != null) {
                      reportsRpc.setReturnRpcResult(false);
                      
                      // Outpatient VA Medications
                      String report = reportsRpc.getReportsText(remotePatientDfn, null, null, 0, 
                                                                ReportsRpc.OUTPATIENT_MEDS_RDV_REPORT);
                      if (report != null) {
                        Date now = new Date();
                        List<MedicationRdv> siteMeds = parseOutpatientMedicationsRdv(remoteSite.getStationNo(), report, startDate);
                        for (MedicationRdv med : siteMeds) {
                          boolean include = med.getStatus().toLowerCase().equals("active");
                          if (!include && med.getStatus().toLowerCase().equals("expired")) {
                            try {
                              Date expireDate = DateUtils.convertEnglishDateStr(med.getExpirationDate());
                              include = now.getTime() - expireDate.getTime() < EXPIRED_REMOTE_MEDS;
                            } catch(Exception e) {
                              include = true;
                            }
                          }
                          if (include) {
                            med.setStationNo(vhaSite.getStationNo());
                            med.setStationName(remoteSite.getStationName());
                            if (med.getRxNumber() != null) {
                              String orderDetails = getRemoteMedDetails(remoteSignon.getRemoteBroker(), med.getRxNumber());
                              // status in external form^NDC^total # refills^remarks^release date/time^rx #
                              String ndc = null;
                              String lastReleaseDate = null;
                              String totalNumRefills = null;
                              if ((orderDetails != null) && !orderDetails.isEmpty()) {
                                ndc = StringUtils.piece(orderDetails, 2);
                                totalNumRefills = StringUtils.piece(orderDetails, 3);
                                String released = StringUtils.piece(orderDetails, 5);
                                if (!released.isEmpty()) {
                                  try {
                                    lastReleaseDate = DateUtils.formatDate(DateUtils.fmDateTimeToDateTime(Double.valueOf(released)).getTime(), "MMMM dd, yyyy");
                                  } catch(Exception e) {
                                    lastReleaseDate = "";
                                  }
                                } else {
                                  lastReleaseDate = "";
                                }
                              }
                              med.setComment(ndc + "^" + lastReleaseDate + "^" + totalNumRefills);
                            }
                            medsList.add(med);
                          }
                        }
                      }
                    }
                  } catch (Exception e) {
                    log.error("Error getting remote meds from Host: " + vhaSite.getHost() + ", Port: " + vhaSite.getPort(), e);
                  }
                }
              }
            }
          } catch(Exception e) {
            log.error("Error retrieving remote VA meds", e);
          }
        } finally {
          try {
            rpcBroker.disconnect();
          } catch(Exception e) {}             
        }
      }
    } catch(Exception e) {
      log.error("Error retrieving remote medications", e);
    }        
    
    return csr;
    
  }
  
  public CollectionServiceResponse<Procedure> getProcedures(ISecurityContext securityContext, EncounterInfo encounterInfo) {
    
    double fmNow = miscRpcs.fmNow(securityContext).getPayload();
    double fmStart = fmNow;
    double fmEnd = fmNow - 10000; // one year ago
    // find earliest and latest encounter date
    List<Encounter> enc = encounterInfo.getEncounterCache().getEncounters();
    if ((enc != null) && (enc.size() > 0)) {
      for (Encounter encounter : enc) {
        if (encounter.getEncounterDatetime() < fmStart) {
          fmStart = encounter.getEncounterDatetime();
        }
        if (encounter.getEncounterDatetime() > fmEnd) {
          fmEnd = encounter.getEncounterDatetime();
        }        
      }
    }     
    
    Date startDate = FMDateUtils.fmDateTimeToDate(Math.floor(fmStart));
    Date endDate =  FMDateUtils.fmDateTimeToDate(Math.floor(fmEnd) + 0.235959);
    
    ServiceResponse<String> response = patientRpcs.getReportText(securityContext, encounterInfo.getPatientDfn(), 
        startDate, endDate, 0, ReportsDao.ICD_PROCEDURES_REPORT);
    
    if (BaseServiceResponse.hasErrorMessage(response)) {
      log.error("Error getting procedures");
    }    
    
    CollectionServiceResponse<Procedure> csr = new CollectionServiceResponse<Procedure>();
    List<Procedure> list = new ArrayList<Procedure>();
    csr.setCollection(list);
    
    if (response.getPayload().equals(""))
      return csr;
    
    String[] procedures = StringUtils.pieceList(response.getPayload(), '\n');
    Procedure rec = null;
    
    for (int i = 0; i < procedures.length; i++) {
      
      String procedure = procedures[i].trim();
      
      if (procedure.equals(""))
        continue;
      String[] flds = StringUtils.pieceList(procedure, '^');
      int fldNum = Integer.valueOf(flds[0]);
      switch(fldNum) {
        
        case 1: 
          rec = new Procedure();
          rec.setStationName(StringUtils.piece(flds[1], ';', 1));
          rec.setStationNo(StringUtils.piece(flds[1], ';', 2));
          break;
          
        case 2: 
          if (flds.length == 2) {
            rec.setDate(flds[1]);
          }
          break;
        
        case 3: 
          if (flds.length == 2) {
            rec.setName(flds[1]);
          }
          break;
          
        case 4: 
          if (flds.length == 2) {
            rec.setCode(flds[1]);
          }
          break;          
      }
    }
    if (rec != null) {
      list.add(rec);
    }
    
    return csr;    
  }  
  
  private static RpcBroker getRpcBroker() throws Exception {
    
    String accessCode = StringUtils.piece(VISTA_AV, ';', 1);
    String verifyCode = StringUtils.piece(VISTA_AV, ';', 2);
    
    // Create a rpc broker instance
    RpcBroker rpcBroker = new RpcBroker(VISTA_SERVER, VISTA_PORT);
    // Signon if connected 
    if (rpcBroker.isConnected()) {
      rpcBroker.doSignon(accessCode, verifyCode);
      return rpcBroker;
    } else {
      log.error("Could not sign on to local RPC Broker server: " + VISTA_SERVER + ":" + VISTA_PORT);
    }
    return null;
  }
  
  private static String getRemotePatientDfn(RpcBroker remoteBroker, String patientSsn) throws Exception {
    String remotePatientDfn = null;
    PatientSelectionRpc patientSelectionRpc = new PatientSelectionRpc(remoteBroker); 
    patientSelectionRpc.setReturnRpcResult(false);
    PatientListItem[] patientListItems = null;
    PatientList patientList = patientSelectionRpc.listPtByFullSSN(patientSsn);
    patientListItems = patientList.getPatientListItems();
    if (patientListItems.length > 0) {
      remotePatientDfn =  patientListItems[0].getDfn();
    }
    return remotePatientDfn;
  }
  
  private static String getRemoteMedDetails(RpcBroker remoteBroker, String rxNumber) throws Exception {
    DdrLister query = new DdrLister(remoteBroker);
    query.setFile("52");
    query.setFields(".01;39.3;100;100E;27;9;12;31");
    query.setFlags("IP");
    query.setXref("B");
    query.setFrom(AvsUtils.adjustForNumericSearch(rxNumber));
    query.setPart(rxNumber);
    query.setMax("10");
    List<String> response = query.execute();   
    if (response.size() > 0) {
      // find the exact match
      for (String result : response) {
        String rxNum = StringUtils.piece(result, 2);
        if (rxNum.equals(rxNumber)) {
          // return status in external form^NDC^total # refills^remarks^release date/time^rx #
          return StringUtils.piece(response.get(0), 5) + "^" + StringUtils.piece(response.get(0), 6) + "^" +
                 StringUtils.piece(response.get(0), 7) + "^" + StringUtils.piece(response.get(0), 8) + "^" + 
                 StringUtils.piece(response.get(0), 9) + "^" + StringUtils.piece(response.get(0), 2);
        }
      }
    }
    return "";
  }  
  
  private List<MedicationRdv> parseOutpatientMedicationsRdv(String stationNo, String results, Date startDate) throws Exception {
    
    String[] rdvMeds = StringUtils.pieceList(results, '\n');
    MedicationRdv rec = null;
    StringBuffer sig = new StringBuffer();
    List<MedicationRdv> list = new ArrayList<MedicationRdv>();
    boolean includeMed = true;
    for (int i = 0; i < rdvMeds.length; i++) {
      
      String rdvMed = rdvMeds[i].trim();
      if (rdvMed.equals(""))
        continue;
      
      String[] flds = StringUtils.pieceList(rdvMed, '^');
      int fldNum = Integer.valueOf(flds[0]);
      switch(fldNum) {
        
        case 1: 
          if ((rec != null) && (includeMed)) {
            rec.setSig(sig.toString().trim());
            rec.setOutpatient(true);
            rec.setType("OP");
            list.add(rec);
          }
          includeMed = true;
          rec = new MedicationRdv();
          sig.delete(0, sig.length());
          if (flds.length == 2) {
            String[] parts = StringUtils.pieceList(flds[1], ';');
            if (parts.length == 2) {
              rec.setStationName(parts[0]);
              rec.setStationNo(parts[1]);
            }
          } else if (!flds[1].equals("")) {
            rec.setStationName(flds[1]);
            rec.setStationNo(stationNo);
          } else if (!flds[1].equals("")) {
            rec.setStationName("");
            rec.setStationNo(stationNo);
          }
          break;
          
        case 2: 
          if (flds.length == 2) {
            rec.setName(StringUtils.mixedCase(flds[1]));
          }
          break;
        
        case 3: 
          if (flds.length == 2) {
            rec.setDrugId(flds[1]);
          }
          break;
          
        case 4: 
          if (flds.length == 2) {
            rec.setRxNumber(flds[1]);
          }
          break;          
          
        case 5: 
          if (flds.length == 2) {
            rec.setStatus(StringUtils.mixedCase(flds[1]));
          }
          break;          
          
        case 6: 
          if (flds.length == 2) {
            rec.setQuantity(flds[1]);
          }
          break;          
          
        case 7: 
          if (flds.length == 2) {
            Date expDate = null;
            try {
              expDate = DateUtils.convertEnglishDateStr(flds[1]);
            } catch(Exception e) {
            }
            if ((expDate == null) || (startDate == null) || (expDate.getTime() > startDate.getTime())) {
              rec.setExpirationDate(flds[1]);
            } else {
              includeMed = false;
            }
          }
          break;           
          
        case 8: 
          if (flds.length == 2) {
            rec.setIssueDate(flds[1]);
          }
          break;
          
        case 9: 
          if (flds.length == 2) {
            rec.setLastFillDate(flds[1]);
          }
          break;       
          
        case 10: 
          if (flds.length == 2) {
            rec.setRefills(flds[1]);
          }
          break;   
          
        case 11: 
          if (flds.length == 2) {
            rec.setProvider(flds[1]);
          }
          break;          
          
        case 12: 
          if (flds.length == 2) {
            rec.setCost(flds[1]);
          }
          break;          
          
        case 14: 
          if (flds.length == 2) {
            sig.append(flds[1]); 
          }
          break;          
          
        case 15: 
          if (flds.length == 2) {
            rec.setId(StringUtils.removeAlphaChars(flds[1]));
          }
          break; 
          
        case 16: 
          if (flds.length == 2) {
            rec.setStopDate(flds[1]);
          }
          break;          
      }
    }
    if ((rec != null) && (includeMed)) {
      rec.setSig(sig.toString().trim());
      rec.setOutpatient(true);
      rec.setType("OP");
      list.add(rec);
    }
    
    return list;    
  }    
  
  public CollectionServiceResponse<MedicationRdv> getRemoteNonVaMedicationsRdv(
      ISecurityContext securityContext, EncounterInfo encounterInfo, Date startDate) {
    
    Precondition.assertNotBlank("patientDfn", encounterInfo.getPatientDfn());
    Precondition.assertNotBlank("patientSsn", encounterInfo.getPatientSsn());
    
    CollectionServiceResponse<MedicationRdv> csr = new CollectionServiceResponse<MedicationRdv>();
    List<MedicationRdv> medsList = new ArrayList<MedicationRdv>();
    csr.setCollection(medsList);
    try {
      List<RemoteStation> remoteSites = 
          (List<RemoteStation>)this.patientRpcs.getRemoteSites(securityContext, encounterInfo.getPatientDfn()).getCollection();
      if (remoteSites.size() > 0) {
        // Get remote broker rpc instance
        RpcBroker rpcBroker = null;
        try {
          try {
            rpcBroker = getRpcBroker();
            if (rpcBroker == null) {
              return csr;
            }
            VistaRemoteSignonRpc vistaRemoteSignonRpc = new VistaRemoteSignonRpc(rpcBroker);
            for (RemoteStation remoteSite : remoteSites) {
              if (!remoteSite.getStationNo().equals(securityContext.getDivision()) && 
                  !remoteSite.getStationNo().equals("-1")) {
                VhaSite vhaSite = vhaSitesDao.findByStationAndProtocol(String.valueOf(remoteSite.getStationNo()), "VISTA");
                if (vhaSite != null) {
                  RemoteSignon remoteSignon = null;
                  try {
                    // Get remote connection
                    remoteSignon = vistaRemoteSignonRpc.getRemoteConnection(vhaSite.getHost(), vhaSite.getPort());
                    
                    // Get remote patient dfn
                    String remotePatientDfn = getRemotePatientDfn(remoteSignon.getRemoteBroker(), encounterInfo.getPatientSsn());
                    if (remotePatientDfn == null) {
                      continue;
                    }
                    
                    // Get outpatient meds report
                    ReportsRpc reportsRpc = new ReportsRpc(remoteSignon.getRemoteBroker());
                    if (reportsRpc != null) {
                      reportsRpc.setReturnRpcResult(false);
                      
                      // Non-VA Medications
                      String report = reportsRpc.getReportsText(remotePatientDfn, null, null, 0, 
                          ReportsRpc.OTHER_MEDS_REQUEST_REPORT);
                      if (report != null) {
                        List<MedicationRdv> siteMeds = parseOtherMedicationsRdv(remoteSite.getStationNo(), report, startDate);
                        for (MedicationRdv med : siteMeds) {
                          medsList.add(med);
                        }
                      }
                    }
    
                  } catch (Exception e) {
                    log.error("Error getting remote meds from Host: " + vhaSite.getHost() + ", Port: " + vhaSite.getPort(), e);
                  }
                }
              }
            }
          } catch(Exception e) {
            log.error("Error retrieving remote non-va meds", e);
          }
        } finally {
          try {
            rpcBroker.disconnect();
          } catch(Exception e) {}   
        }
      }
    } catch(Exception e) {
      log.error("Error retrieving remote medications", e);
    }        
    
    return csr;
    
  }  
  
  private List<MedicationRdv> parseOtherMedicationsRdv(String stationNo, String results, Date startDate) throws Exception {
    
    String[] rdvMeds = StringUtils.pieceList(results, '\n');
    MedicationRdv rec = null;
    StringBuffer sig = new StringBuffer();
    StringBuffer comment = new StringBuffer();
    List<MedicationRdv> list = new ArrayList<MedicationRdv>();
    boolean includeMed = true;
    for (int i = 0; i < rdvMeds.length; i++) {
      
      String rdvMed = rdvMeds[i].trim();
      if (rdvMed.equals(""))
        continue;
      String[] flds = StringUtils.pieceList(rdvMed, '^');
      int fldNum = Integer.valueOf(flds[0]);
      switch(fldNum) {
        
        case 1: 
          if ((rec != null) && (includeMed)) {
            rec.setSig(sig.toString().trim());
            rec.setComment(comment.toString().trim());
            rec.setOutpatient(true);
            rec.setType("NV");
            list.add(rec);
          }
          includeMed = true;
          rec = new MedicationRdv();
          sig.delete(0, sig.length());
          comment.delete(0, comment.length());
          if (flds.length == 2) {
            String[] parts = StringUtils.pieceList(flds[1], ';');
            if (parts.length == 2) {
              rec.setStationName(parts[0]);
              rec.setStationNo(parts[1]);
            }
          } else if (!flds[1].equals("")) {
            rec.setStationName(flds[1]);
            rec.setStationNo(stationNo);
          } else if (!flds[1].equals("")) {
            rec.setStationName("");
            rec.setStationNo(stationNo);
          }
          break;      
      
        case 2: 
          if (flds.length == 2) {
            if (flds[1].equalsIgnoreCase("No Reported Usage Miscellaneous")) {
              if (rec != null) {
                list.remove(rec);
              }
              continue;
            }
            rec.setName(flds[1]);
          }
          break;          
        
        case 3: 
          if (flds.length == 2) {
            rec.setStatus(StringUtils.mixedCase(flds[1]));
          }
          break;
          
        case 4: 
          if (flds.length == 2) {
            rec.setStartDate(flds[1]);
          }
          break;          
          
        case 5: 
          if (flds.length == 2) {
            rec.setDateDocumented(StringUtils.mixedCase(flds[1]));
          }
          break;          
          
        case 6: 
          if (flds.length == 2) {
            rec.setDocumentor(flds[1]);
          }
          break;          
          
        case 7: 
          if (flds.length == 2) {
            rec.setStopDate(flds[1]);
          }
          break;            
          
        case 8: 
          if (flds.length == 2) {
            sig.append(flds[1]); 
          }
          break;           
          
        case 9: 
          break;
          
        case 10: 
          if (flds.length == 2) {
            comment.append(flds[1]); 
          }
          break;       
          
      }
    }
    if ((rec != null) && (includeMed)) {
      rec.setSig(sig.toString().trim());
      rec.setComment(comment.toString().trim());
      rec.setOutpatient(true);
      rec.setType("OP");
      list.add(rec);
    }
    
    return list;    
  }      
  
  public CollectionServiceResponse<ClinicalReminder> getClinicalReminders(ISecurityContext securityContext, EncounterInfo encounterInfo) {
    
    securityContext.setSocketTimeout(60000);
    return this.patientRpcs.getClinicalReminders(securityContext, encounterInfo.getPatientDfn());
    
  }
  
  public ServiceResponse<String> getClinicalReminderDetail(ISecurityContext securityContext, EncounterInfo encounterInfo, String ien) {
    
    securityContext.setSocketTimeout(60000);
    return this.patientRpcs.getClinicalReminderDetails(securityContext, encounterInfo.getPatientDfn(), ien);
    
  }
  
  public CollectionServiceResponse<PceData> getPceData(ISecurityContext securityContext, EncounterInfo encounterInfo) {
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(encounterInfo);
    ddrSecCtx.setSocketTimeout(60000);
    
    String encounterLocationIen = null;
    double encounterFmDatetime = miscRpcs.fmNow(securityContext).getPayload();
    
    CollectionServiceResponse<PceData> response = new CollectionServiceResponse<PceData>();
    List<PceData> pceDataList = new ArrayList<PceData>();
    response.setCollection(pceDataList);
    HashMap<String, String> dxMap = new HashMap<String, String>();
    List<Encounter> enc = encounterInfo.getEncounterCache().getEncounters();
    for (Encounter encounter : enc) {
      encounterLocationIen = encounter.getLocation().getLocationIen();
      encounterFmDatetime = encounter.getEncounterDatetime();
      CollectionServiceResponse<String> pceDataCSR = 
          this.patientRpcs.getPCEDataForNote2(securityContext, null, encounterInfo.getPatientDfn(), encounter.getVisitString());
      List<String> pceResults = (List<String>)pceDataCSR.getCollection();
      PceData pceData = new PceData();
      pceDataList.add(pceData);
      pceData.setDatetime(encounterFmDatetime);
      if (pceResults.size() > 0) {
        if (IS_DEMO && encounterInfo.getFacilityNo().equals(DEMO_STATION_NO) && encounterInfo.getPatientDfn().equals(DEMO_PT_DFN)) {
          pceData.addImmunization("FLU,3 YRS");
        }
        pceData.setVisitString(encounter.getVisitString());
        for (String s : pceResults) {
          if ((s.substring(0, 3).equals("PRV") && s.charAt(3) != '-')) {
            String providerDuz = StringUtils.piece(s, '^', 2);
            pceData.addProviderDuz(providerDuz);
            String title = null;
            String titleIen = null;
            if (!providerDuz.isEmpty()) {
              // get the user title
              title = (String)MEM_CACHE.get(securityContext.getDivision(), providerDuz, "providerTitle");
              if (title != null) {
                pceData.addProviderTitle(title);
              } else {
                try {
                  String arg = "@\"^VA(200," + providerDuz + ",0)\"";
                  String rtn = miscRpcs.getVariableValue(ddrSecCtx, arg).getPayload();
                  titleIen = StringUtils.piece(rtn, 9);
                  if (!titleIen.isEmpty()) {
                    arg = "@\"^DIC(3.1," + titleIen + ",0)\"";
                    title = miscRpcs.getVariableValue(ddrSecCtx, arg).getPayload();
                    if (title != null) {
                      title = StringUtils.piece(title, 1);
                      pceData.addProviderTitle(title);
                      MEM_CACHE.put(securityContext.getDivision(), providerDuz, "providerTitle", title);
                    } else {
                      pceData.addProviderTitle("");
                    }
                  } else {
                    pceData.addProviderTitle("");
                  }
                } catch(Exception e) {
                }
              }
            }
            pceData.addProvider(StringUtils.piece(s, '^', 5));
          } else if ((s.substring(0, 3).equals("POV") && s.charAt(3) != '-')) {
            String diagnosis = StringUtils.piece(s, '^', 4);
            diagnosis = diagnosis.replace("*", "");
            // remove the ICD-9 code and SNOMED from the diagnosis (not useful to the patient)
            String code = null;
            for (int i = 0; i < 2; i++) {
              switch(i) {
                case 0 : code = "(ICD"; break;
                case 1 : code = "(SCT";
              }
              int index = diagnosis.indexOf(code);
              if (index > 0) {
                int index1 = diagnosis.indexOf(" ", index) + 1;
                int index2 = diagnosis.indexOf(")", index1);
                if ((index1 > 0) && (index2 > 0)) {
                  diagnosis = diagnosis.substring(0, index-1);
                }
              }
            }
            String diagnosisCode = StringUtils.piece(s, '^', 2);
            if (CONFIDENTIAL_ICD9_MAP.containsKey(diagnosisCode)) {
              break;
            }
            if (!dxMap.containsKey(diagnosisCode)) {
              pceData.addDiagnosis(diagnosis);    
              pceData.addCode(diagnosisCode);
              boolean isPrimaryDx = StringUtils.strToBool(StringUtils.piece(s, '^', 5), "1");
              if (isPrimaryDx) {
                pceData.setReasonForVisit(diagnosis);
                pceData.setReasonForVisitCode(diagnosisCode);
              }
            } else {
              dxMap.put(diagnosisCode, diagnosis);
            }
          } else if ((s.substring(0, 3).equals("IMM") && s.charAt(3) != '-')) {
            pceData.addImmunization(StringUtils.piece(s, '^', 4));
          } else if ((s.length() > 7) && s.substring(0, 7).equals("VST^HL^")) {
            String clinicIen = StringUtils.piece(s, 3);
            String clinicName =  StringUtils.piece(s, 5);
            pceData.setClinicIen(clinicIen);
            pceData.setClinicName(clinicName);
            String instIen = (String)MEM_CACHE.get(securityContext.getDivision(), encounterInfo.getPatientDfn(), "instIen");
            String instName = (String)MEM_CACHE.get(securityContext.getDivision(), encounterInfo.getPatientDfn(), "instName");
            if ((instIen == null) || (instName == null)) {
              CollectionServiceResponse<String> csr = ddrRPCs.execDdrLister(ddrSecCtx, "44", null, ".01;1;3;3E", "IP", null, null, null, "#", 
                  "I + Y=" + clinicIen, null, null, null, null);
              List<String> list = (List<String>)csr.getCollection();
              if (list.size() > 0) {
                // get the clinic from the hospital location file
                String instNo = StringUtils.piece(list.get(0), 4);
                instName = StringUtils.piece(list.get(0), 5);
                // get the institution ien from the medical center division file
                csr = ddrRPCs.execDdrLister(ddrSecCtx, "40.8", null, ".07;1", "IP", null, null, null, "AD", 
                    "I $P($G(^(0)),U,7)=\"" + instNo + "\"", null, null, null, null);
                list = (List<String>)csr.getCollection();
                if (list.size() > 0) {
                  instIen = StringUtils.piece(list.get(0), 3);
                }
              }
              MEM_CACHE.put(securityContext.getDivision(), encounterInfo.getPatientDfn(), "instIen", instIen);
              MEM_CACHE.put(securityContext.getDivision(), encounterInfo.getPatientDfn(), "instName", instName);
            }
            pceData.setInstitutionIen(instIen);
            pceData.setInstitutionName(instName);
          }
        }
      } else {
        pceData.addProviderDuz(encounterInfo.getUserDuz());
        String title = (String)MEM_CACHE.get(securityContext.getDivision(), encounterInfo.getUserDuz(), "userTitle");
        if (title != null) {
          pceData.addProviderTitle(title);
          pceData.addProvider((String)MEM_CACHE.get(securityContext.getDivision(), encounterInfo.getUserDuz(), "userName"));
        } else {
          try {
            String arg = "@\"^VA(200," + encounterInfo.getUserDuz() + ",0)\"";
            String rtn = miscRpcs.getVariableValue(ddrSecCtx, arg).getPayload();
            String providerName = StringUtils.piece(rtn, 1);
            if (providerName != null) {
              pceData.addProvider(providerName);
              MEM_CACHE.put(securityContext.getDivision(), encounterInfo.getUserDuz() , "userName", providerName);
            }
            String titleIen = StringUtils.piece(rtn, 9);
            if (!titleIen.isEmpty()) {
              arg = "@\"^DIC(3.1," + titleIen + ",0)\"";
              rtn = miscRpcs.getVariableValue(ddrSecCtx, arg).getPayload();
              if (rtn != null) {
                title = StringUtils.piece(rtn, 1);
                pceData.addProviderTitle(title);
                MEM_CACHE.put(securityContext.getDivision(), encounterInfo.getUserDuz() , "userTitle", title);
              } else {
                pceData.addProviderTitle("");
              }
            } else {
              pceData.addProviderTitle("");
            }
          } catch(Exception e) {}
        }
        String clinicName = (String)MEM_CACHE.get(securityContext.getDivision(), encounterLocationIen, "clinicName");
        if (clinicName != null) {
          pceData.setClinicName(clinicName);
        } else {
          try {
            String arg = "@\"^SC(" + encounterLocationIen + ",0)\"";
            String rtn = miscRpcs.getVariableValue(ddrSecCtx, arg).getPayload();
            if (rtn != null) {
              clinicName = StringUtils.piece(rtn, 1);
              pceData.setClinicName(clinicName);
              MEM_CACHE.put(securityContext.getDivision(), encounterLocationIen, "clinicName", clinicName);
            } else {
              pceData.setClinicName("");
            }
          } catch(Exception e) {}
        }
      }
    }
    return response;
  }
  
  public ServiceResponse<String> saveHealthFactor(ISecurityContext securityContext, 
      String patientDfn, double fmDatetime, String locationIen, String noteIen, 
      String hfIen, String hfName) {
    List<String> pceList = new ArrayList<String>();
    pceList.add("HDR^0^^" + locationIen + ";" + fmDatetime + ";A");
    pceList.add("VST^DT^" + fmDatetime);
    pceList.add("VST^PT^" + patientDfn);
    pceList.add("VST^VC^E");
    pceList.add("HF+^" + hfIen + "^^" + hfName + "^@^^^^^1^");
    return this.patientRpcs.savePceData(securityContext, pceList, noteIen, locationIen);
  }
  
  public ServiceResponse<String> getLabResults(ISecurityContext securityContext, EncounterInfo encounterInfo, int daysBack) {
    return this.patientRpcs.getTestsByDate(securityContext, encounterInfo.getPatientDfn(), daysBack, null, null);
  }
  
  public CollectionServiceResponse<Problem> getPatientProblems(ISecurityContext securityContext, EncounterInfo encounterInfo) {
    return this.patientRpcs.getProblems(securityContext, encounterInfo.getPatientDfn(), "A");
  }
  
  public CollectionServiceResponse<String> getPatientTeamMembers(ISecurityContext securityContext, 
      EncounterInfo encounterInfo, String primaryCareTeam) {

    CollectionServiceResponse<String> response = new CollectionServiceResponse<String>();
    
    List<String> results = (List<String>)MEM_CACHE.get(encounterInfo, "patientTeamMembers"); 
    if (results != null) {
      response.setCollection(results);
      return response;
    }
        
    results = new ArrayList<String>();
    response.setCollection(results);
    
    // Get service account DUZ
    ISecurityContext ddrSecCtx = getServiceSecurityContext(encounterInfo);
    ddrSecCtx.setSocketTimeout(60000);
    
    // Get primary care team for patient from patient team assignment file
    CollectionServiceResponse<String> csr = ddrRPCs.execDdrLister(ddrSecCtx, "404.42", null, ".02;.03;.03E;.01", "IP", null,
        AvsUtils.adjustForNumericSearch(encounterInfo.getPatientDfn()), encounterInfo.getPatientDfn(), "B", null, null, null, null, null);
    
    List<String> list = (List<String>)csr.getCollection();
    double lastDt = 0.0;
    String team = null;
    String teamIen = null;
    for (String line : list) {
      String dfn = StringUtils.piece(line, 5);
      if (!dfn.equals(encounterInfo.getPatientDfn())) {
        continue;
      }
      team = StringUtils.piece(line, 4);
      if (primaryCareTeam != null) {
        if (team.equals(primaryCareTeam)) {
          teamIen = StringUtils.piece(line, 3);
          break;
        }
      } else {
        double dt = StringUtils.toDouble(StringUtils.piece(line, 2), 0.0);
        if (dt > lastDt) {
          teamIen = StringUtils.piece(line, 3);
          lastDt = dt;
        } else {
          team = null;
        }
      }
    }
    
    if (teamIen != null) {
      // Get list of positions for team from team positions file
      csr = ddrRPCs.execDdrLister(ddrSecCtx, "404.57", null, ".01;.01E", "IP", null,
          null, null, "C", "I $P($G(^(0)),U,2)=" + teamIen + ",+$$ACTTP^SCMCTPU(Y)=1", null, null, null, null);
      list = (List<String>)csr.getCollection();
      for (String line : list) {
        String teamPositionIen = StringUtils.piece(line, 1);
        String teamPosition = StringUtils.piece(line, 2);
        if (teamPosition.contains("CARE MANAGER") || 
            teamPosition.contains("LVN") || 
            teamPosition.contains("PHYSICIAN")) {
        
          // Get list of providers associated with the positions
          csr = ddrRPCs.execDdrLister(ddrSecCtx, "404.52", null, ".02;.03E", "IP", null,
              null, null, "B", "I $P($G(^(0)),U,1)=" + teamPositionIen, null, null, null, null);
          list = (List<String>)csr.getCollection();
          lastDt = 0.0;
          String memberName = null;
          for (String x : list) {
            double effectiveDate = StringUtils.toDouble(StringUtils.piece(x, 2), 0.0); 
            if (effectiveDate > lastDt) {
              memberName = StringUtils.piece(x, 3);
              if (!memberName.isEmpty()) {
                if (teamPosition.contains("LVN") || teamPosition.contains("PHYSICIAN")) {
                  teamPosition = StringUtils.piece(teamPosition.trim(), ' ', 1);
                }
              }
              lastDt = effectiveDate;
            } else {
              memberName = null;
            }
          }
          results.add(memberName + " - " + teamPosition);
        }  
      }
    }
    
    MEM_CACHE.put(encounterInfo, "patientTeamMembers", results);
    
    return response;
  }
  
  public CollectionServiceResponse<gov.va.med.lom.vistabroker.patient.data.Encounter> 
      getOutpatientEncounters(ISecurityContext securityContext, EncounterInfo encounterInfo) {

    List<gov.va.med.lom.vistabroker.patient.data.Encounter> encounters = 
        new ArrayList<gov.va.med.lom.vistabroker.patient.data.Encounter>();
    for (Encounter selEnc : encounterInfo.getEncounterCache().getEncounters()) {
      double fmdatetime = selEnc.getEncounterDatetime();
      GregorianCalendar dateStart = this.getDateFloor(fmdatetime);
      GregorianCalendar dateEnd = this.getDateCeil(fmdatetime);
  
      CollectionServiceResponse<EncounterAppointment> vistaResponse = this.patientRpcs
        .getOutpatientEncounters(securityContext, encounterInfo.getPatientDfn(), dateStart, dateEnd);
      super.checkVistaExceptions(vistaResponse);
      Collection<EncounterAppointment> list = vistaResponse.getCollection();
  
      
      for (EncounterAppointment encounterAppointment : list) {
        String status = encounterAppointment.getStatus();
        if (encounterInfo.getDocType().equals(EncounterInfo.PVS) ||
            (!status.equals("CANCELLED BY PATIENT")
             && !status.equals("CANCELLED BY CLINIC")
             && !status.equals("NO-SHOW")
             && !status.equals("NON-COUNT")
             && !status.equals(""))) {
          gov.va.med.lom.vistabroker.patient.data.Encounter encounter = 
              new gov.va.med.lom.vistabroker.patient.data.Encounter();
          encounter.setDfn(encounterAppointment.getDfn());
          encounter.setLocationIen(encounterAppointment.getLocationIen());
          encounter.setDatetime(encounterAppointment.getDatetime());
          encounter.setDatetimeStr(encounterAppointment.getDatetimeStr());
          encounter.setType(encounterAppointment.getType());
          ServiceResponse<gov.va.med.lom.vistabroker.patient.data.Encounter> response = 
              this.patientRpcs.getEncounterDetails(securityContext, encounter, false);
          super.checkVistaExceptions(response);
          encounter = response.getPayload();
          encounters.add(encounter);
        }
      }
    }
    
    CollectionServiceResponse<gov.va.med.lom.vistabroker.patient.data.Encounter> response = 
        new CollectionServiceResponse<gov.va.med.lom.vistabroker.patient.data.Encounter>();
    response.setCollection(encounters);

    return response;
  }
  
  public CollectionServiceResponse<VitalSignMeasurement> getVitals(ISecurityContext securityContext, 
      EncounterInfo encounterInfo) {

    double fmNow = miscRpcs.fmNow(securityContext).getPayload();
    double fmStart = fmNow;
    double fmEnd = fmNow - 10000; // one year ago
    // find earliest and latest encounter date
    List<Encounter> enc = encounterInfo.getEncounterCache().getEncounters();
    if ((enc != null) && (enc.size() > 0)) {
      for (Encounter encounter : enc) {
        if (encounter.getEncounterDatetime() < fmStart) {
          fmStart = encounter.getEncounterDatetime();
        }
        if (encounter.getEncounterDatetime() > fmEnd) {
          fmEnd = encounter.getEncounterDatetime();
        }        
      }
    }     
    
    Double startDate = Math.floor(fmStart);
    Double endDate =  Math.floor(fmEnd) + 0.235959;

    CollectionServiceResponse<VitalSignMeasurement> vitalsResponse = patientRpcs.getVitalSignsList(
        securityContext, encounterInfo.getPatientDfn(), startDate, endDate);
    super.checkVistaExceptions(vitalsResponse);
    Collection<VitalSignMeasurement> tempVitals = vitalsResponse.getCollection();
    // find latest set of vitals
    HashMap<String, VitalSignMeasurement> vitalsMap = new HashMap<String, VitalSignMeasurement>();
    for (VitalSignMeasurement vital : tempVitals) {
      if (vitalsMap.containsKey(vital.getTypeAbbr())) {
        VitalSignMeasurement vs = vitalsMap.get(vital.getTypeAbbr());
        if ((vital.getDate().getTime() - vs.getDate().getTime()) > 0) {
          vitalsMap.put(vital.getTypeAbbr(), vital);
        }
      } else {
        vitalsMap.put(vital.getTypeAbbr(), vital);
      }
    }

    CollectionServiceResponse<VitalSignMeasurement> response = new CollectionServiceResponse<VitalSignMeasurement>();
    response.setCollection(vitalsMap.values());

    return response;
  }
  
  public ServiceResponse<EncounterCacheMongo> getEncounterCacheMongoById(ObjectId id) {
    ServiceResponse<EncounterCacheMongo> sr = new ServiceResponse<EncounterCacheMongo>();
    EncounterCacheMongoDao encounterCacheDao = new EncounterCacheMongoDaoImpl();
    EncounterCacheMongo encounterCache = encounterCacheDao.get(id);
    sr.setPayload(encounterCache);
    return sr;
  }  
  
  public ServiceResponse<EncounterCacheMongo> getEncounterCacheMongo(String facilityNo, String patientDfn, 
      List<String> locationIens, List<Double> datetimes, String docType) {
    ServiceResponse<EncounterCacheMongo> sr = new ServiceResponse<EncounterCacheMongo>();
    EncounterCacheMongoDao encounterCacheDao = new EncounterCacheMongoDaoImpl();
    EncounterCacheMongo encounterCache = encounterCacheDao.find(facilityNo, patientDfn, locationIens, datetimes, docType);
    sr.setPayload(encounterCache);
    return sr;    
  }
  
  public CollectionServiceResponse<EncounterCacheMongo> getEncounterCacheMongoByDates(String facilityNo, Date startDate, Date endDate) {
    CollectionServiceResponse<EncounterCacheMongo> sr = new CollectionServiceResponse<EncounterCacheMongo>();
     EncounterCacheMongoDao encounterCacheDao = new EncounterCacheMongoDaoImpl();
    List<EncounterCacheMongo> list = encounterCacheDao.findByDates(facilityNo, startDate, endDate);
    sr.setCollection(list);
    return sr;     
  }
  
  public void saveEncounterCacheMongo(EncounterCacheMongo encounterCache) {
    EncounterCacheMongoDao encounterCacheDao = new EncounterCacheMongoDaoImpl();
    encounterCacheDao.save(encounterCache);
  }
  
  public void updateEncounterCacheMongo(EncounterCacheMongo encounterCache, String field, Object value) {
    EncounterCacheMongoDao encounterCacheDao = new EncounterCacheMongoDaoImpl();
    UpdateOperations<EncounterCacheMongo> ops = 
        encounterCacheDao.getUpdateOperations(EncounterCacheMongo.class);
    ops.set(field, value);
    encounterCacheDao.update(EncounterCacheMongo.class, encounterCache.getId(), ops);
  }
  
  public void updateEncounterCacheMongoEncounterNoteIen(EncounterCacheMongo encounterCache, String visitString, String encounterNoteIen) {
    EncounterCacheMongoDao encounterCacheDao = new EncounterCacheMongoDaoImpl();
    encounterCacheDao.updateEncounterNoteIen(encounterCache.getId(), visitString, encounterNoteIen);
  }  
  
  public void updateEncounterCacheMongoPrintStatus(EncounterCacheMongo encounterCache) {
    this.updateEncounterCacheMongo(encounterCache, "printed", encounterCache.isPrinted());
  }
  
  public void updateEncounterCacheMongoLockStatus(EncounterCacheMongo encounterCache) {
    this.updateEncounterCacheMongo(encounterCache, "locked", encounterCache.isLocked());
  }
  
  public void updateEncounterCacheMongoCustomContent(EncounterCacheMongo encounterCache) {
    this.updateEncounterCacheMongo(encounterCache, "customContent", encounterCache.getCustomContent());
  }
  
  public void updateEncounterCacheMongoInstructions(EncounterCacheMongo encounterCache) {
    this.updateEncounterCacheMongo(encounterCache, "instructions", encounterCache.getInstructions());
  }
  
  public void updateEncounterCacheMongoPdfFilename(EncounterCacheMongo encounterCache) {
    this.updateEncounterCacheMongo(encounterCache, "pdfFilename", encounterCache.getPdfFilename());
  }
  
  public void updateEncounterCacheMongoRemoteVaMedications(EncounterCacheMongo encounterCache) {
    this.updateEncounterCacheMongo(encounterCache, "remoteVaMedications", encounterCache.getRemoteVaMedications());
  }
  
  public void updateEncounterCacheMongoRemoteNonVaMedications(EncounterCacheMongo encounterCache) {
    this.updateEncounterCacheMongo(encounterCache, "remoteNonVaMedications", encounterCache.getRemoteNonVaMedications());
  }
  
  public void deleteEncounterCacheMongo(EncounterCacheMongo encounterCache) {
    EncounterCacheMongoDao encounterCacheDao = new EncounterCacheMongoDaoImpl();
    encounterCacheDao.delete(encounterCache);
  }  
  
  public CollectionServiceResponse<MedDescription> getMedicationDescriptions(List<String> medNdcs) {
    
    CollectionServiceResponse<MedDescription> csr = new CollectionServiceResponse<MedDescription>();
    List<MedDescription> results = new ArrayList<MedDescription>();
    csr.setCollection(results);
    
    String medNdc = null; 
    String shape = null;
    String color = null;
    String frontImprint = null;
    String backImprint = null;
    List<String> ndcsNotCached = new ArrayList<String>();
    
    MedDescriptionsDao medDescriptionsDao = null;
    medDescriptionsDao = new MedDescriptionsDaoImpl();
  
    for (String ndc : medNdcs) {
      MedDescription medDescription = null;
      if (ndc != null) {
        medDescription = medDescriptionsDao.findByNdc(ndc);
        if (medDescription != null) { 
          results.add(medDescription);
        } else {
          // not cached in local db so add to list that will need to be retrieved from MIL db
          ndcsNotCached.add(ndc);
        }
      }
    }
    
    if (!ndcsNotCached.isEmpty()) {

      if (dbConnectionPool == null) {
        try {
          ResourceBundle milDbBundle = ResourceBundle.getBundle("gov.va.med.lom.avs.mil");
          dbConnectionPool = new DBConnectionPool(milDbBundle.getString("mil.drivertype"), milDbBundle.getString("mil.database"), 
            milDbBundle.getString("mil.dbserver"), Integer.valueOf(milDbBundle.getString("mil.dbport")).intValue(),  
            milDbBundle.getString("mil.dbname"), milDbBundle.getString("mil.domain"), milDbBundle.getString("mil.username"), 
            milDbBundle.getString("mil.password"), milDbBundle.getString("mil.driver"), 
            DB_MIN_POOL_SIZE, DB_REAP_DELAY, DB_INACTIVE_TIMEOUT);
        } catch(Exception e) {
          log.error("Error creating SQL connection pool.", e);
          return csr;
        }
      }
      
      Connection connection = null;  
      boolean success = false;
      int numTries = 0;
      
      try {
        for (String ndc : ndcsNotCached) {
          success = false;
          numTries = 0;
          while (!success && numTries <= 3) {
            numTries++;
            PreparedStatement ps = null;
            try {
              connection = dbConnectionPool.getConnection(); // fetch a connection
              String sql = "SELECT NDC, Shape, Color, FrontImprint, BackImprint " +
                  "FROM [VHAIP_AVS].[App].[AcceptedImageMetadata] " +
                  "WHERE NDC=?";
              ps = connection.prepareStatement(sql);
            } catch(Exception e) {
              log.error("Error creating prepared statement.", e);
              ps = null;
            }
            
            if (ps == null) {
              dbConnectionPool.destroyConnection(connection);
              continue;
            }
            
            try {
              ps.setString(1, ndc);
              ResultSet rs = ps.executeQuery();
              if (rs.next()) {
                medNdc = rs.getString(1) != null ? rs.getString(1).trim() : ""; 
                shape = rs.getString(2) != null ? rs.getString(2).trim() : "";
                color = rs.getString(3) != null ? rs.getString(3).trim() : "";
                frontImprint =rs.getString(4) != null ? rs.getString(4).trim() : "";
                backImprint = rs.getString(5) != null ? rs.getString(5).trim() : "";
                MedDescription medDescription = new MedDescription();
                medDescription.setNdc(medNdc);
                medDescription.setShape(shape);
                medDescription.setColor(color);
                medDescription.setFrontImprint(frontImprint);
                medDescription.setBackImprint(backImprint);
                /*
                if (MIL_IMAGES_DIR != null) {
                  try {
                    byte[] fileBytes = rs.getBytes(6);
                    medDescription.setImageFilename(medNdc + ".jpg");
                    OutputStream targetFile=  
                      new FileOutputStream(MIL_IMAGES_DIR + "//" + medDescription.getImageFilename());
                    targetFile.write(fileBytes);
                    targetFile.close();
                  } catch(Exception e) {
                    e.printStackTrace();
                  }
                }
                */
                results.add(medDescription);
                // write to mongodb
                medDescriptionsDao.save(medDescription);
              }
              success = true;
            } catch(Exception e) {
              log.error("Error executing prepared statement.", e);
            }

          }
        }
      } finally {
        dbConnectionPool.returnConnection(connection);
      }              
    }
    return csr;
  }
  
  public ServiceResponse<String> getClinicLocationByIen(ISecurityContext securityContext, String ien, EncounterInfo encounterInfo) {
    
    ServiceResponse<String> sr = new ServiceResponse<String>();
    ISecurityContext ddrSecCtx = getServiceSecurityContext(encounterInfo);
    ddrSecCtx.setSocketTimeout(60000);
    
    CollectionServiceResponse<String> csr = ddrRPCs.execDdrLister(ddrSecCtx, "44", null, ".01;1;3;3E", "IP", null, null, null, "#", 
                                                                                    "I + Y=" + ien, null, null, null, null);
    List<String> list = (List<String>)csr.getCollection();
    if (list.size() > 0) {
      sr.setPayload(list.get(0));
    }
    return sr;
    
  }   
  
  public CollectionServiceResponse<HealthFactor> getPatientHealthFactors(ISecurityContext securityContext, EncounterInfo encounterInfo) {
    
    CollectionServiceResponse<HealthFactor> response = new CollectionServiceResponse<HealthFactor>();
    List<HealthFactor> list = new ArrayList<HealthFactor>();
    response.setCollection(list);
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(encounterInfo);
    ddrSecCtx.setSocketTimeout(60000);
    
    CollectionServiceResponse<String> csr = ddrRPCs.execDdrLister(ddrSecCtx, "9000010.23", null, ".01;.01E;.02;.02E;.03;.03E;.04;80102;81101", 
        "IP", null, AvsUtils.adjustForNumericSearch(encounterInfo.getPatientDfn()), encounterInfo.getPatientDfn(), 
        "C", "I $P(^(0),U,2)'=\"\"", null, null, null, null);
    List<String> results = (List<String>)csr.getCollection();
    for (String x : results) {
      String dfn = StringUtils.piece(x, 4);
      if (!dfn.equals(encounterInfo.getPatientDfn())) {
        continue;
      }
      HealthFactor hf = new HealthFactor();
      hf.setIen(StringUtils.piece(x, 1));
      hf.setHealthFactorIen(StringUtils.piece(x, 2));
      hf.setHealthFactorName(StringUtils.piece(x, 3));
      hf.setPatientDfn(StringUtils.piece(x, 4));
      hf.setVisitIen(StringUtils.piece(x, 6));
      hf.setVisitDate(StringUtils.piece(x, 7));
      hf.setLevelSeverity(StringUtils.piece(x, 8));
      hf.setAuditTrail(StringUtils.piece(x, 9));
      hf.setComment(StringUtils.piece(x, 10));
      list.add(hf);
    }
    
    return response;
  }
  
  public CollectionServiceResponse<FacilityHealthFactor> getFacilityHealthFactors(String facilityNo) {
    
    CollectionServiceResponse<FacilityHealthFactor> response = new CollectionServiceResponse<FacilityHealthFactor>();
    List<FacilityHealthFactor> list = this.facilityHealthFactorsDao.find(facilityNo);
    response.setCollection(list);
    
    return response;
  }
  
  public ISecurityContext getServiceSecurityContext(EncounterInfo encounterInfo) {
    
    if (!DDR_SEC_CTX_MAP.containsKey(encounterInfo.getFacilityNo())) {
      ISecurityContext secCtx = null;
      String serviceDuz = this.settingsService.getServiceDuz(encounterInfo.getFacilityNo()).getPayload();
      String facilityNo = this.settingsService.getFacilityNoForDivision(encounterInfo.getFacilityNo()).getPayload();
      String facNum = encounterInfo.getFacilityPrefs().getFacilityNo();
      facNum = String.valueOf(StringUtils.toInt(facNum, new Integer(facilityNo)));
      if (serviceDuz != null) {
        secCtx = SecurityContextFactory.createDuzSecurityContext(facNum, serviceDuz);
      } else {
        secCtx = SecurityContextFactory.createDuzSecurityContext(facNum, encounterInfo.getUserDuz());
      }
      DDR_SEC_CTX_MAP.put(encounterInfo.getFacilityNo(), secCtx);
    }
    return DDR_SEC_CTX_MAP.get(encounterInfo.getFacilityNo());
    
  }
  
  private GregorianCalendar getDateFloor(Double datetime) {

    GregorianCalendar floor = new GregorianCalendar();
    floor.setTime(FMDateUtils.fmDateTimeToDate(datetime));
    floor.set(Calendar.HOUR_OF_DAY, 0);
    floor.set(Calendar.MINUTE, 0);
    floor.set(Calendar.SECOND, 0);
    
    return floor;
  }

  private GregorianCalendar getDateCeil(Double datetime) {

    GregorianCalendar ceil = new GregorianCalendar();
    ceil.setTime(FMDateUtils.fmDateTimeToDate(datetime));
    ceil.set(Calendar.HOUR_OF_DAY, 23);
    ceil.set(Calendar.MINUTE, 59);
    ceil.set(Calendar.SECOND, 59);
    
    return ceil;
  }  
  
  private static long parseInterval(final String s) {
    final Pattern p = Pattern.compile("^0\\.(\\d{0,2})(\\d{0,2})(\\d{0,2})$");
    final Matcher m = p.matcher(s);
    if (m.matches()) {
      long hr = 0;
      if (!m.group(1).isEmpty()) {
        hr = Long.parseLong(m.group(1)) * TimeUnit.HOURS.toMillis(1);
      }
      long min = 0;
      if (!m.group(2).isEmpty()) {
        min = Long.parseLong(m.group(2)) * TimeUnit.MINUTES.toMillis(1);
      }
      long sec = 0;
      if (!m.group(3).isEmpty()) {
        sec = Long.parseLong(m.group(2)) * TimeUnit.SECONDS.toMillis(1);
      }      
      return hr + min + sec;
    } else {
      return 0;
    }
  }
  
  private static String formatInterval(final long l) {
    final long hr = TimeUnit.MILLISECONDS.toHours(l);
    final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
    return String.format("0.%02d%02d", hr, min);
  }  
  
  private static String getStringResource(String stationNo, String name, String language) {
    return StringResources.getStringResources().getStringResource(stationNo, name, language); 
  }
  
}

