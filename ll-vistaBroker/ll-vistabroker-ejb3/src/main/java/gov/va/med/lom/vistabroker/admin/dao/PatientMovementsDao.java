package gov.va.med.lom.vistabroker.admin.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.admin.data.PatientMovement;
import gov.va.med.lom.vistabroker.dao.BaseDao;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class PatientMovementsDao extends BaseDao {

    // CONSTRUCTORS
    public PatientMovementsDao() {
        super();
    }
    
    public PatientMovementsDao(BaseDao baseDao) {
        super(baseDao);
    }
    
    // RPC API
    public List<PatientMovement> fetch(GregorianCalendar startDate, GregorianCalendar stopDate) throws Exception {
        setDefaultContext("ALT INTRANET RPCS");
        setDefaultRpcName("ALSI PATIENT MOVEMENT");
        List<String> list = null;
        try {
        	String start = String.valueOf(DateUtils.dateTimeToFMDateTime(startDate));
        	String stop = String.valueOf(DateUtils.dateTimeToFMDateTime(stopDate));
        	Object[] params = {start,stop};
          list = lCall(params);
        } catch(Exception e){
        	return null;
        }
        List<PatientMovement> mvs = new ArrayList<PatientMovement>();
        PatientMovement mv;
        for (String s : list) {
            mv = new PatientMovement();
            mv.setId(Integer.valueOf(StringUtils.piece(s,1)));
            mv.setStationNo(getDivision());
            mv.setDate(DateUtils.fmDateTimeToDateTime(Double.valueOf(StringUtils.piece(s,2))));
            mv.setTransactionIen(StringUtils.piece(s,3));
            mv.setTransaction(StringUtils.piece(s,4));
            mv.setDfn(StringUtils.piece(s,5));
            mv.setSsn(StringUtils.piece(s,6));
            mv.setName(StringUtils.piece(s,7));
            mv.setTransactionTypeIen(StringUtils.piece(s,8));
            mv.setTransactionType(StringUtils.piece(s,9));
            mv.setWardIen(StringUtils.piece(s,10));
            mv.setWard(StringUtils.piece(s,11));
            mv.setRoom(StringUtils.piece(s,12));
            mv.setWardDischargeIen(StringUtils.piece(s,13));
            mv.setWardDischarge(StringUtils.piece(s,14));
            mvs.add(mv);
        }
        return mvs;
    }
    
}
