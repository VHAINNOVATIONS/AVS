/*
 * NewClass.java
 *
 * Created on November 1, 2005, 11:07 AM
 */
package gov.va.med.lom.javaBroker.rpc.admin.equipinv;

import gov.va.med.lom.javaBroker.rpc.*;

/**
 *
 * @author amccarty
 */
public class InventoryUpdateRpc extends AbstractRpc {
    
    private Integer result;
    
    /** Creates a new instance of NewClass */
    public InventoryUpdateRpc() {
        super();
    }
  
    public InventoryUpdateRpc(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
  
    // RPC API
    public synchronized Integer update(String EE, 
                                       String SN,
                                       String Room,
                                       int CategoryIEN,
                                       int ManufacturerIEN,
                                       int ModelIEN) throws BrokerException {
        
        if(!setContext("ALS6 ITS REMOTE PROCEDURES"))
            return new Integer(-1);
        
        Object[] params = {EE, SN, Room, CategoryIEN,
                           ManufacturerIEN,ModelIEN};
        
        String y = sCall("ALS6 ITS INVENTORY UPDATE", params);            
        
        result =  new Integer(y);
        return result;
        
    }
  
}
