package gov.va.med.lom.javaUtils.html;

import java.util.*;
import java.text.DateFormat;
import gov.va.med.lom.javaUtils.misc.DateUtils;

public class HTMLContent {
  private int numRows;
  private int numCols;
  private StringBuffer htmlContent;
  private String date;

  public String title;
  public String patientName;
  public String patientID;
  public String bgColor;
  public String tableBGColor;
  public String cellSpacing;
  public String cellPadding;
  public String headerColor;
  public String font;
  public String textColor;
  public String headerFontSize;
  public String fontSize;
  public String rowColor1;
  public String rowColor2;
  public String[] rowColors;
  public String[] colSizes;
  public String[] colHeaders;
  public String[][] data;

  // constructor
  public HTMLContent(String title, int numCols, int numRows ) {
    this.numCols = numCols;
    this.numRows = numRows;
    this.title = title;
    bgColor = "#FFFFFF";
    tableBGColor = "#6D6C6C";
    cellSpacing = "1";
    cellPadding = "1";
    headerColor = "#CCCCCC";
    font = "ms sans serif";
    textColor = "#000000";
    headerFontSize = "1";
    fontSize = "2";
    rowColor1 = "#FFFFFF";
    rowColor2 = "#E8E8E8";
    rowColors = new String[numRows];
    colSizes = new String[numCols];
    colHeaders = new String[numCols];
    data = new String[numCols][numRows];
    htmlContent = new StringBuffer();
    date = DateFormat.getDateInstance().format(new Date());
    for (int i = 0; i < rowColors.length;i++)
      rowColors[i] = "";
  }

  // private methods
  private String getCol(String width, String bgColor, String  font,
                        String fontColor, String fontSize, String value) {
    return  "<TD width=\"" + width + "\" bgcolor=\"" + bgColor + "\">" + "<FONT face=\"" + font +
            "\" color = \"" + fontColor + "\" size=\"" + fontSize + "\">" + value + "</FONT></TD>";
  }

  // public methods
  public void createTable() {
    boolean evenRow = false;
    String currentColor;
    htmlContent.append("<TABLE width=\"100%\" border=\"0\" cellspacing=\"" + cellSpacing + "\" cellpadding=\"" +
                       cellPadding + "\" bgcolor=\"" + tableBGColor + "\">");
    htmlContent.append("<TR><!-- Header Row -->");
    for(int i=0;i < numCols;i++) {
      htmlContent.append(getCol(colSizes[i], headerColor, font, textColor,
                                headerFontSize, "<B>" + colHeaders[i] + "</B>"));
    }
    htmlContent.append("</TR>");
    currentColor = rowColor1;
    for(int i=0;i < numRows;i++) {
      htmlContent.append("<TR><!-- Data Row -->");
      for(int j=0;j < numCols;j++) {
        String color;
        if (rowColors[i].equals(""))
          color = currentColor;
        else
          color = rowColors[i];
        htmlContent.append(getCol(colSizes[j], color, font, textColor, fontSize, data[j][i]));
      }
      if(currentColor.compareTo(rowColor1) == 0)
        currentColor = rowColor2;
      else
        currentColor = rowColor1;
      htmlContent.append("</TR>");
    }
    htmlContent.append("</TABLE>");
  }

