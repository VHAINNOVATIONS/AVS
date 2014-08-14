package gov.va.med.lom.avs.client.model;

import java.util.List;
import java.io.Serializable;

public class PatientEncounterJson implements Serializable {

  private int id;
  private String text;
  private String iconCls;
  private PatientEncounterJson parent;
  private String fmDatetime;
  private String datetime;
  private String locationIen;
  private String locationName;
  private String status;
  private Boolean selected;
  private Boolean expanded;
  private Boolean checked;
  private Boolean leaf;  
  private List<PatientEncounterJson> children;
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getFmDatetime() {
    return fmDatetime;
  }
  public void setFmDatetime(String fmDatetime) {
    this.fmDatetime = fmDatetime;
  }
  public String getDatetime() {
    return datetime;
  }
  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }
  public String getLocationIen() {
    return locationIen;
  }
  public void setLocationIen(String locationIen) {
    this.locationIen = locationIen;
  }
  public String getLocationName() {
    return locationName;
  }
  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public Boolean isSelected() {
    return selected;
  }
  public void setSelected(Boolean selected) {
    this.selected = selected;
  }
  public Boolean getExpanded() {
    return expanded;
  }
  public void setExpanded(Boolean expanded) {
    this.expanded = expanded;
  }
  public Boolean getChecked() {
    return checked;
  }
  public void setChecked(Boolean checked) {
    this.checked = checked;
  }
  public Boolean getLeaf() {
    return leaf;
  }
  public void setLeaf(Boolean leaf) {
    this.leaf = leaf;
  }
  public List<PatientEncounterJson> getChildren() {
    return children;
  }
  public void setChildren(List<PatientEncounterJson> children) {
    this.children = children;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  public String getIconCls() {
    return iconCls;
  }
  public void setIconCls(String iconCls) {
    this.iconCls = iconCls;
  }
  public PatientEncounterJson getParent() {
    return parent;
  }
  public void setParent(PatientEncounterJson parent) {
    this.parent = parent;
  }
  
}
