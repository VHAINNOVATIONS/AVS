package gov.va.med.lom.javaUtils.html;

import java.util.Vector;
import java.util.Enumeration;

/*
 * This utility class is used to compose an HTML table.
 */
public class HtmlTable {

  private String caption = null;
  private int percentWidth = 100;
  private int percentHeight = 100;
  private int borderWidth = -1;
  private int cellPadding = -1;
  private int cellSpacing = -1;
  private String bgColor = null;
  private String rules = null;
  private String frame = null;
  private Vector rows = new Vector();
  private Object thead = null;

  public HtmlTable() {
    this.caption = null;
  }

  public HtmlTable(String caption) {
    this.caption = caption;
  }

  public void setBorderWidth(int pixels) {
    this.borderWidth = pixels;
  }

  public void addRow(HtmlTableRow row) {
    rows.addElement(row);
  }

  public void addRow(String row) {
    rows.addElement(row);
  }

  public void setHeader(Object row) {
    thead = row;
  }

  public void setPercentWidth(int percent) {
    if (percent > 0 && percent <= 100)
      this.percentWidth = percent;
  }

  public void setPercentHeight(int percent) {
    if (percent > 0 && percent <= 100)
      this.percentHeight = percent;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public void setCellSpacing(int size) {
    if (size >= 0)
      cellSpacing = size;
  }

  public void setCellPadding(int size) {
    if (size >= 0)
      cellPadding = size;
  }

  public void setBackgroundColor(String bgColor) {
    this.bgColor = bgColor;
  }

  public void setFrame(String frame) {
    this.frame = frame;
  }

  public void setRules(String rules) {
    this.rules = rules;
  }

  public String toString() {
    StringBuffer html = new StringBuffer();

    html.append("<TABLE");
    if (borderWidth >= 0) 
      html.append(" BORDER=" + borderWidth);
    if (percentWidth > 0) 
      html.append(" WIDTH=" + percentWidth + "%");
    if (cellPadding >= 0) 
      html.append(" CELLPADDING=" + cellPadding);
    if (cellSpacing >= 0) 
      html.append(" CELLSPACING=" + cellSpacing);
    if (bgColor != null)
      html.append(" BGCOLOR=" + bgColor);
    if (rules != null)
      html.append(" RULES=" + rules);
    if (frame != null)
      html.append(" FRAME=" + frame);
    html.append(">\n");

    if (caption != null) {
      html.append("<CAPTION>");
      html.append(caption);
      html.append("</CAPTION>\n");
    }

    if (thead != null) {
      html.append("<THEAD>");
      html.append(thead.toString());
      html.append("</THEAD>\n");
    }
    
    Enumeration e = rows.elements();
    while (e.hasMoreElements()) {
      html.append(e.nextElement().toString());
    }
    html.append("</TABLE>\n");

    return html.toString();
  }
}
