package gov.va.med.lom.javaUtils.html;

/*
 * This base class is extended to compose a particular type of cell
 * within a row in an HTML table. 
 */
public abstract class HtmlTableCell {

  public static final int ALIGN_LEFT = 1;
  public static final int ALIGN_CENTER = 2;
  public static final int ALIGN_RIGHT = 3;
  public static final int VALIGN_TOP = 1; 
  public static final int VALIGN_MIDDLE = 2;
  public static final int VALIGN_BOTTOM = 3;

  protected String caption;
  protected int rowSpan = 0;
  protected int colSpan = 0;
  protected String tdValue = null;
  protected String align = null;
  protected String valign = null;
  protected String bgColor = null;
  protected int percentWidth = 0;
  protected String font = null;

  public HtmlTableCell(String tdValue) {
    this.tdValue = tdValue;
  }

  public HtmlTableCell(String tdValue, String defaultValue) {
	if (tdValue.equals("")) {
	  this.tdValue = defaultValue;
	} else {
	  this.tdValue = tdValue;
	}
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
  
  public void setValue(String tdValue) {
    this.tdValue = tdValue;
  }

  public void setBackgroundColor(String bgColor) {
    this.bgColor = bgColor;
  }

  public void setPercentWidth(int percent) {
    if (percent > 0 && percent <= 100)
      this.percentWidth = percent;
  }

  public void setFont(String face, String color, int size) {
    font = "<FONT face=\"" + face + "\" color=\"" + color + "\" size=\"" + size + "\">";
  }

}
