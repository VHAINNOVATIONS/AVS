

package gov.va.med.lom.javaBroker.rpc.admin.equipinv.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;
import java.io.Serializable;

/**
 *
 * @author amccarty
 */
public class ItemClass extends BaseBean implements Serializable {
    
    /** Creates a new instance of ItemClass */
    private String ien;
    private String name;
    private String parentIen;
  
    public ItemClass() {
        this.ien = null;
        this.name = "";
        this.parentIen = null;
    }

    public String getIen() {
        return ien;
    }

    public void setIen(String ien) {
        this.ien = ien;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentIen() {
        return parentIen;
    }

    public void setParentIen(String parentIen) {
        this.parentIen = parentIen;
    }
    
    
}
