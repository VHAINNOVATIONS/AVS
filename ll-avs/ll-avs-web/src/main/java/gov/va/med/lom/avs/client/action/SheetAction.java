package gov.va.med.lom.avs.client.action;

import gov.va.med.lom.avs.client.model.PatientInfoJson;
import gov.va.med.lom.avs.client.model.VitalSignJson;
import gov.va.med.lom.avs.client.model.AllergiesReactionsJson;
import gov.va.med.lom.avs.client.model.AppointmentJson;
import gov.va.med.lom.avs.client.model.AvsJson;
import gov.va.med.lom.avs.client.model.ClientStringsJson;
import gov.va.med.lom.avs.client.model.ClinicVisitedJson;
import gov.va.med.lom.avs.client.model.DataModel;
import gov.va.med.lom.avs.client.model.DiagnosisJson;
import gov.va.med.lom.avs.client.model.Header;
import gov.va.med.lom.avs.client.model.OrderJson;
import gov.va.med.lom.avs.client.model.MedicationJson;
import gov.va.med.lom.avs.client.model.VistaPrinterJson;
import gov.va.med.lom.avs.client.model.DiscreteItemJson;
import gov.va.med.lom.avs.client.model.ProcedureJson;
import gov.va.med.lom.avs.client.model.PrimaryCareTeamMemberJson;
import gov.va.med.lom.avs.client.thread.*;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.web.util.WebCacheUtil;
import gov.va.med.lom.avs.model.BasicDemographics;
import gov.va.med.lom.avs.model.EncounterLocation;
import gov.va.med.lom.avs.model.EncounterProvider;
import gov.va.med.lom.avs.model.FacilityPrefs;
import gov.va.med.lom.avs.model.PceData;
import gov.va.med.lom.avs.model.Service;
import gov.va.med.lom.avs.model.EncounterCacheMongo;
import gov.va.med.lom.avs.model.EncounterInfo;
import gov.va.med.lom.avs.model.Encounter;
import gov.va.med.lom.avs.model.UserSettings;
import gov.va.med.lom.avs.model.PatientInformation;
import gov.va.med.lom.avs.model.FacilityHealthFactor;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SettingsService;
import gov.va.med.lom.avs.service.SheetService;
import gov.va.med.lom.avs.util.SheetConfig;
import gov.va.med.lom.avs.util.StringResources;

import gov.va.med.lom.foundation.util.Precondition;
import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.Printer;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.login.struts.session.SessionUtil;

import gov.va.med.lom.vistabroker.util.Hash;
import gov.va.med.lom.vistabroker.user.data.VistaUser;
import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;
import gov.va.med.lom.vistabroker.patient.data.NewTiuNote;
import gov.va.med.lom.vistabroker.patient.data.TiuNote;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.service.UserVBService;
import gov.va.med.lom.vistabroker.service.MiscVBService;
import gov.va.med.lom.vistabroker.util.FMDateUtils;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import org.ghost4j.converter.PSConverter;
import org.ghost4j.document.PDFDocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

public class SheetAction extends BaseCardAction {

  private static final Log log = LogFactory.getLog(SheetAction.class);

  private static final long serialVersionUID = 0;
  private static final String BASE_URI = "http://localhost";
  private static final String SHEET_CSS_PATH = "/avs/css/sheet.css";
  private static final String PDF_CSS_PATH = "/avs/css/pdf.css";
  private static final String IMG_PATH = "/avs/artwork/";
  private static final int MEDIA_HTML = 1;
  private static final int MEDIA_PDF = 2;
  private static final int MEDIA_JSON = 3;
  private static final int MEDIA_XML = 4;
  private static final String NORMAL_FONT_CLS = "normalFont";
  private static final int DEF_LAB_DATE_RANGE = -1; // days
  private static final String DEF_LANGUAGE = "en";
  private static final String DEF_AVS_SECTIONS = "clinicsVisited,providers,diagnoses,vitals," +
  		"immunizations,orders,appointments,comments,pcp,primaryCareTeam,allergies,medications";
  private static final String DEF_PVS_SECTIONS = "clinicsVisited,diagnoses,clinicalReminders,pcp," +
  		"primaryCareTeam,allergies,medications";
  private static String CSS_CONTENTS;
  private static final int LINES_PER_PAGE = 54;
  private static final int THREAD_TIMEOUT = 120000;
  private static final Pattern lnBrkPattern = Pattern.compile("<br/>|<br />|</div>|</p>");
  private static String viUploadsLocalPath;
  private static String viUploadsRemotePath;
  private static String viUploadsUsername;
  private static String viUploadsPassword;
  private static String viUploadsIxType;
  private static String viUploadsStsCb;
  private static String viUploadsGdesc;
  private static String viUploadsTType;
  private static String viUploadsPxPkg;
  private static String viUploadsDflg;
  
  public static boolean IS_DEMO;
  public static String DEMO_PT_NAME;
  public static double DEMO_PT_DOB_DM;
  public static String DEMO_PT_DOB;
  public static int DEMO_PT_AGE;
  public static String DEMO_PT_SSN;
  
  private static final String TPL_SHEET_WRAPPER = "" +
    "<div id=\"sheet-wrapper\">\n" +
      "<div id=\"sheet\">\n" +
        "<div id=\"sheet-contents\" class=\"__CLASS__\">\n" +
          "__CONTENTS__\n" +
        "</div>" +
      "</div>" +
    "</div>";

  private static final String TPL_FOOTER = "<div id=\"sheet-footer\" class=\"__CLASS__\">__FOOTER_HTML__ __BOILERPLATES__</div>";
  private static final String TPL_HEADER = "<div id=\"sheet-header\" class=\"__CLASS__\">__CONTENTS__</div>";
  private static final String TPL_GROUPS_WRAPPER = "<div id=\"groups\">__CONTENTS__</div>";

  private static final String TPL_GROUP = "" +
    "<div id=\"group-__GROUP_ID_SUFFIX__\" class=\"group\">" +
      "<h2 class=\"group-title\">__GROUP_TITLE__</h2>" +
      "__CONTENTS__" +
    "</div>"; 

  private static final String TPL_SECTION = "" +
    "<div id=\"section-__SECTION_ID_SUFFIX__\" class=\"__SECTION_CLASS__\">" +
      "<h3 class=\"section-title\">__SECTION_TITLE__</h3>" +
      "<div class=\"section-body\">__CONTENTS__</div>" + 
    "</div>";
  
  static {
    
    ResourceBundle res = ResourceBundle.getBundle("gov.va.med.lom.avs.vistaimaging");
    viUploadsLocalPath = res.getString("avs.vi.localpath");
    viUploadsRemotePath = res.getString("avs.vi.remotepath");
    viUploadsUsername = res.getString("avs.vi.username");
    viUploadsPassword = res.getString("avs.vi.password");
    viUploadsIxType = res.getString("avs.vi.ixtype");
    viUploadsStsCb = res.getString("avs.vi.stscb");
    viUploadsGdesc = res.getString("avs.vi.gdesc");
    viUploadsTType = res.getString("avs.vi.ttype");
    viUploadsPxPkg = res.getString("avs.vi.pxpkg");
    viUploadsDflg = res.getString("avs.vi.dflg");
    
    res = ResourceBundle.getBundle("gov.va.med.lom.avs.avs");
    IS_DEMO = StringUtils.strToBool(res.getString("avs.demo"), "true");
    DEMO_PT_NAME = res.getString("avs.demo.ptName");
    DEMO_PT_DOB_DM = Double.valueOf(res.getString("avs.demo.ptDobDm")).doubleValue();
    DEMO_PT_DOB = res.getString("avs.demo.ptDob");
    DEMO_PT_AGE = Integer.valueOf(res.getString("avs.demo.ptAge")).intValue();
    DEMO_PT_SSN = res.getString("avs.demo.ptSsn");
    
  }  
  
  // services
  private SheetService sheetService;
  private SettingsService settingsService;
  private PatientVBService patientVBService;
  private UserVBService userVBService;
  private SheetConfig sheetConfig;
  
  // client request params
  private String avsBody;
  private String pvsBody;
  private String additionalInformationSheetBody;
  private String fontClass;
  private String labDateRange;
  private String comments;
  private String printerIen;
  private String printerIp;
  private String printerName;
  private boolean printAllServiceDescriptions;
  private String selectedServiceDescriptions;
  private String sections;
  private String chartFilenames;
  private boolean locked;
  private String customContent;
  private String remoteVaMedicationsHtml;
  private String remoteNonVaMedicationsHtml;
  private String docType;
  private int media;
  private int labResultsDaysBack; 
  private String charts;
  private boolean initialRequest;
  private String language;
  private boolean print;
  private String format;
  
  // data fields
  private SheetDataThread pceDataThread;
  private EncounterCacheMongo encounterCache;
  private boolean userIsProvider;
  private VistaUser vistaUser;
  private BasicDemographics demographics;
  private String stationNo;
  private List<PceData> pceDataList;
  private HashMap<String, PceData> pceDataMap;
  private double fmNow;
  private FacilityPrefs facilityPrefs;
  private List<MedicationJson> remoteVaMeds;
  private List<MedicationJson> remoteNonVaMeds;
  private HashMap<String, String> replacements;
  private HashMap<String, String> sectionsDisplayed;
  private Hashtable<String, String> smokingHT;
  private Hashtable<String, String> languageHT;
  private AvsJson avsJson;
  private DataModel dataModel;
  private Date startTime;
  private String pdfFilename;

  public String avs() throws Exception {

    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("locationIens", super.locationIens);
    Precondition.assertNotBlank("datetimes", super.datetimes);

    this.startTime = new Date();
    this.docType = EncounterInfo.AVS;
    if (this.getFormat().toLowerCase().equals("html") || (this.getFormat() == null)) {
      this.media = MEDIA_HTML;
    } else {
      if (this.getFormat().toLowerCase().equals("xml")) {
        this.media = MEDIA_XML;
      } else {
        this.media = MEDIA_JSON;
      }
    }
    this.comments = this.getComments();
    if (!this.comments.equals("None")) {
      this.comments = this.getComments();
    }
    this.setDefaults();
    usageLog("Build After Visit Summary", "Font Class=" + this.fontClass);
    this.initialize();
    this.buildAvs();
    
    if ((this.printAllServiceDescriptions) || 
        ((this.selectedServiceDescriptions != null) && !this.selectedServiceDescriptions.isEmpty())) {
      this.buildAdditionalInformationSheet();
    }
    
    usageLog("Return After Visit Summary", "Elapsed Time=" + (new Date().getTime() - startTime.getTime()));    
    if (this.media == MEDIA_HTML) {
      new PdfFileThread(cleanCommentsForPdf(this.avsBody, this.fontClass), this.additionalInformationSheetBody, this.getStationNo(), 
          this.patientDfn, super.getLocationIens(), super.getDatetimes(), viUploadsLocalPath, 
          CSS_CONTENTS, BASE_URI + IMG_PATH, this.getDocType()).start();
      
      return writeJson(this.avsJson);
    } else if (this.media == MEDIA_XML) {
      return writeXml(this.dataModel);
    } else {
      return writeJson(this.dataModel);
    }
  }
  
  public String pvs() throws Exception {
    
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("locationIens", super.locationIens);
    Precondition.assertNotBlank("datetimes", super.datetimes);

    usageLog("Build Pre-Visit Summary", "");
    this.startTime = new Date();
    this.initialRequest = true; 
    this.docType = EncounterInfo.PVS;
    this.media = MEDIA_HTML;
    this.setDefaults();
    this.initialize();
    
    MiscVBService miscVBService = VistaBrokerServiceFactory.getMiscVBService();
    this.fmNow = miscVBService.fmNow(super.securityContext).getPayload();
    
    this.buildPvs();
    
    new PdfFileThread(this.pvsBody, "", this.getStationNo(), 
        this.patientDfn, super.getLocationIens(), super.getDatetimes(), viUploadsLocalPath, 
        CSS_CONTENTS, BASE_URI + IMG_PATH, this.getDocType()).start();    
    
    usageLog("Return Pre-Visit Summary", "Elapsed Time=" + (new Date().getTime() - startTime.getTime()));
    return setHtml(TPL_SHEET_WRAPPER
        .replace("__CLASS__", this.fontClass)
        .replace("__CONTENTS__", this.pvsBody));
  }  
  
