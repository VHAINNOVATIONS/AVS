package gov.va.med.lom.avs.ejb.reports;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import gov.va.med.lom.reports.ejb.filters.IReportFilters;
import gov.va.med.lom.reports.ejb.model.Filter;

import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SheetService;

@Stateless(name="AvsReportFiltersEjb")
@Remote(IReportFilters.class)

public class AvsReportFiltersEjb implements Serializable {
  
  public List<Filter> getReportFilters(String name) {
    
    //SheetService sheetService = ServiceFactory.getSheetService();
    
    List<Filter> filters = new ArrayList<Filter>();
    
    if (name.equals("avsDateRanges")) {
      Filter filter = new Filter();
      filter.setId(name + "_1");
      filter.setText("Today");
      filters.add(filter);
      filter = new Filter();
      filter.setId(name + "_2");
      filter.setText("Last 24 Hours");
      filters.add(filter);      
      filter = new Filter();
      filter.setId(name + "_3");
      filter.setText("Last 2 Days");
      filters.add(filter);
      filter = new Filter();
      filter.setId(name + "_4");
      filter.setText("Last 3 Days");
      filters.add(filter);  
      filter = new Filter();
      filter.setId(name + "_5");
      filter.setText("Last 7 Days");
      filters.add(filter);       
      filter = new Filter();
      filter.setId(name + "_6");
      filter.setText("Last 14 Days");
      filters.add(filter);       
      filter = new Filter();
      filter.setId(name + "_7");
      filter.setText("Last 30 Days");      
      filters.add(filter);         
      
    } else if (name.equals("avsStations")) {
      Filter filter = new Filter();
      filter.setId(name + "_1");
      filter.setText("Loma Linda HCS (605)");
      filters.add(filter);
    }
    
    return filters;
  }
  
}
