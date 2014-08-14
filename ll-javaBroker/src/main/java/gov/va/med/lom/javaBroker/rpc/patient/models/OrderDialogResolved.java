package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class OrderDialogResolved extends BaseBean implements Serializable {

  private String inputId;      // can be dialog IEN or '#ORIFN'
  private int quickLevel;      // 0=dialog,1=auto,2=verify,8=reject,9=cancel
  private String responseId;   // DialogID + ';' + $H
  private String dialogIen;    // pointer to 101.41 for dialog (may be quick order IEN)
  private char dialogType;     // type of dialog (Q or D)
  private int formId;          // windows form to display
  private String displayGroup; // pointer to 100.98, display group for dialog
  private String showText;     // text to show for verify or rejection
  private String qoKeyVars;    // from entry action of quick order
  
  public String getInputId() {
    return inputId;
  }
  public void setInputId(String inputId) {
    this.inputId = inputId;
  }
  public int getQuickLevel() {
    return quickLevel;
  }
  public void setQuickLevel(int quickLevel) {
    this.quickLevel = quickLevel;
  }
  public String getResponseId() {
    return responseId;
  }
  public void setResponseId(String responseId) {
    this.responseId = responseId;
  }
  public String getDialogIen() {
    return dialogIen;
  }
  public void setDialogIen(String dialogIen) {
    this.dialogIen = dialogIen;
  }
  public char getDialogType() {
    return dialogType;
  }
  public void setDialogType(char dialogType) {
    this.dialogType = dialogType;
  }
  public int getFormId() {
    return formId;
  }
  public void setFormId(int formId) {
    this.formId = formId;
  }
  public String getDisplayGroup() {
    return displayGroup;
  }
  public void setDisplayGroup(String displayGroup) {
    this.displayGroup = displayGroup;
  }
  public String getShowText() {
    return showText;
  }
  public void setShowText(String showText) {
    this.showText = showText;
  }
  public String getQoKeyVars() {
    return qoKeyVars;
  }
  public void setQoKeyVars(String qoKeyVars) {
    this.qoKeyVars = qoKeyVars;
  }
  
}
