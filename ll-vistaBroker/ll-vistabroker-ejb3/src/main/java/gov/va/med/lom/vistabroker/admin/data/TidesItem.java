package gov.va.med.lom.vistabroker.admin.data;

import gov.va.med.lom.javaUtils.misc.StringUtils;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;

public class TidesItem implements Serializable{
	/**
	 * RPC Data Segments
	 */
	private String displayOrder;
	private String textPrompt;
	private String dataType;
	private String subDirectory;
	private String field;
	private String setOfCodes;
	private String rpcResult;
	/**
	 * Calculated Fields
	 */
	private String header;
	private String formElement;
	private TidesItem parentItem;
	private SortedMap<String, String> answerList;
	
	public TidesItem() {
		this.displayOrder = "";
		this.textPrompt = "";
		this.dataType = "";
		this.subDirectory = "";
		this.field = "";
		this.setOfCodes = "";
		this.rpcResult = "";
		
		this.header = "";
		this.formElement = "";
		this.parentItem = null;
		this.answerList = new TreeMap<String, String>();
	}
	public TidesItem( String s ){
		this.displayOrder = StringUtils.piece(s,1);
		this.textPrompt = StringUtils.escapeEntities(StringUtils.piece(s,2));
		this.dataType = StringUtils.piece(s,3);
		this.subDirectory = StringUtils.piece(s,4);
		this.field = StringUtils.piece(s,5);
		this.setOfCodes = StringUtils.piece(s,6);
		this.rpcResult = s;
		
		this.header = "";
		this.formElement = "";
		this.parentItem = null;
		this.answerList = new TreeMap<String, String>();
	}
	public SortedMap<String, String> getAnswerList() {
		return answerList;
	}
	public void setAnswerList(SortedMap<String, String> answerList) {
		this.answerList = answerList;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getFormElement() {
		return formElement;
	}
	public void setFormElement(String formElement) {
		this.formElement = formElement;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public TidesItem getParentItem() {
		return parentItem;
	}
	public void setParentItem(TidesItem parentItem) {
		this.parentItem = parentItem;
	}
	public String getRpcResult() {
		return rpcResult;
	}
	public void setRpcResult(String rpcResult) {
		this.rpcResult = rpcResult;
	}
	public String getSetOfCodes() {
		return setOfCodes;
	}
	public void setSetOfCodes(String setOfCodes) {
		this.setOfCodes = setOfCodes;
	}
	public String getSubDirectory() {
		return subDirectory;
	}
	public void setSubDirectory(String subDirectory) {
		this.subDirectory = subDirectory;
	}
	public String getTextPrompt() {
		return textPrompt;
	}
	public void setTextPrompt(String textPrompt) {
		this.textPrompt = textPrompt;
	}
}
