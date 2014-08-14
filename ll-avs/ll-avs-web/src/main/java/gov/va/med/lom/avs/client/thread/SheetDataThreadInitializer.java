package gov.va.med.lom.avs.client.thread;

public class SheetDataThreadInitializer extends SheetDataThread {

  public SheetDataThreadInitializer(String stationNo, String language) {
    super();
    super.setStationNo(stationNo);
    super.setLanguage(language);
  }
  
  public void run() {
    // do nothing
  }
  
}
