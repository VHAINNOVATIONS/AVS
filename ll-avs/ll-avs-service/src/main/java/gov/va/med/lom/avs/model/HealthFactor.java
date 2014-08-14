package gov.va.med.lom.avs.model;

public class HealthFactor {

  private String ien;
  private String healthFactorIen;
  private String healthFactorName;
  private String patientDfn;
  private String visitIen;
  private String visitDate;
  private String levelSeverity;
  private String auditTrail;
  private String comment;
  private String code;
  
  public String getIen() {
    return ien;
  }
  public void setIen(String ien) {
    this.ien = ien;
  }
  public String getHealthFactorIen() {
    return healthFactorIen;
  }
  public void setHealthFactorIen(String healthFactorIen) {
    this.healthFactorIen = healthFactorIen;
  }
  public String getHealthFactorName() {
    return healthFactorName;
  }
  public void setHealthFactorName(String healthFactorName) {
    this.healthFactorName = healthFactorName;
  }
  public String getPatientDfn() {
    return patientDfn;
  }
  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }
  public String getVisitIen() {
    return visitIen;
  }
  public void setVisitIen(String visitIen) {
    this.visitIen = visitIen;
  }
  public String getVisitDate() {
    return visitDate;
  }
  public void setVisitDate(String visitDate) {
    this.visitDate = visitDate;
  }
  public String getLevelSeverity() {
    return levelSeverity;
  }
  public void setLevelSeverity(String levelSeverity) {
    this.levelSeverity = levelSeverity;
  }
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }
  public String getAuditTrail() {
    return auditTrail;
  }
  public void setAuditTrail(String auditTrail) {
    this.auditTrail = auditTrail;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  
}
