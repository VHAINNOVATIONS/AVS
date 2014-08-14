package gov.va.med.lom.javaBroker.rpc.admin;

import gov.va.med.lom.javaBroker.rpc.AbstractRpc;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class SurgeryScheduleRpc extends AbstractRpc{

	private List<SurgeryScheduleItem> items;

	
	/** Creates a new instance of EquipmentFetch */
    public SurgeryScheduleRpc() {
        super();
    }
    
    public SurgeryScheduleRpc(RpcBroker rpcBroker) throws BrokerException {
        super(rpcBroker);
    }
	
    @SuppressWarnings("unchecked")
	public synchronized List<SurgeryScheduleItem> fetch(GregorianCalendar date) throws BrokerException {
        
        if(!setContext("ARIL O.R.VIEWER 2"))
            return null;
        
        ArrayList<String> arr = null;
        
        try{
        	String start = String.valueOf(DateUtils.formatDate(date.getTime(), DateUtils.ENGLISH_DATE_FORMAT));
        	Object[] params = {start};
        	arr = lCall("ARIL GET SDATA2", params);
        }catch(Exception e){
        	return null;
        }
        
        items = new ArrayList<SurgeryScheduleItem>();
        SurgeryScheduleItem item;
        
        for (int i = 0; i < arr.size(); i++){
            String x = (String)arr.get(i);
            item = new SurgeryScheduleItem();
            item.setOperatingRoom(StringUtils.piece(x, 1));
            item.setPatientName(StringUtils.piece(x, 2));
            item.setLast4(StringUtils.piece(x, 3));
            item.setPatientLocation(StringUtils.piece(x, 4));
            item.setPrincipleProcedure(StringUtils.piece(x, 5));
            item.setOtherProcedure(StringUtils.piece(x, 6));
            item.setScheduledStartTime(StringUtils.piece(x, 7));
            item.setEstmatedCompletionTime(StringUtils.piece(x, 8));
            item.setCaseLength(StringUtils.piece(x, 9));
            item.setOperationEndTime(StringUtils.piece(x, 10));
            item.setStatus(StringUtils.piece(x, 11));
            //StringUtils.piece(x, 12);  piece 12 appears to never get set
            item.setTimeInOr(StringUtils.piece(x, 13));
            item.setInductionCompletionTime(StringUtils.piece(x, 14));
            item.setActualBeginTime(StringUtils.piece(x, 15));
            item.setActualEndTime(StringUtils.piece(x, 16));
            item.setTimeOutOfOr(StringUtils.piece(x, 17));
            item.setOrOccupancyTime(StringUtils.piece(x, 18));
            item.setSurgeonName(StringUtils.piece(x, 19));
            item.setAttendingName(StringUtils.piece(x, 20));
            item.setAnesthesiologistName(StringUtils.piece(x, 21));
            item.setAnesthesiologistSupervisor(StringUtils.piece(x, 22));
            item.setCirculationNurseName(StringUtils.piece(x, 23));
            item.setScrubNurseName(StringUtils.piece(x, 24));
            item.setIen(StringUtils.piece(x, 25));
            item.setConcurrentProcedure(StringUtils.piece(x, 26));
            item.setCaseScheduleType(StringUtils.piece(x, 27));
            item.setPrincipleProcedureCpt(StringUtils.piece(x, 28));
            item.setCaseLengthLeft(StringUtils.piece(x, 29));
                        
            // this RPC does not accurately the patient location or fetch the
            // patients disposition post procedure so fetch those now
            disposition(item);
            
            items.add(item);
        }
        
        return items;
        
    }
    
    
    @SuppressWarnings("unchecked")
	private synchronized void disposition(SurgeryScheduleItem i) throws BrokerException {
    
    	if (i == null) return;
    	if (i.getIen() == null || i.getIen().length() == 0) return;
    	
        if(!setContext("ARIL O.R.VIEWER 2"))
            return;

        String x;
        try{
        	Object[] params = {i.getIen()};
        	x = sCall("ALSI SURGERY DISPOSITION", params);
        }catch(Exception e){
        	return;
        }
        
        i.setPatientLocation(StringUtils.piece(x, 2));
        i.setPatientDisposition(StringUtils.piece(x, 3));
     
        
        
    }
}
