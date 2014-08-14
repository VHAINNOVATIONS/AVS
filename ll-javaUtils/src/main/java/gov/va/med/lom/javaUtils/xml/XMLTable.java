package gov.va.med.lom.javaUtils.xml;

import gov.va.med.lom.javaUtils.misc.StringUtils;

public class XMLTable {

  private StringBuffer records;
  private boolean withProlog = false;
  private String title = null;
  private String subtitle = null;
  private String datetime = null;
  private String additionalContent = null;
  private String clinicName;
  private String userName;
  private int numRecords = -1;
  private String[] fieldNames = null;
  private String[] fieldWidths = null;
  private String[] fieldAlignments = null;
  private String[] fieldHeaders = null;
  private String[] fieldTargets = null;
  private long targetEntityID = 0;

  public XMLTable(boolean withProlog) {
    this.withProlog = withProlog;
    reset();
  }

  public void reset() {
    title = null;
    subtitle = null;
    datetime = null;
    additionalContent = null;
    clinicName = null;
    userName = null;
    numRecords = -1;
    fieldNames = null;
    fieldWidths = null;
    fieldAlignments = null;
    fieldHeaders = null;
    fieldTargets = null;
    targetEntityID = 0;
    records = new StringBuffer();
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public void setAdditionalContent(String additionalContent) {
    this.additionalContent = additionalContent;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setClinicName(String clinicName) {
    this.clinicName = clinicName;
  }

  public void setNumRecords(int numRecords) {
    this.numRecords = numRecords;
  }

  public void setFieldNames(String[] fieldNames) {
    this.fieldNames = fieldNames;
  }

  public void setFieldWidths(String[] fieldWidths) {
    this.fieldWidths = fieldWidths;
  }

  public void setFieldAlignments(String[] fieldAlignments) {
    this.fieldAlignments = fieldAlignments;
  }

  public void setFieldHeaders(String[] fieldHeaders) {
    this.fieldHeaders = fieldHeaders;
  }

  public void setFieldTargets(long targetEntityID, String[] fieldTargets) {
    this.targetEntityID = targetEntityID;
    this.fieldTargets = fieldTargets;
  }

  public void newRecord(String[] fieldValues, String rowColor) {
    records.append("<record color=\"" + rowColor + "\">");
    for (int col = 0; col < fieldValues.length; col++) {     
      records.append("<field ");

      if (fieldNames != null)
        records.append("name=\"" + fieldNames[col] + "\" ");

      if ((fieldTargets != null) && (fieldTargets[col].length() > 0))
        records.append("target=\"" + targetEntityID + ";" + fieldTargets[col] + "\" ");

      if ((fieldAlignments != null) && (fieldAlignments[col].length() > 0))
        records.append("align=\"" + fieldAlignments[col] + "\" ");

      records.append(">");
      records.append(StringUtils.escapeEntities(fieldValues[col]));
      records.append("</field>");
    }
    records.append("</record>");
  }
  
  public void newRecord(String[] fieldValues) {
    newRecord(fieldValues, "");
  }

  public String toString() {
    StringBuffer xml = new StringBuffer(); 
    if (withProlog)
      xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    xml.append("<data>");
    if (title != null)
      xml.append("<title>" + title + "</title>");
    else
      xml.append("<title />");
    if (subtitle != null)
      xml.append("<subtitle>" + subtitle + "</subtitle>");
    else
      xml.append("<subtitle />");
    if (clinicName != null)
      xml.append("<clinic_name>" + clinicName + "</clinic_name>");
    else
      xml.append("<clinic_name />");
    if (userName != null)
      xml.append("<user_name>" + userName + "</user_name>");
    else
      xml.append("<user_name />");
    if (datetime != null)
      xml.append("<datetime>" + datetime + "</datetime>");
    else
      xml.append("<datetime />");
    if (numRecords >= 0)
      xml.append("<num_records>" + numRecords + "</num_records>");
    else
      xml.append("<num_records />");
    if (additionalContent != null)
      xml.append("<additional_content><![CDATA[" + additionalContent + "]]></additional_content>");
    else
      xml.append("<additional_content />");
    if (fieldHeaders != null) {
      xml.append("<header>");
      for(int i = 0; i < fieldHeaders.length; i++) {
        xml.append("<col");
        if (fieldWidths != null)
          xml.append(" width=\"" + fieldWidths[i] + "\"");
        if (fieldAlignments != null)
          xml.append(" align=\"" + fieldAlignments[i] + "\"");
        xml.append(">" + fieldHeaders[i] + "</col>");
      } 
      xml.append("</header>");
    }
    xml.append("<records>");
    xml.append(records.toString());  
    xml.append("</records>");
    xml.append("</data>");
    return xml.toString();
  }
}