package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class VistaImageInfo implements Serializable {

  private String imageIen;
  private String location;
  private String filename;
  
  public String getImageIen() {
    return imageIen;
  }
  public void setImageIen(String imageIen) {
    this.imageIen = imageIen;
  }
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  public String getFilename() {
    return filename;
  }
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
}
