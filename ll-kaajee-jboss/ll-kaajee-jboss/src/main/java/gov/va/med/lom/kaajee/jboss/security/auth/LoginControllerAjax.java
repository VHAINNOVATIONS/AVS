package gov.va.med.lom.kaajee.jboss.security.auth;

import gov.va.med.lom.kaajee.jboss.model.json.LoginUserInfoVOJson;

import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.web.tomcat.security.login.WebAuthentication;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class LoginControllerAjax extends HttpServlet {

    private static final Log log = LogFactory.getLog(LoginControllerAjax.class);

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
        
        String station = request.getParameter("station");
        String institution = request.getParameter("institution");
        String access = request.getParameter("access");
        String verify = request.getParameter("verify");
        String userDuz = request.getParameter("userDuz");  
        String vistaToken = request.getParameter("vistaToken");  
        
        if ((institution != null) && (station == null)) {
          station = institution;
        }
        
        if (station != null) {
            username = station + ";" + UUID.randomUUID().toString();
        }
        
        if(access != null){
            password = access;
            
            if(verify != null){
                password += ";" + verify;
            }
        } else if (vistaToken != null) {
          password = vistaToken;
          if (userDuz != null) {
            password += "^" + userDuz;
          }
        } else if (userDuz != null) {
          password = userDuz;
        }
        
        boolean success = new WebAuthentication().login(username, password);
        
        LoginUserInfoVOJson userJson = (LoginUserInfoVOJson)
            request.getSession().getAttribute(LoginUserInfoVOJson.SESSION_KEY);
        
        if (success) {
            log.debug("login succeeded");
        } else {
            log.debug("login failed");
            // remove userJson from session
            request.getSession().removeAttribute(LoginUserInfoVOJson.SESSION_KEY);
        }
      
        writeResponse(userJson);
        
    }
    
    
    private void writeResponse(LoginUserInfoVOJson userJson) throws IOException{
        
        if (userJson == null) {
            return;
        }
        
        XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
            public HierarchicalStreamWriter createWriter(Writer writer) {
                return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
            }
        });
        
        xstream.setMode(XStream.NO_REFERENCES);
        response.getWriter().write(xstream.toXML(userJson));
        
    }

}
