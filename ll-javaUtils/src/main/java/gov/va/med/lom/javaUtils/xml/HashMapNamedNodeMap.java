package gov.va.med.lom.javaUtils.xml;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Node;

/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.NamedNodeMap
 */
class HashMapNamedNodeMap extends NamedNodeMapImpl {

	
	/*
	 * HashMap of Node items.
	 */
	HashMap hNodes;


	/*
	 * Constructs a HashMapNamedNodeMap from the given HashMap.
	 */
	public HashMapNamedNodeMap(HashMap nodes) {
		super(new ArrayList(nodes.values()));
		this.hNodes = nodes;
	}


	/*
	 * Returns the items count.
	 */
	public int getLength() {
		return hNodes.size();
	}

	
	/*
	 * Returns the Node item with the given name.
	 */
	public Node getNamedItem(String name) {
		return (Node) hNodes.get(name);
	}
	
}
