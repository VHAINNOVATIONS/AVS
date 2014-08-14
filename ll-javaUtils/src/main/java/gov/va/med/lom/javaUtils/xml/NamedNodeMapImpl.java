package gov.va.med.lom.javaUtils.xml;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.NamedNodeMap
 */
class NamedNodeMapImpl implements NamedNodeMap {

	/*
	 * List of Nodes.
	 */
	List nodes;


	/*
	 * Constructs new NamedNodeMapImpl with the given list of nodes.
	 */
	public NamedNodeMapImpl(List nodes) {
		this.nodes = nodes;
	}

	/*
	 * Returns the count of nodes.
	 */
	public int getLength() {
		return nodes.size();
	}


	/*
	 * Returns the Node with the given name.
	 */
    public Node getNamedItem(String name) {
        Iterator iter = nodes.iterator();
        while (iter.hasNext()) {
            Node node = (Node)iter.next();
            if (name.equals(node.getNodeName())) {
                return node;
            }
        }

        return null;
    }

	/*
	 * Returns the Node with the given index.
	 */
	public Node item(int index) {
		Node node = (Node) nodes.get(index);
		return node;
	}


	/*
	 * Modification of the items is not allowed !
	 * Removes the item with the given name.
	 */
	public Node removeNamedItem(java.lang.String name) {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This NamedNodeMap is read-only!");
	}


	/*
	 * Modification of the items is not allowed !
	 * Sets the item with the given name.
	 */
	public Node setNamedItem(Node arg) {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This NamedNodeMap is read-only!");
	}


	/*
	 * Equivalent to getNamedItem(localName).
	 */
	public Node getNamedItemNS(String namespaceURI, String localName) {
		return getNamedItem(localName);
	}


	/*
	 * Equivalent to setNamedItem(arg).
	 */
	public Node setNamedItemNS(Node arg) {
		return setNamedItem(arg);
	}


	/*
	 * Equivalent to removeNamedItem(localName).
	 */
	public Node removeNamedItemNS(String namespaceURI, String localName) {
		return removeNamedItem(localName);
	}
	
}
