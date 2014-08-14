package gov.va.med.lom.javaUtils.xml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.Element
 *
 * Namespaces are ignored in this implementation.  The terms "tag
 * name" and "node name" are always considered to be synonymous.
 */
public class TextImpl extends CharacterDataImpl implements Text {


	/*
	 * Constructs a TextImpl from the given node.
	 */
	public TextImpl(TextImpl node) {
		super((NodeImpl)node);
	}
	
	
	/*
	 * Constructs a TextImpl from the given node value.
	 */
	public TextImpl(String value) {
		nodeValue = value;
		type = Node.TEXT_NODE;
	}


	/*
	 * Constructs a TextImpl from a given node,
	 * as a Node
	 */
	public TextImpl(Node node) {
		super(node);
	}

	
	/*
	 * Returns the node type.
	 */
    public short getNodeType() {
        return Node.TEXT_NODE;
    }

    /*
	 * Returns the name ("#text") associated with this node.
	 */
    public String getNodeName() {
        return "#text";
    }
    
    
    /*
	 * Returns the trimed node value associated with this node.
	 */
	public String getNodeValue() throws DOMException {
		return nodeValue.trim();
	}
	
	
	/*
	 * Method beginToString for this class writes the value
	 * of this node (text).
	 */
	protected void beginToString(StringBuffer sb, Indent indent) {
		sb.append(this.nodeValue.trim());
	}
	
	/*
	 * Method endToString does nothing.
	 */
	protected void endToString(StringBuffer sb, Indent indent) {
	}
	
	
	/*
     * Break a text node into two sibling nodes.  (Note that if the
     * current node has no parent, they won't wind up as "siblings" --
     * they'll both be orphans.)
     */
    public Text splitText(int offset) 
        throws DOMException {

    	if (offset < 0 || offset > nodeValue.length() ) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
        }
    		
        // split text into two separate nodes
    	TextImpl newText = new TextImpl(nodeValue.substring(offset));
    	nodeValue = nodeValue.substring(0, offset);

        // insert new text node
        Node parentNode = getParentNode();
    	if (parentNode != null) {
    		parentNode.insertBefore(newText, nextSibling);
        }

    	return newText;

    }

  /*
   * Dummy method that implements the abstract method in the interface.
   */      
  public String getWholeText() {
    return null;
  }
    
  /*
   * Dummy method that implements the abstract method in the interface.
   */     
  public Text replaceWholeText(String content) throws DOMException {
    return null;
  }    
    
  /*
   * Dummy method that implements the abstract method in the interface.
   */     
  public boolean isElementContentWhitespace() {
    return false;
  }     
  
}
