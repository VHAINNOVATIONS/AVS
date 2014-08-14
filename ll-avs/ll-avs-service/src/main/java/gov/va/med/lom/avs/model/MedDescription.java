package gov.va.med.lom.avs.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("medDescriptions")
public class MedDescription implements Serializable {

  @Id private ObjectId id;
  private String ndc; 
  private String shape;
  private String color;
  private String frontImprint;
  private String backImprint;
  
  public ObjectId getId() {
    return id;
  }
  public void setId(ObjectId id) {
    this.id = id;
  }
  public String getNdc() {
    return ndc;
  }
  public void setNdc(String ndc) {
    this.ndc = ndc;
  }
  public String getShape() {
    return shape;
  }
  public void setShape(String shape) {
    this.shape = shape;
  }
  public String getColor() {
    return color;
  }
  public void setColor(String color) {
    this.color = color;
  }
  public String getFrontImprint() {
    return frontImprint;
  }
  public void setFrontImprint(String frontImprint) {
    this.frontImprint = frontImprint;
  }
  public String getBackImprint() {
    return backImprint;
  }
  public void setBackImprint(String backImprint) {
    this.backImprint = backImprint;
  }
}