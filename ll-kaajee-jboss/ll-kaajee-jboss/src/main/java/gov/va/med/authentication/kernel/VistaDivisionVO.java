package gov.va.med.authentication.kernel;

import java.io.Serializable;

/**
 * Represents a Vista Division, Station Name and Station Number.
 * @author Application Modernization - Foundations Team
 * @version 1.1.0.007
 */
public class VistaDivisionVO implements Serializable {

	private String divisionName;
	private String stationNumber;
	private boolean isDefault;
	;

	/**
	 * Instantiates a VistaDivisionVO with all fields set to the null string.
	 */
	public VistaDivisionVO() {
		divisionName = "";
		stationNumber = "";
		isDefault = false;
	}

	/**
	 * returns the station name of the division, presumably from the VistA Institution file entry
	 * (depending on the source of the information the instance contains)
	 * @return the name of the division
	 */
	public String getName() {
		return divisionName;
	}

	/**
	 * Sets the name of the division.
	 * @param divisionName name to set.
	 */
	public void setName(String divisionName) {
		this.divisionName = divisionName;
	}

	/**
	 * returns the station number of the division, presumably from the VistA Institution file entry
	 * (depending on the source of the information the instance contains)
	 * @return the station number of the division
	 */
	public String getNumber() {
		return stationNumber;
	}

	/**
	 * Sets the station number
	 * @param stationNumber station number to set.
	 */
	public void setNumber(String stationNumber) {
		this.stationNumber = stationNumber;
	}

	/**
	 * gets whether this is set to the default login division
	 * @return true if default
	 */
	public boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * sets the default login division
	 * @param isDefault true if default, false if not
	 */
	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * returns a string representation of the division information
	 * @return a string with name and number labels and values.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(" Division Name: ");
		sb.append(divisionName);
		sb.append(" Station Number: ");
		sb.append(stationNumber);
		sb.append(" Is Default: ");
		sb.append(isDefault);
		return sb.toString();

	}
}
