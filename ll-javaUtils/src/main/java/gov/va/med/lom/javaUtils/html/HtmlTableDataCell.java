package gov.va.med.lom.javaUtils.html;

/*
 * This utility class is used to compose an data cell within a row in an HTML table. 
 */
public class HtmlTableDataCell extends HtmlTableCell {

  public HtmlTableDataCell(String tdValue) {
	super(tdValue);
  }

  public HtmlTableDataCell(String tdValue, String defaultValue) {
	super(tdValue, defaultValue);
  }

  public String toString() {
    String html = "<TD";
    if (align != null)
      html += " ALIGN=" + align;
    if (valign != null)
      html += " VALIGN=" + valign;
    if (rowSpan > 0)
      html += " ROWSPAN=" + rowSpan;
    if (colSpan > 0)
      html += " COLSPAN=" + colSpan;
    if (bgColor != null)
      html += " BGCOLOR=" + bgColor;
    if (percentWidth > 0) 
      html += " WIDTH=" + percentWidth + "%";
    html += ">";
    if (font != null)
      html += font;
    html += tdValue;
    if (font != null)
      html += "</FONT>";
    html += "</TD>\n";
    return html;
  }
}
