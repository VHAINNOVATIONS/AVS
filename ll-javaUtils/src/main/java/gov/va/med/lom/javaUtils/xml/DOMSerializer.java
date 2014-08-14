package gov.va.med.lom.javaUtils.xml;

import java.io.*;
import org.w3c.dom.*;

public class DOMSerializer  {
  
  public static final String XML_PROLOG = "<?xml version=\"1.0\" standalone = \"yes\"?>";

  // Indentation to use
  private String indent;

  // Line seperator to use
  private String lineSeperator;

  public DOMSerializer() {
    // set defaults
    indent = "";
    lineSeperator = "";
  }

  public void setLineSeperator(String lineSeperator) {
    this.lineSeperator = lineSeperator;
  }

  public void setIndentation(String indent) {
    this.indent = indent;
  }
  
  public void serialize(Document doc, OutputStream out) throws IOException {
    serialize(doc, out, true);
  }

  public void serialize(Document doc, OutputStream out, boolean includeXmlProlog) throws IOException {
    Writer writer = new OutputStreamWriter(out);
    serialize(doc, writer, includeXmlProlog);
  }

  public void serialize(Document doc, File file) throws IOException {
    serialize(doc, file, true);
  }
  
  public void serialize(Document doc, File file, boolean includeXmlProlog) throws IOException {
    Writer writer = new FileWriter(file);
    serialize(doc, writer, includeXmlProlog);
  }

  public void serialize(Document doc, Writer writer) throws IOException {
    serialize(doc, writer, true);
  }
  
  public void serialize(Document doc, Writer writer, boolean includeXmlProlog) throws IOException {
    // start the serialization recursion
    serializeNode(doc, writer, indent, includeXmlProlog);
    writer.flush();
    writer.close();
  }

  public void serializeNode(Node node, Writer writer, String indentLevel) throws IOException {
    serializeNode(node, writer, indentLevel, true);
  }
  
  public void serializeNode(Node node, Writer writer, String indentLevel, 
                            boolean includeXmlProlog) throws IOException {
    // determine action based on node type
    switch(node.getNodeType()) {
      case Node.DOCUMENT_NODE:
        if (includeXmlProlog)
          writer.write(XML_PROLOG);
        writer.write(lineSeperator);
        // recurse on each child
        NodeList nodes = node.getChildNodes();
        if (nodes != null) {
          for (int i = 0; i < nodes.getLength(); i++) {
            serializeNode(nodes.item(i), writer, "", includeXmlProlog);
          }
        }
        break;

      case Node.ELEMENT_NODE:
        String name = node.getNodeName();
        writer.write(indentLevel + "<" + name);
        NamedNodeMap attributes = node.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
          Node current = attributes.item(i);
          writer.write(" " + current.getNodeName() +
                       "=\"" + current.getNodeValue() + "\"");
        }
        writer.write(">");

        // recurse on each child
        NodeList children = node.getChildNodes();
        if (children != null) {
          if ((children.item(0) != null) &&
              (children.item(0).getNodeType() == Node.ELEMENT_NODE)) {
            writer.write(lineSeperator);
          }
          for (int i = 0; i < children.getLength(); i++) {
            serializeNode(children.item(i), writer, indentLevel + indent);
          }
          if ((children.item(0) != null) &&
              (children.item(children.getLength() - 1).getNodeType() == Node.ELEMENT_NODE)) {
            writer.write(indentLevel);
          }
        }
        writer.write("</" + name + ">");
        writer.write(lineSeperator);
        break;

      case Node.TEXT_NODE:
        writer.write(node.getNodeValue());
        break;

      case Node.CDATA_SECTION_NODE:
        writer.write("<![CDATA[" + node.getNodeValue() + "]]>");
        break;

      case Node.COMMENT_NODE:
        writer.write(indentLevel + "<!-- " + node.getNodeValue() + " -->");
        writer.write(lineSeperator);
        break;

      case Node.PROCESSING_INSTRUCTION_NODE:
        writer.write("<?" + node.getNodeName() + " " + node.getNodeValue() + "?>");
        writer.write(lineSeperator);
        break;

      case Node.ENTITY_REFERENCE_NODE:
        writer.write("&" + node.getNodeName() + ";");
        break;

      case Node.DOCUMENT_TYPE_NODE:
        DocumentType docType = (DocumentType)node;
        writer.write("<!DOCTYPE " + docType.getName());
        if (docType.getPublicId() != null) {
          System.out.print(" PUBLIC \"" + docType.getPublicId() + "\" ");
        } else {
          writer.write(" SYSTEM ");
        }
        writer.write("\"" + docType.getSystemId() + "\">");
        writer.write(lineSeperator);
        break;
    }
  }

}
