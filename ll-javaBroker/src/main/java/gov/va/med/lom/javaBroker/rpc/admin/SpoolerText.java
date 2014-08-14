package gov.va.med.lom.javaBroker.rpc.admin;

import gov.va.med.lom.javaBroker.rpc.AbstractRpc;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

import java.util.ArrayList;

public class SpoolerText  extends AbstractRpc{

    /** Creates a new instance of EquipmentFetch */
    public SpoolerText() {
        super();
    }
    
    public SpoolerText(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
    
    /* returns the report as an array separated at the line breaks */
    @SuppressWarnings("unchecked")
    public synchronized String[] getSpoolerTextList(String reportName) throws BrokerException {
        
        if(!setContext("ALTZ G&L WEB TOOLS"))
            return null;
        
        Object[] params = {reportName};
        
        ArrayList rpt = lCall("ALTZ GET TEXT FROM SPOOLER", params);            
        
        String[] report = new String[rpt.size()];
        
        for (int i = 0; i < rpt.size(); i++){
            String x = (String)rpt.get(i);
            report[i] = new String(x);
        }
        
        return report;
        
    }
        
    /* returns the report on one continuous string */
    public synchronized String getSpoolerText(String reportName) throws BrokerException {
        
        if(!setContext("ALTZ G&L WEB TOOLS"))
            return null;
        
        Object[] params = {reportName};
        
        return sCall("ALTZ GET TEXT FROM SPOOLER", params);            
        
    }
    
}



