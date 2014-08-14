package gov.va.med.lom.kaajee.jboss.security.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Initialization servlet to initialize KAAJEE. Should be configured to launch at application startup, for
 * an application hosting KAAJEE. Requires a parameter to pass the location of the KAAJEE configuration file.
 * Example web.xml configuration of this servlet:
 * <pre>
 *    &lt;servlet&gt;
 *     &lt;servlet-name&gt;KaajeeInit&lt;/servlet-name&gt;
 *     &lt;servlet-class&gt;gov.va.med.authentication.kernel.InitKaajeeServlet&lt;/servlet-class&gt;
 *     &lt;init-param&gt;
 *       &lt;param-name&gt;kaajee-config-file-location&lt;/param-name&gt;
 *       &lt;param-value&gt;/WEB-INF/kaajeeConfig.xml&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;load-on-startup&gt;3&lt;/load-on-startup&gt;
 *   &lt;/servlet&gt;  
 * </pre>
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
public class InitKaajeeServlet extends HttpServlet {

	private static Log log = LogFactory.getLog(InitKaajeeServlet.class);

	/**
	 * servlet parameter name to pass in the location of the KAAJEE configuration file
	 */
	public static final String CONFIG_FILE_PARAM_NAME = "kaajee-config-file-location";
	/**
	 * Runs the KAAJEE startup controller.
	 * @throws ServletException 
	 */
	public void init() throws ServletException {

		
		InputStream configFileStream = null;

		// retrieve KAAJEE configuration settings
		String configFileLocation = getInitParameter(CONFIG_FILE_PARAM_NAME);
		if (log.isDebugEnabled()) {
			log.debug("Config File Location: " + configFileLocation);
		}

		// if init-file is not set, then no point in trying
		if (configFileLocation == null) {
			if (log.isErrorEnabled()) {
				StringBuffer sb = new StringBuffer("KAAJEE configuration file location '");
				sb.append(configFileLocation);
				sb.append("' is null; can't retrieve KAAJEE settings.");
				log.error(sb.toString());
			}
		} else {

			if (log.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer("about to try to retrieve '");
				sb.append(configFileLocation);
				sb.append("'");
				log.debug(sb.toString());
			}
			try {
				// read in the XML doc
				configFileStream = getServletContext().getResourceAsStream(configFileLocation);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document configDocument = builder.parse(configFileStream);

				// pass to the startup controller
				StartupController.doStartup(configDocument);

			} catch (FactoryConfigurationError e) {
				log.error("Problem reading configuration file.", e);
			} catch (ParserConfigurationException e) {
				log.error("Problem reading configuration file.", e);
			} catch (SAXException e) {
				log.error("Problem reading configuration file.", e);
			} catch (IOException e) {
				log.error("Problem reading configuration file.", e);
			} finally {

				// close the stream
				try {
					configFileStream.close();
				} catch (IOException e) {
					if (log.isErrorEnabled()) {
						log.error("Problem closing input stream for KAAJEE configuration file.", e);
					}
				}
			}
		}
	}

	/**
	 * implements required doGet method.
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if problem encountered.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		displayInfo(response);
	}

	/**
	 * implements required doPost method.
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if problem encountered.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		displayInfo(response);
	}

	/**
	 * what to display if someone runs the servlet interactively
	 * @param response
	 */
	private void displayInfo(HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.println("InitKaajeeServlet");
		} catch (IOException e) {
			if (log.isErrorEnabled()) {
				log.error("Could not get a PrintWriter to display output.", e);
			}
		}
	}
	
	

}
