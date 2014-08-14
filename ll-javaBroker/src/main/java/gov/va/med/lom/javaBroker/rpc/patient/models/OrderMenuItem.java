package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class OrderMenuItem extends BaseBean implements Serializable {

  private int row;
  private int col;
  private char dlgType;
  private String formId;
  private boolean autoAck;
  private String itemText;
  private String mnemonic;
  private int display;
  private boolean selected;
  
  public int getRow() {
    return row;
  }
  public void setRow(int row) {
    this.row = row;
  }
  public int getCol() {
    return col;
  }
  public void setCol(int col) {
    this.col = col;
  }
  public char getDlgType() {
    return dlgType;
  }
  public void setDlgType(char dlgType) {
    this.dlgType = dlgType;
  }
  public String getFormId() {
    return formId;
  }
  public void setFormId(String formId) {
    this.formId = formId;
  }
  public boolean isAutoAck() {
    return autoAck;
  }
  public void setAutoAck(boolean autoAck) {
    this.autoAck = autoAck;
  }
  public String getItemText() {
    return itemText;
  }
  public void setItemText(String itemText) {
    this.itemText = itemText;
  }
  public String getMnemonic() {
    return mnemonic;
  }
  public void setMnemonic(String mnemonic) {
    this.mnemonic = mnemonic;
  }
  public int getDisplay() {
    return display;
  }
  public void setDisplay(int display) {
    this.display = display;
  }
  public boolean isSelected() {
    return selected;
  }
  public void setSelected(boolean selected) {
    this.selected = selected;
  }
  
}
