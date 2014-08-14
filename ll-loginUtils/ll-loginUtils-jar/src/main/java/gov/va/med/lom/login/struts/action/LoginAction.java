package gov.va.med.lom.login.struts.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.struts2.interceptor.ServletRequestAware;
import com.opensymphony.xwork2.Preparable;

import gov.va.med.lom.kaajee.jboss.security.auth.ConfigurationVO;
import gov.va.med.lom.kaajee.jboss.model.json.InstitutionJson;
import gov.va.med.authentication.kernel.VistaDivisionVO;

import gov.va.med.lom.json.struts.action.BaseAction;

public class LoginAction extends BaseAction implements ServletRequestAware, Preparable {
    
  /*
   * Action Methods
   */
  public void prepare() throws Exception {
    super.prepare();    
  }   
  
  public String stations() {
    
    ConfigurationVO kaajeeConfig = ConfigurationVO.getInstance();
    Map<String, VistaDivisionVO> instMap = kaajeeConfig.getInstitutionMap();
    Set<String> instSet = instMap.keySet();
    Iterator<String> instSetIterator = instSet.iterator();
    List<InstitutionJson> list = new ArrayList<InstitutionJson>();
    
    while (true) {
      try {
        String stationNumber = (String) instSetIterator.next();
        VistaDivisionVO im = (VistaDivisionVO) instMap.get(stationNumber);
        InstitutionJson ij = new InstitutionJson();
        ij.setStationNumber(im.getNumber());
        ij.setDivisionName(im.getName());
        list.add(ij);
      } catch (NoSuchElementException e) {
        break;
      }
    }
    return setJson(list);
    
  }  
  
  public String logout() {
    
    getServletRequest().getSession().invalidate();
    return success("user signed out");
    
  }
  
}
