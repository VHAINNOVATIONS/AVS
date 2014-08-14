package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class Address extends BaseBean implements Serializable {

  private String street1;
  private String street2;
  private String street3;
  private String city;
  private int stateNumber;
  private String state;
  private String zipCode;
  private int countyNumber;
  private String county;
  private String phoneNumber;
  private int flagNumber;
  private String flag;
  private String email;
  
  public Address() {
    this.street1 = null;
    this.street2 = null;
    this.street3 = null;
    this.city = null;
    this.stateNumber = 0;
    this.state = null;
    this.zipCode = null;
    this.countyNumber = 0;
    this.county = null;
    this.phoneNumber = null;
    this.flagNumber = 0;
    this.flag = null;
    this.email = null;    
  }
  
  public String getCity() {
    return city;
  }
  
  public void setCity(String city) {
    this.city = city;
  }
  
  public String getCounty() {
    return county;
  }
  
  public void setCounty(String county) {
    this.county = county;
  }
  
  public int getCountyNumber() {
    return countyNumber;
  }
  
  public void setCountyNumber(int countyNumber) {
    this.countyNumber = countyNumber;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getFlag() {
    return flag;
  }
  
  public void setFlag(String flag) {
    this.flag = flag;
  }
  
  public int getFlagNumber() {
    return flagNumber;
  }
  
  public void setFlagNumber(int flagNumber) {
    this.flagNumber = flagNumber;
  }
  
  public String getPhoneNumber() {
    return phoneNumber;
  }
  
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  
  public String getState() {
    return state;
  }
  
  public void setState(String state) {
    this.state = state;
  }
  
  public int getStateNumber() {
    return stateNumber;
  }
  
  public void setStateNumber(int stateNumber) {
    this.stateNumber = stateNumber;
  }
  
  public String getStreet1() {
    return street1;
  }
  
  public void setStreet1(String street1) {
    this.street1 = street1;
  }
  
  public String getStreet2() {
    return street2;
  }
  
  public void setStreet2(String street2) {
    this.street2 = street2;
  }
  
  public String getStreet3() {
    return street3;
  }
  
  public void setStreet3(String street3) {
    this.street3 = street3;
  }
  
  public String getZipCode() {
    return zipCode;
  }
  
  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }
  
}
