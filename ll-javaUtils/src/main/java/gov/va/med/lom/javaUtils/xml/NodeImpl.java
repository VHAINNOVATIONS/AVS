package gov.va.med.lom.javaUtils.xml;

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.NodeList
 *
 * Namespaces are ignored in this implementation.  The terms "tag
 * name" and "node name" are always considered to be synonymous.
 */
public class NodeImpl implements Node, NodeList {

	/*
	 * Owner document.
	 */
	protected Document ownerDocument;


	/*
	 * The name (tag) of the node as a String.
	 */
	protected String nodeName = null;


	/*
	 * The value of the node as a String.
	 */
	protected String nodeValue = null;


	/*
	 * The type of the node as a short.
	 */
	protected short type = ELEMENT_NODE;


	/*
	 * The parent node of this node, or null if this node
	 * forms the root of its own tree.
	 */
	protected NodeImpl parent = null;


	/*
	 * The number of child nodes.
	 */
	protected int numChildren = 0;


	/*
	 * The first (leftmost) child node of this node, or
	 * null if this node is a leaf node.
	 */
	protected NodeImpl firstChild = null;


	/*
	 * The last (rightmost) child node of this node, or
	 * null if this node is a leaf node.
	 */
	protected NodeImpl lastChild = null;


	/*
	 * The next (right) sibling node of this node, or
	 * null if this node is its parent's last child node.
	 */
	protected NodeImpl nextSibling = null;


	/*
	 * The previous (left) sibling node of this node, or
	 * null if this node is its parent's first child node.
	 */
	protected NodeImpl previousSibling = null;


	/*
	 * Constructs an empty NodeImpl.
	 */
	public NodeImpl() {
	}


	/*
	 * Constructs a NodeImpl from the given node,
	 * without creating entire children subtree.
	 */
	public NodeImpl(NodeImpl node) {
		ownerDocument = node.ownerDocument;
		nodeName = node.nodeName;
		nodeValue = node.nodeValue;
		type = node.type;
		parent = node.parent;
		numChildren = node.numChildren;
		firstChild = node.firstChild;
		lastChild = node.lastChild;
		nextSibling = node.nextSibling;
		previousSibling = node.previousSibling;
	}


	/*
	 * Constructs an NodeImpl from a given node (creates the children subtree too),
	 * as a Node
	 *
	 * @param node, as a Node.
	 */
	public NodeImpl(Node node) {
	    this(node, true);
	}


	/*
	 * Constructs an NodeImpl from a given node, as a Node,
	 * and deep as boolean.
	 */
	public NodeImpl(Node node, boolean deep) {
		this.ownerDocument = node.getOwnerDocument();
		this.nodeName = node.getNodeName();
		this.type = node.getNodeType();
		this.nodeValue = node.getNodeValue();
		if (deep)
		    this.initNodeImplChildren(node);
	}


	/*
	 * Constructs a NodeImpl from the given document owner and node name.
	 */
	public NodeImpl(Document ownerDoc, String name) {
		this.ownerDocument = ownerDoc;
		this.nodeName = name;
	}


	/*
	 * Constructs an NodeImpl from a given document owner,
	 * node name and node type.
	 */
	public NodeImpl(Document ownerDoc, String nodeName, short type) {
		this.ownerDocument = ownerDoc;
		this.nodeName = nodeName;
		this.type = type;
	}


	/*
	 * Constructs an NodeImpl from a given document owner,
	 * node name, node type and node value.
	 */
	public NodeImpl(Document ownerDoc, String nodeName, short type, String value) {
		this.ownerDocument = ownerDoc;
		this.nodeName = nodeName;
		this.type = type;
		this.nodeValue = value;
	}



	/*
	 * Creates the children subtree and adds to this node.
	 * (this part had to be splited from the constructor)
	 */
	protected void initNodeImplChildren(Node node) {
		// add children
		Node child = node.getFirstChild();
		while (child != null) {
    		switch (child.getNodeType()) {
    			case Node.ELEMENT_NODE : {
    				this.appendChild(newElementInstance(child));
    			}; break;
    			case Node.TEXT_NODE : {
    				this.appendChild(newTextInstance(child));
    			}; break;
    			case Node.COMMENT_NODE : {
    				this.appendChild(newCommentInstance(child));
    			}; break;
    			default : {
    				this.appendChild(newDefaultInstance(child));
    			};
    		}
			child = child.getNextSibling();
		}
	}


	/*
	 * Creates new instance of the ElementImpl class.
	 */
    protected Node newElementInstance(Node node) {
        return new ElementImpl(node);
    }

    /*
	 * Creates new instance of the TextImpl class.
	 */
    protected Node newTextInstance(Node node) {
        return new TextImpl(node);
    }

    /*
	 * Creates new instance of the CommentImpl class.
	 */
    protected Node newCommentInstance(Node node) {
        return new CommentImpl(node);
    }

    /*
	 * Creates new instance of the NodeImpl class.
	 */
    protected Node newDefaultInstance(Node node) {
        return new NodeImpl(node);
    }


