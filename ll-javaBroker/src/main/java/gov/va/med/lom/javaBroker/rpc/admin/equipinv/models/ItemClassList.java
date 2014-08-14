/*
 * ItemClassList.java
 *
 * Created on November 1, 2005, 12:07 PM
 */

package gov.va.med.lom.javaBroker.rpc.admin.equipinv.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;
import java.io.Serializable;


/**
 *
 * @author amccarty
 */
public class ItemClassList extends BaseBean implements Serializable {
    
    private ItemClass[] ItemClasses;
  
    public ItemClassList() {
        this.setItemClasses(null);
    }

    public ItemClass[] getItemClasses() {
        return ItemClasses;
    }

    public void setItemClasses(ItemClass[] ItemClasses) {
        this.ItemClasses = ItemClasses;
    }
    
}
