package gov.va.med.lom.javaBroker.rpc.admin;

import gov.va.med.lom.javaBroker.rpc.AbstractRpc;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.admin.models.PatientMovement;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;

public class PatientMovements extends AbstractRpc{

    private List<PatientMovement> mvs;
    
    
    /** Creates a new instance of EquipmentFetch */
    public PatientMovements() {
        super();
    }
    
    public PatientMovements(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
    
    @SuppressWarnings("unchecked")
    public synchronized List<PatientMovement> fetch(GregorianCalendar startDate, GregorianCalendar stopDate) throws BrokerException {
        
        if(!setContext("ALT INTRANET RPCS"))
            return null;
        
        ArrayList arr = null;
        
        try{
        	String start = String.valueOf(DateUtils.dateTimeToFMDateTime(startDate));
        	String stop = String.valueOf(DateUtils.dateTimeToFMDateTime(stopDate));
        	Object[] params = {start,stop};
        	arr = lCall("ALSI PATIENT MOVEMENT", params);
        }catch(Exception e){
        	return null;
        }
        
        mvs = new ArrayList<PatientMovement>();
        PatientMovement mv;
        
        for (int i = 0; i < arr.size(); i++){
            String x = (String)arr.get(i);
            mv = new PatientMovement();
            mv.setId(StringUtils.piece(x,1));
            mv.setStationNo(rpcBroker.getStationNo());
            mv.setDate(DateUtils.fmDateTimeToDateTime(Double.valueOf(StringUtils.piece(x,2))));
            mv.setTransactionIen(StringUtils.piece(x,3));
            mv.setTransaction(StringUtils.piece(x,4));
            mv.setDfn(StringUtils.piece(x,5));
            mv.setSsn(StringUtils.piece(x,6));
            mv.setName(StringUtils.piece(x,7));
            mv.setTransactionTypeIen(StringUtils.piece(x,8));
            mv.setTransactionType(StringUtils.piece(x,9));
            mv.setWardIen(StringUtils.piece(x,10));
            mv.setWard(StringUtils.piece(x,11));
            mv.setRoom(StringUtils.piece(x,12));
            mv.setWardDischargeIen(StringUtils.piece(x,13));
            mv.setWardDischarge(StringUtils.piece(x,14));
            
            mvs.add(mv);
        }
        
        return mvs;
        
    }
    
}
