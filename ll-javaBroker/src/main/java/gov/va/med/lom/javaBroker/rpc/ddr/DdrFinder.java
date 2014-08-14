package gov.va.med.lom.javaBroker.rpc.ddr;

import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;


import gov.va.med.lom.javaBroker.rpc.Params;
import gov.va.med.lom.javaBroker.rpc.Mult;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class DdrFinder extends DdrQuery {

  private String file;
  private String iens;
  private List<String> requestedFields;
  private Hashtable<String, String> requestedFieldsTable;
  private List<String> ienList;
  private String flags;
  private String max;
  private String value;
  private String xref;
  private String screen;
  private String id;
  private String options;
  private String root;
  
  public DdrFinder(RpcBroker rpcBroker) {
    super(rpcBroker);
  }
  
  public List<String> execute() throws Exception {
    Params params = new Params();
    Mult mult = new Mult();
    if ((file != null) && (!file.equals(""))) {
      mult.setMultiple("\"FILE\"", file);
    }
    if ((iens != null) && (!iens.equals(""))) {
      mult.setMultiple("\"IENS\"", iens);
    }
    if ((requestedFields != null) && (requestedFields.size() > 0)) {
      mult.setMultiple("\"FIELDS\"", getFieldsArg());
    }
    if ((flags != null) && (!flags.equals(""))) {
      mult.setMultiple("\"FLAGS\"", flags);
    }
    if ((max != null) && (!max.equals(""))) {
      mult.setMultiple("\"MAX\"", max);
    }
    if ((value != null) && (!value.equals(""))) {
      mult.setMultiple("\"VALUE\"", value);
    }
    if ((xref != null) && (!xref.equals(""))) {
      mult.setMultiple("\"XREF\"", xref);
    }
    if ((screen != null) && (!screen.equals(""))) {
      mult.setMultiple("\"SCREEN\"", screen);
    }
    if ((id != null) && (!id.equals(""))) {
      mult.setMultiple("\"ID\"", id);
    }
    if ((root != null) && (!root.equals(""))) {
      mult.setMultiple("\"ROOT\"", root);
    }
    if ((options != null) && (!options.equals(""))) {
      mult.setMultiple("\"OPTIONS\"", options);
    }     
    params.addMult(mult); 
    String response = super.execute("DDR FINDER", params);
    List<String> list = StringUtils.getStringList(response);
    if (flags.indexOf("P") != -1) {
      return parsePackedResult(list);
    } else {
      return packResult(list);
    }
  }
  
  public static DdrFinder buildIenNameQuery(RpcBroker rpcBroker, String fileNumber) {
    DdrFinder query = new DdrFinder(rpcBroker);
    query.setFile(fileNumber);
    query.setFields(".01");
    query.setFlags("IP");
    query.setXref("#");
    return query;   
  }
  
  public static DdrFinder buildFileQuery(RpcBroker rpcBroker, String fileNumber, String fieldString) {
    DdrFinder query = new DdrFinder(rpcBroker);
    query.setFile(fileNumber);
    query.setFields(fieldString);
    query.setFlags("IP");
    query.setXref("#");
    return query;  
  }
  
  private String getFieldsArg() {
    String result = "@";
    for (int i = 0; i < requestedFields.size(); i++) {
      if (requestedFields.get(i).equals("@")) {
        continue;
      }
      result += ';' + requestedFields.get(i);
    }
    return result;  
  }
  
  private List<String> toStringArray(Hashtable<String, Hashtable<String, DdrField>> hashedRex) {
    ArrayList<String> list = new ArrayList<String>();
    for (int recnum = 0; recnum < ienList.size(); recnum++)     {
      String s = ienList.get(recnum);
      Hashtable<String, DdrField> hashedFlds = hashedRex.get(ienList.get(recnum));
      for (int fldnum = 0; fldnum < requestedFields.size(); fldnum++) {
        String fmNum = requestedFields.get(fldnum);
        boolean external = false;
        if (fmNum.indexOf('E') != -1) {
          fmNum = fmNum.substring(0, fmNum.length() - 1);
          external = true;
        }
        DdrField fld = (DdrField)hashedFlds.get(fmNum);
        if (external) {
          s += '^' + fld.getExternalVal();
        } else {
          s += '^' + fld.getVal();
        }
      }
      list.add(s);
    }
    return list;
  }
  
  private List<String> parsePackedResult(List<String> lines) throws Exception {
    int index = -1;
    for (String str : lines) {
      index++;
      if (str.startsWith("[BEGIN_diERRORS]")) {
        throw new Exception(getErrMsg(lines, index));
      }
    }
    index = -1;
    for (String str : lines) {
      index++;
      if (str.startsWith("[BEGIN_diDATA]")) {
        List<String> lst = new ArrayList<String>();
        index++;
        while (index < lines.size() && !lines.get(index).equals("[END_diDATA]")) {
          lst.add(lines.get(index++));
        }
        return lst;
      }
    }
    throw new Exception("Error parsing packed result: expected [BEGIN_diDATA], found none.");
  }
  
  private List<String> packResult(List<String> lines) throws Exception {
    int index = 0;
    if (lines.get(index).equals("0^*^0^")) {
      return new ArrayList<String>();
    }
    Hashtable<String, Hashtable<String, DdrField>> rs = new Hashtable<String, Hashtable<String, DdrField>>();
    for (String str : lines) {
      if (str.startsWith("[BEGIN_diERRORS]")) {
        throw new Exception(getErrMsg(lines, index));
      }
      index++;
    }
    
    for (String str : lines) {
      if (str.startsWith("[BEGIN_diDATA]")) {
        ienList = new ArrayList<String>();
        if (!lines.get(++index).equals("BEGIN_IENs")) {
          throw new Exception("Incorrectly formatted return data");
        }
      }
      index++;
      while (!lines.get(index).equals("END_IENs")) {
        ienList.add(lines.get(index++));
      }
      index++;
      if (!lines.get(index).equals("BEGIN_IDVALUES")) {
        throw new Exception("Incorrectly formatted return data");
      }
      String[] flds = StringUtils.pieceList(lines.get(++index), ';');
      int recIdx = 0;
      index++;
      while (!lines.get(index).equals("END_IDVALUES")) {
        // the last field in flds is the field count, not a field
        Hashtable<String, DdrField> rec = new Hashtable<String, DdrField>();
        for (int fldIdx = 0; fldIdx < flds.length-1; fldIdx++) {
          DdrField f = new DdrField();
          f.setFmNumber(flds[fldIdx]);
          String requestedOptions = requestedFieldsTable.get(f.getFmNumber());
          f.setFExternal(requestedOptions.indexOf('E') != -1);
          if (f.isFExternal()) {
            f.setExternalVal(lines.get(index++));
          }
          if (requestedOptions.indexOf('I') != -1) {
            f.setVal(lines.get(index++));
          }
          rec.put(f.getFmNumber(), f);
        }
        rs.put((String)ienList.get(recIdx++), rec);
      }
      // at this point line should be VistaConstants.END_DATA
      // unless more functionality is added.
    }
    return toStringArray(rs);  
  }
  
  private String getErrMsg(List<String> lines, int index) {
    String msg = lines.get(index + 3);
    int endIndex = -1;
    for (String s : lines) {
      endIndex++;
      if (s.startsWith("[END_diERRORS]")) {
        break;
      }
    }
    for (int i = index + 4; i < endIndex; i++) {
      if (msg.charAt(msg.length() - 1) != '.') {
        msg += ". ";
      }
      msg += lines.get(i);
    }
    return msg;
  }  

  // Getter/Setter Methods
  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getIens() {
    return iens;
  }

  public void setIens(String iens) {
    this.iens = iens;
  }

  public List<String> getRequestedFields() {
    return requestedFields;
  }

  public void setRequestedFields(List<String> requestedFields) {
    this.requestedFields = requestedFields;
  }

  public Hashtable<String, String> getRequestedFieldsTable() {
    return requestedFieldsTable;
  }

  public void setRequestedFieldsTable(
    Hashtable<String, String> requestedFieldsTable) {
    this.requestedFieldsTable = requestedFieldsTable;
  }

  public List<String> getIenList() {
    return ienList;
  }

  public void setIenList(List<String> ienList) {
    this.ienList = ienList;
  }
  
  public void setFields(String value) {
    String arr[] = StringUtils.pieceList(value, ';');
    requestedFields = new ArrayList<String>();
    for (int i = 0; i < arr.length; i++)
      requestedFields.add(arr[i]);
    requestedFieldsTable = new Hashtable<String, String>();
    for (int i = 0; i < requestedFields.size(); i++) {
      if (requestedFields.get(i).equals("")) {
        continue;
      }
      String fldnum = requestedFields.get(i);
      String option = "I";
      if (fldnum.indexOf('E') != -1) {
        fldnum = fldnum.substring(0, fldnum.length() - 1);
        option = "E";
      }
      if (!requestedFieldsTable.containsKey(fldnum)) {
        requestedFieldsTable.put(fldnum, option);
      } else {
        requestedFieldsTable.put(fldnum,  requestedFieldsTable.get(fldnum) + option);
      }
    }    
  }

  public String getFlags() {
    return flags;
  }

  public void setFlags(String flags) {
    this.flags = flags;
  }

  public String getMax() {
    return max;
  }

  public void setMax(String max) {
    this.max = max;
  }

  public String getXref() {
    return xref;
  }

  public void setXref(String xref) {
    this.xref = xref;
  }

  public String getScreen() {
    return screen;
  }

  public void setScreen(String screen) {
    this.screen = screen;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOptions() {
    return options;
  }

  public void setOptions(String options) {
    this.options = options;
  }

  public String getRoot() {
    return root;
  }

  public void setRoot(String root) {
    this.root = root;
  }

}
