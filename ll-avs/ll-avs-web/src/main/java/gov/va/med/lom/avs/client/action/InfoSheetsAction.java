package gov.va.med.lom.avs.client.action;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;

import nu.xom.*; 

import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.login.struts.session.SessionUtil;

import gov.va.med.lom.vistabroker.security.SecurityContextFactory;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.avs.client.model.*;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.model.EncounterLocation;
import gov.va.med.lom.avs.model.PceData;
import gov.va.med.lom.avs.model.EncounterInfo;
import gov.va.med.lom.avs.model.EncounterCacheMongo;
import gov.va.med.lom.avs.model.Encounter;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SheetService;

public class InfoSheetsAction extends BaseCardAction {

  private static final String CSS_PATH = "/avs/css/krames.css";
  private static final String IMG_PATH = "/avs/artwork/";
  private static final String BASE_URI = "http://localhost";
  private static String WEB_PATH;
  
  private static final String KRAMES_BASE_URL = 
      "http://external.ws.StayWell.com/LomaLindaVAWS";
  private static final String KRAMES_SEARCH_CONTENT_URL = 
      KRAMES_BASE_URL + "/Content.svc/SearchContent";
  private static final String KRAMES_GET_CONTENT_URL = 
      KRAMES_BASE_URL + "/Content.svc/GetContent";
  private static final int MEDIA_HTML = 1;
  private static final int MEDIA_PDF = 2;
  
  private static String CSS_CONTENTS;
  
  private SheetService sheetService;
  private String documentType;
  private String contentTypeId;
  private String contentId;
  private String language;
  private String fullText;
  private String logicalOperator;
  private String icd9Codes;
  private String meshCodes;
  private String cptCodes;
  
  static {
    ResourceBundle res = ResourceBundle.getBundle("krames");
    WEB_PATH = res.getString("webpath");
  }
  
  /*
   * Action Methods
   */  
  public void prepare() throws Exception {
    
    super.prepare();    
    if (!SessionUtil.isActiveSession(super.request))
      return;
    try {
      sheetService = ServiceFactory.getSheetService();
    } catch(Exception e) {
      log.error("error creating service", e);
    }
    
  }  
  
	public String kramesSearchContent() {
	  
	  if (this.securityContext == null) {
	    this.securityContext = SecurityContextFactory.createDuzSecurityContext(super.facilityNo, super.userDuz);
	  }
	  
	  List<Encounter> encounters = new ArrayList<Encounter>();
	  String[] locations = StringUtils.pieceList(super.locationIens, ',');
	  String[] dts = StringUtils.pieceList(super.datetimes, ',');
	  int i = 0;
    for (String s : dts) {
      Encounter encounter = new Encounter();
      // required params
      encounter.setLocation(new EncounterLocation( locations[i], ""));
      encounter.setEncounterDatetime(StringUtils.toDouble(s.trim(), 0.0));
      encounters.add(encounter);
      i++;
    }

    EncounterInfo encounterInfo = new EncounterInfo();
    EncounterCacheMongo ecm = new EncounterCacheMongo();
    ecm.setPatientDfn(super.patientDfn);
    ecm.setEncounters(encounters);
    encounterInfo.setEncounterCache(ecm);
    
    List<String> meshList = null;
    List<String> icd9List = new ArrayList<String>();
    List<String> cptList = null;
    
    if (((fullText == null) || (fullText.trim().isEmpty())) && 
        ((icd9Codes == null) || (icd9Codes.trim().isEmpty())) && 
        ((meshCodes == null) || (meshCodes.trim().isEmpty())) && 
        ((cptCodes == null) || (cptCodes.trim().isEmpty()))) {
      CollectionServiceResponse<PceData> pceDataSr = this.sheetService.getPceData(securityContext, encounterInfo);
      List<PceData> pceDataList = (List<PceData>)pceDataSr.getCollection();
      for (PceData pceData : pceDataList) {
        icd9List.addAll(pceData.getCodes());
        if (icd9List.size() == 0) {
          icd9List.add(""); // so krames won't return everything
        }
      }
    } else {
      if ((meshCodes != null) && (!meshCodes.trim().isEmpty())) {
        meshList = delimStrToList(meshCodes);
      }
	    if ((icd9Codes != null) && (!icd9Codes.trim().isEmpty())) {
	      icd9List = delimStrToList(icd9Codes);
	    }
      if ((cptCodes != null) && (!cptCodes.trim().isEmpty())) {
        cptList = delimStrToList(cptCodes);
      }
    }
    if ((language == null) || (language.trim().isEmpty())) {
      language = "en";
    }
    
    usageLog("Krames Search", "Language=" + language + ", Text=" + fullText + ", MESH=" + meshCodes + 
        ", ICD9=" + icd9Codes + ", CPT=" + cptCodes);
    
    String xmlRequest = getSearchContentXml(language, fullText, logicalOperator, meshList, icd9List, cptList);
    String xmlResponse = sendRequest(KRAMES_SEARCH_CONTENT_URL, xmlRequest);
    
    List<SearchResultJson> searchResults = new ArrayList<SearchResultJson>();
    try {
      Builder parser = new Builder();
      StringReader reader = new StringReader(xmlResponse);
      nu.xom.Document input = parser.build(reader, null);
      Element root = input.getRootElement();
      parseSearchResults(root, root.getLocalName(), searchResults);
    } catch (ValidityException ex) {
      System.err.println("XML is invalid.");
    } catch (ParsingException ex) {
      System.err.println("XML is malformed.");
    } catch (IOException ex) {
      System.err.println("Could not connect to localhost. The site may be down.");
    }
	  
    return setJson(searchResults);
	  
	}
	
