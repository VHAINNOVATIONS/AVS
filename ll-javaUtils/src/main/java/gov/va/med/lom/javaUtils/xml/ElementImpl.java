package gov.va.med.lom.javaUtils.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.Element
 *
 * Namespaces are ignored in this implementation.  The terms "tag
 * name" and "node name" are always considered to be synonymous.
 */
public class ElementImpl extends NodeImpl implements Element {

	/*
	 * A HashMap of AttrImpl nodes representing
	 * attributes.
	 */
	protected HashMap attributes = null;

	/*
	 * Constructs an empty ElementImpl.
	 */
	public ElementImpl() {
		attributes = new HashMap();
		type = ELEMENT_NODE;
	}

	/*
	 * Constructs a ElementImpl from the given node,
	 * without creating entire children subtree.
	 */
	public ElementImpl(ElementImpl element) {
		super(element);
		attributes = element.attributes;
		type = ELEMENT_NODE;
	}

	/*
	 * Constructs an ElementImpl with the given
	 * document owner and node name.
	 */
	public ElementImpl(Document ownerDoc, String name) {
		super(ownerDoc,name,ELEMENT_NODE);
		this.attributes = new HashMap();
	}

	/*
	 * Constructs an ElementImpl with the given
	 * document owner, node name, node type and node value.
	 */
	protected ElementImpl(Document ownerDoc, String nodeName, short type, String value) {
		super(ownerDoc, nodeName, type, value);
	}

	/*
	 * Constructs an ElementImpl from a given node (creates the children subtree too),
	 * as a Node
	 */
	public ElementImpl(Node node) {
	    this(node,true);
	}

	/*
	 * Constructs an ElementImpl from a given node, as a Node,
	 * and deep as boolean.
	 */
	public ElementImpl(Node node, boolean deep) {
		super(node,false);
		attributes = new HashMap();
		NamedNodeMap attrs = node.getAttributes();
        if (attrs != null) {
    		for (int i = 0; i < attrs.getLength(); i++) {
    			Attr attr = new AttrImpl((Attr) attrs.item(i));
    			attributes.put(attr.getName(), attr);
    		}
        }
		if (deep)
		    initNodeImplChildren(node);
	}

	/*
	 * Creates new instance of ElementImpl from a given document
	 * as a Document.
	 */
	public static Element newInstance(Document document) {
		Node root = document.getDocumentElement();
		return new ElementImpl(root);
	}

	/*
	 * Inserts the node newChild before the existing
	 * child node refChild. If refChild is
	 * null, insert newChild at the end of
	 * the list of children.
	 */
	public Node insertBefore(Node newChild, Node refChild) {
        super.insertBefore(newChild,refChild);
		return newChild;
	}

	/*
	 * Replaces the child node oldChild with
	 * newChild in the list of children, and returns the
	 * oldChild node.
	 */
	public Node replaceChild(Node newChild, Node oldChild) {
		super.replaceChild(newChild,oldChild);
		return oldChild;
	}

	/*
	 * Removes the child node indicated by oldChild from
	 * the list of children, and returns it.
	 */
	public Node removeChild(Node oldChild) {
        super.removeChild(oldChild);
		return oldChild;
	}

	/*
	 * Returns a duplicate of this node.  The duplicate node has no
	 * parent (getParentNode returns null).
	 * If a shallow clone is being performed (deep is
	 * false), the new node will not have any children or
	 * siblings.  If a deep clone is being performed, the new node
	 * will form the root of a complete cloned subtree.
	 */
	public Node cloneNode(boolean deep) {
		return new ElementImpl(this,deep);
	}

	/*
	 * Returns tag name of this node.
	 */
	public String getTagName() {
		return nodeName;
	}

	/*
	 * Returns all attribute nodes of this node.
	 */
	public NamedNodeMap getAttributes() {
		return new HashMapNamedNodeMap(attributes);
	}

	/*
	 * Returns the value of the attribute with given name.
	 */
	public String getAttribute(String name) {
		Attr attr = getAttributeNode(name);
		if (attr == null) {
			return "";
		}
		return attr.getValue();
	}


	/*
	 * Equivalent to getAttribute(localName).
	 */
	public String getAttributeNS(String namespaceURI, String localName) {
		return getAttribute(localName);
	}

	/*
	 * To the name attribute set value to value.
	 */
	public void setAttribute(String name, String value) {
		attributes.put(name, new AttrImpl(this, name, value));
	}

	/*
	 * Equivalent to setAttribute(qualifiedName, value).
	 */
	public void setAttributeNS(String namespaceURI, String qualifiedName, String value) {
		setAttribute(qualifiedName, value);
	}


