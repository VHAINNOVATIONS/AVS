package gov.va.med.lom.avs.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="vhaSite")
public class VhaSite extends BaseModel implements Serializable  {

  private VhaVisn vhaVisn;
  private String name;
  private String location;
  private String stationNo;
  private String moniker;
  private String modality;
  private String protocol;
  private String host;
  private int port;
  private String status;
  
  @ManyToOne
  @JoinColumn(name="visnId") 
  public VhaVisn getVhaVisn() {
    return vhaVisn;
  }
  
  public void setVhaVisn(VhaVisn vhaVisn) {
    this.vhaVisn = vhaVisn;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getStationNo() {
    return stationNo;
  }
  
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  
  public String getMoniker() {
    return moniker;
  }
  
  public void setMoniker(String moniker) {
    this.moniker = moniker;
  }

  public String getModality() {
    return modality;
  }
  
  public void setModality(String modality) {
    this.modality = modality;
  }
  
  public String getProtocol() {
    return protocol;
  }
  
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }
  
  public String getHost() {
    return host;
  }
  
  public void setHost(String host) {
    this.host = host;
  }
  
  public int getPort() {
    return port;
  }
  
  public void setPort(int port) {
    this.port = port;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
  
}
