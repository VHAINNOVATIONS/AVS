package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.exception.ExceptionUtils;
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.exception.VistaBrokerIllegalStateException;
import gov.va.med.lom.vistabroker.patient.data.*;
import gov.va.med.lom.vistabroker.util.FMDateUtils;
import gov.va.med.vistalink.rpc.RpcRequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OrdersDao extends BaseDao {
  
  private static final Log log = LogFactory.getLog(OrdersDao.class);
  private static HashMap<String, List<String>> dGroupMap = new HashMap<String, List<String>>();
  
  // STATUS IDENTIFIERS
  public static final int STS_ACTIVE       = 2;
  public static final int STS_DISCONTINUED = 3;
  public static final int STS_COMPLETE     = 4;
  public static final int STS_EXPIRING     = 5;
  public static final int STS_RECENT       = 6;
  public static final int STS_UNVERIFIED   = 8;
  public static final int STS_UNVER_NURSE  = 9;
  public static final int STS_UNVER_CLERK  = 10;
  public static final int STS_UNSIGNED     = 11;
  public static final int STS_FLAGGED      = 12;
  public static final int STS_HELD         = 18;
  public static final int STS_NEW          = 19;
  public static final int STS_CURRENT      = 23;
  
  // ORDER VIEW TITLES
  public static final String TITLE_ACTIVE = "Active Orders (includes Pending & Recent Activity) - ALL SERVICES";
  public static final String TITLE_CURRENT = "Current Orders (Active & Pending Status Only) - ALL SERVICES";
  public static final String TITLE_EXPIRING = "Expiring Orders - ALL SERVICES";
  public static final String TITLE_UNSIGNED = "Unsigned Orders - ALL SERVICES";
  
  // ORDER URGENCY CUSTOM RPC
  private static final int ORDER_URGENCY_RPC_EXISTS = 1;
  private static final int ORDER_URGENCY_RPC_NOT_EXISTS = 0;
  private int orderUrgencyRpcContextState = ORDER_URGENCY_RPC_EXISTS;
  private String orderUrgencyRpcContext = null;
 
  // REG-EX PATTERNS
  private static Pattern labOrderNumPattern = null;
  private static Pattern orderTextPattern = null;
  private static Pattern natureOfOrderPattern = null;
  private static Pattern enteredByPattern = null;
  private static Pattern releasedByPattern = null;
  private static Pattern signedByPattern = null;
  private static Pattern orderedByPattern = null;
  private static Pattern currentDataPattern = null;
  private static Pattern treatingSpecialtyPattern = null;
  private static Pattern orderingLocationPattern = null;
  private static Pattern startDatetimePattern = null;
  private static Pattern stopDatetimePattern = null;
  private static Pattern currentStatusPattern = null;
  private static Pattern orderNumPattern = null;
  private static Pattern labTestPattern = null;
  private static Pattern collectedByPattern = null;
  private static Pattern collectionSamplePattern = null;
  private static Pattern specimenPattern = null;
  private static Pattern collectionDatetimePattern = null;
  private static Pattern urgencyPattern = null;
  private static Pattern howOftenPattern = null;
  
  // ORDER DELAY EVENT CODES
  private static final char[] DELAY_EVENTS = {'A','D','T','M','O'};
  
  // WORD PROCESSING FIELD
  public static final String TX_WPTYPE = "^WP^";
  
  // STATIC INITIALIZER
  static {
    // Set up regular expression patterns
    try {
      labOrderNumPattern = Pattern.compile("^[\\w\\s]+\\s{1}LB #(\\d{4,12})$");
      enteredByPattern = Pattern.compile("^[\\w\\s/:]+\\s{1}New Order entered by\\s{1}([\\w\\s,\\(\\)'-]+)$");
      orderTextPattern = Pattern.compile("^\\s{5}Order Text:\\s{8}([\\w\\s,\\(\\)'-]+)$");
      natureOfOrderPattern = Pattern.compile("^\\s{5}Nature of Order:\\s{3}([\\w\\s,\\(\\)'-]+)\\s{1}on\\s{1}.*$");
      releasedByPattern = Pattern.compile("^\\s{5}Released by:\\s{7}([\\w\\s,\\(\\)'-]+)\\s{1}on\\s{1}.*$");
      signedByPattern = Pattern.compile("^\\s{5}Elec Signature:\\s{4}([\\w\\s,\\(\\)'-]+)\\s{1}on\\s{1}.*$");
      orderedByPattern = Pattern.compile("^\\s{5}Ordered by:\\s{8}([\\w\\s,\\(\\)'-]+).*$");
      currentDataPattern = Pattern.compile("^Current Data:\\s{17}([\\w\\s\\d,\\(\\)'-\\\\\\.\\<\\>/&]+)$");
      treatingSpecialtyPattern = Pattern.compile("^Treating Specialty:\\s{11}([\\w\\s\\d,\\(\\)'-\\\\\\.\\<\\>/&]+)$");
      orderingLocationPattern = Pattern.compile("^Ordering Location:\\s{12}([\\w\\s\\d,\\(\\)'-\\\\\\.\\<\\>/&]+)$");
      startDatetimePattern = Pattern.compile("^Start Date/Time:\\s{14}(\\d{2}\\/\\d{2}\\/\\d{4}\\s{1}\\d{2}:\\d{2})$");
      stopDatetimePattern = Pattern.compile("^Stop Date/Time:\\s{14}(\\d{2}\\/\\d{2}\\/\\d{4}\\s{1}\\d{2}:\\d{2})$");
      currentStatusPattern = Pattern.compile("^Current Status:\\s{15}([\\w\\s\\d,\\(\\)'-\\\\\\.\\<\\>/]+)$");
      orderNumPattern = Pattern.compile("^Order #(\\d{4,12})$");
      labTestPattern = Pattern.compile("^Lab Test:\\s{21}([\\w\\s\\d,\\(\\)'-\\\\\\.\\<\\>/&]+)$");
      collectedByPattern = Pattern.compile("^Collected By:\\s{17}([\\w\\s\\d,\\(\\)'-\\\\\\.\\<\\>/&]+)$");
      collectionSamplePattern = Pattern.compile("^Collection Sample:\\s{12}([\\w\\s\\d,\\(\\)'-\\\\\\.\\<\\>/&]+)$");
      specimenPattern = Pattern.compile("^Specimen:\\s{21}([\\w\\s\\d,\\(\\)'-\\\\\\.\\<\\>/&]+)$");
      collectionDatetimePattern = Pattern.compile("^Collection Date/Time:\\s{9}([\\w\\s/:]+)$");
      urgencyPattern = Pattern.compile("^Urgency:\\s{22}([\\w\\s]+)$");
      howOftenPattern = Pattern.compile("^How often:\\s{20}([\\w\\s]+)$");
    } catch(PatternSyntaxException pse) {
      log.error(ExceptionUtils.describeException(pse));
    }
  }
  
  // CONSTRUCTORS
  public OrdersDao() {
    super();
  }
  
  public OrdersDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // Returns the status of an order given the status ien.
  public static String getNameOfStatus(String ien) {
    String name = null;
    if (ien.equals("0")) name = "error";
    else if (ien.equals("1")) name = "discontinued"; 
    else if (ien.equals("2")) name = "complete"; 
    else if (ien.equals("3")) name = "hold";
    else if (ien.equals("4")) name = "flagged";
    else if (ien.equals("5")) name = "pending";
    else if (ien.equals("6")) name = "active";
    else if (ien.equals("7")) name = "expired"; 
    else if (ien.equals("8")) name = "scheduled"; 
    else if (ien.equals("9")) name = "partial results"; 
    else if (ien.equals("10")) name = "delayed"; 
    else if (ien.equals("11")) name = "unreleased";
    else if (ien.equals("12")) name = "dc/edit"; 
    else if (ien.equals("13")) name = "cancelled"; 
    else if (ien.equals("14")) name = "lapsed"; 
    else if (ien.equals("15")) name = "renewed";
    else if (ien.equals("97")) name = "";      // null status, used for 'No Orders Found.
    else if (ien.equals("98")) name = "new"; 
    else if (ien.equals("99")) name = "no status"; 
    return name;
  }  
  
  // RPC API  
  /*
   * Returns the details of a lab order.
   */
  public String getLabOrderDetails(String dfn, String orderId) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQLR DETAIL");
    Object[] params = {String.valueOf(dfn), orderId};
    List<String> list = lCall(params);
    StringBuffer results = new StringBuffer();
    for (String s : list)
      results.append(s + '\n');
    return results.toString().trim();
  }
  
  /*
   * Retrieves the user's default view for the orders tab.
   * Example Results:  
   *   0;0;2;1;L;R;1;Active Orders (includes Pending & Recent Activity) - ALL SERVICES
   */
  public OrderView getOrderViewDefault() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWOR VWGET");
    String x = sCall();
    OrderView orderView = new OrderView();
    orderView.setChanged(false);
    orderView.setDGroup(StringUtils.piece(x, ';', 4));
    orderView.setFilter(StringUtils.toInt(StringUtils.piece(x, ';', 3), 0));
    orderView.setInvChrono(StringUtils.piece(x, ';', 6).equals("R"));
    orderView.setByService(StringUtils.piece(x, ';', 7).equals("1"));
    orderView.setFmTimeFrom(StringUtils.toDouble(StringUtils.piece(x, ';', 1), 0.0));
    orderView.setFmTimeFrom(StringUtils.toDouble(StringUtils.piece(x, ';', 2), 0.0));
    orderView.setContextTime(0);
    orderView.setTextView(0);
    orderView.setViewName(StringUtils.piece(x, ';', 8));
    orderView.setEventDelayType("C");
    orderView.setEventDelaySpecialty(0);
    orderView.setEventDelayEffective(0);
    return orderView;
  }
  
  /*
   * Returns the fields for a single order in the format:
        1   2    3     4      5     6   7   8   9   10     11    12
   LST=~IFN^Grp^ActTm^StrtTm^StopTm^Sts^Sig^Nrs^Clk^PrvID^PrvNam^ActDA
   */
  public OrderInfo getOrderByIfn(String id) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWORR GETBYIFN");
    List<String> l = lCall(id);
    return setOrderFromResults(l);
  }
  
  private OrderInfo setOrderFromResults(List<String> l) {
    String x = "";
    StringBuffer y = new StringBuffer();
    StringBuffer z = new StringBuffer();
    while(l.size() > 0) {
      x = l.get(0);
      l.remove(0);
      if (x.charAt(0) != '~') {
        continue;
      }
      y.delete(0, y.length());
      while ((l.size() > 0) && (l.get(0).charAt(0) != '~') && (l.get(0).charAt(0) != '|')) {
        y.append(l.get(0).substring(1, l.get(0).length()));
        y.append('\r');
        y.append('\n');
      }
      if (y.length() > 0) {
        y.delete(y.length() - 2, y.length());
      }
      z.delete(0, z.length());
      if ((l.size() > 0) && (l.get(0).equals("|"))) {
        l.remove(0);         
        while ((l.size() > 0) && (l.get(0).charAt(0) != '~') && (l.get(0).charAt(0) != '|')) {
          z.append(l.get(0).substring(1, l.get(0).length()));
          l.remove(0);
        }
      }
    }
    return setOrderFields(x, y.toString(), z.toString());    
  }
  
  /*
  private Order setOrderFields(String x, String y, String z) {
    Order order = new Order();
    order.setId(StringUtils.piece(x,1).substring(1, StringUtils.piece(x, 1).length()));
    order.setDGroup(StringUtils.piece(x, 2));
    try {
      order.setOrderTime(Double.valueOf(StringUtils.piece(x, 3)));
    } catch(Exception e) {
      order.setOrderTime(0);
    }
    order.setStartTime(StringUtils.piece(x, 4));
    order.setStopTime(StringUtils.piece(x, 5));
    try {
      order.setStatus(Integer.valueOf(StringUtils.piece(x, 6)));
    } catch(Exception e) {
      order.setStatus(0);
    }
    try {
      order.setSignature(Integer.valueOf(StringUtils.piece(x, 7)));
    } catch(Exception e) {
      order.setSignature(0);
    }
    order.setVerNurse(StringUtils.piece(x, 8));
    order.setVerClerk(StringUtils.piece(x, 9));
    order.setProviderDuz(StringUtils.piece(x, 10));
    order.setProviderName(StringUtils.piece(x, 11));
    order.setChartRev(StringUtils.piece(x, 15));
    order.setProviderDEA(StringUtils.piece(x, 16));
    order.setProviderVa(StringUtils.piece(x, 17));
    order.setDigSigReq(StringUtils.piece(x, 18));
    order.setFlagged(StringUtils.strToBool(StringUtils.piece(x, 13), "1"));
    order.setRetrieved(true);
    order.setOrderLocIen(StringUtils.piece(StringUtils.piece(x, 19), ':', 2));
    order.setOrderLocName(StringUtils.piece(StringUtils.piece(x, 19), ':', 1));
    order.setText(y);
    order.setXmlText(z);
    try {
      DGroup dgroup = getSeqOfDGroup(order.getDGroup());
      if (dgroup != null) {
        order.setDGroupSeq(dgroup.getIen());
        order.setDGroupName(dgroup.getItem());
      }
    } catch(Exception e) {}
    if (y.indexOf("Entered in error") > 0) {
      order.setEnteredInError(1);
    }
    return order;
  }
  */  
    
  /**
   * Returns detailed order information.
   */
  public String getOrderDetails(String orderId, String patientDfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQOR DETAIL");
    Object[] params = {orderId, patientDfn};
    List<String> list = lCall(params);
    StringBuffer results = new StringBuffer();
    for (String s : list)
      results.append(s + '\n');
    return results.toString().trim();
  }  

  /**
   * Returns detailed order information.
   * @deprecated To avoid Mumps errors, use the other version of this method, which requires patientDfn as a second argument.
   */
  public String getOrderDetails(String orderId) throws Exception {
	return this.getOrderDetails(orderId, "0");
  }  

  public String getAlsiOrderDetails(String orderId) throws Exception {
  	setDefaultContext(null);
  	setDefaultRpcName("ALSI ORDER DETAIL");
  	Object[] params = {orderId};
  	List<String> list = lCall(params);
  	StringBuffer results = new StringBuffer();
  	for (String s : list)
  	  results.append(s + '\n');
  	return results.toString().trim();
  }  
  
  /*
   * Returns results of a CPRS order.
   */
  public String getOrderResult(String dfn, String orderId) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWOR RESULT");
    Object[] params = {String.valueOf(dfn), orderId, orderId};
    List<String> list = lCall(params);
    StringBuffer results = new StringBuffer();
    for (String s : list)
      results.append(s + '\n');
    return results.toString().trim();
  }    
  
  
  /**
   * public interface for fetching DGroup
   * 
   * @return
   * @throws Exception
   */
  public List<String> getDGroupMap() throws Exception {
  
      return getDGroupMap(true);
      
  }
  
  
  
  /*
   * Retrieves the order group map.
   * Example Results:
   *   2=72^OTC/Herbal Products^OTC/Herbal P
   *   3=70^Inpt. Meds^Inpt. Meds
   *   4=65^Out. Meds^Out. Meds
   *   5=75^Lab^Lab
   *   6=75^Lab^Chemistry
   *   7=75^Lab^Hematology
   *   8=75^Lab^Microbiology
   *   9=80^Imaging^Radiology
   */
  
  /**
   * 
   * Fetches the DGroups from VistA  
   * into a static map for use later by other 
   * RPC results
   * 
   * @param primary    !!should only be true by calling from the public interface
   *                   meainng an application called this directly.
   *                   we are trying to determine if there are potential rpc context
   *                   and name conflicts.  All internal callers should set this
   *                   parameter to false  
   * @return
   */
  private List<String> getDGroupMap(boolean primary) throws Exception {
    
      // see if we already have it
      List<String> dGroup = (List<String>)dGroupMap.get(getDivision());
      if(dGroup != null) return dGroup;
    
      // OK, we don't have the DGroup for this station.  
      // if this is not the primary RPC being called (@primary = false) 
      // and the rpc context or rpc name in the security context is 
      // set then we need to throw an error
      
      if(primary == false){
          if((securityContext.getRpcContext() != null && 
              securityContext.getRpcContext().length() >0) ||
             (securityContext.getRpcName() != null && 
              securityContext.getRpcName().length() > 0)){
            
              String err = "The security context has an overriding rpc context or rpc name so this  " +
                      "rpc cannot be called indirectly.  This method caches the DGroup into a static " +
                      "map, it must be called once before any method requiring access to the map, " +
                      "particularly patient orders";
                      
              log.error(err);
              throw new VistaBrokerIllegalStateException(err);
        }
      }
      
      setDefaultContext("OR CPRS GUI CHART");
      setDefaultRpcName("ORWORDG MAPSEQ");
      dGroup = lCall();  
      dGroupMap.put(getDivision(), dGroup);
      return dGroup;
  } 
  
  /*
   * For a given IEN, returns the group.
   */
  public DGroup getSeqOfDGroup(String ien) throws Exception {
    List<String> dGroup = getDGroupMap(false);
    String value = null;
    for (int i = 0; i < dGroup.size(); i++) {
      String k = StringUtils.piece(dGroup.get(i), '=', 1);
      if (k.equals(ien)) {
        value = StringUtils.piece(dGroup.get(i), '=', 2);
        break;
      }
    }
    if (value != null) {
      DGroup dgroup = new DGroup();
      dgroup.setIen(StringUtils.piece(value, 1));
      dgroup.setItem(StringUtils.piece(value, 2));
      dgroup.setSubitem(StringUtils.piece(value, 3));
      return dgroup;
    } else
      return null;
  }
  
  /*
   * Returns order sheets for a patient.
   * 
   * Example Results: 
   *   C;O^Current View
   *   A;-1^Admit...
   */
  public List<OrderSheet> getOrderSheets(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWOR SHEETS");
    List<OrderSheet> orderSheets = new ArrayList<OrderSheet>();
    List<String> list = lCall(dfn); 
    for (String s : list) {
      OrderSheet orderSheet = new OrderSheet();
      orderSheet.setCode(StringUtils.piece(s, 1));
      orderSheet.setId(StringUtils.piece(s, 2));
      orderSheet.setName(StringUtils.piece(s, 3));
      orderSheets.add(orderSheet);
    }
    return orderSheets;
  }
  
  /*
   * Returns list of order discontinue reasons.
   */
  public List<DcReason> getDiscontinueReasons() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDX2 DCREASON");
    List<DcReason> dcReasons = new ArrayList<DcReason>();
    List<String> list = lCall(); 
    for (String s : list) {
      if (!s.startsWith("~")) {
        DcReason dcReason = new DcReason();
        dcReason.setIen(StringUtils.piece(s.substring(1, s.length()), 1));
        dcReason.setName(StringUtils.piece(s, 2));
        dcReasons.add(dcReason);
      }
    }
    
    return dcReasons;
  }  
  
  /*
   * RPC to discontinue, cancel, or delete an existing order.
   */
  public int discontinueOrder(String orderId, String providerDuz, String locationIen, String reasonIen, 
      boolean dcOrigOrder, boolean newOrder) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDXA DC");
    String dcOrigOrderStr = StringUtils.boolToStr(dcOrigOrder, "1", "0");
    String newOrderStr = StringUtils.boolToStr(newOrder, "1", "0");
    Object[] params = {orderId, providerDuz, locationIen, reasonIen, dcOrigOrderStr, newOrderStr};
    String x = sCall(params);
    return StringUtils.toInt(StringUtils.piece(x, 14), 0);
  }
  
  /*
   * RPC to hold an order.
   */
  public OrderInfo holdOrder(String orderId, String providerDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDXA HOLD");
    Object[] params = {orderId, providerDuz};
    List<String> l = lCall(params);
    return setOrderFromResults(l);
  }
  
  /*
   * RPC to release an order hold.
   */
  public OrderInfo releaseOrderHold(String orderId, String providerDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDXA UNHOLD");
    Object[] params = {orderId, providerDuz};
    List<String> l = lCall(params);
    return setOrderFromResults(l);
  }  
  
  /*
   * RPC to verify an order.
   */
  public OrderInfo verifyOrder(String orderId, String esCode) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDXA VERIFY");
    Object[] params = {orderId, esCode};
    List<String> l = lCall(params);
    return setOrderFromResults(l);
  }  
  
  /*
   * RPC to complete an order.
   */
  public OrderInfo completeOrder(String orderId, String esCode) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDXA COMPLETE");
    Object[] params = {orderId, esCode};
    List<String> l = lCall(params);
    return setOrderFromResults(l);
  }  
  
  /*
   * Returns the order fields for a list of orders for the user's default view.
   */
  public OrdersInfoList getOrdersListForDefaultView(String dfn) throws Exception {
    OrderView orderView = getOrderViewDefault();
    return getOrdersList(dfn, orderView);
  }
  
  public OrdersInfoList getOrdersList(String dfn, OrderView orderView) throws Exception {
    return getOrdersList(dfn, orderView, null);
  }
  
  /*
   * Returns the order fields for a list of orders.
   *            1   2    3     4      5     6   7   8   9    10    11    12    13    14     15     16  17    18
   * Pieces:  ~IFN^Grp^ActTm^StrtTm^StopTm^Sts^Sig^Nrs^Clk^PrvID^PrvNam^ActDA^Flag^DCType^ChrtRev^DEA#^VA#^DigSig}
   * Example: ~15002123;2^4^3050929.1421^3050929^3051019^6^6^^^11183^SCHMOE,JOE^^0^^^BZ3098230-T2342^^^
   *          t*OTHER PHARMACEUTICAL MISCELLANEOUS  OTHER PHARMACEUTICAL 
   *          tUSE FFFFFF    DAILY
   *          tQuantity: 5 Refills: 0
   */
  public OrdersInfoList getOrdersList(String dfn, OrderView orderView, 
                                                   Calendar startDate) throws Exception {
    return getOrdersList(dfn, orderView, startDate, true);
  }
  
  public OrdersInfoList getOrdersList(String dfn, OrderView orderView, 
                                                   Calendar startDate, 
                                                   boolean checkUrgency) throws Exception {
    OrdersInfoList ordersInfoList = getOrdersAbbr(dfn, orderView);
    return getOrdersList(dfn, startDate, checkUrgency, ordersInfoList);
  }    
  
  
  public OrdersInfoList getOrdersList(String dfn, Calendar startDate, 
          boolean checkUrgency, OrdersInfoList ordersInfoList) throws Exception {
      
      if(ordersInfoList == null || 
         ordersInfoList.getOrdersInfo() == null || 
         ordersInfoList.getOrdersInfo().size() == 0){
          return null;
      }
         
      List<OrderInfo> ordersList = new ArrayList<OrderInfo>();
      List<String> idList = new ArrayList<String>();
      List<OrderInfo> orders = ordersInfoList.getOrdersInfo();
      for (OrderInfo o : orders)
          idList.add(o.getId());
      setDefaultContext("OR CPRS GUI CHART");
      setDefaultRpcName("ORWORR GET4LST");    
      Object[] params = {String.valueOf(ordersInfoList.getTextView()), 
              String.valueOf(ordersInfoList.getFmDatetime()), idList};

      setTimeoutForCall(30000);

      List<String> list = lCall(params);
      int orderIndex = -1;
      StringBuffer orderData = new StringBuffer();
      StringBuffer text = new StringBuffer();
      StringBuffer xmlText = new StringBuffer();
      OrderInfo orderInfo = null;
      int lineIndex = 0;
      while ((list.size() > 0) && (lineIndex < list.size())) {
          String x = (String)list.get(lineIndex);
          text.delete(0, text.length());
          if ((x.charAt(0) != '~') && (!StringUtils.piece(x, 1).equals("~" + orderInfo.getId()))) {
              while ((lineIndex < list.size()) && (x.charAt(0) != '~') && (x.charAt(0) != '|')) {
                  orderData.append(x + '\n');
                  text.append(x.substring(1) + '\n') ;
                  do {
                      lineIndex++;
                      if (lineIndex < list.size()) {
                          x = (String)list.get(lineIndex);
                      }
                  } while((x.length() == 0) && (lineIndex < list.size()));
              }
              if (text.length() > 0)
                  text.deleteCharAt(text.length()-1); // take off last newline char
              orderInfo.setText(text.toString());
              xmlText.delete(0, xmlText.length());
              if (x.charAt(0) == '|') {
                  lineIndex++;
                  x = (String)list.get(lineIndex);
                  while ((lineIndex < list.size()) && (x.charAt(0) != '~') && (x.charAt(0) != '|')) {
                      orderData.append(x + '\n');
                      text.append(x.substring(1) + '\n') ;
                      do {
                          lineIndex++;
                          if (lineIndex < list.size()) {
                              x = (String)list.get(lineIndex);
                          }
                      } while((x.length() == 0) && (lineIndex < list.size()));
                  }
              }
              orderInfo.setXmlText(xmlText.toString());
              orders.set(orderIndex, orderInfo);
          } else {
              double orderFmDatetime = StringUtils.toDouble(StringUtils.piece(x, 3), 0);
              Date date = FMDateUtils.fmDateTimeToDate(orderFmDatetime);
              orderData.append(x + '\n');
              orderInfo = new OrderInfo();
              orderIndex++;
              if (date != null) {
                  GregorianCalendar gc = new GregorianCalendar();
                  gc.setTime(date);
                  orderInfo.setOrderDatetime(gc);
              }
              OrderInfo oi = orders.get(orderIndex);
              orderInfo.setId(oi.getId());
              orderInfo.setEventPtr(oi.getEventPtr());
              orderInfo.setEventName(oi.getEventName());
              orderInfo.setDGroupSeq(oi.getDGroupSeq());
              orderInfo.setDGroupName(oi.getDGroupName());
              orderInfo.setDGroup(StringUtils.piece(x, 2));
              orderInfo.setFmOrderDatetime(orderFmDatetime);
              try {
                  orderInfo.setOrderDatetimeStr(FMDateUtils.fmDateTimeToEnglishDateTime(orderFmDatetime));
              } catch(ParseException pe) {}     
              try {
                  orderInfo.setStartTimeStr(FMDateUtils.fmDateTimeToEnglishDateTime(StringUtils.toDouble(StringUtils.piece(x, 4), 0)));
              } catch(ParseException pe) {} 
              try {
                  orderInfo.setStopTimeStr(FMDateUtils.fmDateTimeToEnglishDateTime(StringUtils.toDouble(StringUtils.piece(x, 5), 0)));
              } catch(ParseException pe) {}         
              orderInfo.setStatusIen(StringUtils.piece(x, 6));
              orderInfo.setStatus(getNameOfStatus(orderInfo.getStatusIen()));
              orderInfo.setSignature(StringUtils.toInt(StringUtils.piece(x, 7), 0));
              orderInfo.setVerNurse(StringUtils.piece(x, 8));
              orderInfo.setVerClerk(StringUtils.piece(x, 9));
              orderInfo.setChartRev(StringUtils.piece(x, 15));
              orderInfo.setProviderDuz(StringUtils.piece(x, 10));
              orderInfo.setProviderName(StringUtils.piece(x, 11));
              orderInfo.setProviderDEA(StringUtils.piece(x, 16));
              orderInfo.setProviderVA(StringUtils.piece(x, 17));
              orderInfo.setDigSigReq(StringUtils.piece(x, 18));
              orderInfo.setFlagged(StringUtils.piece(x, 13).equals("1"));
              String urgency = null;
              if (checkUrgency) 
                  urgency = getOrderUrgency(orderInfo.getId(), dfn);
              if (urgency == null)
                  urgency = "";
              orderInfo.setUrgency(urgency);
              orderData.delete(0, orderData.length());
              if ((startDate == null) || (date.getTime() >= startDate.getTimeInMillis()))
                  ordersList.add(orderInfo);
              lineIndex++;
          }
      }
      OrdersInfoList newOrdersInfoList = new OrdersInfoList();
      newOrdersInfoList.setCount(ordersList.size());
      newOrdersInfoList.setTextView(ordersInfoList.getTextView());
      newOrdersInfoList.setFmDatetime(ordersInfoList.getFmDatetime());
      newOrdersInfoList.setOrdersInfo(orders);
      return newOrdersInfoList;
  }    


  public OrdersInfoList getOrdersAbbrForDefaultView(String dfn) throws Exception {
    OrderView orderView = getOrderViewDefault();
    return getOrdersAbbr(dfn, orderView);
  }
  
  /*
   * Get an abbreviated order list for a patient.
   * 
   * Example Results:
   *   29^2^3051001.222041
   *   15002123;2^4^3050929.1421^^
   *   15065015;1^6^3050927.1151^^
   */
  public OrdersInfoList getOrdersAbbr(String dfn, OrderView orderView) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWORR AGET");
      String filterTS = String.valueOf(orderView.getFilter()) + "^" + 
                        String.valueOf(orderView.getEventDelaySpecialty());
      Object[] params = {dfn, filterTS, orderView.getDGroup(),
                         String.valueOf(orderView.getFmTimeFrom()), 
                         String.valueOf(orderView.getFmTimeThru()), "C;0"};

      setTimeoutForCall(60000);
      List<String> list = lCall(params);   
      OrdersInfoList ordersInfoList = new OrdersInfoList();
      if (list.size() > 0) {
        String x = (String)list.get(0);
        int num = StringUtils.toInt(StringUtils.piece(x, 1), 0);
        ordersInfoList.setTextView(StringUtils.toInt(StringUtils.piece(x, 2), 0));
        ordersInfoList.setFmDatetime(StringUtils.toDouble(StringUtils.piece(x, 3), 0));
        List<OrderInfo> orders = new ArrayList<OrderInfo>();
        if (num > 0) {
          int index = 0;
          for (String s : list) {
            if (index > 0) {
              OrderInfo orderInfo = new OrderInfo();
              orderInfo = new OrderInfo();
              orderInfo.setId(StringUtils.piece(s, 1));
              orderInfo.setDGroup(StringUtils.piece(s, 2));
              orderInfo.setFmOrderDatetime(StringUtils.toDouble(StringUtils.piece(s, 3), 0));
              Date date = FMDateUtils.fmDateTimeToDate(orderInfo.getFmOrderDatetime());
              if (date != null) {
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(date);
                orderInfo.setOrderDatetime(gc);
                try {
                  orderInfo.setOrderDatetimeStr(FMDateUtils.fmDateTimeToEnglishDateTime(orderInfo.getFmOrderDatetime()));
                } catch(ParseException pe) {}
              }
              orderInfo.setEventPtr(StringUtils.piece(s, 4));
              orderInfo.setEventName(StringUtils.piece(s, 5));
              DGroup dgroup = getSeqOfDGroup(orderInfo.getDGroup());
              if (dgroup != null) {
                orderInfo.setDGroupSeq(dgroup.getIen());
                orderInfo.setDGroupName(dgroup.getItem());
              }
              orders.add(orderInfo);
            }
            index++;
          }
        }
        ordersInfoList.setOrdersInfo(orders);
      }
      return ordersInfoList;
  }
  
  /*
   * Returns the IDs of outstanding unsigned orders.
   */
  public List<String> getUnsignedOrders(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWOR UNSIGN");
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("0", "");
    Object[] params = {String.valueOf(dfn), map};
    return lCall(params);  
  } 
  
  public String getDisplayGroupIen(String group) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWORDG IEN");
    Object[] params = {group};
    return sCall(params);
  }
  
  /*
   * Parses the detail text of an order and returns an object
   * that encapsulates the detailed status of the order.
   */
  public OrderStatus parseOrderDetail(String orderId, String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQOR DETAIL");
    Object[] params = {orderId, String.valueOf(dfn)};
    List<String> list = lCall(params);
    OrderStatus orderStatus = new OrderStatus();
    int n = list.size();
    for(int i = 0; i < n; i++) {
      String x = ((String)list.get(i)).toUpperCase();
      if (x.startsWith("ACTIVITY:")) {
        orderStatus.setOrderDatetimeStr(((String)list.get(i+1)).substring(0, 16));
        try {
          orderStatus.setOrderDatetime(DateUtils.convertDateStr(orderStatus.getOrderDatetimeStr(), DateUtils.ENGLISH_DATE_TIME_FORMAT2));
        } catch(ParseException pe) {}
      } else if (x.startsWith("LAB TEST:"))
        orderStatus.setOrderName(x.substring(9, x.length()));
      else if (x.startsWith("ORDERED BY:"))
        orderStatus.setOrderedBy(x.substring(11, x.length()));
      else if (x.startsWith("COLLECTION DATE/TIME:")) {
        orderStatus.setCollectionDatetimeStr(x.substring(21, x.length()));
        try {
          orderStatus.setCollectionDatetime(DateUtils.convertDateStr(orderStatus.getCollectionDatetimeStr(), DateUtils.ENGLISH_DATE_TIME_FORMAT2));
        } catch(ParseException pe) {}        
      } else if (x.startsWith("SIGNATURE:"))
        orderStatus.setSignedBy(x.substring(10, x.length()));
      else if (x.startsWith("URGENCY:"))
        orderStatus.setUrgency(x.substring(8, x.length()));  
      else if (x.startsWith("ELEC SIGNATURE:"))
        orderStatus.setSignedBy(x.substring(15, x.length()));  
      else if (x.startsWith("CURRENT STATUS:"))
        orderStatus.setStatus(x.substring(15, x.length()));
    }
    return orderStatus;
  }

  /*
   * Parses the detail text of an order and returns the urgency text.
   */
  public String getOrderUrgency(String orderId, String dfn) throws Exception {
	  setDefaultContext("OR CPRS GUI CHART");
    // first try calling the rpc using the standard context
    if (orderUrgencyRpcContextState == ORDER_URGENCY_RPC_EXISTS) {
      if (orderUrgencyRpcContext == null)
        orderUrgencyRpcContext = "OR CPRS GUI CHART";
      try {
        return doGetOrderUrgency(orderId, orderUrgencyRpcContext);
      } catch(Exception be1) {
        // rpc isn't registered to standard context, try local context
        try {
          log.warn("'ALSI URGENCY OF ORDER' is not registered to '" + orderUrgencyRpcContext + "' context.");
          orderUrgencyRpcContext = "ALS CLINICAL RPC";
          return doGetOrderUrgency(orderId, orderUrgencyRpcContext);
        } catch(Exception be2) {
          log.warn("'ALSI URGENCY OF ORDER' is not registered to '" + orderUrgencyRpcContext + "' context.");
          orderUrgencyRpcContext = "";
          orderUrgencyRpcContextState = ORDER_URGENCY_RPC_NOT_EXISTS;
        	return parseOrderUrgency(orderId, dfn);
        }
      }
    } else
      return parseOrderUrgency(orderId, dfn);
  }  
  
  private String doGetOrderUrgency(String orderId, String context) throws Exception {
    setDefaultContext(context);   
    orderId = StringUtils.piece(orderId, ';', 1);
    Object[] params = {orderId};
    String urgency = null;
    String saveRpcName = null;
    try {
      // if there is an overriding RPC name in the security context 
      // ignore it by replacing it with the correct RPC name
      // we will continue to use an overriding context if it exists
      if(securityContext.getRpcName() != null && securityContext.getRpcName().length() >0){
          saveRpcName = securityContext.getRpcName();
          securityContext.setRpcName("ALSI URGENCY OF ORDER");
      }
      setDefaultRpcName("ALSI URGENCY OF ORDER");
      urgency = sCall(params);
    } catch(Exception be) {
      throw new Exception("ALSI URGENCY OF ORDER is not registered in the context " + context); 
    }finally{
        // reset the original RPC name
        if(saveRpcName != null){ 
            securityContext.setRpcName(saveRpcName);
        }
    }
    if (urgency.equals("0"))
      urgency = "";
    return urgency;  
  }
  
  /*
   * Parses the detail text of an order and returns the urgency text.
   */
  public String parseOrderUrgency(String orderId, String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQOR DETAIL");
    Object[] params2 = {orderId, String.valueOf(dfn)};
    List<String> list = lCall(params2);
    int n = list.size();
    String urgency = null;
    for(int i = 0; i < n; i++) {
      String x = ((String)list.get(i));
      if (x.length() > 0) {
        char c = x.charAt(0);
        if ((c == 'U') || (c == 'u')) {
          if (x.toUpperCase().startsWith("URGENCY:")) {
            urgency = x.substring(8, x.length()).trim();
            break;
          } 
        }
        if ((c == 'P') || (c == 'p')) {
          if (x.toUpperCase().startsWith("PRIORITY:")) {
            urgency = x.substring(9, x.length()).trim();
            break;
          } 
        }
      }
    }
    return urgency;
  }    
  
  /*
   * Parse order details text using regular expressions and populate bean.
   */
  public OrderDetails parseOrderDetails(String orderId, String patientDfn) throws Exception {
    OrderDetails orderDetails = new OrderDetails();
    String text = getOrderDetails(orderId, patientDfn);
    String[] lines = StringUtils.pieceList(text, '\n');
    Matcher m = null;
    for (int i = 0; i < lines.length; i++) {
      if ((m = labOrderNumPattern.matcher(lines[i])).matches()) {
        orderDetails.setLabOrderNum(m.group(1));
      } else if ((m = orderTextPattern.matcher(lines[i])).matches()) {
        orderDetails.setOrderText(m.group(1));
      } else if ((m = natureOfOrderPattern.matcher(lines[i])).matches()) {
        orderDetails.setNatureOfOrder(m.group(1));
      } else if ((m = enteredByPattern.matcher(lines[i])).matches()) {
        String str = m.group(1);
        str = StringUtils.piece(str, '(', 1).trim();
        orderDetails.setEnteredBy(str);
      } else if ((m = releasedByPattern.matcher(lines[i])).matches()) {
        String str = m.group(1);
        str = StringUtils.piece(str, '(', 1).trim();
        orderDetails.setReleasedBy(str);
      } else if ((m = signedByPattern.matcher(lines[i])).matches()) {
        String str = m.group(1);
        str = StringUtils.piece(str, '(', 1).trim();
        orderDetails.setSignedBy(str);
      } else if ((m = orderedByPattern.matcher(lines[i])).matches()) {
        String str = m.group(1);
        str = StringUtils.piece(str, '(', 1).trim();
        orderDetails.setOrderedBy(str);
      } else if ((m = orderNumPattern.matcher(lines[i])).matches()) {
        orderDetails.setOrderNum(m.group(1));        
      } else if ((m = currentDataPattern.matcher(lines[i])).matches()) {
        orderDetails.setCurrentData(m.group(1));
      } else if ((m = treatingSpecialtyPattern.matcher(lines[i])).matches()) {
        orderDetails.setTreatingSpecialty(m.group(1));
      } else if ((m = orderingLocationPattern.matcher(lines[i])).matches()) {
        orderDetails.setOrderingLocation(m.group(1));
      } else if ((m = startDatetimePattern.matcher(lines[i])).matches()) {
        String str =  m.group(1);
        try {
          orderDetails.setStartDatetime(DateUtils.toDate(str, "MM/dd/yyyy HH:mm"));
        } catch (ParseException pe) {} 
      } else if ((m = stopDatetimePattern.matcher(lines[i])).matches()) {
        String str =  m.group(1);
        try {
          orderDetails.setStopDatetime(DateUtils.toDate(str, "MM/dd/yyyy HH:mm"));
        } catch (ParseException pe) {}         
      } else if ((m = currentStatusPattern.matcher(lines[i])).matches()) {
        orderDetails.setCurrentStatus(m.group(1));
      } else if ((m = orderNumPattern.matcher(lines[i])).matches()) {
        orderDetails.setOrderNum(m.group(1));
      } else if ((m = labTestPattern.matcher(lines[i])).matches()) {
        orderDetails.setLabTest(m.group(1));
      } else if ((m = collectedByPattern.matcher(lines[i])).matches()) {
        orderDetails.setCollectedBy(m.group(1));
      } else if ((m = collectionSamplePattern.matcher(lines[i])).matches()) {
        orderDetails.setCollectionSample(m.group(1));
      } else if ((m = specimenPattern.matcher(lines[i])).matches()) {
        orderDetails.setSpecimen(m.group(1));
      } else if ((m = collectionDatetimePattern.matcher(lines[i])).matches()) {
        String str =  m.group(1);
        if (str.trim().equals("NOW"))
          orderDetails.setCollectionDatetime(new Date());
        else {
          try {
            orderDetails.setCollectionDatetime(DateUtils.toDate(str, "MM/dd/yyyy HH:mm"));
          } catch (ParseException pe) {}
        }
      } else if ((m = urgencyPattern.matcher(lines[i])).matches()) {
        orderDetails.setUrgency(m.group(1));
      } else if ((m = howOftenPattern.matcher(lines[i])).matches()) {
        orderDetails.setHowOften(m.group(1));
      }
    }
    return orderDetails;
  }
  
  /**
   * @deprecated To avoid Mumps errors, use the other version of this method, which requires patientDfn as a second argument.
   */
  public OrderDetails parseOrderDetails(String orderId) throws Exception {
	  return this.parseOrderDetails(orderId, "0");
  }

  /*
   * Returns the status of an order.
   */
  public int getOrderStatus(String orderId) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("OREVNTX1 GETSTS");
    String x = sCall(orderId);
    return StringUtils.toInt(x, 1);
  }
  
  /*
   * Return sample, specimen, & urgency info about a lab test.
   */
  public String getLabTestOrderData(String orderId) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDLR32 LOAD");
    return sCall(orderId);
  }
  
  public String alertOrder(String orderIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ALSLR ORDER ALERT");
    return sCall(orderIen);
  }
  
  /*
   * Return an error if the selected action is not valid for a particular order
   */
  public String validateOrderAction(String id, String action, String providerDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDXA VALID");
    Object[] params = {id, action, providerDuz};
    return sCall(params);
  }
  
  public String validateComplexOrderAction(String id) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDXA OFCPLX");
    return sCall(id);
  }  
  
  /*
   * Set order to send an alert when the order is resulted.
   */
  public String alertOrderForRecipient(String orderIen, String recipientDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDXA ALERT");
    Object[] params = {orderIen, recipientDuz};
    return sCall(params);
  }
  
  public LockDocumentResult lockPatient(String patientDfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDX LOCK");
    LockDocumentResult lockDocumentResult = new LockDocumentResult();
    String x = sCall(patientDfn);
    if (x.charAt(0) == '1') {
      lockDocumentResult.setSuccess(true);
      lockDocumentResult.setMessage("");
    } else {
      lockDocumentResult.setSuccess(false);
      lockDocumentResult.setMessage(StringUtils.piece(x, 2));      
    }
    return lockDocumentResult;
  }
  
  public String unlockPatient(String patientDfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");  
    setDefaultRpcName("ORWDX UNLOCK");
    return sCall(patientDfn);
  }   
  
  public LockDocumentResult lockOrder(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");    
    setDefaultRpcName("ORWDX LOCK ORDER");
    LockDocumentResult lockDocumentResult = new LockDocumentResult();
    String x = sCall(ien);
    if (x.charAt(0) == '1') {
      lockDocumentResult.setSuccess(true);
      lockDocumentResult.setMessage("");
    } else {
      lockDocumentResult.setSuccess(false);
      lockDocumentResult.setMessage(StringUtils.piece(x, 2));      
    }
    return lockDocumentResult;
  }   
  
  public String unlockOrder(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");  
    setDefaultRpcName("ORWDX UNLOCK ORDER");
    return sCall(ien);
  }    
  
  public OrderDialogResolved buildReponses(String patientDfn, String encLocation, String encProvider, boolean inpatient, 
      String patientSex, int patientAge, int scPercent, String keyVars, String inputId, boolean forImo) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDXM1 BLDQRSP");
    String delayEvent = "0;C;0;0";
    StringBuffer sb = new StringBuffer();
    sb.append(patientDfn + "^");
    sb.append(encLocation + "^");
    sb.append(encProvider + "^");
    sb.append(StringUtils.boolToStr(inpatient, "1", "0") + "^");
    sb.append(patientSex + "^");
    sb.append(patientAge + "^");
    sb.append(delayEvent + "^");
    sb.append(scPercent + "^");
    sb.append("^");
    sb.append("^");
    sb.append(keyVars);
    String[] params = {inputId, sb.toString(), StringUtils.boolToStr(forImo, "1", "0"), encLocation};
    List<String> list = lCall(params);
    String x = list.get(0);
    OrderDialogResolved dialog = new OrderDialogResolved();
    dialog.setQuickLevel(StringUtils.toInt(StringUtils.piece(x, 1), 0));
    dialog.setResponseId(StringUtils.piece(x, 2));
    dialog.setDialogIen(StringUtils.piece(x, 3));
    dialog.setDialogType(StringUtils.piece(x, 4).charAt(0));
    dialog.setFormId(StringUtils.toInt(StringUtils.piece(x, 5), 0));
    dialog.setDisplayGroup(StringUtils.piece(x, 6));
    dialog.setQoKeyVars(StringUtils.pieces(x, '^', 7, 17));
    
    return dialog;
  }
   
   public List<OrderResponse> loadResponses(String orderId, boolean xferOutToInOnMeds, 
       boolean xferInToOutNow) throws Exception {
     
     setDefaultContext("OR CPRS GUI CHART");
     setDefaultRpcName("ORWDX LOADRSP");
     List<OrderResponse> responses = new ArrayList<OrderResponse>();
     String transfer = StringUtils.boolToStr((xferOutToInOnMeds || xferInToOutNow) && orderId.charAt(0) == 'C', "1", "0"); 
     String[] params = {orderId, transfer};
     List<String> list = lCall(params);
     int i = 0;
     while (i < list.size()) {
       if (list.get(i).charAt(0) == '~') {
         OrderResponse response = new OrderResponse();
         response.setPromptIen(StringUtils.piece(list.get(i).substring(1, list.get(i).length()), 1));
         response.setInstance(StringUtils.toInt(StringUtils.piece(list.get(i), 2), 0));
         response.setPromptId(StringUtils.piece(list.get(i), 3));
         i++;
         while ((i < list.size()) && (list.get(i).charAt(0) != '~')) {
           if (list.get(i).charAt(0) == 'i') {
             response.setiValue(list.get(i).substring(1, list.get(i).length()));
           } else if (list.get(i).charAt(0) == 'e') {
             response.seteValue(list.get(i).substring(1, list.get(i).length()));
           } else if (list.get(i).charAt(0) == 't') {
             String eValue = response.geteValue();
             if (eValue == null) {
               eValue = "";
             }
             if (eValue.length() > 0) {
               eValue += "\r\n";                        
             }
             eValue += list.get(i).substring(1, list.get(i).length());
             response.seteValue(eValue);
             response.setiValue(TX_WPTYPE);
           }
           i++;
         }
         // TO-DO: expand order objects for iValue and eValue
         responses.add(response);
       }
     }
     
     return responses;
   }
   
   public List<String> buildOcItems(String startDtTm, String fillerId, 
       List<OrderResponse> orderResponses) throws Exception {
     
     setDefaultContext("OR CPRS GUI CHART");
     setDefaultRpcName("ORWDX LOADRSP");
     List<String> ocItems = new ArrayList<String>();
     for (OrderResponse orderResponse : orderResponses) {
       if (orderResponse.getPromptId().equals("ORDERABLE") || orderResponse.getPromptId().equals("ADDITIVE")) {
         String orderableIen = orderResponse.getiValue();      
         String pkgPart = null;
         int instance = orderResponse.getInstance();
         if (fillerId.equals("LR")) {
           pkgPart = "^LR^" + iValueFor(orderResponses, "SPECIMEN", instance);
         } else if (fillerId.equals("PSI") || fillerId.equals("PSO") || fillerId.equals("PSH") || fillerId.equals("PSNV")) {
           pkgPart = "^" + fillerId + "^" + iValueFor(orderResponses, "DRUG", instance);
         } else if (fillerId.equals("PSIV")) {
           if (orderResponse.getPromptId().equals("ORDERABLE")) {
             pkgPart = "PSIV^B;" + iValueFor(orderResponses, "VOLUME", instance); 
           } else if (orderResponse.getPromptId().equals("ADDITIVE")) {
             pkgPart = "PSIV^A;"; 
           }
         }
         ocItems.add(orderableIen + pkgPart);
       }
       // AGP IV CHANGES
       if (fillerId.equals("PSI") || fillerId.equals("PSO") || fillerId.equals("PSH") || fillerId.equals("PSNV")) {
         if (orderResponse.getPromptId().equals("COMMENT")) {
           continue;
         }
         ocItems.add(fillerId + "^" + orderResponse.getPromptId() + "^" + orderResponse.getInstance() + "^" + 
                     iValueFor(orderResponses, orderResponse.getPromptId(), orderResponse.getInstance()) + "^" +
                     eValueFor(orderResponses, orderResponse.getPromptId(), orderResponse.getInstance()));
       }
     }
     
     return ocItems;
   }
   
   private static String eValueFor(List<OrderResponse> orderResponses, String promptId, int instance) {
     for (OrderResponse orderResponse : orderResponses) {
       if (orderResponse.getPromptId().equals(promptId) && (orderResponse.getInstance() == instance)) {
         return orderResponse.geteValue();
       }
     }
     return null;
   }    
     
   private static String iValueFor(List<OrderResponse> orderResponses, String promptId, int instance) {
     for (OrderResponse orderResponse : orderResponses) {
       if (orderResponse.getPromptId().equals(promptId) && (orderResponse.getInstance() == instance)) {
         return orderResponse.getiValue();
       }
     }
     return null;
   }
   
   public List<OrderDialogPrompt> loadDialogDefinition(String dialogName) throws Exception {
     List<OrderDialogPrompt> prompts = new ArrayList<OrderDialogPrompt>();
     setDefaultContext("OR CPRS GUI CHART");
     setDefaultRpcName("ORWDX DLGDEF");
     List<String> list = lCall(dialogName);
     for (int i = 0; i < list.size(); i++) {
       OrderDialogPrompt prompt = new OrderDialogPrompt();
       prompt.setId(StringUtils.piece(list.get(i),1));
       prompt.setIen(StringUtils.piece(list.get(i),2));
       if (StringUtils.piece(list.get(i),3).length() > 0) {
         prompt.setSequence(StringUtils.toDouble(StringUtils.piece(list.get(i), 3), 0));
       } else {
         prompt.setSequence(0);
       }
       prompt.setFmtCode(StringUtils.piece(list.get(i),4));
       prompt.setOmit(StringUtils.piece(list.get(i),5));
       prompt.setLeading(StringUtils.piece(list.get(i),6));
       prompt.setTrailing(StringUtils.piece(list.get(i),7));
       prompt.setNewLine(StringUtils.piece(list.get(i),8).equals("1"));
       prompt.setWrapWp(StringUtils.piece(list.get(i),9).equals("1"));
       prompt.setChildren(StringUtils.piece(list.get(i),10));
       prompt.setIsChild(StringUtils.piece(list.get(i),11).equals("1"));
       prompts.add(prompt);
     }
     return prompts;
   }
     
   public List<String> orderChecksOnAccept(String patientDfn, String encLocation, String fillerId, String startDtTm, 
       List<String> oiList, String dupORIFN, String renewal) throws Exception {
     setDefaultContext("OR CPRS GUI CHART");
     setDefaultRpcName("ORWDXC ACCEPT");
     if ((oiList != null) && (oiList.size() > 0)) {
       Object[] params = {patientDfn, fillerId, startDtTm, encLocation, oiList, dupORIFN, renewal};
       return lCall(params); 
     } else {
       String[] params = {patientDfn, fillerId, startDtTm, encLocation};
       return lCall(params); 
     }
   }
   
   public OrderInfo putNewOrder(NewOrder newOrder) throws Exception {
     
     // Get order dialog info
     OrderDialogResolved dialog = this.buildReponses(newOrder.getPatientDfn(), newOrder.getEncLocation(), 
         newOrder.getEncLocation(), newOrder.isInpatient(), newOrder.getPatientSex(), newOrder.getPatientAge(),
         newOrder.getScPercent(), "", newOrder.getOrderId(), false);
     
     // Get order responses and put into hashtable
     List<OrderResponse> responseList = this.loadResponses(dialog.getResponseId(), false, false);
     Hashtable<String, String> responseListHT = new Hashtable<String, String>();
     for (OrderResponse orderResponse : responseList) {
       responseListHT.put(orderResponse.getPromptId(), orderResponse.getPromptId());
     }
     
     // Get dialog definitions
     List<OrderDialogPrompt> prompts = this.loadDialogDefinition(newOrder.getDialogName());
     
     // Add prompts to response list
     for (OrderDialogPrompt prompt : prompts) {
       if (!responseListHT.containsKey(prompt.getId())) {
         OrderResponse orderResponse = new OrderResponse();
         orderResponse.setPromptId(prompt.getId());
         orderResponse.setPromptIen(prompt.getIen());
         responseList.add(orderResponse);
       }
     }
     // Set internal and external values of order responses
     for (OrderResponse orderResponse : responseList) {
       if (orderResponse.getPromptId().equals("ORDERABLE") && (newOrder.getOrderable() != null)) {
         orderResponse.setiValue(newOrder.getOrderable().getiValue());
         orderResponse.seteValue(newOrder.getOrderable().geteValue());
       } else if (orderResponse.getPromptId().equals("INSTR") && (newOrder.getInstr() != null)) {
         orderResponse.setiValue(newOrder.getInstr().getiValue());
         orderResponse.seteValue(newOrder.getInstr().geteValue());
       } else if (orderResponse.getPromptId().equals("DRUG") && (newOrder.getDrug() != null)) {
         orderResponse.setiValue(newOrder.getDrug().getiValue());
         orderResponse.seteValue(newOrder.getDrug().geteValue());
       } else if (orderResponse.getPromptId().equals("DOSE") && (newOrder.getDose() != null)) {
         orderResponse.setiValue(newOrder.getDose().getiValue());
         orderResponse.seteValue(newOrder.getDose().geteValue());
       } else if (orderResponse.getPromptId().equals("STRENGTH") && (newOrder.getStrength() != null)) {
         orderResponse.setiValue(newOrder.getStrength().getiValue());
         orderResponse.seteValue(newOrder.getStrength().geteValue());
       } else if (orderResponse.getPromptId().equals("ROUTE") && (newOrder.getRoute() != null)) {
         orderResponse.setiValue(newOrder.getRoute().getiValue());
         orderResponse.seteValue(newOrder.getRoute().geteValue());
       } else if (orderResponse.getPromptId().equals("SCHEDULE") && (newOrder.getSchedule() != null)) {
         orderResponse.setiValue(newOrder.getSchedule().getiValue());
         orderResponse.seteValue(newOrder.getSchedule().geteValue());
       } else if (orderResponse.getPromptId().equals("URGENCY") && (newOrder.getUrgency() != null)) {
         orderResponse.setiValue(newOrder.getUrgency().getiValue());
         orderResponse.seteValue(newOrder.getUrgency().geteValue());
       } else if (orderResponse.getPromptId().equals("SUPPLY") && (newOrder.getSupply() != null)) {
         orderResponse.setiValue(newOrder.getSupply().getiValue());
         orderResponse.seteValue(newOrder.getSupply().geteValue());
       } else if (orderResponse.getPromptId().equals("QTY") && (newOrder.getQty() != null)) {
         orderResponse.setiValue(newOrder.getQty().getiValue());
         orderResponse.seteValue(newOrder.getQty().geteValue());
       } else if (orderResponse.getPromptId().equals("REFILLS") && (newOrder.getRefills() != null)) {
         orderResponse.setiValue(newOrder.getRefills().getiValue());
         orderResponse.seteValue(newOrder.getRefills().geteValue());
       } else if (orderResponse.getPromptId().equals("SC") && (newOrder.getSc() != null)) {
         orderResponse.setiValue(newOrder.getSc().getiValue());
         orderResponse.seteValue(newOrder.getSc().geteValue());
       } else if (orderResponse.getPromptId().equals("PICKUP") && (newOrder.getPickup() != null)) {
         orderResponse.setiValue(newOrder.getPickup().getiValue());
         orderResponse.seteValue(newOrder.getPickup().geteValue());
       } else if (orderResponse.getPromptId().equals("PI") && (newOrder.getPi() != null)) {
         orderResponse.setiValue(newOrder.getPi().getiValue());
         orderResponse.seteValue(newOrder.getPi().geteValue());
       } else if (orderResponse.getPromptId().equals("SIG") && (newOrder.getSig() != null)) {
         orderResponse.setiValue(newOrder.getSig().getiValue());
         orderResponse.seteValue(newOrder.getSig().geteValue());
       } else if (orderResponse.getPromptId().equals("COMMENT") && (newOrder.getComment() != null)) {
         orderResponse.setiValue(newOrder.getComment().getiValue());
         orderResponse.seteValue(newOrder.getComment().geteValue());
       } else if (orderResponse.getPromptId().equals("START") && (newOrder.getStart() != null)) {
         orderResponse.setiValue(newOrder.getStart().getiValue());
         orderResponse.seteValue(newOrder.getStart().geteValue());
       } else if (orderResponse.getPromptId().equals("STOP") && (newOrder.getStop() != null)) {
         orderResponse.setiValue(newOrder.getStop().getiValue());
         orderResponse.seteValue(newOrder.getStop().geteValue());
       } else if (orderResponse.getPromptId().equals("SETTING") && (newOrder.getSetting() != null)) {
         orderResponse.setiValue(newOrder.getSetting().getiValue());
         orderResponse.seteValue(newOrder.getSetting().geteValue());
       } else if (orderResponse.getPromptId().equals("PLACE") && (newOrder.getPlace() != null)) {
         orderResponse.setiValue(newOrder.getPlace().getiValue());
         orderResponse.seteValue(newOrder.getPlace().geteValue());
       } else if (orderResponse.getPromptId().equals("EARLIEST") && (newOrder.getEarliest() != null)) {
         orderResponse.setiValue(newOrder.getEarliest().getiValue());
         orderResponse.seteValue(newOrder.getEarliest().geteValue());
       }
     }
     
     // Get order checks
     List<String> oiList = this.buildOcItems(newOrder.getStartDtTm(), newOrder.getFillerId(), responseList);
     List<String> ocList = this.orderChecksOnAccept(newOrder.getPatientDfn(), newOrder.getEncLocation(), 
         newOrder.getFillerId(), newOrder.getStartDtTm(), oiList, "", "0");
     // Construct new order
     ConstructOrder constructOrder = new ConstructOrder();
     constructOrder.setDialogName(newOrder.getDialogName());
     constructOrder.setOrderItem(newOrder.getOrderId());
     constructOrder.setResponseList(responseList);
     constructOrder.setDgroup(dialog.getDisplayGroup());
     constructOrder.setOcList(ocList);
     
     // Save the new order
     return this.putNewOrder(newOrder.getPatientDfn(), newOrder.getEncProvider(), 
         newOrder.getEncLocation(), newOrder.getPatientSpecialty(), 
         newOrder.getEncDatetime(), constructOrder, "", "");
   }
   
   public OrderInfo putNewOrder(String patientDfn, String encProvider, String encLocation, String patientSpecialty,
       double encDatetime, ConstructOrder constructOrder, String orderSource, String editOf) throws Exception {

     setDefaultContext("OR CPRS GUI CHART");
     setDefaultRpcName("ORWDX SAVE");
     
     HashMap<String, String> param7 = setupORDialog(constructOrder.getResponseList(), constructOrder.getDialogName().equals("PSJI OR PAT FLUID OE"));
     if ((constructOrder.getLeadText() != null) && (constructOrder.getLeadText().length() > 0)) {
       param7.put("\"ORLEAD\"", constructOrder.getLeadText());
     }
     if ((constructOrder.getTrailText() != null) && (constructOrder.getTrailText().length() > 0)) {
       param7.put("\"ORTRAIL\"", constructOrder.getTrailText());
     }      
     if (constructOrder.getOcList() != null) {
       param7.put("\"ORCHECK\"", String.valueOf(constructOrder.getOcList().size()));
     } else {
       param7.put("\"ORCHECK\"", "0");
     }
     List<String> ocList = constructOrder.getOcList();
     for (int i = 0; i < ocList.size(); i++) {
       String y = "\"ORCHECK\",\"" + StringUtils.piece(ocList.get(i), 1) + "\",\"" + 
         StringUtils.piece(ocList.get(i), 3) + "\",\"" + (i+1) + "\"";
       String ocStr = StringUtils.pieces(ocList.get(i), '^', 2, 4);
       int len = ocStr.length();
       if (len > 255) {
         int numLoop = len / 255;
         int remain = len % 255;
         int inc = 0;
         while (inc <= numLoop) {
           String tmpStr = ocStr.substring(0, 254);
           ocStr = ocStr.substring(255, ocStr.length());
           param7.put(RpcRequest.buildMultipleMSubscriptKey(y + "," + inc + ",0"), tmpStr);
         }
         if (remain > 0) {
           param7.put(RpcRequest.buildMultipleMSubscriptKey(y + "," + inc + ",0"), ocStr);
         }
       } else {
         param7.put(y, ocStr);
       }
     }
     if (Arrays.asList(DELAY_EVENTS).contains(constructOrder.getDelayEvent())) {
       param7.put("\"OREVENT\"", constructOrder.getPtEventPtr());
     }
     if (constructOrder.getLogTime() > 0) {
       param7.put("\"ORSLOG\"", String.valueOf(constructOrder.getLogTime()));
     }
     if ((patientSpecialty == null) || patientSpecialty.equals("")) {
       patientSpecialty = "0";
     }
     param7.put("\"ORTS\"", patientSpecialty);
     
     java.util.Set<String> keys = param7.keySet();
     java.util.Iterator<String> it = keys.iterator();
     
     Object[] params = {patientDfn, encProvider, encLocation, constructOrder.getDialogName(), 
         constructOrder.getDgroup(), constructOrder.getOrderItem(), editOf, param7,
         constructOrder.getDigSig(), String.valueOf(encDatetime), orderSource, 
         String.valueOf(constructOrder.getIsEventDefaultOr())};
     List<String> list = lCall(params);
     if (list.size() == 0) {
       return null;
     }
     String x = list.get(0);
     String y = "";
     String z = "";
     while ((list.size() > 0) && (list.get(0).charAt(0) != '~') && (list.get(0).charAt(0) != '|')) {
       y += list.get(0).substring(1, list.get(0).length()) + "\r\n";
       list.remove(0);
     }
     if (y.length() > 0) {
       y = y.substring(0, y.length() - 2); // take off last CRLF
     }
     if ((list.size() > 0) && list.get(0).equals("|")) {
       list.remove(0);          
       while ((list.size() > 0) && (list.get(0).charAt(0) != '~') && (list.get(0).charAt(0) != '|')) {
         z += list.get(0).substring(1, list.get(0).length());
         list.remove(0);
       }
     }
     return setOrderFields(x, y, z);
   }
   
   private OrderInfo setOrderFields(String x, String y, String z) {
     OrderInfo order = new OrderInfo();
     String t = StringUtils.piece(x, 1);
     order.setId(t.substring(1, t.length()));
     order.setDGroup(StringUtils.piece(x, 2));
     order.setFmOrderDatetime(Double.valueOf(StringUtils.piece(x, 3)));
     order.setStartTimeStr(StringUtils.piece(x, 4));
     order.setStopTimeStr(StringUtils.piece(x, 5));
     order.setStatusIen(StringUtils.piece(x, 6));
     order.setSignature(StringUtils.toInt(StringUtils.piece(x, 7), 0));
     order.setVerNurse(StringUtils.piece(x, 8));
     order.setVerClerk(StringUtils.piece(x, 9));
     order.setChartRev(StringUtils.piece(x, 15));
     order.setProviderDuz(StringUtils.piece(x, 10));
     order.setProviderName(StringUtils.piece(x, 11));
     order.setProviderDEA(StringUtils.piece(x, 16));
     order.setProviderVA(StringUtils.piece(x, 17));
     order.setDigSigReq(StringUtils.piece(x, 18));
     t = StringUtils.piece(x, 13);
     order.setFlagged((t != null) ? t.equals("1") : false);
     order.setOrderLocIen(StringUtils.piece(StringUtils.piece(x, 19), ':', 2)); // IMO
     if (StringUtils.piece(StringUtils.piece(x, 19), ':', 1).equals("0;SC(")) {
       order.setOrderLocName("Unknown");
     } else {
       order.setOrderLocName(StringUtils.piece(StringUtils.piece(x, 19), ':', 1));
     }
     order.setText(y);
     order.setXmlText(z);
     try {
       DGroup dgroup = getSeqOfDGroup(order.getDGroup());
       order.setDGroupSeq(dgroup.getItem());
       order.setDGroupName(dgroup.getSubitem());
     } catch(Exception e) {}
     if (order.getText().indexOf("Entered in error") >= 0) {
       order.setEnteredInError(1);
     } else {
       order.setEnteredInError(0);
     }
     if (StringUtils.piece(x, 20).equals("1")) {
       order.setDcOriginalOrder(true);
     } else {
       order.setDcOriginalOrder(false);
     }
     if (StringUtils.piece(x, 21).equals("1")) {
       order.setIsOrderPendDc(true);
     } else {
       order.setIsOrderPendDc(false);
     }    
     if (StringUtils.piece(x, 22).equals("1")) {
       order.setIsDelayOrder(true);
     } else {
       order.setIsDelayOrder(false);
     }  
     
     return order;
   }
   
   private HashMap<String, String> setupORDialog(List<OrderResponse> responseList, boolean isIV) {
     int piIdx = 0;
     int odIdx = 0;
     String ivDuration;
     String ivDurVal;
     String thePI = "";
     String odTxt = "";
     for (int i = 0; i < responseList.size(); i++) {
       if (responseList.get(i).getPromptId().equals("SIG")) {
         odTxt = responseList.get(i).geteValue();
         odIdx = i;
       }
       if (responseList.get(i).getPromptId().equals("PI")) {
         thePI = responseList.get(i).geteValue();
       }
       if (thePI.trim().length() > 0) {
         piIdx = odTxt.indexOf(thePI);
       }
       if (piIdx > 0) {
         StringBuffer sb = new StringBuffer(odTxt);
         odTxt = sb.delete(piIdx, thePI.length()).toString();
         responseList.get(odIdx).seteValue(odTxt);
       }
       if (isIV && responseList.get(i).getPromptId().equals("DAYS")) {
         ivDuration = responseList.get(i).geteValue();
         if (ivDuration.length() > 1) {
           if ((ivDuration.toUpperCase().indexOf("TOTAL") > 0) ||
               (ivDuration.toUpperCase().indexOf("FOR") > 0)) {
             continue;
           }
           if (ivDuration.toUpperCase().indexOf("H") > 0) {
             ivDurVal = ivDuration.substring(0, ivDuration.length()-1);
             responseList.get(i).setIen("for " + ivDurVal + " hours");
           } else if (ivDuration.toUpperCase().indexOf("D") > 0) {
             if (ivDuration.toUpperCase().indexOf("DOSES") > 0) {
               ivDurVal = ivDuration.substring(0, ivDuration.length() - 5);
               responseList.get(i).setiValue("for a total of " + ivDurVal + " doses");
             } else {
               ivDurVal = ivDuration.substring(0, ivDuration.length() - 1);
               responseList.get(i).setiValue("for " + ivDurVal + " days");
             }
           } else if ((ivDuration.toUpperCase().indexOf("ML") > 0) || (ivDuration.toUpperCase().indexOf("CC") > 0)) {
             ivDurVal = ivDuration.substring(0, ivDuration.length() - 2);
             responseList.get(i).setiValue("with total volume " + ivDurVal + " ml");
           } else if (ivDuration.toUpperCase().indexOf("L") > 0) {
             ivDurVal = ivDuration.substring(0, ivDuration.length() - 1);
             responseList.get(i).setiValue("with total volume " + ivDurVal + " L");
           }
         }
       }
     }
     HashMap<String, String> map = new HashMap<String, String>();
     for (int i = 0; i < responseList.size(); i++) {
       OrderResponse resp = responseList.get(i);
       String subs = resp.getPromptIen() + "," + resp.getInstance();
       if (resp.getiValue().equals(TX_WPTYPE)) {
         if (resp.geteValue().length() > 74) {
           resp.seteValue(StringUtils.wrapLine(resp.geteValue(), 74));
         }
         map.put(subs, "ORDIALOG(\"WP\"," + subs + ")");
         String[] wpStrings = StringUtils.pieceList(resp.geteValue(), '\n');
         for (int j = 0; j < wpStrings.length; j++) {
           map.put(RpcRequest.buildMultipleMSubscriptKey("\"WP\"," + subs + "," + (j+1) + ",0"), wpStrings[j].trim());
         }
       } else {
         map.put(subs, resp.getiValue());
       }
     }
     return map;
   }  
  
}