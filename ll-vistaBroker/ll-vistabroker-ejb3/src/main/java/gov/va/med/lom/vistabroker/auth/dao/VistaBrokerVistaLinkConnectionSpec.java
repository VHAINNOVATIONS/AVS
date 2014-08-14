package gov.va.med.lom.vistabroker.auth.dao;

import gov.va.med.crypto.VistaKernelHash;
import gov.va.med.crypto.VistaKernelHashCountLimitExceededException;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpecImpl;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This is the connection spec class to use for Access/Verify-code based re-authentication.
 * @author DAIS Infrastructure & Security Service
 * @version 1.0.0.019
 */
public class VistaBrokerVistaLinkConnectionSpec
	extends VistaLinkConnectionSpecImpl {

	/**
	 * Value used to identify type as AV
	 */
	private static final String TYPE_AV = "av";

	/**
	 * Element name given to AV type
	 */
	private static final String ELEMENT_AV = "AccessVerify";

	/**
	 * Name given to attribute representing the combined, hashed "access;verify" code
	 */
	@SuppressWarnings("unused")
	private static final String ATTRIBUTE_AVCODE = "avCode";

	/**
	 * The access code
	 */
	private String accessCode;

	/**
	 * The verify code
	 */
	private String verifyCode;

	/**
	 * The client IP address
	 */
	private String clientIp;

	/**
	 * The combined, hashed "access;verify" code
	 */
	private String avCode;

	/**
	 * The logger used by this class
	 */
	private static Log logger = LogFactory.getLog(VistaBrokerVistaLinkConnectionSpec.class);

	/**
	 * parameter-less constructor -- not very useful, use the constructor with parameters instead.
	 */
	@SuppressWarnings("deprecation")
    public VistaBrokerVistaLinkConnectionSpec() {
		super();
	}

	/**
	 * Main constructor for this connection spec.
	 * @param division station # (external format) of the division to log the user in against
	 * @param accessCode user access code
	 * @param verifyCode user verify code
	 */
	public VistaBrokerVistaLinkConnectionSpec(
		String division,
		String accessCode,
		String verifyCode,
		String clientIp) {

		super(division);
		this.accessCode = accessCode;
		this.verifyCode = verifyCode;
		this.clientIp = clientIp;
		this.avCode = "";
		try {
			this.avCode =
				VistaKernelHash.encrypt(
					accessCode + ";" + verifyCode + ";" + clientIp,
					true);
		} catch (VistaKernelHashCountLimitExceededException e) {
			logger.error("Could not encrypt access/verify code", e);
		}
	}

	/**
	 * @see gov.va.med.foundations.adapter.cci.VistaLinkConnectionSpec#getProprietaryString()
	 */
	public ArrayList<String> getProprietarySecurityInfo() {
		ArrayList<String> values = new ArrayList<String>();
		values.add(this.avCode);
		return values;
	}

	/**
	 * @see gov.va.med.foundations.adapter.cci.VistaLinkConnectionSpec#setAuthenticationNodes()
	 */
	public void setAuthenticationNodes(
		Document requestDoc,
		Node securityNode) {

		if (logger.isDebugEnabled()) {
			logger.debug("setAuthenticationNodes -> Re Auth type is 'av'");
		}

		//AC/OAK OIFO - Next line commented out and replaced by following line as required for upgrading to VL 1.5 dev17:
		//		setSecurityDivision(securityNode, this.getDivision());
		setSecurityDivisionAttr(securityNode);
		//AC/OAK OIFO - Next line commented out and replaced by following line as required for upgrading to VL 1.5 dev17:
		//		setSecurityType(securityNode, TYPE_AV);
		setSecurityTypeAttr(securityNode);

		Element elemAV = requestDoc.createElement(ELEMENT_AV);

		/* add CDATA section for encoded AV code */
		CDATASection cdata = requestDoc.createCDATASection(this.avCode);
		Node currentAvCdataNode = elemAV.getFirstChild();
		if (currentAvCdataNode != null) {
			elemAV.removeChild(currentAvCdataNode);
		}
		elemAV.appendChild(cdata);

		securityNode.appendChild(elemAV);

	}

	/**
	 * checks equality with any object
	 * @see gov.va.med.foundations.adapter.cci.VistaLinkConnectionSpec#isConnSpecEqual(java.lang.Object)
	 */
	public boolean isConnSpecEqual(Object obj) {

		//AC/OAK OIFO - Entire body below replaced by following line as required for upgrading to VL 1.5 dev17:
		//		if (obj instanceof KaajeeVistaLinkConnectionSpec) {
		//
		//			KaajeeVistaLinkConnectionSpec connSpec = (KaajeeVistaLinkConnectionSpec) obj;
		//
		//			if ((connSpec.getDivision().equals(this.getDivision()))
		//				&& (connSpec.getAccessCode().equals(this.getAccessCode()))
		//				&& (connSpec.getVerifyCode().equals(this.getVerifyCode()))
		//				&& (connSpec.getClientIp().equals(this.getClientIp()))) {
		//
		//				if (logger.isDebugEnabled()) {
		//					logger.debug("AV Conn spec is equal");
		//				}
		//				return true;
		//			}
		//		}
		//
		//		if (logger.isDebugEnabled()) {
		//			logger.debug("AV Conn spec is NOT equal");
		//		}
		//		return false;
		return equals(obj);

	}

	/**
	 * @return whether the object is equal
	 */
	public boolean equals(Object obj) {
		if (obj instanceof VistaBrokerVistaLinkConnectionSpec) {
			VistaBrokerVistaLinkConnectionSpec connSpec =
				(VistaBrokerVistaLinkConnectionSpec) obj;
			if ((connSpec.getDivision().equals(this.getDivision()))
				&& (connSpec.getAccessCode().equals(this.getAccessCode()))
				&& (connSpec.getVerifyCode().equals(this.getVerifyCode()))
				&& (connSpec.getClientIp().equals(this.getClientIp()))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the hashCode
	 */
	public int hashCode() {
		// algorithm taken from "Effective Java" item #8.
		int HASHCODE_SEED = 17;
		int returnVal = HASHCODE_SEED;

		// division contribution to hashcode
		int divisionHashCode = this.getDivision().hashCode();
		returnVal = 37 * returnVal + divisionHashCode;
		// Access code contribution to hashcode
		int accessHashCode = this.getAccessCode().hashCode();
		returnVal = 37 * returnVal + accessHashCode;
		// Verify code contribution to hashcode
		int verifyHashCode = this.getVerifyCode().hashCode();
		returnVal = 37 * returnVal + verifyHashCode;
		// Client IP contribution to hashcode
		int clientIPHashCode = this.getClientIp().hashCode();
		returnVal = 37 * returnVal + clientIPHashCode;
		return returnVal;
	}

	/**
	 * @return the client ip address
	 */
	public String getClientIp() {
		return this.clientIp;
	}

	/**
	 * @return the internal access code
	 */
	public String getAccessCode() {
		return accessCode;
	}

	/**
	 * @return the internal verify code
	 */
	public String getVerifyCode() {
		return verifyCode;
	}

	/**
	 * returns the security type.
	 * @see gov.va.med.foundations.adapter.cci.VistaLinkConnectionSpec#getSecurityType()
	 */
	public String getSecurityType() {
		return TYPE_AV;
	}
}
