package gov.va.med.lom.vistabroker.service;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.vistabroker.patient.data.*;
import gov.va.med.lom.vistabroker.security.ISecurityContext;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface PatientVBService extends Service {
  
  public abstract ServiceResponse<Address> getAddress(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<Admission> getAdmissions(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<AllergiesReactants> getAllergiesReactants(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<String> getAllergyDetail(ISecurityContext securityContext, String dfn, String ien);
  public abstract CollectionServiceResponse<Appointment> getAppointments(ISecurityContext securityContext, String dfn, Date fromDate, Date throughDate);
  public abstract CollectionServiceResponse<String> getVisitDetail(ISecurityContext securityContext, String dfn, String id);
  public abstract ServiceResponse<CancelledAppointment> cancelAppointment(ISecurityContext securityContext, String dfn, double apptDatetime, String clinic);
  public abstract ServiceResponse<ClinicalProcedure> getClinicalProcedure(ISecurityContext securityContext, String orderEntryNumber);
  public abstract ServiceResponse<String> closeCpNote(ISecurityContext securityContext, String tiuNoteIen);
  public abstract CollectionServiceResponse<ClinicalReminder> getClinicalReminders(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<String> getClinicalReminderDetails(ISecurityContext securityContext, String dfn, String ien);
  public abstract CollectionServiceResponse<String> getCombatVetInfo(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<ConsultRequest> getDefaultConsultRequests(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<ConsultRequest> getConsultRequests(ISecurityContext securityContext, String dfn, ConsultRequestsSelection consultRequestsSelection);
  public abstract ServiceResponse<String> getConsultDetail(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<String> getMedResults(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<ConsultRec> getConsultRec(ISecurityContext securityContext, String ien, boolean showAddenda);
  public abstract ServiceResponse<Consult> findConsult(ISecurityContext securityContext, String consultIen);
  public abstract ServiceResponse<String> getConsultOrderIen(ISecurityContext securityContext, String consultIen);
  public abstract ServiceResponse<String> getProcedureIen(ISecurityContext securityContext, String orderIen);
  public abstract ServiceResponse<String> getCPRequests(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<String> denyConsult(ISecurityContext securityContext, String orderIen, String userDuz, Date dcDate, String comments);
  public abstract ServiceResponse<String> discontinueConsult(ISecurityContext securityContext, String orderIen, String userDuz, Date dcDate, String comments);
  public abstract ServiceResponse<String> addConsultComment(ISecurityContext securityContext, String consultId, List<String> comments, boolean alert, String alertTo, double actionDate);
  public abstract ServiceResponse<EditResubmit> loadConsultForEdit(String consultIen);
  public abstract ServiceResponse<LockDocumentResult> lockConsultRequest(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<String> unlockConsult(ISecurityContext securityContext, String noteIen);
  public abstract ServiceResponse<Demographics> getDemographics(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<Demographics> getAlsiDemographics(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<String> getAlsiDfnLookup(ISecurityContext securityContext, String ssn);
  public abstract ServiceResponse<String> lookupDiscreteItemNum(ISecurityContext securityContext, String dfn, String fileNum, String itemName);
  public abstract CollectionServiceResponse<DiscreteItemData> getDiscreteItemDataSet(ISecurityContext securityContext, String dfn, String typeNum, String itemNum, double fmStartDate);  
  public abstract ServiceResponse<DiscreteItemData> getLatestDiscreteItemDataPoint(ISecurityContext securityContext, String dfn, String typeNum, String itemNum);  
  public abstract ServiceResponse<String> getInsuranceInfo(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<Boolean> isPatientVested(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<Boolean> patientNeedsVestingCanVest(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<LabTestResult> getRecentLabs(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<String> getLabTestData(ISecurityContext securityContext, String labTestIen);
  public abstract ServiceResponse<CumulativeLabResults> getCumulativeLabResults(ISecurityContext securityContext, String patientDfn, int daysBack, Date date1, Date date2);
  public abstract CollectionServiceResponse<Medication> getMedications(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<Medication> getCoverSheetMeds(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<String> getMedDetails(ISecurityContext securityContext, String dfn, String id);
  public abstract ServiceResponse<String> getMedAdminHistory(ISecurityContext securityContext, String dfn, String id);
  public abstract ServiceResponse<TiuNote> createTiuNote(ISecurityContext securityContext, NewTiuNote newTiuNote);
  public abstract ServiceResponse<TiuNote> updateTiuNote(ISecurityContext securityContext, EditedTiuNote editedTiuNote);
  public abstract ServiceResponse<TiuNote> createAddendum(ISecurityContext securityContext, NewTiuNote newTiuNote, String addendumTo);
  public abstract CollectionServiceResponse<String> saveOrder(ISecurityContext securityContext, String dfn, NewOrder newOrder);
  public abstract ServiceResponse<String> getLabOrderDetails(ISecurityContext securityContext, String dfn, String orderId);
  public abstract ServiceResponse<OrderView> getOrderViewDefault(ISecurityContext securityContext);
  public abstract ServiceResponse<String> getOrderDetails(ISecurityContext securityContext, String orderId, String patientDfn);
  /**
   * @deprecated To avoid Mumps errors, use the other version of this method, which requires patientDfn as a third argument.
   */
  public abstract ServiceResponse<String> getOrderDetails(ISecurityContext securityContext, String orderId);
  public abstract ServiceResponse<String> getAlsiOrderDetails(ISecurityContext securityContext, String orderId);
  public abstract ServiceResponse<String> getOrderResult(ISecurityContext securityContext, String dfn, String orderId);
  public abstract CollectionServiceResponse<String> getDGroupMap(ISecurityContext securityContext);
  public abstract ServiceResponse<DGroup> getSeqOfDGroup(ISecurityContext securityContext, String ien);
  public abstract CollectionServiceResponse<OrderSheet> getOrderSheets(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<DcReason> getDiscontinueReasons(ISecurityContext securityContext);
  public abstract ServiceResponse<Integer> discontinueOrder(ISecurityContext securityContext, String orderId, String providerDuz, 
      String locationIen, String reasonIen, boolean dcOrigOrder, boolean newOrder);
  public abstract ServiceResponse<OrderInfo> holdOrder(ISecurityContext securityContext, String orderId, String providerDuz);
  public abstract ServiceResponse<OrderInfo> releaseOrderHold(ISecurityContext securityContext, String orderId, String providerDuz);
  public abstract ServiceResponse<OrderInfo> verifyOrder(ISecurityContext securityContext, String orderId, String esCode);
  public abstract ServiceResponse<OrderInfo> completeOrder(ISecurityContext securityContext, String orderId, String esCode);
  public abstract ServiceResponse<OrdersInfoList> getOrdersListForDefaultView(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<OrdersInfoList> getOrdersList(ISecurityContext securityContext, String dfn, OrderView orderView);
  public abstract ServiceResponse<OrdersInfoList> getOrdersList2(ISecurityContext securityContext, String dfn, OrderView orderView, Calendar startDate);
  public abstract ServiceResponse<OrdersInfoList> getOrdersList3(ISecurityContext securityContext, String dfn, OrderView orderView, Calendar startDate,
                                                boolean checkUrgency);
  public abstract ServiceResponse<OrdersInfoList> getOrdersList4(ISecurityContext securityContext, String dfn, Calendar startDate, 
                                                boolean checkUrgency,OrdersInfoList ordersInfoList);
  public abstract ServiceResponse<OrdersInfoList> getOrdersAbbrForDefaultView(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<OrdersInfoList> getOrdersAbbr(ISecurityContext securityContext, String dfn, OrderView orderView);
  public abstract CollectionServiceResponse<String> getUnsignedOrders(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<String> getDisplayGroupIen(ISecurityContext securityContext, String group);
  public abstract ServiceResponse<OrderStatus> parseOrderDetail(ISecurityContext securityContext, String orderId, String dfn);
  public abstract ServiceResponse<String> getOrderUrgency(ISecurityContext securityContext, String orderId, String dfn);
  public abstract ServiceResponse<String> doGetOrderUrgency(ISecurityContext securityContext, String orderId, String context);
  public abstract ServiceResponse<String> parseOrderUrgency(ISecurityContext securityContext, String orderId, String dfn);
  public abstract ServiceResponse<OrderDetails> parseOrderDetails(ISecurityContext securityContext, String orderId, String patientDfn);
  
  /**
   * @deprecated To avoid Mumps errors, use the other version of this method, which requires patientDfn as a third argument.
   */
  public abstract ServiceResponse<OrderDetails> parseOrderDetails(ISecurityContext securityContext, String orderId);
  public abstract ServiceResponse<Integer> getOrderStatus(ISecurityContext securityContext, String orderId);
  public abstract ServiceResponse<String> getLabTestOrderData(ISecurityContext securityContext, String orderId);
  public abstract ServiceResponse<String> alertOrder(ISecurityContext securityContext, String orderIen);
  public abstract ServiceResponse<String> validateOrderAction(ISecurityContext securityContext, String id, String action, String providerDuz);
  public abstract ServiceResponse<String> validateComplexOrderAction(ISecurityContext securityContext, String id);
  public abstract ServiceResponse<String> alertOrderForRecipient(ISecurityContext securityContext, String orderIen, String recipientDuz);
  public abstract ServiceResponse<LockDocumentResult> lockPatient(ISecurityContext securityContext, String patientDfn);
  public abstract ServiceResponse<String> unlockPatient(ISecurityContext securityContext, String patientDfn);
  public abstract ServiceResponse<LockDocumentResult> lockOrder(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<String> unlockOrder(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<OrderInfo> getOrderByIfn(ISecurityContext securityContext, String id);
  public abstract ServiceResponse<OrderDialogResolved> buildReponses(ISecurityContext securityContext, String patientDfn, String encLocation, 
      String encProvider, boolean inpatient, String patientSex, int patientAge, int scPercent, 
      String keyVars, String inputId, boolean forImo);
  public abstract CollectionServiceResponse<OrderResponse> loadResponses(ISecurityContext securityContext, String orderId, boolean xferOutToInOnMeds, 
       boolean xferInToOutNow);
  public abstract CollectionServiceResponse<String> buildOcItems(ISecurityContext securityContext, String startDtTm, String fillerId, 
       List<OrderResponse> orderResponses);
  public abstract CollectionServiceResponse<OrderDialogPrompt> loadDialogDefinition(ISecurityContext securityContext, String dialogName); 
  public abstract CollectionServiceResponse<String> orderChecksOnAccept(ISecurityContext securityContext, String patientDfn, 
       String encLocation, String fillerId, String startDtTm, 
       List<String> oiList, String dupORIFN, String renewal);
  public abstract ServiceResponse<OrderInfo> putNewOrder(ISecurityContext securityContext, NewOrder newOrder);
  public abstract ServiceResponse<OrderInfo> putNewOrder(ISecurityContext securityContext, String patientDfn, String encProvider, 
      String encLocation, String patientSpecialty, double encDatetime, ConstructOrder constructOrder, String orderSource, String editOf);
  public abstract ServiceResponse<String> getTestsByDate(ISecurityContext securityContext, String patientDfn, int daysBack, Date date1, Date date2);
  
  public abstract CollectionServiceResponse<EncounterAppointment> getTodaysEncounterAppointments(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<EncounterAppointment> getOutpatientEncounters(ISecurityContext securityContext, String dfn, Calendar fromDate, Calendar toDate);
  public abstract CollectionServiceResponse<EncounterAppointment> getInpatientEncounters(ISecurityContext securityContext, String dfn, Calendar fromDate, Calendar toDate);
  public abstract CollectionServiceResponse<EncounterAppointment> getAllEncounters(ISecurityContext securityContext, String dfn, Calendar fromDate, Calendar toDate);
  public abstract CollectionServiceResponse<EncounterAppointment> getAllEncounters2(ISecurityContext securityContext, String dfn, Calendar fromDate, Calendar toDate, boolean skipAdmits);
  public abstract ServiceResponse<Encounter> getEncounterDetails(ISecurityContext securityContext, Encounter anEncounter, boolean inpatient);
  public abstract ServiceResponse<String> getVisitCategory(ISecurityContext securityContext, String initialCat, String locationIen, boolean inpatient);
  public abstract ServiceResponse<String> getEncFutureDays(ISecurityContext securityContext);
  public abstract ServiceResponse<String> savePceData(ISecurityContext securityContext, List<String> pceList, String noteIen, String locationIen);
  public abstract ServiceResponse<PatientLocation> getCurrentPatientLocation(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<MeansTestStatus> getMeansTestRequired(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<PatientInfo> getPatientInfo(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<String> getPatientInquiry(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<Patient> listAllPatients(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<Patient> getSubSetOfPatients(ISecurityContext securityContext, String startFrom, int direction);
  public abstract CollectionServiceResponse<Patient> listPtByProvider(ISecurityContext securityContext, String ien);
  public abstract CollectionServiceResponse<Patient> listPtBySpecialty(ISecurityContext securityContext, String ien);
  public abstract CollectionServiceResponse<Patient> listPtByTeam(ISecurityContext securityContext, String ien);
  public abstract CollectionServiceResponse<Patient> listPtByClinic(ISecurityContext securityContext, String ien, Date firstDate, Date lastDate);
  public abstract CollectionServiceResponse<Patient> listPtByWard(ISecurityContext securityContext, String ien);
  public abstract CollectionServiceResponse<Patient> listPtByLast5(ISecurityContext securityContext, String last5);
  public abstract CollectionServiceResponse<Patient> listPtByRPLLast5(ISecurityContext securityContext, String last5);
  public abstract CollectionServiceResponse<Patient> listPtByFullSSN(ISecurityContext securityContext, String ssn);
  public abstract CollectionServiceResponse<Patient> listPtByRPLFullSSN(ISecurityContext securityContext, String ssn);
  public abstract CollectionServiceResponse<Patient> readRPLPtList(ISecurityContext securityContext, String rplJobNumber, String startFrom, int direction);
  public abstract CollectionServiceResponse<Patient> listPtByDflt(ISecurityContext securityContext);
  public abstract ServiceResponse<Patient> listPtTop(ISecurityContext securityContext);
  public abstract ServiceResponse<Patient> getDfltPtList(ISecurityContext securityContext);
  public abstract ServiceResponse<Character> getDfltPtListSrc(ISecurityContext securityContext);
  public abstract void savePtListDflt(ISecurityContext securityContext, String x);
  public abstract ServiceResponse<String> getDfltSort(ISecurityContext securityContext);
  public abstract ServiceResponse<String> makeRPLPtList(ISecurityContext securityContext, String rplList);
  public abstract void killRPLPtList(ISecurityContext securityContext, String rplJobNumber);
  public abstract ServiceResponse<SimilarRecordsStatus> getSimilarRecordsFound(ISecurityContext securityContext, String dfn); 
  public abstract CollectionServiceResponse<String> listDateRangeClinic(ISecurityContext securityContext);
  public abstract ServiceResponse<String> defaultDateRangeClinic(ISecurityContext securityContext);
  public abstract CollectionServiceResponse<Posting> getPostings(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<Patient> getDetailPosting(ISecurityContext securityContext, String dfn, String id);
  public abstract CollectionServiceResponse<Problem> getProblems(ISecurityContext securityContext, String dfn, String status);
  public abstract ServiceResponse<String> getProblemDetail(ISecurityContext securityContext, String dfn, String ien);
  public abstract ServiceResponse<SensitiveRecordAccessStatus> getSensitiveRecordAccess(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<Boolean> logSensitiveRecordAccess(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<TiuNote> getTiuNote(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<TiuNote> getTiuNoteForEdit(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<String> getTiuListItem(ISecurityContext securityContext, String docIen);
  public abstract ServiceResponse<Boolean> isSurgeryTitle(ISecurityContext securityContext, String titleIen);
  public abstract ServiceResponse<Boolean> isConsultTitle(ISecurityContext securityContext, String titleIen);
  public abstract ServiceResponse<Boolean> authorHasSigned(ISecurityContext securityContext, String titleIen, String userDuz);
  public abstract ServiceResponse<String> getTitleIenForNote(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<String> getPackageRefForNote(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<String> getConsultIenforNote(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<String> getVisitStrForNote(ISecurityContext securityContext, String ien);
  public abstract CollectionServiceResponse<String> getPCEDataForNote(ISecurityContext securityContext, String ien);
  public abstract CollectionServiceResponse<String> getPCEDataForNote2(ISecurityContext securityContext, String ien, String patietnDfn, String visitStr);
  public abstract ServiceResponse<String> getTiuDetails(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<LockDocumentResult> lockDocument(ISecurityContext securityContext, String ien);
  public abstract void unlockDocument(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<Boolean> deleteDocument(ISecurityContext securityContext, String ien, String reason);
  public abstract ServiceResponse<Boolean> justifyDocumentDelete(ISecurityContext securityContext, String ien);
  public abstract CollectionServiceResponse<TemplateType> getTemplateRoots(ISecurityContext securityContext, String userDuz);
  public abstract ServiceResponse<Integer> getTemplateAccess(ISecurityContext securityContext, String id, String userDuz, String encounterLocation);
  public abstract CollectionServiceResponse<TiuNoteHeader> getSignedProgressNoteHeaders(ISecurityContext securityContext, String dfn, int limit);
  public abstract CollectionServiceResponse<TiuNoteHeader> getSignedDischargeSummaryHeaders(ISecurityContext securityContext, String dfn, int limit);
  public abstract CollectionServiceResponse<TiuNoteHeader> getSignedTiuNoteHeaders(ISecurityContext securityContext, String dfn, String noteClass, int limit);
  public abstract CollectionServiceResponse<TiuNoteHeader> getUnsignedTiuNoteHeadersByAuthor(ISecurityContext securityContext, String dfn, String duz, int limit);
  public abstract CollectionServiceResponse<TiuNoteHeader> getUncosignedTiuNoteHeadersByAuthor(ISecurityContext securityContext, String dfn, String duz, int limit);
  public abstract CollectionServiceResponse<TiuNoteHeader> getAllUnsignedTiuNoteHeaders(ISecurityContext securityContext, String dfn, String duz, int limit);
  public abstract CollectionServiceResponse<TiuNoteHeader> getAllTiuNoteHeaders(ISecurityContext securityContext, String dfn, String duz, int limit);
  public abstract ServiceResponse<UnsignedNoteHeaders> getUnsignedNoteHeaders(ISecurityContext securityContext, String authorDuz, int limit, boolean unsignedNotes, 
                                                             boolean uncosignedNotes);
  public abstract CollectionServiceResponse<TiuNoteHeader> getTiuNoteHeaders(ISecurityContext securityContext, String dfn, TiuNoteHeadersSelection tiuNoteHeadersSelection);
  public abstract ServiceResponse<QueueHandle> getUnsignedByServiceQueueHandle(ISecurityContext securityContext, String service);
  public abstract CollectionServiceResponse<TiuNoteHeader> getUnsignedByServiceReport(ISecurityContext securityContext, String handle, boolean delete);
  public abstract ServiceResponse<Boolean> deleteUnsignedByServiceReport(ISecurityContext securityContext, String handle);
  public abstract ServiceResponse<VitalSigns> getVitalSigns(ISecurityContext securityContext, String dfn);
  public abstract ServiceResponse<VitalSigns> getVitalSigns(ISecurityContext securityContext, String dfn, double fmFromDate, double fmThroughDate);
  public abstract CollectionServiceResponse<VitalSignMeasurement> getVitalSignsList(ISecurityContext securityContext, String dfn);
  public abstract CollectionServiceResponse<VitalSignMeasurement> getVitalSignsList(ISecurityContext securityContext, String dfn, double fmFromDate, double fmThroughDate);
  public abstract ServiceResponse<String> getReportText(ISecurityContext securityContext, String dfn, Date fromDate, Date toDate, int nrpts, String arg);
  public abstract ServiceResponse<VistaImageInfo> addVistaImage(ISecurityContext securityContext, String dfn, String duz, String fileExt);
  public abstract ServiceResponse<String> linkNoteToImage(ISecurityContext securityContext, String imageIen, String tiuNoteIen);
  public abstract CollectionServiceResponse<String> importVistaImage(ISecurityContext securityContext, List<String> nodeList);
  public abstract CollectionServiceResponse<RemoteStation> getRemoteSites(ISecurityContext securityContext, String dfn);
}
