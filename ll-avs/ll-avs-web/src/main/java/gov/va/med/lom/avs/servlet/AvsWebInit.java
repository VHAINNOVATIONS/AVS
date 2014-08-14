package gov.va.med.lom.avs.servlet;

import gov.va.med.term.access.Institution;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AvsWebInit extends HttpServlet{
	
	private static final long serialVersionUID = 0;

	private static final Log log = LogFactory.getLog(AvsWebInit.class);

	/**
	 * Initialize the sample application.
	 */
	public void init() {
		Institution.factory.obtainFromKeyString("1002222").getVisnDivisions();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		displayInfo(response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		displayInfo(response);
	}

	private void displayInfo(HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>Init Servlet</title></head>");
			out.println("<body>");
			out.println("<p>Init Servlet</p>");
			out.println("</body></html>");
		} catch (IOException e) {
			log.error("Could not get a PrintWriter to display output.", e);
		}
	}
	
}
