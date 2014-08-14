package gov.va.med.lom.vistabroker.user.data;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {
  
  private String duz;
  private String name;
  private String stationNo;
  private String title;
  private String extension;
  private String pager;
  private String room;
  private String ssn;
  private String mailCode;
  private long paidId;
  private Date terminationDate;
  private String officePhone;
  private Date lastSignon;
  private String serviceIen;
  private String degree;
  private String program;
  private String serviceName;
  private String sectionIen;
  private String sectionName;
  
  public String getServiceName() {
	return serviceName;
}

public void setServiceName(String serviceName) {
	this.serviceName = serviceName;
}

public String getSectionIen() {
	return sectionIen;
}

public void setSectionIen(String sectionIen) {
	this.sectionIen = sectionIen;
}

public String getSectionName() {
	return sectionName;
}

public void setSectionName(String sectionName) {
	this.sectionName = sectionName;
}

public Employee() {
    this.name = null;
    this.stationNo = null;
    this.title = null;
    this.extension = null;
    this.pager = null;
    this.room = null;
    this.ssn = null;
    this.mailCode = null;
    this.paidId = 0;
    this.terminationDate = null;
    this.lastSignon = null;
    this.serviceIen = "0";
    this.degree = null;
    this.program = null;
  }

  public String getDuz() {
    return duz;
  }

  public void setDuz(String duz) {
    this.duz = duz;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public Date getLastSignon() {
    return lastSignon;
  }

  public void setLastSignon(Date lastSignon) {
    this.lastSignon = lastSignon;
  }
  
  public Date getTerminationDate() {
    return terminationDate;
  }

  public void setTerminationDate(Date terminationDate) {
    this.terminationDate = terminationDate;
  }
  
  public String getMailCode() {
    return mailCode;
  }

  public void setMailCode(String mailCode) {
    this.mailCode = mailCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPager() {
    return pager;
  }

  public void setPager(String pager) {
    this.pager = pager;
  }

  public long getPaidId() {
    return paidId;
  }

  public void setPaidId(long paidId) {
    this.paidId = paidId;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public String getServiceIen() {
    return serviceIen;
  }

  public void setServiceIen(String serviceIen) {
    this.serviceIen = serviceIen;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public String getStationNo() {
    return stationNo;
  }

  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getProgram() {
    return program;
  }

  public void setProgram(String program) {
    this.program = program;
  }

public String getOfficePhone() {
    return officePhone;
}

public void setOfficePhone(String officePhone) {
    this.officePhone = officePhone;
}
  
}
