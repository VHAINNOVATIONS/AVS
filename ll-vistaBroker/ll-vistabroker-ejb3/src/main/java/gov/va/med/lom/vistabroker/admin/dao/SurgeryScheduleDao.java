package gov.va.med.lom.vistabroker.admin.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.admin.data.SurgeryScheduleItem;
import gov.va.med.lom.vistabroker.dao.BaseDao;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class SurgeryScheduleDao extends BaseDao{
	
	private List<SurgeryScheduleItem> items;
	
	public List<SurgeryScheduleItem> fetch(GregorianCalendar date) throws Exception {
        
    	setDefaultContext(null);
    	setDefaultRpcName("ARIL GET SDATA2");
    	
        List<String> arr = null;
        
        try{
        	String start = String.valueOf(DateUtils.formatDate(date.getTime(), DateUtils.ENGLISH_DATE_FORMAT));
        	Object[] params = {start};
        	arr = lCall(params);
        }catch(Exception e){
        	return null;
        }
        
        items = new ArrayList<SurgeryScheduleItem>();
        SurgeryScheduleItem item;
        
        for (String x : arr){
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
    
    
	private void disposition(SurgeryScheduleItem i) {
    
    	if (i == null) return;
    	if (i.getIen() == null || i.getIen().length() == 0) return;
    	
    	setDefaultContext(null);
    	setDefaultRpcName("ALSI SURGERY DISPOSITION");
    	
        String x;
        try{
        	Object[] params = {i.getIen()};
        	x = sCall(params);
        }catch(Exception e){
        	return;
        }
        
        i.setPatientLocation(StringUtils.piece(x, 2));
        i.setPatientDisposition(StringUtils.piece(x, 3));
   
    }
	
    
	
}
