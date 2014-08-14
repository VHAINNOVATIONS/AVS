package gov.va.med.lom.avs.model;

public class MedicationRdv {

  private String id;
  private String name;
  private String rxNumber;
  private String rxIen;
  private String drugId;
  private String quantity;
  private String expirationDate;
  private String issueDate;
  private String startDate;
  private String stopDate;
  private String orderId;
  private String status;
  private String refills;
  private boolean outpatient;
  private boolean inpatient;
  private boolean iv;
  private boolean unitDose;
  private boolean nonVA;
  private boolean imo;
  private String lastFillDate;
  private String remaining;
  private String stationNo;
  private String stationMoniker;  
  private String stationName; 
  private String provider;
  private String cost;
  private String sig;
  private String type;
  private String additives;
  private String solution;
  private String rate;
  private String route;
  private String dose;
  private String instruction;
  private String comment;
  private String dateDocumented;
  private String documentor;
  private String detail;
  private String schedule;
  private String daysSupply;
  private String drugClassCode;
  private String drugClass;
  private String action;
  
  public MedicationRdv() {
    this.action = ""; 
  }
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getRxNumber() {
    return rxNumber;
  }
  public void setRxNumber(String rxNumber) {
    this.rxNumber = rxNumber;
  }
  public String getQuantity() {
    return quantity;
  }
  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }
  public String getExpirationDate() {
    return expirationDate;
  }
  public void setExpirationDate(String expirationDate) {
    this.expirationDate = expirationDate;
  }
  public String getIssueDate() {
    return issueDate;
  }
  public void setIssueDate(String issueDate) {
    this.issueDate = issueDate;
  }
  public String getStartDate() {
    return startDate;
  }
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
  public String getStopDate() {
    return stopDate;
  }
  public void setStopDate(String stopDate) {
    this.stopDate = stopDate;
  }
  public String getOrderId() {
    return orderId;
  }
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getRefills() {
    return refills;
  }
  public void setRefills(String refills) {
    this.refills = refills;
  }
  public boolean isOutpatient() {
    return outpatient;
  }
  public void setOutpatient(boolean outpatient) {
    this.outpatient = outpatient;
  }
  public boolean isInpatient() {
    return inpatient;
  }
  public void setInpatient(boolean inpatient) {
    this.inpatient = inpatient;
  }
  public boolean isIv() {
    return iv;
  }
  public void setIv(boolean iv) {
    this.iv = iv;
  }
  public boolean isUnitDose() {
    return unitDose;
  }
  public void setUnitDose(boolean unitDose) {
    this.unitDose = unitDose;
  }
  public boolean isNonVA() {
    return nonVA;
  }
  public void setNonVA(boolean nonVA) {
    this.nonVA = nonVA;
  }
  public boolean isImo() {
    return imo;
  }
  public void setImo(boolean imo) {
    this.imo = imo;
  }
  public String getLastFillDate() {
    return lastFillDate;
  }
  public void setLastFillDate(String lastFillDate) {
    this.lastFillDate = lastFillDate;
  }
  public String getRemaining() {
    return remaining;
  }
  public void setRemaining(String remaining) {
    this.remaining = remaining;
  }
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getStationName() {
    return stationName;
  }
  public void setStationName(String stationName) {
    this.stationName = stationName;
  }
  public String getProvider() {
    return provider;
  }
  public void setProvider(String provider) {
    this.provider = provider;
  }
  public String getCost() {
    return cost;
  }
  public void setCost(String cost) {
    this.cost = cost;
  }
  public String getSig() {
    return sig;
  }
  public void setSig(String sig) {
    this.sig = sig;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getAdditives() {
    return additives;
  }
  public void setAdditives(String additives) {
    this.additives = additives;
  }
  public String getSolution() {
    return solution;
  }
  public void setSolution(String solution) {
    this.solution = solution;
  }
  public String getRate() {
    return rate;
  }
  public void setRate(String rate) {
    this.rate = rate;
  }
  public String getRoute() {
    return route;
  }
  public void setRoute(String route) {
    this.route = route;
  }
  public String getDose() {
    return dose;
  }
  public void setDose(String dose) {
    this.dose = dose;
  }
  public String getInstruction() {
    return instruction;
  }
  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }
  public String getDateDocumented() {
    return dateDocumented;
  }
  public void setDateDocumented(String dateDocumented) {
    this.dateDocumented = dateDocumented;
  }
  public String getDocumentor() {
    return documentor;
  }
  public void setDocumentor(String documentor) {
    this.documentor = documentor;
  }
  public String getDetail() {
    return detail;
  }
  public void setDetail(String detail) {
    this.detail = detail;
  }
  public String getSchedule() {
    return schedule;
  }
  public void setSchedule(String schedule) {
    this.schedule = schedule;
  }
  public String getDaysSupply() {
    return daysSupply;
  }
  public void setDaysSupply(String daysSupply) {
    this.daysSupply = daysSupply;
  }
  public String getDrugId() {
    return drugId;
  }
  public void setDrugId(String drugId) {
    this.drugId = drugId;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getDrugClassCode() {
    return drugClassCode;
  }

  public void setDrugClassCode(String drugClassCode) {
    this.drugClassCode = drugClassCode;
  }

  public String getDrugClass() {
    return drugClass;
  }

  public void setDrugClass(String drugClass) {
    this.drugClass = drugClass;
  }

  public String getRxIen() {
    return rxIen;
  }

  public void setRxIen(String rxIen) {
    this.rxIen = rxIen;
  }

  public String getStationMoniker() {
    return stationMoniker;
  }

  public void setStationMoniker(String stationMoniker) {
    this.stationMoniker = stationMoniker;
  }
  
}
