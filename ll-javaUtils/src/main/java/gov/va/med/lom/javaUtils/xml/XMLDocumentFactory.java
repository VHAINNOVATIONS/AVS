package gov.va.med.lom.javaUtils.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/*
 * A class for manipulating the entire xml file (reading, writing...).
 */
public class XMLDocumentFactory {

   private static String[] properties = {
      "method",
      "version",
      "encoding",
      "omit-xml-declaration",
      "standalone",
      "doctype-public",
      "doctype-system",
      "indent",
      "media-type"
   };


   private static int METHOD = 0;
   private static int VERSION = 1;
   private static int ENCODING = 2;
   private static int OMIT_XML_DECLARATION = 3;
   private static int STANDALONE = 4;
   private static int DOCTYPE_PUBLIC = 5;
   private static int DOCTYPE_SYSTEM = 6;
   private static int CDATA_SECTION_ELEMENTS = 7;
   private static int INDENT = 8;
   private static int MEDIA_TYPE = 9;

  /*
   * xml file name.
   */
  private String fileName;


  /*
   * Constructs an empty XMLDocumentFactory
   */
  public XMLDocumentFactory() {
  }


  /*
   * Constructs a XMLDocumentFactory with the given
   * xml file name as String
   */
  public XMLDocumentFactory(String fileName) {
    this.fileName = fileName;
  }


  /*
   * Returns xml file name.
   */
  public String getFileName() {
    return this.fileName;
  }


  /*
   * Parses xml file with the given name and creates Document.
   */
  public static Document parse(String fileName) {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     try {
       DocumentBuilder builder = factory.newDocumentBuilder();
       builder.setErrorHandler(new UtilErrorHandler());
       Document doc = builder.parse(fileName);
       return doc;
     } catch (SAXParseException e) {
       e.printStackTrace();
     } catch (ParserConfigurationException e) {
       e.printStackTrace();
     } catch (IOException e) {
       e.printStackTrace();
     } catch (SAXException e) {
       e.printStackTrace();
     }
      return null;

  }


  /*
   * Parses xml file and creates creates Document.
   */
  public Document parse() {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setErrorHandler(new UtilErrorHandler());
      Document doc = builder.parse(fileName);
      return doc;
      } catch (SAXParseException e) {
      e.printStackTrace();
      } catch (ParserConfigurationException e) {
      e.printStackTrace();
      } catch (IOException e) {
      e.printStackTrace();
      } catch (SAXException e) {
      e.printStackTrace();
      }
      return null;
  }


  /*
   * Serializes node with all subnodes to the xml file with the given name,
   * and with the Properties of the xml declaration.
   */
  public static void serialize(Node node, String fileName, Properties prop) {
    String out = "<?xml version=\"1.0\"?>";
    File file = new File(fileName);
    
    //serialize xml declaration
    if (prop != null) {
     out = "<?xml";
     String str = "";
     for (int i=0; i<properties.length; i++) {
      str = (String)prop.get(properties[i]);
      if (str != null)
         out += " "+properties[i]+"=\""+str+"\"";
     }
     out += "?>";
    }
    
    //serialize document
    try {
     FileOutputStream outStream = new FileOutputStream(file);
     out += node.toString();
     outStream.write(out.getBytes());
     outStream.close();
    } catch(Exception e) {
     System.err.println("Error serializing file");
    }
  }


  /*
   * Serializes node with all subnodes to the xml file 
   * with the default Properties of the xml declaration.
   */
  public void serialize(Node node) {   
    File file = new File(fileName);
    try {
      FileOutputStream outStream = new FileOutputStream(file);
      outStream.write(node.toString().getBytes());
      outStream.close();
    } catch(Exception e) {
      System.err.println("Error serializing file");
    }
  }

   static class UtilErrorHandler implements ErrorHandler
  {

  // throw SAXException for fatal errors
  public void fatalError( SAXParseException exception ) throws SAXException
  {
    throw new SAXException(exception);
  }

  public void error( SAXParseException errorException ) throws SAXException
  {
    throw new SAXException(errorException);
  }

  // print any warnings
  public void warning( SAXParseException warningError ) throws SAXException
  {
    System.err.println("[Validation : Warning] URI = " + warningError.getMessage());
  }
  }



}
