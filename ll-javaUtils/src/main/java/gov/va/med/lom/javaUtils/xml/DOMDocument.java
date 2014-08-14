package gov.va.med.lom.javaUtils.xml;

import java.io.*;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.xml.sax.InputSource;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.jaxp.*;

public class DOMDocument {

  private Document document;

  public DOMDocument(String namespaceURI, String qualifiedName, DocumentType docType) {
    try {
      DocumentBuilder db = new DocumentBuilderFactoryImpl().newDocumentBuilder();
      DOMImplementation domImpl = db.getDOMImplementation();
      document = domImpl.createDocument(namespaceURI, qualifiedName, docType);
    } catch(Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public DOMDocument(File file) {
    try {
      DOMParser parser = new DOMParser();
      parser.parse(file.toURL().toString());
      document = parser.getDocument();
    } catch(Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public DOMDocument(Document document) {
    this.document = document;
  }

  public DOMDocument(String xml) {
    try {
      StringReader reader = new StringReader(xml);
      DOMParser parser = new DOMParser();
      parser.parse(new InputSource(reader));
      document = parser.getDocument();
    } catch(Throwable t) {
      t.printStackTrace();
    }
  }

  public Document getDocument() {
    return document;
  }

  public Node getRootNode() {
    return document.getDocumentElement();
  }

  public Properties getAtributes(Node node) {
    Properties props = new Properties();
    NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Node current = attributes.item(i);
      props.put(current.getNodeName(), current.getNodeValue());
    }
    return props;
  }

  public NodeList getNodeList(String tagName) {
    Element root = document.getDocumentElement();
    NodeList nodeList = root.getElementsByTagName(tagName);
    return nodeList;
  }

  public NodeIterator getNodeIterator(NodeList nodeList) {
    // get node to start iterating with
    Element element = (Element)nodeList.item(0);
    // get a NodeIterator
    return ((DocumentTraversal)document).createNodeIterator(element,
                                         NodeFilter.SHOW_ALL, null, true);
  }

  public NodeIterator getNodeIterator(String tagName) {
    return getNodeIterator(getNodeList(tagName));
  }

  public int getNumElements(NodeList nodeList) {
    return nodeList.getLength();
  }

  public String getValue(NodeList nodeList, int index) {
    if ((index >= 0) && (index < nodeList.getLength())) {
      Node node = nodeList.item(index);
      Node textNode = node.getFirstChild();
      if ((textNode != null) && (textNode.getNodeType() == Node.TEXT_NODE))
        return textNode.getNodeValue();
      else
        return null;
    } else
      return null;
  }

  public int getIndex(NodeList nodeList, String value) {
    String[] values = getValueList(nodeList);
    int count = values.length;
    int i = 0;
    while ((i < count) && (!values[i].equalsIgnoreCase(value)))
      i++;
    if (i < count)
      return i;
    else
      return -1;
  }

  public String[] getValueList(NodeList nodeList) {
    int count = getNumElements(nodeList);
    String[] values = new String[count];
    for (int i = 0; i < count; i++) {
      values[i] = getValue(nodeList, i);
    }
    return values;
  }

  public void serialize(Writer writer, String lineSeperator, 
                        String indent, boolean includeXmlProlog) {
    DOMSerializer serializer = new DOMSerializer();
    try {
      serializer.setLineSeperator(lineSeperator);
      serializer.setIndentation(indent);
      serializer.serialize(getDocument(), writer, includeXmlProlog);
    } catch(Exception e) {
      System.err.println(e.getMessage());
    }
  }
  
  public void serialize(Writer writer, String lineSeperator, String indent) {
    serialize(writer, lineSeperator, indent, true);
  }

  public void serialize(Writer writer, boolean includeXmlProlog) {
    serialize(writer, "", "", includeXmlProlog);
  }
  
  public void serialize(Writer writer) {
    serialize(writer, true);
  }
  
  public String serialize(String lineSeperator, String indent, boolean includeXmlProlog) {
    StringWriter writer = new StringWriter();
    serialize(writer, lineSeperator, indent, includeXmlProlog);
    return writer.toString();
  }
  
  public String serialize(String lineSeperator, String indent) {
    return serialize(lineSeperator, indent, true);
  }
  
  public String serialize(boolean includeXmlProlog) {
    return serialize("", "", includeXmlProlog);
  }  
  
  public String serialize() {
    return serialize(true);
  }
}