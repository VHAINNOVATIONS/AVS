package gov.va.med.lom.javaBroker.rpc.admin;

import gov.va.med.lom.javaBroker.rpc.AbstractRpc;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.admin.models.ScheduledAdmission;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;

public class ScheduledAdmits extends AbstractRpc{

    private List<ScheduledAdmission> admits;
    
    
    /** Creates a new instance of EquipmentFetch */
    public ScheduledAdmits() {
        super();
    }
    
    public ScheduledAdmits(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
    
    @SuppressWarnings("unchecked")
    public synchronized List<ScheduledAdmission> fetch(GregorianCalendar startDate, GregorianCalendar stopDate) throws BrokerException {
        
        if(!setContext("ALT INTRANET RPCS"))
            return null;
        
        ArrayList arr = null;
        
        try{
        	String start = String.valueOf(DateUtils.dateTimeToFMDateTime(startDate));
        	String stop = String.valueOf(DateUtils.dateTimeToFMDateTime(stopDate));
        	Object[] params = {start,stop};
        	arr = lCall("ALSI SCHEDULED ADMITS", params);
        }catch(Exception e){
        	return null;
        }
        
        List<ScheduledAdmission> admits = new ArrayList<ScheduledAdmission>();
        ScheduledAdmission admit;
        
        for (int i = 0; i < arr.size(); i++){
            String x = (String)arr.get(i);
            admit = new ScheduledAdmission();
            admit.setId(StringUtils.piece(x,1));
            try {
				admit.setDfn(StringUtils.piece(x,2));
			} catch (NumberFormatException e1) {
				admit.setDfn(null);
			}
            admit.setName(StringUtils.piece(x,3));
            admit.setSsn(StringUtils.piece(x,4));
            try {
				admit.setAdmitDate(DateUtils.fmDateTimeToDateTime(Double.valueOf(StringUtils.piece(x,5))));
			} catch (Exception e1) {
				admit.setAdmitDate(null);
			}
            admit.setLengthOfStay(StringUtils.toInt(StringUtils.piece(x,6),0));
            admit.setDiagnosis(StringUtils.piece(x,7));
            admit.setProvider(StringUtils.piece(x,8));
            admit.setSurgeryRequired(StringUtils.getBoolean(StringUtils.piece(x,9)));
            try{
            	admit.setWardIen(StringUtils.piece(x,10));
            }catch(java.lang.NumberFormatException e){
            	admit.setWardIen(null);
            }
            admit.setWardName(StringUtils.piece(x,11));
            admit.setTreatingSpecialty(StringUtils.piece(x,12));
            admit.setScheduler(StringUtils.piece(x,13));
            admit.setStationNo(StringUtils.piece(x,14));
            if(StringUtils.piece(x,15).length() == 0){
            	admit.setCancelledDate(null);
            }else{
            	admit.setCancelledDate(DateUtils.fmDateTimeToDateTime(Double.valueOf(StringUtils.piece(x,15))));
            }
            admit.setCancelledBy(StringUtils.piece(x,16));
            admit.setCancelledReason(StringUtils.piece(x,17));
            admits.add(admit);
        }
        
        return admits;
        
    }
    
}
