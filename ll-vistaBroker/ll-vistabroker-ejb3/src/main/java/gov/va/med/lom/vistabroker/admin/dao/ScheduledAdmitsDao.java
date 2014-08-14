package gov.va.med.lom.vistabroker.admin.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.admin.data.ScheduledAdmission;
import gov.va.med.lom.vistabroker.dao.BaseDao;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ScheduledAdmitsDao extends BaseDao {
    
    // CONSTRUCTORS
    public ScheduledAdmitsDao() {
        super();
    }
    
    public ScheduledAdmitsDao(BaseDao baseDao) {
      super(baseDao);
    }
    
    // RPC API
    public List<ScheduledAdmission> fetch(GregorianCalendar startDate, GregorianCalendar stopDate) throws Exception {
        setDefaultContext("ALT INTRANET RPCS");
        setDefaultRpcName("ALSI SCHEDULED ADMITS");
        List<String> list = null;
        try{
        	String start = String.valueOf(DateUtils.dateTimeToFMDateTime(startDate));
        	String stop = String.valueOf(DateUtils.dateTimeToFMDateTime(stopDate));
        	Object[] params = {start,stop};
          list = lCall(params);
        } catch(Exception e){
        	return null;
        }
        List<ScheduledAdmission> admits = new ArrayList<ScheduledAdmission>();
        ScheduledAdmission admit;
        for (String s : list) {
            admit = new ScheduledAdmission();
            admit.setId(Integer.valueOf(StringUtils.piece(s,1)));
            admit.setDfn(StringUtils.piece(s,2));
            admit.setName(StringUtils.piece(s,3));
            admit.setSsn(StringUtils.piece(s,4));
            admit.setAdmitDate(DateUtils.fmDateTimeToDateTime(Double.valueOf(StringUtils.piece(s,5))));
            admit.setLengthOfStay(StringUtils.toInt(StringUtils.piece(s,6),0));
            admit.setDiagnosis(StringUtils.piece(s,7));
            admit.setProvider(StringUtils.piece(s,8));
            admit.setSurgeryRequired(StringUtils.getBoolean(StringUtils.piece(s,9)));
            try{
            	admit.setWardIen(StringUtils.piece(s,10));
            }catch(java.lang.NumberFormatException e){
            	admit.setWardIen("0");
            }
            admit.setWardName(StringUtils.piece(s,11));
            admit.setTreatingSpecialty(StringUtils.piece(s,12));
            admit.setScheduler(StringUtils.piece(s,13));
            admit.setStationNo(StringUtils.piece(s,14));
            if(StringUtils.piece(s,15).length() == 0){
            	admit.setCancelledDate(null);
            }else{
            	admit.setCancelledDate(DateUtils.fmDateTimeToDateTime(Double.valueOf(StringUtils.piece(s,15))));
            }
            admit.setCancelledBy(StringUtils.piece(s,16));
            admit.setCancelledReason(StringUtils.piece(s,17));
            admits.add(admit);
        }
        
        return admits;
    }
}
