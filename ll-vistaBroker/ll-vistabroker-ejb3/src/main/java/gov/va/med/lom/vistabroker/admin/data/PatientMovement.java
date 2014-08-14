package gov.va.med.lom.vistabroker.admin.data;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class PatientMovement implements Serializable{

	private int id;
	private String stationNo;
	private GregorianCalendar date;
	private String transactionIen;
	private String transaction;
	private String dfn;
	private String ssn;
	private String name;
	private String transactionTypeIen;
	private String transactionType;
	private String wardIen;
	private String ward;
	private String room;
	private String wardDischargeIen;
	private String wardDischarge;
	
	public GregorianCalendar getDate() {
		return date;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	public String getDfn() {
		return dfn;
	}
	public void setDfn(String dfn) {
		this.dfn = dfn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getTransaction() {
		return transaction;
	}
	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	public String getTransactionIen() {
		return transactionIen;
	}
	public void setTransactionIen(String transactionIen) {
		this.transactionIen = transactionIen;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionTypeIen() {
		return transactionTypeIen;
	}
	public void setTransactionTypeIen(String transactionTypeIen) {
		this.transactionTypeIen = transactionTypeIen;
	}
	public String getWard() {
		return ward;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public String getWardDischarge() {
		return wardDischarge;
	}
	public void setWardDischarge(String wardDischarge) {
		this.wardDischarge = wardDischarge;
	}
	public String getWardDischargeIen() {
		return wardDischargeIen;
	}
	public void setWardDischargeIen(String wardDischargeIen) {
		this.wardDischargeIen = wardDischargeIen;
	}
	public String getWardIen() {
		return wardIen;
	}
	public void setWardIen(String wardIen) {
		this.wardIen = wardIen;
	}
	public String getStationNo() {
		return stationNo;
	}
	public void setStationNo(String stationNo) {
		this.stationNo = stationNo;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	
}
