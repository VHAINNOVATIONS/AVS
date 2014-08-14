package gov.va.med.lom.vistabroker.admin.data;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class ScheduledAdmission implements Serializable{

	private int id;
	private String dfn;
	private String name;
	private String ssn;
	private GregorianCalendar admitDate;
	private int lengthOfStay;
	private String diagnosis;
	private String provider;
	private boolean surgeryRequired;
	private String wardIen;
	private String wardName;
	private String treatingSpecialty;
	private String scheduler;
	private String stationNo;
	private GregorianCalendar cancelledDate;
	private String cancelledBy;
	private String cancelledReason;
	private boolean transfer;
	
	public boolean isTransfer() {
		return transfer;
	}
	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
	}
	public GregorianCalendar getAdmitDate() {
		return admitDate;
	}
	public void setAdmitDate(GregorianCalendar admitDate) {
		this.admitDate = admitDate;
	}
	public String getCancelledBy() {
		return cancelledBy;
	}
	public void setCancelledBy(String cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	public String getCancelledReason() {
		return cancelledReason;
	}
	public void setCancelledReason(String cancelledReason) {
		this.cancelledReason = cancelledReason;
	}
	public GregorianCalendar getCancelledDate() {
		return cancelledDate;
	}
	public void setCancelledDate(GregorianCalendar cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	public String getDfn() {
		return dfn;
	}
	public void setDfn(String dfn) {
		this.dfn = dfn;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLengthOfStay() {
		return lengthOfStay;
	}
	public void setLengthOfStay(int lengthOfStay) {
		this.lengthOfStay = lengthOfStay;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getScheduler() {
		return scheduler;
	}
	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getStationNo() {
		return stationNo;
	}
	public void setStationNo(String stationNo) {
		this.stationNo = stationNo;
	}
	public boolean isSurgeryRequired() {
		return surgeryRequired;
	}
	public void setSurgeryRequired(boolean surgeryRequired) {
		this.surgeryRequired = surgeryRequired;
	}
	public String getTreatingSpecialty() {
		return treatingSpecialty;
	}
	public void setTreatingSpecialty(String treatingSpecialty) {
		this.treatingSpecialty = treatingSpecialty;
	}
	public String getWardIen() {
		return wardIen;
	}
	public void setWardIen(String wardIen) {
		this.wardIen = wardIen;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	
	
}
