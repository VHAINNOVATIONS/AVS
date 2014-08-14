package gov.va.med.lom.login.struts.session;

import java.util.Date;

/*
 * Used to store global session information. 
 */
public class StrutsSession {
		
  private Date loginTime;
	private String stationNo;	
	private String stationName;
	private String userDuz;
	private String userVpid;
	private String userName01;
	private String userNameDisplay;
	
	public StrutsSession() {
	  this.loginTime = new Date();
	}
	
  public Date getLoginTime() {
    return loginTime;
  }
  
  public void setLoginTime(Date loginTime) {
    this.loginTime = loginTime;
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
  
  public String getUserDuz() {
    return userDuz;
  }
  
  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }

  public String getUserVpid() {
    return userVpid;
  }

  public void setUserVpid(String userVpid) {
    this.userVpid = userVpid;
  }

  public String getUserName01() {
    return userName01;
  }

  public void setUserName01(String userName01) {
    this.userName01 = userName01;
  }

  public String getUserNameDisplay() {
    return userNameDisplay;
  }

  public void setUserNameDisplay(String userNameDisplay) {
    this.userNameDisplay = userNameDisplay;
  }
  
}
