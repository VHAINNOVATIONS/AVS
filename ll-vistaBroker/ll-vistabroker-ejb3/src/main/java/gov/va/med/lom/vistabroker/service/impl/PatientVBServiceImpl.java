package gov.va.med.lom.vistabroker.service.impl;

import gov.va.med.lom.vistabroker.patient.dao.*;
import gov.va.med.lom.vistabroker.patient.data.*;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless(name = "gov.va.med.lom.vistabroker.PatientVBService")
@Remote(PatientVBService.class)
public class PatientVBServiceImpl extends BaseService implements PatientVBService,
    Serializable {

  /*
   * CONSTRUCTORS
   */
  public PatientVBServiceImpl() {
    super();
  }

  /*
   * BUSINESS METHODS
   */

  // Address RPC
  public ServiceResponse<Address> getAddress(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(Address.class, AddressDao.class, "getAddress", dfn);
  }

  // Admission RPC

  public CollectionServiceResponse<Admission> getAdmissions(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(Admission.class, AdmissionsDao.class,
        "getAdmissions", dfn);
  }

  // Allergies/Reactions RPCs
  public ServiceResponse<AllergiesReactants> getAllergiesReactants(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(AllergiesReactants.class,
        AllergiesReactantsDao.class, "getAllergiesReactants", dfn);
  }

  public ServiceResponse<String> getAllergyDetail(
      ISecurityContext securityContext, String dfn, String ien) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, ien };
    return singleResult(String.class, AllergiesReactantsDao.class,
        "getAllergyDetail", params);
  }

  // Appointments RPCs
  public CollectionServiceResponse<Appointment> getAppointments(
      ISecurityContext securityContext, String dfn, Date fromDate,
      Date throughDate) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, fromDate, throughDate };
    return collectionResult(Appointment.class, AppointmentsDao.class,
        "getAppointments", params);
  }

  public CollectionServiceResponse<String> getVisitDetail(
      ISecurityContext securityContext, String dfn, String id) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, id };
    return collectionResult(String.class, AppointmentsDao.class,
        "getVisitDetail", params);
  }

  public ServiceResponse<CancelledAppointment> cancelAppointment(
      ISecurityContext securityContext, String dfn, double apptDatetime,
      String clinic) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, apptDatetime, clinic };
    return singleResult(CancelledAppointment.class, AppointmentsDao.class,
        "cancelAppointment", params);
  }

  // Clinical Procedures RPC
  public ServiceResponse<ClinicalProcedure> getClinicalProcedure(
      ISecurityContext securityContext, String orderEntryNumber) {
    setSecurityContext(securityContext);
    return singleResult(ClinicalProcedure.class,
        ClinicalProceduresDao.class, "getClinicalProcedure",
        orderEntryNumber);
  }

  public ServiceResponse<String> closeCpNote(
      ISecurityContext securityContext, String tiuNoteIen) {
    setSecurityContext(securityContext);
    return singleResult(String.class,
        ClinicalProceduresDao.class, "closeCpNote",
        tiuNoteIen);
  }  
  
  // Clinical Reminders RPC
  public CollectionServiceResponse<ClinicalReminder> getClinicalReminders(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(ClinicalReminder.class,
        ClinicalRemindersDao.class, "getClinicalReminders", dfn);
  }

  public ServiceResponse<String> getClinicalReminderDetails(
      ISecurityContext securityContext, String dfn, String ien) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, ien };
    return singleResult(String.class, ClinicalRemindersDao.class,
        "getClinicalReminderDetails", params);
  }

  // Combat Vet RPC
  public CollectionServiceResponse<String> getCombatVetInfo(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(String.class, CombatVetDao.class,
        "getCombatVetInfo", dfn);
  }

  // Consults RPCs
  public CollectionServiceResponse<ConsultRequest> getDefaultConsultRequests(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(ConsultRequest.class, ConsultsDao.class,
        "getDefaultConsultRequests", dfn);
  }

  public CollectionServiceResponse<ConsultRequest> getConsultRequests(
      ISecurityContext securityContext, String dfn,
      ConsultRequestsSelection consultRequestsSelection) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, consultRequestsSelection };
    return collectionResult(ConsultRequest.class, ConsultsDao.class,
        "getConsultRequests", params);
  }

  public ServiceResponse<String> getConsultDetail(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(String.class, ConsultsDao.class,
        "getConsultDetail", ien);
  }

  public ServiceResponse<String> getMedResults(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(String.class, ConsultsDao.class, "getMedResults",
        ien);
  }

  public ServiceResponse<ConsultRec> getConsultRec(
      ISecurityContext securityContext, String ien, boolean showAddenda) {
    setSecurityContext(securityContext);
    Object[] params = { ien, showAddenda };
    return singleResult(ConsultRec.class, ConsultsDao.class,
        "getConsultRec", params);
  }

  public ServiceResponse<Consult> findConsult(
      ISecurityContext securityContext, String consultIen) {
    setSecurityContext(securityContext);
    return singleResult(Consult.class, ConsultsDao.class, "findConsult",
        consultIen);
  }

  public ServiceResponse<String> getConsultOrderIen(
      ISecurityContext securityContext, String consultIen) {
    setSecurityContext(securityContext);
    return singleResult(String.class, ConsultsDao.class,
        "getConsultOrderIen", consultIen);
  }

  public ServiceResponse<String> denyConsult(ISecurityContext securityContext, String orderIen, 
      String userDuz, Date dcDate, String comments) {
    setSecurityContext(securityContext);
    Object[] params = { orderIen, userDuz, dcDate, comments };
    return singleResult(String.class, ConsultsDao.class,
      "denyConsult", params);    
  }
  
  public ServiceResponse<String> discontinueConsult(ISecurityContext securityContext, String orderIen, 
      String userDuz, Date dcDate, String comments) {
    setSecurityContext(securityContext);
    Object[] params = { orderIen, userDuz, dcDate, comments };
    return singleResult(String.class, ConsultsDao.class,
      "discontinueConsult", params);      
  }
  
  public ServiceResponse<String> getProcedureIen(
      ISecurityContext securityContext, String orderIen) {
    setSecurityContext(securityContext);
    return singleResult(String.class, ConsultsDao.class, "getProcedureIen",
        orderIen);
  }

  public ServiceResponse<String> getCPRequests(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(String.class, ConsultsDao.class, "getCPRequests",
        dfn);
  }
  
  public ServiceResponse<String> addConsultComment(ISecurityContext securityContext, String consultId, 
      List<String> comments, boolean alert, String alertTo, double actionDate) {
    setSecurityContext(securityContext);
    Object[] params = { consultId, comments, alert, alertTo, actionDate };
    return singleResult(String.class, ConsultsDao.class,
        "addConsultComment", params);
  }
  
  public ServiceResponse<EditResubmit> loadConsultForEdit(String consultIen) {
    setSecurityContext(securityContext);
    return singleResult(EditResubmit.class, ConsultsDao.class,
        "loadConsultForEdit", consultIen);
  }

  public ServiceResponse<LockDocumentResult> lockConsultRequest(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(LockDocumentResult.class, ConsultsDao.class,
        "lockConsultRequest", ien);
  }

  public ServiceResponse<String> unlockConsult(
      ISecurityContext securityContext, String noteIen) {
    setSecurityContext(securityContext);
    return singleResult(String.class, ConsultsDao.class, "unlockConsult",
        noteIen);
  }

  // Demographics RPC
  public ServiceResponse<Demographics> getDemographics(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(Demographics.class, DemographicsDao.class,
        "getDemographics", dfn);
  }

  // ALSI Demographics RPC
  public ServiceResponse<Demographics> getAlsiDemographics(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(Demographics.class, DemographicsDao.class,
        "getAlsiDemographics", dfn);
  }

  public ServiceResponse<String> getAlsiDfnLookup(
      ISecurityContext securityContext, String ssn) {
    setSecurityContext(securityContext);
    return singleResult(String.class, PatientSelectionDao.class,
        "getAlsiDfnLookup", ssn);
  }

  // Discrete Results RPCs
  public ServiceResponse<String> lookupDiscreteItemNum(
      ISecurityContext securityContext, String dfn, String fileNum,
      String itemName) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, fileNum, itemName };
    return singleResult(String.class, DiscreteResultsDao.class,
        "lookupItemNum", params);
  }

  public CollectionServiceResponse<DiscreteItemData> getDiscreteItemDataSet(
      ISecurityContext securityContext, String dfn, String typeNum,
      String itemNum, double fmStartDate) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, typeNum, itemNum, fmStartDate };
    return collectionResult(DiscreteItemData.class,
        DiscreteResultsDao.class, "getItemDataSet", params);
  }

  public ServiceResponse<DiscreteItemData> getLatestDiscreteItemDataPoint(
      ISecurityContext securityContext, String dfn, String typeNum,
      String itemNum) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, typeNum, itemNum };
    return singleResult(DiscreteItemData.class, DiscreteResultsDao.class,
        "getLatestItemDataPoint", params);
  }

  // Insurance RPC
  public ServiceResponse<String> getInsuranceInfo(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(String.class, InsuranceDao.class,
        "getInsuranceInfo", dfn);
  }

  // Patient Vesting RPCs
  public ServiceResponse<Boolean> isPatientVested(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(Boolean.class, PatientVestingDao.class,
        "isPatientVested", dfn);
  }

  public ServiceResponse<Boolean> patientNeedsVestingCanVest(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(Boolean.class, PatientVestingDao.class,
        "patientNeedsVestingCanVest", dfn);
  }

  // Labs RPC
  public CollectionServiceResponse<LabTestResult> getRecentLabs(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(LabTestResult.class, LabsDao.class,
        "getRecentLabs", dfn);
  }

  public CollectionServiceResponse<String> getLabTestData(
      ISecurityContext securityContext, String labTestIen) {
    setSecurityContext(securityContext);
    return collectionResult(String.class, LabsDao.class, "getLabTestData",
        labTestIen);
  }

  public ServiceResponse<CumulativeLabResults> getCumulativeLabResults(
      ISecurityContext securityContext, String patientDfn, int daysBack,
      Date date1, Date date2) {
    setSecurityContext(securityContext);
    Object[] params = { patientDfn, daysBack, date1, date2 };
    return singleResult(CumulativeLabResults.class, LabsDao.class,
        "getCumulativeLabResults", params);
  }

  public ServiceResponse<String> getTestsByDate(ISecurityContext securityContext, String patientDfn, int daysBack, Date date1, Date date2) {
    setSecurityContext(securityContext);
    Object[] params = { patientDfn, daysBack, date1, date2 };
    return singleResult(String.class, LabsDao.class,
        "getTestsByDate", params);    
  }
  
  // Medications RPCs
  public CollectionServiceResponse<Medication> getMedications(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(Medication.class, MedicationsDao.class,
        "getMedications", dfn);
  }

  public CollectionServiceResponse<Medication> getCoverSheetMeds(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(Medication.class, MedicationsDao.class,
        "getCoverSheetMeds", dfn);
  }

  public ServiceResponse<String> getMedDetails(
      ISecurityContext securityContext, String dfn, String id) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, id };
    return singleResult(String.class, MedicationsDao.class,
        "getMedDetails", params);
  }

  public ServiceResponse<String> getMedAdminHistory(
      ISecurityContext securityContext, String dfn, String id) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, id };
    return singleResult(String.class, MedicationsDao.class,
        "getMedAdminHistory", params);
  }

  // New TIU Note RPCs
  public ServiceResponse<TiuNote> createTiuNote(
      ISecurityContext securityContext, NewTiuNote newTiuNote) {
    setSecurityContext(securityContext);
    return singleResult(TiuNote.class, NewTiuNoteDao.class,
        "createTiuNote", newTiuNote);
  }

  public ServiceResponse<TiuNote> updateTiuNote(
      ISecurityContext securityContext, EditedTiuNote editedTiuNote) {
    setSecurityContext(securityContext);
    return singleResult(TiuNote.class, NewTiuNoteDao.class,
        "updateTiuNote", editedTiuNote);
  }

  public ServiceResponse<TiuNote> createAddendum(
      ISecurityContext securityContext, NewTiuNote newTiuNote,
      String addendumTo) {
    setSecurityContext(securityContext);
    Object[] params = { newTiuNote, addendumTo };
    return singleResult(TiuNote.class, NewTiuNoteDao.class,
        "createAddendum", params);
  }

  // Orders RPCs
  public CollectionServiceResponse<String> saveOrder(
      ISecurityContext securityContext, String dfn, NewOrder newOrder) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, newOrder };
    return collectionResult(String.class, OrdersDao.class, "saveOrder",
        params);
  }

  public ServiceResponse<String> getLabOrderDetails(
      ISecurityContext securityContext, String dfn, String orderId) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, orderId };
    return singleResult(String.class, OrdersDao.class,
        "getLabOrderDetails", params);
  }

  public ServiceResponse<OrderView> getOrderViewDefault(
      ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return singleResult(OrderView.class, OrdersDao.class,
        "getOrderViewDefault");
  }

  public ServiceResponse<String> getOrderDetails(
      ISecurityContext securityContext, String orderId, String dfn) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, dfn };
    return singleResult(String.class, OrdersDao.class, "getOrderDetails",
        params);
  }

  /**
   * @deprecated To avoid Mumps errors, use the other version of this method, which requires patientDfn as a third argument.
   */
  public ServiceResponse<String> getOrderDetails(
      ISecurityContext securityContext, String orderId) {
    setSecurityContext(securityContext);
    return singleResult(String.class, OrdersDao.class, "getOrderDetails",
        orderId);
  }

  public ServiceResponse<String> getAlsiOrderDetails(
      ISecurityContext securityContext, String orderId) {
    setSecurityContext(securityContext);
    return singleResult(String.class, OrdersDao.class,
        "getAlsiOrderDetails", orderId);
  }

  public ServiceResponse<String> getOrderResult(
      ISecurityContext securityContext, String dfn, String orderId) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, orderId };
    return singleResult(String.class, OrdersDao.class, "getOrderResult",
        params);
  }

  public CollectionServiceResponse<String> getDGroupMap(
      ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return collectionResult(String.class, OrdersDao.class, "getDGroupMap");
  }

  public ServiceResponse<DGroup> getSeqOfDGroup(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(DGroup.class, OrdersDao.class, "getSeqOfDGroup",
        ien);
  }

  public CollectionServiceResponse<OrderSheet> getOrderSheets(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(OrderSheet.class, OrdersDao.class,
        "getOrderSheets", dfn);
  }

  public CollectionServiceResponse<DcReason> getDiscontinueReasons(ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return collectionResult(DcReason.class, OrdersDao.class,
        "getDiscontinueReasons");
  }    
  
  public ServiceResponse<Integer> discontinueOrder(
      ISecurityContext securityContext, String orderId, 
      String providerDuz, String locationIen, String reasonIen, boolean dcOrigOrder, boolean newOrder) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, providerDuz, locationIen, reasonIen, dcOrigOrder, newOrder };
    return singleResult(Integer.class, OrdersDao.class,
        "discontinueOrder", params);    
  }
  
  public ServiceResponse<OrderInfo> holdOrder(
      ISecurityContext securityContext, String orderId, String providerDuz) {
  setSecurityContext(securityContext);
  Object[] params = { orderId, providerDuz };
  return singleResult(Order.class, OrdersDao.class,
      "holdOrder", params);      
  }
  
  public ServiceResponse<OrderInfo> releaseOrderHold(
      ISecurityContext securityContext, String orderId, String providerDuz) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, providerDuz };
    return singleResult(Order.class, OrdersDao.class,
        "releaseOrderHold", params);     
  }
  
  public ServiceResponse<OrderInfo> verifyOrder(
      ISecurityContext securityContext, String orderId, String esCode) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, esCode };
    return singleResult(Order.class, OrdersDao.class,
        "verifyOrder", params);     
  }
  
  public ServiceResponse<OrderInfo> completeOrder(
      ISecurityContext securityContext, String orderId, String esCode) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, esCode };
    return singleResult(Order.class, OrdersDao.class,
        "completeOrder", params);     
  }
  
  public ServiceResponse<OrdersInfoList> getOrdersListForDefaultView(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(OrdersInfoList.class, OrdersDao.class,
        "getOrdersListForDefaultView", dfn);
  }

  public ServiceResponse<OrdersInfoList> getOrdersList(
      ISecurityContext securityContext, String dfn, OrderView orderView) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, orderView };
    return singleResult(OrdersInfoList.class, OrdersDao.class,
        "getOrdersList", params);
  }

  public ServiceResponse<OrdersInfoList> getOrdersList2(
      ISecurityContext securityContext, String dfn, OrderView orderView,
      Calendar startDate) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, orderView, startDate };
    return singleResult(OrdersInfoList.class, OrdersDao.class,
        "getOrdersList", params);
  }

  public ServiceResponse<OrdersInfoList> getOrdersList3(
      ISecurityContext securityContext, String dfn, OrderView orderView,
      Calendar startDate, boolean checkUrgency) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, orderView, startDate, checkUrgency };
    return singleResult(OrdersInfoList.class, OrdersDao.class,
        "getOrdersList", params);
  }

  public ServiceResponse<OrdersInfoList> getOrdersList4(
      ISecurityContext securityContext, String dfn, Calendar startDate,
      boolean checkUrgency, OrdersInfoList ordersInfoList) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, startDate, checkUrgency, ordersInfoList };
    return singleResult(OrdersInfoList.class, OrdersDao.class,
        "getOrdersList", params);
  }

  public ServiceResponse<OrdersInfoList> getOrdersAbbrForDefaultView(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(OrdersInfoList.class, OrdersDao.class,
        "getOrdersAbbrForDefaultView", dfn);
  }

  public ServiceResponse<OrdersInfoList> getOrdersAbbr(
      ISecurityContext securityContext, String dfn, OrderView orderView) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, orderView };
    return singleResult(OrdersInfoList.class, OrdersDao.class,
        "getOrdersAbbr", params);
  }

  public CollectionServiceResponse<String> getUnsignedOrders(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(String.class, OrdersDao.class,
        "getUnsignedOrders", dfn);
  }

  public ServiceResponse<String> getDisplayGroupIen(
      ISecurityContext securityContext, String group) {
    setSecurityContext(securityContext);
    return singleResult(String.class, OrdersDao.class,
        "getDisplayGroupIen", group);
  }

  public ServiceResponse<OrderStatus> parseOrderDetail(
      ISecurityContext securityContext, String orderId, String dfn) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, dfn };
    return singleResult(OrderStatus.class, OrdersDao.class,
        "parseOrderDetail", params);
  }

  public ServiceResponse<String> getOrderUrgency(
      ISecurityContext securityContext, String orderId, String dfn) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, dfn };
    return singleResult(String.class, OrdersDao.class, "getOrderUrgency",
        params);
  }

  public ServiceResponse<String> doGetOrderUrgency(
      ISecurityContext securityContext, String orderId, String context) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, context };
    return singleResult(String.class, OrdersDao.class, "doGetOrderUrgency",
        params);
  }

  public ServiceResponse<String> parseOrderUrgency(
      ISecurityContext securityContext, String orderId, String dfn) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, dfn };
    return singleResult(String.class, OrdersDao.class, "parseOrderUrgency",
        params);
  }

  public ServiceResponse<OrderDetails> parseOrderDetails(
      ISecurityContext securityContext, String orderId, String dfn) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, dfn };
    return singleResult(OrderDetails.class, OrdersDao.class,
        "parseOrderDetails", params);
  }

  /**
   * @deprecated To avoid Mumps errors, use the other version of this method, which requires patientDfn as a third argument.
   */
  public ServiceResponse<OrderDetails> parseOrderDetails(
      ISecurityContext securityContext, String orderId) {
    setSecurityContext(securityContext);
    return singleResult(OrderDetails.class, OrdersDao.class,
        "parseOrderDetails", orderId);
  }

  public ServiceResponse<Integer> getOrderStatus(
      ISecurityContext securityContext, String orderId) {
    setSecurityContext(securityContext);
    return singleResult(Integer.class, OrdersDao.class, "getOrderStatus",
        orderId);
  }

  public ServiceResponse<String> getLabTestOrderData(
      ISecurityContext securityContext, String orderId) {
    setSecurityContext(securityContext);
    return singleResult(String.class, OrdersDao.class,
        "getLabTestOrderData", orderId);
  }

  public ServiceResponse<String> alertOrder(ISecurityContext securityContext,
      String orderIen) {
    setSecurityContext(securityContext);
    return singleResult(String.class, OrdersDao.class, "alertOrder",
        orderIen);
  }

  public ServiceResponse<String> alertOrderForRecipient(
      ISecurityContext securityContext, String orderIen,
      String recipientDuz) {
    setSecurityContext(securityContext);
    Object[] params = { orderIen, recipientDuz };
    return singleResult(String.class, OrdersDao.class,
        "alertOrderForRecipient", params);
  }

  public ServiceResponse<String> validateOrderAction(
      ISecurityContext securityContext, String id,
      String action, String providerDuz) {
    setSecurityContext(securityContext);
    Object[] params = { id, action, providerDuz };
    return singleResult(String.class, OrdersDao.class,
        "validateOrderAction", params);
  }

  public ServiceResponse<String> validateComplexOrderAction(
      ISecurityContext securityContext, String id) {
    setSecurityContext(securityContext);
    return singleResult(String.class, OrdersDao.class,
        "validateComplexOrderAction", id);
  }
  
  public ServiceResponse<LockDocumentResult> lockPatient(ISecurityContext securityContext, String patientDfn) {
      setSecurityContext(securityContext);
      return singleResult(Integer.class, OrdersDao.class,
          "lockPatient", patientDfn);    
  }
  
  public ServiceResponse<String> unlockPatient(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(String.class, OrdersDao.class, "unlockPatient", ien);
  }  
  
  public ServiceResponse<LockDocumentResult> lockOrder(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(LockDocumentResult.class, OrdersDao.class,
        "lockOrder", ien);
  }

  public ServiceResponse<String> unlockOrder(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(String.class, OrdersDao.class, "unlockOrder", ien);
  }
  
  public ServiceResponse<OrderInfo> getOrderByIfn(
    ISecurityContext securityContext, String id) {
      setSecurityContext(securityContext);
      return singleResult(Order.class, OrdersDao.class, "getOrderByIfn", id);
  }
  
  public ServiceResponse<OrderDialogResolved> buildReponses(ISecurityContext securityContext, String patientDfn, String encLocation, 
      String encProvider, boolean inpatient, String patientSex, int patientAge, 
      int scPercent, String keyVars, String inputId, boolean forImo) {
      setSecurityContext(securityContext);
      Object[] params = { patientDfn, encLocation, encProvider, inpatient, patientSex, 
          patientAge, scPercent, keyVars, inputId, forImo };
      return singleResult(OrderDialogResolved.class, OrdersDao.class, "buildReponses", params);
  }
  
  public CollectionServiceResponse<OrderResponse> loadResponses(ISecurityContext securityContext, String orderId, boolean xferOutToInOnMeds, 
       boolean xferInToOutNow) {
    setSecurityContext(securityContext);
    Object[] params = { orderId, xferOutToInOnMeds, xferInToOutNow };
    return collectionResult(OrderResponse.class, OrdersDao.class, "loadResponses", params);    
  }
  
  public CollectionServiceResponse<String> buildOcItems(ISecurityContext securityContext, String startDtTm, String fillerId, 
       List<OrderResponse> orderResponses) {
    setSecurityContext(securityContext);
    Object[] params = { startDtTm, fillerId, orderResponses };
    return collectionResult(String.class, OrdersDao.class, "buildOcItems", params);    
  }
  
  public CollectionServiceResponse<OrderDialogPrompt> loadDialogDefinition(ISecurityContext securityContext, String dialogName) {
    setSecurityContext(securityContext);
    return collectionResult(OrderDialogPrompt.class, OrdersDao.class, "loadDialogDefinition", dialogName);       
  }
  
  public CollectionServiceResponse<String> orderChecksOnAccept(ISecurityContext securityContext, String patientDfn, String encLocation, 
      String fillerId, String startDtTm, List<String> oiList, String dupORIFN, String renewal) {
    setSecurityContext(securityContext);
    Object[] params = { patientDfn, encLocation, fillerId, startDtTm, oiList, dupORIFN, renewal };
    return collectionResult(String.class, OrdersDao.class, "orderChecksOnAccept", params);    
  }
  
  public ServiceResponse<OrderInfo> putNewOrder(ISecurityContext securityContext, NewOrder newOrder) {
    setSecurityContext(securityContext);
    return singleResult(OrderInfo.class, OrdersDao.class, "putNewOrder", newOrder);    
  }
  
  public ServiceResponse<OrderInfo> putNewOrder(ISecurityContext securityContext, String patientDfn, String encProvider, 
      String encLocation, String patientSpecialty, double encDatetime, ConstructOrder constructOrder, String orderSource, String editOf) {
    setSecurityContext(securityContext);
    Object[] params = { patientDfn, encProvider, encLocation, patientSpecialty, encDatetime, 
        constructOrder, orderSource, editOf };
    return singleResult(OrderInfo.class, OrdersDao.class, "putNewOrder", params);    
  }
  
  // Patient Encounter RPCs
  public CollectionServiceResponse<EncounterAppointment> getTodaysEncounterAppointments(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(EncounterAppointment.class,
        PatientEncounterDao.class, "getTodaysEncounterAppointments",
        dfn);
  }

  public CollectionServiceResponse<EncounterAppointment> getOutpatientEncounters(
      ISecurityContext securityContext, String dfn, Calendar fromDate,
      Calendar toDate) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, fromDate, toDate };
    return collectionResult(EncounterAppointment.class,
        PatientEncounterDao.class, "getOutpatientEncounters", params);
  }

  public CollectionServiceResponse<EncounterAppointment> getInpatientEncounters(
      ISecurityContext securityContext, String dfn, Calendar fromDate,
      Calendar toDate) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, fromDate, toDate };
    return collectionResult(EncounterAppointment.class,
        PatientEncounterDao.class, "getInpatientEncounters", params);
  }

  public CollectionServiceResponse<EncounterAppointment> getAllEncounters(
      ISecurityContext securityContext, String dfn, Calendar fromDate,
      Calendar toDate) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, fromDate, toDate };
    return collectionResult(EncounterAppointment.class,
        PatientEncounterDao.class, "getAllEncounters", params);
  }

  public CollectionServiceResponse<EncounterAppointment> getAllEncounters2(
      ISecurityContext securityContext, String dfn, Calendar fromDate,
      Calendar toDate, boolean skipAdmits) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, fromDate, toDate, skipAdmits };
    return collectionResult(EncounterAppointment.class,
        PatientEncounterDao.class, "getAllEncounters", params);
  }

  public ServiceResponse<Encounter> getEncounterDetails(
      ISecurityContext securityContext, Encounter anEncounter,
      boolean inpatient) {
    setSecurityContext(securityContext);
    Object[] params = { anEncounter, inpatient };
    return singleResult(Encounter.class, PatientEncounterDao.class,
        "getEncounterDetails", params);
  }

  public ServiceResponse<String> getVisitCategory(
      ISecurityContext securityContext, String initialCat,
      String locationIen, boolean inpatient) {
    setSecurityContext(securityContext);
    Object[] params = { initialCat, locationIen, inpatient };
    return singleResult(String.class, PatientEncounterDao.class,
        "getVisitCategory", params);
  }
  
  public ServiceResponse<String> savePceData(ISecurityContext securityContext, 
      List<String> pceList, String noteIen, String locationIen) {
    setSecurityContext(securityContext);
    Object[] params = { pceList, noteIen, locationIen };
    return singleResult(String.class, PatientEncounterDao.class,
        "savePceData", params);
  }
  
  public ServiceResponse<String> getEncFutureDays(ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return singleResult(String.class, PatientEncounterDao.class,
        "getEncFutureDays");
  }
  
  // Patient Info RPCs
  public ServiceResponse<PatientLocation> getCurrentPatientLocation(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(PatientLocation.class, PatientInfoDao.class,
        "getCurrentPatientLocation", dfn);
  }

  public ServiceResponse<MeansTestStatus> getMeansTestRequired(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(MeansTestStatus.class, PatientInfoDao.class,
        "getMeansTestRequired", dfn);
  }

  public ServiceResponse<PatientInfo> getPatientInfo(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(PatientInfo.class, PatientInfoDao.class,
        "getPatientInfo", dfn);
  }

  // Patient Inquiry RPC
  public ServiceResponse<String> getPatientInquiry(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(String.class, PatientInquiryDao.class,
        "getPatientInquiry", dfn);
  }

  // Patient Selection RPCs
  public CollectionServiceResponse<Patient> listAllPatients(
      ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listAllPatients");
  }

  public CollectionServiceResponse<Patient> getSubSetOfPatients(
      ISecurityContext securityContext, String startFrom, int direction) {
    setSecurityContext(securityContext);
    Object[] params = { startFrom, direction };
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "getSubSetOfPatients", params);
  }

  public CollectionServiceResponse<Patient> listPtByProvider(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtByProvider", ien);
  }

  public CollectionServiceResponse<Patient> listPtBySpecialty(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtBySpecialty", ien);
  }

  public CollectionServiceResponse<Patient> listPtByTeam(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtByTeam", ien);
  }

  public CollectionServiceResponse<Patient> listPtByClinic(
      ISecurityContext securityContext, String ien, Date firstDate,
      Date lastDate) {
    setSecurityContext(securityContext);
    Object[] params = { ien, firstDate, lastDate };
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtByClinic", params);
  }

  public CollectionServiceResponse<Patient> listPtByWard(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtByWard", ien);
  }

  public CollectionServiceResponse<Patient> listPtByLast5(
      ISecurityContext securityContext, String last5) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtByLast5", last5);
  }

  public CollectionServiceResponse<Patient> listPtByRPLLast5(
      ISecurityContext securityContext, String last5) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtByRPLLast5", last5);
  }

  public CollectionServiceResponse<Patient> listPtByFullSSN(
      ISecurityContext securityContext, String ssn) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtByFullSSN", ssn);
  }

  public CollectionServiceResponse<Patient> listPtByRPLFullSSN(
      ISecurityContext securityContext, String ssn) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtByRPLFullSSN", ssn);
  }

  public CollectionServiceResponse<Patient> readRPLPtList(
      ISecurityContext securityContext, String rplJobNumber,
      String startFrom, int direction) {
    setSecurityContext(securityContext);
    Object[] params = { rplJobNumber, startFrom, direction };
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "readRPLPtList", params);
  }

  public CollectionServiceResponse<Patient> listPtByDflt(
      ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return collectionResult(Patient.class, PatientSelectionDao.class,
        "listPtByDflt");
  }

  public ServiceResponse<Patient> listPtTop(ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return singleResult(Patient.class, PatientSelectionDao.class,
        "listPtTop");
  }

  public ServiceResponse<Patient> getDfltPtList(
      ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return singleResult(Patient.class, PatientSelectionDao.class,
        "getDfltPtList");
  }

  public ServiceResponse<Character> getDfltPtListSrc(
      ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return singleResult(Character.class, PatientSelectionDao.class,
        "getDfltPtListSrc");
  }

  public void savePtListDflt(ISecurityContext securityContext, String x) {
    setSecurityContext(securityContext);
    try {
      invokeCall(PatientSelectionDao.class, "getDfltPtListSrc", x);
    } catch (Exception e) {
      log.error("error unlockDocument", e);
    }
  }

  public ServiceResponse<String> getDfltSort(ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return singleResult(String.class, PatientSelectionDao.class,
        "getDfltSort");
  }

  public ServiceResponse<String> makeRPLPtList(
      ISecurityContext securityContext, String rplList) {
    setSecurityContext(securityContext);
    return singleResult(String.class, PatientSelectionDao.class,
        "makeRPLPtList", rplList);
  }

  public void killRPLPtList(ISecurityContext securityContext,
      String rplJobNumber) {
    setSecurityContext(securityContext);
    try {
      invokeCall(PatientSelectionDao.class, "killRPLPtList", rplJobNumber);
    } catch (Exception e) {
      log.error("error unlockDocument", e);
    }
  }

  public ServiceResponse<SimilarRecordsStatus> getSimilarRecordsFound(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(SimilarRecordsStatus.class,
        PatientSelectionDao.class, "getSimilarRecordsFound", dfn);
  }

  public CollectionServiceResponse<String> listDateRangeClinic(
      ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return collectionResult(String.class, PatientSelectionDao.class,
        "listDateRangeClinic");
  }

  public ServiceResponse<String> defaultDateRangeClinic(
      ISecurityContext securityContext) {
    setSecurityContext(securityContext);
    return singleResult(String.class, PatientSelectionDao.class,
        "defaultDateRangeClinic");
  }

  // Postings RPCs
  public CollectionServiceResponse<Posting> getPostings(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(Posting.class, PostingsDao.class,
        "getPostings", dfn);
  }

  public CollectionServiceResponse<Patient> getDetailPosting(
      ISecurityContext securityContext, String dfn, String id) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, id };
    return collectionResult(Patient.class, PostingsDao.class,
        "getDetailPosting", params);
  }

  // Problems RPCs
  public CollectionServiceResponse<Problem> getProblems(
      ISecurityContext securityContext, String dfn, String status) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, status };
    return collectionResult(Problem.class, ProblemsDao.class,
        "getProblems", params);
  }

  public ServiceResponse<String> getProblemDetail(
      ISecurityContext securityContext, String dfn, String ien) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, ien };
    return singleResult(String.class, ProblemsDao.class,
        "getProblemDetail", params);
  }

  // Sensitive Record Access RPCs
  public ServiceResponse<SensitiveRecordAccessStatus> getSensitiveRecordAccess(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(SensitiveRecordAccessStatus.class,
        SensitiveRecordAccessDao.class, "getSensitiveRecordAccess", dfn);
  }

  public ServiceResponse<Boolean> logSensitiveRecordAccess(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(Boolean.class, SensitiveRecordAccessDao.class,
        "logSensitiveRecordAccess", dfn);
  }

  // TIU Note RPCs
  public ServiceResponse<TiuNote> getTiuNote(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(TiuNote.class, TiuNoteDao.class, "getTiuNote", ien);
  }

  public ServiceResponse<TiuNote> getTiuNoteForEdit(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(TiuNote.class, TiuNoteDao.class,
        "getTiuNoteForEdit", ien);
  }

  public ServiceResponse<String> getTiuListItem(
      ISecurityContext securityContext, String docIen) {
    setSecurityContext(securityContext);
    return singleResult(String.class, TiuNoteDao.class, "getTiuListItem",
        docIen);
  }

  public ServiceResponse<Boolean> isSurgeryTitle(
      ISecurityContext securityContext, String titleIen) {
    setSecurityContext(securityContext);
    return singleResult(Boolean.class, TiuNoteDao.class, "isSurgeryTitle",
        titleIen);
  }

  public ServiceResponse<Boolean> isConsultTitle(
      ISecurityContext securityContext, String titleIen) {
    setSecurityContext(securityContext);
    return singleResult(Boolean.class, TiuNoteDao.class, "isConsultTitle",
        titleIen);
  }

  public ServiceResponse<Boolean> authorHasSigned(
      ISecurityContext securityContext, String titleIen, String userDuz) {
    setSecurityContext(securityContext);
    Object[] params = { titleIen, userDuz };
    return singleResult(Boolean.class, TiuNoteDao.class, "authorHasSigned",
        params);
  }

  public ServiceResponse<String> getTitleIenForNote(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(String.class, TiuNoteDao.class,
        "getTitleIenForNote", ien);
  }

  public ServiceResponse<String> getPackageRefForNote(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(String.class, TiuNoteDao.class,
        "getPackageRefForNote", ien);
  }

  public ServiceResponse<String> getConsultIenforNote(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(String.class, TiuNoteDao.class,
        "getConsultIenforNote", ien);
  }

  public ServiceResponse<String> getVisitStrForNote(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(String.class, TiuNoteDao.class,
        "getVisitStrForNote", ien);
  }

  public CollectionServiceResponse<String> getPCEDataForNote(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    Object[] params = { ien, null, null };
    return collectionResult(String.class, TiuNoteDao.class,
        "getPCEDataForNote", params);
  }
  
  public CollectionServiceResponse<String> getPCEDataForNote2(
      ISecurityContext securityContext, String ien, String patientDfn, String visitStr) {
    setSecurityContext(securityContext);
    Object[] params = { ien, patientDfn, visitStr };
    return collectionResult(String.class, TiuNoteDao.class,
        "getPCEDataForNote", params);
  }  

  public ServiceResponse<String> getTiuDetails(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(String.class, TiuNoteDao.class, "getTiuDetails",
        ien);
  }

  public ServiceResponse<LockDocumentResult> lockDocument(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(LockDocumentResult.class, TiuNoteDao.class,
        "lockDocument", ien);
  }

  public void unlockDocument(ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);

    try {
      invokeCall(TiuNoteDao.class, "unlockDocument", ien);
    } catch (Exception e) {
      log.error("error unlockDocument", e);
    }
  }

  public ServiceResponse<Boolean> deleteDocument(
      ISecurityContext securityContext, String ien, String reason) {
    setSecurityContext(securityContext);
    Object[] params = { ien, reason };
    return singleResult(Boolean.class, TiuNoteDao.class, "deleteDocument",
        params);
  }

  public ServiceResponse<Boolean> justifyDocumentDelete(
      ISecurityContext securityContext, String ien) {
    setSecurityContext(securityContext);
    return singleResult(Boolean.class, TiuNoteDao.class,
        "justifyDocumentDelete", ien);
  }

  // Template RPCs
  public CollectionServiceResponse<TemplateType> getTemplateRoots(
      ISecurityContext securityContext, String userDuz) {
      setSecurityContext(securityContext);
      return collectionResult(TemplateType.class, TemplateDao.class,
          "getTemplateRoots", userDuz);    
  }
  
  public ServiceResponse<Integer> getTemplateAccess(
      ISecurityContext securityContext, String id, String userDuz, String encounterLocation) {
    setSecurityContext(securityContext);
    Object[] params = { id, userDuz, encounterLocation };
    return singleResult(TemplateType.class, TemplateDao.class,
        "getTemplateAccess", params);    
  }
  
  // TIU Note Headers RPCs
  public CollectionServiceResponse<TiuNoteHeader> getSignedProgressNoteHeaders(
      ISecurityContext securityContext, String dfn, int limit) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, limit };
    return collectionResult(TiuNoteHeader.class, TiuNoteHeadersDao.class,
        "getSignedProgressNoteHeaders", params);
  }

  public CollectionServiceResponse<TiuNoteHeader> getSignedDischargeSummaryHeaders(
      ISecurityContext securityContext, String dfn, int limit) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, limit };
    return collectionResult(TiuNoteHeader.class, TiuNoteHeadersDao.class,
        "getSignedDischargeSummaryHeaders", params);
  }

  public CollectionServiceResponse<TiuNoteHeader> getSignedTiuNoteHeaders(
      ISecurityContext securityContext, String dfn, String noteClass,
      int limit) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, noteClass, limit };
    return collectionResult(TiuNoteHeader.class, TiuNoteHeadersDao.class,
        "getSignedTiuNoteHeaders", params);
  }

  public CollectionServiceResponse<TiuNoteHeader> getUnsignedTiuNoteHeadersByAuthor(
      ISecurityContext securityContext, String dfn, String duz, int limit) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, duz, limit };
    return collectionResult(TiuNoteHeader.class, TiuNoteHeadersDao.class,
        "getUnsignedTiuNoteHeadersByAuthor", params);
  }

  public CollectionServiceResponse<TiuNoteHeader> getUncosignedTiuNoteHeadersByAuthor(
      ISecurityContext securityContext, String dfn, String duz, int limit) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, duz, limit };
    return collectionResult(TiuNoteHeader.class, TiuNoteHeadersDao.class,
        "getUncosignedTiuNoteHeadersByAuthor", params);
  }

  public CollectionServiceResponse<TiuNoteHeader> getAllUnsignedTiuNoteHeaders(
      ISecurityContext securityContext, String dfn, String duz, int limit) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, duz, limit };
    return collectionResult(TiuNoteHeader.class, TiuNoteHeadersDao.class,
        "getAllUnsignedTiuNoteHeaders", params);
  }

  public CollectionServiceResponse<TiuNoteHeader> getAllTiuNoteHeaders(
      ISecurityContext securityContext, String dfn, String duz, int limit) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, duz, limit };
    return collectionResult(TiuNoteHeader.class, TiuNoteHeadersDao.class,
        "getAllTiuNoteHeaders", params);
  }

  public ServiceResponse<UnsignedNoteHeaders> getUnsignedNoteHeaders(
      ISecurityContext securityContext, String authorDuz, int limit,
      boolean unsignedNotes, boolean uncosignedNotes) {
    setSecurityContext(securityContext);
    Object[] params = { authorDuz, limit, unsignedNotes, uncosignedNotes };
    return singleResult(UnsignedNoteHeaders.class, TiuNoteHeadersDao.class,
        "getUnsignedNoteHeaders", params);
  }

  public CollectionServiceResponse<TiuNoteHeader> getTiuNoteHeaders(
      ISecurityContext securityContext, String dfn,
      TiuNoteHeadersSelection tiuNoteHeadersSelection) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, tiuNoteHeadersSelection };
    return collectionResult(TiuNoteHeader.class, TiuNoteHeadersDao.class,
        "getTiuNoteHeaders", params);
  }

  public ServiceResponse<QueueHandle> getUnsignedByServiceQueueHandle(
      ISecurityContext securityContext, String service) {
    setSecurityContext(securityContext);
    return singleResult(QueueHandle.class, TiuNoteHeadersDao.class,
        "getUnsignedByServiceQueueHandle", service);
  }

  public CollectionServiceResponse<TiuNoteHeader> getUnsignedByServiceReport(
      ISecurityContext securityContext, String handle, boolean delete) {
    setSecurityContext(securityContext);
    Object[] params = { handle, delete };
    return collectionResult(TiuNoteHeader.class, TiuNoteHeadersDao.class,
        "getUnsignedByServiceReport", params);
  }

  public ServiceResponse<Boolean> deleteUnsignedByServiceReport(
      ISecurityContext securityContext, String handle) {
    setSecurityContext(securityContext);
    return singleResult(Boolean.class, TiuNoteHeadersDao.class,
        "deleteUnsignedByServiceReport", handle);
  }

  // Vital Signs RPCs
  public ServiceResponse<VitalSigns> getVitalSigns(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return singleResult(VitalSigns.class, VitalSignsDao.class,
        "getVitalSigns", dfn);
  }

  public ServiceResponse<VitalSigns> getVitalSigns(
      ISecurityContext securityContext, String dfn, double fmFromDate,
      double fmThroughDate) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, fmFromDate, fmThroughDate };
    return singleResult(VitalSigns.class, VitalSignsDao.class,
        "getVitalSigns", params);
  }

  public CollectionServiceResponse<VitalSignMeasurement> getVitalSignsList(
      ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    return collectionResult(VitalSignMeasurement.class,
        VitalSignsDao.class, "getVitalSignsList", dfn);
  }

  public CollectionServiceResponse<VitalSignMeasurement> getVitalSignsList(
      ISecurityContext securityContext, String dfn, double fmFromDate,
      double fmThroughDate) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, fmFromDate, fmThroughDate };
    return collectionResult(VitalSignMeasurement.class,
        VitalSignsDao.class, "getVitalSignsList", params);
  }
  
  public ServiceResponse<String> getReportText(ISecurityContext securityContext, String dfn, 
                                                Date fromDate, Date toDate, int nrpts, String arg) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, fromDate, toDate, nrpts, arg };
    return singleResult(String.class, ReportsDao.class, "getReportText", params);
  }
  
  public ServiceResponse<VistaImageInfo> addVistaImage(ISecurityContext securityContext, String dfn, String duz, String fileExt) {
    setSecurityContext(securityContext);
    Object[] params = { dfn, duz, fileExt };
    return singleResult(VistaImageInfo.class, VistaImagingDao.class, "addVistaImage", params);
  }
  
  public ServiceResponse<String> linkNoteToImage(ISecurityContext securityContext, String imageIen, String tiuNoteIen) {
    setSecurityContext(securityContext);
    Object[] params = { imageIen, tiuNoteIen };
    return singleResult(String.class, VistaImagingDao.class, "linkNoteToImage", params);
  }

  public CollectionServiceResponse<String> importVistaImage(ISecurityContext securityContext, List<String> nodeList) {
    setSecurityContext(securityContext);
    Object[] params = { nodeList};
    return collectionResult(String.class, VistaImagingDao.class, "importVistaImage", params);
  }
  
  public CollectionServiceResponse<RemoteStation> getRemoteSites(ISecurityContext securityContext, String dfn) {
    setSecurityContext(securityContext);
    Object[] params = { dfn};
    return collectionResult(RemoteStation.class, RemoteSitesDao.class, "getRemoteSites", params);
  }

}
