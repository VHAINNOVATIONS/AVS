package gov.va.med.lom.javaUtils.xml;

// Imported XML classes
import gov.va.med.lom.javaUtils.file.TextFile;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;

// Imported java io/util classes
import java.io.*;
import java.util.Properties;

public class XslTransform {
  
  // FIELDS
  private static Properties transformers = new Properties();

  
  // PUBLIC METHODS 
  
  public static Transformer createTransformer(String xsltFile) throws TransformerException, 
                                                                      TransformerConfigurationException { 
    /* Use the static TransformerFactory.newInstance() method to instantiate 
       a TransformerFactory. The javax.xml.transform.TransformerFactory 
       system property setting determines the actual class to instantiate --
       org.apache.xalan.transformer.TransformerImpl.
    */
    TransformerFactory tFactory = TransformerFactory.newInstance();
    /* Use the TransformerFactory to instantiate a Transformer that will work with  
       the stylesheet specified. This method call also processes the stylesheet
       into a compiled Templates object.
    */
    Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));
    /* Use the Transformer to apply the associated Templates object to an XML document
      (e.g. foo.xml) and return the output as a string.
    */
    return transformer;
  }
  
  // Transform the XML content with the named transformer
  public static String transformWithStoredXslt(String xml, String name) throws TransformerException, 
                                                                               TransformerConfigurationException, 
                                                                               IOException {
    Transformer transformer = (Transformer)transformers.get(name);
    if (transformer != null)
      return transform(xml, transformer);
    else
      return xml;
  }

  // Transform the XML content with the specified transformer object
  public static String transform(String xml, Transformer transformer) throws TransformerException, 
                                                                             TransformerConfigurationException, 
                                                                             IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    StringReader reader = new StringReader(xml);
    transformer.transform(new StreamSource(reader), new StreamResult(outputStream));
    return outputStream.toString();
  }

  // Transform the XML content with the XSLT doc
  public static String transform(String xml, String xslt) throws TransformerException, 
                                                                 TransformerConfigurationException {  
	  TransformerFactory tFactory = TransformerFactory.newInstance();
    StreamSource xsltSource = new StreamSource(new StringReader(xslt));
    Transformer transformer = tFactory.newTransformer(xsltSource);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    StringReader reader = new StringReader(xml);
	  transformer.transform(new StreamSource(reader), new StreamResult(outputStream));
    return outputStream.toString();
  }
  
  // Transform the specified XML file with the XSLT file 
  public static String transformFile(String xmlFile, String xsltFile) throws TransformerException, 
                                                                             TransformerConfigurationException {  
    Transformer transformer = createTransformer(xsltFile);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    transformer.transform(new StreamSource(xmlFile), new StreamResult(outputStream));
    return outputStream.toString();
  }

  /*
   * Perform a transformation on the specificed input XML file with the given
   * XSLT file and write the result to the output file specified.
   * 
   * Usage: java Transform <infile> <outfile> <xslt>
   * 
   */
  public static void main(String[] args) throws TransformerException, 
                                                TransformerConfigurationException, 
                                                IOException {  
    if (args.length != 3) {
      System.out.println("Usage: java Transform <infile> <outfile> <xslt>");
      return;
    }
    String inFile = args[0];
    String outFile = args[1];
    String xslFile = args[2];
    
    TextFile xmlFile = new TextFile(inFile);
    Transformer transformer = createTransformer(xslFile);
    
    String result = XslTransform.transform(xmlFile.getText(), transformer);
    gov.va.med.lom.javaUtils.file.TextFile textFile = new gov.va.med.lom.javaUtils.file.TextFile();
    textFile.setText(result);
    textFile.saveFile(outFile);
  }
}