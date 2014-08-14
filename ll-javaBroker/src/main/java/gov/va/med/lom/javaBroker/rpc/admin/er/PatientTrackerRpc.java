package gov.va.med.lom.javaBroker.rpc.admin.er;

import java.util.ArrayList;
import java.util.Date;

import gov.va.med.lom.javaBroker.rpc.*;

import gov.va.med.lom.javaBroker.rpc.admin.er.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class PatientTrackerRpc extends AbstractRpc {
  
  // CONSTRUCTORS
  public PatientTrackerRpc() throws BrokerException {
    super();
  }
  
  public PatientTrackerRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  
  /*
   * NAME: AVJED BED CHECK                 TAG: BEDCHK
   * ROUTINE: AVJED                        RETURN VALUE TYPE: SINGLE VALUE
   */
  public synchronized String bedCheck() throws BrokerException {
    if (setContext("AVJ ED DISPLAY"))
      return sCall("AVJED BED CHECK");
    else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED DIAG DELETE                 TAG: DIAGDEL
   * ROUTINE: AVJED                          RETURN VALUE TYPE: SINGLE VALUE
   * INPUT PARAMETER: IEN                    PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: DIAGLIST               PARAMETER TYPE: LITERAL
   */
  public synchronized String deleteDiagnosis(String ien, String diagList) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {ien, diagList};
      return sCall("AVJED DIAG DELETE", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED ELASPED TIME                TAG: ETIME
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   */
  public synchronized ArrayList elapsedTime() throws BrokerException {
    if (setContext("AVJ ED DISPLAY"))
      return lCall("VJED ELASPED TIME");
    else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED FILE EDITED REC             TAG: FILEC
   * ROUTINE: AVJED                          RETURN VALUE TYPE: SINGLE VALUE
   * INPUT PARAMETER: RECORD                 PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: COMPLAINT              PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: COMMENT                PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: COMMENT                PARAMETER TYPE: LITERAL
   */
  public synchronized String fileEditedRec(String record, String complaint, 
                                           String comment1, String comment2) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {record, complaint, comment1, comment2};
      return sCall("AVJED FILE EDITED REC", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }

  /*
   * NAME: AVJED FILE NEW REC                TAG: FILENEW
   * ROUTINE: AVJED                          RETURN VALUE TYPE: SINGLE VALUE
   * INPUT PARAMETER: RECORD                 PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: COMPLAINT              PARAMETER TYPE: LITERAL
   */
  public synchronized String fileNewRec(String record, String complaint) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {record, complaint};
      return sCall("AVJED FILE NEW REC", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }

  /*
   * NAME: AVJED GET ACUITY VALUES           TAG: GETACUD
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   * INPUT PARAMETER: DIVISION               PARAMETER TYPE: LITERAL 
   */
  public synchronized ArrayList getAcuityValues(String division) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {division};
      return lCall("AVJED FILE NEW REC", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GET DELAY REASON            TAG: DELAY
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   */
  public synchronized ArrayList getDelayReason() throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      return lCall("AVJED GET DELAY REASON");
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GET DIAGNOSIS               TAG: DIAG
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   * INPUT PARAMETER: IEN                    PARAMETER TYPE: LITERAL
   */
  public synchronized ArrayList getDiagnosis(String ien) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {ien};
      return lCall("AVJED GET DIAGNOSIS", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GET DISPOSITION             TAG: DISP
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   * INPUT PARAMETER: IEN                    PARAMETER TYPE: LITERAL
   */
  public synchronized ArrayList getDisposition(String ien) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {ien};
      return lCall("AVJED GET DISPOSITION", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GET DISPOSITION LIST        TAG: DISP2
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   */
  public synchronized ArrayList getDispositionList() throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      return lCall("AVJED GET DISPOSITION LIST");
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GET MATCHING NAMES          TAG: PL
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   * INPUT PARAMETER: STRING                 PARAMETER TYPE: LITERAL
   */
  public synchronized ArrayList getMatchingNames(String str) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {str};
      return lCall("AVJED GET MATCHING NAMES", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GET ORDER INFO              TAG: ORDER
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   * INPUT PARAMETER: DFN                    PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: FILTER                 PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: GROUPS                 PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: DTFROM                 PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: DTTHRU                 PARAMETER TYPE: LITERAL
   */
  public synchronized ArrayList getOrderInfo(String dfn, String filter, String groups, 
                                             Date dateFrom, Date dateThru) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      double date1 = FMDateUtils.dateToFMDate(dateFrom);
      double date2 = FMDateUtils.dateToFMDate(dateThru);
      String[] params = {dfn, filter, groups, String.valueOf(date1), String.valueOf(date2)};
      return lCall("AVJED GET ORDER INFO", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GET PAT DATA                TAG: GETPAT
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   * INPUT PARAMETER: IEN                    PARAMETER TYPE: LITERAL
   */
  public synchronized ArrayList getPatientData(String ien) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {ien};
      return lCall("AVJED GET PAT DATA", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GET REPORT                  TAG: START
     ROUTINE: AVJEDR                         RETURN VALUE TYPE: ARRAY
     DESCRIPTION:
       The daily report for the Emergency Room Display Board - patients seen.
     INPUT PARAMETER: DIVISION               PARAMETER TYPE: LITERAL
     INPUT PARAMETER: FDATE                  PARAMETER TYPE: LITERAL
     DESCRIPTION:
       Start date of the report
     INPUT PARAMETER: TDATE                  PARAMETER TYPE: LITERAL
     DESCRIPTION:
       End date of the report
     INPUT PARAMETER: DEPT                   PARAMETER TYPE: LITERAL
     INPUT PARAMETER: REPORT                 PARAMETER TYPE: LITERAL
   */
  public synchronized ArrayList getReport(String division,Date fromDate, Date toDate, 
                                          String department, String report) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      double date1 = FMDateUtils.dateToFMDate(fromDate);
      double date2 = FMDateUtils.dateToFMDate(toDate);
      String[] params = {division, String.valueOf(date1), String.valueOf(date2), department, report};
      return lCall("AVJED GET REPORT", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GET STATUS VALUES           TAG: GETSTAT
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   */
  public synchronized ArrayList getStatusValues() throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      return lCall("AVJED GET STATUS VALUES");
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED GETALL                      TAG: GETALL
   *   ROUTINE: AVJED                        RETURN VALUE TYPE: ARRAY
   * INPUT PARAMETER: DIVISION               PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 6                REQUIRED: YES
   *  RETURN PARAMETER DESCRIPTION:
   *  An array of current patients in the ED.
   *  Patient id^Complaint^location^md^rn^Acuity^TimeIn
   */
  public synchronized ErPatient[] getAll(String division) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {division};
      ArrayList list = lCall("AVJED GETALL", params);
      ErPatient[] erPatients = new ErPatient[list.size()];
      for (int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        erPatients[i] = new ErPatient();
        erPatients[i].setPatientId(StringUtils.piece(x, 1));
        erPatients[i].setComplaint(StringUtils.piece(x, 2));
        erPatients[i].setLocation(StringUtils.piece(x, 3));
        erPatients[i].setMd(StringUtils.piece(x, 4));
        erPatients[i].setRn(StringUtils.piece(x, 5));
        try {
          erPatients[i].setTimeIn(DateUtils.convertEnglishDatetimeStr(StringUtils.piece(x, 6)));
        } catch(Exception e) {}
      }
      return null;
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED KEY CHECK                   TAG: KEYCHK
   * ROUTINE: AVJED                          RETURN VALUE TYPE: SINGLE VALUE
   * INPUT PARAMETER: DUZ                    PARAMETER TYPE: LITERAL
   */
  public synchronized String keyCheck(String duz) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {duz};
      return sCall("AVJED KEY CHECK", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED KILL DIAGNOSIS              TAG: KILLDX
   * ROUTINE: AVJED                          RETURN VALUE TYPE: SINGLE VALUE
   * INPUT PARAMETER: IEN                    PARAMETER TYPE: LITERAL
   */
  public synchronized String killDiagnosis(String ien) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {ien};
      return sCall("AVJED KILL DIAGNOSIS", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED PATIENT CHECK               TAG: PATCHK
   * ROUTINE: AVJED                          RETURN VALUE TYPE: SINGLE VALUE
   * INPUT PARAMETER: RESULT                 PARAMETER TYPE: LITERAL
   */
  public synchronized String patientCheck(String result) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {result};
      return sCall("AVJED PATIENT CHECK", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED PATIENT EDIT                TAG: PATEDCHK
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   * INPUT PARAMETER: DUZ2                   PARAMETER TYPE: LITERAL
   */
  public synchronized ArrayList patientEdit(String duz) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {duz};
      return lCall("AVJED PATIENT EDIT", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED PATIENT LOOKUP            TAG: EDPT
   * ROUTINE: AVJED                        RETURN VALUE TYPE: ARRAY
   * INPUT PARAMETER: LIT
   * INPUT PARAMETER: STRING               PARAMETER TYPE: LITERAL
   */
  public synchronized ArrayList patientLookup(String lit, String str) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {lit, str};
      return lCall("AVJED PATIENT LOOKUP", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED PATIENT NAME                TAG: PATNAME
   * ROUTINE: AVJED                          RETURN VALUE TYPE: ARRAY
   */
  public synchronized ArrayList patientName() throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      return lCall("AVJED PATIENT NAME");
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
  /*
   * NAME: AVJED SAVE DIAGNOSIS              TAG: FILEDX
   * ROUTINE: AVJED                          RETURN VALUE TYPE: SINGLE VALUE
   * INPUT PARAMETER: IEN                    PARAMETER TYPE: LITERAL
   * INPUT PARAMETER: DIAG                   PARAMETER TYPE: LITERAL
   */
  public synchronized String saveDiagnosis(String ien, String diagnosis) throws BrokerException {
    if (setContext("AVJ ED DISPLAY")) {
      String[] params = {ien, diagnosis};
      return sCall("AVJED SAVE DIAGNOSIS", params);
    } else
      throw getCreateContextException("AVJ ED DISPLAY");
  }
  
}