package gov.va.med.lom.javaUtils.html;

import java.util.Vector;
import java.util.Enumeration;

/*
 * This utility class is used to compose a row within an HTML table.
 */
public class HtmlTableRow {

  public static final int ALIGN_LEFT = 1;
  public static final int ALIGN_CENTER = 2;
  public static final int ALIGN_RIGHT = 3;
  public static final int VALIGN_TOP = 1; 
  public static final int VALIGN_MIDDLE = 2;
  public static final int VALIGN_BOTTOM = 3;

  private String caption = null;
  private String align = null;
  private String valign = null;
  private Vector row = new Vector();
  private int colSpan = 0;
  private int rowSpan = 0;
  private String bgColor = null;

  public HtmlTableRow() {
    // noop
  }

  public HtmlTableRow(HtmlTableCell td) {
    addCell(td);
  }

  public HtmlTableRow(String td) {
    addCell(td);
  }

  public void addCell(HtmlTableCell td) {
    row.addElement(td);
  }

  public void addCell(String td) {
    row.addElement(td);
  }

  public void setHorizontalAlignment(int alignment) {
    switch (alignment) {
    case ALIGN_CENTER:
      this.align = "CENTER";
      break;
    case ALIGN_RIGHT:
      this.align = "RIGHT";
      break;
    default:
      this.align = "LEFT";
      break;
    }
  }

  public void setVerticalAlignment(int alignment) {
    switch (alignment) {
    case VALIGN_TOP:
      this.valign = "TOP";
      break;
    case VALIGN_MIDDLE:
      this.valign = "MIDDLE";
      break;
    default:
      this.valign = "BOTTOM";
      break;
    }
  }

  public void setRowSpan(int rowSpan) {
    if (rowSpan > 0)
      this.rowSpan = rowSpan;
  }

  public void setColSpan(int colSpan) {
    if (colSpan > 0)
      this.colSpan = colSpan;
  }

  public void setBackgroundColor(String bgColor) {
    this.bgColor = bgColor;
  }

  public String toString() {
    StringBuffer html = new StringBuffer();

    html.append("<TR");
    if (align != null)
      html.append(" ALIGN=" + align);
    if (valign != null)
      html.append(" VALIGN=" + valign);
    if (colSpan > 0)
      html.append(" COLSPAN=" + colSpan);
    if (rowSpan > 0)
      html.append(" ROWSPAN=" + rowSpan);
    if (bgColor != null)
      html.append(" BGCOLOR=" + bgColor);
    html.append(">\n");

    Enumeration e = row.elements();
    while (e.hasMoreElements()) {
      html.append(e.nextElement().toString());
    }
    html.append("</TR>\n");

    return html.toString();
  }
}