  public void createPdf() {
    
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("locationIens", super.locationIens);
    Precondition.assertNotBlank("datetimes", super.datetimes);

    this.startTime = new Date();
    this.media = MEDIA_PDF;
    this.setDefaults();
    this.encounterCache = this.getEncounterCache();
    if (this.encounterCache == null) {
      this.initEncounterCache();
      this.setEncounterCache();
    }
    boolean alreadyPrinted = this.encounterCache.isPrinted();
    
    // update the cached encounter with print status
    if (this.print && this.getDocType().equals(EncounterInfo.AVS)) {
      try {
        this.encounterCache.setPrinted(true);
        this.getSheetService().updateEncounterCacheMongoPrintStatus(this.encounterCache);
      } catch(Exception e) {
        log.error("Error setting print status", e);
      } 
    }
    usageLog("Create " + this.getDocType().toUpperCase() + " PDF", "Font Class=" + this.fontClass + ", Printing=" + this.print);
    
    ByteArrayOutputStream outputStream = null;
    try {
      if (this.getDocType().equals(EncounterInfo.AVS)) {
        boolean cachedPdf = false;
        if ((this.encounterCache.getPdfFilename() != null) && !this.encounterCache.getPdfFilename().isEmpty() &&
            sameLists(Arrays.asList(StringUtils.pieceList(this.sections, ',')), this.encounterCache.getSections()) &&
            this.fontClass.equals(this.encounterCache.getFontClass()) &&
            this.labDateRange.equals(String.valueOf(this.encounterCache.getLabDateRange())) &&
            this.charts.isEmpty() &&
            (this.printAllServiceDescriptions == this.encounterCache.isPrintServiceDescriptions()) &&
            this.selectedServiceDescriptions.equals(this.encounterCache.getSelectedServiceDescriptions()) &&
            this.language.equals(this.encounterCache.getLanguage())) {
            File pdfFile = new File(viUploadsLocalPath + "/" + this.encounterCache.getPdfFilename());
          if (pdfFile.exists()) {
            try {
              FileInputStream fis = new FileInputStream(pdfFile);
              outputStream = new ByteArrayOutputStream();
              byte[] buf = new byte[1024];
              try {
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                  outputStream.write(buf, 0, readNum);
                }
                cachedPdf = true;
              } catch (IOException ioe) {
                log.error("IO Exception loading cached PDF: " + this.encounterCache.getPdfFilename(), ioe);
              }
            } catch(FileNotFoundException fne) {
              log.error("File not found: " + this.encounterCache.getPdfFilename(), fne);
            }
          }
        }
        if (!cachedPdf) {
          this.initialRequest = true;
          this.encounterCache = null;
          this.initialize();
          this.buildAvs();
          outputStream = AvsWebUtils.createPdfWithAddlInfo(CSS_CONTENTS, BASE_URI + IMG_PATH,
              cleanCommentsForPdf(this.avsBody, this.fontClass), this.additionalInformationSheetBody); 
          if ((this.printAllServiceDescriptions) || 
              ((this.selectedServiceDescriptions != null) && !this.selectedServiceDescriptions.isEmpty())) {
            this.buildAdditionalInformationSheet();
          }
          this.pdfFilename = AvsWebUtils.savePdfToFile(this.stationNo, super.patientDfn, 
              super.getLocationIens(), super.getDatetimes(), viUploadsLocalPath, outputStream);
          this.savePdfFilename(this.pdfFilename);
        }
      } else {
        this.buildPvs();
        outputStream = AvsWebUtils.createPdfWithAddlInfo(CSS_CONTENTS, BASE_URI + IMG_PATH,
            this.pvsBody, this.additionalInformationSheetBody); 
      }
    } catch(Exception e) {
      log.error("Error creating PDF", e);
    }
    
    boolean success = false;
    
