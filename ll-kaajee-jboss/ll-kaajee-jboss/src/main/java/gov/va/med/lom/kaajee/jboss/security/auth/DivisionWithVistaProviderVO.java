package gov.va.med.lom.kaajee.jboss.security.auth;

/**
 * Value object used to link a division and it's Vista Provider, both values based on station #
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
public class DivisionWithVistaProviderVO {
	
	private String divisionStationNumber;
	private String vistaProviderStationNumber;

	/**
	 * Station number of the division
	 * @param stationNumber
	 */	
	void setDivisionStationNumber(String stationNumber) {
		this.divisionStationNumber = stationNumber;
	}
	
	/** 
	 * 
	 * @return station number of the division
	 */
	String getDivisionStationNumber() {
		return divisionStationNumber;
	}
	
	/**
	 * station number of the Vista Provider (assumed to be retrieved via SDS institution utilities)
	 * @param stationNumber
	 */
	void setVistaProviderStationNumber(String stationNumber) {
		this.vistaProviderStationNumber = stationNumber;
	}

	/**
	 * 
	 * @return station number of the division's Vista Provider
	 */	
	String getVistaProviderStationNumber() {
		return this.vistaProviderStationNumber;
	}
}