	public String kramesGetContent() {
	  
	  int media = ((documentType == null) || documentType.equals("html")) ? MEDIA_HTML : MEDIA_PDF;
	  
	  String[] contentTypeArr = StringUtils.pieceList(this.contentTypeId, ',');
	  String[] contentIdArr = StringUtils.pieceList(this.contentId, ',');
	  
	  ContentObj contentObj = null;
	  StringBuffer content = new StringBuffer();
	  for (int i = 0; i < contentIdArr.length; i++) {
	    contentObj = doGetDocumentContent(contentTypeArr[i], contentIdArr[i]);
	    content.append(contentObj.getHtml());
	    content.append("<br/><br/>");
	  }
	  
    String title = null;
	  if (contentIdArr.length > 1) {
	    title = "Krames StayWell HealthSheets"; 
	  } else {
	    title = contentObj.getTitle();    
	  }
	  
	  usageLog("Krames Content", title);
	  
	  String baseURI = this.request.getScheme() + "://" + this.request.getServerName();
	  StringBuffer html = new StringBuffer();
	  
	  if (media == MEDIA_HTML) {
	    html.append("<html><head><title>");
	    html.append(title);
	    html.append("</title>");
	    html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + baseURI + CSS_PATH + "\" />");
	    html.append("</head><body>");
	    html.append(content.toString());
	    html.append("</body></html>");
	    
	    try {
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setContentType("text/html");
        response.setContentLength(html.length());
        response.getWriter().write(html.toString());
      } catch (Exception e) {
        log.error("error creating json data", e);
      }
	    
	  } else {
	    
	    try {
    	  if (CSS_CONTENTS == null) {
          HttpClient client = new DefaultHttpClient();
          HttpGet request = new HttpGet(baseURI + CSS_PATH);
          HttpResponse response = client.execute(request);
    
          // Get the response
          BufferedReader rd = new BufferedReader
            (new InputStreamReader(response.getEntity().getContent()));
              
          StringBuffer sb = new StringBuffer();
          String line = "";
          while ((line = rd.readLine()) != null) {
            sb.append(line);
          }         
          
          CSS_CONTENTS = sb.toString();
        }
    	  
    	  html.append("<html><head><style type=\"text/css\">");
        html.append(CSS_CONTENTS);
        html.append("</style>");
        html.append(title);
        html.append("</title></head><body>");
        html.append(content.toString());
        html.append("</body></html>");
  	  
        StringReader htmlReader = new StringReader(html.toString());
        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        
        TagNode node = cleaner.clean(htmlReader);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new PrettyXmlSerializer(props).writeToStream(node, out);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(new String(out.toByteArray()))));
        
