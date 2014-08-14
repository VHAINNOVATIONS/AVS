package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;
import java.util.Vector;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class TiuNoteHeadersRpc extends AbstractRpc {

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
  
  // FIELDS
  private TiuNoteHeadersList tiuNoteHeadersList;
  
  // CONSTRUCTORS
  public TiuNoteHeadersRpc() throws BrokerException {
    super();
  }
  
  public TiuNoteHeadersRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  public TiuNoteHeadersList getTiuNoteHeadersList() {
    return tiuNoteHeadersList;
  }
  
  // RPC API  
  public synchronized TiuNoteHeadersList getSignedProgressNoteHeaders(String dfn, int limit) throws BrokerException {
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
  
  public synchronized TiuNoteHeadersList getSignedDischargeSummaryHeaders(String dfn, int limit) throws BrokerException {
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
  
  public synchronized TiuNoteHeadersList getSignedTiuNoteHeaders(String dfn, String noteClass, int limit) throws BrokerException {
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
  
  public synchronized TiuNoteHeadersList getUnsignedTiuNoteHeadersByAuthor(String dfn, String duz, int limit) throws BrokerException {
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
  
  public synchronized TiuNoteHeadersList getUncosignedTiuNoteHeadersByAuthor(String dfn, String duz, int limit) throws BrokerException {
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
  
  public synchronized TiuNoteHeadersList getAllUnsignedTiuNoteHeaders(String dfn, String duz, int limit) throws BrokerException {
    TiuNoteHeadersList list1 = getUnsignedTiuNoteHeadersByAuthor(dfn, duz, limit);
    TiuNoteHeader[] array1 = list1.getTiuNoteHeaders();
    TiuNoteHeadersList list2 = getUncosignedTiuNoteHeadersByAuthor(dfn, duz, limit);
    TiuNoteHeader[] array2 = list2.getTiuNoteHeaders();
    TiuNoteHeader[] array = new TiuNoteHeader[array1.length + array2.length];
    int j = -1;
    for (int i = 0; i < array1.length; i++) {
      array[++j] = array1[i];
    }
    for (int i = 0; i < array2.length; i++) {
      array[++j] = array2[i];
    }  
    TiuNoteHeadersList list = new TiuNoteHeadersList();
    list.setTiuNoteHeaders(array);
    return list;
  }  
  
  public synchronized TiuNoteHeadersList getAllTiuNoteHeaders(String dfn, String duz, int limit) throws BrokerException {
    TiuNoteHeadersList list1 = getUnsignedTiuNoteHeadersByAuthor(dfn, duz, limit);
    TiuNoteHeader[] array1 = list1.getTiuNoteHeaders();
    TiuNoteHeadersList list2 = getUncosignedTiuNoteHeadersByAuthor(dfn, duz, limit);
    TiuNoteHeader[] array2 = list2.getTiuNoteHeaders();
    TiuNoteHeadersList list3 = getSignedProgressNoteHeaders(dfn, limit);
    TiuNoteHeader[] array3 = list3.getTiuNoteHeaders();
    TiuNoteHeadersList list4 = getSignedDischargeSummaryHeaders(dfn, limit);
    TiuNoteHeader[] array4 = list4.getTiuNoteHeaders();
    TiuNoteHeader[] array = new TiuNoteHeader[array1.length + array2.length + array3.length + array4.length];
    int j = -1;
    for (int i = 0; i < array1.length; i++) {
      array[++j] = array1[i];
    }
    for (int i = 0; i < array2.length; i++) {
      array[++j] = array2[i];
    }  
    for (int i = 0; i < array3.length; i++) {
      array[++j] = array3[i];
    } 
    for (int i = 0; i < array4.length; i++) {
      array[++j] = array4[i];
    }     
    TiuNoteHeadersList list = new TiuNoteHeadersList();
    list.setTiuNoteHeaders(array);
    return list;
  }
  
  public synchronized UnsignedNoteHeaders getUnsignedNoteHeaders(String authorDuz, int limit, boolean unsignedNotes, 
                                                                 boolean uncosignedNotes) throws BrokerException {
    TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection(); 
    tiuNoteHeadersSelection.setAscending(false);
    tiuNoteHeadersSelection.setBeginDate(null);
    tiuNoteHeadersSelection.setEndDate(null);
    tiuNoteHeadersSelection.setLimit(limit);
    tiuNoteHeadersSelection.setShowAddenda(true);
    
    UnsignedNoteHeaders unsignedNoteHeaders = new UnsignedNoteHeaders();
    TiuNoteHeadersList list = null;
    
    /* UNSIGNED DOCUMENTS */
    if (unsignedNotes) {
      tiuNoteHeadersSelection.setAuthorDuz(authorDuz);
      // Unsigned Progress Notes    
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_PROGRESS_NOTES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedProgressNotes(list.getTiuNoteHeaders());
      // Unsigned Discharge Summaries    
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_DISCHARGE_SUMMARIES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedDischargeSummaries(list.getTiuNoteHeaders());    
      // Unsigned Clinical Procedures   
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_CLINICAL_PROCEDURES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedClinicalProcedures(list.getTiuNoteHeaders());       
      // Unsigned Surgical Reports   
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_SURGICAL_REPORTS);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedSurgicalReports(list.getTiuNoteHeaders());
      // Unsigned by Expected Additional Signer  
      tiuNoteHeadersSelection.setNoteStatus(UNSIGNED_BY_ADDITIONAL_SIGNER);
      tiuNoteHeadersSelection.setNoteClass(null); // All note types are included
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUnsignedByExpectedAdditonalSignerNotes(list.getTiuNoteHeaders());
    }
    
    /* UNCOSIGNED DOCUMENTS */
    if (uncosignedNotes) {
      tiuNoteHeadersSelection.setAuthorDuz(authorDuz);
      // Uncosigned Progress Notes    
      tiuNoteHeadersSelection.setNoteStatus(UNCOSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_PROGRESS_NOTES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUncosignedProgressNotes(list.getTiuNoteHeaders());   
      // Uncosigned Discharge Summaries    
      tiuNoteHeadersSelection.setNoteStatus(UNCOSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_DISCHARGE_SUMMARIES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUncosignedDischargeSummaries(list.getTiuNoteHeaders());
      // Uncosigned Clinical Procedures   
      tiuNoteHeadersSelection.setNoteStatus(UNCOSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_CLINICAL_PROCEDURES);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUncosignedClinicalProcedures(list.getTiuNoteHeaders());  
      // Unsigned Surgical Reports   
      tiuNoteHeadersSelection.setNoteStatus(UNCOSIGNED_DOCUMENTS);
      tiuNoteHeadersSelection.setNoteClass(CLASS_SURGICAL_REPORTS);
      list = getTiuNoteHeaders(null, tiuNoteHeadersSelection);
      unsignedNoteHeaders.setUncosignedSurgicalReports(list.getTiuNoteHeaders());     
    }
    
    return unsignedNoteHeaders;
  }
  
  public synchronized TiuNoteHeadersList getTiuNoteHeaders(String dfn, TiuNoteHeadersSelection tiuNoteHeadersSelection) throws BrokerException {
    String context = "OR CPRS GUI CHART";
    String rpc = null;
    if (dfn != null) {
      rpc = "TIU DOCUMENTS BY CONTEXT";
    } else {
      rpc = "ALS TIU CONTEXT";      
    }
    if (setContext(context)) {
      setDfn(dfn);
      tiuNoteHeadersList = new TiuNoteHeadersList();
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
      Object[] params = {tiuNoteHeadersSelection.getNoteClass(), 
                         String.valueOf(tiuNoteHeadersSelection.getNoteStatus()), 
                         dfn, String.valueOf(fmDate1), 
                         String.valueOf(fmDate2), tiuNoteHeadersSelection.getAuthorDuz(), 
                         String.valueOf(tiuNoteHeadersSelection.getLimit()), 
                         StringUtils.boolToStr(tiuNoteHeadersSelection.getAscending(), "A", "D"), 
                         StringUtils.boolToStr(tiuNoteHeadersSelection.getShowAddenda(), "1", "0")};
      
      ArrayList list = lCall(rpc, params);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        tiuNoteHeadersList.setRpcResult(sb.toString().trim());
      }     
      tiuNoteHeadersList = doGetTiuNoteHeadersList(list, tiuNoteHeadersSelection.getNoteClass(),
                                                   tiuNoteHeadersSelection.getLimit());
      return tiuNoteHeadersList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   * ALS QUEUE UNSIGNED-BY-SERVICE has one (optional) input parameter STR which is the string to match 
   * in the note title (leftmost substring of title).  The return parameter is a single value, the first
   * piece of which is a handle that must be argued to the second RPC.  The second piece is the task #.
   */
  public synchronized QueueHandle getUnsignedByServiceQueueHandle(String service) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {service};
      String x = sCall("ALS QUEUE UNSIGNED-BY-SERVICE", params);
      QueueHandle queueHandle  = new QueueHandle();
      queueHandle.setHandle(StringUtils.piece(x, 1));
      queueHandle.setTaskNo(Integer.valueOf(StringUtils.piece(x, 2)));
      return queueHandle;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
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
  public synchronized String getUnsignedByServiceReport(String handle, boolean delete) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      tiuNoteHeadersList = new TiuNoteHeadersList();
      String delFlag = null;
      if (delete)
        delFlag = "1";
      else
        delFlag = "0";
      String[] params = {handle, delFlag};
      ArrayList list = lCall("ALS REPORT UNSIGNED-BY-SERVICE", params);
      String x = null;
      if (list.size() > 0) {
        if (!StringUtils.piece((String)list.get(0), '^', 1).equals("-1")) {
          StringBuffer sb = new StringBuffer();
          for(int i = 0; i < list.size(); i++)
            sb.append((String)list.get(i) + "\n");
          x = sb.toString();
          if (returnRpcResult)  
            tiuNoteHeadersList.setRpcResult(sb.toString().trim());
          tiuNoteHeadersList = doGetTiuNoteHeadersList(list, null, 0);
        } else
          x = (String)list.get(0);
      }
      return x;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   * Deletes a report file in the HFS directory.
   * Accepts one input parameter HANDLE, and returns a single value indicating success or failure. 
   */
  public synchronized boolean deleteUnsignedByServiceReport(String handle) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {handle};
      String x = sCall("ALS DELETE UNSIGNED-BY-SERVICE", params);
      return x.equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  private TiuNoteHeadersList doGetTiuNoteHeadersList(ArrayList list, String noteClassIen, int limit) {
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
    Vector headersVect = new Vector();
    int num = list.size();
    if (limit > 0) {
      if (num > limit) 
        num = limit + 1;
    }
    for(int i = 0; i < num; i++) {
      String x = (String)list.get(i);
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
        if (dfn == null)
          dfn = StringUtils.piece(temp, '~', 2);
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
        if (returnRpcResult)
          tiuNoteHeader.setRpcResult(x); 
        tiuNoteHeader.setDuz(rpcBroker.getUserDuz());
        tiuNoteHeader.setDfn(dfn);
        tiuNoteHeader.setIen(ien);
        tiuNoteHeader.setTitle(title);
        tiuNoteHeader.setReferenceDatetimeStr(refDate);
        try {
          tiuNoteHeader.setReferenceDatetime(DateUtils.toDate(refDate, DateUtils.ENGLISH_SHORT_DATE_TIME_FORMAT2));
        } catch(ParseException pe) {}
        tiuNoteHeader.setPatientName(patientName);
        tiuNoteHeader.setAuthorDuz(authorDuz);
        tiuNoteHeader.setAuthorName(author);
        tiuNoteHeader.setHospitalLocation(hospitalLocation);
        tiuNoteHeader.setSignatureStatus(signatureStatus);
        tiuNoteHeader.setVisitDatetimeStr(visitDateTime);
        try {
          tiuNoteHeader.setVisitDatetime(DateUtils.toDate(visitDateTime, DateUtils.ENGLISH_DATE_TIME_FORMAT2));
        } catch(ParseException pe) {}        
        tiuNoteHeader.setDischargeDatetimeStr(dischargeDateTime);
        try {
          tiuNoteHeader.setDischargeDatetime(DateUtils.toDate(dischargeDateTime, DateUtils.ENGLISH_DATE_TIME_FORMAT2));
        } catch(ParseException pe) {}            
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
        headersVect.add(tiuNoteHeader);
      }
    }
    TiuNoteHeader[] tiuNoteHeaders = new TiuNoteHeader[headersVect.size()];
    for (int i = 0; i < headersVect.size(); i++)
      tiuNoteHeaders[i] = (TiuNoteHeader)headersVect.get(i);
    tiuNoteHeadersList.setTiuNoteHeaders(tiuNoteHeaders);
    return tiuNoteHeadersList;
  }
  
}
