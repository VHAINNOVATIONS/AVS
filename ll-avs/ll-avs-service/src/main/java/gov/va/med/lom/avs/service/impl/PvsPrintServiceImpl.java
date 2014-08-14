package gov.va.med.lom.avs.service.impl;

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Schedule;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.jboss.ejb3.annotation.TransactionTimeout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;


import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.util.FMDateUtils;
import gov.va.med.lom.vistabroker.patient.data.Patient;

import gov.va.med.lom.avs.dao.PvsClinicDao;
import gov.va.med.lom.avs.dao.PvsPrintLogDao;
import gov.va.med.lom.avs.model.FacilityPrefs;
import gov.va.med.lom.avs.model.PvsClinic;
import gov.va.med.lom.avs.model.PvsPrintLog;
import gov.va.med.lom.avs.service.SettingsService;

@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class PvsPrintServiceImpl {

  private static final Log log = LogFactory.getLog(PvsPrintServiceImpl.class);
  private static final String LOGOUT_URI = "http://localhost/avs/w/login/logout";
  
  @EJB
  private PvsClinicDao pvsClinicDao;
  @EJB
  private PvsPrintLogDao pvsPrintLogDao;
  @EJB
  private SettingsService settingsService;
  
  @Schedule(hour="03", minute="00", persistent=false)
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  @TransactionTimeout(7200)
  private void processPvsPrintouts(Timer timer) {
    
    ResourceBundle res = ResourceBundle.getBundle("gov.va.med.lom.avs.avs");
    if (!res.containsKey("avs.pvs") || !res.getString("avs.pvs").equals("true")) {
      return;
    }

    log.info("--------------------------------------");
    log.info("Commencing pre-visit summary printouts.");
    
    HashMap<String, List<PvsClinic>> clinicsMap = new HashMap<String, List<PvsClinic>>();
    List<PvsClinic> clinics = pvsClinicDao.findAll();
    for (PvsClinic clinic : clinics) {
      List<PvsClinic> clinicsList = clinicsMap.get(clinic.getStationNo());
      if (clinicsList == null) {
        clinicsList = new ArrayList<PvsClinic>();
        clinicsMap.put(clinic.getStationNo(), clinicsList);
      }
      clinicsList.add(clinic);
    }
    
    Set<String> set = clinicsMap.keySet();
    log.info("total # stations: " + set.size());
    Iterator<String> it = set.iterator();
    while (it.hasNext()) {
      
      String stationNo = it.next();
      FacilityPrefs facilityPrefs = settingsService.getFacilityPrefs(stationNo).getPayload();
      ISecurityContext securityContext = SecurityContextFactory.createDuzSecurityContext(facilityPrefs.getFacilityNo(), facilityPrefs.getServiceDuz());
      
      // Get today's date and convert to ANSI string
      TimeZone tz = TimeZone.getTimeZone(facilityPrefs.getTimeZone());
      Calendar today = Calendar.getInstance(tz);
      
      clinics = clinicsMap.get(stationNo);
      
      log.info("total # clinics for station " + stationNo + ": " + clinics.size());
      
      CloseableHttpClient httpclient = null;
      
      if (clinics.size() > 0) {
        try {
          httpclient = login(facilityPrefs.getFacilityNo(), facilityPrefs.getServiceDuz());
        } catch(Exception e) {
          log.error(e);
        }
      } else {
        log.info("No clinics configured. Aborting.");
        return;
      }
      
      try {
      
        for (PvsClinic clinic : clinics) {
      
          log.info("clinic: " + clinic.getClinicName());
          
          // Get the list of patients for the location
          List<Patient> patients = null;
          if (clinic.isInpatient()) {
            patients = (List<Patient>)settingsService.listPatientsByWard(securityContext, clinic.getClinicIen()).getCollection();
          } else {
            patients = (List<Patient>)settingsService.listPatientsByClinic(securityContext, clinic.getClinicIen(), today.getTime(), today.getTime()).getCollection();
          }
          
          log.info("total # patients for location " + clinic.getClinicName() + ": " + patients.size());

          for (Patient patient : patients) {
            log.info(patient.getDate() + " - " + patient.getDateStr());
            double datetime = FMDateUtils.dateTimeToFMDateTime(patient.getDate());
            try {
              URI uri = new URI("http", "localhost", "/avs/w/s/avs/printPdf.action",
                  String.format("docType=pvs&facilityNo=%s&userDuz=%s&patientDfn=%s&datetime=%f&locationIen=%s&printerIen=%s&printerIp=%s&printerName=%s", 
                  facilityPrefs.getFacilityNo(), facilityPrefs.getServiceDuz(), patient.getDfn(), datetime, clinic.getClinicIen(), 
                  clinic.getPrinterIen(), clinic.getPrinterIp(), clinic.getPrinterName() ), null);
              String results = sendRequest(httpclient, uri);
              PvsPrintLog printLog = new PvsPrintLog();
              printLog.setClinic(clinic);
              printLog.setPatientDfn(patient.getDfn());
              if (results != null) {
                printLog.setPrinted(true);
                log.info(results);
              } else {
                printLog.setPrinted(false);
                log.error("Error sending PVS request.");
              }
              pvsPrintLogDao.save(printLog);
            } catch(Exception e) {
              log.error(e);
            }            
          }
        }
        
      } finally {
        try {
          httpclient.close();
          CloseableHttpResponse response = null;
          try {
            URI uri = new URI(LOGOUT_URI, null, null);
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(uri);
            response = httpclient.execute(httpPost);
          } finally {
            response.close();
          }        
        } catch(Exception e) {}
      }  
    }
        
    log.info("Pre-Visit Summary printouts complete.");
    log.info("--------------------------------------");
  }
  
  private static CloseableHttpClient login(String facilityNo, String userDuz) throws Exception {
    
    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
    
    CloseableHttpResponse response = null;
    try {
      try {
        URI uri = new URI("http", "localhost", "/avs/w/login/LoginController",
            String.format("institution=%s&userDuz=%s", facilityNo, userDuz), null);
        
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(uri);
        response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream instream = entity.getContent();
        try {
          try {
            instream.read();
            String result = convertStreamToString(instream);
            log.info("Login Result: " + result);
          } catch(Exception e) {
            e.printStackTrace();
          }
        } finally {
          try { 
            instream.close(); 
          } catch (Exception ignore) {}
        }
      } finally {
        response.close();
        if (response.getStatusLine().getStatusCode() != 200) {
          throw new Exception("Login failed");
        }
      }
    } catch(Exception e) {
      throw e;
    }
    return httpclient;
  }
    
  private static String sendRequest(CloseableHttpClient httpclient, URI uri) {
  
    try {
      HttpPost httpPost = new HttpPost(uri);
      httpPost.setURI(uri);
      HttpResponse response = httpclient.execute(httpPost);

      // Get the response
      BufferedReader rd = new BufferedReader
        (new InputStreamReader(response.getEntity().getContent()));
        
      StringBuffer sb = new StringBuffer();
      String line = "";
      while ((line = rd.readLine()) != null) {
        sb.append(line);
      }
      
      return sb.toString();
    } catch(Exception e) {
      System.err.println(e.getMessage());
    }
    
    return null;
  }
  
  private static String convertStreamToString(InputStream is) throws Exception {
    
    if (is != null) {
      Writer writer = new StringWriter();
      char[] buffer = new char[1024];
      try {
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1) {
          writer.write(buffer, 0, n);
        }
      } finally {
        is.close();
      }
      return writer.toString();
    } else {        
      return "";
    }    
    
  }  
  
}
