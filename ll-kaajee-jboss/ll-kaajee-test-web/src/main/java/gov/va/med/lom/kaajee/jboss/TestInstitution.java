package gov.va.med.lom.kaajee.jboss;

import gov.va.med.term.access.Institution;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestInstitution {

    
    private static Log log = LogFactory.getLog(TestInstitution.class);
    
    public static String siteName(String stationNo){
        
        Institution inst = Institution.factory.obtainByStationNumber("605");
        
        if(inst == null) {
            log.error("unable to fetch instituition");
            return "";
        }
        
        return inst.getName();
        
    }
    
    public static Institution[] sites(){
      
      Institution[] instArr = Institution.factory.obtain();
      
      if(instArr == null) {
          log.error("unable to fetch instituition list");
          return null;
      }
      
      return instArr;
      
  }
    
    
    
    
    
}
