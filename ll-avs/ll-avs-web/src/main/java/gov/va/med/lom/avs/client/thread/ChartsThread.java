package gov.va.med.lom.avs.client.thread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import gov.va.med.lom.avs.web.util.LabCache;
import gov.va.med.lom.avs.web.util.WebCacheUtil;
import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;

public class ChartsThread extends SheetDataThread {

  static final String CHARTS_PATH = "/avs/w/s/labs/charts.action";
  
  private String charts;
  private String chartFilenames;
  
  public ChartsThread(String charts, String chartFilenames) {
    super();
    this.charts = charts;
    this.chartFilenames = chartFilenames;
  }
  
  public void run() {
    
    double fmdatetime = super.getLatestEncounter().getEncounterDatetime();
    
    String sectionClass = "section-hidden";
    StringBuffer body = new StringBuffer();
    LinkedHashMap<String, List<DiscreteItemData>> data = null;
    try {
      ServiceResponse<LinkedHashMap<String, List<DiscreteItemData>>> response = 
          super.getLabValuesService().getRecentLabValues(securityContext, super.patientDfn, fmdatetime);
      LinkedHashMap<String, List<DiscreteItemData>> chartsData = response.getPayload();
      
      if (super.media == MEDIA_PDF) {
        if ((this.chartFilenames != null) && (this.chartFilenames.length() > 0)) {
          String[] chartFilenamesArr = StringUtils.pieceList(this.chartFilenames, ',');
          if (chartFilenamesArr.length > 0) {
            sectionClass = "section";
            for (int i = 0; i < chartFilenamesArr.length; i++) {
              body.append("<div class=\"chart\"><img src='")
              .append(chartFilenamesArr[i])
              .append("' /></div>\n");
            }
          }
        }
        
      } else {
      
        HashMap<String, String> chartsMap = new HashMap<String, String>();
        if ((this.charts != null) || (this.charts == "")) {
          String[] chartsArr = StringUtils.pieceList(this.charts, ',');
          for (int i = 0; i < chartsArr.length; i++) {
            chartsMap.put(chartsArr[i], chartsArr[i]);
          }
        }
        
        if ((chartsMap.size() == 0) || (chartsMap.containsKey("NONE"))) {
          return;
        }
        
        data = new LinkedHashMap<String, List<DiscreteItemData>>();
        if (chartsData.size() > 0) {
          sectionClass = "section";
          Iterator<String> it = chartsData.keySet().iterator();
          while (it.hasNext()) {
            String chartType = it.next();
            List<DiscreteItemData> chartData = chartsData.get(chartType);
            if ((chartData != null) && (chartData.size() > 1) && 
                (chartsMap.containsKey(chartType) || chartsMap.containsKey("ALL"))) {
              data.put(chartType, chartData);
            }
          }
          int height = data.size() * 400;
          LabCache labCache = new LabCache(data);
          WebCacheUtil.getWebCacheUtil().cacheLabData(super.stationNo, super.userDuz, super.patientDfn, labCache);
          String src = String.format("%s?stationNo=%s&userDuz=%s&patientDfn=%s&datetime=%s", CHARTS_PATH,
              super.stationNo, super.userDuz, super.patientDfn, String.valueOf(fmdatetime));
          body.append("<iframe src='" + src + "' id='chartsFrame' width='100%' height='" + height + "' frameborder='0' scrolling='no'></iframe>");
        }        
      }
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", sectionClass)
        .replace("__SECTION_ID_SUFFIX__", "charts")
        .replace("__SECTION_TITLE__", super.getStringResource("clinicalCharts"))
        .replace("__CONTENTS__", body.toString());
      if (data != null) {
        super.setContentData("clinicalCharts", content, data);
      } else {
        super.setContent("clinicalCharts", content);
      }
    }
    
  }
  
}
