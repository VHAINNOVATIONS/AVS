package gov.va.med.lom.avs.util;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import gov.va.med.lom.javaUtils.misc.JdbcConnection;
import gov.va.med.lom.javaUtils.misc.StringUtils;

public class FacilityClinicalRemindersConfig {

  static String DB_USER;
  static String DB_PASS;
  static String DB_DRIVER;
  static String DB_URL;
  
  static {
    ResourceBundle res = ResourceBundle.getBundle("gov.va.med.lom.avs.db");
    String dbServer = res.getString("db.server");
    String dbPort = res.getString("db.port");
    String dbName = res.getString("db.name");
    String dbDriverType = res.getString("db.drivertype");
    String dbDatabase = res.getString("db.database");
    DB_USER = res.getString("db.username");
    DB_PASS = res.getString("db.password");
    DB_DRIVER = res.getString("db.driver");
    DB_URL = "jdbc:" + dbDriverType + ":" + dbDatabase + "://" + 
             dbServer + ":" + dbPort + ";DatabaseName=" + dbName;
  }
  
  public static void main(String[] args) throws IOException {

    String stationNo = "605";
    List<String> clinicalReminders = new ArrayList<String>();
    clinicalReminders.add("10^LR176^Abdominal Aortic Aneurysm Screening");
    clinicalReminders.add("20^LR605047^Advance Directive");
    clinicalReminders.add("30^LR605002^Alcohol Screening (AUDIT-C)");
    clinicalReminders.add("50^LR605088^Blood Pressure > 140/90");
    clinicalReminders.add("60^LR605059^Colonoscopy");
    clinicalReminders.add("70^LR262^Depression Screening");
    clinicalReminders.add("80^LR245^Depression Screen Positive");
    clinicalReminders.add("90^LR326^Diabetes - Aspirin");
    clinicalReminders.add("110^LR605092^Diabetes Hgb A1C > 9.0%");
    clinicalReminders.add("120^LR194^Diabetic Eye Exam");
    clinicalReminders.add("130^LR293^Diabetic Foot Exam");
    clinicalReminders.add("140^LR296^Diabetes LDL>100 or Not Done v3");
    clinicalReminders.add("150^LR605056^Diabetic Microalbuminuria");
    clinicalReminders.add("155^LR369^Discuss Breast CA Screen: Age 40-49");
    clinicalReminders.add("170^LR361^FIT/Colon Cancer Screening V3");
    clinicalReminders.add("180^LR318^Functional Assessment (Katz ADLs)");
    clinicalReminders.add("200^LR33^Heart Failure EF<40%");
    clinicalReminders.add("210^LR12^Heart Failure Weight Monitoring");
    clinicalReminders.add("220^LR605079^Hep C Risk Assessment v1");
    clinicalReminders.add("230^LR49^Hepatitis C Ab Testing");
    clinicalReminders.add("240^LR605060^Hemoglobin A1C");
    clinicalReminders.add("250^LR363^Homelessness Screening");
    clinicalReminders.add("260^LR160^Hyperlipidemia Screen (Female)");
    clinicalReminders.add("270^LR605051^Hyperlipidemia Screen (Male)");
    clinicalReminders.add("290^LR605062^Influenza Vaccine");
    clinicalReminders.add("300^LR605063^Inhaler Education");
    clinicalReminders.add("310^LR568022^Iraq&Afghan Post-Deployment Screen");
    clinicalReminders.add("320^LR605082^Ischemic Heart Disease - Aspirin");
    clinicalReminders.add("330^LR605085^Ischemic Heart Disease-B Blocker");
    clinicalReminders.add("335^LR295^Ischemic Heart LDL>100 Not Done");
    clinicalReminders.add("340^LR360^Language Preference");
    clinicalReminders.add("350^LR130^Mammogram Screening");
    clinicalReminders.add("360^LR61^Military Sexual Trauma (MST) v1");
    clinicalReminders.add("370^LR273^MOVE Referral");
    clinicalReminders.add("380^LR349^MyHealtheVet Enrollment v2");
    clinicalReminders.add("390^LR350^MyHealtheVet Secure Messaging");
    clinicalReminders.add("420^LR272^Overweight/Obesity Counseling");
    clinicalReminders.add("430^LR132^PAP Smear Screening");
    clinicalReminders.add("440^LR605066^Pneumovax");
    clinicalReminders.add("450^LR925^Positive AUDIT-C Needs Evaluation");
    clinicalReminders.add("480^LR207^PTSD Screen Positive");
    clinicalReminders.add("490^LR299^Screen for Embedded Fragments");
    clinicalReminders.add("500^LR751^Screen for PTSD");
    clinicalReminders.add("510^LR229^Smoking Cessation Education/Meds");
    clinicalReminders.add("520^LR605068^Spirometry");
    clinicalReminders.add("530^LR793^TBI Screening");
    clinicalReminders.add("540^LR605069^Tetanus Diptheria (TD-Adult)");
    clinicalReminders.add("550^LR305^Tetanus for > 65 Years Old");
    clinicalReminders.add("560^LR605070^Tobacco Use Screening");
    
    try {
      JdbcConnection jdbcConnection = new JdbcConnection(DB_URL, DB_DRIVER);
      jdbcConnection.connect(DB_USER, DB_PASS);
      
      StringBuilder sql = new StringBuilder("INSERT INTO ckoPvsClinicalReminders (stationNo, reminderIen, reminderName) VALUES (?,?,?)");
      
      Connection connection = jdbcConnection.getConnection();
      PreparedStatement pstmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
      
      for (String clinicalReminder : clinicalReminders) {
        pstmt.setString(1, stationNo);
        String s = StringUtils.piece(clinicalReminder, 2);
        if (s.startsWith("LR")) {
          s = s.substring(2);
        }
        pstmt.setString(2, s);
        pstmt.setString(3, StringUtils.piece(clinicalReminder, 3));
        pstmt.executeUpdate();
      }
      
    } catch(Exception e) {
      System.err.println("JDBC Error, exiting: " + e.getMessage());
      System.exit(1);
    }
    
  }
  
}
