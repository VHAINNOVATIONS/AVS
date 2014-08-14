package gov.va.med.lom.kaajee.jboss.security.auth;

import gov.va.med.authentication.kernel.KaajeeException;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Filters used to filter lists of divisions based on various criteria.
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
class LoginUserInfoDivisionFilters {

	private static final Log log = LogFactory.getLog(LoginUserInfoDivisionFilters.class);

	/**
	 * Filter out divisions from a Treemap of divisions (key must be station number string).
	 * Divisions are filtered out if they aren't present in the KAAJEE configured division list.
	 * @param loginDivisionStationNumber used for logging purposes only
	 * @param divisionMap division list to filter
	 * @param userDuz used for logging purposes only
	 * @param configVO Kaajee configuration info
	 */
	static void isDivisionConfiguredInKaajeeFilter(
		String loginDivisionStationNumber,
		TreeMap<?, ?> divisionMap,
		String userDuz,
		ConfigurationVO configVO) {

		Set<?> keySet = divisionMap.keySet();
		Iterator<?> it = keySet.iterator();
		while (it.hasNext()) {
			String stationNumber = (String) it.next();
			if (!configVO.isKaajeeLoginDivision(stationNumber)) {
				it.remove();
				if (log.isErrorEnabled()) {
					StringBuffer sb = new StringBuffer("Division '");
					sb.append(stationNumber);
					sb.append("' in New Person file for user DUZ '");
					sb.append(userDuz);
					sb.append("' at facility '");
					sb.append(loginDivisionStationNumber);
					sb.append("' not configured as a KAAJEE login division. Not adding to permitted divisions.");
					log.error(sb.toString());
				}
			}
		}
	}

	/**
	 * Filter out divisions from a Treemap of divisions (key must be station number string).
	 * Treemap entries are removed if:
	 * 1. The entry does not have a corresponding SDS Institution table entry.
	 * 2. The entry's Vista Provider is different than the Vista Provider of the specified login division.
	 * In addition, if there is a problem with the specified login division (can't retrieve from Institution file,
	 * can't retrieve Vista Provider, etc.) all division entries are removed from the Treemap.
	 * @param loginDivisionStationNumber the login division; used to obtain Vista Computing facility.
	 * @param divisionMap division map to filter based on Vista Computing facility of login division.
	 * @param userDuz used for logging purposes only.
	 * @throws KaajeeException thrown if a fundamental problem encountered with the specified login division.
	 */
	static void sameVistaProviderFilter(String loginDivisionStationNumber, TreeMap<?, ?> divisionMap, String userDuz)
		throws KaajeeException {

		if (log.isDebugEnabled()) {
			log.debug("In sameVistaProviderFilter.");
		}

		DivisionWithVistaProviderVO loginDiv;

		// validate the login station #
		if ((loginDivisionStationNumber == null) || (loginDivisionStationNumber.length() == 0)) {

			divisionMap.clear();
			StringBuffer sb = new StringBuffer("For user DUZ '");
			sb.append(userDuz);
			sb.append("', Login Division Station Number parameter was null or of length 0. Removing all divisions from the user division list.");
			throw new KaajeeException(sb.toString());
		}

		try {
			loginDiv = LoginControllerUtils.getValidatedVistaProvider(loginDivisionStationNumber);
		} catch (KaajeeException e) {
			divisionMap.clear();
			throw new KaajeeException("Login Division Number passed to sameVistaProviderFilter was invalid.", e);
		}

		// validate candidate divisions passed in
		Set<?> keySet = divisionMap.keySet();
		Iterator<?> it = keySet.iterator();
		while (it.hasNext()) {

			DivisionWithVistaProviderVO candidateDiv;
			String stationNumber = (String) it.next();
			if (log.isDebugEnabled()) {
				log.debug("Processing station # " + stationNumber);
			}

			try {
				
				candidateDiv = LoginControllerUtils.getValidatedVistaProvider(stationNumber);
				
			} catch (KaajeeException e) {
				
				// filter out if not in Institution table, or without a Vista Provider identified.
				it.remove();
				if (log.isErrorEnabled()) {
					StringBuffer sb = new StringBuffer("Division '");
					sb.append(stationNumber);
					sb.append("' in New Person file for user DUZ '");
					sb.append(userDuz);
					sb.append("' at facility '");
					sb.append(loginDiv.getDivisionStationNumber());
					sb.append("' not valid as a permitted division. ");
					log.error(sb.toString(), e);
				}
				continue;
			}

			// filter out if Vista Provider is different than that of the login division.
			if (!(loginDiv.getVistaProviderStationNumber().equals(candidateDiv.getVistaProviderStationNumber()))) {
				it.remove();
				if (log.isErrorEnabled()) {
					StringBuffer sb = new StringBuffer("Division '");
					sb.append(candidateDiv.getDivisionStationNumber());
					sb.append("' in New Person file for user DUZ '");
					sb.append(userDuz);
					sb.append("' at facility '");
					sb.append(loginDiv.getDivisionStationNumber());
					sb.append("' has a different Vista Provider (");
					sb.append(candidateDiv.getVistaProviderStationNumber());
					sb.append(") identified in Institution table that identified for the login system: (");
					sb.append(loginDiv.getVistaProviderStationNumber());
					sb.append("). Not adding to permitted divisions.");
					log.error(sb.toString());
				}
				continue;
			}

			// success
			if (log.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer("Accepted division: '");
				sb.append(candidateDiv.getDivisionStationNumber());
				sb.append("' from New Person file for user DUZ '");
				sb.append(userDuz);
				sb.append("' with Vista Provider station number: '");
				sb.append(candidateDiv.getVistaProviderStationNumber());
				sb.append("' and Login station number: ");
				sb.append(loginDiv.getDivisionStationNumber());
				sb.append("' and Login station Vista Provider station number: '");
				sb.append(loginDiv.getVistaProviderStationNumber());
				sb.append("'.");
				log.debug(sb.toString());
			}
		}
	}

}
