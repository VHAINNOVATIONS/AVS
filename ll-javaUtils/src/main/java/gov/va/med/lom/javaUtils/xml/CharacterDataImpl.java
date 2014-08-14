package gov.va.med.lom.javaUtils.xml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;

/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.CharacterData
 */
public class CharacterDataImpl extends NodeImpl implements CharacterData {
	
	
	/*
	 * Constructs an empty CharacterDataImpl.
	 */
	public CharacterDataImpl() {
	}
	
	/*
	 * Constructs a CharacterDataImpl from the 
	 * given node as Node.
	 */
	public CharacterDataImpl(Node node) {
		super(node);
	}


	/*
	 * Returns node value.
	 */
	public String getData() throws DOMException {
		return nodeValue;
	}

	/*
	 * Sets the new value of this node.
	 */
	public void setData(String data) throws DOMException {
		nodeValue = data;
	}


	/*
	 * Returns the substring from the node's value.
	 */
	public String substringData(int offset, int count) throws DOMException {
		int length = nodeValue.length();
        if (count < 0 || offset < 0 || offset > length - 1)
            throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");

        int tailIndex = length;
        if(offset + count < length)
        	tailIndex = offset + count;
        return nodeValue.substring(offset, tailIndex);
	}


	/*
	 * Appends data to the node's value.
	 */
	public void appendData(String arg) {
		nodeValue += arg;
	}


	/*
	 * Inserts substring into node's value string.
	 */
	public void insertData(int offset, String arg) throws DOMException {
		try {
			nodeValue = new StringBuffer(nodeValue).insert(offset, arg).toString();
		} catch (StringIndexOutOfBoundsException e) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
		}
	}


	/*
	 * Deletes characters from the node's value string.
	 */
	public void deleteData(int offset, int count) throws DOMException {
		int tailLength = nodeValue.length() - count - offset;
		if(nodeValue.length() - count - offset < 0)
			tailLength = 0;
        try {
            nodeValue = nodeValue.substring(0, offset) +
                (tailLength > 0 ? nodeValue.substring(offset + count, offset + count + tailLength) : "");
        } catch (StringIndexOutOfBoundsException e) {
        	throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
        }
	}


	/*
	 * Replaces characters in the node's value string.
	 */
	public void replaceData(int offset, int count, String arg) throws DOMException {
		deleteData(offset, count);
        insertData(offset, arg);
	}


	/*
	 * Returns the namespace of the node.
	 */
	public String getNamespaceURI() {
		return super.getNamespaceURI();
	}
	

}
