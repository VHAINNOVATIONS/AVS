package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class LocationsList extends BaseBean implements Serializable {

  private Location[] locations;
  
  public LocationsList() {
    this.locations = null;
  }

  public Location[] getLocations() {
    return locations;
  }
  
  public void setLocations(Location[] locations) {
    this.locations = locations;
  }
}
