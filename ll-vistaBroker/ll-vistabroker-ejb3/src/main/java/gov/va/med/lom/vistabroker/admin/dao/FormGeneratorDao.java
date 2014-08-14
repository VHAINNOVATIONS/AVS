package gov.va.med.lom.vistabroker.admin.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.admin.data.GenFormData;
import gov.va.med.lom.vistabroker.admin.data.GenFormListItem;
import gov.va.med.lom.vistabroker.admin.data.GenFormListParams;
import gov.va.med.lom.vistabroker.dao.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class FormGeneratorDao extends BaseDao{

  // CONSTRUCTORS
  public FormGeneratorDao() {
    super();
  }
  
  public FormGeneratorDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  /*
   * Files data to multiple files and fields, as specified in the input array.
   *  
   * INPUT PARAMETER: LIST                   PARAMETER TYPE: LIST
   *
   * Array of specifications for data to be filed.  Subscripts are numbered 1,
   * 2, 3, ...  Each node has the following form:
   *
   * LIST(I)= FileIen^EntryIen^FieldNumber^DataType^Data^DINUM_IEN^FLAGS
   *
   * If EntryIen is not valued, a new entry will be created.  If a new entry
   * is created and DINUM_IEN is valued, the entry will be created at the
   * internal entry number specified by DINUM_IEN.
   * 
   * If Data is equal to "@", data from that field.
   *
   * If Data for FieldNumber #.01 is equal to "@" (or NULL), the entry will be
   *
   * Enter RETURN to continue or '^' to exit:
   * deleted by ^DIE, respecting "no-delete" checks.
   * 
   * If EntryIen is valued and FieldNumber is "@" the entry will be deleted
   * through ^DIK (bypassing no-delete checks).
   * 
   * FLAGS refers to the corresponding parameter of UPDATE^ or FILE^DIE.  Note
   * that this field is checked only when the FileIEN or EntryIEN changes
   * value.  In other words, FLAGS refers to ALL values filed for a given
   * FileIEN x EntryIEN combination.
   * 
   * RETURN PARAMETER DESCRIPTION:
   * If successful, returns subscripted array of entry IENs that were filed.
   *
   * ALSRSLT(I)= FileNumber^EntryIen^[optional]Message
   *
   * If an error occurs, the current results array node has -1^Error message.
   * Note that some specifications may have been successfully processed before
   * an error occurs.  However, processing stops at the point of the error and
   * no further specifications are processed.
   *
   */
  public List<GenFormData> putData(List<GenFormData> genFormDataList) throws Exception {
    setDefaultContext("ALS CLINICAL RPC");
    setDefaultRpcName("ALS GENFORM PUT DATA");
    List<String> list = new ArrayList<String>();
    for (GenFormData data : genFormDataList) {
      StringBuffer sb = new StringBuffer();
      sb.append(data.getFileIen());
      sb.append("^");
      if (data.getEntryIen() != null)
        sb.append(data.getEntryIen());
      sb.append("^");
      sb.append(data.getFieldIen());
      sb.append("^");
      if (data.getDataType() != null)
        sb.append(data.getDataType());
      sb.append("^");
      if (data.getIntFormat() != null)
        sb.append(data.getIntFormat());
      else
        sb.append(data.getExtFormat());
      sb.append("^");
      if (data.getDiNumIen() != null) 
        sb.append(data.getDiNumIen());
      sb.append("^");
      if (data.getFlags() != null)
        sb.append(data.getFlags());
      list.add(sb.toString());
    }
    List<String> x = lCall(list);
    List<GenFormData> dataList = new ArrayList<GenFormData>();
    for (String s : x) {
      GenFormData data = new GenFormData();
      String p1 = StringUtils.piece(s, 1);
      data.setFileIen(p1);
      if (!p1.equals("-1"))
        data.setEntryIen(StringUtils.piece(s, 2));
      else
        data.setMessage(StringUtils.piece(s, 2));
      dataList.add(data);
    }
    return dataList;    
  }
  
  /*
   * Files word-processing data to multiple files and fields as specified in
   * input array.
   * 
   * INPUT PARAMETER: LIST                   PARAMETER TYPE: LIST
   * The input array specifies where to file word processing data, as well as
   * the word processing text itself.  Subscripts are sequential numbers 1, 2,
   * 3, ...  Each block of text is bounded by $START and $END tags that
   * specify the target file, field, and IEN.  The format is as follows:
   *  
   *         ...
   *         LIST(I)     = "$START FileIEN^EntryIEN^FieldIEN"
   *         ...
   *         ...
   *         LIST(I+x)   = "text" where 'x' means the xth line of n lines
   *         ...
   *         ...
   *         LIST(I+n+1) = "$END FileIEN^EntryIEN^fieldIEN"
   *         ...
   *          
   * RETURN PARAMETER DESCRIPTION:
   * If all specifications were processed successfully, returns 0.  If an
   * error occurred, returns -1^Error message.
   *
   */
  public boolean putWpData(List<GenFormData> genFormDataList) throws Exception {
    setDefaultContext("ALS CLINICAL RPC");
    setDefaultRpcName("ALS GENFORM PUT WPS");
    List<String> list = new ArrayList<String>();
    for (GenFormData data : genFormDataList) {
      StringBuffer sb = new StringBuffer();
      sb.append("$START ");
      sb.append(data.getFileIen());
      sb.append("^");
      sb.append(data.getEntryIen());
      sb.append("^");
      sb.append(data.getFieldIen());
      list.add(sb.toString());
      String[] lines = null;
      if (data.getIntFormat() != null)
        lines = StringUtils.pieceList(data.getIntFormat(), '\n');
      else
        lines = StringUtils.pieceList(data.getExtFormat(), '\n');
      for (int i = 0; i < lines.length; i++) {
        list.add(lines[i]);
      }
      sb.append("$END ");
      sb.append(data.getFileIen());
      sb.append("^");
      sb.append(data.getEntryIen());
      sb.append("^");
      sb.append(data.getFieldIen());
      list.add(sb.toString());      
    }
    String x = sCall(list);
    if (StringUtils.piece(x, 1).equals("0"))
      return true;
    else
      throw new Exception(StringUtils.piece(x, 2));
  }
  
  /*
   * Retrieve data from multiple files and fields, as specified in input
   * array.  Include field type for each field, and both internal and external
   * data values, when different.
   * 
   * INPUT PARAMETER: LIST                   PARAMETER TYPE: LIST
   * Array of specifications for data to be retrieved.  Subscripts are
   * numbered 1, 2, 3, ...  Each node has the following format:
   * 
   *  LIST(I)= ID^FileNumber^EntryIen^FieldNumber^DataType
   *
   * ID is a handle that is meaningful to the calling program.  It is returned
   * with the results.  DataType is optional and is currently ignored.
   * 
   * RETURN PARAMETER DESCRIPTION:
   * Each node of the global array returns data in the following format:
   *
   *  @ALSRSLT@(I)= ID^FileNumber^EntryIen^FieldNumber^DataType^Int^Ext
   *
   * where DataType refers to the actual type of the field, Int is the
   * internal form of the data and Ext is the external form.  Note that if
   * internal and external are the same, such as in free-text data, the
   * external form is omitted from the return.
   *
   * ID is a handle that was sent by the caller and returned with the
   * corresponding result.
   * 
   * If an error occurs, the result node will have -1^Error message.  Note
   * that valid results may be returned before an error occurs.  However, when
   * an error is detected, processing stops at that point and no further
   * results are returned.
   *
   */  
  public List<GenFormData> getData(List<GenFormData> genFormDataList) throws Exception {
    setDefaultContext("ALS CLINICAL RPC");
    setDefaultRpcName("ALS GENFORM GET DATA");
    List<String> list = new ArrayList<String>();
    for (GenFormData data : genFormDataList) {
      StringBuffer sb = new StringBuffer();
      if (data.getId() != null)
        sb.append(data.getId());
      sb.append("^");
      sb.append(data.getFileIen());
      sb.append("^");
      sb.append(data.getEntryIen());
      sb.append("^");
      sb.append(data.getFieldIen());
      sb.append("^");
      if (data.getDataType() != null)
        sb.append(data.getDataType());
      list.add(sb.toString());
    }
    List<String> results = lCall(list);
    List<GenFormData> dataList = new ArrayList<GenFormData>();
    for (String s : results) {
      GenFormData data = new GenFormData();
      String p1 = StringUtils.piece(s, 1);
      if (p1.equals("-1"))
        throw new Exception(StringUtils.piece(s, 2));
      data.setId(StringUtils.piece(s, 1));
      data.setFileIen(StringUtils.piece(s, 2));
      data.setEntryIen(StringUtils.piece(s, 3));
      data.setFieldNumber(StringUtils.piece(s, 4));
      data.setDataType(StringUtils.piece(s, 5));
      data.setIntFormat(StringUtils.piece(s, 6));
      data.setExtFormat(StringUtils.piece(s, 7));
      dataList.add(data);
    }
    return dataList;    
  }
   
  /*
   * Wraps LIST^DIC to return values matching input specifications.
   * 
   * INPUT PARAMETER: SHORT FORM FLAG        PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 2                REQUIRED: NO
   *   SEQUENCE NUMBER: 1
   *  DESCRIPTION:
   *  [Optional] If used this parameter should be set equal to 1, to request a
   *  short (abbreviated) form of the return, i.e. target values only.
   * INPUT PARAMETER: FILE NUMBER            PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 20               REQUIRED: YES
   *   SEQUENCE NUMBER: 2
   *  DESCRIPTION:
   *  [Required] File number of the target file.  This is equivalent to the
   *  first parameter in the LIST^DIC call.  Subsequent RPC parameters
   *  correspond to other FILE^DIC parameters in the same order: RPC parameter N
   *  = FILE^DIC parameter N-1.
   * INPUT PARAMETER: IENS                   PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 20               REQUIRED: NO
   *   SEQUENCE NUMBER: 3
   *  DESCRIPTION:
   *  [Optional] Corresponds to IENS parameter in FILE^DIC call.
   * INPUT PARAMETER: FIELDS                 PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 245              REQUIRED: NO
   *   SEQUENCE NUMBER: 4
   *  DESCRIPTION:
   *  [Optional] Corresponds to FIELDS parameter in LIST^DIC call.
   * INPUT PARAMETER: FLAGS                  PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 8                REQUIRED: NO
   *   SEQUENCE NUMBER: 5
   *  DESCRIPTION:
   *  [Optional] Corresponds to the FLAGS parameter in LIST^DIC call.
   * INPUT PARAMETER: NUMBER OF RESULTS      PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 8                REQUIRED: NO
   *   SEQUENCE NUMBER: 6
   *  DESCRIPTION:
   *  [Optional] Number of results to return.  Corresponds to the same parameter
   *  in LIST^DIC call.
   * INPUT PARAMETER: FROM                   PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 45               REQUIRED: NO
   *   SEQUENCE NUMBER: 7
   *  DESCRIPTION:
   * FROM=[Optional] Corresponds to FROM in LIST^DIC call.
   * INPUT PARAMETER: PART                   PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 45               REQUIRED: NO
   *   SEQUENCE NUMBER: 8
   *  DESCRIPTION:
   *  [Optional] Corresponds to PART in LIST^DIC call.
   * INPUT PARAMETER: INDEX                  PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 30               REQUIRED: NO
   *   SEQUENCE NUMBER: 9
   *  DESCRIPTION:
   *  [Optional] Corresponds to INDEX in LIST^DIC call.
   * INPUT PARAMETER: SCREEN                 PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 245              REQUIRED: NO
   *   SEQUENCE NUMBER: 10
   *  DESCRIPTION:
   *  [Optional] Corresponds to SCREEN in LIST^DIC call.
   * INPUT PARAMETER: IDENTIFIER             PARAMETER TYPE: LITERAL
   *   MAXIMUM DATA LENGTH: 45               REQUIRED: NO
   *   SEQUENCE NUMBER: 11
   *  DESCRIPTION:
   *  [Optional] Corresponds to IDENTIFIER parameter in LIST^DIC call.
   *
   * RETURN PARAMETER DESCRIPTION:
   * If short form is specified, only entry number^value pairs are returned.
   * For example, File=691814.5, SHORT FORM=1:
   * 
   *        (1)= "1^Patient will be discussed with mental health specialist."
   *        (2)= "2^Mental Health appointment is being made."
   *        (3)= "3^Suicidal ideation will continue to be monitored."
   *        (4)= "4^Care Manager discussed with patient not to harm self"
   * 
   * If short form is not specified, the entire results from LIST^DIC are
   * returned, odd subscripts having the "DILIST" subscript list, and even
   * subscripts having the corresponding value.  It is expected that SHORT FORM
   * will be specified routinely.
   * 
   * If an error occurs, the first subscript will have -1^Error message.
   * If an error occurs within LIST^DIC, subscripts 2, 3, 4, ... will have
   * additional details.
   *
   */
  public List<GenFormListItem> getListValues(GenFormListParams listParams) throws Exception {
    setDefaultContext("ALS CLINICAL RPC");
    setDefaultRpcName("ALS GENFORM LIST VALUES");
    if (listParams.getIens() == null)
      listParams.setIens("");
    if (listParams.getFields() == null)
      listParams.setFields("");
    if (listParams.getFlags() == null)
      listParams.setFlags("");
    if (listParams.getNumResults() <= 0)
      listParams.setNumResults(100);
    if (listParams.getFrom() == null)
      listParams.setFrom("");
    if (listParams.getPart() == null)
      listParams.setPart("");
    if (listParams.getIndex() == null)
      listParams.setIndex("");
    if (listParams.getScreen() == null)
      listParams.setScreen("");
    if (listParams.getIdentifier() == null)
      listParams.setIdentifier("");
    Object[] rpcParams = new Object[] {StringUtils.boolToStr(listParams.isShortFormFlag(), "1", "0"),
                                       listParams.getFileIen(), listParams.getIens(), listParams.getFields(),
                                       listParams.getFlags(), listParams.getNumResults(), listParams.getFrom(),
                                       listParams.getPart(), listParams.getIndex(), listParams.getScreen(),
                                       listParams.getIdentifier()};
    List<String> results = lCall(rpcParams);
    List<GenFormListItem> listItems = new ArrayList<GenFormListItem>();
    int i = 0;
    for (String s : results) {
      if ((i == 0) && (StringUtils.piece(s, 1).equals("-1")))
        throw new Exception(StringUtils.piece(s, 2));
      GenFormListItem item = new GenFormListItem();
      item.setIen(StringUtils.piece(s, 1));
      item.setValue(StringUtils.piece(s, 2));
      item.setRpcResult(s);
      listItems.add(item);
    }
    return listItems;    
  }  
   
}
