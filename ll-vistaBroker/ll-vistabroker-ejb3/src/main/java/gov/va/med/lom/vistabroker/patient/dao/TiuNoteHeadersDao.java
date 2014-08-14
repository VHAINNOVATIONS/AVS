package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.QueueHandle;
import gov.va.med.lom.vistabroker.patient.data.TiuNoteHeader;
import gov.va.med.lom.vistabroker.patient.data.TiuNoteHeadersSelection;
import gov.va.med.lom.vistabroker.patient.data.UnsignedNoteHeaders;
import gov.va.med.lom.vistabroker.user.dao.VistaUserDao;
import gov.va.med.lom.vistabroker.user.data.VistaUser;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.GregorianCalendar;

public class TiuNoteHeadersDao extends BaseDao {

  // Note Classes
  public static final String CLASS_PROGRESS_NOTES = "3";
  public static final String CLASS_DISCHARGE_SUMMARIES = "244";
  public static final String CLASS_CLINICAL_PROCEDURES = "1758";
  public static final String CLASS_SURGICAL_REPORTS = "1390";
  
  // Note Status
  public static int SIGNED_DOCUMENTS_ALL = 1;           // signed documents (all)
  public static int UNSIGNED_DOCUMENTS = 2;             // unsigned documents  
  public static int UNCOSIGNED_DOCUMENTS = 3;           // uncosigned documents
  public static int SIGNED_DOCUMENTS_AUTHOR = 4;        // signed documents/author
  public static int SIGNED_DOCUMENTS_DATE_RANGE = 5;    // signed documents/date range
  public static int UNSIGNED_BY_ADDITIONAL_SIGNER = 91; // unsigned documents by expected additional signer
  
  // CONSTRUCTORS
  public TiuNoteHeadersDao() {
    super();
  }
  
  public TiuNoteHeadersDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API  
  public List<TiuNoteHeader> getSignedProgressNoteHeaders(String dfn, int limit) throws Exception {
    TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection(); 
    tiuNoteHeadersSelection.setAscending(false);
    tiuNoteHeadersSelection.setAuthorDuz(null);
    tiuNoteHeadersSelection.setBeginDate(null);
    tiuNoteHeadersSelection.setEndDate(null);
    tiuNoteHeadersSelection.setLimit(limit);
    tiuNoteHeadersSelection.setShowAddenda(true);
    tiuNoteHeadersSelection.setNoteStatus(SIGNED_DOCUMENTS_ALL);
    tiuNoteHeadersSelection.setNoteClass(CLASS_PROGRESS_NOTES);
    return getTiuNoteHeaders(dfn, tiuNoteHeadersSelection);
  }
  
  public List<TiuNoteHeader> getSignedDischargeSummaryHeaders(String dfn, int limit) throws Exception {
    TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection(); 
    tiuNoteHeadersSelection.setAscending(false);
    tiuNoteHeadersSelection.setAuthorDuz(null);
    tiuNoteHeadersSelection.setBeginDate(null);
    tiuNoteHeadersSelection.setEndDate(null);
    tiuNoteHeadersSelection.setLimit(limit);
    tiuNoteHeadersSelection.setShowAddenda(true);
    tiuNoteHeadersSelection.setNoteStatus(SIGNED_DOCUMENTS_ALL);
    tiuNoteHeadersSelection.setNoteClass(CLASS_DISCHARGE_SUMMARIES);
    return getTiuNoteHeaders(dfn, tiuNoteHeadersSelection);
  }  
  
  public List<TiuNoteHeader> getSignedTiuNoteHeaders(String dfn, String noteClass, int limit) throws Exception {
    TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection(); 
    tiuNoteHeadersSelection.setAscending(false);
    tiuNoteHeadersSelection.setAuthorDuz(null);
    tiuNoteHeadersSelection.setBeginDate(null);
    tiuNoteHeadersSelection.setEndDate(null);
    tiuNoteHeadersSelection.setLimit(limit);
    tiuNoteHeadersSelection.setShowAddenda(true);
    tiuNoteHeadersSelection.setNoteStatus(SIGNED_DOCUMENTS_ALL);
    tiuNoteHeadersSelection.setNoteClass(noteClass);
    return getTiuNoteHeaders(dfn, tiuNoteHeadersSelection);
  }    
  
