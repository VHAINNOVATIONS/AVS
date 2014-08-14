package gov.va.med.lom.vistabroker.admin.dao;

import gov.va.med.lom.vistabroker.dao.BaseDao;

import java.util.List;

public class SpoolerTextDao  extends BaseDao {
  
    // CONSTRUCTORS
    public SpoolerTextDao() {
        super();
    }
    
    public SpoolerTextDao(BaseDao baseDao) {
        super(baseDao);
    }
    
    // RPC API
    /* returns the report as an array separated at the line breaks */
    public List<String> getSpoolerTextList(String reportName) throws Exception {
        setDefaultContext("ALTZ G&L WEB TOOLS");
        setDefaultRpcName("ALTZ GET TEXT FROM SPOOLER");
        Object[] params = {reportName};
        return lCall(params);            
    }
        
    /* returns the report on one continuous string */
    public String getSpoolerText(String reportName) throws Exception {
        setDefaultContext("ALTZ G&L WEB TOOLS");
        setDefaultRpcName("ALTZ GET TEXT FROM SPOOLER");
        Object[] params = {reportName};
        return sCall(params);            
    }
}



