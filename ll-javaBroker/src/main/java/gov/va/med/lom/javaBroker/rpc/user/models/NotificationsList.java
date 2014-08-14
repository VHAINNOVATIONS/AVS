package gov.va.med.lom.javaBroker.rpc.user.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class NotificationsList extends BaseBean implements Serializable {

  private Notification[] notifications;
  
  public NotificationsList() {
    this.notifications = null;
  }
  
  public Notification[] getNotifications() {
    return notifications;
  }
  
  public void setNotifications(Notification[] notifications) {
    this.notifications = notifications;
  }
}
