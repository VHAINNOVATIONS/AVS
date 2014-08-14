/*
 * ExampleUserNotificationsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Displays a list of notifications for the user.
 *
 * Usage: java ExampleUserNotificationsRpc AUTH_PROPS
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;
 
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

public class ExampleUserNotificationsRpc {
  
  /*
   * Prints a list of providers. 
   */
  public static void main(String[] args) {
    // If user didn't pass the name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleUserNotificationsRpc AUTH_PROPS");
      System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      server = res.getString("server");
      port = Integer.valueOf(res.getString("port")).intValue();
      access = res.getString("accessCode");
      verify = res.getString("verifyCode");
    }       
    try {
      // Call the static signon method and get an instance of the vista signon rpc
      VistaSignonRpc vistaSignonRpc = ExampleVistaSignonRpc.doVistaSignon(server, port, access, verify);
      // Get the vista signon result and check if signon was successful
      VistaSignonResult vistaSignonResult = vistaSignonRpc.getVistaSignonResult(); 
      if (vistaSignonResult.getSignonSucceeded()) {
        RpcBroker rpcBroker = vistaSignonRpc.getRpcBroker();
        // Create a notifications rpc object
        NotificationsRpc notificationsRpc = new NotificationsRpc(rpcBroker);
        notificationsRpc.setReturnRpcResult(false);
        // get and print the list of notifications
        NotificationsList notificationsList = notificationsRpc.getNotifications();
        Notification[] notification = notificationsList.getNotifications(); 
        System.out.println("---------- NOTIFICATIONS ----------");
        for(int i = 0; i < notification.length; i++) {
          System.out.println("Info: " + notification[i].getInfo());
          System.out.println("Patient: " + notification[i].getPatient());
          System.out.println("Location: " + notification[i].getLocation());
          System.out.println("Urgency: " + notification[i].getUrgency());
          System.out.println("Alert Date/Time: " + notification[i].getAlertDateTimeStr());
          System.out.println("Message: " + notification[i].getText());
          System.out.println("Forwarding Info: " + notification[i].getForwardingInfo());
        }
      } else {
        System.out.println(vistaSignonResult.getMessage());
      }    
      // Close the connection to the broker
      vistaSignonRpc.disconnect();
    } catch(BrokerException be) {
      System.err.println(be.getMessage());
      System.err.println("Action: " + be.getAction());
      System.err.println("Code: " + be.getCode());
      System.err.println("Mnemonic: " + be.getMnemonic());
    }    
  }

}