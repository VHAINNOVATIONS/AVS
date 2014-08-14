package gov.va.med.lom.json.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import gov.va.med.lom.json.util.JsonResponse;

public class BaseAction extends ActionSupport implements ServletRequestAware, Preparable, ServletResponseAware {
    
  protected static final Log log = LogFactory.getLog(BaseAction.class);
  
  protected HttpServletRequest request;
  protected HttpServletResponse response;

  public void prepare() throws Exception {
  }  
    
  /*
   * Embeds root and other properties into serialized json and sends to client.
   * Use this method to serialize an object that does not extend BaseJson.
   */
  protected String setJson(Object obj) {
    boolean multi = false;
    int size = 0;
    if (obj != null) {
      Class cls = obj.getClass();    
      if ((cls == ArrayList.class) || (cls == List.class)) {
        size = ((List)obj).size();
        multi = true;
      } else if (cls == java.lang.reflect.Array.class) {
        size = java.lang.reflect.Array.getLength(obj);
        multi = true;
      }
      if (multi) {
        JsonResponse.embedRoot(response, true, size, "root", false, obj);
      } else {
        JsonResponse.embedRoot(response, true, 1, "root", true, obj);
      }
    }
    return SUCCESS;
  }
  
  /*
   * Serializes the object as json and sends to client.
   * Does not embed a root or other properties into the serialized json.
   * Use this method to serialize an object that extends BaseJson.
   */
  protected String writeJson(Object obj) {
    JsonResponse.writeJson(response, obj);
    return SUCCESS;
  }  
   
  protected String userUnauthorized() {
    JsonResponse.userUnauthorized(response);
    return SUCCESS;
  }
  
  protected String userNotAllowed() {
    JsonResponse.userNotAllowed(response);
    return SUCCESS;
  }
  
  protected String error(Exception ex) {
    JsonResponse.error(response, ex.getMessage());
    return SUCCESS;
  }
  
  protected String error(String error) {
    JsonResponse.error(response, error);
    return SUCCESS;
  }   
  
  protected String success() {
    JsonResponse.success(response);
    return SUCCESS;
  }  
  
  protected String success(String msg) {
    JsonResponse.success(response, msg);
    return SUCCESS;
  }      
  
  public String doNothing() {
    return SUCCESS;
  }  
  
  /*
   * XML serialization
   */
  protected String writeXml(Object obj) {
    JsonResponse.writeXml(response, obj);
    return SUCCESS;
  } 
  
  /*
   * Gets the value of a request parameter
   */
  public String getParameter(String paramName) {
    return request.getParameter(paramName);
  }

  /*
   * Returns a list of strings for values of this parameter.  Returns
   * a null if there is no such parameter.
   */
  public String[] getParameterValues(String paramName) {
    return request.getParameterValues(paramName);
  }

  /*
   * Returns a list of strings for values of this parameter.  Returns
   * an empty list if there is no such parameter.  Never returns null.
   */
  public List<String> getParameterValueList(String paramName) {
    List<String> result = new ArrayList<String>();
    String[] values = request.getParameterValues(paramName);
    if ( values != null ) {
      for(String val : values) {
        result.add(val);
      }
    }
    return result;
  }

  public Boolean getParameterAsBoolean(String paramName) {
    return new Boolean(request.getParameter(paramName));
  }
  
  public Long getParameterAsLong(String paramName) {
    try {
      return new Long(request.getParameter(paramName));
    } catch (Exception e) {
      log.debug("Error parsing parameter:" + paramName + " value:" + getParameter(paramName), e);
      return null;
    }
  }
  
  public boolean hasAttribute(String attrName){
    return getServletRequest().getSession().getAttribute(attrName) == null ?  false :  true;
  }
  
  public void setAttribute(String attrName, Object o){
    getServletRequest().setAttribute(attrName, o);
  }

  public Object getSessionAttribute(String attrName) {
    return getServletRequest().getSession().getAttribute(attrName);
  }

  public void setSessionAttribute(String attrName, Object value) {
    getServletRequest().getSession().setAttribute(attrName, value);
  }

  public void removeSessionAttribute(String attrName) {
    getServletRequest().getSession().removeAttribute(attrName);
  }
  
  public void setServletResponse(HttpServletResponse httpServletResponse) {
    this.response = httpServletResponse;
  }

  public HttpServletResponse getServletResponse() {
    return this.response;
  }
  
  public final void setServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public final HttpServletRequest getServletRequest() {
    return request;
  }  
 
}
