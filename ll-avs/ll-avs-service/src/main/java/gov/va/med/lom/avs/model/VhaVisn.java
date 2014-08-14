package gov.va.med.lom.avs.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="vhaVisn")
public class VhaVisn extends BaseModel implements Serializable  {

  private String name;
  private String visnNo;
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getVisnNo() {
    return visnNo;
  }
  
  public void setVisnNo(String visnNo) {
    this.visnNo = visnNo;
  }
  
}
