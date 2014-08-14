package gov.va.med.lom.avs.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("patients")
public class Patient implements Serializable {

  @Id private ObjectId id;
  private String stationNo;
  private String dfn; 
  private String name;
  
  public ObjectId getId() {
    return id;
  }
  public void setId(ObjectId id) {
    this.id = id;
  }
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDfn() {
    return dfn;
  }
  public void setDfn(String dfn) {
    this.dfn = dfn;
  }
  
}