package gov.va.med.lom.javaUtils.html;

/*
 * This utility class is used to compose a header cell within a row within a HTML table. 
 */
public class HtmlTableHeaderCell extends HtmlTableCell {

  public HtmlTableHeaderCell(String tdValue) {
    super(tdValue);
  }

  public HtmlTableHeaderCell(String tdValue, String defaultValue) {
    super(tdValue, defaultValue);
  }

  public String toString() {
    String html = "<TH";
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
	  html += tdValue;
    html += "</TH>\n";
    return html;
  }
}
