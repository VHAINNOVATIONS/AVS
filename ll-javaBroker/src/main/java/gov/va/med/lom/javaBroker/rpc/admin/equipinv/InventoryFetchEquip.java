/*
 * EquipmentFetch.java
 *
 * Created on November 1, 2005, 1:41 PM
 */

package gov.va.med.lom.javaBroker.rpc.admin.equipinv;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.admin.equipinv.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

/**
 *
 * @author amccarty
 */
public class InventoryFetchEquip extends AbstractRpc {
    
    private EquipmentItem equip;
    
    /** Creates a new instance of EquipmentFetch */
    public InventoryFetchEquip() {
        super();
    }
    
    public InventoryFetchEquip(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
    
    public synchronized EquipmentItem fetch(String EE) throws BrokerException {
        
        if(!setContext("ALS6 ITS REMOTE PROCEDURES"))
            return null;
        
        Object[] params = {EE};
        
        String x = sCall("ALS6 EQP INV INQ LOCAL FIELDS", params);            
        
        equip = new EquipmentItem();
        equip.setEe(EE);
        equip.setCategoryIen(StringUtils.piece(x,1));
        equip.setManufactureIen(StringUtils.piece(x,2));
        equip.setModelIen(StringUtils.piece(x,3));
        
        return equip;
        
    }
    
}
