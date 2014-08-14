package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class OrderView extends BaseBean implements Serializable {

  private boolean changed;
  private String dGroup;
  private int filter;
  private boolean invChrono;
  private boolean byService;
  private double fmTimeFrom;
  private String timeFromStr;
  private double fmTimeThru;
  private String timeThruStr;
  private int contextTime;
  private int textView;
  private String viewName;
  private String eventDelayType;
  private int eventDelaySpecialty = 0;
  private int eventDelayEffective = 0;
  
  public OrderView() {
    this.changed = false;
    this.dGroup = null;
    this.filter = 0;
    this.invChrono = false;
    this.byService = false;
    this.fmTimeFrom = 0;
    this.timeFromStr = null;
    this.fmTimeThru = 0;
    this.timeThruStr = null;
    this.contextTime = 0;
    this.textView = 0;
    this.viewName = null;
    this.eventDelayType = null;
    this.eventDelaySpecialty = 0;
    this.eventDelayEffective = 0;
  }
  
  public boolean getByService() {
    return byService;
  }
  
  public void setByService(boolean byService) {
    this.byService = byService;
  }
  
  public boolean getChanged() {
    return changed;
  }
  
  public void setChanged(boolean changed) {
    this.changed = changed;
  }
  
  public int getContextTime() {
    return contextTime;
  }
  
  public void setContextTime(int contextTime) {
    this.contextTime = contextTime;
  }
  
  public String getDGroup() {
    return dGroup;
  }
  
  public void setDGroup(String group) {
    dGroup = group;
  }
  
  public int getEventDelayEffective() {
    return eventDelayEffective;
  }
  
  public void setEventDelayEffective(int eventDelayEffective) {
    this.eventDelayEffective = eventDelayEffective;
  }
  
  public int getEventDelaySpecialty() {
    return eventDelaySpecialty;
  }
  
  public void setEventDelaySpecialty(int eventDelaySpecialty) {
    this.eventDelaySpecialty = eventDelaySpecialty;
  }
  
  public String getEventDelayType() {
    return eventDelayType;
  }
  
  public void setEventDelayType(String eventDelayType) {
    this.eventDelayType = eventDelayType;
  }
  
  public int getFilter() {
    return filter;
  }
  
  public void setFilter(int filter) {
    this.filter = filter;
  }
  
  public double getFmTimeFrom() {
    return fmTimeFrom;
  }
  
  public void setFmTimeFrom(double fmTimeFrom) {
    this.fmTimeFrom = fmTimeFrom;
  }
  
  public double getFmTimeThru() {
    return fmTimeThru;
  }
  
  public void setFmTimeThru(double fmTimeThru) {
    this.fmTimeThru = fmTimeThru;
  }
  
  public boolean getInvChrono() {
    return invChrono;
  }
  
  public void setInvChrono(boolean invChrono) {
    this.invChrono = invChrono;
  }
  
  public int getTextView() {
    return textView;
  }
  
  public void setTextView(int textView) {
    this.textView = textView;
  }
  
  public String getTimeFromStr() {
    return timeFromStr;
  }
  
  public void setTimeFromStr(String timeFromStr) {
    this.timeFromStr = timeFromStr;
  }
  
  public String getTimeThruStr() {
    return timeThruStr;
  }
  
  public void setTimeThruStr(String timeThruStr) {
    this.timeThruStr = timeThruStr;
  }
  
  public String getViewName() {
    return viewName;
  }
  
  public void setViewName(String viewName) {
    this.viewName = viewName;
  }
}