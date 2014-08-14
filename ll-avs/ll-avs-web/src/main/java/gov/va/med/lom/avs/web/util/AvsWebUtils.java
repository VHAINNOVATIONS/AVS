package gov.va.med.lom.avs.web.util;

import gov.va.med.lom.foundation.service.response.BaseServiceResponse;
import gov.va.med.lom.foundation.service.response.messages.Message;
import gov.va.med.lom.foundation.service.response.messages.Messages;
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;

import org.apache.commons.lang.WordUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;

import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;

public class AvsWebUtils {

  static final char[] capitalizingDelimiters = {' ', '/', ',', '&', '(', ')', '-'};
  static final String[] htmlElements = {"<html>", "</html>", "<head>", "<head />", "</head>",
                                        "<body>", "</body>"};
  
  static final String NORMAL_FONT_CLS = "normalFont";
  static final String LARGE_FONT_CLS = "largeFont";
  static final String VERY_LARGE_FONT_CLS = "veryLargeFont";
  
  public static void handleServiceErrors(BaseServiceResponse response, Log log) throws RuntimeException {
    handleServiceErrors(response, null, log);
  }

  public static void handleServiceErrors(BaseServiceResponse response, String prefix, Log log) throws RuntimeException {

    if (!BaseServiceResponse.hasErrorMessage(response)) {
      return;
    }

    StringBuffer sb = new StringBuffer();
    sb.append("ERROR: ");
    if (prefix != null) {
      sb.append(prefix);
    }
    sb.append(":\n");
    Messages messages = response.getMessages();
    Iterator<Message> it = messages.getErrorMessages().iterator();
    while (it.hasNext()) {
      Message msg = it.next();
      sb.append(msg.getKey()).append("\n");
    }
    String message = sb.toString();
    log.error(message);
    System.out.println(message);
    throw new RuntimeException(message);
  }
  
  public static String renderUnorderedList(List<String> items) {

    StringBuffer html = new StringBuffer();

    html.append("<ul>\n");
    for (String item : items) {
      html.append("\t<li>").append(item).append("</li>\n");
    }
    html.append("</ul>\n");
    
    return html.toString();
  }
  
  public static String renderTwoColumnList(List<String> items) {
    return renderTwoColumnList(items, 0);    
  }
  
  public static String renderTwoColumnList(List<String> items, boolean wrap, String fontClass) {
    int maxLen = 0;
    if (wrap) {
      if (fontClass.equals(NORMAL_FONT_CLS)) {
        maxLen = 30;
      } else if (fontClass.equals(LARGE_FONT_CLS)) {
        maxLen = 28;
      } else if (fontClass.equals(VERY_LARGE_FONT_CLS)) {
        maxLen = 26;
      }
    }
    return renderTwoColumnList(items, maxLen);
  }
  
  public static String renderTwoColumnList(List<String> items, int maxLen) {

    StringBuffer html = new StringBuffer();

    int halfway = (int)Math.ceil(items.size() / 2);
    if ((items.size() % 2) != 0) {
      halfway++;
    }

    html.append("<ul class=\"two-columns\">\n");
    for (int i = 0; i < items.size(); i++) {
      String s = items.get(i);
      if (maxLen > 0) {
        if (s.length() > maxLen) {
          s = StringUtils.wrapLine(s, maxLen, ',', "<br>\n", false);
          if (s.indexOf("<br>") < 0) {
            s = StringUtils.wrapLine(s, maxLen, ' ', "<br>\n", false);
          }
        }
      }
      html.append("\t<li>");
      html.append(s).append("</li>\n");
      if (i + 1 == halfway) {
        html.append("</ul>\n<ul class=\"two-columns\">\n");
      }
    }
    html.append("</ul>\n");
    
    return html.toString();
  }
  
  public static String adjustCapitalization(String text) {
    return WordUtils.capitalizeFully(text, capitalizingDelimiters);
  }
  
  public static String delimitString(List<String> lines, boolean space) {
    return delimitString(lines.toArray(new String[lines.size()]), space);
  }
  
  public static String delimitString(List<String> lines, char delim, boolean space) {
    return delimitString(lines.toArray(new String[lines.size()]), delim, space);
  }
  
  public static String delimitString(String[] lines) {
    return delimitString(lines, ',', false);
  }
  
  public static String delimitString(String[] lines, boolean postDelimSpace) {
    return delimitString(lines, ',', postDelimSpace);
  }
  
