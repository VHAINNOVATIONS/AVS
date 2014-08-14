package gov.va.med.lom.javaUtils.xml;

import java.util.ArrayList;
import java.util.List;

//import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.Element
 *
 * Namespaces are ignored in this implementation.  The terms "tag
 * name" and "node name" are always considered to be synonymous.
 */
public class SearchElement extends HashMapElement {

	public static String TAG_SEPARATOR = "/";
	public static String ATTR_SEPARATOR = "@";
	public static String EQUAL_SEPARATOR = "=";

	/*
	 * Constructs an empty SearchElement.
	 */
	public SearchElement() {
	}

	/*
	 * Constructs an SearchElement with a given document owner and node name.
	 */
	public SearchElement(Document ownerDoc, String name) {
		super(ownerDoc, name);
	}

	/*
	 * Constructs an SearchElement with a given Node.
	 */
	public SearchElement(Node node) {
		super(node);
	}

	/*
	 * Constructs an SearchElement with a given SearchElement.
	 */
	public SearchElement(SearchElement node) {
		super((HashMapElement) node);
	}

	/*
	 * Returns new SearchElement instance from a given Node.
	 */
	protected Node newElementInstance(Node node) {
		return new SearchElement(node);
	}

  /*
	 * Creates new instance of SearchElement from a given document
	 * as a Document.
	 */
	public static Element newInstance(Document document) {
		Node root = document.getDocumentElement();
		return new SearchElement(root);
	}

	/*
	 * Returns a list of elements in the subtree of this node, with the given tag name.
	 */
	public NodeList getSubElementsByTagName(String namePath) {
		List list = new ArrayList();
		getSubElementsByTagName(namePath, list);
		return new NodeListImpl(list);
	}

	/*
	 * Recursively fills the list with all the nodes on the given path.
	 */
	private void getSubElementsByTagName(String name, List list) {
		String[] keys = name.split(TAG_SEPARATOR, 2);
		if (keys.length == 1) {
			List l = (List) this.children.get(name);
			if (l != null)
				list.addAll(l);
			return;
		}
		NodeList tagChildren = this.getChildrenByTagName(keys[0]);
		if (tagChildren != null)
			for (int i = 0; i < tagChildren.getLength(); i++)
				((SearchElement) tagChildren.item(i)).getSubElementsByTagName(
					keys[1],
					list);
	}

	/*
	 * Returns a list of Elements in the subtree of this node,
	 * which contain attribute with the given name and value.
	 */
	public NodeList getSubElementsByAttrValue(String attrPath, String attrValue) {
		String[] keys = attrPath.split(ATTR_SEPARATOR, 2);
		if (keys.length != 2)
			throw new DOMException(
				DOMException.INVALID_ACCESS_ERR,
				"Parameter not supported");
		List list = new ArrayList();
		getSubElementsByAttrValue(keys[0], keys[1], attrValue, list);
		return new NodeListImpl(list);
	}


	/*
	 * Recursively fills the list with all the nodes in the given path.
	 */
	private void getSubElementsByAttrValue(String tagName, String attrName, String attrValue, List list) {
		String[] keys = tagName.split(TAG_SEPARATOR, 2);
		if (keys.length == 1) {
			List fList = (List) this.children.get(tagName);
			if (fList != null) {
				for (int i = 0; i < fList.size(); i++) {
					Element elm = (Element) fList.get(i);
					String val = (String) elm.getAttribute(attrName);
					if (val != null)
						if (val.equals(attrValue))
							list.add(elm);
				}
			}
			return;
		}
		NodeList tagChildren = this.getChildrenByTagName(keys[0]);

		if (tagChildren != null) {
			for (int i = 0; i < tagChildren.getLength(); i++)
				((SearchElement) tagChildren.item(i)).getSubElementsByAttrValue(
					keys[1],
					attrName,
					attrValue,
					list);
		}
	}


	/*
	 * Returns a list of Elements in the subtree of this node,
	 * with the given tag name and value.
	 */
	public NodeList getSubElementsByTagText(String tagPath, String tagValue) {
		List list = new ArrayList();
		getSubElementsByTagText(tagPath, tagValue, list);
		return new NodeListImpl(list);
	}


	/*
	 * Recursively fills the list with all the nodes in the given path.
	 */
	private void getSubElementsByTagText(
		String tagName,
		String tagValue,
		List list) {
		String[] keys = tagName.split(TAG_SEPARATOR, 2);
		if (keys.length == 1) {
			List fList = (List) this.children.get(tagName);
			if (fList != null) {
				for (int i = 0; i < fList.size(); i++) {
					HashMapElement elm = (HashMapElement) fList.get(i);
					String val = (String) elm.getText();
					if (val != null)
						if (val.equals(tagValue))
							list.add(elm);
				}
			}
			return;
		}
		NodeList tagChildren = this.getChildrenByTagName(keys[0]);
		if (tagChildren != null) {
			for (int i = 0; i < tagChildren.getLength(); i++)
				((SearchElement) tagChildren.item(i)).getSubElementsByTagText(
					keys[1],
					tagValue,
					list);
		}
	}


	/*
	 * Returns a list of Elements in the subtree of this node,
	 * that satisfy the given condition.
	 */
	public NodeList getSubElementsByCondition(String condition) {
		if (!condition.matches("([^@=]*)(@?[^@=/]*=[^@=/]*)"))
			throw new DOMException(
				DOMException.INVALID_ACCESS_ERR,
				"Parameter not supported");
		String[] keys = condition.split(EQUAL_SEPARATOR, 2);
		String namePath = keys[0];
		if (namePath.indexOf(ATTR_SEPARATOR) != -1) {
			return getSubElementsByAttrValue(namePath, keys[1]);
		} else
			return getSubElementsByTagText(namePath, keys[1]);
	}


	/*
	 * Returns the first Element in the subtree of this node,
	 * that satisfy the given condition.
	 */
	public Element getFirstSubElementByCondition(String condition) {
		NodeList nodes = getSubElementsByCondition(condition);
		if (nodes != null && nodes.getLength() > 0)
			return (Element) nodes.item(0);
		return null;
	}


	/*
	 * Returns the first Element in the subtree of this node,
	 * with the given tag name.
	 */
	public Element getFirstSubElementByTagName(String namePath) {
		NodeList nodes = getSubElementsByTagName(namePath);
		if (nodes != null && nodes.getLength() > 0)
			return (Element) nodes.item(0);
		return null;
	}


	/*
	 * Return the text of the Element found on the given path.
	 */
	public String getText(String namePath) {
		NodeList nodes = this.getSubElementsByTagName(namePath);
		if (nodes != null && nodes.getLength() > 0)
			return ((SearchElement) nodes.item(0)).getText();
		return null;
	}


	/*
	 * Sets the given text to the Element found on the given path.
	 */
	public void setText(String namePath, String text) {
		NodeList nodes = this.getSubElementsByTagName(namePath);
		if (nodes != null && nodes.getLength() > 0)
			 ((SearchElement) nodes.item(0)).setText(text);
	}


	/*
	 * Sets the value of an attribute found on the given path.
	 */
	public void setAttr(String namePath, String value) {
		if (!namePath.matches("([^@]*)(@[^@/]*)"))
			throw new DOMException(
				DOMException.INVALID_ACCESS_ERR,
				"Parameter not supported");
		String[] keys = namePath.split(ATTR_SEPARATOR, 2);

		NodeList nodes = this.getSubElementsByTagName(keys[0]);
		if (nodes != null && nodes.getLength() > 0)
			 ((SearchElement) nodes.item(0)).setAttribute(keys[1], value);
	}


}
