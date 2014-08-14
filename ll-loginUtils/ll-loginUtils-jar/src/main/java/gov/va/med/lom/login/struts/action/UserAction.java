package gov.va.med.lom.login.struts.action;

import gov.va.med.lom.json.struts.action.BaseAction;

import org.apache.struts2.interceptor.ServletRequestAware;
import com.opensymphony.xwork2.Preparable;

import gov.va.med.authentication.kernel.LoginUserInfoVO;

import gov.va.med.lom.login.struts.session.SessionUtil;

public class UserAction extends BaseAction implements ServletRequestAware, Preparable {

  /*
   * Action Methods
   */  
  
  public void prepare() throws Exception {
    super.prepare();    
  }  
  
	public String userInfo() {
	  
	  LoginUserInfoVO loginUserInfo = SessionUtil.getLoginUserInfo(request);
	  
  	if (loginUserInfo == null) {
			return userNotAllowed();
		} else {
			return setJson(loginUserInfo);
		}	
		
	}
	
}
