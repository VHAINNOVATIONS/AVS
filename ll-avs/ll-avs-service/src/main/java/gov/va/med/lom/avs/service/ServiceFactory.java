package gov.va.med.lom.avs.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ServiceFactory {
  
  private static Context ctx;
  
	public enum SERVICE {	  
	  SETTINGS("ll-avs-1.3/gov.va.med.lom.avs.SettingsService/local"),
	  SHEET("ll-avs-1.3/gov.va.med.lom.avs.SheetService/local"),
	  SHEET_REMOTE("ll-avs-1.3/gov.va.med.lom.avs.SheetService/remote"),
	  LAB_VALUES_LOCAL("ll-avs-1.3/gov.va.med.lom.avs.LabValuesService/local");

    private String jndi;
    
    private SERVICE(String jndi){
      this.jndi = jndi;
    }
    
    public String getJndi(){
      return jndi;
    }
  };
  
  private static Map<SERVICE, BaseService> serviceMap;
  
  static {
    
    serviceMap = new HashMap<SERVICE, BaseService>();
    for(SERVICE s : SERVICE.values()) {
      serviceMap.put(s, null);
    }
    
    try {
      Properties props = new Properties();
      props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
      props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
      props.put("java.naming.provider.url", "localhost:1099");
      ctx = new InitialContext(props);
    } catch(NamingException e) {
      System.out.println("error creating context");
      e.printStackTrace();
    }
    
  }
    
  private static BaseService fetchService(SERVICE s, boolean refresh) {
    
    BaseService service = serviceMap.get(s);
    
    if(refresh == false && service != null && isAlive(service)) {
      return service;
    }
    
    try {
      service = (BaseService)ctx.lookup(s.getJndi());
    } catch(javax.naming.CommunicationException e) {
      e.printStackTrace();
      throw new RuntimeException("unable to connect with application server");
    } catch (NamingException e) {
      e.printStackTrace();
      service = null;
    }
    serviceMap.put(s,service);
    return service;
  }
  
  private static boolean isAlive(BaseService s) {
    try {
      return s.isAlive();
    } catch (Exception e) {
      return false;
    }
  }

	public static LabValuesService getLabValuesService() {
		return (LabValuesService)fetchService(SERVICE.LAB_VALUES_LOCAL, false);
	}

	public static SettingsService getSettingsService() {
		return (SettingsService)fetchService(SERVICE.SETTINGS, false);
	}

	public static SheetService getSheetService() {
		return (SheetService)fetchService(SERVICE.SHEET, false);
	}
	
}
