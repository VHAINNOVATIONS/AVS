package gov.va.med.lom.javaBroker.rpc.admin;

import gov.va.med.lom.javaBroker.rpc.AbstractRpc;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.admin.models.SoftwarePackage;
import gov.va.med.lom.javaBroker.util.StringUtils;

import java.util.ArrayList;

public class PackageSupport extends AbstractRpc{

    private SoftwarePackage[] softwarePackages;
    
    
    /** Creates a new instance of EquipmentFetch */
    public PackageSupport() {
        super();
    }
    
    public PackageSupport(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
    
    @SuppressWarnings("unchecked")
    public synchronized SoftwarePackage[] fetch(int sort) throws BrokerException {
        
        if(sort < 1 || sort > 2)
            sort = 1;
        
        if(!setContext("ALT INTRANET RPCS"))
            return null;
        
        Object[] params = {String.valueOf(sort)};
        
        ArrayList packages = lCall("ALSI ITS PACKAGE SUPPORT", params);            
        
        softwarePackages = new SoftwarePackage[packages.size()];
        
        for (int i = 0; i < packages.size(); i++){
            String x = (String)packages.get(i);
            softwarePackages[i] = new SoftwarePackage();
            softwarePackages[i].setNamespace(StringUtils.piece(x,1));
            softwarePackages[i].setPackageName(StringUtils.piece(x,2));
            softwarePackages[i].setAdpacSupport(StringUtils.piece(x,3));
            softwarePackages[i].setItsSupport(StringUtils.piece(x,4));
        }
        
        return softwarePackages;
        
    }
    
}
