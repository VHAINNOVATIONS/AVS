package gov.va.med.lom.login.struts.session;

import javax.servlet.http.HttpServletRequest;

import gov.va.med.authentication.kernel.LoginUserInfoVO;

public class SessionUtil {

  public static boolean isActiveSession(HttpServletRequest request) {
    return getStrutsSession(request) != null;
  }
  
  public static StrutsSession getStrutsSession(HttpServletRequest request) {
    
    StrutsSession session = (StrutsSession)request.getSession().getAttribute(StrutsSession.class.getCanonicalName());
    LoginUserInfoVO loginUserInfo = (LoginUserInfoVO)request.getSession().getAttribute(LoginUserInfoVO.SESSION_KEY);
        
    if (session == null) {
      if (loginUserInfo == null) {
        return null;
      }
    
      // setup a new session with basic information 
      session = new StrutsSession();
      session.setStationNo(loginUserInfo.getLoginStationNumber());
      session.setStationName(loginUserInfo.getDivisionName());
      session.setUserVpid(loginUserInfo.getUserVpid());
      session.setUserDuz(loginUserInfo.getUserDuz());
      session.setUserName01(loginUserInfo.getUserName01());
      session.setUserNameDisplay(loginUserInfo.getUserNameDisplay());
      request.getSession().setAttribute(StrutsSession.class.getCanonicalName(), session);    
    }

    return session;
  }
  
  public static LoginUserInfoVO getLoginUserInfo(HttpServletRequest request) {
    return (LoginUserInfoVO)request.getSession().getAttribute(LoginUserInfoVO.SESSION_KEY);
  }
  
}