    try {
      usageLog("Return PDF", "Elapsed Time=" + (new Date().getTime() - startTime.getTime()));
      try {
        flushPdf(outputStream);
        success = true;
      } catch(Exception e) {
        log.error("Error flushing PDF.", e);
      }
    } finally {
      try {
        outputStream.close();
      } catch(IOException e) {}
      // if first time printing, create TIU note and/or upload to VI
      if (this.getDocType().equals(EncounterInfo.AVS) && success && this.print && !alreadyPrinted) {
        if (!IS_DEMO) {
          String tiuNoteIen = null;
          String titleIen = this.getSettingsService().getTiuTitleIenForFacility(this.getStationNo()).getPayload();
          if ((titleIen != null) && !titleIen.isEmpty()) {         
            tiuNoteIen = doCreateTiuNote(); 
          }
          this.doUploadToVI(tiuNoteIen, outputStream); 
          this.doCreateAvsPrintedHF();
        }
      } else if (!success) {
        try {
          encounterCache.setPrinted(false);
          this.getSheetService().updateEncounterCacheMongoPrintStatus(encounterCache);
        } catch(Exception e) {
          log.error("Error setting print status", e);
        }
      }
    }

  }
  
  public String printPdf() {
   
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("locationIens", super.locationIens);
    Precondition.assertNotBlank("datetimes", super.datetimes);
    
    this.startTime = new Date();
    boolean success = false;
    this.media = MEDIA_PDF;
    this.setDefaults();
    this.encounterCache = this.getEncounterCache();
    if (this.encounterCache == null) {
      this.initEncounterCache();
      this.setEncounterCache();
    }
    boolean alreadyPrinted = this.encounterCache.isPrinted();
    
    // update the cached encounter with print status
    if (this.getDocType().equals(EncounterInfo.AVS)) {
      try {
        this.encounterCache.setPrinted(true);
        this.getSheetService().updateEncounterCacheMongoPrintStatus(this.encounterCache);
      } catch(Exception e) {
        log.error("Error setting print status", e);
      } 
    }
    
    String filename = null;
    if ((this.printerIp != null) && !this.printerIp.isEmpty()) {
      usageLog("Print " + this.getDocType().toUpperCase() + " PDF", "Printer IEN=" + printerIen + ", Printer IP=" + printerIp + ", Printer Name=" + printerName);
    } else {
      filename = EncounterInfo.AVS + "-" + this.getStationNo() + "_" + super.patientDfn + ".pdf";
      usageLog("Print " + this.getDocType().toUpperCase() + " PDF", "Filename=" + filename);
    }

    ByteArrayOutputStream outputStream = null;
    try {
      if (this.getDocType().equals(EncounterInfo.AVS)) {
        boolean cachedPdf = false;
        if ((this.encounterCache.getPdfFilename() != null) && !this.encounterCache.getPdfFilename().isEmpty() &&
            sameLists(Arrays.asList(StringUtils.pieceList(this.sections, ',')), this.encounterCache.getSections()) &&
            this.fontClass.equals(this.encounterCache.getFontClass()) &&
            this.labDateRange.equals(String.valueOf(this.encounterCache.getLabDateRange())) &&
            this.charts.isEmpty() &&
            (this.printAllServiceDescriptions == this.encounterCache.isPrintServiceDescriptions()) &&
            this.selectedServiceDescriptions.equals(this.encounterCache.getSelectedServiceDescriptions()) &&
            this.language.equals(this.encounterCache.getLanguage())) {      
          File pdfFile = new File(viUploadsLocalPath + "/" + this.encounterCache.getPdfFilename());
          if (pdfFile.exists()) {
            try {
              FileInputStream fis = new FileInputStream(pdfFile);
              outputStream = new ByteArrayOutputStream();
              byte[] buf = new byte[1024];
              try {
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                  outputStream.write(buf, 0, readNum);
                }
                cachedPdf = true;
              } catch (IOException ioe) {
                log.error("IO Exception loading cached PDF: " + this.encounterCache.getPdfFilename(), ioe);
              }
            } catch(FileNotFoundException fne) {
              log.error("File not found: " + this.encounterCache.getPdfFilename(), fne);
            }
          }
        }
        if (!cachedPdf) {
          this.initialRequest = true;
          this.encounterCache = null;
          this.initialize();
          this.buildAvs();
          outputStream = AvsWebUtils.createPdfWithAddlInfo(CSS_CONTENTS, BASE_URI + IMG_PATH,
              cleanCommentsForPdf(this.avsBody, this.fontClass), this.additionalInformationSheetBody); 
          if ((this.printAllServiceDescriptions) || 
              ((this.selectedServiceDescriptions != null) && !this.selectedServiceDescriptions.isEmpty())) {
            this.buildAdditionalInformationSheet();
          }
          this.pdfFilename = AvsWebUtils.savePdfToFile(this.stationNo, super.patientDfn, 
              super.getLocationIens(), super.getDatetimes(), viUploadsLocalPath, outputStream);
          this.savePdfFilename(this.pdfFilename);
        }
      } else {
        this.buildPvs();
        outputStream = AvsWebUtils.createPdfWithAddlInfo(CSS_CONTENTS, BASE_URI + IMG_PATH,
            this.pvsBody, this.additionalInformationSheetBody); 
      }    
    } catch(Exception e) {
      log.error("Error creating PDF", e);
    }      
    
    ByteArrayOutputStream buffer = null;
    try {
      if ((this.printerIp != null) && !this.printerIp.isEmpty()) {
        try {
          buffer = new ByteArrayOutputStream();
          
          // convert to postscript
          ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());  
          PDFDocument pdfDoc = new PDFDocument();
          pdfDoc.load(inputStream);
          PSConverter psConv = new PSConverter();
          psConv.run(pdfDoc, buffer);
          
          // print via lpd        
          Printer lpr = new Printer();
          lpr.setPrintRaw(true);
          lpr.setUseOutOfBoundPorts(true);
          lpr.printStream(buffer, printerIp, printerName, this.getStringResource(this.getDocType(), DEF_LANGUAGE));
          
          success = true;
        } catch(Exception e) {
          log.error("Error printing AVS.", e);
        }
      } else {
        success = true;
      }
    } finally {
      if (success) {
        usageLog("Finished Printing PDF", "Elapsed Time=" + (new Date().getTime() - startTime.getTime()));
      }
      try {
        outputStream.close();
      } catch(IOException e) {}
      
      // if first time printing, create TIU note and/or upload to VI
      if (this.getDocType().equals(EncounterInfo.AVS) && success && !alreadyPrinted) {
        if (!IS_DEMO) {
          String tiuNoteIen = null;
          String titleIen = this.getSettingsService().getTiuTitleIenForFacility(this.getStationNo()).getPayload();
          if ((titleIen != null) && !titleIen.isEmpty()) {         
            tiuNoteIen = doCreateTiuNote(); 
          }
          this.doUploadToVI(tiuNoteIen, outputStream); 
          this.doCreateAvsPrintedHF();
        }
      } else if (!success) {
        try {
          this.encounterCache.setPrinted(false);
          this.getSheetService().updateEncounterCacheMongoPrintStatus(this.encounterCache);
        } catch(Exception e) {
          log.error("Error setting print status", e);
        }
      }
    }
    
    if (this.printerIp == null) {
      super.downloadPdf(outputStream, filename);
      return null;
    } else {
      return success ? SUCCESS : ERROR;
    }
  }
  
  private void initEncounterCache() {
    List<Encounter> encounters = new ArrayList<Encounter>();
    int i = 0;
    for (Double dt : super.getDatetimes()) {
      Encounter encounter = new Encounter();
      encounter.setLocation(new EncounterLocation( super.getLocationIens().get(i), super.getLocationNames().get(i)));
      encounter.setVisitString(this.getVisitStr(i));
      encounter.setEncounterDatetime(dt);
      encounters.add(encounter);
      i++;
    }
    this.encounterCache = new EncounterCacheMongo();
    this.encounterCache.setFacilityNo(super.facilityNo);
    this.encounterCache.setPatientDfn(super.patientDfn);
    this.encounterCache.setEncounters(encounters);       
  }
  
  private void initialize() {
    
    if (this.encounterCache == null) {
      this.initEncounterCache();
    }
    
    // get pce data
    CallbackObject callback = new CallbackObject();
    pceDataThread = new SheetDataThreadInitializer(super.facilityNo, this.getLang());
    pceDataThread = new PceDataThread();
    initSheetDataThread(pceDataThread, callback);
    callback.setNumThreadsCreated(1);
    pceDataThread.start();
    synchronized (callback) {
      try {
        callback.wait(THREAD_TIMEOUT);
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
    }
    this.pceDataList = (List<PceData>)callback.getThreadData().get("pceData");
    this.processPceData();
    this.setEncounterCache();
    
  }  
  
  public String setDefaultPrinter() {
    
    UserSettings userSettings = 
        this.getSettingsService().getUserSettings(this.getStationNo(), this.userDuz).getPayload();
        
    if (userSettings == null) {
      userSettings = new UserSettings();
    }
    
    userSettings.setFacilityNo(this.getStationNo());
    userSettings.setUserDuz(this.userDuz);
    userSettings.setPrinterIen(this.printerIen);
    userSettings.setPrinterIp(this.printerIp);
    userSettings.setPrinterName(this.printerName);
    userSettings.setIsDefaultPrinter(true); 
    
    if ((userSettings.getId() == null) || (userSettings.getId() == 0L)) {
      this.getSettingsService().saveUserSettings(userSettings);
    } else {
      this.getSettingsService().updateUserSettings(userSettings);
    }
    
    return SUCCESS;
    
  }
  
  public String getDefaultPrinter() {
    
    UserSettings userSettings = 
        this.getSettingsService().getUserSettings(this.getStationNo(), this.userDuz).getPayload();

    VistaPrinterJson vpJson = new VistaPrinterJson();
    if (userSettings != null) {
      vpJson.setIen(userSettings.getPrinterIen());
      vpJson.setName(userSettings.getPrinterName());
      vpJson.setIpAddress(userSettings.getPrinterIp());
      vpJson.setIsDefault(userSettings.getIsDefaultPrinter());
    } else {
      vpJson.setIen("");
      vpJson.setName("");
      vpJson.setIpAddress("");
      vpJson.setIsDefault(false);
    }
    return writeJson(vpJson);
  }
  
  public String setCustomContent() {
    
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("locationIens", super.locationIens);
    Precondition.assertNotBlank("datetimes", super.datetimes);
    Precondition.assertNotNull("customContent", this.customContent);
    
    this.encounterCache = this.getEncounterCache();
    if (this.encounterCache == null) {
      this.initEncounterCache();
      this.setEncounterCache();
    }    
    encounterCache.setCustomContent(this.customContent);
    try {
      this.getSheetService().updateEncounterCacheMongoCustomContent(encounterCache);
    } catch(Exception e) {
      log.error("Error setting custom content", e);
    }
    
    this.docType = EncounterInfo.AVS;
    this.media = MEDIA_PDF;
    this.initialRequest = false;
    this.initialize();
    this.buildAvs();
    
    if ((this.printAllServiceDescriptions) || 
        ((this.selectedServiceDescriptions != null) && !this.selectedServiceDescriptions.isEmpty())) {
      this.buildAdditionalInformationSheet();
    }
    new PdfFileThread(cleanCommentsForPdf(this.avsBody, this.fontClass), this.additionalInformationSheetBody, this.getStationNo(), 
        this.patientDfn, this.getLocationIens(), this.getDatetimes(), viUploadsLocalPath, 
        CSS_CONTENTS, BASE_URI + IMG_PATH, this.getDocType()).start();    
    
    return SUCCESS;
  }
  
  public String setLocked() {
    
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("locationIens", super.locationIens);
    Precondition.assertNotBlank("datetimes", super.datetimes);
    Precondition.assertNotNull("locked", this.locked);
    
    this.encounterCache = this.getEncounterCache();
    if (this.encounterCache == null) {
      this.initEncounterCache();
      this.setEncounterCache();
    }    
    encounterCache.setLocked(this.locked);
    try {
      this.getSheetService().updateEncounterCacheMongoLockStatus(encounterCache);
    } catch(Exception e) {
      log.error("Error setting lock", e);
    }
    
    return SUCCESS;
  }  
  
  public String saveComments() {
    
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("locationIens", super.locationIens);
    Precondition.assertNotBlank("datetimes", super.datetimes);
    Precondition.assertNotNull("comments", this.comments);
    
    this.docType = EncounterInfo.AVS;
    this.media = MEDIA_PDF;
    this.initialRequest = false;
    
    this.encounterCache = this.getEncounterCache();
    if (this.encounterCache == null) {
      this.initEncounterCache();
      this.setEncounterCache();
    }    
    this.comments = this.getComments();
    if (!this.comments.equals("None")) {
      this.comments = this.getComments();
    }
    this.encounterCache.setInstructions(this.comments);
    this.sheetService.updateEncounterCacheMongoInstructions(this.encounterCache);
    
    this.initialize();
    this.buildAvs();
    
    if ((this.printAllServiceDescriptions) || 
        ((this.selectedServiceDescriptions != null) && !this.selectedServiceDescriptions.isEmpty())) {
      this.buildAdditionalInformationSheet();
    }
    
    new PdfFileThread(cleanCommentsForPdf(this.avsBody, this.fontClass), this.additionalInformationSheetBody, this.getStationNo(), 
        this.patientDfn, this.getLocationIens(), this.getDatetimes(), viUploadsLocalPath, 
        CSS_CONTENTS, BASE_URI + IMG_PATH, this.getDocType()).start();
    
    return SUCCESS;
  }   
  
  private void savePdfFilename(String pdfFilename) {
    this.encounterCache = this.getEncounterCache();
    if (this.encounterCache == null) {
      this.initEncounterCache();
      this.setEncounterCache();
    }    
    encounterCache.setPdfFilename(pdfFilename);
    try {
      this.sheetService.updateEncounterCacheMongoPdfFilename(encounterCache);
    } catch(Exception e) {
      log.error("Error saving PDF filename in encounter cache", e);
    }
  }  
  
  public String cacheComments() {
    
    this.comments = this.getComments();
    if (!this.comments.equals("None")) {
      this.comments = this.getComments();
    }
    String key = WebCacheUtil.getWebCacheUtil().cacheComments(this.comments);
    return writeJson(key);
  }
  
  public String getClientStrings() {
    
    new SheetDataThreadInitializer(super.facilityNo, this.getLang());
    
    ClientStringsJson csj = new ClientStringsJson();
    csj.setAppTitle(this.getStringResource("avs"));
    csj.setKramesInstructions(this.getStringResource("kramesInstructions"));
    csj.setMedsTakingTitle(this.getStringResource("medsTaking"));
    csj.setMedsNotTakingTitle(this.getStringResource("medsNotTaking"));
    csj.setRemoteMedsNotTakingDisclaimer(this.getStringResource("remoteMedsNotTakingDisclaimer"));
    csj.setRefillsTitle(this.getStringResource("refills"));
    csj.setLastFilledTitle(this.getStringResource("lastFilled"));
    csj.setLastReleasedTitle(this.getStringResource("lastReleased"));
    csj.setExpiresTitle(this.getStringResource("expires"));
    csj.setFacilityTitle(this.getStringResource("facility"));
    csj.setProviderTitle(this.getStringResource("provider"));
    csj.setDescriptionTitle(this.getStringResource("description"));
    csj.setNonVaMedsNotTakingDisclaimer(this.getStringResource("nonVaMedsNotTakingDisclaimer"));
    csj.setStartDateTitle(this.getStringResource("startDate"));
    csj.setStopDateTitle(this.getStringResource("stopDate"));
    csj.setDocumentingFacilityTitle(this.getStringResource("documentingFacility"));

    return writeJson(csj);
  }
  
  private String doCreateTiuNote() {
    
    // Set new tiu note properties
    String noteText = this.getSettingsService().getTiuNoteTextForFacilty(this.getStationNo()).getPayload();
    String titleIen = this.getSettingsService().getTiuTitleIenForFacility(this.getStationNo()).getPayload();
    
    String dtTmStr = null;
    try {
      dtTmStr = DateUtils.toDateTimeStr(DateUtils.fmDateTimeToDateTime(this.fmNow).getTime(), "yyyy-MM-dd HH:mm:ss");
    } catch(Exception e) {}
    
    NewTiuNote newTiuNote = new NewTiuNote();
    newTiuNote.setDfn(super.patientDfn);
    newTiuNote.setAuthorDuz(super.userDuz);
    newTiuNote.setTitleIen(titleIen);
    newTiuNote.setVisitLocationIen(super.getLocationIens().get(0));
    newTiuNote.setVisitStr(getVisitStr(0)); 
    newTiuNote.setRefDate(dtTmStr);
    newTiuNote.setDictDate(dtTmStr);
    try {
      newTiuNote.setVisitDate(DateUtils.toAnsiDateTime(FMDateUtils.fmDateTimeToDate(super.getDatetimes().get(0))));
    } catch(Exception e) {}
    newTiuNote.setCpt(false);
    newTiuNote.setSubject("");
    newTiuNote.setPackageRef("");
    newTiuNote.setSuppress(false);
    newTiuNote.setText(noteText);
    
    // Create the TIU note
    ServiceResponse<TiuNote> tiuSr = this.getPatientVBService().createTiuNote(super.securityContext, newTiuNote);
    TiuNote tiuNote = tiuSr.getPayload();
    
    usageLog("Create TIU Note", "Title IEN=" + titleIen + ", Note IEN=" + tiuNote.getIen());
    
    // Close the TIU note administratively
    this.getUserVBService().tiuAdminClose(super.securityContext, tiuNote.getIen(), "S", super.userDuz);
    
    return tiuNote.getIen();
  }
  
  private void doUploadToVI(String tiuNoteIen) {
    doUploadToVI(tiuNoteIen, null);
  }
  
  private void doUploadToVI(String tiuNoteIen, ByteArrayOutputStream outputStream) {
    
    if (outputStream == null) {
      // Build AVS and PDF
      this.media = MEDIA_PDF;
      this.comments = this.getComments();
      if (!this.comments.equals("None")) {
        this.comments = this.getComments();
      }
      this.setDefaults();
      
      this.setEncounterCache();
      
      if (this.getDocType().equals(EncounterInfo.AVS)) {
        this.buildAvs();
      } else {
        this.buildPvs();
      }
      try {
        outputStream = AvsWebUtils.doCreatePdf(CSS_CONTENTS, BASE_URI + IMG_PATH, cleanCommentsForPdf(this.avsBody, this.fontClass));
      } catch(Exception e) {
        log.error("Error converting HTML to PDF", e);
      }
    }
    
    // Upload PDF to remote VI uploads path
    String filename = StringUtils.getRandomString(5, 10) + "-" + 
      StringUtils.replaceChar(String.valueOf(FMDateUtils.dateTimeToFMDateTime(new Date())), '.', '-') + ".pdf";
    File localFile = new File(viUploadsLocalPath, filename);
    usageLog("Create PDF File for VI Import", "Path=" + localFile.getPath());
    
    try {
      localFile.createNewFile();
      OutputStream fileOutputStream = null;
      fileOutputStream = new FileOutputStream(localFile);
      outputStream.writeTo(fileOutputStream);
      fileOutputStream.close();
    } catch(Exception e) {
      log.error("ERROR CREATING PDF FILE", e);
      e.printStackTrace();
    }
    
    Date now = new Date();
    String acqd = request.getServerName();
    String acql = super.getLocationIens().get(0);
    String trkId = EncounterInfo.AVS + ";" + this.getStationNo() + "_" +  (int)FMDateUtils.dateToFMDate(now) + "_" + now.getTime();
    List<String> images = new ArrayList<String>();
    images.add(viUploadsRemotePath + "\\" + filename + "^" + this.getStringResource(this.getDocType(), DEF_LANGUAGE));
    
    List<String> params = new ArrayList<String>();
    
    // Acquisition Device (computer name or domain name)
    params.add("ACQD^" + acqd);
    // Hospital Location
    params.add("ACQL^" + acql);
    // Acquisition Site (station number)
    params.add("ACQS^" + this.getStationNo());
    // Procedure Date
    params.add("PXDT^" + String.valueOf(FMDateUtils.dateTimeToFMDateTime(now)));
    // Tracking ID
    params.add("TRKID^" + trkId);    
    // Procedure IEN (TIU document IEN)
    if ((tiuNoteIen != null) && !tiuNoteIen.isEmpty()) {
      params.add("PXIEN^" + tiuNoteIen);
    }
    // Patient DFN
    params.add("IDFN^" + patientDfn);
    // Procedure Package         
    params.add("PXPKG^" + viUploadsPxPkg);
    // Status Callback
    params.add("STSCB^" + viUploadsStsCb);
    // Delete Flag
    params.add("DFLG^" + viUploadsDflg);
    // Image/Document type
    params.add("IXTYPE^" + viUploadsIxType);
    // General Description
    params.add("GDESC^" + viUploadsGdesc);
    // Image Type
    params.add("ITYPE^" + viUploadsTType);
    // Images
    for (String image : images) {
      params.add("IMAGE^" + image);
    }
    // Username/Password
    params.add("USERNAME^" + viUploadsUsername);
    params.add("PASSWORD^" + Hash.encrypt(viUploadsPassword));
    
    usageLog("Import Document into VI", "Tracking ID=" + trkId); 
    
    CollectionServiceResponse<String> csr = this.getPatientVBService().importVistaImage(super.securityContext, params);
    List<String> results = (List<String>)csr.getCollection();
    if ((results != null) && (results.size() > 0)) {
      for (String result : results) {
        usageLog("VI Import Result", result);
      }
    }
  } 
  
  public void doCreateAvsPrintedHF() {
    
    String hfIen = null;
    String hfName = null;
    List<FacilityHealthFactor> facilityHealthFactors = (List<FacilityHealthFactor>)
        this.getSheetService().getFacilityHealthFactors(this.getStationNo()).getCollection();
    for (FacilityHealthFactor facilityHealthFactor : facilityHealthFactors) {
      if (facilityHealthFactor.getType().equals("avs_printed")) {
        hfIen = facilityHealthFactor.getIen();
        hfName = facilityHealthFactor.getValue();
        break;
      }
    }
    if ((hfIen != null) && (hfName != null) && 
        (this.encounterCache.getEncounters() != null)) {
      for (Encounter encounter : this.encounterCache.getEncounters()) {
        if (encounter.getEncounterNoteIen() == null) {
          encounter.setEncounterNoteIen("");
        }
        this.getSheetService().saveHealthFactor(super.securityContext, this.encounterCache.getPatientDfn(), 
            encounter.getEncounterDatetime(), encounter.getLocation().getLocationIen(), 
            encounter.getEncounterNoteIen(), hfIen, hfName);
      }
    }
  }
  
  public String getRemoteMedsJson() {
    
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    
    CallbackObject callback = new CallbackObject();
    List<SheetDataThread> dataThreads = new ArrayList<SheetDataThread>();
    SheetDataThread sheetDataThread = 
        new SheetDataThreadInitializer(super.facilityNo, this.getLang());
    
    // remote va meds
    sheetDataThread = new RemoteVaMedicationsThread();
    initSheetDataThread(sheetDataThread, callback);
    dataThreads.add(sheetDataThread);
    
    // remote non-va meds
    sheetDataThread = new RemoteNonVaMedicationsThread();
    initSheetDataThread(sheetDataThread, callback);
    dataThreads.add(sheetDataThread);    
    
    callback.setNumThreadsCreated(dataThreads.size());
    
    for (SheetDataThread dataThread : dataThreads) {
      // Start each of the threads
      dataThread.start();
    }
    
    // Wait for the app threads to return or timeout
    synchronized (callback) {
      try {
        callback.wait(THREAD_TIMEOUT);
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
    }
    
    // All data threads returned or timed out so get results
    dataThreads.clear();
    callback.setTimedOut(true);
    
    Hashtable<String, Object> data = callback.getThreadData();
    List<MedicationJson> rmjList = null;        
    if (data.containsKey("remote-va_medications")) {
      rmjList = (List<MedicationJson>)data.get("remote-va_medications");
    }
    if (data.containsKey("remote-non_va_medications")) {
      rmjList.addAll((List<MedicationJson>)data.get("remote-non_va_medications"));
    }    
    
    return writeJson(rmjList);
  }
  
  public String setRemoteVaMedsHtml() {
    
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("locationIens", super.locationIens);
    Precondition.assertNotBlank("datetimes", super.datetimes);
    Precondition.assertNotNull("remoteVaMedicationsHtml", this.remoteVaMedicationsHtml);
    
    usageLog("Set Remote VA Meds HTML", "");
    
    this.encounterCache = this.getEncounterCache();
    if (this.encounterCache == null) {
      this.initEncounterCache();
      this.setEncounterCache();
    }
    try {
      encounterCache.setRemoteVaMedications(this.remoteVaMedicationsHtml);
      this.getSheetService().updateEncounterCacheMongoRemoteVaMedications(encounterCache);
    } catch(Exception e) {
      log.error("Error setting remote va meds html", e);
    }
    
    this.docType = EncounterInfo.AVS;
    this.media = MEDIA_PDF;
    this.initialRequest = false;
    this.initialize();
    this.buildAvs();
    
    if ((this.printAllServiceDescriptions) || 
        ((this.selectedServiceDescriptions != null) && !this.selectedServiceDescriptions.isEmpty())) {
      this.buildAdditionalInformationSheet();
    }
    new PdfFileThread(cleanCommentsForPdf(this.avsBody, this.fontClass), this.additionalInformationSheetBody, this.getStationNo(), 
        this.patientDfn, this.getLocationIens(), this.getDatetimes(), viUploadsLocalPath, 
        CSS_CONTENTS, BASE_URI + IMG_PATH, this.getDocType()).start();     
    
    return success();
  }
  
  public String setRemoteNonVAMedsHtml() {
    
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("locationIens", super.locationIens);
    Precondition.assertNotBlank("datetimes", super.datetimes);
    Precondition.assertNotNull("remoteNonVaMedicationsHtml", this.remoteNonVaMedicationsHtml);

    usageLog("Set Remote Non-VA Meds HTML", "");
    
    this.encounterCache = this.getEncounterCache();;
    if (this.encounterCache == null) {
      this.initEncounterCache();
      this.setEncounterCache();
    }
    try {
      encounterCache.setRemoteNonVaMedications(this.remoteNonVaMedicationsHtml);
      this.getSheetService().updateEncounterCacheMongoRemoteNonVaMedications(encounterCache);
    } catch(Exception e) {
      log.error("Error setting remote non-va meds html", e);
    }
    
    this.docType = EncounterInfo.AVS;
    this.media = MEDIA_PDF;
    this.initialRequest = false;
    this.initialize();
    this.buildAvs();
    
    if ((this.printAllServiceDescriptions) || 
        ((this.selectedServiceDescriptions != null) && !this.selectedServiceDescriptions.isEmpty())) {
      this.buildAdditionalInformationSheet();
    }
    new PdfFileThread(cleanCommentsForPdf(this.avsBody, this.fontClass), this.additionalInformationSheetBody, this.getStationNo(), 
        this.patientDfn, this.getLocationIens(), this.getDatetimes(), viUploadsLocalPath, 
        CSS_CONTENTS, BASE_URI + IMG_PATH, this.getDocType()).start();     
    
    return success();
  }  
  
  private String buildAdditionalInformationSheet() {
    
    StringBuffer body = new StringBuffer();
    body.append(this.renderAdditionalInformationGroup());
    
    StringBuffer csBody = new StringBuffer();
    csBody.append("<div id=\"sheet-contents\" class=\"" + this.fontClass + "\">");
    csBody.append(body.toString());
    csBody.append("</div>");

    this.additionalInformationSheetBody = csBody.toString();
    
    return TPL_SHEET_WRAPPER
        .replace("__CLASS__", this.fontClass)
        .replace("__CONTENTS__", body.toString());
  }
  
  private void buildAvs() {
    
    this.sectionsDisplayed = new HashMap<String, String>();
    if ((this.sections != null)) {
      String[] sectionsArr = StringUtils.pieceList(this.sections, ',');
      for (int i = 0; i < sectionsArr.length; i++) {
        this.sectionsDisplayed.put(sectionsArr[i], sectionsArr[i]);
      }
    }
    
    CallbackObject callback = new CallbackObject();
    List<SheetDataThread> dataThreads = new ArrayList<SheetDataThread>();
    SheetDataThread sheetDataThread = 
        new SheetDataThreadInitializer(super.facilityNo, this.getLang());    
    
    if (this.customContent == null) {
      
      if (isSectionDisplayed("patientInfo") || (this.media == MEDIA_JSON) || (this.media == MEDIA_XML)) {
        
        List<FacilityHealthFactor> facilityHealthFactors = (List<FacilityHealthFactor>)
            this.getSheetService().getFacilityHealthFactors(this.getStationNo()).getCollection();
        
        this.smokingHT = new Hashtable<String, String>();
        this.languageHT = new Hashtable<String, String>();
        for (FacilityHealthFactor facilityHealthFactor : facilityHealthFactors) {
          if (facilityHealthFactor.getType().equals("smoking")) {
            smokingHT.put(facilityHealthFactor.getIen(), facilityHealthFactor.getIen());
          } else if (facilityHealthFactor.getType().equals("language")) {
            languageHT.put(facilityHealthFactor.getIen(), facilityHealthFactor.getValue());
          }
        }

        sheetDataThread = new PatientInfoThread(getDemographics(), smokingHT, languageHT);
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if (this.pceDataList == null) {
        sheetDataThread = new PceDataThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);      
      }
      
      if (isSectionDisplayed("clinicsVisited")) {
        sheetDataThread = new ClinicsVisitedThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if (isSectionDisplayed("vitals")) {
        sheetDataThread = new VitalsThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if (isSectionDisplayed("orders")) {
        sheetDataThread = new OrdersThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if (isSectionDisplayed("appointments")) {
        sheetDataThread = new AppointmentsThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if (isSectionDisplayed("comments")) {
        sheetDataThread = new CommentsThread(this.comments);
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if (isSectionDisplayed("pcp")) {
        sheetDataThread = new PcpThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if (isSectionDisplayed("primaryCareTeam")) {
        sheetDataThread = new PrimaryCareTeamThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if (isSectionDisplayed("procedures")) {
        sheetDataThread = new ProceduresThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }      
      if (isSectionDisplayed("allergies")) {
        sheetDataThread = new AllergiesThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if (isSectionDisplayed("medications")) {
        // local va meds
        sheetDataThread = new LocalVaMedicationsThread();
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
        // remote non-va meds
        sheetDataThread = new LocalNonVaMedicationsThread(this.remoteNonVaMedicationsHtml);
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
        if (this.initialRequest || (this.media == MEDIA_JSON) || (this.media == MEDIA_XML)) {
          // remote va meds
          sheetDataThread = new RemoteVaMedicationsThread();
          initSheetDataThread(sheetDataThread, callback);
          dataThreads.add(sheetDataThread);
          // remote non-va meds
          sheetDataThread = new RemoteNonVaMedicationsThread();
          initSheetDataThread(sheetDataThread, callback);
          dataThreads.add(sheetDataThread);        
        }
      }
      
      if (((this.charts != null) && !this.charts.isEmpty()) || 
          ((this.chartFilenames != null) && !this.chartFilenames.isEmpty()) &&
         isSectionDisplayed("clinicalCharts")) {
        sheetDataThread = new ChartsThread(this.charts, this.chartFilenames);
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
      if ((this.labResultsDaysBack > 0) && isSectionDisplayed("labResults")) {
        sheetDataThread = new LabResultsThread(this.labResultsDaysBack);
        initSheetDataThread(sheetDataThread, callback);
        dataThreads.add(sheetDataThread);
      }
     
      callback.reset();
      callback.setNumThreadsCreated(dataThreads.size());
     
      if (dataThreads.size() > 0) {
        for (SheetDataThread dataThread : dataThreads) {
          // Start each of the threads
          dataThread.start();
        }
       
        // Wait for the app threads to return or timeout
        synchronized (callback) {
          try {
            callback.wait(THREAD_TIMEOUT);
          } catch (InterruptedException ie) {
            ie.printStackTrace();
          }
        }
        // All data threads returned or timed out so get results
        dataThreads.clear();
        callback.setTimedOut(true);     
      }
    }
    
    if ((this.media == MEDIA_HTML) || (this.media == MEDIA_PDF)) {
      
      String groupContent = null;
      if (this.customContent == null) {
        groupContent = this.renderGroups(callback.getThreadContent(), callback.getThreadData());
      } else {
        groupContent = this.renderCustomContent();
      }
      
      // render header, body, and footer sections
      StringBuffer body = new StringBuffer();
      body.append(this.renderHeader());
      body.append(groupContent);
      String footer = this.renderFooter();
      
      // insert footers at the end of each page
      StringBuffer ptNameFooter = new StringBuffer("<div class='footer'>");
      ptNameFooter.append(!IS_DEMO ? this.getDemographics().getName() : DEMO_PT_NAME); 
      ptNameFooter.append("</div>");
      
      Matcher m = lnBrkPattern.matcher(body.toString());
      int count = 0;
      int pageNum = 0;
      while (m.find()) {
        count++;
        if (count == LINES_PER_PAGE) {
          int end = m.end();
          body.insert(end + (ptNameFooter.length() * pageNum), ptNameFooter.toString());
          count = 0;
          pageNum++;
        }
      }
      int numLines = footer.length() / 100; // assume a line has 100 characters
      int over = (count + numLines) - LINES_PER_PAGE;
      if (over > 0) {
        body.append(ptNameFooter.toString());
      } 
      body.append(footer);
      
      StringBuffer csBody = new StringBuffer();
      csBody.append("<div id=\"sheet-contents\" class=\"" + this.fontClass + "\">");
      csBody.append(body.toString());
      csBody.append("</div>");
  
      this.avsBody = csBody.toString();
      
      this.avsJson = new AvsJson();
      this.avsJson.setContent(TPL_SHEET_WRAPPER
        .replace("__CLASS__", this.fontClass)
        .replace("__CONTENTS__", body.toString()));
      this.avsJson.setInstructions(this.comments);
      this.avsJson.setCharts(this.charts);
      this.avsJson.setFontClass(this.fontClass);
      this.avsJson.setLanguage(this.getLang());
      this.avsJson.setLabDateRange(this.labDateRange);
      this.avsJson.setPrintAllServiceDescriptions(this.printAllServiceDescriptions);
      this.avsJson.setSelectedServiceDescriptions(this.selectedServiceDescriptions);
      this.avsJson.setSections(this.sections);
      this.avsJson.setLocked(this.locked);
      this.avsJson.setUserIsProvider(this.userIsProvider);
      this.avsJson.setContentEdited(this.customContent != null);
      this.avsJson.setRemoteVaMeds(this.remoteVaMeds);
      this.avsJson.setRemoteNonVaMeds(this.remoteNonVaMeds);
      if ((this.pceDataList != null) && (this.pceDataList.size() > 0)) {
        List<String> codes = new ArrayList<String>();
        for (PceData pce : this.pceDataList) {
          if (pce.getCodes() != null) {
            codes.addAll(pce.getCodes());
          }
        }
        this.avsJson.setDiagnosisCodes(AvsWebUtils.delimitString(codes, false));
      }
      MiscVBService miscVBService = VistaBrokerServiceFactory.getMiscVBService();
      this.fmNow = miscVBService.fmNow(super.securityContext).getPayload();
      String dtTmStr = null;
      try {
        dtTmStr = DateUtils.toDateTimeStr(DateUtils.fmDateTimeToDateTime(this.fmNow).getTime(), "MMM dd, yy@HH:mm:ss");
      } catch(Exception e) {}  
      this.avsJson.setLastRefreshed(dtTmStr);    
      
    } else if ((this.media == MEDIA_JSON) || (this.media == MEDIA_XML)) {
      
      Hashtable<String, Object> data = callback.getThreadData();
      this.dataModel = getDataModel();
      if (data.containsKey("patientInfo")) {
        BasicDemographics dem = getDemographics();
        PatientInformation patientInfo = (PatientInformation)data.get("patientInfo");
        PatientInfoJson patientInfoJson = new PatientInfoJson();
        patientInfoJson.setDfn(dem.getDfn());
        patientInfoJson.setName(!IS_DEMO ? dem.getName() : DEMO_PT_NAME);
        patientInfoJson.setSsn(!IS_DEMO ? dem.getSsn() : DEMO_PT_SSN);
        patientInfoJson.setSex(dem.getSex());
        patientInfoJson.setDob(!IS_DEMO ? dem.getDobStr() : DEMO_PT_DOB);
        if (!dem.getDeceasedDateStr().isEmpty()) {
          patientInfoJson.setDeceasedDate(dem.getDeceasedDateStr());
        }
        patientInfoJson.setAge(!IS_DEMO ? dem.getAge() : DEMO_PT_AGE);
        patientInfoJson.setVeteran(dem.isVeteran());
        patientInfoJson.setScPct(dem.getScPct());
        if (!dem.getLocation().isEmpty()) {
          patientInfoJson.setLocation(dem.getLocation());
        }
        if (!dem.getRoomBed().isEmpty()) {
          patientInfoJson.setRoomBed(dem.getRoomBed());
        }
        patientInfoJson.setInpatient(dem.isInpatient());
        patientInfoJson.setSmokingStatus(patientInfo.getSmokingStatus());
        patientInfoJson.setSmokingStatusDate(patientInfo.getSmokingStatusDate());
        patientInfoJson.setSmokingStatusCode(patientInfo.getSmokingStatusCode());
        patientInfoJson.setLanguagePreference(patientInfo.getPreferredLanguage());
        this.dataModel.setPatientInfo(patientInfoJson);
      }
      if (data.containsKey("procedures")) {
        this.dataModel.setProcedures((List<ProcedureJson>)data.get("procedures"));
      }      
      if (data.containsKey("allergies")) {
        this.dataModel.setAllergiesReactions((AllergiesReactionsJson)data.get("allergies"));
      }
      if (data.containsKey("appointments")) {
        this.dataModel.setAppointments((List<AppointmentJson>)data.get("appointments"));
      }
      if (data.containsKey("clinicsVisited")) {
        this.dataModel.setClinicsVisited((List<ClinicVisitedJson>)data.get("clinicsVisited"));
      }
      if (data.containsKey("comments")) {
        this.dataModel.setPatientInstructions(this.encounterCache.getInstructions());
      }
      if (data.containsKey("labResults")) {
        this.dataModel.setLabResults((String)data.get("labResults"));
      }
      if (data.containsKey("orders")) {
        this.dataModel.setOrders((List<OrderJson>)data.get("orders"));
      }
      if (data.containsKey("vitals")) {
        this.dataModel.setVitals((List<VitalSignJson>)data.get("vitals"));
      }
      
      this.dataModel.setReasonForVisit(new ArrayList<DiagnosisJson>());
      this.dataModel.setProviders(new ArrayList<String>());
      this.dataModel.setDiagnoses(new ArrayList<DiagnosisJson>());
      this.dataModel.setImmunizations(new ArrayList<String>());
      
      for (PceData pce : this.pceDataList) {
        DiagnosisJson rvj = new DiagnosisJson();
        rvj.setCode(pce.getReasonForVisitCode());
        rvj.setDiagnosis(pce.getReasonForVisit());
        this.dataModel.getReasonForVisit().add(rvj);
      
        int j = 0;
        for (String provider : pce.getProviders()) {
          if (j < pce.getProviderTitles().size()) {
            String title = pce.getProviderTitles().get(j);
            if (!title.isEmpty()) {
              this.dataModel.getProviders().add(provider + " - " + title);
            }
          }
          j++;
        }
        if (pce.getDiagnoses().size() >  0) {
          int i = 0;
          for (String diagnosis : pce.getDiagnoses()) {
            DiagnosisJson dj = new DiagnosisJson();
            dj.setDiagnosis(diagnosis);
            dj.setCode(pce.getCodes().get(i++));
            this.dataModel.getDiagnoses().add(dj);
          }
          
        }
        this.dataModel.getImmunizations().addAll(pce.getImmunizations());
      }
      if (this.dataModel.getReasonForVisit().size() == 0) {
        this.dataModel.setReasonForVisit(null);
      }
      if (this.dataModel.getProviders().size() == 0) {
        this.dataModel.setProviders(null);
      }
      if (this.dataModel.getDiagnoses().size() == 0) {
        this.dataModel.setDiagnoses(null);
      }
      if (this.dataModel.getImmunizations().size() == 0) {
        this.dataModel.setImmunizations(null);
      }
      
      this.dataModel.setPrimaryCareProvider(this.getDemographics().getPrimaryProvider());
      this.dataModel.setPrimaryCareTeam(this.getDemographics().getPrimaryTeam());
      this.dataModel.setPrimaryCareTeamMembers((List<PrimaryCareTeamMemberJson>)data.get("primaryCareTeam"));
      
      List<MedicationJson> vaMeds = new ArrayList<MedicationJson>();
      List<Object> results = (List<Object>)data.get("va_medications");
      if ((results != null) && (results.size() == 4)) {
        vaMeds.addAll((List<MedicationJson>)results.get(1));  // Clinic Administered Meds
        vaMeds.addAll((List<MedicationJson>)results.get(3));  // Outpatient Meds
      }
      List<MedicationJson> remoteVaMeds = (List<MedicationJson>)data.get("remote-va_medications");
      if (remoteVaMeds != null) {
        for (MedicationJson mj : remoteVaMeds) {
          mj.setId(null);
          mj.setType("Outpatient");
          if (mj.getDateExpires() != null) {
            try {
              mj.setDateExpires(DateUtils.formatDate(DateUtils.toDate(mj.getDateExpires(), "MMMM dd, yyyy"), "MM/dd/yyyy"));
            } catch(Exception e) {}
          }
          if (mj.getDateLastFilled() != null) {
            try {
              mj.setDateLastFilled(DateUtils.formatDate(DateUtils.toDate(mj.getDateLastFilled(), "MMMM dd, yyyy"), "MM/dd/yyyy"));
            } catch(Exception e) {}
          }
          if (mj.getDateLastReleased() != null) {
            try {
              mj.setDateLastReleased(DateUtils.formatDate(DateUtils.toDate(mj.getDateLastReleased(), "MMMM dd, yyyy"), "MM/dd/yyyy"));
            } catch(Exception e) {}
          }
        }
        vaMeds.addAll(remoteVaMeds);
      }
      this.dataModel.setVaMedications(vaMeds);
      
      List<MedicationJson> nonVaMeds = new ArrayList<MedicationJson>();
      List<MedicationJson> localNonVaMeds = (List<MedicationJson>)data.get("non-va_medications");
      if (localNonVaMeds != null) {
        for (MedicationJson mj : localNonVaMeds) {
          mj.setType("Outpatient");
          if (mj.getStartDate() != null) {
            try {
              mj.setStartDate(DateUtils.formatDate(DateUtils.toDate(mj.getStartDate(), "MMMM dd, yyyy"), "MM/dd/yyyy"));
            } catch(Exception e) {}
          }   
          if (mj.getStopDate() != null) {
            try {
              mj.setStopDate(DateUtils.formatDate(DateUtils.toDate(mj.getStopDate(), "MMMM dd, yyyy"), "MM/dd/yyyy"));
            } catch(Exception e) {}
          }          
        }
        nonVaMeds.addAll(localNonVaMeds);
      }
      List<MedicationJson> remoteNonVaMeds = (List<MedicationJson>)data.get("remote-non_va_medications");
      if (remoteNonVaMeds != null) {
        for (MedicationJson mj : remoteNonVaMeds) {
          mj.setId(null);
          mj.setType("Outpatient");
          if (mj.getStartDate() != null) {
            try {
              mj.setStartDate(DateUtils.formatDate(DateUtils.toDate(mj.getStartDate(), "MMMM dd, yyyy"), "MM/dd/yyyy"));
            } catch(Exception e) {}
          }   
          if (mj.getStopDate() != null) {
            try {
              mj.setStopDate(DateUtils.formatDate(DateUtils.toDate(mj.getStopDate(), "MMMM dd, yyyy"), "MM/dd/yyyy"));
            } catch(Exception e) {}
          }          
        }        
        nonVaMeds.addAll(remoteNonVaMeds);
      }
      this.dataModel.setNonvaMedications(nonVaMeds);
      
      if (data.containsKey("clinicalCharts")) {
        LinkedHashMap<String, List<DiscreteItemData>> chartData = 
            (LinkedHashMap<String, List<DiscreteItemData>>)data.get("clinicalCharts");
        LinkedHashMap<String, List<DiscreteItemJson>> cdJson =
            new LinkedHashMap<String, List<DiscreteItemJson>>();
        Set<String> chartTypes = chartData.keySet();
        Iterator<String> it = chartTypes.iterator();
        while (it.hasNext()) {
          String chartType = it.next();
          List<DiscreteItemData> cd = chartData.get(chartType);
          List<DiscreteItemJson> list = new ArrayList<DiscreteItemJson>();
          cdJson.put(chartType, list);
          for (DiscreteItemData di : cd) {
            DiscreteItemJson dij = new DiscreteItemJson();
            try {
              dij.setDatetime(DateUtils.toDateTimeStr(DateUtils.fmDateTimeToDateTime(di.getFmDate()).getTime(), "MM/dd/yyyy@HH:mm:ss"));
            } catch(Exception e) {}
            dij.setFmDate(di.getFmDate());
            dij.setValue(di.getValue());
            if (!di.getAlert().isEmpty()) {
              dij.setAlert(di.getAlert());
            }
            dij.setSampleType(di.getSampleType());
            dij.setReferenceRange(di.getReferenceRange());
            list.add(dij);
          }
        }
        this.dataModel.setDiscreteData(cdJson);
      }
    }
  }
    
  private void buildPvs() {
    
    this.sections = DEF_PVS_SECTIONS;
    
    sectionsDisplayed = new HashMap<String, String>();
    if ((sections != null)) {
      String[] sectionsArr = StringUtils.pieceList(sections, ',');
      for (int i = 0; i < sectionsArr.length; i++) {
        sectionsDisplayed.put(sectionsArr[i], sectionsArr[i]);
      }
    }
    
    CallbackObject callback = new CallbackObject();
    List<SheetDataThread> dataThreads = new ArrayList<SheetDataThread>();
    SheetDataThread sheetDataThread = 
        new SheetDataThreadInitializer(super.facilityNo, this.getLang());
    
    sheetDataThread = new PceDataThread();
    initSheetDataThread(sheetDataThread, callback);
    callback.setNumThreadsCreated(1);
    sheetDataThread.start();
    
    synchronized (callback) {
      try {
        callback.wait(THREAD_TIMEOUT);
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
    }

    Hashtable<String, Object> results = callback.getThreadData();
    this.pceDataList = (List<PceData>)results.get("pceData");
    this.processPceData();
      
    if (isSectionDisplayed("clinicsVisited")) {
      sheetDataThread = new ClinicsVisitedThread();
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
    }
    if (isSectionDisplayed("clinicalReminders")) {
      sheetDataThread = new ClinicalRemindersThread();
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
    }      
    if (isSectionDisplayed("appointments")) {
      sheetDataThread = new AppointmentsThread();
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
    }
    if (isSectionDisplayed("pcp")) {
      sheetDataThread = new PcpThread();
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
    }
    if (isSectionDisplayed("primaryCareTeam")) {
      sheetDataThread = new PrimaryCareTeamThread();
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
    }    
    if (isSectionDisplayed("procedures")) {
      sheetDataThread = new ProceduresThread();
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
    }      
    if (isSectionDisplayed("allergies")) {
      sheetDataThread = new AllergiesThread();
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
    }
    if (isSectionDisplayed("medications")) {
      // local va meds
      sheetDataThread = new LocalVaMedicationsThread();
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
      // local non-va meds
      sheetDataThread = new LocalNonVaMedicationsThread();
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
      // remote va meds
      sheetDataThread = new RemoteVaMedicationsThread(true);
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);
      // remote non-va meds
      sheetDataThread = new RemoteNonVaMedicationsThread(true);
      initSheetDataThread(sheetDataThread, callback);
      dataThreads.add(sheetDataThread);        
    }
    callback.reset();
    callback.setNumThreadsCreated(dataThreads.size());
   
    if (dataThreads.size() > 0) {
      for (SheetDataThread dataThread : dataThreads) {
        // Start each of the threads
        dataThread.start();
      }
     
      // Wait for the app threads to return or timeout
      synchronized (callback) {
        try {
          callback.wait(THREAD_TIMEOUT);
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
      // All data threads returned or timed out so get results
      dataThreads.clear();
      callback.setTimedOut(true);     
    }
    
    String groupContent = this.renderGroups(callback.getThreadContent(), callback.getThreadData());
    
    // render header, body, and footer sections
    StringBuffer body = new StringBuffer();
    body.append(this.renderHeader());
    body.append(groupContent);
    String footer = this.renderFooter();
    
    // if the target media is a PDF document, insert footers at the end of each page
    if (this.media == MEDIA_PDF) {
      StringBuffer ptNameFooter = new StringBuffer("<div class='footer'>");
      ptNameFooter.append(!IS_DEMO ? this.getDemographics().getName() : DEMO_PT_NAME); 
      ptNameFooter.append("</div>");
      
      Matcher m = lnBrkPattern.matcher(body.toString());
      int count = 0;
      int pageNum = 0;
      while (m.find()) {
        count++;
        if (count == LINES_PER_PAGE) {
          int end = m.end();
          body.insert(end + (ptNameFooter.length() * pageNum), ptNameFooter.toString());
          count = 0;
          pageNum++;
        }
      }
      int numLines = footer.length() / 100; // assume a line has 100 characters
      int over = (count + numLines) - LINES_PER_PAGE;
      if (over > 0) {
        body.append(ptNameFooter.toString());
      } 
    }
    
    body.append(footer);
    
    StringBuffer csBody = new StringBuffer();
    csBody.append("<div id=\"sheet-contents\" class=\"" + this.fontClass + "\">");
    csBody.append(body.toString());
    csBody.append("</div>");

    this.pvsBody = csBody.toString();
  }  
  
  private void initSheetDataThread(SheetDataThread sheetDataThread, ICallbackObject callback) {
    sheetDataThread.setSecurityContext(super.securityContext);
    sheetDataThread.setFacilityNo(this.getStationNo());
    sheetDataThread.setStationNo(super.facilityNo);
    sheetDataThread.setFacilityName(super.facilityName);
    sheetDataThread.setUserDuz(super.userDuz);
    sheetDataThread.setPatientDfn(super.patientDfn);
    sheetDataThread.setFontClass(this.fontClass);
    sheetDataThread.setDemographics(this.getDemographics());
    if (this.getDemographics() != null) {
      sheetDataThread.setPatientSsn(this.getDemographics().getSsn());
    }
    sheetDataThread.setEncounterCache(this.encounterCache);
    sheetDataThread.setMedia(this.media);
    sheetDataThread.setDocType(this.getDocType());
    sheetDataThread.setInitialRequest(this.initialRequest);
    sheetDataThread.setLanguage(this.getLang());
    sheetDataThread.setCallback(callback);
    sheetDataThread.setContentType(SheetDataThread.CONTENT_HTML);
    sheetDataThread.setFacilityPrefs(this.getFacilityPrefs());
    sheetDataThread.setPceDataList(this.pceDataList);
  }
  
  private DataModel getDataModel() {
    if (this.dataModel != null) {
      return this.dataModel;
    }
    Header header = new Header();
    header.setStationNo(super.facilityNo);
    header.setSite(SessionUtil.getLoginUserInfo(this.request).getDivisionName());
    header.setUserDuz(super.userDuz);
    header.setUserName(SessionUtil.getLoginUserInfo(this.request).getUserName01());
    
    try {
      MiscVBService miscVBService = VistaBrokerServiceFactory.getMiscVBService();
      double fmNow = miscVBService.fmNow(super.securityContext).getPayload();
      String dtTmStr = DateUtils.toDateTimeStr(DateUtils.fmDateTimeToDateTime(fmNow).getTime(), "MM/dd/yyyy@HH:mm:ss");
      header.setTimestamp(dtTmStr);
    } catch(Exception e) {}
    dataModel = new DataModel();
    dataModel.setHeader(header);
    return dataModel;
  }
  
  
  private String renderCustomContent() {
    return TPL_GROUPS_WRAPPER.replace("__CONTENTS__", this.customContent);
  }
  
  private String renderGroups(Hashtable<String, String> content, Hashtable<String, Object> data) {
    
    StringBuffer html = new StringBuffer();
    if (isSectionDisplayed("patientInfo")) {
      html.append(this.renderPatientInformationGroup(content, data));
    }    
    html.append(this.renderTodaysVisitGroup(content, data));
    html.append(this.renderImportantNotesGroup(content, data));
    html.append(this.renderOngoingCareGroup(content, data));
    
    HashMap<String, String> strings = this.fetchDynamicStrings();
    String temp = html.toString();
    for (String key : strings.keySet()) {
      temp = temp.replaceAll(key, strings.get(key));
    }
    
    return TPL_GROUPS_WRAPPER.replace("__CONTENTS__", temp);
  }
  
  private String renderHeader() {
    String html = this.getSheetConfig().getHeader();
    
    HashMap<String, String> strings = this.fetchDynamicStrings(); 
    for (String key : strings.keySet()) {
      html = html.replaceAll(key, strings.get(key));
    }
    
    if (this.getDocType().equals(EncounterInfo.PVS)) {
      html = html.replaceAll(this.getStringResource("avs"), this.getStringResource("pvs"));
    }    
    
    return TPL_HEADER
        .replace("__CLASS__", this.fontClass)
        .replace("__CONTENTS__", html);
  }
  
  private String renderFooter() {
    
    String html = null; 
    
    if (this.getDocType().equals(EncounterInfo.AVS)) {
      html = this.getSheetConfig().getFooter();
    } else {
      html = this.getSheetConfig().getPvsFooter();
    }

    HashMap<String, String> strings = this.fetchDynamicStrings(); 
    for (String key : strings.keySet()) {
      html = html.replaceAll(key, strings.get(key));
    }

    html = "<p>" + html + "</p><br/>";
    
    StringBuffer boilerplates = new StringBuffer();

    List<String> items = this.getBoilerplateComments();

    if (items.size() > 0) {

      boilerplates.append("<div>");

      for (int i = 0; i < items.size(); i++) {
        if (i > 0) {
          boilerplates.append("\n\n"); 
        }
        boilerplates.append("<p>").append(items.get(i).trim()).append("</p><br/>");
      }

      boilerplates.append("</div>");
    }

    return TPL_FOOTER
      .replace("__CLASS__", this.fontClass)
      .replace("__FOOTER_HTML__", html)
      .replace("__BOILERPLATES__", boilerplates.toString());
  }
  
  private String renderPatientInformationGroup(Hashtable<String, String> content, Hashtable<String, Object> data) {

    StringBuffer contents = new StringBuffer();
    
    PatientInformation patientInfo = (PatientInformation)data.get("patientInfo");
    contents.append(renderPatientName(patientInfo));
    contents.append(renderPatientDob(patientInfo));
    contents.append(renderPatientSmokingStatus(patientInfo));
    contents.append(renderPatientPreferredLanguage(patientInfo));
    
    return TPL_GROUP
        .replace("__GROUP_ID_SUFFIX__", "patientInfo")
        .replace("__GROUP_TITLE__", this.getStringResource("patientInfo"))
        .replace("__CONTENTS__", contents.toString()); 
  }  
  
  private String renderPatientName(PatientInformation patientInfo) {
    if ((patientInfo == null) || (patientInfo.getName() == null) || patientInfo.getName().isEmpty()) {
      return "";
    } else {
      return TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "patientName")
        .replace("__SECTION_TITLE__", this.getStringResource("patientName"))
        .replace("__CONTENTS__", !IS_DEMO ? patientInfo.getName() : DEMO_PT_NAME); 
    }     
  }  
  
  private String renderPatientDob(PatientInformation patientInfo) {
    if ((patientInfo == null) || (patientInfo.getDob() == null) || patientInfo.getDob().isEmpty()) {
      return "";
    } else {
      StringBuffer content = new StringBuffer();
      content.append(!IS_DEMO ? patientInfo.getDob() : DEMO_PT_DOB)
      .append(" (")
      .append(!IS_DEMO ? patientInfo.getAge() : DEMO_PT_AGE)
      .append(")");      
      return TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "patientDob")
        .replace("__SECTION_TITLE__", this.getStringResource("patientDob"))
        .replace("__CONTENTS__", content.toString());
    }     
  }  
  
  private String renderPatientSmokingStatus(PatientInformation patientInfo) {
    if ((patientInfo == null) || (patientInfo.getSmokingStatus() == null) || patientInfo.getSmokingStatus().isEmpty()) {
      return "";
    } else {
      StringBuffer content = new StringBuffer();
      content.append(patientInfo.getSmokingStatus())
      .append(" (" + this.getStringResource("updated") + ": ")
      .append(StringUtils.piece(patientInfo.getSmokingStatusDate(), '@', 1))
      .append(")");
      return TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "smokingStatus")
        .replace("__SECTION_TITLE__", this.getStringResource("smokingStatus"))
        .replace("__CONTENTS__", content.toString());
    }     
  }  
  
  private String renderPatientPreferredLanguage(PatientInformation patientInfo) {
    if ((patientInfo == null) || (patientInfo.getPreferredLanguage() == null) || 
        patientInfo.getPreferredLanguage().isEmpty()) {
      return "";
    } else {
      return TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "preferredLanguage")
        .replace("__SECTION_TITLE__", this.getStringResource("preferredLanguage"))
        .replace("__CONTENTS__", patientInfo.getPreferredLanguage());
    }     
  }   
  
  private String renderTodaysVisitGroup(Hashtable<String, String> content, Hashtable<String, Object> data) {

    StringBuffer contents = new StringBuffer();
    
    if (isSectionDisplayed("clinicsVisited")) {
      contents.append(content.get("clinicsVisited"));
    }
    if (isSectionDisplayed("providers")) {
      contents.append(renderProviders());
    }
    if (isSectionDisplayed("diagnoses")) {
      contents.append(renderReasonForVisit());
      if (this.getDocType().equals(EncounterInfo.AVS)) {
        contents.append(renderDiagnoses());
      }
    }
    if (isSectionDisplayed("clinicalReminders") && 
        content.containsKey("clinicalReminders")) {
      contents.append(content.get("clinicalReminders"));
    }      
    if (isSectionDisplayed("immunizations")) {
      contents.append(renderImmunizations());
    }
    if (isSectionDisplayed("procedures")) {
      contents.append(content.get("procedures"));
    }    
    if (isSectionDisplayed("vitals")) {
      contents.append(content.get("vitals"));
    }
    if (isSectionDisplayed("orders")) {
      contents.append(content.get("orders"));
    }
    if (isSectionDisplayed("medications")) {
      List<Object> results = (List<Object>)data.get("va_medications");
      if ((results != null) && (results.size() > 0)) {
        contents.append((String)results.get(0));  // Clinic Administered Medications
      }
    }    

    return TPL_GROUP
        .replace("__GROUP_ID_SUFFIX__", "today")
        .replace("__GROUP_TITLE__", this.getStringResource("todaysVisit"))
        .replace("__CONTENTS__", contents.toString()); 
  }
  
  private String renderProviders() {
    if ((this.pceDataList == null) || (this.pceDataList.size() == 0)) {
      return "";
    }
    StringBuffer body = new StringBuffer();
    
    HashMap<String, String> providersMap = new HashMap<String, String>();
    for (PceData pce : this.pceDataList) {
      int j = 0;
      for (String provider : pce.getProviders()) {
        if (j < pce.getProviderTitles().size()) {
          String providerDuz = pce.getProviderDuzs().get(j);
          if (!providersMap.containsKey(providerDuz)) {
            String title = pce.getProviderTitles().get(j);
            if (!title.isEmpty()) {
              providersMap.put(providerDuz, provider + " - " + title);
            } else {
              providersMap.put(providerDuz, provider);
            }
          }
        }
        j++;
      }
    }
    Iterator<String> keys = providersMap.keySet().iterator();
    switch (providersMap.size()) {
      case 0:
        return "";
      case 1:
        body.append("<p>").append(providersMap.get(keys.next())).append("</p>");
        break;
      default:
        List<String> providers = new ArrayList<String>();
        while (keys.hasNext()) {
          providers.add(providersMap.get(keys.next()));
        }
        body.append(AvsWebUtils.renderUnorderedList(providers));
    }    
    return TPL_SECTION
      .replace("__SECTION_CLASS__", "section")
      .replace("__SECTION_ID_SUFFIX__", "providers")
      .replace("__SECTION_TITLE__", this.getStringResource("providers"))
      .replace("__CONTENTS__", body.toString());
  }
  
  private String renderImmunizations() {
    if ((this.pceDataList == null) || (this.pceDataList.size() == 0)) {
      return "";
    }
    List<String> immunizations = new ArrayList<String>();
    for (PceData pce : this.pceDataList) {
      if ((pce.getImmunizations() != null) && !pce.getImmunizations().isEmpty()) {
        immunizations.addAll(pce.getImmunizations());
      }
    }
    if (immunizations.size() == 0) {
      return "";
    }    
    String list = AvsWebUtils.renderTwoColumnList(immunizations, this.media == MEDIA_PDF, this.fontClass);
    return TPL_SECTION
      .replace("__SECTION_CLASS__", "section")
      .replace("__SECTION_ID_SUFFIX__", "immunizations")
      .replace("__SECTION_TITLE__", this.getStringResource("immunizations"))
      .replace("__CONTENTS__", list);
  }
  
  private String renderDiagnoses() {
    if ((this.pceDataList == null) || (this.pceDataList.size() == 0)) {
      return "";
    }
    List<String> diagnoses = new ArrayList<String>();
    for (PceData pce : this.pceDataList) {
      if ((pce.getDiagnoses() != null) && !pce.getDiagnoses().isEmpty()) {
        diagnoses.addAll(pce.getDiagnoses());
      }
    }
    if (diagnoses.size() > 0) {
      String list = AvsWebUtils.renderTwoColumnList(diagnoses, this.media == MEDIA_PDF, this.fontClass);
      return TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "diagnoses")
        .replace("__SECTION_TITLE__", this.getStringResource("diagnoses"))
        .replace("__CONTENTS__", list);
    } else {
      return "";
    }
  }
  
  private String renderReasonForVisit() {
    if (this.getDocType().equals(EncounterInfo.AVS)) {
      if ((this.pceDataList == null) || (this.pceDataList.size() == 0)) {
        return "";
      }
      List<String> rfv = new ArrayList<String>();
      for (PceData pce : this.pceDataList) {
        if ((pce.getReasonForVisit() != null) && !pce.getReasonForVisit().isEmpty()) {
          rfv.add(pce.getReasonForVisit());
        }
      }
      String content = null;
      if (rfv.size() > 1) {
        content = AvsWebUtils.renderTwoColumnList(rfv, this.media == MEDIA_PDF, this.fontClass);
      } else if (rfv.size() == 1) {
        content = rfv.get(0);
      }
      if (content != null) {
        return TPL_SECTION
          .replace("__SECTION_CLASS__", "section")
          .replace("__SECTION_ID_SUFFIX__", "reasonForVisit")
          .replace("__SECTION_TITLE__", this.getStringResource("reasonForVisit"))
          .replace("__CONTENTS__", content);
      } else {
        return "";
      }
    } else {
      StringBuffer body = new StringBuffer("<div class=\"visit-instructions\">");
      body.append(this.getStringResource("reasonForVisitInstructions"))
      .append("</div><div class=\"visit-comments\">&nbsp;</div>")
          .append(this.getStringResource("pvsPatientQuestions")) 
          .append("<br><br>")
          .append("<input type=\"checkbox\">&nbsp;&nbsp;")
          .append(this.getStringResource("pvsPatientMedicationsQuestions"))
          .append("<br>")
          .append("<input type=\"checkbox\">&nbsp;&nbsp;")
          .append(this.getStringResource("pvsPatientTestsQuestions"))
          .append("<br>")
          .append("<input type=\"checkbox\">&nbsp;&nbsp;")
          .append(this.getStringResource("pvsPatientDiagnosesQuestions"))
          .append("<br>")
          .append("<input type=\"checkbox\">&nbsp;&nbsp;")
          .append(this.getStringResource("pvsPatientReferralsQuestions"))
          .append("<br>")
          .append("<input type=\"checkbox\">&nbsp;&nbsp;")
          .append(this.getStringResource("pvsPatientOtherQuestions"))
          .append("<br><br>")          
          .append("</div>");
      return TPL_SECTION
          .replace("__SECTION_CLASS__", "section")
          .replace("__SECTION_ID_SUFFIX__", "reasonForVisit")
          .replace("__SECTION_TITLE__", this.getStringResource("reasonForVisit"))
          .replace("__CONTENTS__", body.toString());
    }
  }  
  
  private String renderImportantNotesGroup(Hashtable<String, String> content, Hashtable<String, Object> data) {

    StringBuffer contents = new StringBuffer();
    
    if (isSectionDisplayed("appointments")) {
      contents.append(content.get("appointments"));
    }    
    if (isSectionDisplayed("comments")) {
      contents.append(content.get("comments"));
    }
    
    return TPL_GROUP
    .replace("__GROUP_ID_SUFFIX__", "notes")
    .replace("__GROUP_TITLE__", this.getStringResource("importantNotes"))
    .replace("__CONTENTS__", contents.toString());
  }

  private String renderOngoingCareGroup(Hashtable<String, String> content, Hashtable<String, Object> data) {

    StringBuffer contents = new StringBuffer();
    
    if (isSectionDisplayed("pcp")) {
      contents.append(content.get("pcp"));
    }
    if (isSectionDisplayed("primaryCareTeam")) {
      contents.append(content.get("primaryCareTeam"));
    }    
    if (isSectionDisplayed("allergies")) {
      contents.append(content.get("allergies"));
    }
    if (isSectionDisplayed("medications")) {
      List<Object> results = (List<Object>)data.get("va_medications");
      if ((results != null) && (results.size() > 2)) {
        contents.append(results.get(2)); // Local VA Medications
      }
      String nonVaMeds = (String)content.get("non-va_medications");
      if ((nonVaMeds != null) && !nonVaMeds.isEmpty() && !nonVaMeds.equals("null")) {
        contents.append(nonVaMeds);
      }
      if (this.getDocType().equals(EncounterInfo.AVS)) {
        contents.append(renderRemoteVaMedicationsSection());
        if (data.containsKey("remote-va_medications")) {
          this.remoteVaMeds = (List<MedicationJson>)data.get("remote-va_medications");
        }
      } else {
        if (content.containsKey("remote-va_medications")) {
          this.remoteVaMedicationsHtml = content.get("remote-va_medications");
          contents.append(this.renderRemoteVaMedicationsSection());    
        }
      }
      if (data.containsKey("remote-non_va_medications") || content.containsKey("remote-non_va_medications")) {
        if (this.getDocType().equals(EncounterInfo.AVS)) {
          this.remoteNonVaMeds = (List<MedicationJson>)data.get("remote-non_va_medications");
        } else {
          this.remoteNonVaMedicationsHtml = content.get("remote-non_va_medications");
          this.renderRemoteNonVaMedicationsSection(contents);    
        }
      }
    }
    if (((this.charts != null) && !this.charts.isEmpty()) || 
         ((this.chartFilenames != null) && !this.chartFilenames.isEmpty()) &&
        isSectionDisplayed("clinicalCharts")) {
      contents.append(content.get("clinicalCharts"));
    }
    if ((this.labResultsDaysBack > 0) && isSectionDisplayed("labResults")) {
      contents.append(content.get("labResults"));
    }

    return TPL_GROUP
    .replace("__GROUP_ID_SUFFIX__", "ongoing")
    .replace("__GROUP_TITLE__", this.getStringResource("myOngoingCare"))
    .replace("__CONTENTS__", contents.toString());
    
  }
  
  private String renderAdditionalInformationGroup() {

    StringBuffer contents = new StringBuffer();
    if ((this.printAllServiceDescriptions) || 
        ((this.selectedServiceDescriptions != null) && !this.selectedServiceDescriptions.isEmpty())) {
      contents.append(this.renderServicesSection());
    }

    if (contents.toString().isEmpty()) {
      return "";
    }
    
    return TPL_GROUP
      .replace("__GROUP_ID_SUFFIX__", "information")
      .replace("__GROUP_TITLE__", this.getStringResource("additionalInformation") + " - " + this.getVistaUser().getStation())
      .replace("__CONTENTS__", contents.toString());
  }
  
  private String renderServicesSection() {

    usageLog("Clinical Services", "");
    
    List<Service> services = (List<Service>)getSettingsService().fetchServices(this.getStationNo()).getCollection();
    
    HashMap<Long, Long> servicesMap = null;
    if ((this.selectedServiceDescriptions != null) && !this.selectedServiceDescriptions.isEmpty()) {
      servicesMap = new HashMap<Long, Long>();
      String[] servicesArr = StringUtils.pieceList(this.selectedServiceDescriptions, ',');
      for (int i = 0; i < servicesArr.length; i++) {
        try {
          Long id = Long.valueOf(servicesArr[i]);
          servicesMap.put(id, id);
        } catch(NumberFormatException nfe) {}
      }
    }
    
    StringBuffer body = new StringBuffer();

    if (services.size() == 0) {
      return "";
    } else {
      for (Service service : services) {
        
        if ((servicesMap != null) && (!servicesMap.containsKey(service.getId()))) {
          continue;
        }
        
        body.append("<div class=\"service-name\">")
          .append(service.getName())
          .append("</div>\n");

        if ((service.getLocation() != null) && !service.getLocation().isEmpty()) {
          body.append("<div class=\"service-detail\">")
          .append("Location: ")
          .append(service.getLocation())
          .append("</div>\n");       
        }
        if ((service.getHours() != null) && !service.getHours().isEmpty()) {
          body.append("<div class=\"service-detail\">")
          .append("Hours of Operation: ")
          .append(service.getHours())
          .append("</div>\n");       
        }  
        if ((service.getPhone() != null) && !service.getPhone().isEmpty()) {
          body.append("<div class=\"service-detail\">")
          .append("Phone: ")
          .append(service.getPhone())
          .append("</div>\n");       
        }        
        if ((service.getComment() != null) && !service.getComment().isEmpty()) {
          body.append("<div class=\"service-detail\">")
          .append("Comment: ")
          .append(service.getComment())
          .append("</div>\n");       
        }
      }
    }

    return TPL_SECTION
      .replace("__SECTION_CLASS__", "section")
      .replace("__SECTION_ID_SUFFIX__", "services")
      .replace("__SECTION_TITLE__", this.getStringResource("clinicalServices"))
      .replace("__CONTENTS__", body.toString());
  }
  
  public String renderRemoteVaMedicationsSection() {
    if ((this.initialRequest && (this.media == MEDIA_HTML) && this.getDocType().equals(EncounterInfo.AVS)) || 
        ((this.remoteVaMedicationsHtml != null) && !this.remoteVaMedicationsHtml.isEmpty())) {
      StringBuffer body = new StringBuffer();
      body.append("<div id=\"remote-va-medications-div\">");
      body.append(this.remoteVaMedicationsHtml != null ? this.remoteVaMedicationsHtml : "");
      body.append("</div>");
      
      return TPL_SECTION
             .replace("__SECTION_CLASS__", "section")
             .replace("__SECTION_ID_SUFFIX__", "remote-va_medications")
             .replace("__SECTION_TITLE__", this.getStringResource("remoteVaMeds"))
             .replace("__CONTENTS__", body.toString());
      
    } else {
      return "";
    }
    
  } 
  
  public void renderRemoteNonVaMedicationsSection(StringBuffer contents) {
    
    if (((this.remoteNonVaMedicationsHtml != null) && !this.remoteNonVaMedicationsHtml.isEmpty())) {
      int index = contents.indexOf("<div id=\"non-va-meds-div\">");
      if (index > 0) {
        contents.insert(index + 26, this.remoteNonVaMedicationsHtml);
      }
      index = contents.indexOf("<div id=\"section-non-va_medications\" class=\"section-hidden\">");
      if (index > 0) {
        contents.replace(index + 44, index + 58, "section");
      }
    }
  }   
  
  private void setDefaults() {
    if ((this.fontClass == null) || this.fontClass.isEmpty()) {
      this.fontClass = NORMAL_FONT_CLS;
    }    
    
    if ((this.labDateRange == null) || this.labDateRange.isEmpty()) {
      this.labResultsDaysBack = DEF_LAB_DATE_RANGE;
    } else {
      this.labResultsDaysBack = StringUtils.toInt(this.labDateRange, -1);
    }      
    
    if ((this.sections == null) || (this.sections.isEmpty())) {
      this.sections = DEF_AVS_SECTIONS;
    }
    
    if (this.charts == null) {
      this.charts = "";
    }     
    
    if (this.selectedServiceDescriptions == null) {
      this.selectedServiceDescriptions = "";
    }
    
    if (this.language == null) {
      this.language = this.getLang();
    }
    
    try {
      if (CSS_CONTENTS == null) {
        StringBuffer sb = new StringBuffer();
        HttpClient client = new DefaultHttpClient();
        for (int i = 0; i < 2; i++) {
          String path = null;
          if (i == 0) {
            path = BASE_URI + SHEET_CSS_PATH;
          } else {
            path = BASE_URI + PDF_CSS_PATH;
          }
          HttpGet request = new HttpGet(path);
          HttpResponse response = client.execute(request);
          BufferedReader rd = new BufferedReader
            (new InputStreamReader(response.getEntity().getContent()));
          String line = "";
          while ((line = rd.readLine()) != null) {
            sb.append(line);
          } 
        }
        CSS_CONTENTS = sb.toString();
      }
    } catch(Exception e) {
      log.error("Error getting CSS contents", e);      
    }      
  }  
  
  private String getStationNo() {
    if (this.stationNo == null) {
      this.stationNo = this.getSettingsService().getFacilityNoForDivision(this.facilityNo).getPayload();
    }
    return this.stationNo;
  } 
  
  private VistaUser getVistaUser() {
    if (this.vistaUser == null) {
      ServiceResponse<VistaUser> response = this.getUserVBService().getVistaUser(this.securityContext);
      AvsWebUtils.handleServiceErrors(response, log);
      this.vistaUser = response.getPayload();
    }
    return this.vistaUser;
  }  
  
  private BasicDemographics getDemographics() {
    if (this.demographics == null) {
      
      CallbackObject callback = new CallbackObject();
      callback.setNumThreadsCreated(1);
      SheetDataThread sheetDataThread = new DemographicsThread();
      sheetDataThread.setSecurityContext(super.securityContext);
      sheetDataThread.setFacilityNo(this.getStationNo());
      sheetDataThread.setStationNo(super.facilityNo);
      sheetDataThread.setUserDuz(super.userDuz);
      sheetDataThread.setPatientDfn(super.patientDfn);  
      sheetDataThread.setCallback(callback);
      sheetDataThread.start();
      
      synchronized (callback) {
        try {
          callback.wait(THREAD_TIMEOUT);
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
      
      Hashtable<String, Object> results = callback.getThreadData();
      this.demographics = (BasicDemographics)results.get("demographics");
    }
    return this.demographics;
  }   
  
  private String getVisitStr(int index) {
    return super.getLocationIens().get(index) + ";" + super.getDatetimes().get(index) + ";A";
  }
  
  private void processPceData() {
    this.pceDataMap = new HashMap<String, PceData>();
    this.userIsProvider = false;
    int index = 0;
    if ((this.pceDataList != null) && (this.pceDataList.size() > 0)) {
      List<gov.va.med.lom.avs.model.Encounter> avsEncounters = new
          ArrayList<gov.va.med.lom.avs.model.Encounter>();
      for (PceData pce : this.pceDataList) {
        this.pceDataMap.put(pce.getVisitString(), pce);
        gov.va.med.lom.avs.model.Encounter avsEncounter = 
            new gov.va.med.lom.avs.model.Encounter();
        avsEncounter.setVisitString(pce.getVisitString());
        avsEncounter.setEncounterDatetime(pce.getDatetime());
        if ((pce.getClinicIen() != null) && !pce.getClinicIen().isEmpty()) {
          avsEncounter.setLocation(new EncounterLocation(pce.getClinicIen(), pce.getClinicName()));
        } else {
          avsEncounter.setLocation(new EncounterLocation(super.getLocationIens().get(index), super.getLocationNames().get(index)));
        }
        List<EncounterProvider> providers = new ArrayList<EncounterProvider>();
        avsEncounter.setProviders(providers);
        int i = 0;
        for (String providerDuz : pce.getProviderDuzs()) {
          String providerName = pce.getProviders().get(i);
          String providerTitle = pce.getProviderTitles().get(i);
          providers.add(new EncounterProvider(providerDuz, providerName, providerTitle));
          i++;
        }
        avsEncounters.add(avsEncounter);
        index++;
        for (String providerDuz : pce.getProviderDuzs()) {
          this.userIsProvider = this.userIsProvider || this.userDuz.equals(providerDuz.trim());
        }
      }
      this.encounterCache.setEncounters(avsEncounters); 
    }    
  }
  
  private SheetConfig getSheetConfig() {
    if (this.sheetConfig == null) {
      String stationNo = (this.pceDataList != null) && (this.pceDataList.size() > 0) && 
          this.pceDataList.get(0).getInstitutionIen() != null ? 
          this.pceDataList.get(0).getInstitutionIen() : this.stationNo;
      ServiceResponse<SheetConfig> response = this.settingsService
          .getSheetConfig(stationNo, super.getLocationIens().get(0), super.userDuz);
      AvsWebUtils.handleServiceErrors(response, log);
      this.sheetConfig = response.getPayload();
    }
    return this.sheetConfig;
  }  
  
  private SheetService getSheetService() {
    if (this.sheetService == null) {
      this.sheetService = ServiceFactory.getSheetService();
    }
    return this.sheetService;
  }
  
  private SettingsService getSettingsService() {
    if (this.settingsService == null) {
      this.settingsService = ServiceFactory.getSettingsService();
    }
    return this.settingsService;
  }
  
  private PatientVBService getPatientVBService() {
    if (this.patientVBService == null) {
      this.patientVBService = VistaBrokerServiceFactory.getPatientVBService();
    }
    return this.patientVBService;
  }
  
  private UserVBService getUserVBService() {
    if (this.userVBService == null) {
      this.userVBService = VistaBrokerServiceFactory.getUserVBService();
    }
    return this.userVBService;
  }  
  
  private EncounterCacheMongo getEncounterCache() {
    EncounterCacheMongo cache = null;
    try {
      if ((this.encounterCache != null) && (this.encounterCache.getId() != null)) {
        cache = this.getSheetService().getEncounterCacheMongoById(this.encounterCache.getId()).getPayload();
      } else {
        cache = 
            this.getSheetService().getEncounterCacheMongo(this.getStationNo(), super.patientDfn, 
               super.getLocationIens(), super.getDatetimes(), this.getDocType()).getPayload();
      }
    } catch(Exception e) {
      log.error("Error getting encounter cache", e);
    }
    return cache;
  }
  
  private EncounterCacheMongo setEncounterCache() {
    EncounterCacheMongo existingCache = this.getEncounterCache();
    boolean doSave = false;
    try {
      // no existing cached encounter, so create new object
      if (existingCache == null) { 
        doSave = true;
      } else {
        this.encounterCache.setPdfFilename(existingCache.getPdfFilename());
        this.userIsProvider = getIsUserProvider();
        this.locked = this.userIsProvider ? this.locked : this.encounterCache.isLocked();
        doSave = (this.media == MEDIA_HTML) && !this.initialRequest;
      }
      if (doSave) {  
        this.encounterCache.setFacilityNo(this.getStationNo());
        this.encounterCache.setPatientDfn(super.patientDfn);
        this.encounterCache.setUserDuz(super.userDuz);
        this.encounterCache.setUserName(super.userName);
        
        if (this.userIsProvider || !this.locked) {
          this.encounterCache.setPrintServiceDescriptions(this.getPrintAllServiceDescriptions());
          this.encounterCache.setSelectedServiceDescriptions(this.getSelectedServiceDescriptions());
          this.encounterCache.setFontClass(this.getFontClass());
          this.encounterCache.setLabDateRange(this.labResultsDaysBack);
          this.encounterCache.setLanguage(this.getLang());
          this.encounterCache.setDocType(this.getDocType());
          this.encounterCache.setCharts(AvsWebUtils.delimitedStringToList(this.getCharts(), ','));
          this.encounterCache.setInstructions(this.getComments());
          this.encounterCache.setSections(AvsWebUtils.delimitedStringToList(this.getSections(), ','));    
          this.encounterCache.setCustomContent(this.getCustomContent());
          this.encounterCache.setRemoteVaMedications(this.getRemoteVaMedicationsHtml());
          this.encounterCache.setRemoteNonVaMedications(this.getRemoteNonVaMedicationsHtml());
        }
        if (existingCache != null) {
          this.encounterCache.setLocked(existingCache.isLocked());
          this.encounterCache.setPrinted(existingCache.isPrinted());
          this.copyEncounterNoteIens(existingCache, this.encounterCache);
          sheetService.deleteEncounterCacheMongo(existingCache);
        } else {
          this.encounterCache.setLocked(false);
          this.encounterCache.setPrinted(false);
        }
        sheetService.saveEncounterCacheMongo(this.encounterCache);
        this.setDefaults();
        this.encounterCache = this.getEncounterCache();
      } else {
        if (this.initialRequest || this.locked || 
            (this.media == MEDIA_PDF) || (this.media == MEDIA_JSON) || (this.media == MEDIA_XML)) {
          // copy existing cache params
          this.encounterCache.setFacilityNo(this.getStationNo());
          this.encounterCache.setPatientDfn(super.patientDfn);
          this.encounterCache.setPrintServiceDescriptions(existingCache.isPrintServiceDescriptions());
          this.encounterCache.setSelectedServiceDescriptions(existingCache.getSelectedServiceDescriptions());
          this.encounterCache.setFontClass(existingCache.getFontClass());
          this.encounterCache.setLabDateRange(existingCache.getLabDateRange());
          this.encounterCache.setLocked(existingCache.isLocked());
          this.encounterCache.setPrinted(existingCache.isPrinted());
          this.encounterCache.setLanguage(existingCache.getLanguage());
          this.encounterCache.setDocType(existingCache.getDocType());
          this.encounterCache.setCharts(existingCache.getCharts());
          this.encounterCache.setInstructions(existingCache.getInstructions());
          this.encounterCache.setSections(existingCache.getSections());    
          this.encounterCache.setCustomContent(existingCache.getCustomContent());
          this.encounterCache.setRemoteVaMedications(existingCache.getRemoteVaMedications());
          this.encounterCache.setRemoteNonVaMedications(existingCache.getRemoteNonVaMedications());          
          this.encounterCache.setUserDuz(existingCache.getUserDuz());
          this.encounterCache.setUserName(existingCache.getUserName());
          
          // set instance fields          
          this.charts = AvsWebUtils.delimitString(this.encounterCache.getCharts(), ',', false);
          this.fontClass = this.encounterCache.getFontClass();
          this.comments = this.encounterCache.getInstructions();
          this.labResultsDaysBack = this.encounterCache.getLabDateRange();
          this.sections = AvsWebUtils.delimitString(this.encounterCache.getSections(), ',', false);
          this.printAllServiceDescriptions = this.encounterCache.isPrintServiceDescriptions();
          this.selectedServiceDescriptions = this.encounterCache.getSelectedServiceDescriptions();
          this.locked = this.encounterCache.isLocked();
          this.customContent = this.encounterCache.getCustomContent();
          this.remoteVaMedicationsHtml = this.encounterCache.getRemoteVaMedications();
          this.remoteNonVaMedicationsHtml = this.encounterCache.getRemoteNonVaMedications();
          this.language = this.encounterCache.getLanguage();
          this.pdfFilename = this.encounterCache.getPdfFilename();
        }
        this.copyEncounterNoteIens(existingCache, this.encounterCache);
      }
    } catch(Exception e) {
      log.error("Error saving encounter cache", e);
    }
    return this.encounterCache;
  }
  
  private void copyEncounterNoteIens(EncounterCacheMongo existingCache, EncounterCacheMongo currentCache) {
    // copy encounter note iens from existing cache to in-memory cache
    HashMap<String, Encounter> encMap = new HashMap<String, Encounter>();
    for (Encounter encounter : existingCache.getEncounters()) {
      encMap.put(encounter.getVisitString(), encounter);
    }
    for (Encounter encounter : currentCache.getEncounters()) {
      Encounter existingEnc = encMap.get(encounter.getVisitString());
      if (existingEnc != null) {
        encounter.setEncounterNoteIen(existingEnc.getEncounterNoteIen());
      }
    }
  }
  
  private boolean getIsUserProvider() {
    for (gov.va.med.lom.avs.model.Encounter encounter : this.encounterCache.getEncounters()) {
      for (EncounterProvider provider : encounter.getProviders()) {
        if ((provider.getProviderDuz() != null) && provider.getProviderDuz().equals(this.userDuz)) {
          return true;
        }
      }
    }
    return true;
  }
  
  private FacilityPrefs getFacilityPrefs() {
    if (this.facilityPrefs == null) {
      this.facilityPrefs = this.getSettingsService().getFacilityPrefs(super.facilityNo).getPayload();
    }
    return this.facilityPrefs;
  }
  
  private HashMap<String, String> fetchDynamicStrings() {

    if (this.replacements == null) {
      this.replacements = new HashMap<String, String>();
      
      TimeZone tz = TimeZone.getTimeZone(this.getFacilityPrefs().getTimeZone());
      SimpleDateFormat df1 = new SimpleDateFormat("MMMM dd, yyyy HH:mm");
      df1.setTimeZone(tz);
      SimpleDateFormat df2 = new SimpleDateFormat("MMMM dd, yyyy");
      df2.setTimeZone(tz);
      Calendar now = Calendar.getInstance(tz);
      Date encounterDatetime = null;
      if ((this.encounterCache != null) && (this.encounterCache.getEncounters().size() > 0)) {
        encounterDatetime = FMDateUtils.fmDateTimeToDate(this.encounterCache.getEncounters().get(0).getEncounterDatetime());
      }
      
      String institution = (this.pceDataList != null) && (this.pceDataList.size() > 0) && 
          (this.pceDataList.get(0).getInstitutionName() != null) ?
          this.pceDataList.get(0).getInstitutionName() : this.getVistaUser().getStation();
      
      this.replacements.put("%PATIENT_NAME%", !IS_DEMO ? this.getDemographics().getName() : DEMO_PT_NAME);  
      this.replacements.put("%FACILITY_NAME%", institution);
      
      try {
        this.replacements.put("%ENCOUNTER_DATETIME%", df1.format(encounterDatetime));
      } catch (Exception e) {}
      try {
        this.replacements.put("%ENCOUNTER_DATE%", df2.format(encounterDatetime));
      } catch (Exception e) {}
      this.replacements.put("%CURRENT_DATETIME%", df1.format(now.getTime()));
      this.replacements.put("%CURRENT_DATE%", df2.format(now.getTime()));
    }
    return this.replacements;
  }  
  
  private List<String> getBoilerplateComments() {
    ArrayList<String> items = new ArrayList<String>();
    
    String providerComments = this.getSheetConfig().getProviderBoilerplate();
    if (providerComments != null && !providerComments.trim().isEmpty()) {
      items.add(providerComments);
    }

    String clinicComments = this.getSheetConfig().getClinicBoilerplate();
    if (clinicComments != null && !clinicComments.trim().isEmpty()) {
      items.add(clinicComments);
    }

    String facilityComments = this.getSheetConfig().getFacilityBoilerplate();
    if (facilityComments != null && !facilityComments.trim().isEmpty()) {
      items.add(facilityComments);
    }

    return items;
  }    
  
  private static String cleanCommentsForPdf(String body, String fontClass) {
    // remove <pre> tags used with comments, as this messes up spacing in pdf
    StringBuffer sb = new StringBuffer(body.replace("<pre id=\"comments-area\">", ""));
    int index = sb.lastIndexOf("</pre>");
    if (index > 0) {
      sb.delete(index, index + 6);
    }
    // remove open and closing <p> tags
    index = sb.indexOf("<p>");
    if (index >= 0) {
      sb.delete(index, index + 3);
    }    
    index = sb.lastIndexOf("</p>");
    if (index > 0) {
      sb.delete(index, index + 4);
    }
    // add selected font class in to the comments div
    index = sb.indexOf("<div id=\"comments-div\">");
    if (index > 0) {
      index = sb.indexOf("<div", index+2);
      if (index > 0) {
        int index2 = sb.indexOf(">", index);
        int index3 = sb.indexOf("font-size: 12px;", index);
        if ((index3 > 0) && (index3 < index2)) {
          sb.replace(index3, index3 + 15, "");
        }
        index = sb.indexOf(">", index);
        sb.insert(index, " class=\"" + fontClass + "\"");
      }
    }
    // add back spacing at bottom that the <pre> tag had added
    index = sb.indexOf("<div id=\"group-ongoing\"");
    if (index > 0) {
      sb.insert(index, "<div style=\"bottom-padding: 70px;\">&nbsp;</div>");
    }
    return sb.toString();
  }
  
  private boolean isSectionDisplayed(String section) {
    return (sectionsDisplayed.isEmpty()) || sectionsDisplayed.containsKey(section);
  }
  
  private static boolean sameLists(List<String> list1, List<String> list2) {
    HashMap<String, String> map = new HashMap<String, String>();
    for (String s : list1) {
      map.put(s, s);
    }
    boolean result = true;
    for (String s : list2) {
      result = result && map.containsKey(s);
    }
    if (!result) {
      return false;
    } else {
      map.clear();
      for (String s : list2) {
        map.put(s, s);
      }
      for (String s : list1) {
        result = result && map.containsKey(s);
      }
      return result;
    }
  }  
  
  private String getStringResource(String name) {
    return this.getStringResource(name, getLang());
  }
  
  private String getStringResource(String name, String language) {
    return StringResources.getStringResources().getStringResource(super.facilityNo, name, language);
  }
  
  public String getComments() {
    return this.comments != null && !this.comments.isEmpty() ? this.comments.trim() : "None";
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
  
  public String getFontClass() {
    return this.fontClass != null && !this.fontClass.isEmpty() ? this.fontClass : NORMAL_FONT_CLS;
  }

  public void setFontClass(String fontClass) {
    this.fontClass = fontClass;
  }

  public String getLabDateRange() {
    return this.labDateRange != null && !this.labDateRange.isEmpty() ? this.labDateRange : String.valueOf(DEF_LAB_DATE_RANGE);
  }

  public void setLabDateRange(String labDateRange) {
    this.labDateRange = labDateRange;
  }

  public String getPrinterIen() {
    return printerIen;
  }

  public void setPrinterIen(String printerIen) {
    this.printerIen = printerIen;
  }

  public String getPrinterIp() {
    return printerIp;
  }

  public void setPrinterIp(String printerIp) {
    this.printerIp = printerIp;
  }

  public String getPrinterName() {
    return printerName;
  }

  public void setPrinterName(String printerName) {
    this.printerName = printerName;
  }

  public boolean getPrintAllServiceDescriptions() {
    return printAllServiceDescriptions;
  }

  public void setPrintAllServiceDescriptions(boolean printServiceDescriptions) {
    this.printAllServiceDescriptions = printServiceDescriptions;
  }

  public String getCharts() {
    return this.charts != null ? this.charts : "";
  }

  public void setCharts(String charts) {
    this.charts = charts;
  }

  public String getSections() {
    return this.sections != null && !this.sections.isEmpty() ? this.sections : DEF_AVS_SECTIONS;
  }

  public void setSections(String sections) {
    this.sections = sections;
  }

  public String getChartFilenames() {
    return this.chartFilenames != null ? this.chartFilenames : "";
  }

  public void setChartFilenames(String chartFilenames) {
    this.chartFilenames = chartFilenames;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public boolean isInitialRequest() {
    return initialRequest;
  }

  public void setInitialRequest(boolean initialRequest) {
    this.initialRequest = initialRequest;
  }

  public String getCustomContent() {
    return this.customContent;
  }

  public void setCustomContent(String customContent) {
    this.customContent = customContent;
  }

  public String getSelectedServiceDescriptions() {
    return selectedServiceDescriptions;
  }

  public void setSelectedServiceDescriptions(String selectedServiceDescriptions) {
    this.selectedServiceDescriptions = selectedServiceDescriptions;
  }

  public String getRemoteVaMedicationsHtml() {
    return remoteVaMedicationsHtml;
  }

  public void setRemoteVaMedicationsHtml(String remoteVaMedicationsHtml) {
    this.remoteVaMedicationsHtml = remoteVaMedicationsHtml;
  }

  public String getRemoteNonVaMedicationsHtml() {
    return remoteNonVaMedicationsHtml;
  }

  public void setRemoteNonVaMedicationsHtml(String remoteNonVaMedicationsHtml) {
    this.remoteNonVaMedicationsHtml = remoteNonVaMedicationsHtml;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLang() {
    return this.language != null && !this.language.isEmpty() ? this.language : DEF_LANGUAGE;
  }

  public boolean isPrint() {
    return print;
  }

  public void setPrint(boolean print) {
    this.print = print;
  }

  public void setDocType(String docType) {
    this.docType = docType;
  }

  public String getFormat() {
    return this.format != null && !this.format.isEmpty() ? this.format : "html";
  }

  public void setFormat(String format) {
    this.format = format;
  }
  
  public String getDocType() {
    this.docType = (this.docType != null) && !this.docType.isEmpty() ? this.docType : EncounterInfo.AVS;
    return this.docType;
  }

}