        ITextRenderer iTextRenderer = new ITextRenderer(34f, 20);
        iTextRenderer.setDocument(doc, BASE_URI + IMG_PATH);
        iTextRenderer.layout();
      
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        iTextRenderer.createPDF(outputStream);
        
        try {
          flushPdf(outputStream);
        } finally {
          try {
            outputStream.close();
          } catch(IOException e) {}
        }
      } catch(Exception e) {
        log.error("Error creating PDF from HTML", e);      
      }
      
	  }
	  
    return SUCCESS;
    
	}
	
	private ContentObj doGetDocumentContent(String contentTypeId, String contentId) {
	  
	  String xml = getGetContentXml(contentTypeId, contentId);
	  String xmlRequest = sendRequest(KRAMES_GET_CONTENT_URL, xml);
	  ContentObj contentObj = new ContentObj();
	  StringBuffer html = new StringBuffer();
    try {
      Builder parser = new Builder();
      StringReader reader = new StringReader(xmlRequest);
      nu.xom.Document input = parser.build(reader, null);
      Element root = input.getRootElement();
      parseContent(root, root.getLocalName(), contentObj);
      
      html.append(contentObj.getContent());
      html.append("<br/>");
      if (((contentObj.getPrintSources() != null) && (contentObj.getPrintSources().size() > 0)) ||
          ((contentObj.getOnlineSources() != null) && (contentObj.getOnlineSources().size() > 0))) {
        html.append("<hr/>");
      }
      if ((contentObj.getPrintSources() != null) && (contentObj.getPrintSources().size() > 0)) {
        html.append("<div class=\"sources\">");
        html.append("Print Sources:");
        html.append("<ul>");
        for (String printSource : contentObj.getPrintSources()) {
          html.append("<li>" + printSource);
        }
        html.append("</ul></div>");
      }
      if ((contentObj.getOnlineSources() != null) && (contentObj.getOnlineSources().size() > 0)) {
        html.append("<div class=\"sources\">");
        html.append("Online Sources:");
        html.append("<ul>");
        for (String onlineSource : contentObj.getOnlineSources()) {
          html.append("<li>" + onlineSource);
        }
        html.append("</ul></div>");
      }      
      html.append("<hr/>");
      html.append("<div class=\"CopyRight\">");
      html.append(contentObj.getCopyrightStatement());
      html.append("</div>");
      html.append("</body></html>");
      
      String filteredHtml = AvsWebUtils.cleanHtml(html.toString());
      
      contentObj.setHtml(filteredHtml);
      
    } catch (ParsingException ex) {
      System.err.println("Well-formedness error in " + ex.getURI());
      error(ex);
    } catch (IOException ex) {
      System.err.println("I/O error while reading input document or stylesheet");
      error(ex);
    }
    
    return contentObj;
    
	}
	
  private static String getSearchContentXml(String language, String fullText, String logicalOperator, 
                                            List<String> meshCodes, List<String> icd9Codes, List<String> cptCodes) {
    
    // Create root element
    Element rootEl = new Element("SearchCriteria");
    Attribute attr = new Attribute("SortBy", "Title");
    rootEl.addAttribute(attr);
    attr = new Attribute("ReturnAdditionalTitles", "true");
    rootEl.addAttribute(attr);
    attr = new Attribute("IncludeBlocked", "true");
    rootEl.addAttribute(attr);
    attr = new Attribute("SortBy", "Title");
    rootEl.addAttribute(attr);
    attr = new Attribute("SortDirection", "Ascending");
    rootEl.addAttribute(attr);    

    // Create language element
    Element languageEl = new Element("Language");
    if (language == null) {
      language = "en";
    }
    attr = new Attribute("Code", language);
    languageEl.addAttribute(attr);
    rootEl.appendChild(languageEl);
    
    // Create age groups element
    Element ageGroupsEl = new Element("AgeGroups");
    Element ageGroupEl = new Element("AgeGroup");
    ageGroupEl.appendChild("Adult (18+)");
    ageGroupsEl.appendChild(ageGroupEl);
    rootEl.appendChild(ageGroupsEl);
    
    // Create content type elements
    Element contentTypesEl = new Element("ContentTypes");
    Element contentTypeEl = new Element("ContentType");
    attr = new Attribute("ContentTypeId", "3");
    contentTypeEl.addAttribute(attr);
    contentTypesEl.appendChild(contentTypeEl);
    rootEl.appendChild(contentTypesEl);
    
    // Create full text element
    if ((fullText != null) && (fullText.trim().length() > 0)) {
      Element fulltextEl = new Element("FullText");
      if ((logicalOperator == null) || (logicalOperator.trim().length() == 0)) {
        logicalOperator = "And";
      }
      attr = new Attribute("LogicalOperator", logicalOperator);
      fulltextEl.addAttribute(attr);
      fulltextEl.appendChild(fullText);
      rootEl.appendChild(fulltextEl);
    }
    
    // Create MESH list elements
    if ((meshCodes != null) && (meshCodes.size() > 0)) {
      Element meshListEl = new Element("MeSHList");
      for (String code : meshCodes) {
        Element meshCodeEl = new Element("MeSH");
        attr = new Attribute("Code", code);
        meshCodeEl.addAttribute(attr);
        meshListEl.appendChild(meshCodeEl);
      }
      rootEl.appendChild(meshListEl);
    }    
    
    // Create ICD9 list elements
    if ((icd9Codes != null) && (icd9Codes.size() > 0)) {
      Element icd9ListEl = new Element("ICD9List");
      for (String code : icd9Codes) {
        Element icd9CodeEl = new Element("ICD9");
        attr = new Attribute("Code", code);
        icd9CodeEl.addAttribute(attr);
        icd9ListEl.appendChild(icd9CodeEl);
      }
      rootEl.appendChild(icd9ListEl);
    }
    
    // Create CPT list elements
    if ((cptCodes != null) && (cptCodes.size() > 0)) {
      Element cptListEl = new Element("CPTList");
      for (String code : cptCodes) {
        Element cptCodeEl = new Element("CPT");
        attr = new Attribute("Code", code);
        cptCodeEl.addAttribute(attr);
        cptListEl.appendChild(cptCodeEl);
      }
      rootEl.appendChild(cptListEl);
    }
    
    nu.xom.Document doc = new nu.xom.Document(rootEl);
    String result = doc.toXML();
    return result.substring(22).trim();
    
  }
  
  private static String getGetContentXml(String contentTypeId, String contentId) {
    
    // Create root element
    Element rootEl = new Element("GetContent");
    Attribute attr = new Attribute("ContentTypeId", contentTypeId);
    rootEl.addAttribute(attr);
    attr = new Attribute("ContentId", contentId);
    rootEl.addAttribute(attr);
    attr = new Attribute("IncludeBlocked", "true");
    rootEl.addAttribute(attr);
    attr = new Attribute("GetOriginal", "true");
    rootEl.addAttribute(attr);
    
    nu.xom.Document doc = new nu.xom.Document(rootEl);
    String result = doc.toXML();
    return result.substring(22).trim();
    
  }
  
  private static String sendRequest(String url, String xmlRequest) {
    
    try {
      HttpClient client = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(url);
      httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      nvps.add(new BasicNameValuePair("XmlRequest", xmlRequest));
      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
      httpPost.setEntity(entity);
      HttpResponse response = client.execute(httpPost);

      // Get the response
      BufferedReader rd = new BufferedReader
        (new InputStreamReader(response.getEntity().getContent()));
        
      StringBuffer sb = new StringBuffer();
      String line = "";
      while ((line = rd.readLine()) != null) {
        sb.append(line);
      }
      
      return sb.toString();
    } catch(Exception e) {
      System.err.println(e.getMessage());
    }
    
    return "";
      
  }
  
  private static void parseSearchResults(Node currentNode, String currentName, List<SearchResultJson> searchResults) {
    
    String name = null;
    String value = null;
    if (currentNode instanceof Element) {
      Element el = (Element) currentNode;
      name = el.getLocalName();   
      currentName = name;
      if (name.equals("ContentObject")) {
        SearchResultJson searchResultJson = new SearchResultJson();
        searchResults.add(searchResultJson);
        for (int i = 0; i < el.getAttributeCount(); i++) {
          Attribute attr = el.getAttribute(i);
          if (attr.getLocalName().equals("ContentObjectType")) {
            searchResultJson.setContentObjectType(attr.getValue());
          } else if (attr.getLocalName().equals("ContentTypeId")) {
            searchResultJson.setContentTypeId(attr.getValue());
          } else if (attr.getLocalName().equals("ContentId")) {
            searchResultJson.setContentId(attr.getValue());
          }
        }
      }
      
      
    } else if (currentNode instanceof Text) {
      value = currentNode.getValue();
      if (value != null) {
        value = value.trim();
      }
    }

    if ((searchResults.size() > 0)) {
      if ((currentName != null) && (value != null)) {
        SearchResultJson searchResult = searchResults.get(searchResults.size()-1); 
        if (currentName.equals("RegularTitle")) {
          searchResult.setTitle(value);
        } else if (currentName.equals("Gender")) {
          searchResult.setGender(value);
        }
      } else {
        SearchResultJson searchResult = searchResults.get(searchResults.size()-1); 
        if (currentName.equals("Language")) {
          Element el = (Element) currentNode;
          if (el.getAttributeCount() > 0) {
            Attribute attr = el.getAttribute(0);
            searchResult.setLanguage(getLanguageFromCode(attr.getValue()));
          }
        } else if (currentName.equals("Blurb")) {
          StringBuffer sb = new StringBuffer(currentNode.toXML());
          String blurb = null;
          if ((sb.length() >= 7) && (sb.length()-9 > 7)) {
            blurb = sb.substring(7, sb.length()-9);
          } else {
            blurb = sb.toString();
          }
          searchResult.setBlurb("<i>" + blurb + "</i>");
        }
      }
    }
    for (int i = 0; i < currentNode.getChildCount(); i++) {
      parseSearchResults(currentNode.getChild(i), name, searchResults);
    }
    
  }
  
  private void parseContent(Node currentNode, String currentName, ContentObj contentObj) {

    String name = null;
    String value = null;
    if (currentNode instanceof Element) {
      Element el = (Element) currentNode;
      name = el.getLocalName();   
      currentName = name;
      if (name.equals("ContentObject")) {
        if (contentObj.getIndex() == 0) {
          contentObj.setIndex(1);
        } else {
          return;
        }
        for (int i = 0; i < el.getAttributeCount(); i++) {
          Attribute attr = el.getAttribute(i);
          if (attr.getLocalName().equals("ContentTypeId")) {
            contentObj.setContentTypeId(attr.getValue());
          } if (attr.getLocalName().equals("ContentId")) {
            contentObj.setContentId(attr.getValue());
          } if (attr.getLocalName().equals("CreatedDate")) {
            contentObj.setCreatedDate(attr.getValue());
          } else if (attr.getLocalName().equals("PublishedDate")) {
            contentObj.setPublishedDate(attr.getValue());
          }
        }
      }
    } else if (currentNode instanceof Text) {
      value = currentNode.getValue();
      if (value != null) {
        value = value.trim();
      }
    }
      
    if ((currentName != null) && (value != null)) {
      if (currentName.equals("RegularTitle")) {
        contentObj.setTitle(value);
      } else if (currentName.equals("CopyrightStatement")) {
        contentObj.setCopyrightStatement(value);
      } else if (currentName.equals("PrintSource")) {
        List<String> printSources = contentObj.getPrintSources();
        if (printSources == null) {
          printSources = new ArrayList<String>();
          contentObj.setPrintSources(printSources);
        }
        printSources.add(value);        
      } else if (currentName.equals("OnlineSources")) {
        List<String> onlineSources = contentObj.getOnlineSources();
        if (onlineSources == null) {
          onlineSources = new ArrayList<String>();
          contentObj.setOnlineSources(onlineSources);
        }
        onlineSources.add(value);        
      }
      
    } else if (currentName.equals("Blurb")) {
      StringBuffer sb = new StringBuffer(currentNode.toXML());
      String blurb = null;
      if ((sb.length() >= 7) && (sb.length()-9 > 7)) {
        blurb = sb.substring(7, sb.length()-9);
      } else {
        blurb = sb.toString();
      }
      contentObj.setBlurb(blurb);
    }
    if (currentName.equals("Content")) {
      String xml = currentNode.toXML();
      if ((xml.length() >= 15) && (xml.length()-17 > 15)) {
        xml = xml.substring(15, xml.length()-17);
      }
      StringBuffer sb = new StringBuffer(xml);
      int fromIndex = 0;
      while (fromIndex >= 0) {
        fromIndex = sb.indexOf("imageid", fromIndex);
        if (fromIndex >= 0) {
          int st = fromIndex;
          while ((st < sb.length()) && (sb.charAt(st) != ' ') && (sb.charAt(st) != '/')) {
            st++;
          }
          if (st < sb.length()) {
            String s = sb.substring(fromIndex, st);
            String imageId = StringUtils.piece(s,  '\"', 2);
            // if not already present on local server, get image from Krames server and save on local server
            try {
              String imagePath = IMG_PATH + imageId + ".jpg";
              File localImageFile = new File(WEB_PATH + imagePath);
              if (!localImageFile.exists()) {
                URL url = new URL(KRAMES_BASE_URL + "/" + imageId + ".img");
                BufferedImage  image = ImageIO.read(url);
                ImageIO.write(image, "jpg", localImageFile);
              } 
              sb.replace(fromIndex, st, "src=\"http://" + java.net.InetAddress.getLocalHost().getHostName() + imagePath + "\"");
            } catch(Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
      contentObj.setContent(sb.toString());
    }
    
    for (int i = 0; i < currentNode.getChildCount(); i++) {
      parseContent(currentNode.getChild(i), name, contentObj);
    }
    
  } 
	
  private static String getLanguageFromCode(String code) {
    if (code.equals("en")) {
      return "English";
    } else if (code.equals("es")) {
      return "Spanish";
    } else if (code.equals("zh")) {
      return "Chinese";
    } else if (code.equals("vi")) {
      return "Vietnamese";
    } else if (code.equals("ru")) {
      return "Russian";
    } else if (code.equals("hy")) {
      return "Armenian";
    } else if (code.equals("fa")) {
      return "Farse (Persian)";
    } else if (code.equals("km")) {
      return "Khmer";
    } else if (code.equals("ko")) {
      return "Korean";
    } else if (code.equals("hmn")) {
      return "Hmong";
    } else if (code.equals("tgl")) {
      return "Tagalog";
    } else if (code.equals("de")) {
      return "German";
    } else if (code.equals("fr")) {
      return "French";
    } else if (code.equals("it")) {
      return "Italian";
    } else if (code.equals("so")) {
      return "Somali";
    } else if (code.equals("aa")) {
      return "Arabic";
    } else if (code.equals("pp")) {
      return "Portuguese";
    }
    return "n/a";
  }
  
  private static List<String> delimStrToList(String str) {
    String[] arr = null;
    if (str.indexOf(',') > 0) {
      arr = StringUtils.pieceList(str, ',');
    } else {
      arr = StringUtils.pieceList(str, ' ');
    }
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < arr.length; i++) {
      list.add(arr[i]);
    }
    return list;
  }
  
  
	/* Getter/Setter Methods */
  public String getUserDuz() {
    return userDuz;
  }

  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }

  public String getPatientDfn() {
    return patientDfn;
  }

  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }

  public String getContentTypeId() {
    return contentTypeId;
  }

  public void setContentTypeId(String contentTypeId) {
    this.contentTypeId = contentTypeId;
  }

  public String getContentId() {
    return contentId;
  }

  public void setContentId(String contentId) {
    this.contentId = contentId;
  }

  public String getFullText() {
    return fullText;
  }

  public void setFullText(String fullText) {
    this.fullText = fullText;
  }

  public String getLogicalOperator() {
    return logicalOperator;
  }

  public void setLogicalOperator(String logicalOperator) {
    this.logicalOperator = logicalOperator;
  }

  public String getIcd9Codes() {
    return icd9Codes;
  }

  public void setIcd9Codes(String icd9Codes) {
    this.icd9Codes = icd9Codes;
  }

  public String getMeshCodes() {
    return meshCodes;
  }

  public void setMeshCodes(String meshCodes) {
    this.meshCodes = meshCodes;
  }

  public String getCptCodes() {
    return cptCodes;
  }

  public void setCptCodes(String cptCodes) {
    this.cptCodes = cptCodes;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }
  
}