	/*
	 * Check that the node is either null or an
	 * NodeImpl.
	 */
	private void checkNode(Node node) throws DOMException {
		if (node == null) {
			return;
		}
		if (!(node instanceof NodeImpl))
			throw new NodeDOMException(DOMException.WRONG_DOCUMENT_ERR, "Node not an NodeImpl!");
	}



	// Methods from Node

	/*
	 * Returns the name associated with this node.
	 */
	public String getNodeName() {
		return nodeName;
	}


	/*
	 * Returns the value associated with this node.
	 */
	public String getNodeValue() {
		return nodeValue;
	}


	/*
	 * Sets the node value of this node.
	 */
	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}


	/*
	 * Returns the node type.
	 */
	public short getNodeType() {
		return type;
	}


	/*
	 * Returns the parent of this node. A null value
	 * indicates that the node is the root of its own tree.  To add a
	 * node to an existing tree, use one of the
	 * insertBefore, replaceChild, or
	 * appendChild methods.
	 */
	public Node getParentNode() {
		return parent;
	}


	/*
	 * Returns all child nodes of this node, or null if
	 * the node has no children.
	 */
	public NodeList getChildNodes() {
		return this;
	}


	/*
	 * Returns the first child of this node, or null if
	 * the node has no children.
	 */
	public Node getFirstChild() {
		return firstChild;
	}


	/*
	 * Returns the last child of this node, or null if
	 * the node has no children.
	 */
	public Node getLastChild() {
		return lastChild;
	}


	/*
	 * Returns the previous sibling of this node, or null
	 * if this node has no previous sibling.
	 */
	public Node getPreviousSibling() {
		return previousSibling;
	}


	/*
	 * Returns the next sibling of this node, or null if
	 * the node has no next sibling.
	 */
	public Node getNextSibling() {
		return nextSibling;
	}


	/*
	 * Returns null, since NodeImpls
	 * do not belong to any Document.
	 */
	public Document getOwnerDocument() {
		return ownerDocument;
	}


	/*
	 * Inserts the node newChild before the existing
	 * child node refChild. If refChild is
	 * null, insert newChild at the end of
	 * the list of children.
	 */
	public Node insertBefore(Node newChild, Node refChild) {
		if (newChild == null) {
			throw new IllegalArgumentException("newChild == null!");
		}

		checkNode(newChild);
		checkNode(refChild);

		NodeImpl newChildNode = (NodeImpl) newChild;
		NodeImpl refChildNode = (NodeImpl) refChild;

		// Siblings, can be null.
		NodeImpl previous = null;
		NodeImpl next = null;

		if (refChild == null) {
			previous = this.lastChild;
			next = null;
			this.lastChild = newChildNode;
		} else {
			previous = refChildNode.previousSibling;
			next = refChildNode;
		}

		if (previous != null) {
			previous.nextSibling = newChildNode;
		}
		if (next != null) {
			next.previousSibling = newChildNode;
		}

		newChildNode.parent = this;
		newChildNode.previousSibling = previous;
		newChildNode.nextSibling = next;

		// N.B.: O.K. if refChild == null
		if (this.firstChild == refChildNode) {
			this.firstChild = newChildNode;
		}
		++numChildren;
		return newChildNode;
	}


	/*
	 * Replaces the child node oldChild with
	 * newChild in the list of children, and returns the
	 * oldChild node.
	 */
	public Node replaceChild(Node newChild, Node oldChild) {
		if (newChild == null) {
			throw new IllegalArgumentException("newChild == null!");
		}

		checkNode(newChild);
		checkNode(oldChild);

		NodeImpl newChildNode = (NodeImpl) newChild;
		NodeImpl oldChildNode = (NodeImpl) oldChild;

		NodeImpl previous = oldChildNode.previousSibling;
		NodeImpl next = oldChildNode.nextSibling;

		if (previous != null) {
			previous.nextSibling = newChildNode;
		}
		if (next != null) {
			next.previousSibling = newChildNode;
		}

		newChildNode.parent = this;
		newChildNode.previousSibling = previous;
		newChildNode.nextSibling = next;

		if (firstChild == oldChildNode) {
			firstChild = newChildNode;
		}
		if (lastChild == oldChildNode) {
			lastChild = newChildNode;
		}

		oldChildNode.parent = null;
		oldChildNode.previousSibling = null;
		oldChildNode.nextSibling = null;

		return oldChildNode;
	}


	/*
	 * Removes the child node indicated by oldChild from
	 * the list of children, and returns it.
	 */
	public Node removeChild(Node oldChild) {
		if (oldChild == null) {
			throw new IllegalArgumentException("oldChild == null!");
		}
		checkNode(oldChild);

		NodeImpl oldChildNode = (NodeImpl) oldChild;

		NodeImpl previous = oldChildNode.previousSibling;
		NodeImpl next = oldChildNode.nextSibling;

		if (previous != null) {
			previous.nextSibling = next;
		}
		if (next != null) {
			next.previousSibling = previous;
		}

		if (this.firstChild == oldChildNode) {
			this.firstChild = next;
		}
		if (this.lastChild == oldChildNode) {
			this.lastChild = previous;
		}

		oldChildNode.parent = null;
		oldChildNode.previousSibling = null;
		oldChildNode.nextSibling = null;

		--numChildren;
		return oldChildNode;
	}


	/*
	 * Adds the node newChild to the end of the list of
	 * children of this node.
	 */
	public Node appendChild(Node newChild) {
		if (newChild == null) {
			throw new IllegalArgumentException("newChild == null!");
		}
		checkNode(newChild);

		// insertBefore will increment numChildren
		return insertBefore(newChild, null);
	}


	/*
	 * Returns true if this node has child nodes.
	 */
	public boolean hasChildNodes() {
		return numChildren > 0;
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
        return new NodeImpl(this, deep);
    }


	/*
	 * Does nothing, since NodeImpls do not
	 * contain Text children.
	 */
	public void normalize() {
	}


	/*
	 * Returns false since DOM features are not
	 * supported.
	 */
	public boolean isSupported(String feature, String version) {
		return false;
	}


	/*
	 * Returns null, since namespaces are not supported.
	 */
	public String getNamespaceURI() throws DOMException {
		return null;
	}


	/*
	 * Returns null, since namespaces are not supported.
	 */
	public String getPrefix() {
		return null;
	}


	/*
	 * Does nothing, since namespaces are not supported.
	 */
	public void setPrefix(String prefix) {
	}


	/*
	 * Equivalent to getNodeName.
	 */
	public String getLocalName() {
		return nodeName;
	}


	/*
	 * Returns all attribute nodes of this node.
	 */
	public NamedNodeMap getAttributes() {
		return null;
	}


	/*
	 * Returns true, if this node has attributes, otherwise
	 * false.
	 */
	public boolean hasAttributes() {
		return false;
	}




	// Methods from NodeList


	/*
	 * Returns number of child nodes.
	 */
	public int getLength() {
		return numChildren;
	}


	/*
	 * Returns child node with the given index.
	 */
	public Node item(int index) {
		if (index < 0) {
			return null;
		}

		Node child = getFirstChild();
		while (child != null && index-- > 0) {
			child = child.getNextSibling();
		}
		return child;
	}



    // String methodes

	/*
	 * Returns String representation of this node.
	 */
	public String toString() {
		return toString(Indent.DEFAULT_TAB);
	}


	/*
	 * Returns String representation of this node.
	 */
	public String toString(String tab) {
		StringBuffer sb = new StringBuffer();
		this.allToString(sb, new Indent(0,tab));
		return sb.toString();
	}


	/*
	 * Method beginToString should be redefined in extended classes.
	 * Each type of node has its own beginToString and
	 * endToString. This was added to support
	 * writing of the xml file. The Element
	 * type of node: it writes the beginning tag, then calls
	 * the child's toString, and then writes the ending tag.
	 */
	protected void beginToString(StringBuffer sb, Indent indent) {
	}


	/*
	 * Method endToString should be redefined in extended classes.
	 * Each type of node has its own beginToString and
	 * endToString. This was added to support
	 * writing of the xml file. The Element
	 * type of node: it writes the beginning tag, then calls
	 * the child's toString, and then writes the ending tag.
	 */
	protected void endToString(StringBuffer sb, Indent indent) {
	}


	private void allToString(StringBuffer sb, Indent indent) {
		this.beginToString(sb, indent);
		Node child = getFirstChild();
		while (child != null) {
			((NodeImpl) child).allToString(sb, indent);
			child = child.getNextSibling();
		}
	    this.endToString(sb, indent);
	}
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */
  public String getBaseURI() {
    return null;
  }
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */  
  public Object getFeature(String feature, String version) {
    return null; 
  }
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */  
  public String getTextContent() {
    return null; 
  }
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */  
  public Object getUserData(String key) {
    return null; 
  }  
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */  
  public boolean isDefaultNamespace(String namespaceURI) {
    return false; 
  } 
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */  
  public boolean isEqualNode(Node arg) {
    return false; 
  }    
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */  
  public boolean isSameNode(Node arg) {
    return false; 
  }      
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */    
  public String lookupNamespaceURI(String prefix) {
    return null;
  }

  /*
   * Dummy method that implements the abstract method in the interface.
   */  
  public String lookupPrefix(String namespaceURI) {
    return null;
  }
 
  /*
   * Dummy method that implements the abstract method in the interface.
   */  
  public void setTextContent(String textContent) throws DOMException {
    //
  }
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */    
  public Object setUserData(String key, Object data, UserDataHandler handler) {
    return null;
  }
  
  /*
   * Dummy method that implements the abstract method in the interface.
   */     
  public short compareDocumentPosition(Node other) throws DOMException {
    return 0;
  }
}



