/*
 * InventoryPrintLabelRpc.java
 *
 * Created on November 1, 2005, 1:55 PM
 */

package gov.va.med.lom.javaBroker.rpc.admin.equipinv;

import gov.va.med.lom.javaBroker.rpc.*;

/**
 *
 * @author amccarty
 */
public class InventoryPrintLabelRpc extends AbstractRpc {
    
    private Integer result;
    
    /** Creates a new instance of InventoryPrintLabelRpc */
    public InventoryPrintLabelRpc() {
        super();
    }
    
    public InventoryPrintLabelRpc(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
    
    public synchronized Integer printLabel(String EE, String SN) throws BrokerException {
        
        if(!setContext("ALS6 ITS REMOTE PROCEDURES"))
            return new Integer(-1);
        
        Object[] params = {EE, SN};
        
        String y = sCall("ALTZ ENGINEERING EQUIP LABEL", params);            
        
        result =  new Integer(y);
        return result;
        
    }
    
}