	/*
	 * Removes attribute with the given name.
	 */
	public void removeAttribute(String name) {
		if (type != ELEMENT_NODE)
			throw new NodeDOMException(
				DOMException.NOT_SUPPORTED_ERR,
				"Node doesn't have attributes");
		removeAttribute(name, true);
	}

	private void removeAttribute(String name, boolean checkPresent) {
		if (attributes.remove(name) != null)
			return;
		// If we get here, the attribute doesn't exist
		if (checkPresent) {
			throw new NodeDOMException(
				DOMException.NOT_FOUND_ERR,
				"No such attribute!");
		}
	}


	/*
	 * Returns true, if this node has attributes, otherwise
	 * false.
	 */
	public boolean hasAttributes() {
		return attributes.size() > 0;
	}


	/*
	 * Returns true, if this node has attribute with given name,
	 * otherwise false.
	 */
	public boolean hasAttribute(String name) {
		return getAttributeNode(name) != null;
	}


	/*
	 * Equivalent to removeAttribute(localName).
	 */
	public void removeAttributeNS(String namespaceURI, String localName) {
		removeAttribute(localName);
	}


	/*
	 * Returns attribute value with given name of this node.
	 */
	public Attr getAttributeNode(String name) {
		return (Attr) attributes.get(name);
	}


	/*
	 * Equivalent to getAttributeNode(localName).
	 */
	public Attr getAttributeNodeNS(String namespaceURI, String localName) {
		return getAttributeNode(localName);
	}


	/*
	 * Add new attribute to this node.
	 */
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		AttrImpl attr;
		if (newAttr instanceof AttrImpl) {
			attr = (AttrImpl) newAttr;
		} else {
			attr = new AttrImpl(newAttr);
		}
		attributes.put(attr.getName(), attr);
		return attr;
	}


	/*
	 * Equivalent to setAttributeNode(newAttr).
	 */
	public Attr setAttributeNodeNS(Attr newAttr) {
		return setAttributeNode(newAttr);
	}


	/*
	 * Remove attribute from this node.
	 */
	public Attr removeAttributeNode(Attr oldAttr) {
		removeAttribute(oldAttr.getName());
		return oldAttr;
	}


	/*
	 * Equivalent to hasAttribute(localName).
	 */
	public boolean hasAttributeNS(String namespaceURI, String localName) {
		return hasAttribute(localName);
	}


	/*
	 * Returns all Element nodes with given name,
	 * searching by all sub nodes from this node.
	 */
    public NodeList getElementsByTagName(String name) {
        List list = new ArrayList();
        getElementsByTagName(name, list);
        return new NodeListImpl(list);
    }


    private void getElementsByTagName(String name, List list) {
        if (nodeName.equals(name)) {
            list.add(this);
        }

        Node child = getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE)
                ((ElementImpl)child).getElementsByTagName(name, list);
            child = child.getNextSibling();
        }
    }


	/*
	 * Equivalent to getElementsByTagName(localName).
	 */
	public NodeList getElementsByTagNameNS(String namespaceURI,	String localName) {
		return getElementsByTagName(localName);
	}


	/*
	 * Returns true if this node has children nodes.
	 */
	public boolean hasElementChildNodes() {
        Node child = getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE)
                return true;
            child = child.getNextSibling();
        }
		return false;
	}


	/*
	 * Method beginToString for this class writes the xml
	 * begining tag string and all attributes.
	 */
	protected void beginToString(StringBuffer sb, Indent indent) {
		sb.append("\n" + indent + "<" + this.nodeName);

		for (Iterator iter = attributes.values().iterator(); iter.hasNext();) {
			Attr attr = (Attr) iter.next();
			sb.append(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
		}
        if (hasChildNodes()) {
       		sb.append(">");
        	indent.increment();
        } else 
       		sb.append("/>");
	}


	/*
	 * Method endToString for this class writes the xml
	 * ending tag string.
	 */
	protected void endToString(StringBuffer sb, Indent indent) {
        if (hasChildNodes()) {
        	indent.decrement();
        	if (hasElementChildNodes())
			    sb.append("\n" + indent + "</" + this.nodeName + ">");
			else
			    sb.append("</" + this.nodeName + ">");
        }
	}
  
  /*
   * Dummy method that implements the abstract method.
   */
  public TypeInfo getSchemaTypeInfo() {
    return null;
  }  
  
  /*
   * Dummy method that implements the abstract method.
   */
  public void setIdAttribute(String name, boolean isId) throws DOMException {
    //
  }    
  
  /*
   * Dummy method that implements the abstract method.
   */
  public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
    //
  }     

  /*
   * Dummy method that implements the abstract method.
   */
  public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
    //
  }      
  
}
