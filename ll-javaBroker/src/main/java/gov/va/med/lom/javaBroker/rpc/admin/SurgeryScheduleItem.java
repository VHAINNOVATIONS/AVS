package gov.va.med.lom.javaBroker.rpc.admin;

import java.io.Serializable;

public class SurgeryScheduleItem implements Serializable {

	private String ien;
	private String operatingRoom;
	private String patientName;
	private String last4;
	private String patientLocation;
	private String principleProcedure;
	private String otherProcedure;
	private String scheduledStartTime;
	private String estmatedCompletionTime;
	private String caseLength;
	private String operationEndTime;
	private String status;   // scheduled, canceled, in progress, completed
	private String timeInOr;
	private String inductionCompletionTime;
	private String actualBeginTime;
	private String actualEndTime;
	private String timeOutOfOr;
	private String orOccupancyTime;
	private String surgeonName;
	private String attendingName;
	private String anesthesiologistName;
	private String anesthesiologistSupervisor;
	private String circulationNurseName;
	private String scrubNurseName;
	private String concurrentProcedure;
	private String caseScheduleType;
	private String principleProcedureCpt;
	private String caseLengthLeft;
	private String patientDisposition;
	
	public String getIen() {
		return ien;
	}
	public void setIen(String ien) {
		this.ien = ien;
	}
	public String getOperatingRoom() {
		return operatingRoom;
	}
	public void setOperatingRoom(String operatingRoom) {
		this.operatingRoom = operatingRoom;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getLast4() {
		return last4;
	}
	public void setLast4(String last4) {
		this.last4 = last4;
	}
	public String getPatientLocation() {
		return patientLocation;
	}
	public void setPatientLocation(String patientLocation) {
		this.patientLocation = patientLocation;
	}
	public String getPrincipleProcedure() {
		return principleProcedure;
	}
	public void setPrincipleProcedure(String principleProcedure) {
		this.principleProcedure = principleProcedure;
	}
	public String getOtherProcedure() {
		return otherProcedure;
	}
	public void setOtherProcedure(String otherProcedure) {
		this.otherProcedure = otherProcedure;
	}
	public String getScheduledStartTime() {
		return scheduledStartTime;
	}
	public void setScheduledStartTime(String scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}
	public String getEstmatedCompletionTime() {
		return estmatedCompletionTime;
	}
	public void setEstmatedCompletionTime(String estmatedCompletionTime) {
		this.estmatedCompletionTime = estmatedCompletionTime;
	}
	public String getCaseLength() {
		return caseLength;
	}
	public void setCaseLength(String caseLength) {
		this.caseLength = caseLength;
	}
	public String getOperationEndTime() {
		return operationEndTime;
	}
	public void setOperationEndTime(String operationEndTime) {
		this.operationEndTime = operationEndTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTimeInOr() {
		return timeInOr;
	}
	public void setTimeInOr(String timeInOr) {
		this.timeInOr = timeInOr;
	}
	public String getInductionCompletionTime() {
		return inductionCompletionTime;
	}
	public void setInductionCompletionTime(String inductionCompletionTime) {
		this.inductionCompletionTime = inductionCompletionTime;
	}
	public String getActualBeginTime() {
		return actualBeginTime;
	}
	public void setActualBeginTime(String actualBeginTime) {
		this.actualBeginTime = actualBeginTime;
	}
	public String getActualEndTime() {
		return actualEndTime;
	}
	public void setActualEndTime(String actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	public String getTimeOutOfOr() {
		return timeOutOfOr;
	}
	public void setTimeOutOfOr(String timeOutOfOr) {
		this.timeOutOfOr = timeOutOfOr;
	}
	public String getOrOccupancyTime() {
		return orOccupancyTime;
	}
	public void setOrOccupancyTime(String orOccupancyTime) {
		this.orOccupancyTime = orOccupancyTime;
	}
	public String getSurgeonName() {
		return surgeonName;
	}
	public void setSurgeonName(String surgeonName) {
		this.surgeonName = surgeonName;
	}
	public String getAttendingName() {
		return attendingName;
	}
	public void setAttendingName(String attendingName) {
		this.attendingName = attendingName;
	}
	public String getAnesthesiologistName() {
		return anesthesiologistName;
	}
	public void setAnesthesiologistName(String anesthesiologistName) {
		this.anesthesiologistName = anesthesiologistName;
	}
	public String getAnesthesiologistSupervisor() {
		return anesthesiologistSupervisor;
	}
	public void setAnesthesiologistSupervisor(String anesthesiologistSupervisor) {
		this.anesthesiologistSupervisor = anesthesiologistSupervisor;
	}
	public String getCirculationNurseName() {
		return circulationNurseName;
	}
	public void setCirculationNurseName(String circulationNurseName) {
		this.circulationNurseName = circulationNurseName;
	}
	public String getScrubNurseName() {
		return scrubNurseName;
	}
	public void setScrubNurseName(String scrubNurseName) {
		this.scrubNurseName = scrubNurseName;
	}
	public String getConcurrentProcedure() {
		return concurrentProcedure;
	}
	public void setConcurrentProcedure(String concurrentProcedure) {
		this.concurrentProcedure = concurrentProcedure;
	}
	public String getCaseScheduleType() {
		return caseScheduleType;
	}
	public void setCaseScheduleType(String caseScheduleType) {
		this.caseScheduleType = caseScheduleType;
	}
	public String getPrincipleProcedureCpt() {
		return principleProcedureCpt;
	}
	public void setPrincipleProcedureCpt(String principleProcedureCpt) {
		this.principleProcedureCpt = principleProcedureCpt;
	}
	public String getCaseLengthLeft() {
		return caseLengthLeft;
	}
	public void setCaseLengthLeft(String caseLengthLeft) {
		this.caseLengthLeft = caseLengthLeft;
	}
	
	/**
	 * where patient to be disposed post procedure 
	 * 
	 */
	public String getPatientDisposition() {
		return patientDisposition;
	}
	public void setPatientDisposition(String patientDisposition) {
		this.patientDisposition = patientDisposition;
	}
	
	
	
}