  public static String delimitString(String[] lines, char delim, boolean postDelimSpace) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0;i < lines.length;i++) {
      sb.append(lines[i]);
      if (i < lines.length-1) {
        sb.append(delim);
        if (postDelimSpace) {
          sb.append(" ");
        }
      }
    }
    return sb.toString();
  }
  
  public static List<String> delimitedStringToList(String str, char delim) {
    String[] arr = StringUtils.pieceList(str, delim);
    if ((arr.length == 1) && (arr[0].length() == 0)) {
      return new ArrayList<String>();
    } else {
      return Arrays.asList(arr);
    }
  }
  
  public static Date getStartDate(int numDaysPastExpCancMeds) {
    try {
      return DateUtils.addDaysToDate(new Date(), -numDaysPastExpCancMeds);
    } catch(Exception e) {
      return null;
    }
  }   
  
  public static String cleanHtml(String html) {
    return cleanHtml(html, true, true);
  }
  
  public static String cleanHtml(String html, boolean omitXmlDeclaration, boolean bodyOnly) {
    
    html = html != null ? html.trim() : "";
    
    try {
      StringReader htmlReader = new StringReader(html);
      HtmlCleaner cleaner = new HtmlCleaner();
      CleanerProperties props = cleaner.getProperties();
      if (omitXmlDeclaration) {
        props.setOmitXmlDeclaration(true);
      }
      TagNode node = cleaner.clean(htmlReader);
      html = new PrettyXmlSerializer(props).getAsString(node);
      if (bodyOnly) {
        for (int i = 0; i < htmlElements.length; i++) {
          html = html.replaceAll("(?i)" + htmlElements[i], "");
        }
      }
    } catch(Exception e) {
      System.err.println("Error cleaning HTML: " + e.getMessage());
    }
    
    return html;
    
  }  
  
  public static ByteArrayOutputStream doCreatePdf(String cssContents, String imgPath, String pdfBody) throws Exception {
    
    StringBuffer html = new StringBuffer();
    html.append("<html><head><style type=\"text/css\">");
    html.append(cssContents);
    html.append("</style></head><body>");
    html.append(pdfBody);
    html.append("</body></html>");
    
    String filteredHtml = AvsWebUtils.cleanHtml(html.toString(), false, false);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte buf[] = filteredHtml.getBytes(); 
    out.write(buf); 
    
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new InputSource(new StringReader(new String(out.toByteArray()))));
    
    ITextRenderer iTextRenderer = new ITextRenderer(34f, 20);
    iTextRenderer.setDocument(doc, imgPath);
    iTextRenderer.layout();
  
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    iTextRenderer.createPDF(outputStream);
    
    return outputStream;
  }
  
  public static ByteArrayOutputStream createPdfWithAddlInfo(String cssContents, String imgPath, 
      String content, String additionalInformationSheetBody) throws Exception {
    ByteArrayOutputStream outputStream = doCreatePdf(cssContents, imgPath, content);
    if ((additionalInformationSheetBody != null) && !additionalInformationSheetBody.isEmpty()) {
      ByteArrayOutputStream outputStream2 = AvsWebUtils.doCreatePdf(cssContents, imgPath, 
          additionalInformationSheetBody);
      List<ByteArrayOutputStream> osList = new ArrayList<ByteArrayOutputStream>();
      osList.add(outputStream);
      osList.add(outputStream2);
      outputStream = concatPdfs(osList);
    }    
    return outputStream;
  }  
  
  public static ByteArrayOutputStream concatPdfs(List<ByteArrayOutputStream> baosList) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PdfCopyFields finalCopy = null;
    try {
      List<PdfReader> pdfReadersList = new ArrayList<PdfReader>();
      for (ByteArrayOutputStream baos : baosList) {
        pdfReadersList.add(new PdfReader(baos.toByteArray()));
      }
      finalCopy = new PdfCopyFields(outputStream);
      finalCopy.open();
      for (PdfReader pdfReader : pdfReadersList) {
        finalCopy.addDocument(pdfReader);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      finalCopy.close();
    }
    
    return outputStream;
  }  
  
  public static String savePdfToFile(String stationNo, String patientDfn, List<String> locationIens, 
      List<Double> datetimes, String filepath, ByteArrayOutputStream outputStream) throws FileNotFoundException, IOException {
    List<String> dtStr = new ArrayList<String>();
    for (Double d : datetimes) {
      dtStr.add(String.valueOf(d));
    }
    StringBuffer filename = new StringBuffer(stationNo);
    filename.append("-");
    filename.append(patientDfn);
    filename.append("-");
    filename.append(StringUtils.replaceChar(delimitString(locationIens, false), ',', '_'));
    filename.append("-");
    filename.append(StringUtils.replaceChar(delimitString(dtStr, false), '.', '-'));
    filename.append(".pdf");
    OutputStream os = new FileOutputStream(filepath + "/" + filename.toString()); 
    outputStream.writeTo(os);
    os.close();    
    return filename.toString();
  }  
  
}
