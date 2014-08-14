package gov.va.med.lom.json.util;

import java.lang.reflect.Type;
import java.io.Writer;
import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import com.google.gson.*;

public class JsonResponse {

  private static final Log log = LogFactory.getLog(JsonResponse.class);
  
  /**
   * Used when the request is being made without 
   * an authenticated session.  This should be needed
   * very rarely because this situation should be taken
   * care of by the Kaajee session handler.
   * 
   * @param response
   */
  
  public static void userUnauthorized(HttpServletResponse response) {   
    response.setStatus(403); 
    flushJson(response, JsonHtmlStrings.get(JsonHtmlStrings.JSON_RESPONSE_UNAUTHORIZED));
  }    
  
  /**
   * used when the user has made a request 
   * -- on an authenticated session -- but the user
   * does not have the authorization to complete the request.
   * 
   * @param response
   */
  public static void userNotAllowed(HttpServletResponse response) {    
    response.setStatus(401);
    flushJson(response, JsonHtmlStrings.get(JsonHtmlStrings.JSON_RESPONSE_NOT_ALLOWED));
  }
  
  /**
   * used when an error condition has arisen 
   * 
   * @param response
   * @param error
   */
  public static void error(HttpServletResponse response, String error) {    
    flushJson(response, String.format(JsonHtmlStrings.get(JsonHtmlStrings.JSON_RESPONSE_ERROR), error));
  }
    
  /**
   * basic success response for use when no other data is required to
   * be sent with response
   * 
   * @param response
   */
  public static void success(HttpServletResponse response) {
    flushJson(response, JsonHtmlStrings.get(JsonHtmlStrings.JSON_RESPONSE_SUCCESS));
  }
  
  /**
   * success response with an accompanying message
   * 
   * @param response
   * @param msg
   */
  public static void success(HttpServletResponse response, String msg) {   
    flushJson(response, String.format(JsonHtmlStrings.get(JsonHtmlStrings.JSON_RESPONSE_SUCCESS_MSG), msg));
  }  
  
  public static void flushJson(HttpServletResponse response, String json) {
    //System.out.println(json);
    try {
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      response.setContentType("application/json");
      response.setContentLength(json.length());
      response.getWriter().write(json);
    } catch (Exception e) {
      log.error("error creating json data", e);
    }
  }  
  
  public static void flushPdf(HttpServletResponse response, String filename, ByteArrayOutputStream buffer) {
    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    flushPdf(response, buffer);
  }
  
  public static void flushPdf(HttpServletResponse response, ByteArrayOutputStream buffer) {
    try {
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      response.setContentType("application/pdf");
      response.setContentLength(buffer.size());
      ServletOutputStream out = response.getOutputStream();
      buffer.writeTo(out);
      out.flush();
    } catch (Exception e) {
      log.error("error creating json data", e);
    }
  }  
  
  public static void flushText(HttpServletResponse response, String filename, String text) {
      response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
      flushText(response, text);
  }  
  
  public static void flushText(HttpServletResponse response, String text) {
    try {
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      response.setContentType("text/plain");
      response.setContentLength(text.length());
      response.getWriter().write(text);
    } catch (Exception e) {
      log.error("error creating json data", e);
    }
  }  
     
  
  public static void flushCsv(HttpServletResponse response, String filename, String csv) {
    try {
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
      response.setContentType("text/csv");
      response.setContentLength(csv.length());
      response.getWriter().write(csv);
    } catch (Exception e) {
      log.error("error creating json data", e);
    }
  }   
  
  public static void embedRoot(HttpServletResponse response, boolean success, int totalCount, 
                               String root, boolean single, Object embed){    
    String base = single ? JsonHtmlStrings.get(JsonHtmlStrings.JSON_RESPONSE_BASE_SINGLE) :
                  JsonHtmlStrings.get(JsonHtmlStrings.JSON_RESPONSE_BASE_MULTIPLE);
    String j = serializeJson(embed);
    String result = String.format(base, success ? "true" : "false", totalCount, root, j);
    flushJson(response, result);
  }        
  
  public static void writeJson(HttpServletResponse response, Object o){    
    flushJson(response, serializeJson(o));    
  }
  
  public static void writeXml(HttpServletResponse response, Object o){    
    flushJson(response, serializeXml(o));    
  }  
  
  public static String serializeJson(Object o) {
    XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
        public HierarchicalStreamWriter createWriter(Writer writer) {
            return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
        }
    });
    xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
    return xstream.toXML(o);
  }
  
  public static String serializeXml(Object o) {
    XStream xstream = new XStream(new DomDriver());
    xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
    return xstream.toXML(o);
  }  
  
  public static Object deserializeJson(String json, Class cls) {
    Gson gson = new Gson();
    return gson.fromJson(json, cls);
  }
  
  public static Object deserializeJson(String json, Type collectionType) {
    Gson gson = new Gson();
    return gson.fromJson(json, collectionType);
  }  
  
}
