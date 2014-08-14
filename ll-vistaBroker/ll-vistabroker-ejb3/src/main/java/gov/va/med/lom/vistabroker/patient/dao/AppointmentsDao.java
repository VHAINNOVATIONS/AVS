package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.QSCallBack;
import gov.va.med.lom.javaUtils.misc.QuickSort;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.Appointment;
import gov.va.med.lom.vistabroker.patient.data.CancelledAppointment;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AppointmentsDao extends BaseDao {
  
  class QuickSortCallBack implements QSCallBack {
     
    public int compare(Object obj1, Object obj2) {
      if ((obj1 != null) && (obj2 != null)) {
        double fmDate1 = ((Appointment)obj1).getFmDatetime();
        double fmDate2 = ((Appointment)obj2).getFmDatetime();
        double c = fmDate1 - fmDate2;
        if (c < 0.0)
          return -1;
        else if (c > 0.0)
          return 1;
        else
          return 0;
      } else
         return 0;
    }
  }
  
  // CONSTRUCTORS
  public AppointmentsDao() {
    super();
  }
  
  public AppointmentsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public List<Appointment> getAppointments(String dfn, Date fromDate, 
                                                        Date throughDate) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQVS VISITS/APPTS");
    if (fromDate == null)
      fromDate = DateUtils.subtractDaysFromDate(new Date(), 30);
    if (throughDate == null)
      throughDate = DateUtils.addDaysToDate(new Date(), 180);   
    double fmDate1 = FMDateUtils.dateToFMDate(fromDate);
    double fmDate2 = FMDateUtils.dateToFMDate(throughDate);
    Object[] params = {dfn, String.valueOf(fmDate1), String.valueOf(fmDate2)};
    List<String> list = lCall(params);
    List<Appointment> appointments = new ArrayList<Appointment>();
    for(int i = list.size()-1; i >= 0; i--) {
      String x = list.get(i);
      if ((x.trim().length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
        // visit/appt id (inpatient admissions include an "a" suffix, visits suffix is"v")^ 
        // visit/appt location^header of "admitted:" or "visited:"^date/time of
        // visit or appt^inverse date/time of visit or appt
        Appointment appointment = new Appointment();
        appointment.setDfn(dfn);
        appointment.setId(StringUtils.piece(StringUtils.piece(x, 1),1));
        appointment.setLocation(StringUtils.piece(x, 2));
        appointment.setHeader(StringUtils.piece(x, 3));
        appointment.setFmDatetime(StringUtils.toDouble(StringUtils.piece(x, 4), 0));
        Date dt = FMDateUtils.fmDateTimeToDate(appointment.getFmDatetime());
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dt);
        appointment.setDatetime(gc);
        try {
          appointment.setDatetimeStr(DateUtils.toEnglishDateTime(dt));
        } catch(ParseException pe) {}
        appointment.setInverseDate(StringUtils.toDouble(StringUtils.piece(x, 5), 0));
        appointments.add(appointment);
      }
    }
    // Sort the appointments
    QuickSort quickSort = new QuickSort();
    QuickSortCallBack qsCallBack = new QuickSortCallBack();
    quickSort.setQSCallBack(qsCallBack);
    quickSort.quickSort(appointments);
    return appointments;
  } 
  
  public String getVisitDetail(String dfn, String id) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWCV DTLVST");
    Object[] params = {dfn, id};
    List<String> list = lCall(params);
    StringBuffer sb = new StringBuffer();
    for(String s : list)
      sb.append(s + "\n");
    return sb.toString().trim();
  }  
  
    /**
	 * RPC: NUMBER: 2843 NAME: ALSI APPOINTMENT CANCELLATION TAG: RD ROUTINE:
	 * ALTSDCNP RETURN VALUE TYPE: SINGLE VALUE AVAILABILITY: RESTRICTED
	 * DESCRIPTION: This RPC was developed for the Web Programmers by Gloria
	 * Dowdy, x1650.
	 * 
	 * Input is: Patient's IEN or DFN Appointment Date and Time (to be
	 * cancelled) Clinic (optional - used for extra validation)
	 * 
	 * Return is: The variable RESULT will equal: "Appointment cancelled" if
	 * successful "NOTHING CANCELLED" if unsuccessful
	 * 
	 * This development is actually a non-interactive version of the national
	 * routines used to cancel an appointment. If national development changes,
	 * it will affect this local development.
	 * 
	 * Non-interactive versions were created for the following routines:
	 * ALTSDCNP ALTSDCN0 ALTSDCN1
	 * 
	 * Input is: Patient's IEN or DFN Appointment Date and Time (to be
	 * cancelled) (Must be future date in internal format-cyymmdd.time) (Today
	 * at 10:00am is 3060922.1 Today at 10:09 is 3060922.1009) Clinic
	 * (optional - used for extra validation) (In external format Exact clinic
	 * name string  Ex: "DENTAL EXAM ROOM")
	 * 
	 * Return is: The variable RESULT will equal: If successful: 0 If not
	 * successful: "error number^error message"
	 * 
	 * Error messages: "1^Appointment not found or not future" (If clinic string
	 * is input, the following errors can occur) "2^Not a valid clinic"
	 * "3^Clinic doesn't match"
	 * 
	 * CREATOR: DOWDY,GLORIA A
	 */
  public CancelledAppointment cancelAppointment(String dfn, double apptDatetime, 
                                                             String clinic) throws Exception {
    setDefaultContext("ALT INTRANET RPCS");
    CancelledAppointment cancelledAppointment = null;
    cancelledAppointment = new CancelledAppointment();
    cancelledAppointment.setDfn(dfn);
    cancelledAppointment.setFmDatetime(apptDatetime);
    cancelledAppointment.setLocation(clinic);
    Object[] params = null;
    if ((clinic != null) && (clinic.trim().length() > 0)) {
      params = new String[3];
      params[2] = clinic;
    } else {
      params = new String[2];
    }
    params[0] = String.valueOf(dfn);
    params[1] = String.valueOf(apptDatetime);
    // Get the appointment comment
    setDefaultRpcName("ALSI APPOINTMENT COMMENTS");
    String x = sCall(params);
    String commentCode = StringUtils.piece(x, 1);
    try {
      cancelledAppointment.setCommentCode(Integer.valueOf(commentCode).intValue());
    } catch (NumberFormatException nfe) {}
    String comment = StringUtils.piece(x, 2);
    cancelledAppointment.setComment(comment);
    // Cancel the appointment
    setDefaultRpcName("ALSI APPOINTMENT CANCELLATION");
    x = sCall(params);
    if (x.equals("0")) {
      cancelledAppointment.setCancelled(true);
      cancelledAppointment.setErrorCode(0);
      cancelledAppointment.setErrorMessage("");
    } else {
      cancelledAppointment.setCancelled(false);
      String code = StringUtils.piece(x, 1);
      try {
        cancelledAppointment.setErrorCode(Integer.valueOf(code).intValue());
      } catch (NumberFormatException nfe) {}
      cancelledAppointment.setErrorMessage(StringUtils.piece(x, 2));
    }
    return cancelledAppointment;
  }
  
}
