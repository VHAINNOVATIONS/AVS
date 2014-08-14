package gov.va.med.lom.javaBroker.rpc.user.models;

import java.util.Date;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class Employee extends BaseBean implements Serializable {
  
  private String name;
  private int stationNo;
  private String title;
  private String extension;
  private String pager;
  private String room;
  private String ssn;
  private String mailCode;
  private long paidId;
  private Date terminationDate;
  private Date lastSignon;
  private int serviceIen;
  private String degree;
  private String program;
  
  public Employee() {
    this.name = null;
    this.stationNo = 0;
    this.title = null;
    this.extension = null;
    this.pager = null;
    this.room = null;
    this.ssn = null;
    this.mailCode = null;
    this.paidId = 0;
    this.terminationDate = null;
    this.lastSignon = null;
    this.serviceIen = 0;
    this.degree = null;
    this.program = null;
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

  public int getServiceIen() {
    return serviceIen;
  }

  public void setServiceIen(int serviceIen) {
    this.serviceIen = serviceIen;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public int getStationNo() {
    return stationNo;
  }

  public void setStationNo(int stationNo) {
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
  
}
