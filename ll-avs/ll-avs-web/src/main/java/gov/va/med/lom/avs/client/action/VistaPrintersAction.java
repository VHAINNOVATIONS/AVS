package gov.va.med.lom.avs.client.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.google.gson.reflect.TypeToken;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.javaUtils.misc.QSCallBack;
import gov.va.med.lom.javaUtils.misc.QuickSort;
import gov.va.med.lom.json.util.JsonResponse;

import gov.va.med.lom.avs.client.model.VistaPrinterJson;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SettingsService;
import gov.va.med.lom.avs.util.FilterProperty;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.model.VistaPrinter;

public class VistaPrintersAction extends BaseCardAction implements QSCallBack {

  protected static final Log log = LogFactory.getLog(VistaPrintersAction.class);
  private static Hashtable<String, List<VistaPrinterJson>> stationPrintersMap 
      = new Hashtable<String, List<VistaPrinterJson>>();
  private static RadixTree<VistaPrinterJson> nameTree;
  private static final long serialVersionUID = 0;
  
  /**
   * JSON filter string sent by ExtJS, e.g. ?filter=[{"property":"name","value":"lom"}] 
   */
  private String filter; 
  private Integer start;
  private Integer limit;
  private String sort;
  private String dir;
  
  private SettingsService settingsService;

  public void prepare() throws Exception {
    
    super.prepare();    
    try {
      this.settingsService = ServiceFactory.getSettingsService();
    } catch(Exception e) {
      log.error("error creating service", e);
    }
    request.getSession(true);
  }   
  
  public String fetch() throws Exception {
    
    List<FilterProperty> filters = null;
    if (filter != null && !filter.isEmpty()) {
      Type collectionType = new TypeToken<Collection<FilterProperty>>(){}.getType();
      filters = (List<FilterProperty>)JsonResponse.deserializeJson(this.filter, collectionType);
    }
    
    String stationNo = this.settingsService.getFacilityNoForDivision(facilityNo).getPayload();
    
    List<VistaPrinterJson> printersJson = getVistaPrinters(stationNo);
    List<VistaPrinterJson> printers = new ArrayList<VistaPrinterJson>();

    int count = 0;
    
    if ((filters == null) || (filters.size() == 0)) {
      
      this.start = this.start >= 0 ? this.start : 0;
      this.limit = this.limit >= 0 ? this.limit : 100;
      
      for (int i = this.start; i < (this.start + this.limit); i++) {
        if (i >= printersJson.size()) {
          break;
        }
        printers.add(printersJson.get(i));
      }
      
      count = printersJson.size();
      
    } else {
      
      this.start = 0;
      this.limit = 100;
      
      Iterable<VistaPrinterJson> nameIt = null;
      for (FilterProperty filter : filters) {
        if (filter.getProperty().toString().equals("name")) {
          nameIt = nameTree.getValuesForKeysStartingWith(filter.getValue().toUpperCase());
          Iterator<VistaPrinterJson> it = nameIt.iterator();
          while (it.hasNext()) {
            count++;
            VistaPrinterJson vpj = it.next();
            if ((count > this.start) && (count <= (this.start + this.limit))) { 
              printers.add(vpj);
            } else if (count > (this.start + this.limit)) {
              break;
            }            
          }
        }
      }
    }
    
    JsonResponse.embedRoot(response, true, count, "root", false, printers);

    return SUCCESS;
    
  }
  
  private List<VistaPrinterJson> getVistaPrinters(String stationNo) {
    
    List<VistaPrinterJson> vistaPrintersList = stationPrintersMap.get(stationNo);
    
    if (vistaPrintersList == null) {
      
      vistaPrintersList = new ArrayList<VistaPrinterJson>();
      
      synchronized(vistaPrintersList) { 
        
        nameTree = new ConcurrentRadixTree<VistaPrinterJson>(new DefaultCharArrayNodeFactory());
        
        super.securityContext.setDivision(stationNo);
        CollectionServiceResponse<VistaPrinter> csr = 
            this.settingsService.getVistaPrinters(super.securityContext, stationNo);
        AvsWebUtils.handleServiceErrors(csr, log);
        Collection<VistaPrinter> vistaPrinters = csr.getCollection();
        for (VistaPrinter vistaPrinter : vistaPrinters) {
          VistaPrinterJson vistaPrinterJson = new VistaPrinterJson();
          vistaPrinterJson.setIen(vistaPrinter.getIen());
          vistaPrinterJson.setName(vistaPrinter.getName());
          vistaPrinterJson.setLocation(vistaPrinter.getLocation());
          vistaPrinterJson.setDisplayName(vistaPrinter.getDisplayName());
          vistaPrinterJson.setRightMargin(vistaPrinter.getRightMargin());
          vistaPrinterJson.setPageLength(vistaPrinter.getPageLength());
          vistaPrinterJson.setIpAddress(vistaPrinter.getIpAddress());
          vistaPrintersList.add(vistaPrinterJson);
          nameTree.put(vistaPrinter.getName(), vistaPrinterJson);
        }
        
        QuickSort qs = new QuickSort();
        qs.setQSCallBack(this);
        qs.quickSort(vistaPrintersList);   
        
        stationPrintersMap.put(stationNo, vistaPrintersList);
      }
    }
    
    return vistaPrintersList;
  }
  
  public int compare(Object obj1, Object obj2) {
    if ((sort != null) && sort.equals("location")) {
      if ((dir != null) && dir.equals("DESC")) {
        return -((VistaPrinterJson)obj1).getLocation().compareTo(((VistaPrinterJson)obj2).getLocation());
      } else {
        return ((VistaPrinterJson)obj1).getLocation().compareTo(((VistaPrinterJson)obj2).getLocation());
      }
    } else {
      if ((dir != null) && dir.equals("DESC")) {
        return -((VistaPrinterJson)obj1).getName().compareTo(((VistaPrinterJson)obj2).getName());
      } else {
        return ((VistaPrinterJson)obj1).getName().compareTo(((VistaPrinterJson)obj2).getName());
      }      
    }
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }
  
  public Integer getStart() {
    return this.start;
  }
  
  public Integer getLimit() {
    return this.limit;
  }

  public void setStart(Integer start) {
    this.start = start;
  }
  
  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public String getDir() {
    return dir;
  }

  public void setDir(String dir) {
    this.dir = dir;
  }
  
}
