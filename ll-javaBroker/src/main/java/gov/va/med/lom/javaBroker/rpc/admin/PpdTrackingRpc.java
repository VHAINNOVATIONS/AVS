package gov.va.med.lom.javaBroker.rpc.admin;

import gov.va.med.lom.javaBroker.rpc.AbstractRpc;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.admin.models.PpdItem;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PpdTrackingRpc extends AbstractRpc{

	private List<PpdItem> items;

	
	/** Creates a new instance of EquipmentFetch */
    public PpdTrackingRpc() {
        super();
    }
    
    public PpdTrackingRpc(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
	
    @SuppressWarnings("unchecked")
	public synchronized List<PpdItem> fetch(int due) throws BrokerException {
        
        if(!setContext("ALT INTRANET RPCS"))
            return null;
        
        ArrayList<String> arr = null;
        
       	Object[] params = {Integer.toString(due)};
       	arr = lCall("ALSI PPD DUE BY SERVICE", params);
 
        items = new ArrayList<PpdItem>();
        PpdItem item;
        
        for (int i = 0; i < arr.size(); i++){
            String x = (String)arr.get(i);
            item = new PpdItem();
            item.setService(StringUtils.piece(x, 1));
            item.setName(StringUtils.piece(x, 2));
            item.setDuz(Long.parseLong(StringUtils.piece(x, 3)));
            String ld = StringUtils.piece(x, 4);
            if(ld != null && (ld.length() > 0)){
            	item.setLastComplete(DateUtils.fmDateTimeToDateTime(
						Double.parseDouble(ld)).getTime());
            }
            items.add(item);
        }
        
        return items;
        
    }
	
}
