package gov.va.med.lom.avs.chart.action;

import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;

import gov.va.med.lom.foundation.util.Precondition;
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.avs.web.util.LabCache;
import gov.va.med.lom.avs.web.util.WebCacheUtil;
import gov.va.med.lom.charts.model.Apply;
import gov.va.med.lom.charts.model.Chart;
import gov.va.med.lom.charts.model.DataSet;
import gov.va.med.lom.charts.model.FusionChart;
import gov.va.med.lom.charts.model.Style;


import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LabChartsAction extends BaseAvsChartsAction {

	protected static final Log log = LogFactory.getLog(LabChartsAction.class);

	private static final String CHARTS_URL = "/avs/w/s/labs/%s.action?stationNo=%s&userDuz=%s&patientDfn=%s&datetime=%s";
  private static final String TPL_CHARTS_HTML = 
      "<html><head>\n" +
        "<script type=\"text/javascript\" src=\"/ext/4.2/ext-all.js\"></script>\n" + 
        "<script type=\"text/javascript\" src=\"/fusion/charts/FusionCharts.js\"></script>\n" + 
        "<script type=\"text/javascript\" src=\"/reportutils/js/FusionUtils.js\"></script>\n" +
        "<script type=\"text/javascript\" src=\"/avs/avs/js/charts.js\"></script>\n" +
        "<style type=\"text/css\">div.chart {padding-bottom: 10px;width:550px;height:390px;}</style>\n" +
        "</head><body>" +  
          "<div id=\"chart-contents\">\n" +
            "__CONTENTS__\n" +
          "</div>" +
      "</body></html>"; 
	
	private static final long serialVersionUID = 0;
	
	public String charts() {
	  
    Precondition.assertNotBlank("patientDfn", super.patientDfn);
    Precondition.assertNotBlank("facilityNo", super.facilityNo);
    
    StringBuffer body = new StringBuffer();
    
	  LabCache labCache = WebCacheUtil.getWebCacheUtil().getLabCache(super.facilityNo, super.userDuz, super.patientDfn);
	  if ((labCache != null) && (labCache.getData().size() > 0)) {
	    LinkedHashMap<String, List<DiscreteItemData>> data = labCache.getData();
      HashMap<String, String> charts = new HashMap<String, String>();
      Iterator<String> it = data.keySet().iterator();
      while (it.hasNext()) {
        String chartType = it.next();
        List<DiscreteItemData> chartData = data.get(chartType);
        if ((chartData != null) && !chartData.isEmpty()) {
          String chartAction = null;
          if (chartType.equals("BP")) {
            chartAction = "bpChart";
          } else if (chartType.equals("PULSE")) {
            chartAction = "pulseChart";
          } else if (chartType.equals("WGT")) {
            chartAction = "weightChart";
          } else if (chartType.equals("BMI")) {
            chartAction = "bmiChart";
          } else if (chartType.equals("CHOL")) {
            chartAction = "totalCholesterolChart";
          } else if (chartType.equals("LDL")) {
            chartAction = "ldlChart";
          } else if (chartType.equals("HDL")) {
            chartAction = "hdlChart";
          } else if (chartType.equals("TRG")) {
            chartAction = "triglyceridesChart";
          } else if (chartType.equals("HBA1C")) {
            chartAction = "hgbA1cChart";
          } else if (chartType.equals("CRT")) {
            chartAction = "creatinineChart";
          } else if (chartType.equals("EGFR")) {
            chartAction = "egfrChart";
          } else if (chartType.equals("HGB")) {
            chartAction = "hgbChart";
          } else if (chartType.equals("PLT")) {
            chartAction = "plateletsChart";
          } else if (chartType.equals("PSA")) {
            chartAction = "psaChart";
          } 
          if (chartAction != null) {
            String chartId = chartType + "-" + StringUtils.getRandomString(4, 5);
            charts.put(chartId, chartAction);
          }
        }
      }
      
      Set<String> keys = charts.keySet();
      it = keys.iterator();
      while (it.hasNext()) {
        String chartId = it.next(); 
        body.append("<div id=\"" + chartId + "\" class=\"chart\"></div>");
      }
      body.append("<script type=\"text/javascript\">\n");
      keys = charts.keySet();
      it = keys.iterator();
      while (it.hasNext()) {
        String chartId = it.next();
        String chartAction = charts.get(chartId);
        String chartUrl = String.format(CHARTS_URL, chartAction, super.facilityNo, super.userDuz, 
            super.patientDfn, String.valueOf(super.datetimes));
        body.append("getChartData('" + chartId + "', '" + chartUrl + "', '" + chartId + "', fsCallback);\n");
      }
      body.append("</script>");
	  } else {
	    body.append("None");
	  }
	  return setHtml(TPL_CHARTS_HTML.replace("__CONTENTS__", body.toString()));
	}
	
  public String pulseChart() {
    return doLineChart("PULSE", "Pulse Rate", "Date", "Beats Per Minute");
  }	
	
  public String weightChart() {
    return doLineChart("WGT", "Weight", "Date", "lbs");
  } 
  
  public String bmiChart() {
    return doLineChart("BMI", "Body Mass Index (BMI)", "Date", "");
  } 
  
  public String totalCholesterolChart() {
    return doLineChart("CHOL", "Total Cholesterol", "Date", "mg/dL");
  } 
  
  public String ldlChart() {
    return doLineChart("LDL", "LDL Cholesterol", "Date", "mg/dL");
  } 
  
  public String hdlChart() {
    return doLineChart("HDL", "HDL Cholesterol", "Date", "mg/dL");
  } 
  
  public String triglyceridesChart() {
    return doLineChart("TRG", "Triglycerides", "Date", "mg/dL");
  }
  
  public String hgbA1cChart() {
    return doLineChart("HBA1C", "Hemoglobin A1c", "Date", "%");
  } 
  
  public String creatinineChart() {
    return doLineChart("CRT", "Creatinine", "Date", "mg/dL");
  } 
  
  public String egfrChart() {
    return doLineChart("EGFR", "Estimated Glomerular Filtration Rate (eGFR)", "Date", "");
  } 
  
  public String hgbChart() {
    return doLineChart("HGB", "Hemoglobin", "Date", "g/dL");
  }  
  
  public String psaChart() {
    return doLineChart("PSA", "PSA", "Date", "ng/mL");
  }    
  
  public String plateletsChart() {
    return doLineChart("PLT", "Platelets", "Date", "x 10(3)/uL");
  } 
  
  public String bpChart() {
    
    LabCache labCache = WebCacheUtil.getWebCacheUtil().getLabCache(super.facilityNo, super.userDuz, super.patientDfn);
    if ((labCache == null) || (!labCache.keyExists("BP"))) {
      return SUCCESS;
    }
    
    List<DiscreteItemData> data = labCache.getCachedData("BP");
    if (!WebCacheUtil.getWebCacheUtil().labCacheContainsValues(super.facilityNo, super.userDuz, super.patientDfn)) {
      WebCacheUtil.getWebCacheUtil().removeLabCache(super.facilityNo, super.userDuz, super.patientDfn);
    }
    LinkedHashMap<Double, String> values = discreteDataToLinkedHashmap(data);
    
    // set meta data
    setType(FusionChart.MS_LINE);
    setWidth("100%");
    setHeight("100%"); 
    
    // set chart properties
    setCaption("Blood Pressure");
    setBgColor("F7F7F7, E9E9E9");
    setShowValues(1);
    setLabelDisplay("ROTATE");
    setXAxisName("Date");
    setYAxisName("mmHg");
    
    // set export properties
    setExportEnabled(1);
    setExportHandler("/avs/w/s/FCExporter");
    setExportAtClient(0);
    setExportAction(Chart.SAVE);
    setExportFormat(Chart.JPG);
    
    // get chart object to set properties not wrapped by BaseChartsAction
    Chart chart = getChart();
    chart.setBgAlpha(100);
    chart.setDivLineAlpha(30);
    chart.setNumDivLines(10);
    chart.setSlantLabels(1);
    chart.setValuePadding(2);
    chart.setCanvasPadding(20);

    List<gov.va.med.lom.charts.model.Set> sysBpData = new ArrayList<gov.va.med.lom.charts.model.Set>();
    List<gov.va.med.lom.charts.model.Set> diaBpData = new ArrayList<gov.va.med.lom.charts.model.Set>();
    
    DataSet sysBpDataSet = addDataSet("Systolic", sysBpData);
    sysBpDataSet.setColor("A66EDD");
    sysBpDataSet.setShowValues(1);
    DataSet diaBpDataSet = addDataSet("Diastolic", diaBpData);
    diaBpDataSet.setShowValues(1);
    diaBpDataSet.setColor("F6BD0F");

    String firstDate = null;
    String lastDate = null;
    
    // set chart data
    Iterator<Double> it = values.keySet().iterator();
    while (it.hasNext()) {
      try {
        Double d = it.next();
        String dt = DateUtils.toEnglishDate(DateUtils.fmDateTimeToDateTime(d).getTime());
        if (firstDate == null) {
          firstDate = dt;
        }
        lastDate = dt;
        double sysVal = Double.valueOf(StringUtils.piece(values.get(d), '/', 1));
        double diaVal = Double.valueOf(StringUtils.piece(values.get(d), '/', 2));
        
        // add date as category
        addCategory(dt);
        
        // add systolic and diastolic chart data
        sysBpData.add(new gov.va.med.lom.charts.model.Set(sysVal));
        diaBpData.add(new gov.va.med.lom.charts.model.Set(diaVal));

      } catch(Exception e) {
        e.printStackTrace();
      }
    }

    setSubCaption(getMonthYear(firstDate) + " to " + getMonthYear(lastDate));
   
    addStyles();
    
    return sendJson();
  }
  
  private String doLineChart(String chartType, String caption, String xAxisName, String yAxisName) {
    
    LabCache labCache = WebCacheUtil.getWebCacheUtil().getLabCache(super.facilityNo, super.userDuz, super.patientDfn);
    if ((labCache == null) || (!labCache.keyExists(chartType))) {
      return SUCCESS;
    }
    
    List<DiscreteItemData> data = labCache.getCachedData(chartType);
    if (!WebCacheUtil.getWebCacheUtil().labCacheContainsValues(super.facilityNo, super.userDuz, super.patientDfn)) {
      WebCacheUtil.getWebCacheUtil().removeLabCache(super.facilityNo, super.userDuz, super.patientDfn);
    }
    LinkedHashMap<Double, String> values = discreteDataToLinkedHashmap(data);
        
    // set meta data
    setType(FusionChart.LINE);
    setWidth("100%");
    setHeight("100%"); 
    
    // set chart properties
    setCaption(caption);
    setBgColor("F7F7F7, E9E9E9");
    setShowValues(1);
    setLabelDisplay("ROTATE");
    setXAxisName(xAxisName);
    setYAxisName(yAxisName);
    
    // set export properties
    setExportEnabled(1);
    setExportHandler("/avs/w/s/FCExporter");
    setExportAtClient(0);
    setExportAction(Chart.SAVE);
    setExportFormat(Chart.JPG);
    
    // get chart object to set properties not wrapped by BaseChartsAction
    Chart chart = getChart();
    chart.setBgAlpha(100);
    chart.setDivLineAlpha(30);
    chart.setNumDivLines(10);
    chart.setSlantLabels(1);
    chart.setValuePadding(2);
    chart.setCanvasPadding(20);
    
    String firstDate = null;
    String lastDate = null;
    
    // set chart data
    Iterator<Double> it = values.keySet().iterator();
    while (it.hasNext()) {
      try {
        Double d = it.next();
        String dt = DateUtils.toEnglishDate(DateUtils.fmDateTimeToDateTime(d).getTime());
        if (firstDate == null) {
          firstDate = dt;
        }
        lastDate = dt;
        addData(dt, Double.valueOf(values.get(d)));
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

    setSubCaption(getMonthYear(firstDate) + " to " + getMonthYear(lastDate));      
    
    // add styles
    addStyles();
    
    return sendJson();
  } 
  
  private void addStyles() {
    // add styles
    Style style = new Style();
    style.setName("LineShadow");
    style.setType(Style.SHADOW);
    style.setColor("333333");
    style.setDistance(5);
    addStyle(style);
    
    /*
    style = new Style();
    style.setName("XScaleAnim");
    style.setType(Style.ANIMATION);
    style.setDuration(1);
    style.setStart(0);
    style.setParam("_xScale");
    addStyle(style);
    
    style = new Style();
    style.setName("YScaleAnim");
    style.setType(Style.ANIMATION);
    style.setDuration(1);
    style.setStart(0);
    style.setParam("_yscale");
    addStyle(style);
    
    style = new Style();
    style.setName("XAnim");
    style.setType(Style.ANIMATION);
    style.setDuration(1);
    style.setStart(0);
    style.setParam("_yscale");
    addStyle(style);
    
    style = new Style();
    style.setName("AlphaAnim");
    style.setType(Style.ANIMATION);
    style.setDuration(1);
    style.setStart(0);
    style.setParam("_alpha");
    addStyle(style);
    */
    // set style applications
    addApplication(Apply.DATAPLOT, "LineShadow");
    //addApplication(Apply.CANVAS, "XScaleAnim, YScaleAnim,AlphaAnim");
    //addApplication(Apply.DIVLINES, "XScaleAnim,AlphaAnim");
    //addApplication("VDIVLINES", "YScaleAnim,AlphaAnim");
    //addApplication(Apply.HGRID, "YScaleAnim,AlphaAnim");
  }
  
  private static LinkedHashMap<Double, String> discreteDataToLinkedHashmap(List<DiscreteItemData> data) {
    LinkedHashMap<Double, String> values = new LinkedHashMap<Double, String>();
    for (DiscreteItemData point : data) {
      values.put(point.getFmDate(), point.getValue());
    }
    return values;
  }
  
  private static String getMonthYear(String dt) {
    String mo = StringUtils.piece(dt, '/', 1);
    String yr = StringUtils.piece(dt, '/', 3);
    int i = Integer.valueOf(mo);
    switch (i) {
      case 1 : return "Jan, " + yr;
      case 2 : return "Feb, " + yr;
      case 3 : return "Mar, " + yr;
      case 4 : return "Apr, " + yr;
      case 5 : return "May, " + yr;
      case 6 : return "Jun, " + yr;
      case 7 : return "Jul, " + yr;
      case 8 : return "Aug, " + yr;
      case 9 : return "Sep, " + yr;
      case 10 : return "Oct, " + yr;
      case 11 : return "Nov, " + yr;
      case 12 : return "Dec, " + yr;
    }
    return "";
  }
		
}
