package gov.va.med.lom.javaUtils.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.NodeList
 *
 * Namespaces are ignored in this implementation.  The terms "tag
 * name" and "node name" are always considered to be synonymous.
 */
public class NodeListImpl implements NodeList {

	/*
	 * List of Nodes.
	 */
	List nodes;


	/*
	 * Constructs an empty NodeListImpl.
	 */
	public NodeListImpl() {
		this.nodes = new ArrayList();
	}
	
	
	/*
	 * Constructs NodeListImpl with the given list of nodes.
	 */
	public NodeListImpl(List nodes) {
		this.nodes = nodes;
	}


	/*
	 * Returns the count of nodes.
	 */
	public int getLength() {
		return nodes.size();
	}


	/*
	 * Returns the Node with the given index.
	 */
	public Node item(int index) {
		if (index < 0 || index > nodes.size()) {
			return null;
		}
		return (Node) nodes.get(index);
	}

	
	/*
	 * Appends the given list to the end of the existing one.
	 */
	public NodeList append(NodeListImpl list) {
		this.nodes.addAll(list.nodes);
		return this;
	}
	
}