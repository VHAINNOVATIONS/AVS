package gov.va.med.lom.javaBroker.rpc.admin.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class SoftwarePackage extends BaseBean implements Serializable{

    private String namespace;
    private String packageName;
    private String AdpacSupport;
    private String ItsSupport;
    
    
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public String getNamespace() {
        return namespace;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getPackageName() {
        return packageName;
    }
    public String getAdpacSupport() {
        return AdpacSupport;
    }
    public void setAdpacSupport(String adpacSupport) {
        AdpacSupport = adpacSupport;
    }
    public String getItsSupport() {
        return ItsSupport;
    }
    public void setItsSupport(String itsSupport) {
        ItsSupport = itsSupport;
    }
    
    
    
}
