package gov.va.med.lom.javaBroker.rpc.user;

import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class NotificationsRpc extends AbstractRpc {
	
  // FIELDS
	private NotificationsList notificationsList;
		
  // CONSTRUCTORS
  public NotificationsRpc() throws BrokerException {
    super();
  }
  
  public NotificationsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API  
  public synchronized NotificationsList getNotifications() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
    ArrayList list = lCall("ORWORB FASTUSER");
    String[] results = new String[list.size()];
    Notification[] notifications = new Notification[results.length];
    for (int i = 0; i < results.length; i++) {
      String x = (String)list.get(i);
      // ^CLZHUXY,U (C4636)^^HIGH^2005/07/28@08:48^Order requires electronic signature.^^OR,10115121,12;8992;3050728.08481^1^
      notifications[i] = new Notification();
      notifications[i].setInfo(StringUtils.piece(x, 1));
      if (!StringUtils.piece(x, '^', 1).equalsIgnoreCase("Forwarded by:")) {
        for(int j = 2;j <= StringUtils.getCharCount(x, '^')+1; j++) {
          String y = StringUtils.piece(x, j);
          switch(j) {
            case 2 : notifications[i].setPatient(y); break;
            case 3 : notifications[i].setLocation(y); break;
            case 4 : notifications[i].setUrgency(y); break;
            case 5 : try { 
                       Date dt = DateUtils.toDate(y, DateUtils.ANSI_SHORT_DATE_TIME_FORMAT2);
                       notifications[i].setAlertDateTime(dt); 
                       notifications[i].setAlertDateTimeStr(y);
                     } catch(ParseException pe) {}
                     break;
            case 6 : notifications[i].setText(y); break;
            case 7 : notifications[i].setForwardingInfo(y); break;
            case 8 : notifications[i].setXqaid(y); 
                     notifications[i].setOrderInfo(StringUtils.piece(y, ';', 1));
                     notifications[i].setDfn(StringUtils.piece(y, ';', 2)); 
                     notifications[i].setOrderType(StringUtils.piece(StringUtils.piece(y, ';', 1), ',', 1));
                     String s = StringUtils.piece(y, ';', 1); 
                     s = StringUtils.piece(s.substring(3, s.length()), ',', 1);
                     notifications[i].setIen(s); 
                     notifications[i].setFollowUp(StringUtils.toInt(StringUtils.piece(StringUtils.piece(y, ';', 1), ',', 3), 0)); 
                     break;
            case 10: notifications[i].setComment(y);
          }
        }
      } else {
        notifications[i].setForwardingInfo(StringUtils.piece(x, 2));
        String y = StringUtils.piece(x, 3);
        if (y.length() > 0)
            y = "Fwd Comment: " + y;
        notifications[i].setComment(y);
      }
      if (notifications[i].getInfo().length() == 0) {
        String z = StringUtils.piece(notifications[i].getXqaid(), ',', 1);
        if (z.length() > 3)
          z = z.substring(0, 3);
        notifications[i].setInfo(z);
      }
      if (returnRpcResult)
        notifications[i].setRpcResult(x);
    }
    notificationsList = new NotificationsList();
    notificationsList.setNotifications(notifications);
    return notificationsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }

  /*
  public synchronized String getUserNotifications(long duz, Date fromDate, Date toDate) throws BrokerException {
	  if (setContext("OR CPRS GUI CHART")) {
		  double fmDate1 = 0;
		  if (fromDate != null)
			  fmDate1 = FMDateUtils.dateToFMDate(fromDate);
		  double fmDate2 = 0;
		  if (toDate != null)
			  fmDate2 = FMDateUtils.dateToFMDate(toDate);    
		  Object[] params = {duz, String.valueOf(fmDate1), String.valueOf(fmDate2)};
		  ArrayList list = lCall("ORQQXQA USER", params);
		  String[] results = new String[list.size()];
		  for (int i = 0; i < results.length; i++) {
			  // FIXME:  ?????
			  // what is this supposed to do? 
			  //String x = (String)list.get(i);
		  }
		  return "";
	  } else
		  throw getCreateContextException("OR CPRS GUI CHART");
  }
  */
  
  // Returns data associated with an alert
  public synchronized String getXQAData(String xqaid) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
    Object[] params = {xqaid};
    return sCall("ORWORB GETDATA", params);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns follow-up text for an alert
  public synchronized String getNotificationFollowUpText(long dfn, long ien, String xqaData) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {dfn, ien, xqaData};
      ArrayList list = lCall("ORWORB TEXT FOLLOWUP", params);
      StringBuffer results = new StringBuffer();
      for (int i = 0; i < list.size(); i++)
        results.append((String)list.get(i) + '\n');
      return results.toString();    
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns DFN and document type associated with a TIU alert
  /*
   * TIUDA^DFN^gui tab indicator
   * TIUDA is the document IEN in ^TIU(8925
   * DFN is the patient IEN
   * gui tab indicator is an arbitrarily set constant based on the document type.
   */
  public synchronized TiuAlertInfo getTIUAlertInfo(String xqaid) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {xqaid};
      String x = sCall("TIU GET ALERT INFO", params);
      TiuAlertInfo tiuAlertInfo = new TiuAlertInfo();
      tiuAlertInfo.setTiuda(StringUtils.toInt(StringUtils.piece(x, 1), 0));
      tiuAlertInfo.setDfn(StringUtils.piece(x, 2));
      tiuAlertInfo.setGuiTabIndicator(StringUtils.piece(x, 3));
      return tiuAlertInfo;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized boolean deleteAlert(String xqaid) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {xqaid};
      String x = sCall("ORB DELETE ALERT", params);
      return StringUtils.strToBool(x, new String[] {"true"});
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Forwards an alert with comment to Recip[ient]
  public synchronized String forwardAlert(String xqaid, long duz, String fwdType, String comment) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {xqaid, duz, fwdType, comment};
      return sCall("ORB FORWARD ALERT", params);  
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
}