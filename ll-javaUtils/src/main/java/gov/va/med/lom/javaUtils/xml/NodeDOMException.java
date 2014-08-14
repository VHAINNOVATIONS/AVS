package gov.va.med.lom.javaUtils.xml;

import org.w3c.dom.DOMException;


/*
 * A class representing a node in a meta-data tree, 
 * which extends DOMException class
 */
public class NodeDOMException extends DOMException {
	public NodeDOMException(short code, String message) {
		super(code, message);
	}
}
