/*
 * InventoryTypeQueryRpc.java
 *
 * Created on November 1, 2005, 12:51 PM
 */

package gov.va.med.lom.javaBroker.rpc.admin.equipinv;

import java.util.ArrayList;
import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.admin.equipinv.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

/**
 *
 * @author amccarty
 */
public class InventoryTypeQueryRpc extends AbstractRpc {
    
    private ItemClassList ic;
    
    /** Creates a new instance of InventoryTypeQueryRpc */
    public InventoryTypeQueryRpc() {
        super();
    }
      
    public InventoryTypeQueryRpc(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
  
    
    public synchronized ItemClassList getCategoryList() throws BrokerException {
        return getList("0");
    }
    
    public synchronized ItemClassList getManufactureList(String CategoryIEN) throws BrokerException {        
        return getList(CategoryIEN);       
    }
    
    public synchronized ItemClassList getModelList(String ManufactureIEN) throws BrokerException {       
        return getList(ManufactureIEN);       
    }
    
    
    public synchronized ItemClassList getList(String IEN) throws BrokerException {
        
        if(!setContext("ALS6 ITS REMOTE PROCEDURES"))
            return null;
        
        Object[] params = {IEN};
        
        ArrayList y = lCall("ALS6 ITS INVENTORY TYPE QUERY", params);            
        ItemClass[] items = getItems(y, IEN);
        ic = new ItemClassList();
        ic.setItemClasses(items);
        return ic;
        
    }
    
    
    
    // Parses the result list from VistA and returns array of patient list item objects
    private ItemClass[] getItems(ArrayList list, String parentIEN) {
        ItemClass[] items = new ItemClass[list.size()];
        for(int i=0; i < list.size(); i++) {
            ItemClass item = new ItemClass();
            String x = (String)list.get(i);
    
            item.setIen(StringUtils.piece(x,1));
            item.setName(StringUtils.piece(x,2));
            item.setParentIen(parentIEN);   
        }
        return items;
    }
  
}
