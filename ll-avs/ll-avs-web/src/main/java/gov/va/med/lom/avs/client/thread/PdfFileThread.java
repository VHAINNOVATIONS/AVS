package gov.va.med.lom.avs.client.thread;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.va.med.lom.avs.model.EncounterCacheMongo;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SheetService;
import gov.va.med.lom.avs.web.util.AvsWebUtils;

public class PdfFileThread extends Thread {

  private SheetService sheetService;
  private String avsBody;
  private String additionalInformationSheetBody;
  private String stationNo;
  private String patientDfn;
  private List<String> locationIens;
  private List<Double> datetimes;
  private String filepath;
  private String cssContents;
  private String imgPath;
  private String docType;
  
  private static final Log log = LogFactory.getLog(PdfFileThread.class);
  
  public PdfFileThread() {}
    
  public PdfFileThread(String avsBody, String additionalInformationSheetBody, String stationNo, String patientDfn,
      List<String> locationIens, List<Double> datetimes, String filepath, String cssContents, String imgPath, String docType) {
    this.avsBody = avsBody;
    this.additionalInformationSheetBody = additionalInformationSheetBody;
    this.stationNo = stationNo;
    this.patientDfn = patientDfn;
    this.locationIens = locationIens;
    this.datetimes = datetimes;
    this.filepath = filepath;
    this.cssContents = cssContents;
    this.imgPath = imgPath;
    this.docType = docType;
  }
  
  public void run() {
    try {
      this.sheetService = ServiceFactory.getSheetService();
      ByteArrayOutputStream outputStream = AvsWebUtils.createPdfWithAddlInfo(cssContents, imgPath,
          this.avsBody, this.additionalInformationSheetBody);
      String filename = AvsWebUtils.savePdfToFile(this.stationNo, this.patientDfn, this.locationIens, 
          this.datetimes, this.filepath, outputStream);
      this.savePdfFilename(filename.toString());
    } catch(Exception e) {
      log.error("Error auto-generating PDF", e);
    }
  }
  
  private void savePdfFilename(String pdfFilename) {
    EncounterCacheMongo encounterCache = 
        this.sheetService.getEncounterCacheMongo(this.stationNo, this.patientDfn, 
            this.locationIens, this.datetimes, this.docType).getPayload();
    if (encounterCache != null) {
      encounterCache.setPdfFilename(pdfFilename);
      try {
        this.sheetService.updateEncounterCacheMongoPdfFilename(encounterCache);
      } catch(Exception e) {
        log.error("Error saving PDF filename in encounter cache", e);
      }
    }
  }

  public String getAvsBody() {
    return avsBody;
  }

  public void setAvsBody(String avsBody) {
    this.avsBody = avsBody;
  }

  public String getAdditionalInformationSheetBody() {
    return additionalInformationSheetBody;
  }

  public void setAdditionalInformationSheetBody(
      String additionalInformationSheetBody) {
    this.additionalInformationSheetBody = additionalInformationSheetBody;
  }

  public String getStationNo() {
    return stationNo;
  }

  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }

  public String getPatientDfn() {
    return patientDfn;
  }

  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }

  public String getFilepath() {
    return filepath;
  }

  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }

  public String getCssContents() {
    return cssContents;
  }

  public void setCssContents(String cssContents) {
    this.cssContents = cssContents;
  }

  public String getImgPath() {
    return imgPath;
  }

  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }   
  
}
