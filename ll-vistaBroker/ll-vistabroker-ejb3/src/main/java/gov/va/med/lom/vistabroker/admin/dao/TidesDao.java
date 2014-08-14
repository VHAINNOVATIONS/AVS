package gov.va.med.lom.vistabroker.admin.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.admin.data.TidesDisplayOrderComparator;
import gov.va.med.lom.vistabroker.admin.data.TidesItem;
import gov.va.med.lom.vistabroker.dao.BaseDao;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TidesDao extends BaseDao{
	private static final Log log = LogFactory.getLog(TidesDao.class);
	
	public TidesDao() {
		super();
	}

	public TidesDao(BaseDao baseDao) {
		super(baseDao);
	}
    /**
     *  FUNCTION: 			getPromptOrData()
     *  RETURN:				Map<String,TidesItem>
     * 	RPC NAME: 			ALS TIDES GET PROMPTS OR DATA
	 *  RETURN VALUE TYPE: 	GLOBAL ARRAY
	 *  DESCRIPTION: 		This remote procedure returns File 691814 field prompts OR
	 *  					data for an existing entry.
	 *  INPUT PARAMETER 1: 	DFN(optional) - patient internal entry number.  If the DFN parameter is specified
	 *						the RPC will return data.  If this prompt is omitted the RPC will
	 *						return prompts only.
	 *  INPUT PARAMETER 2:	CONTACT DT(optional) - Contact date/time in FileMan (internal) format.  If specified, this
	 *						value acts as a screen on the patient lookup, returning only data
	 *						corresponding to the specified contact date.
	 *  INPUT PARAMETER 3:	FIELD(optional) - This parameter is the same as the FIELD parameter in the call to
	 *						GETS^DIQ.  If omitted the default is "**" meaning all fields and
	 *						subfields.
	 *  INPUT PARAMETER 4:	FLAGS(optional) - This parameter is the same as the FLAGS parameter in the call to
	 *						GETS^DIQ.  If omitted the default is "EIN" meaning internal AND
	 *						external values, omit null-valued fields.                      
	 *	 
	 *  RETURN PARAMETER:	
	 *   If an error, subscript 1 will have -1^Error message.  Otherwise,if the RPC is called with a
	 *   patient ID, data for that patient will be returned.
	 *   Each odd subscript will have: file/subfile#^IENS^field#^E/I and
	 *   each even subscript will have the corresponding data value.
	 *   Subscript
	 *      (I-odd)=file/subfile#^IENS^field#^E/I
	 *      (I-evn)=corresponding data value
	 *      
	 *   If no input parameters are included in the call, the return will consist of prompts 
	 *   only, in the format:
	 * 	 Prompt sequence order^Prompt text^code^sub-dic#^field#^choices
	 * 	 where,code m=simple multiple (#.01 only), M=multiple with subfields to follow
	 * 	 w=word processing, s=set with choices, d=date(/time), f=free text
	 * 	 P=pointer (followed by target file#)
	 *	 choices=value;value;... where number of acceptable values is small,
	 *	 i.e. data type is set-of-codes.
     */
 	public SortedMap<String,TidesItem> getPromptOrData(	 String dfn,
														 String contactDT,
														 String field,
														 String flags) throws Exception {
 		setDefaultContext("ALS CLINICAL RPC");
 		setDefaultRpcName("ALS TIDES GET PROMPTS OR DATA");
 		//setDefaultRpcName("TEST TIDES GET PROMPTS OR DATA");
 		Object[] params = {dfn,contactDT,field,flags};
 		List<String> results = lCall(params);
 		SortedMap<String,TidesItem> questions = new TreeMap<String,TidesItem>(new TidesDisplayOrderComparator());
 		String header = "";
 		for(String s : results){
 		 TidesItem item = new TidesItem(s);		 
 		 if( 0 != item.getDisplayOrder().compareToIgnoreCase("0") ){
 			 String[] displayOrder = item.getDisplayOrder().split("\\.");
			 item.setHeader(header);
			 if( (2 == displayOrder.length) && (true == questions.containsKey(displayOrder[0])) ){
				 TidesItem parentItem = questions.get(displayOrder[0]);
				 TidesItem childItem = parentItem;
				 while( null != childItem.getParentItem()){
					 childItem = childItem.getParentItem();
				 }
				 if( null != parentItem ){
					 //modify the display order so we overwrite the parent question in the hash
					 item.setDisplayOrder(displayOrder[0]);
					 //save the parent question within the child object
					 childItem.setParentItem(item);
					 if( 0 == parentItem.getDataType().compareTo("m")){
						 //Generate HTML (this assumes all 'm' question have s/p subquestions
						 parentItem.setFormElement(CreateFormObjectCheckBoxes(parentItem));
					 }else if( 0 == parentItem.getDataType().compareTo("M")){
						 parentItem.setFormElement(CreateFormObjectMULTIPLE(parentItem));
					 }else if( 0 == parentItem.getDataType().compareToIgnoreCase("s") &&
							   0 == item.getDataType().compareToIgnoreCase("w")){
						 parentItem.setFormElement(CreateFormObjectBasic(item));
					 }
					 item.setDisplayOrder(displayOrder[1]);
				 }
			 }else{
	 			 if( 0 == item.getDataType().compareToIgnoreCase("f")){
	 				 item.setFormElement(CreateFormObjectBasic(item));
	 			 }else if( 0 == item.getDataType().compareToIgnoreCase("d")){
	 				 item.setFormElement(CreateFormObjectBasic(item));
	 			 }else if( 0 == item.getDataType().compareToIgnoreCase("s")){
					 item.setFormElement(CreateFormObjectDropDowns(item));
	 			 }else if( 0 == item.getDataType().compareToIgnoreCase("n")){
	 				 item.setFormElement(CreateFormObjectDropDowns(item));
	 			 }else if( 0 == item.getDataType().compareToIgnoreCase("r")){
	 				 item.setFormElement(CreateRadioButtons(item));
	 		 	 }else if( 0 == item.getDataType().compareToIgnoreCase("w")){
	 		 		item.setFormElement(CreateFormObjectBasic(item));
	 			 }else if( 0 == item.getDataType().compareTo("m")){
	 				//Create Element for child only
	 			 }else if( 0 == item.getDataType().compareTo("M")){
	 				//Create Element for child only
	 			 }else{
	 				String p = item.getDataType().substring(0,1);
	 				if( 0 == p.compareToIgnoreCase("p")){
	 					item.setFormElement(CreateFormObjectDropDowns(item));
	 				}
	 			 }
	 			questions.put(item.getDisplayOrder(), item); 
			 }
 		 }else{
 			if( 0 == item.getDataType().compareToIgnoreCase("h")){
 				header = item.getTextPrompt();
 			}else if( 0 == item.getDataType().compareToIgnoreCase("d")){
				//Ignore these for now but we may need them later.
 			}
 		 }
 		}
        return questions;
  	}
    /**
     * 	RPC NAME:			ALS TIDES FILE DATA
	 *  RETURN VALUE TYPE: 	SINGLE VALUE
	 *  DESCRIPTION: 		File data to top-level and multiple fields in File 691814 --
	 *  					all fields except word-processing.
	 *  INPUT PARAMETER 1: 	LIST -Subscripted list of data to be filed, in the format:
	 *  					LIST(I)=null or sub-dictionary#^field#^reserved^value
	 *  					A value is required for the #.01 field (patient DFN).  This value
	 *  					may refer to an existing or new (to-be-created) entry in File 691814.
	 *  INPUT PARAMETER 2:	FLAGS(optional) -  Analogous to the FLAGS parameter in FILE^ and UPDATE^DIE.  If not 
	 *  					specified, the default is INTERNAL format.                     
	 *	 
	 *  RETURN PARAMETER:	
	 *   If successful, returns the File 691814 IEN that was created or edited.
	 *   If error, -1^Error message.
     */
    public String setFileData( 	List<String> data,
    							String flags ) throws Exception {
    	setDefaultContext("ALS CLINICAL RPC");
    	setDefaultRpcName("ALS TIDES FILE DATA");
		Object[] params = {data,flags};
		String results = sCall(params); 
 		return results;
 	}
    /**
     * 	RPC NAME:			ALS TIDES FILE WORD PROCESSING
	 *  RETURN VALUE TYPE: 	SINGLE VALUE
	 *  DESCRIPTION: 		Files word-processing data to an existing entry in File 691814.
	 *  INPUT PARAMETER 1: 	IEN - File 691814 internal entry number.
	 *  INPUT PARAMETER 2:	FIELD NUMBER - Field number of word-processing field to which data should be filed.
	 *  					Note that this is not the sub-dictionary number.      
	 *	INPUT PARAMETER 3:	DATA - Word processing data to file to the specified field.  Data will
 	 *						replace any data previously filed to the same field. Max Length 245
	 *  RETURN PARAMETER:	
	 *   0 for success or -1^Error message.
     */
    public String setWordProcessingData( String ien,
    									 String fieldNumber,
    									 List<String> data ) throws Exception {
    	setDefaultContext("ALS CLINICAL RPC");
    	//setDefaultRpcName("ALS TIDES FILE WORD PROCESSING");
    	//Object[] params = {ien,fieldNumber,data};
    	setDefaultRpcName("ALS TIDES MULTI-WP FILER");
		Object[] params = {ien,data};
		String results = sCall(params); 
 		return results;
 	}
    /**
     * 	RPC NAME:			ALS TIDES LIST TARGET VALUES
	 *  RETURN VALUE TYPE: 	GLOBAL ARRAY
	 *  DESCRIPTION: 		Wraps LIST^DIC to return values from pointed-to files, i.e. allowed
	 *  					values for a pointer field in File 691814.  These values may be used
	 *  					to populate a selection box in the GUI.
	 *  INPUT PARAMETER 1: 	SHORT FORM(optional) - If used this parameter should be set equal to 1, to request a short
	 *  					(abbreviated) form of the return, i.e. target values only.
	 *  INPUT PARAMETER 2:	FILE - File number of the target file.  This is equivalent to the first
	 *  					parameter in the LIST^DIC call.  Subsequent RPC parameters correspond
	 *  					to other FILE^DIC parameters in the same order: RPC parameter N =
	 *  					FILE^DIC parameter N-1.    
	 *	INPUT PARAMETER 3:	IENS(optional) - Corresponds to IENS parameter in FILE^DIC call - NOT REQUIRED.
 	 *	INPUT PARAMETER 4:	FIELDS(optional) - Corresponds to FIELDS parameter in LIST^DIC call - NOT REQUIRED.
 	 *	INPUT PARAMETER 5:	FLAGS(optional) - Corresponds to the FLAGS parameter in LIST^DIC call - NOT REQUIRED.
 	 *	INPUT PARAMETER 6:	NUMBER(optional) - Number of results to return.  Corresponds to the same parameter in LIST^DIC - NOT REQUIRED.
 	 *	INPUT PARAMETER 7:	FROM(optional) - Corresponds to FROM in LIST^DIC - NOT REQUIRED.
 	 *	INPUT PARAMETER 8:	PART(optional) - Corresponds to PART in LIST^DIC - NOT REQUIRED.
 	 *	INPUT PARAMETER 9:	INDEX(optional) - Corresponds to INDEX in LIST^DIC - NOT REQUIRED.
 	 *	INPUT PARAMETER 10:	SCREEN(optional) - Corresponds to SCREEN in LIST^DIC - NOT REQUIRED.
 	 *	INPUT PARAMETER 11:	IDENTIFIER(optional) - Corresponds to IDENTIFIER parameter in LIST^DIC - NOT REQUIRED.
	 *  RETURN PARAMETER:	
	 *    If short form is specified, only entry number^value pairs are
	 *    returned, in sequential order.  For example, File=691814.5, SHORT FORM=1:
	 *      (1)="1^Patient will be discussed with mental health specialist."
	 *      (2)="2^Mental Health appointment is being made."
	 *      (3)="3^Suicidal ideation will continue to be monitored."
	 *      (4)="4^Care Manager discussed with patient not to harm self,
	 *             assessed and reinforced social support, persons to contact & what
	 *             patient has done in the past to feel better or resist suicide."
	 *             
	 *	 If short form is not specified, the entire results from LIST^DIC are returned, odd
	 *	 subscripts having the "DILIST" subscript list, and even subscripts having the
	 *	 corresponding value.  It is expected that SHORT FORM will be specified routinely.\
	 *
	 *	 If an error occurs, the first subscript will have -1^Error message.
	 *	 If the error occurred within LIST^DIC, subscripts 2, 3, 4, ... will have additional details.
     */
     public List<String> getTargetValues(   String shortForm,
    									 	String file,
    									 	String iens,
    									 	String fields,
    									 	String flags,
    									 	String number,
    									 	String from,
    									 	String part,
    									 	String index,
    									 	String screen,
    									 	String indextifier) throws Exception {
    	setDefaultContext("ALS CLINICAL RPC");
    	setDefaultRpcName("ALS TIDES LIST TARGET VALUES");
        Object[] params = {shortForm,file,iens,fields,flags,number,from,part,index,screen,indextifier};
        List<String> results = null;
        try{
         results = lCall(params);
 		}catch (Exception e) {
			log.error("ALS TIDES LIST TARGET VALUES failed with input of: " + file);
		}
       return results;
 	 }

     /**
      * HTML Generation Functions
      *
      */     
     private String CreateFormObjectBasic(TidesItem item) throws Exception{
    	 StringBuffer element = new StringBuffer();
    	 element.append("<formElement>");
    	 element.append("<displayOrder>" + item.getDisplayOrder() + "</displayOrder>");
    	 element.append("<dataType>" + item.getDataType() + "</dataType>");
    	 element.append("<textPrompt>" + item.getTextPrompt() + "</textPrompt>");
    	 element.append("</formElement>");
    	 return element.toString();
     }
     private String CreateFormObjectDropDowns(TidesItem item) throws Exception{
    	 StringBuffer element = new StringBuffer();
    	 element.append("<formElement>");
    	 element.append("<displayOrder>" + item.getDisplayOrder() + "</displayOrder>");
    	 element.append("<textPrompt>" + item.getTextPrompt() + "</textPrompt>");
 
    	 if( 0 == item.getDataType().compareToIgnoreCase("s")){
    		 element.append("<dataType>" + item.getDataType() + "</dataType>");
    		 item.setAnswerList(ParseOptionsS(item.getSetOfCodes()));
    		 Iterator<?> itr = item.getAnswerList().keySet().iterator();
    		 while(itr.hasNext()){
    			 String value = itr.next().toString();
    			 String name = StringUtils.escapeEntities(item.getAnswerList().get(value).toString());
    			 element.append("<answer><name>" + name + "</name><value>" + value + "</value></answer>"); 
    		 }
    	 }else if( 0 == item.getDataType().compareToIgnoreCase("n")){
    		element.append("<dataType>" + item.getDataType() + "</dataType>");
    		String[] optionsArray = item.getSetOfCodes().split(":");
    		if( 2 <= optionsArray.length ){
    			int start = new Integer(optionsArray[0]).intValue();
    			int stop = new Integer(optionsArray[1]).intValue();
    			for( int i = start; i <= stop; i++ )
    				element.append("<answer><name>" + i + "</name><value>" + i + "</value></answer>");  
    		}
    	 }else{
    		String p = item.getDataType().substring(0,1);
			if( 0 == p.compareToIgnoreCase("p")){
				element.append("<dataType>s</dataType>");
				List<String> optionsList = GetTargetValues(item.getDataType());
				item.setAnswerList(ParseOptionsP(optionsList));
				Iterator<?> itr = item.getAnswerList().keySet().iterator();
	    		 while(itr.hasNext()){
	    			 String value = itr.next().toString();
	    			 String name = StringUtils.escapeEntities(item.getAnswerList().get(value).toString());
	    			 element.append("<answer><name>" + name + "</name><value>" + value + "</value></answer>");  
	    		 }
			}
    	 }    	 
    	
    	 element.append("</formElement>");
    	 return element.toString();   	 
     }
     private String CreateFormObjectCheckBoxes(TidesItem item) throws Exception{
    	 StringBuffer element = new StringBuffer();
    	 element.append("<formElement>");
    	 element.append("<displayOrder>" + item.getDisplayOrder() + "</displayOrder>");
    	 element.append("<textPrompt>" + item.getTextPrompt() + "</textPrompt>");
    	 TidesItem childItem = item.getParentItem();
    	 Iterator<?> itr = null;
    	 if( null != childItem ){
        	 if( 0 == childItem.getDataType().compareToIgnoreCase("s")){
        		 element.append("<dataType>" + item.getDataType() + "</dataType>");
        		 childItem.setAnswerList(ParseOptionsS(childItem.getSetOfCodes()));
        	 }else{//P dataType
     			String file = childItem.getDataType().substring(1, childItem.getDataType().length());
     			if( 0 == file.compareToIgnoreCase("200")){
     				element.append("<dataType>200</dataType>");
   			 	}else{
   			 		element.append("<dataType>" + item.getDataType() + "</dataType>");
   			 	}
    			List<String> optionsList = GetTargetValues(childItem.getDataType());
    			childItem.setAnswerList(ParseOptionsP(optionsList));  
        	 }

        	 if( null != childItem.getAnswerList() )
        		 itr = childItem.getAnswerList().keySet().iterator();

        	 if( null != itr ){
    	   		 while(itr.hasNext()){
    	   			 String value = itr.next().toString();
    	   			 String name = childItem.getAnswerList().get(value).toString();
    	   	    	 element.append("<answer><id>" + item.getDisplayOrder() + "</id><name>" + StringUtils.escapeEntities(name) + "</name><value>" + StringUtils.escapeEntities(value) + "</value></answer>");
    	   		 }
        	 } 
    	 }
    	 element.append("</formElement>");
    	 return element.toString();   	 
     }
     private String CreateFormObjectMULTIPLE(TidesItem item) throws Exception{
    	 StringBuffer element = new StringBuffer();
    	 element.append("<formElement>");
    	 element.append("<displayOrder>" + item.getDisplayOrder() + "</displayOrder>");
    	 element.append("<dataType>" + item.getDataType() + "</dataType>");
    	 element.append("<textPrompt>" + item.getTextPrompt() + "</textPrompt>");
    	 TidesItem child = item.getParentItem();
    	 while( null != child ){
    		 element.append("<answer>");
    		 element.append("<name>" + child.getDataType() +"</name>");
    		 if( 0 == child.getDataType().compareToIgnoreCase("f")){
        		 element.append("<value>");
        		 element.append("<textPrompt>" + child.getTextPrompt() + "</textPrompt>");
        		 element.append("<displayOrder>" + item.getDisplayOrder() + "</displayOrder>");
        		 element.append("</value>");    			 
    		 }else if( 0 == child.getDataType().compareToIgnoreCase("s")){
        		 element.append("<value>");
        		 element.append("<textPrompt>" + child.getTextPrompt() + "</textPrompt>");
        		 element.append("<displayOrder>" + item.getDisplayOrder() + "</displayOrder>");
        		 child.setAnswerList(ParseOptionsS(child.getSetOfCodes()));
        		 Iterator<?> itr = null;
        		 if( null != child.getAnswerList() )
            		 itr = child.getAnswerList().keySet().iterator();
            	 if( null != itr ){
        	   		 while(itr.hasNext()){
        	   			 String value = itr.next().toString();
        	   			 String name = child.getAnswerList().get(value).toString();
        	   	    	 element.append("<answer><name>" + StringUtils.escapeEntities(name) + "</name><value>" + StringUtils.escapeEntities(value) + "</value></answer>");
        	   		 }
            	 } 
        		 element.append("</value>");   
    		 }
    		 element.append("</answer>");
    		 child = child.getParentItem();
    	 }
    	 element.append("</formElement>");
    	 return element.toString();   	 
     }       
     private String CreateRadioButtons(TidesItem item) throws Exception{
    	 StringBuffer element = new StringBuffer();
    	 element.append("<formElement>");
    	 element.append("<displayOrder>" + item.getDisplayOrder() + "</displayOrder>");
    	 element.append("<textPrompt>" + item.getTextPrompt() + "</textPrompt>");
 
		 element.append("<dataType>" + item.getDataType() + "</dataType>");
		 item.setAnswerList(ParseOptionsS(item.getSetOfCodes()));
		 Iterator<?> itr = item.getAnswerList().keySet().iterator();
		 while(itr.hasNext()){
			 String value = itr.next().toString();
			 String name = StringUtils.escapeEntities(item.getAnswerList().get(value).toString());
			 element.append("<answer><name>" + name + "</name><value>" + value + "</value></answer>"); 
		 }
    	
    	 element.append("</formElement>");
    	 return element.toString();   	 
     }
     /**
      * Utility Functions
      */
     private SortedMap<String,String> ParseOptionsS(String options) throws Exception{
     	SortedMap<String,String> optionsList = new TreeMap<String,String>(new TidesDisplayOrderComparator());
      	try{
    		String[] optionsArray = options.split(";");
 	    	for( String o : optionsArray ){
 	    	 String[] stripped = o.split(":");
      		 if( null != stripped && 2 <= stripped.length)
 	    	  optionsList.put(stripped[0],stripped[1]);
 	    	} 
      	}catch (Exception e) {
 			// TODO: handle exception
 		}
      	return optionsList;
      }
      private SortedMap<String,String> ParseOptionsP( List<String> options) throws Exception{
     	SortedMap<String,String> optionsList = new TreeMap<String,String>(new TidesDisplayOrderComparator());
     	try{
     		Iterator<String> itr = options.iterator();
 	    	while(itr.hasNext()){
 	    		String option = itr.next();
 	    		String[] stripped = option.split("\\^");
 	    		if( null != stripped && 2 <= stripped.length){
 	    			optionsList.put(stripped[0],stripped[1]);
 	    		}else{
 	    			optionsList.put(option,option);
 	    		}
 	    	}
     	}catch (Exception e) {
 			// TODO: handle exception
 		}
     	return optionsList;    	 
      }
     
     private List<String> GetTargetValues( String pointer ) throws Exception{
    	 String file = pointer.substring(1, pointer.length());
		 if( 0 != file.compareToIgnoreCase("200")){
			 return getTargetValues("1", file, "", "", "", "", "", "", "", "", "");
		 }
		 return null;
     }

}


