package gov.va.med.lom.javaUtils.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

/*
 * A class representing a node in a meta-data tree, which implements
 * the org.w3c.dom.Attr
 *
 * Namespaces are ignored in this implementation.  The terms "tag
 * name" and "node name" are always considered to be synonymous.
 */
public class AttrImpl extends NodeImpl implements Attr {

	/*
	 * If this attribute was explicitly given a value in the original 
     * document, this is true; otherwise, it is 
     * false. 
	 */
	boolean specified = true;

	/*
	 * Document owner.
	 */
	Element owner;
	
	/*
	 * Attribute name.
	 */
	String name;
	
	/*
	 * Attribute value.
	 */
	String value;
  
  /* 
   * Is the attribute an ID?
   */
  boolean isId;

	
	/*
	 * Constructs an empty AttrImpl.
	 */
	public AttrImpl(Element owner, String name, String value) {
		this.owner = owner;
		this.name = name;
		this.value = value;
    isId = false;
	}
  
  /*
   * Constructs an empty AttrImpl with isId flag.
   */
  public AttrImpl(Element owner, String name, String value, boolean isId) {
    this.owner = owner;
    this.name = name;
    this.value = value;
    this.isId = isId;
  }  

	/*
	 * Constructs a AttrImpl from the given node.
	 */
	public AttrImpl(Attr attr) {
		this.owner = attr.getOwnerElement();
		this.name = attr.getName();
		this.value = attr.getValue();
	}

	/*
	 * Returns the attribute name associated with this node.
	 */
	public String getName() {
		return name;
	}

	/*
	 * Returns the name associated with this node.
	 */
	public String getNodeName() {
		return name;
	}

	/*
	 * Returns the node type.
	 */
	public short getNodeType() {
		return ATTRIBUTE_NODE;
	}


	/*
     * If this attribute was explicitly given a value in the original 
     * document, this is true; otherwise, it is 
     * false. Note that the implementation is in charge of this 
     * attribute, not the user. If the user changes the value of the 
     * attribute (even if it ends up having the same value as the default 
     * value) then the specified flag is automatically flipped 
     * to true. To re-specify the attribute as the default 
     * value from the DTD, the user must delete the attribute. The 
     * implementation will then make a new attribute available with 
     * specified set to false and the default 
     * value (if one exists).
     * <br>In summary: If the attribute has an assigned value in the document 
     * then specified is true, and the value is 
     * the assigned value.If the attribute has no assigned value in the 
     * document and has a default value in the DTD, then 
     * specified is false, and the value is the 
     * default value in the DTD.If the attribute has no assigned value in 
     * the document and has a value of #IMPLIED in the DTD, then the 
     * attribute does not appear in the structure model of the document.If 
     * the ownerElement attribute is null (i.e. 
     * because it was just created or was set to null by the 
     * various removal and cloning operations) specified is 
     * true. 
     * 
     * Retuns always true.
     */
	public boolean getSpecified() {
		return specified;
	}

	/*
	 * Returns the value associated with this attributes.
	 */
	public String getValue() {
		return value;
	}

	/*
	 * Returns the value associated with this node.
	 */
	public String getNodeValue() {
		return value;
	}

	/*
	 * Sets the value of this attribute to the given one.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * Sets the value of this node to the given one.
	 */
	public void setNodeValue(String value) {
		this.value = value;
	}

	/*
	 * Returns the owner of this attribute.
	 */
	public Element getOwnerElement() {
		return owner;
	}
  
  /*
   * Sets the isId flag of this node to the given one.
   */
  public void setIsId(boolean isId) {
    this.isId = isId;
  }

  /*
   * Returns the isId flag of this attribute.
   */
  public boolean isId() {
    return isId;
  }  
  
  /*
   * Dummy method that implements the abstract method.
   */
  public TypeInfo getSchemaTypeInfo() {
    return null;
  }
 
}