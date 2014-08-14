package gov.va.med.lom.vistabroker.service.impl;

import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.misc.dao.*;
import gov.va.med.lom.vistabroker.misc.data.*;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.service.MiscVBService;

import java.io.Serializable;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless(name = "gov.va.med.lom.vistabroker.MiscVBService")
@Remote(MiscVBService.class)
public class MiscVBServiceImpl extends BaseService implements MiscVBService, Serializable {

	/*
	 * CONSTRUCTORS
	 */
	public MiscVBServiceImpl() {
		super();
	}

	/*
	 * BUSINESS METHODS
	 */

	// Date/Time RPCs
	public ServiceResponse<Double> strToFMDateTime(
			ISecurityContext securityContext, String str) {
		setSecurityContext(securityContext);
		return singleResult(Double.class, MiscRpcsDao.class, "strToFMDateTime",
				str);
	}

	public ServiceResponse<Double> validDateTimeStr(
			ISecurityContext securityContext, String str, String flags) {
		setSecurityContext(securityContext);
		String[] params = { str, flags };
		return singleResult(Double.class, MiscRpcsDao.class,
				"validDateTimeStr", params);
	}

	public ServiceResponse<Double> fmNow(ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return singleResult(Double.class, MiscRpcsDao.class, "fmNow");
	}

	public ServiceResponse<Integer> fmToday(ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return singleResult(Integer.class, MiscRpcsDao.class, "fmToday");
	}

	// VistA Station RPC
	public ServiceResponse<VistaStation> getStation(
			ISecurityContext securityContext, String stationNo) {
		setSecurityContext(securityContext);
		return singleResult(VistaStation.class, VistaStationDao.class,
				"getStation", stationNo);
	}

	// Other Misc RPCs
	public ServiceResponse<String> externalName(
			ISecurityContext securityContext, String ien, double fileNumber) {
		setSecurityContext(securityContext);
		String[] params = { ien, String.valueOf(fileNumber) };
		return singleResult(String.class, MiscRpcsDao.class, "externalName",
				params);
	}
	
	public ServiceResponse<String> getVariableValue(ISecurityContext securityContext, String arg) {
    setSecurityContext(securityContext);
    String[] params = { arg };
    return singleResult(String.class, MiscRpcsDao.class, "getVariableValue", params);	  
	}

}
