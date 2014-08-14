package gov.va.med.lom.kaajee.jboss.security.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

/**
 * Performs KAAJEE startup actions. Called at startup by, for example, the InitKaajeeServlet class, if
 * configured by the enclosing application.
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
class StartupController {

	private static Log log = LogFactory.getLog(StartupController.class);

	static void doStartup(Document kaajeeConfigDocument /* InputStream configFileStream , InputStream webXmlStream */) {

		// retrieve KAAJEE configuration settings
		// if init-file is not set, then no point in trying
		if (kaajeeConfigDocument == null) {
			if (log.isErrorEnabled()) {
				log.error("KAAJEE configuration document object is null; can't retrieve KAAJEE settings.");
			}
		} else {

			// initialize application roles
			ApplicationRoleListVOSingleton.createInstance(kaajeeConfigDocument);
			// initialize KAAJEE configuration object.
			ConfigurationVO.createInstance(kaajeeConfigDocument);
		}

	//	DAOFactory oracleDAOFactory = DAOFactory.getDAOFactory();

		// purge one-time token file
	/*	ILoginTokenDAO loginTokenDAO = oracleDAOFactory.getLoginTokenDAO();
		loginTokenDAO.purgeTable();

		// purge role table
		ILoginRoleDAO loginRoleDAO = oracleDAOFactory.getLoginRoleDAO();
		loginRoleDAO.purgeTable();           */
	}
}
