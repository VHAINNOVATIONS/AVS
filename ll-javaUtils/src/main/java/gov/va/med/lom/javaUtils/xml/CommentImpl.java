package gov.va.med.lom.javaUtils.xml;

import org.w3c.dom.Comment;
import org.w3c.dom.Node;

/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.Comment
 *
 * Namespaces are ignored in this implementation.  The terms "tag
 * name" and "node name" are always considered to be synonymous.
 */
public class CommentImpl extends CharacterDataImpl implements Comment {

	/*
	 * Constructs a CommentImpl from the given node.
	 */
	public CommentImpl(CommentImpl node) {
		super((NodeImpl)node);
	}


	/*
	 * Constructs a CommentImpl from the given node value.
	 */
	public CommentImpl(String value) {
		nodeValue = value;
		type = Node.COMMENT_NODE;
	}
	
	
	/*
	 * Constructs a CommentImpl from a given node,
	 * as a Node
	 */
	public CommentImpl(Node node) {
		super(node);
	}


	/*
	 * Returns the node type.
	 */
    public short getNodeType() {
        return Node.COMMENT_NODE;
    }

    /*
	 * Returns the name ("#comment") associated with this node.
	 */
    public String getNodeName() {
        return "#comment";
    }
    
	
	/*
	 * Method beginToString for this class writes the xml
	 * comment prefix string and the comment string.
	 */
	protected void beginToString(StringBuffer sb, Indent indent) {
		sb.append("\n" + indent + "<!-- " + this.nodeValue.trim());
	}
	
	
	/*
	 * Method endToString writes the xml comment postfix string.
	 */
	protected void endToString(StringBuffer sb, Indent indent) {
		sb.append(" -->");
	}
	

}
