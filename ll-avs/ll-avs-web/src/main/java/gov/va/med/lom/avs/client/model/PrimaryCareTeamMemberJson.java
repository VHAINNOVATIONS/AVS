package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class PrimaryCareTeamMemberJson implements Serializable {

    private String name;
    private String title;
    
    public PrimaryCareTeamMemberJson() {}
    
    public PrimaryCareTeamMemberJson(String name, String title) {
      this.name = name;
      this.title = title;
    }
    
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getTitle() {
      return title;
    }
    public void setTitle(String title) {
      this.title = title;
    }
    
}
