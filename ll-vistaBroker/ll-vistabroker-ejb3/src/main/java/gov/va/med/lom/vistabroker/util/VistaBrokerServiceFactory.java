package gov.va.med.lom.vistabroker.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import gov.va.med.lom.vistabroker.service.*;

public class VistaBrokerServiceFactory {
  
  private static final String VISTABROKER = "ll-vistabroker";
  private static final String JNP_URL = "java.naming.provider.url";
  private static Context ctx;
  
  public enum SERVICE {
    
    ADMIN("admin.jndi"),
    AUTH("auth.jndi"),
    DDR("ddr.jndi"),
    LISTS("lists.jndi"),
    MISC("misc.jndi"),
    PATIENT("patient.jndi"),
    USER("user.jndi");
    
    private String name;
    
    private SERVICE(String name){
      this.name = name;
    }
    
    public String getName(){
      return name;
    }
  };
  
  private static Map<SERVICE, Service> serviceMap = new HashMap<SERVICE, Service>();
  private static Map<SERVICE, String> serviceJndiMap = new HashMap<SERVICE, String>();
  
  static {
    
    ResourceBundle res = ResourceBundle.getBundle("vistabroker");
    
    // adminJndi
    String jndiPrefix = VISTABROKER + "-" + res.getString("version") + "/";
    
    for(SERVICE s : SERVICE.values()) {
      serviceMap.put(s, null);
      serviceJndiMap.put(s, jndiPrefix + res.getString(s.getName()));
    }
    
    try {
      ctx = new InitialContext();
      Hashtable<?,?> env = ctx.getEnvironment();
      if (!env.containsKey(JNP_URL)) {
        ctx.addToEnvironment(JNP_URL, "localhost:1099");
      }
    } catch(NamingException e) {
      e.printStackTrace();
    }
    
  }
    
  private static Service fetchService(SERVICE s, boolean refresh) {
    
    Service service = serviceMap.get(s);
    
    if(refresh == false && service != null && isAlive(service)) {
      return service;
    }
    
    try {
      service = (Service)ctx.lookup(serviceJndiMap.get(s));
    } catch(javax.naming.CommunicationException e) {
      e.printStackTrace();
      throw new RuntimeException("unable to connect with application server");
    } catch (NamingException e) {
      e.printStackTrace();
      service = null;
    }
    serviceMap.put(s, service);
    return service;
  }
  
  private static boolean isAlive(Service s) {
    try {
      return s.isAlive();
    } catch (Exception e) {
      return false;
    }
  }
  
  public static AdminVBService getAdminVBService(){
    return (AdminVBService)fetchService(SERVICE.ADMIN, false);
  }
  
  public static AuthVBService getAuthVBService(){
    return (AuthVBService)fetchService(SERVICE.AUTH, false);
  }
  
  public static DdrVBService getDdrVBService() {
    return (DdrVBService)fetchService(SERVICE.DDR, false);
  }
  
  public static ListsVBService getListsVBService(){
    return (ListsVBService)fetchService(SERVICE.LISTS, false);
  }
  
  public static MiscVBService getMiscVBService(){
    return (MiscVBService)fetchService(SERVICE.MISC, false);
  }
  
  public static PatientVBService getPatientVBService(){
    return (PatientVBService)fetchService(SERVICE.PATIENT, false);
  }
  
  public static UserVBService getUserVBService(){
    return (UserVBService)fetchService(SERVICE.USER, false);
  }
  
}