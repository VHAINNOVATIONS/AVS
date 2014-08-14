package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.exception.VistaBrokerException;
import gov.va.med.lom.vistabroker.lists.data.ListItem;

import java.util.ArrayList;
import java.util.List;

public class NoteTitlesDao extends BaseDao {
	
  // Note Classes
  public static final String CLASS_PROGRESS_NOTES = "3";
  public static final String CLASS_DISCHARGE_SUMMARIES = "244";  

  // CONSTRUCTORS
  public NoteTitlesDao() {
    super();
  }
  
  public NoteTitlesDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public synchronized List<ListItem> getTitlesForClass(String classIen, String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU LONG LIST OF TITLES");
    Object[] params = {classIen, startFrom, direction};
    List<String> list = lCall(params);
    List<ListItem> listItems = new ArrayList<ListItem>();
    for(String s : list) {
      ListItem listItem = new ListItem();
      listItem.setIen(StringUtils.piece(s, 1));
      listItem.setName(StringUtils.piece(s, 2));
      listItem.setValue(StringUtils.piece(s, 3));
      listItem.setRpcResult(s);
      listItems.add(listItem);
    }
    return listItems;
  }
  
  public synchronized List<ListItem> subsetOfProgressNoteTitles(String startFrom, int direction) throws Exception {
    return getTitlesForClass(CLASS_PROGRESS_NOTES, startFrom, direction);
  }
  
  public synchronized List<ListItem> subSetOfDCSummaryTitles(String startFrom, int direction) throws Exception {
    return getTitlesForClass(CLASS_DISCHARGE_SUMMARIES, startFrom, direction);
  }    
  
  /**
   * Returns a pointer to a list of clinical procedures titles (for use in a long list box).
   * @param startFrom
   * @param direction
   * @param idNoteTitlesOnly
   * @return
   * @throws VistaBrokerException
   */
  public synchronized List<ListItem> subSetOfClinProcTitles(String startFrom, int direction, boolean idNoteTitlesOnly) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU LONG LIST CLINPROC TITLES");
    Object[] params = {startFrom, direction, StringUtils.boolToStr(idNoteTitlesOnly, "true", "false")};
    List<String> list = lCall(params);
    List<ListItem> listItems = new ArrayList<ListItem>();
    for(String s : list) {
      ListItem listItem = new ListItem();
      listItem.setIen(StringUtils.piece(s, 1));
      listItem.setName(StringUtils.piece(s, 2));
      listItems.add(listItem);
    }
    return listItems;
  }
  
}
