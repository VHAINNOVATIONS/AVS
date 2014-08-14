package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Calendar;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.user.VistaSignonRpc;
import gov.va.med.lom.javaBroker.rpc.user.VistaSignonSetupRpc;
import gov.va.med.lom.javaBroker.rpc.user.models.VistaSignonResult;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class OrdersRpc extends AbstractRpc {
  
  private static HashMap dGroupMap = new HashMap();
  
  // STATUS IDENTIFIERS
  public static final int STS_ACTIVE       = 2;
  public static final int STS_DISCONTINUED = 3;
  public static final int STS_COMPLETE     = 4;
  public static final int STS_EXPIRING     = 5;
  public static final int STS_RECENT       = 6;
  public static final int STS_UNVERIFIED   = 8;
  public static final int STS_UNVER_NURSE  = 9;
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
 
  // ORDER DELAY EVENT CODES
  private static final char[] DELAY_EVENTS = {'A','D','T','M','O'};
  
  // WORD PROCESSING FIELD
  public static final String TX_WPTYPE = "^WP^";
  
  
  // CONSTRUCTORS
  public OrdersRpc() throws BrokerException {
    super();
  }
  
  public OrdersRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  
  // Returns the status of an order given the status ien.
  public static String getNameOfStatus(String ien) {
    String name = null;
    int n = Integer.valueOf(ien);
    switch(n) {
      case 0 : name = "error"; break;
      case 1 : name = "discontinued"; break;
      case 2 : name = "complete"; break;
      case 3 : name = "hold"; break;
      case 4 : name = "flagged"; break;
      case 5 : name = "pending"; break;
      case 6 : name = "active"; break;
      case 7 : name = "expired"; break;
      case 8 : name = "scheduled"; break;
      case 9 : name = "partial results"; break;
      case 10 : name = "delayed"; break;
      case 11 : name = "unreleased"; break;
      case 12 : name = "dc/edit"; break;
      case 13 : name = "cancelled"; break;
      case 14 : name = "lapsed"; break;
      case 15 : name = "renewed"; break;
      case 97 : name = ""; break;       // null status, used for 'No Orders Found.
      case 98 : name = "new"; break;
      case 99 : name = "no status"; break;
    }
    return name;
  }  
  
  // RPC API  
  
  /*
   * Returns the details of a lab order.
   */
  public synchronized String getLabOrderDetails(String dfn, String orderId) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {dfn, orderId};
      ArrayList list = lCall("ORQQLR DETAIL", params);
      StringBuffer results = new StringBuffer();
      for (int i = 0; i < list.size(); i++)
        results.append((String)list.get(i) + '\n');
      return results.toString();
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   * Retrieves the user's default view for the orders tab.
   * Example Results:  
   *   0;0;2;1;L;R;1;Active Orders (includes Pending & Recent Activity) - ALL SERVICES
   */
  public synchronized OrderView getOrderViewDefault() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("ORWOR VWGET");
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
      if (returnRpcResult)
        orderView.setRpcResult(x);    
      return orderView;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   * Returns detailed order information.
   */
  public synchronized String getOrderDetails(String orderId, String patientDfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {orderId, patientDfn};
      ArrayList list = lCall("ORQOR DETAIL", params);
      StringBuffer results = new StringBuffer();
      for (int i = 0; i < list.size(); i++)
        results.append((String)list.get(i) + '\n');
      return results.toString();
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  /*
   * Returns results of a CPRS order.
   */
  public synchronized String getOrderResult(String dfn, String orderId) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {dfn, orderId, orderId};
      ArrayList list = lCall("ORWOR RESULT", params);
      StringBuffer results = new StringBuffer();
      for (int i = 0; i < list.size(); i++)
        results.append((String)list.get(i) + '\n');
      return results.toString();
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
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
  public synchronized String[] getDGroupMap() throws BrokerException {
    String[] dGroup = (String[])dGroupMap.get(rpcBroker.getStationNo());
    if (dGroup == null) {
      if (setContext("OR CPRS GUI CHART")) {
        ArrayList list = lCall("ORWORDG MAPSEQ");  
        dGroup = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
          dGroup[i] = (String)list.get(i);
        dGroupMap.put(rpcBroker.getStationNo(), dGroup);
      } else
        throw getCreateContextException("OR CPRS GUI CHART");
    }
    return dGroup;
  } 
  
  /*
   * For a given IEN, returns the group.
   */
  public synchronized DGroup getSeqOfDGroup(String ien) throws BrokerException {
    String[] dGroup = getDGroupMap();
    String value = null;
    for (int i = 0; i < dGroup.length; i++) {
      String k = StringUtils.piece(dGroup[i], '=', 1);
      if (k.equals(ien)) {
        value = StringUtils.piece(dGroup[i], '=', 2);
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
  public synchronized OrderSheetList getOrderSheets(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORWOR SHEETS", dfn); 
      OrderSheet[] orderSheets = new OrderSheet[list.size()];
      for (int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        orderSheets[i].setCode(StringUtils.piece(x, 1));
        orderSheets[i].setId(StringUtils.piece(x, 2));
        orderSheets[i].setName(StringUtils.piece(x, 3));
        if (returnRpcResult)
          orderSheets[i].setRpcResult(x);   
      }
      OrderSheetList orderSheetList = new OrderSheetList();
      orderSheetList.setOrderSheets(orderSheets);
      return orderSheetList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   * Returns the order fields for a list of orders for the user's default view.
   */
  public synchronized OrdersInfoList getOrdersListForDefaultView(String dfn) throws BrokerException {
    OrderView orderView = getOrderViewDefault();
    return getOrdersList(dfn, orderView);
  }
  
  public synchronized OrdersInfoList getOrdersList(String dfn, OrderView orderView) throws BrokerException {
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
  public synchronized OrdersInfoList getOrdersList(String dfn, OrderView orderView, 
                                                   Calendar startDate) throws BrokerException {
    return getOrdersList(dfn, orderView, startDate, true);
  }
  
  public synchronized OrdersInfoList getOrdersList(String dfn, OrderView orderView, 
                                                   Calendar startDate, 
                                                   boolean checkUrgency) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      OrdersInfoList ordersInfoList = getOrdersAbbr(dfn, orderView);
      ArrayList idList = new ArrayList();
      OrderInfo[] ordersInfo = ordersInfoList.getOrdersInfo();
      for (int i = 0; i < ordersInfoList.getCount(); i++)
        idList.add(ordersInfo[i].getId());
      Object[] params = {String.valueOf(ordersInfoList.getTextView()), 
                         String.valueOf(ordersInfoList.getFmDatetime()), idList};
      ArrayList list = lCall("ORWORR GET4LST", params);
      int orderIndex = -1;
      StringBuffer orderData = new StringBuffer();
      StringBuffer text = new StringBuffer();
      StringBuffer xmlText = new StringBuffer();
      Vector ordersVect = new Vector();
      OrderInfo orderInfo = null;
      int lineIndex = 0;
      while (lineIndex < list.size()) {
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
          orderInfo.setId(ordersInfo[orderIndex].getId());
          orderInfo.setEventPtr(ordersInfo[orderIndex].getEventPtr());
          orderInfo.setEventName(ordersInfo[orderIndex].getEventName());
          orderInfo.setDGroupSeq(ordersInfo[orderIndex].getDGroupSeq());
          orderInfo.setDGroupName(ordersInfo[orderIndex].getDGroupName());
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
          if (returnRpcResult) {
            if (orderData.length() > 0)
              orderData.deleteCharAt(orderData.length()-1); // take off last newline char
            orderInfo.setRpcResult(orderData.toString());
          }
          orderData.delete(0, orderData.length());
          if ((startDate == null) || (date.getTime() >= startDate.getTimeInMillis()))
            ordersVect.add(orderInfo);
          lineIndex++;
        }
      }
      OrdersInfoList ordersList = new OrdersInfoList();
      ordersList.setCount(ordersVect.size());
      ordersList.setTextView(ordersInfoList.getTextView());
      ordersList.setFmDatetime(ordersInfoList.getFmDatetime());
      OrderInfo[] orders = new OrderInfo[ordersVect.size()];
      for(int i = 0; i < orders.length; i++)
        orders[i] = (OrderInfo)ordersVect.get(i);
      ordersList.setOrdersInfo(orders);
      return ordersList;
   } else
     throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  public synchronized OrdersInfoList getOrdersAbbrForDefaultView(String dfn) throws BrokerException {
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
  public synchronized OrdersInfoList getOrdersAbbr(String dfn, OrderView orderView) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String filterTS = String.valueOf(orderView.getFilter()) + "^" + 
                        String.valueOf(orderView.getEventDelaySpecialty());
      Object[] params = {dfn, filterTS, String.valueOf(orderView.getDGroup()),
                         String.valueOf(orderView.getFmTimeFrom()), String.valueOf(orderView.getFmTimeThru()), "C;0"};
      ArrayList list = lCall("ORWORR AGET", params);   
      OrdersInfoList ordersInfoList = new OrdersInfoList();
      if (list.size() > 0) {
        String x = (String)list.get(0);
        int num = StringUtils.toInt(StringUtils.piece(x, 1), 0);
        ordersInfoList.setTextView(StringUtils.toInt(StringUtils.piece(x, 2), 0));
        ordersInfoList.setFmDatetime(StringUtils.toDouble(StringUtils.piece(x, 3), 0));
        if (num > 0) {
          Vector ordersInfoVect = new Vector();
          for (int i = 1; i <= num; i++) {
            OrderInfo orderInfo = new OrderInfo();
            if (i < list.size()) {
              x = (String)list.get(i);
              orderInfo = new OrderInfo();
              orderInfo.setId(StringUtils.piece(x, 1));
              orderInfo.setDGroup(StringUtils.piece(x, 2));
              orderInfo.setFmOrderDatetime(StringUtils.toDouble(StringUtils.piece(x, 3), 0));
              Date date = FMDateUtils.fmDateTimeToDate(orderInfo.getFmOrderDatetime());
              if (date != null) {
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(date);
                orderInfo.setOrderDatetime(gc);
                try {
                  orderInfo.setOrderDatetimeStr(FMDateUtils.fmDateTimeToEnglishDateTime(orderInfo.getFmOrderDatetime()));
                } catch(ParseException pe) {}
              }
              orderInfo.setEventPtr(StringUtils.piece(x, 4));
              orderInfo.setEventName(StringUtils.piece(x, 5));
              DGroup dgroup = getSeqOfDGroup(orderInfo.getDGroup());
              if (dgroup != null) {
                orderInfo.setDGroupSeq(dgroup.getIen());
                orderInfo.setDGroupName(dgroup.getItem());
              }
              if (returnRpcResult)
                orderInfo.setRpcResult(x);   
            }
            ordersInfoVect.add(orderInfo);
          }
          ordersInfoList.setCount(ordersInfoVect.size());
          OrderInfo[] ordersInfo = new OrderInfo[ordersInfoVect.size()];
          for(int i = 0; i < ordersInfoVect.size(); i++)
            ordersInfo[i] = (OrderInfo)ordersInfoVect.get(i);
          ordersInfoList.setOrdersInfo(ordersInfo);
        }
      }
      return ordersInfoList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   * Returns the IDs of outstanding unsigned orders.
   */
  public synchronized String[] getUnsignedOrders(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Mult mult = new Mult();
      mult.setMultiple("0", "");
      Object[] params = {dfn, mult};
      ArrayList list = lCall("ORWOR UNSIGN", params);  
      String[] idList = new String[list.size()];
      for (int i = 0; i < list.size(); i++)
        idList[i] = (String)list.get(i);
      return idList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
  public synchronized String getDisplayGroupIen(String group) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {group};
      return sCall("ORWORDG IEN", params);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   * Parses the detail text of an order and returns an object
   * that encapsulates the detailed status of the order.
   */
  public synchronized OrderStatus parseOrderDetail(String orderId, String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {orderId, dfn};
      ArrayList list = lCall("ORQOR DETAIL", params);
      OrderStatus orderStatus = new OrderStatus();
      StringBuffer sb = new StringBuffer();
      int n = list.size();
      for(int i = 0; i < n; i++) {
        String x = ((String)list.get(i)).toUpperCase();
        if (returnRpcResult)
          sb.append(x);
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
      if (returnRpcResult)
        orderStatus.setRpcResult(sb.toString());
      return orderStatus;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }

  /*
   * Parses the detail text of an order and returns the urgency text.
   */
  public synchronized String getOrderUrgency(String orderId, String dfn) throws BrokerException {
	  if (setContext("OR CPRS GUI CHART")) {
      // first try calling the rpc using the standard context
      if (orderUrgencyRpcContextState == ORDER_URGENCY_RPC_EXISTS) {
        if (orderUrgencyRpcContext == null)
          orderUrgencyRpcContext = "OR CPRS GUI CHART";
        try {
          return doGetOrderUrgency(orderId, orderUrgencyRpcContext);
        } catch(BrokerException be1) {
          if (be1.getCode() == 1) {
            // rpc isn't registered to standard context, try local context
            try {
              logWarning("'ALSI URGENCY OF ORDER' is not registered to '" + orderUrgencyRpcContext + "' context.");
              orderUrgencyRpcContext = "ALS CLINICAL RPC";
              return doGetOrderUrgency(orderId, orderUrgencyRpcContext);
            } catch(BrokerException be2) {
              logWarning("'ALSI URGENCY OF ORDER' is not registered to '" + orderUrgencyRpcContext + "' context.");
              orderUrgencyRpcContext = "";
              orderUrgencyRpcContextState = ORDER_URGENCY_RPC_NOT_EXISTS;
              if( (be2.getCode() == 1) || (be2.getCode() == 2 )) 
            	return parseOrderUrgency(orderId, dfn);
              else 
                throw be2;
            }
          } else
            throw be1;
        }
      } else
        return parseOrderUrgency(orderId, dfn);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  private synchronized String doGetOrderUrgency(String orderId, String context) throws BrokerException {
    if (setContext(context)) {   
      orderId = StringUtils.piece(orderId, ';', 1);
      Object[] params = {orderId};
      String urgency = null;
      try {
        urgency = sCall("ALSI URGENCY OF ORDER", params);
      } catch(BrokerException be) {
        throw new BrokerException(null, "rpc", 1, "XWB RPC NOT REG", 
           "ALSI URGENCY OF ORDER is not registered in the context " + context, 
           "ALSI URGENCY OF ORDER", null, null, rpcBroker.getServer(), 
           rpcBroker.getListenerPort());
      }
      if (urgency.equals("0"))
        urgency = "";
      return urgency;  
    } else {
      throw new BrokerException(null, "context", 2, "XWB RPC NOT REG", 
                                "Application context could not be created: ALS CLINICAL RPC", 
                                "ALSI URGENCY OF ORDER", null, null, rpcBroker.getServer(), 
                                rpcBroker.getListenerPort());
    }    
  }
  
  /*
   * Parses the detail text of an order and returns the urgency text.
   */
  public synchronized String parseOrderUrgency(String orderId, String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params2 = {orderId, String.valueOf(dfn)};
      ArrayList list = lCall("ORQOR DETAIL", params2);
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
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  /*
   * Returns the status of an order.
   */
  public synchronized int getOrderStatus(String orderId) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {orderId};
      String x = sCall("OREVNTX1 GETSTS", params);
      return StringUtils.toInt(x, 1);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   * Return sample, specimen, & urgency info about a lab test.
   */
  public synchronized LabTest getLabTestOrderData(String labTestIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {labTestIen};
      List<String> list = lCall("ORWDLR32 LOAD", params);
      LabTest labTest = new LabTest();
      labTest.setTestId(labTestIen);
      labTest.setTestName(StringUtils.piece(StringUtils.extractDefault(list, "Test Name"), 1));
      labTest.setItemId(StringUtils.piece(StringUtils.extractDefault(list, "Item ID"), 1));
      labTest.setLabSubscript(StringUtils.piece(StringUtils.extractDefault(list, "Item ID"), 2));
      labTest.setTestReqComment(StringUtils.extractDefault(list, "ReqCom"));
      String x = StringUtils.extractDefault(list, "Unique CollSamp");
      labTest.setUniqueCollSamp(x.length() > 0);
      if (x.length() == 0) {
        x = StringUtils.extractDefault(list, "Lab CollSamp");
      }
      if (x.length() == 0) {
        x = StringUtils.extractDefault(list, "Default CollSamp");
      }
      if (x.length() == 0) {
        x = "-1";
      }
      labTest.setDftlCollSamp(x);
      labTest.setSpecimenList(StringUtils.extractItems(list, "Specimens"));
      if (StringUtils.extractDefault(list, "Default Urgency").length() > 0) {
        labTest.setUrgencyList(StringUtils.extractItems(list, "Default Urgency"));
      } else {
        labTest.setUrgencyList(StringUtils.extractItems(list, "Urgencies"));
      }
      labTest.setCurWardComment(StringUtils.extractText(list, "GenWardInstructions"));
      return labTest;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String alertOrder(String orderIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART"))
      return sCall("ALSLR ORDER ALERT", orderIen);
    else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   * Set order to send an alert when the order is resulted.
   */
  public String alertOrderForRecipient(String orderIen, String recipientDuz) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {orderIen, recipientDuz};
      return sCall("ORWDXA ALERT", params);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized LockDocumentResult lockPatient(String patientDfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {    
      LockDocumentResult lockDocumentResult = new LockDocumentResult();
      String x = sCall("ORWDX LOCK", patientDfn);
      if (returnRpcResult)
        lockDocumentResult.setRpcResult(x);
      if (x.charAt(0) == '1') {
        lockDocumentResult.setSuccess(true);
        lockDocumentResult.setMessage("");
      } else {
        lockDocumentResult.setSuccess(false);
        lockDocumentResult.setMessage(StringUtils.piece(x, 2));      
      }
      return lockDocumentResult;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  public synchronized void unlockPatient(String patientDfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {    
       sCall("ORWDX UNLOCK", patientDfn);
    } else {
      throw getCreateContextException("OR CPRS GUI CHART");
    }
  }  
  
  public synchronized LockDocumentResult lockOrder(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {    
      LockDocumentResult lockDocumentResult = new LockDocumentResult();
      String x = sCall("ORWDX LOCK ORDER", ien);
      if (returnRpcResult)
        lockDocumentResult.setRpcResult(x);
      if (x.charAt(0) == '1') {
        lockDocumentResult.setSuccess(true);
        lockDocumentResult.setMessage("");
      } else {
        lockDocumentResult.setSuccess(false);
        lockDocumentResult.setMessage(StringUtils.piece(x, 2));      
      }
      return lockDocumentResult;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized String unlockOrder(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART"))    
      return sCall("ORWDX UNLOCK ORDER", ien);
    else
      throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  public synchronized int dcOrder(String orderId, String providerDuz, String locationIen, boolean dcOrigOrder, 
                                  int reason, boolean newOrder) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String dcOrigOrderStr = StringUtils.boolToStr(dcOrigOrder, "1", "0");
      String newOrderStr = StringUtils.boolToStr(newOrder, "1", "0");
      Object[] params = {orderId, providerDuz, locationIen, String.valueOf(reason), dcOrigOrderStr, newOrderStr};
      
      String results =  sCall("ORWDXA DC", params);
      // returns d/c type
      return StringUtils.toInt(StringUtils.piece(results, 14), 0);
    } else {
      throw getCreateContextException("OR CPRS GUI CHART");
    }
  }
  
  public synchronized OrderMenu loadOrderMenu(String menuIen) throws BrokerException {
    
    OrderMenu orderMenu = new OrderMenu();
    if (setContext("OR CPRS GUI CHART")) {    
      List<String> list = lCall("ORWDXM MENU", menuIen);
      if (list.size() > 0) {
        StringBuffer sb = new StringBuffer(list.get(0) + "\n");
        orderMenu.setTitle(StringUtils.piece(list.get(0), 1));
        int index = orderMenu.getTitle().indexOf('&');
        if ((index >= 0) && (!orderMenu.getTitle().substring(index+1, index+2).equals('&'))) {
          orderMenu.setTitle(orderMenu.getTitle().substring(0, index) + '&' + 
              orderMenu.getTitle().substring(index + 1, orderMenu.getTitle().length()-1));
        }
        orderMenu.setNumCols(StringUtils.toInt(StringUtils.piece(list.get(0), 2), 1));
        orderMenu.setKeyVars(StringUtils.pieces(list.get(0), '^', 6, 16));
        orderMenu.setMenuItems(new ArrayList<OrderMenuItem>());
        for (int i = 1; i < list.size(); i++) {
          sb.append(list.get(i) + "\n");
          OrderMenuItem menuItem = new OrderMenuItem();
          menuItem.setCol(StringUtils.toInt(StringUtils.piece(list.get(i), 1), 0) - 1);
          menuItem.setRow(StringUtils.toInt(StringUtils.piece(list.get(i), 2), 0) - 1);
          if (StringUtils.piece(list.get(i), 3).length() > 0) {
            menuItem.setDlgType(StringUtils.piece(list.get(i), 3).charAt(0));
          }
          menuItem.setIen(StringUtils.piece(list.get(i), 4));
          menuItem.setFormId(StringUtils.piece(list.get(i), 5));
          menuItem.setAutoAck(StringUtils.piece(list.get(i), 6).equals("1"));
          menuItem.setItemText(StringUtils.piece(list.get(i), 7));
          menuItem.setMnemonic(StringUtils.piece(list.get(i), 8));
          menuItem.setCol(StringUtils.toInt(StringUtils.piece(list.get(i), 9), 0));
          orderMenu.getMenuItems().add(menuItem);
        }
        orderMenu.setRpcResult(sb.toString());
      }
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
    
    return orderMenu;
    
  }
  
  public synchronized List<OrderDialogPrompt> loadDialogDefinition(String dialogName) throws BrokerException {
    List<OrderDialogPrompt> prompts = new ArrayList<OrderDialogPrompt>();
    if (setContext("OR CPRS GUI CHART")) {    
      List<String> list = lCall("ORWDX DLGDEF", dialogName);
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
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
    return prompts;
  }
  
  /*
    returns a pointer to a list of orderable items matching an S.xxx cross reference (for use in
    a long list box) 
   */
  public synchronized List<String> subsetOfOrderItems(String startFrom, int dir, String xref) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {    
      String[] params = {startFrom, String.valueOf(dir), xref};
      return lCall("ORWDX ORDITM", params);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /*
   Returns init values for laboratory dialog.  The results must be used immediately.
   */
  public synchronized List<String> odForLab(String location, String division) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {    
      String[] params = {location, division};
      return lCall("ORWDLR32 DEF", params);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  
  public synchronized OrderDialogResolved buildReponses(String patientDfn, String encLocation, String encProvider, boolean inpatient, 
     String patientSex, int patientAge, int scPercent, String keyVars, String inputId, boolean forImo) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {    
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
      List<String> list = lCall("ORWDXM1 BLDQRSP", params);
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
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized List<OrderResponse> loadResponses(String orderId, boolean xferOutToInOnMeds, boolean xferInToOutNow) throws BrokerException {
    List<OrderResponse> responses = new ArrayList<OrderResponse>();
    if (setContext("OR CPRS GUI CHART")) {    
      String transfer = StringUtils.boolToStr((xferOutToInOnMeds || xferInToOutNow) && orderId.charAt(0) == 'C', "1", "0"); 
      String[] params = {orderId, transfer};
      List<String> list = lCall("ORWDX LOADRSP", params);
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
      
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
    
    return responses;
  }
  
  public synchronized List<String> buildOcItems(String startDtTm, String fillerId, 
      List<OrderResponse> orderResponses) throws BrokerException {
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
    
  public synchronized List<String> orderChecksOnAccept(String patientDfn, String encLocation, String fillerId, String startDtTm, 
      List<String> oiList, String dupORIFN, String renewal) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {    
      if ((oiList != null) && (oiList.size() > 0)) {
        Object[] params = {patientDfn, fillerId, startDtTm, encLocation, oiList, dupORIFN, renewal};
        return lCall("ORWDXC ACCEPT", params); 
      } else {
        String[] params = {patientDfn, fillerId, startDtTm, encLocation};
        return lCall("ORWDXC ACCEPT", params); 
      }
    }
    return null;
  }
  
  public synchronized OrderInfo putNewOrder(NewOrder newOrder) throws BrokerException {
    
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
  
  public synchronized OrderInfo putNewOrder(String patientDfn, String encProvider, String encLocation, String patientSpecialty,
      double encDatetime, ConstructOrder constructOrder, String orderSource, String editOf) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      
      Mult param7 = setupORDialog(constructOrder.getResponseList(), constructOrder.getDialogName().equals("PSJI OR PAT FLUID OE"));
      if ((constructOrder.getLeadText() != null) && (constructOrder.getLeadText().length() > 0)) {
        param7.setMultiple("\"ORLEAD\"", constructOrder.getLeadText());
      }
      if ((constructOrder.getTrailText() != null) && (constructOrder.getTrailText().length() > 0)) {
        param7.setMultiple("\"ORTRAIL\"", constructOrder.getTrailText());
      }      
      if (constructOrder.getOcList() != null) {
        param7.setMultiple("\"ORCHECK\"", String.valueOf(constructOrder.getOcList().size()));
      } else {
        param7.setMultiple("\"ORCHECK\"", "0");
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
            param7.setMultiple(y + "," + inc, tmpStr);
          }
          if (remain > 0) {
            param7.setMultiple(y + "," + inc, ocStr);
          }
        } else {
          param7.setMultiple(y, ocStr);
        }
      }
      if (Arrays.asList(DELAY_EVENTS).contains(constructOrder.getDelayEvent())) {
        param7.setMultiple("\"OREVENT\"", constructOrder.getPtEventPtr());
      }
      if (constructOrder.getLogTime() > 0) {
        param7.setMultiple("\"ORSLOG\"", String.valueOf(constructOrder.getLogTime()));
      }
      if ((patientSpecialty == null) || patientSpecialty.equals("")) {
        patientSpecialty = "0";
      }
      param7.setMultiple("\"ORTS\"", patientSpecialty);
      
      for (int i = 0; i < param7.getCount(); i++) {
        System.out.println(param7.getMultiple(i)[0] + " = " + param7.getMultiple(i)[1]);
      }
      
      Object[] params = {patientDfn, encProvider, encLocation, constructOrder.getDialogName(), 
          constructOrder.getDgroup(), constructOrder.getOrderItem(), editOf, param7,
          constructOrder.getDigSig(), String.valueOf(encDatetime), orderSource, String.valueOf(constructOrder.getIsEventDefaultOr())};
      List<String> list = lCall("ORWDX SAVE", params);
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
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
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
  
  private Mult setupORDialog(List<OrderResponse> responseList, boolean isIV) {
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
    Mult mult = new Mult();
    for (int i = 0; i < responseList.size(); i++) {
      OrderResponse resp = responseList.get(i);
      String subs = resp.getPromptIen() + "," + resp.getInstance();
      if (resp.getiValue().equals(TX_WPTYPE)) {
        if (resp.geteValue().length() > 74) {
          resp.seteValue(StringUtils.wrapLine(resp.geteValue(), 74));
        }
        String x = "ORDIALOG(\"WP\"," + subs + ")";
        mult.setMultiple(subs, x);
        String[] wpStrings = StringUtils.pieceList(resp.geteValue(), '\n');
        for (int j = 0; j < wpStrings.length; j++) {
          x =  "\"WP\"," + subs + "," + (j+1) + ",0";
          mult.setMultiple(x, wpStrings[j]);
        }
      } else {
        mult.setMultiple(subs, resp.getiValue());
      }
    }
    
    return mult;
    
  }

}