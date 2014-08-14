package gov.va.med.lom.kaajee.jboss.security.auth;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.web.tomcat.security.login.WebAuthentication;

public class LoginController extends HttpServlet {

    private static final Log log = LogFactory.getLog(LoginController.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        this.request = request;
        this.response = response;
        login();

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void login() throws IOException {

        String username = null;
        String password = null;
        
        String station = request.getParameter("STATION");
        String access = request.getParameter("ACCESS");
        String verify = request.getParameter("VERIFY");
        String userDuz = request.getParameter("USERDUZ");  
        
        
        if(station != null){
            username = station + ";" + UUID.randomUUID().toString();
        }
        
        if(access != null){
            password = access;
            
            if(verify != null){
                password += ";" + verify;
            }
        } else if (userDuz != null) {
            password = userDuz;
        }
        
        if (new WebAuthentication().login(username, password)) {

           log.debug("login succeeded");
           log.debug("attempting to redirect to " + request.getHeader("Referer"));
           String redirectUrl = request.getHeader("Referer");
           if(redirectUrl != null){
               response.sendRedirect(redirectUrl);
           }
           

        } else {
            log.info("login failed");
            response.sendRedirect(request.getContextPath() + "/login/login-err.jsp");
        }

    }

}
