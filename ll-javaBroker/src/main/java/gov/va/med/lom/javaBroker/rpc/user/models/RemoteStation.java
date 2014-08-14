package gov.va.med.lom.javaBroker.rpc.user.models;

public class RemoteStation implements java.io.Serializable {

  private String stationNo;
  private String stationName;
  private double lastSeenDate;
  private String lastEvent;
  
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getStationName() {
    return stationName;
  }
  public void setStationName(String stationName) {
    this.stationName = stationName;
  }
  public double getLastSeenDate() {
    return lastSeenDate;
  }
  public void setLastSeenDate(double lastSeenDate) {
    this.lastSeenDate = lastSeenDate;
  }
  public String getLastEvent() {
    return lastEvent;
  }
  public void setLastEvent(String lastEvent) {
    this.lastEvent = lastEvent;
  }
  
}
