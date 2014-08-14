package gov.va.med.lom.vistabroker.user.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.user.data.Notification;
import gov.va.med.lom.vistabroker.user.data.TiuAlertInfo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationsDao extends BaseDao {
	
  // CONSTRUCTORS
  public NotificationsDao() {
    super();
  }
  
  public NotificationsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API  
  public List<Notification> getNotifications() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWORB FASTUSER");
    List<String> list = lCall();
    List<Notification> notifications = new ArrayList<Notification>();
    for (String x : list) {
      // ^CLZHUXY,U (C4636)^^HIGH^2005/07/28@08:48^Order requires electronic signature.^^OR,10115121,12;8992;3050728.08481^1^
      Notification notification = new Notification();
      notification = new Notification();
      notification.setInfo(StringUtils.piece(x, 1));
      if (!StringUtils.piece(x, '^', 1).equalsIgnoreCase("Forwarded by:")) {
        for(int j = 2;j <= StringUtils.getCharCount(x, '^')+1; j++) {
          String y = StringUtils.piece(x, j);
          switch(j) {
            case 2 : notification.setPatient(y); break;
            case 3 : notification.setLocation(y); break;
            case 4 : notification.setUrgency(y); break;
            case 5 : try { 
                       Date dt = DateUtils.toDate(y, DateUtils.ANSI_SHORT_DATE_TIME_FORMAT2);
                       notification.setAlertDateTime(dt); 
                       notification.setAlertDateTimeStr(y);
                     } catch(ParseException pe) {}
                     break;
            case 6 : notification.setText(y); break;
            case 7 : notification.setForwardingInfo(y); break;
            case 8 : notification.setXqaid(y); 
                     notification.setOrderInfo(StringUtils.piece(y, ';', 1));
                     notification.setDfn(StringUtils.piece(y, ';', 2)); 
                     notification.setOrderType(StringUtils.piece(StringUtils.piece(y, ';', 1), ',', 1));
                     String s = StringUtils.piece(y, ';', 1); 
                     s = StringUtils.piece(s.substring(3, s.length()), ',', 1);
                     notification.setIen(s); 
                     notification.setFollowUp(StringUtils.piece(StringUtils.piece(y, ';', 1), ',', 3)); 
                     break;
            case 10: notification.setComment(y);
          }
        }
      } else {
        notification.setForwardingInfo(StringUtils.piece(x, 2));
        String y = StringUtils.piece(x, 3);
        if (y.length() > 0)
            y = "Fwd Comment: " + y;
        notification.setComment(y);
      }
      if (notification.getInfo().length() == 0) {
        String z = StringUtils.piece(notification.getXqaid(), ',', 1);
        if (z.length() > 3)
          z = z.substring(0, 3);
        notification.setInfo(z);
      }
      notifications.add(notification);
    }
    return notifications;
  }

  // Returns data associated with an alert
  public String getXQAData(String xqaid) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWORB GETDATA");
    return sCall(xqaid);
  }
  
  // Returns follow-up text for an alert
  public String getNotificationFollowUpText(String dfn, String ien, String xqaData) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWORB TEXT FOLLOWUP");
    Object[] params = {dfn, ien, xqaData};
    List<String> list = lCall(params);
    StringBuffer results = new StringBuffer();
    for (String s : list)
      results.append(s + '\n');
    return results.toString().trim();    
  }
  
  // Returns DFN and document type associated with a TIU alert
  /*
   * TIUDA^DFN^gui tab indicator
   * TIUDA is the document IEN in ^TIU(8925
   * DFN is the patient IEN
   * gui tab indicator is an arbitrarily set constant based on the document type.
   */
  public TiuAlertInfo getTIUAlertInfo(String xqaid) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU GET ALERT INFO"); 
    String x = sCall(xqaid);
    TiuAlertInfo tiuAlertInfo = new TiuAlertInfo();
    tiuAlertInfo.setTiuda(StringUtils.piece(x, 1));
    tiuAlertInfo.setDfn(StringUtils.piece(x, 2));
    tiuAlertInfo.setGuiTabIndicator(StringUtils.piece(x, 3));
    return tiuAlertInfo;
  }
  
  public boolean deleteAlert(String xqaid) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORB DELETE ALERT");
    String x = sCall(xqaid);
    return StringUtils.strToBool(x, "true");
  }
  
  // Forwards an alert with comment to Recip[ient]
  public String forwardAlert(String xqaid, String duz, String fwdType, String comment) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORB FORWARD ALERT");
    Object[] params = {xqaid, duz, fwdType, comment};
    return sCall(params);  
  }
  
}