  public String getHTMLContent(String title, int numRecords, String additionalContent) {
    StringBuffer content = new StringBuffer();
    content.append("<HTML><HEAD><TITLE>" + title + "</TITLE></HEAD><BODY bgcolor=\"#FFFFFF\" ");
    content.append("style=\"font-family:ms sans serif;border:0px;margin:0px\">");
    content.append("<STYLE> SPAN {BORDER-RIGHT: #CCCCCC 1px solid;");
    content.append("PADDING-RIGHT: 3px;BORDER-TOP: #CCCCCC 1px solid;");
    content.append("DISPLAY: inline;PADDING-LEFT: 3px;PADDING-BOTTOM: 2px;");
    content.append("BORDER-LEFT: #CCCCCC 1px solid;PADDING-TOP: 2px;");
    content.append("BORDER-BOTTOM: #CCCCCC 1px solid;POSITION: relative;");
    content.append("TOP: 1px;HEIGHT: 17px }");
    content.append("A.MouseOver {COLOR: #0000FF} A.MouseOut {COLOR: #000000}");
    content.append("A {COLOR: #000000;TEXT-DECORATION: none }</STYLE><SPAN>");    
    content.append("<TABLE width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\">");
    content.append("<TR><TD align=\"left\"><FONT face=\"ms sans serif\" size=\"4\" color=\"#000000\"><B>" + title + "</B></FONT></TD>");
    content.append("<TD align=\"right\"><FONT face=\"ms sans serif\" size=\"2\" color=\"#000000\">Date@Time: " + DateUtils.getEnglishDateTime() + 
                   "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Records: " + numRecords + "</FONT></TD></TR>");
    content.append("</TABLE>");
    if (!additionalContent.equals("")) {
      content.append(additionalContent);
      content.append("<BR>");
    }
    content.append(getContent());
    content.append("</SPAN></BODY></HTML>");
    return content.toString();
  }

  public void setColName(int index, String name) {
    colHeaders[index] = name;
  }

  public void setColNames(String[] names) {
    for(int i=0;i < names.length;i++)
      colHeaders[i] = names[i];
  }

  public void setColSize(int index, String size) {
    colSizes[index] = size;
  }

  public void setColSizes(String[] sizes) {
    for(int i=0;i < sizes.length;i++)
      colSizes[i] = sizes[i];
  }

  public void setCellData(int colIndex, int rowIndex, String data) {
    this.data[colIndex][rowIndex] = data;
  }

  public void setCellDataAnchor(int colIndex, int rowIndex, 
                                String data, String anchorValue) {
    this.data[colIndex][rowIndex] = "<A onmouseover = \"this.className=\'MouseOver\'\" " + 
                                       "onmouseout = \"this.className=\'MouseOut\'\" " + 
                                       "href=\"" + anchorValue + "\">" + data +"</A>";
  }

  public void setData(String[][] data) {
    for(int i=0;i < data.length;i++) {
      for(int j=0;j < data[i].length;j++) {
        this.data[i][j] = data[i][j];
      }
    }
  }

  public void setData(Vector[] data) {
    for(int i=0;i < data.length;i++) {
      for(int j=0;j < data[i].size();j++) {
        this.data[i][j] = (String)data[i].elementAt(j);
      }
    }
  }

  public void reset() {
    htmlContent.replace(0,htmlContent.length(),"");
    colSizes = null;
    colHeaders = null;
  }

  public void clear() {
     reset();
  }

  public String getContent() {
    return htmlContent.toString();
  }

  public void setBGColor(String bgColor) {
    this.bgColor = bgColor;
  }

  public void setTableBGColor(String tableBGColor) {
    this.tableBGColor = tableBGColor;
  }

  public void setCellSpacing(String cellSpacing) {
    this.cellSpacing = cellSpacing;
  }

  public void setCellPadding(String cellPadding) {
    this.cellPadding = cellPadding;
  }

  public void setHeaderColor(String headerColor) {
    this.headerColor = headerColor;
  }

  public void setFont(String font) {
    this.font = font;
  }

  public void setTextColor(String textColor) {
    this.textColor = textColor;
  }

  public void setFontSize(String fontSize) {
    this.fontSize = fontSize;
  }

  public void setRowColor(int rowIndex, String rowColor) {
    rowColors[rowIndex] = rowColor;
  }

  public void setRowColor1(String rowColor1) {
    this.rowColor1 = rowColor1;
  }

  public void setRowColor2(String rowColor2) {
    this.rowColor2 = rowColor2;
  }
}






