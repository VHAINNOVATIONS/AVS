package gov.va.med.lom.javaUtils.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.Element
 *
 * Namespaces are ignored in this implementation.  The terms "tag
 * name" and "node name" are always considered to be synonymous.
 */
public class HashMapElement extends ElementImpl {


	/*
	 * All Element type children of this Element
	 */
	protected HashMap children = null;


	/*
	 * Constructs an empty HashMapElement.
	 */
	public HashMapElement() {
		super();
		children = new HashMap();
	}


	/*
	 * Constructs a HashMapElement from the given node,
	 * without creating entire children subtree.
	 */
	public HashMapElement(HashMapElement element) {
		super(element);
		children = element.children;
	}


	/*
	 * Constructs an HashMapElement with the given
	 * document owner and node name.
	 */
	public HashMapElement(Document ownerDoc, String name) {
		super(ownerDoc, name);
		this.children = new HashMap();
	}


	/*
	 * Constructs an HashMapElement with the given
	 * document owner, node name, node type and node value.
	 */
	protected HashMapElement(Document ownerDoc, String nodeName, short type, String value) {
		super(ownerDoc, nodeName, type, value);
	}


	/*
	 * Constructs an HashMapElement from a given node
	 * (creates the children subtree too), as a Node
	 */
	public HashMapElement(Node node) {
		this(node, true);
	}

	/*
	 * Constructs an HashMapElement from a given node,
	 * as a Node, and deep as boolean.
	 */
	public HashMapElement(Node node, boolean deep) {
		super(node, false);
		children = new HashMap();
		if (deep)
			initNodeImplChildren(node);
	}


	/*
	 * Creates new instance of the HashMapElement class from the given Node.
	 */
	protected Node newElementInstance(Node node) {
		return new HashMapElement(node);
	}

  /*
	 * Creates new instance of HashMapElement from a given document
	 * as a Document.
	 */
	public static Element newInstance(Document document) {
		Node root = document.getDocumentElement();
		return new HashMapElement(root);
	}

	/*
	 * Inserts the node newChild before the existing
	 * child node refChild. If refChild is
	 * null, insert newChild at the end of
	 * the list of children.
	 */
	public Node insertBefore(Node newChild, Node refChild) {
		super.insertBefore(newChild, refChild);

		if (newChild.getNodeType() == ELEMENT_NODE) {
			HashMapElement newChildNode = (HashMapElement) newChild;

			List list = (List) children.get(newChildNode.getTagName());
			if (list == null)
				list = new ArrayList();
			list.add(newChildNode);
			children.put(newChildNode.getTagName(), list);
		}
		return newChild;
	}


	/*
	 * Replaces the child node oldChild with
	 * newChild in the list of children, and returns the
	 * oldChild node.
	 */
	public Node replaceChild(Node newChild, Node oldChild) {
		super.replaceChild(newChild, oldChild);

		if (oldChild.getNodeType() == ELEMENT_NODE) {
			HashMapElement oldChildNode = (HashMapElement) oldChild;
			List list = (List) children.get(oldChildNode.getTagName());
			if (list != null) {
				int index = list.indexOf(oldChildNode);
				if (index != -1)
					list.remove(index);
				if (list.size() == 0)
					children.remove(oldChildNode.getTagName());
			}
		}
		if (newChild.getNodeType() == ELEMENT_NODE) {
			HashMapElement newChildNode = (HashMapElement) newChild;
			List list = (List) children.get(newChildNode.getTagName());
			if (list == null)
				list = new ArrayList();
			list.add(newChildNode);
			children.put(newChildNode.getTagName(), list);
		}

		return oldChild;
	}


	/*
	 * Removes the child node indicated by oldChild from
	 * the list of children, and returns it.
	 */
	public Node removeChild(Node oldChild) {
		super.removeChild(oldChild);

		if (oldChild.getNodeType() == ELEMENT_NODE) {
			HashMapElement oldChildNode = (HashMapElement) oldChild;

			List list = (List) children.get(oldChildNode.getTagName());
			if (list != null) {
				int index = list.indexOf(oldChildNode);
				if (index != -1)
					list.remove(index);
				if (list.size() == 0)
					children.remove(oldChildNode.getTagName());
			}
		}
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
		return new HashMapElement(this, deep);
	}



	/*
	 * Returns all Element nodes with given name,
	 * searching by all sub nodes from this node.
	 */
	public NodeList getElementsByTagName(String name) {
		List list = new ArrayList();

		// added for new search
		if (nodeName.equals(name)) {
			list.add(this);
		}

		getElementsByTagName(name, list);
		return new NodeListImpl(list);
	}


	private void getElementsByTagName(String name, List list) {
		if (numChildren == 0)
			return;
		List fList = (List) children.get(name);
		if (fList != null);
		list.addAll(fList);
		for (Iterator iter = children.values().iterator(); iter.hasNext();) {
			fList = (List) iter.next();
			for (int i = 0; i < fList.size(); i++) {
				((HashMapElement) fList.get(i)).getElementsByTagName(
					name,
					list);
			}
		}
	}


	/*
	 * Returns true if this node has child nodes.
	 */
	public boolean hasElementChildNodes() {
		return children.size() > 0;
	}


	/*
	 * Returns the list of all children nodes with the given tag name.
	 */
	public NodeList getChildrenByTagName(String name) {
		List list = (List) this.children.get(name);
		if (list != null)
			return new NodeListImpl(list);
		return null;
	}


	/*
	 * Returns the first child Element with the given tag name.
	 */
	public Element getFirstChildByTagName(String name) {
		NodeList children = getChildrenByTagName(name);
		if (children != null && children.getLength() > 0)
			return (HashMapElement) children.item(0);
		return null;
	}


	/*
	 * Returns the next Element node (if exists) with the same tag name.
	 */
	public Element getNextSameNameNode() {
		try {
			HashMapElement parent = (HashMapElement) this.getParentNode();
			List tagList = (List) parent.children.get(this.nodeName);
			int index = tagList.indexOf(this);
			if (++index <= tagList.size())
				return (HashMapElement) tagList.get(index);
		} catch (NullPointerException e) {
			throw new NodeDOMException(
				DOMException.NOT_FOUND_ERR,
				"Root node doesn't have a successor");
		}
		return null;
	}


	/*
	 * Returns the concatenation of values of all text type children.
	 */
	public String getText() {
		String text = "";
		Node child = this.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.TEXT_NODE)
				text += child.getNodeValue();
			child = child.getNextSibling();
		}
		if (!text.equals(""))
			return text;
		return null;
	}


	/*
	 * Set the value of the first text child node to the given text,
	 * and remove all other text child nodes.
	 */
	public void setText(String text) {
		Node child = this.getFirstChild();
		if (child != null) {
			child.setNodeValue(text);
			child = child.getNextSibling();
			while (child != null) {
				if (child.getNodeType() == Node.TEXT_NODE) {
					Node temp = child;
					child = child.getNextSibling();
					this.removeChild(temp);
				} else {
					child = child.getNextSibling();
				}
			}
		}
	}


}