package gov.va.med.lom.vistabroker.admin.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.admin.data.SoftwarePackage;
import gov.va.med.lom.vistabroker.dao.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class PackageSupportDao extends BaseDao {

    // CONSTRUCTORS
    public PackageSupportDao() {
        super();
    }
    
    public PackageSupportDao(BaseDao baseDao) {
      super(baseDao);
    }
    
    // RPC API
    public List<SoftwarePackage> fetch(int sort) throws Exception {
        setDefaultContext("ALT INTRANET RPCS");
        setDefaultRpcName("ALSI ITS PACKAGE SUPPORT");
        if(sort < 1 || sort > 2)
            sort = 1;
        Object[] params = {String.valueOf(sort)};
        List<String> list = lCall(params);            
        List<SoftwarePackage> softwarePackages = new ArrayList<SoftwarePackage>();
        for (String s : list){
            SoftwarePackage softwarePackage = new SoftwarePackage();
            softwarePackage.setNamespace(StringUtils.piece(s,1));
            softwarePackage.setPackageName(StringUtils.piece(s,2));
            softwarePackage.setAdpacSupport(StringUtils.piece(s,3));
            softwarePackage.setItsSupport(StringUtils.piece(s,4));
            softwarePackages.add(softwarePackage);
        }
        return softwarePackages;
    }
    
}
