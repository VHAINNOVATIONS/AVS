package gov.va.med.lom.avs.report.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;

import gov.va.med.lom.reports.model.GridData;
import gov.va.med.lom.reports.pdf.AbstractPdfReport;

import gov.va.med.lom.avs.report.model.ServiceJson;

public class ClinicalServiceDescriptionsPdfReport extends AbstractPdfReport {
  
  static {
    FONT_CELL = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.NORMAL);
    FONT_CELL_HEADER = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
    FONT_PAGE_HEADER = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
  }
  
  public ClinicalServiceDescriptionsPdfReport(String station, GridData report) {
    super("", station, report, new int[] { 25, 35, 20, 20 }, PageSize.LETTER);
  }

  public Class getRootClass() {
    return ServiceJson.class;
  }
  
  public String columnValue(int colIndex, Object obj) {
    ServiceJson serviceJson = (ServiceJson)obj;
    
    switch(colIndex) {
      case 0 : return serviceJson.getName();
      case 1 : return serviceJson.getLocation();
      case 2 : return serviceJson.getHours();
      case 3 : return serviceJson.getComment();
    }
    return "";
  }
  
  protected PdfPTable metaHeader() {
    try {
      PdfPTable datatable = new PdfPTable(1);
      datatable.setWidths(new int[] { 20 });
      datatable.setWidthPercentage(100);
      return datatable;
    } catch (DocumentException e) {
      e.printStackTrace();
    }
    return null;
  }
  
}