  public List<TiuNoteHeader> getUnsignedTiuNoteHeadersByAuthor(String dfn, String duz, int limit) throws Exception {
    TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection(); 
    tiuNoteHeadersSelection.setAscending(false);
    tiuNoteHeadersSelection.setAuthorDuz(duz);
    tiuNoteHeadersSelection.setBeginDate(null);
    tiuNoteHeadersSelection.setEndDate(null);
    tiuNoteHeadersSelection.setLimit(limit);
    tiuNoteHeadersSelection.setShowAddenda(true);
    tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_DOCUMENTS);
    tiuNoteHeadersSelection.setNoteClass(null);
    return getTiuNoteHeaders(dfn, tiuNoteHeadersSelection);
  }   
  
  public List<TiuNoteHeader> getUncosignedTiuNoteHeadersByAuthor(String dfn, String duz, int limit) throws Exception {
    TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection(); 
    tiuNoteHeadersSelection.setAscending(false);
    tiuNoteHeadersSelection.setAuthorDuz(duz);
    tiuNoteHeadersSelection.setBeginDate(null);
    tiuNoteHeadersSelection.setEndDate(null);
    tiuNoteHeadersSelection.setLimit(limit);
    tiuNoteHeadersSelection.setShowAddenda(true);
    tiuNoteHeadersSelection.setNoteStatus(UNCOSIGNED_DOCUMENTS);
    tiuNoteHeadersSelection.setNoteClass(null);
    return getTiuNoteHeaders(dfn, tiuNoteHeadersSelection);
  }   
  
  public List<TiuNoteHeader> getAllUnsignedTiuNoteHeaders(String dfn, String duz, int limit) throws Exception {
    List<TiuNoteHeader> list1 = getUnsignedTiuNoteHeadersByAuthor(dfn, duz, limit);
    List<TiuNoteHeader> list2 = getUncosignedTiuNoteHeadersByAuthor(dfn, duz, limit);
    list1.addAll(list2);
    return list1;
  }  
  
  public List<TiuNoteHeader> getAllTiuNoteHeaders(String dfn, String duz, int limit) throws Exception {
    List<TiuNoteHeader> list1 = getUnsignedTiuNoteHeadersByAuthor(dfn, duz, limit);
    List<TiuNoteHeader> list2 = getUncosignedTiuNoteHeadersByAuthor(dfn, duz, limit);
    List<TiuNoteHeader> list3 = getSignedProgressNoteHeaders(dfn, limit);
    List<TiuNoteHeader> list4 = getSignedDischargeSummaryHeaders(dfn, limit);
    list1.addAll(list2);
    list1.addAll(list3);
    list1.addAll(list4);
    return list1;
  }
  
  public UnsignedNoteHeaders getUnsignedNoteHeaders(String authorDuz, int limit, boolean unsignedNotes, 
                                                                 boolean uncosignedNotes) throws Exception {
    TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection(); 
    tiuNoteHeadersSelection.setAscending(false);
    tiuNoteHeadersSelection.setBeginDate(null);
    tiuNoteHeadersSelection.setEndDate(null);
    tiuNoteHeadersSelection.setLimit(limit);
    tiuNoteHeadersSelection.setShowAddenda(true);
    
    UnsignedNoteHeaders unsignedNoteHeaders = new UnsignedNoteHeaders();
    List<TiuNoteHeader> list = null;
    
    /* UNSIGNED DOCUMENTS */
    if (unsignedNotes) {
      tiuNoteHeadersSelection.setAuthorDuz(authorDuz);
      // Unsigned Progress Notes    
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_PROGRESS_NOTES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedProgressNotes(list);
      // Unsigned Discharge Summaries    
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_DISCHARGE_SUMMARIES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedDischargeSummaries(list);    
      // Unsigned Clinical Procedures   
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_CLINICAL_PROCEDURES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedClinicalProcedures(list);       
      // Unsigned Surgical Reports   
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_SURGICAL_REPORTS);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedSurgicalReports(list);
      // Unsigned by Expected Additional Signer  
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_BY_ADDITIONAL_SIGNER);
      tiuNoteHeadersSelection.setNoteClass(null); // All note types are included
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedByExpectedAdditonalSignerNotes(list);
    }
    
    /* UNCOSIGNED DOCUMENTS */
    if (uncosignedNotes) {
      tiuNoteHeadersSelection.setAuthorDuz(authorDuz);
      // Uncosigned Progress Notes    
      tiuNoteHeadersSelection.setNoteStatus(UNCOSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_PROGRESS_NOTES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUncosignedProgressNotes(list);   
      // Uncosigned Discharge Summaries    
      tiuNoteHeadersSelection.setNoteStatus(UNCOSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_DISCHARGE_SUMMARIES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUncosignedDischargeSummaries(list);
      // Uncosigned Clinical Procedures   
      tiuNoteHeadersSelection.setNoteStatus(UNCOSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_CLINICAL_PROCEDURES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUncosignedClinicalProcedures(list);  
      // Unsigned Surgical Reports   
      tiuNoteHeadersSelection.setNoteStatus(UNCOSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_SURGICAL_REPORTS);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUncosignedSurgicalReports(list);     
    }
    return unsignedNoteHeaders;
  }
  
  public UnsignedNoteHeaders getAllUnsignedNoteHeaders(String authorDuz) throws Exception {
    TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection(); 
    tiuNoteHeadersSelection.setAscending(false);
    tiuNoteHeadersSelection.setBeginDate(null);
    tiuNoteHeadersSelection.setEndDate(null);
    tiuNoteHeadersSelection.setShowAddenda(true);
    
    UnsignedNoteHeaders unsignedNoteHeaders = new UnsignedNoteHeaders();
    List<TiuNoteHeader> list = null;
    
    tiuNoteHeadersSelection.setAuthorDuz(authorDuz);
    // Unsigned Progress Notes    
    list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
    unsignedNoteHeaders.setUnsignedProgressNotes(list);
    return unsignedNoteHeaders;
  }
  
  public List<TiuNoteHeader> getTiuNoteHeaders(String dfn, TiuNoteHeadersSelection tiuNoteHeadersSelection) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    if ((dfn != null) && (dfn.length() > 0)) {
      setDefaultRpcName("TIU DOCUMENTS BY CONTEXT");
    } else {
      setDefaultRpcName("ALS TIU CONTEXT");      
    }
    // Set call timeout to 1 minute
    setTimeoutForCall(60000);
    List<TiuNoteHeader> tiuNoteHeadersList = new ArrayList<TiuNoteHeader>();
    String fmDate1 = null;
    if (tiuNoteHeadersSelection.getBeginDate() != null) {
      Long d = Math.round(FMDateUtils.dateToFMDate(tiuNoteHeadersSelection.getBeginDate()));
      fmDate1 = String.valueOf(d);
    } else
      fmDate1 = "";
    String fmDate2 = null;
    if (tiuNoteHeadersSelection.getEndDate() != null) {
      Long d = Math.round(FMDateUtils.dateToFMDate(tiuNoteHeadersSelection.getEndDate()));
      fmDate2 = String.valueOf(d);
    } else
      fmDate2 = "";
    Object[] params = {String.valueOf(tiuNoteHeadersSelection.getNoteClass()), 
                       String.valueOf(tiuNoteHeadersSelection.getNoteStatus()), 
                       String.valueOf(dfn), String.valueOf(fmDate1), 
                       String.valueOf(fmDate2), String.valueOf(tiuNoteHeadersSelection.getAuthorDuz()), 
                       String.valueOf(tiuNoteHeadersSelection.getLimit()), 
                       StringUtils.boolToStr(tiuNoteHeadersSelection.getAscending(), "A", "D"), 
                       StringUtils.boolToStr(tiuNoteHeadersSelection.getShowAddenda(), "1", "0")};
    
    List<String> list = lCall(params);
    
    tiuNoteHeadersList = doGetTiuNoteHeadersList(list, tiuNoteHeadersSelection.getNoteClass(),
                                                 tiuNoteHeadersSelection.getLimit());
    return tiuNoteHeadersList;
  }
  
  /*
   * ALS QUEUE UNSIGNED-BY-SERVICE has one (optional) input parameter STR which is the string to match 
   * in the note title (leftmost substring of title).  The return parameter is a single value, the first
   * piece of which is a handle that must be argued to the second RPC.  The second piece is the task #.
   */
  public QueueHandle getUnsignedByServiceQueueHandle(String service) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ALS QUEUE UNSIGNED-BY-SERVICE");
    String[] params = {service};
    String x = sCall(params);
    QueueHandle queueHandle  = new QueueHandle();
    queueHandle.setHandle(StringUtils.piece(x, 1));
    queueHandle.setTaskNo(Integer.valueOf(StringUtils.piece(x, 2)));
    return queueHandle;
  }
  
  /*
   * ALS REPORT UNSIGNED-BY-SERVICE takes a single input parameter, which is the handle returned by the 
   * "QUEUE" remote procedure call.  It returns a global array in the same format as ALS TIU CONTEXT.
   * 
   * The QUEUE RPC queues a background task to produce the report, which is then written to an output 
   * file in the system default HFS directory (the one specified in Kernel System Parameters).  
   * The handle that is returned to the caller suffices to identify the file.  If the caller requests 
   * the report prematurely an error message should be returned by the "REPORT" RPC, either 
   * "-1^Report file not found" or "-1^Report compilation in progress".  If no results are available for 
   * a particular note title, the RPC returns the message "-1^No results matching 'XYZ' found.".
   * 
   * The QUEUE process does not write to the temporary file until it has accumulated all results in the ^TMP global.  
   * Therefore, when you check REPORT it should say "-1^Report file not found" until the job is almost complete.  
   * Once it starts writing to the temporary file, results should be available within a few additional seconds.
   * 
   * DELFLAG, if non-zero, causes the temporary HFS file to be deleted after results are copied to the return array. 
   * 
   * These RPC's use the FTG and GTF utilities in ^%ZISH.
   */
  public List<TiuNoteHeader> getUnsignedByServiceReport(String handle, boolean delete) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ALS REPORT UNSIGNED-BY-SERVICE");
    String delFlag = null;
    if (delete)
      delFlag = "1";
    else
      delFlag = "0";
    String[] params = {handle, delFlag};
    List<String> list = lCall(params);
    if (list.size() > 1)
      return doGetTiuNoteHeadersList(list, null, 0);
    else
      return null;
  }
  
  /*
   * Deletes a report file in the HFS directory.
   * Accepts one input parameter HANDLE, and returns a single value indicating success or failure. 
   */
  public boolean deleteUnsignedByServiceReport(String handle) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ALS DELETE UNSIGNED-BY-SERVICE");
    String x = sCall(handle);
    return x.equals("1");
  }
  
  private List<TiuNoteHeader> doGetTiuNoteHeadersList(List<String> list, String noteClassIen, int limit) {
    /*
     * Pieces returned:
     * 
     * IEN^TITLE^REFERENCE DATE/TIME (INT)^PATIENT NAME (LAST I/LAST 4)^AUTHOR
     * (INT;EXT)^HOSPITAL LOCATION^SIGNATURE STATUS^Visit Date/Time^
     * Discharge Date/time^Variable Pointer to Request (e.g., Consult)^# of
     * Associated Images^Subject^Has Children^IEN of Parent Document
     *
     * Note: The ALS TIU CONTEXT rpc includes a sub-piece for the fourth field
     *       which is the patient dfn (i.e. PATIENT NAME (LAST I/LAST 4)~DFN).
     *       
     * Note: The ALS REPORT UNSIGNED-BY-SERVICE rpc includes a 16th piece for the
     *       expected signer duz.
     */
    List<TiuNoteHeader> tiuNoteHeaders = new ArrayList<TiuNoteHeader>();
    int num = list.size();
    if (limit > 0) {
      if (num > limit) 
        num = limit + 1;
    }
    for(int i = 0; i < num; i++) {
      String x = list.get(i);
      if ((x.trim().length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
        String ien = StringUtils.piece(x, 1);
        String title = StringUtils.piece(x, 2);
        String refDate = null;
        try {
          refDate = FMDateUtils.fmDateTimeToEnglishDateTime(StringUtils.toDouble(StringUtils.piece(x, 3), 0));
        } catch(ParseException pe) {
          refDate = "";
        }
        String temp = StringUtils.piece(x, 4);
        String patientName = StringUtils.piece(temp, '~', 1);
        String dfn = StringUtils.piece(temp, '~', 2);
        temp = StringUtils.piece(x, 5);
        String authorDuz = StringUtils.piece(temp, ';', 1);
        String author = StringUtils.piece(temp, ';', 2);
        String hospitalLocation = StringUtils.piece(x, 6); 
        String signatureStatus = StringUtils.piece(x, 7); 
        String visitDateTime = StringUtils.piece(x, 8);
        String dischargeDateTime = StringUtils.piece(x, 9);
        String requestIen = StringUtils.piece(x, 10);
        int numAssociatedImages = StringUtils.toInt(StringUtils.piece(x, 11), 0);
        String subject = StringUtils.piece(x, 12); 
        boolean hasChildren = StringUtils.strToBool(StringUtils.piece(x, 13), new String[] {"+"});
        String parentIen = StringUtils.piece(x, 14);
        String expectedSignerDuz = StringUtils.piece(x, 16);
        TiuNoteHeader tiuNoteHeader = new TiuNoteHeader();
        
        // Call userinfo RPC if duz not available
        String duz = getUserId();
        if (duz == null) {
          VistaUserDao vistaUserDao = new VistaUserDao(this);
          try {
            VistaUser vistaUser = vistaUserDao.getVistaUser();
            duz = vistaUser.getDuz();
          } catch(Exception e) {}
        }
        tiuNoteHeader.setDuz(duz);
        tiuNoteHeader.setDfn(dfn);
        tiuNoteHeader.setIen(ien);
        tiuNoteHeader.setTitle(title);
        tiuNoteHeader.setReferenceDatetimeStr(refDate);
        try {
          tiuNoteHeader.setReferenceDatetime(DateUtils.toDate(refDate, DateUtils.ENGLISH_SHORT_DATE_TIME_FORMAT2));
        } catch(ParseException pe) {
          pe.printStackTrace();
        }
        tiuNoteHeader.setPatientName(patientName);
        tiuNoteHeader.setAuthorDuz(authorDuz);
        tiuNoteHeader.setAuthorName(author);
        tiuNoteHeader.setHospitalLocation(hospitalLocation);
        tiuNoteHeader.setSignatureStatus(signatureStatus);
        tiuNoteHeader.setVisitDatetimeStr(visitDateTime);
        try {
          GregorianCalendar gc = DateUtils.fmDateTimeToDateTime(Double.valueOf(StringUtils.piece(visitDateTime, ';', 2)));
          tiuNoteHeader.setVisitDatetime(gc.getTime());
        } catch(Exception pe) {}        
        tiuNoteHeader.setDischargeDatetimeStr(dischargeDateTime);
        try {
          GregorianCalendar gc = DateUtils.fmDateTimeToDateTime(Double.valueOf(StringUtils.piece(dischargeDateTime, ';', 2)));
          tiuNoteHeader.setDischargeDatetime(gc.getTime());          
        } catch(Exception pe) {}            
        tiuNoteHeader.setRequestIen(requestIen);
        tiuNoteHeader.setNumAssociatedImages(numAssociatedImages);
        tiuNoteHeader.setSubject(subject);
        tiuNoteHeader.setHasChildren(hasChildren);
        tiuNoteHeader.setParentIen(parentIen);
        tiuNoteHeader.setExpectedSignerDuz(expectedSignerDuz);
        tiuNoteHeader.setDocumentClassIen(noteClassIen);
        if (tiuNoteHeader.getDocumentClassIen() == null)
          tiuNoteHeader.setDocumentClass("");
        else if (tiuNoteHeader.getDocumentClassIen().equals(CLASS_PROGRESS_NOTES))
          tiuNoteHeader.setDocumentClass("Progress Note");
        else if (tiuNoteHeader.getDocumentClassIen().equals(CLASS_DISCHARGE_SUMMARIES))
          tiuNoteHeader.setDocumentClass("Discharge Summary");
        else if (tiuNoteHeader.getDocumentClassIen().equals(CLASS_CLINICAL_PROCEDURES))
          tiuNoteHeader.setDocumentClass("Clinical Procedure");
        else if (tiuNoteHeader.getDocumentClassIen().equals(CLASS_SURGICAL_REPORTS))
          tiuNoteHeader.setDocumentClass("Surgical Report");
        tiuNoteHeaders.add(tiuNoteHeader);
      }
    }
    return tiuNoteHeaders;
  }
  
}
