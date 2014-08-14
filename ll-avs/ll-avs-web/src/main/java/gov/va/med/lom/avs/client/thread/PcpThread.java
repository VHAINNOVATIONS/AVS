package gov.va.med.lom.avs.client.thread;

public class PcpThread extends SheetDataThread {

  public void run() {
    
    StringBuffer body = new StringBuffer();
    try {
      String provider = super.demographics.getPrimaryProvider();
      String team = super.demographics.getPrimaryTeam();
    
      if (provider == null || provider.isEmpty() || team == null || team.isEmpty()) {
        body.append(super.getStringResource("unknown"));
        
      } else {
        
        if (provider != null && !provider.isEmpty()) {
          body.append("<div>").append(provider).append("</div>\n");
        }
        if (team != null && !team.isEmpty()) {
          body.append("<div>").append(team).append("</div>\n");
        }
        
      }
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "provider")
        .replace("__SECTION_TITLE__", super.getStringResource("pcp"))
        .replace("__CONTENTS__", body.toString());
      
      setContent("pcp", content);
    }    
    
  }
  
}
