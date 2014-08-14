package gov.va.med.lom.javaUtils.config;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/*
 * Class that implements a recursive keyword/value table.  The key is a string
 * that is restricted to be a valid Java identifier.  That is, starting with
 * an letter and containing letters or digits.  The characters '_' and '$'
 * are also allowed and are treated as letters.  The value maybe any object.
 * A keyword and its value are collectively referred to as a field.
 *
 * The table is recursive. Values of class KeywordValueTable are referred to as
 * sections.  A field of a section maybe addressed from the parent
 * object using a dot ('.') separated name path.
 *
 */

public class KeywordValueTable implements java.io.Serializable {
  private Hashtable hashTable;   // Table keyed by keyword component.
    
  public KeywordValueTable() {
    hashTable = new Hashtable();
  }

  private synchronized KeywordValueTable findSection(String[] keywordPath,
                                                       boolean create,
                                                       int pathIdx) throws KeywordValueException {
    if (pathIdx == keywordPath.length - 1) {
      return this;
    }

    Object value = hashTable.get(keywordPath [pathIdx]);
    if (value != null) {
      if (!(value instanceof KeywordValueTable)) {
        if (!create) {
          String msg = "keyword specifies a non-leaf component " +
                       "that is not a KeywordValueTable: " +
                       KeywordParser.join (keywordPath) +
                       " (component #" + pathIdx + ")";
          throw new KeywordValueException(msg);
        }
        value = newSection();
        hashTable.put(keywordPath [pathIdx], value);
      }
    } else {
      if (!create) {
        return null;
      }
      value = newSection();
      hashTable.put(keywordPath [pathIdx], value);
    }
    return ((KeywordValueTable) value).findSection(keywordPath,create,pathIdx + 1);
  }
    
  protected KeywordValueTable newSection() {
    return new KeywordValueTable();
  }

  public synchronized Object get(String keyword) throws KeywordValueException {
    String [] keywordPath = KeywordParser.parse(keyword);
    KeywordValueTable section = findSection(keywordPath,false,0);
    if (section == null) {
      return null;
    }
    return section.hashTable.get(keywordPath [keywordPath.length - 1]);
  }

  public synchronized Object get(String keyword,Object defaultValue) throws KeywordValueException {
    Object value;
    String [] keywordPath = KeywordParser.parse (keyword);
    KeywordValueTable section = findSection(keywordPath,false,0);
    if (section == null) {
      value = defaultValue;
    } else {
      value = section.hashTable.get(keywordPath [keywordPath.length - 1]);
      if (value == null) {
        value = defaultValue;
      }
    }
    return value;
  }

  public synchronized String getString(String keyword) throws KeywordValueException {
    Object value = get(keyword);
    if (value == null) {
      return null;
    }
    return value.toString();
  }

  public synchronized String getString(String keyword,String defaultValue) throws KeywordValueException {
    Object value = get(keyword);
    if (value == null) {
      return defaultValue;
    }
    return value.toString();
  }

  public synchronized KeywordValueTable getSection(String keyword) throws KeywordValueException {
    Object value = get(keyword);
    if (value == null) {
      return null;
    }
    if (!(value instanceof KeywordValueTable)) {
      String msg = "Value of field \"" + keyword + " is not a KeywordValueTable; it is " +
                   value.getClass().getName ();
      throw new KeywordValueException(msg);
    }
    return (KeywordValueTable)value;
  }

  public synchronized void set(String keyword,Object value) throws KeywordValueException {
    String [] keywordPath = KeywordParser.parse(keyword);
    KeywordValueTable section = findSection(keywordPath,true,0);
    section.hashTable.put (keywordPath[keywordPath.length - 1],value);
  }

  public synchronized void setDefault(String keyword,Object defaultValue) throws KeywordValueException {
    if (!containsKey(keyword))
      set(keyword, defaultValue);
  }
    
  public synchronized boolean containsKey (String keyword) throws KeywordValueException {
    String [] keywordPath = KeywordParser.parse (keyword);
    KeywordValueTable section = findSection(keywordPath,false,0);
    if (section == null) {
      return false;
    }
    return section.hashTable.containsKey(keywordPath [keywordPath.length - 1]);
  }

  public synchronized String[] keys() {
    Enumeration keyEnum = hashTable.keys();
    Vector keyList = new Vector ();
    while (keyEnum.hasMoreElements()) {
      keyList.addElement (keyEnum.nextElement());
    }
    String [] keyStrings = new String [keyList.size()];
    for (int idx = 0; idx < keyList.size (); idx++) {
      keyStrings [idx] = (String) keyList.elementAt (idx);
    }
    return keyStrings;
  }
  
  public synchronized String [] leafKeys() {
    Enumeration keyEnum = hashTable.keys ();
    Vector keyList = new Vector ();
    while (keyEnum.hasMoreElements()) {
      String key = (String)keyEnum.nextElement();
      Object value = hashTable.get(key);
      if (value instanceof KeywordValueTable) {
        String subKeys [] = ((KeywordValueTable) value).leafKeys ();
        for (int idx = 0; idx < subKeys.length; idx++) {
          keyList.addElement(KeywordParser.concat(key,subKeys[idx]));
        }
      } else {
        keyList.addElement(key);
      }
    }
    String [] keyStrings = new String[keyList.size()];
    for (int idx = 0; idx < keyList.size(); idx++) {
      keyStrings [idx] = (String)keyList.elementAt(idx);
    }
    return keyStrings;
  }
  
  public synchronized void remove(String keyword) throws KeywordValueException {

    String [] keywordPath = KeywordParser.parse(keyword);
    KeywordValueTable section = findSection(keywordPath,false,0);
    if (section != null) {
      section.hashTable.remove(keywordPath[keywordPath.length - 1]);
    }
  }

  public synchronized String toString () {
    return hashTable.toString();
  }

  public synchronized String toHtml() {
	  StringBuffer html = new StringBuffer();
    Enumeration keyEnum = hashTable.keys();
    html.append ("<UL>\n");
    Vector keyList = new Vector();
    if (!keyEnum.hasMoreElements())
      return "";  // This makes it possible to detect empty tables.
    while (keyEnum.hasMoreElements()) {
      String key = (String)keyEnum.nextElement();
      Object value = hashTable.get(key);
      html.append("<LI> <TT>");
      html.append(key);
      html.append(": </TT>");
      html.append(formatFieldAsHtml(value));
      html.append ("\n");
    }
    html.append ("</UL>\n");
	  return html.toString();
  }

  private String formatArrayAsHtml(Object arrayObj) {
  	StringBuffer html = new StringBuffer();

	  html.append("<OL START=0>\n");
	  for (int idx = 0; idx < Array.getLength(arrayObj); idx++) {
      html.append("<LI>");
    	html.append(formatFieldAsHtml(Array.get(arrayObj, idx)));
    	html.append("\n");
    }
	  html.append("</OL>\n");
  	return html.toString();
  }

  private String formatObjectAsHtml(Object obj) {
	  String html;

	  if (obj instanceof String) {
      html = obj.toString();
    } else if (obj instanceof Integer) {
	    html = "<I><FONT SIZE=-1>(Integer)</FONT></I>" + obj.toString();
    } else if (obj instanceof Boolean) {
	    html = "<I><FONT SIZE=-1>(Boolean)</FONT></I>" + obj.toString();
    } else if (obj instanceof Double) {
	    html = "<I><FONT SIZE=-1>(Double)</FONT></I>" + obj.toString();
    } else if (obj instanceof Long) {
	    html = "<I><FONT SIZE=-1>(Long)</FONT></I>" + obj.toString();
    } else if (obj instanceof Short) {
	    html = "<I><FONT SIZE=-1>(Short)</FONT></I>" + obj.toString();
    } else if (obj instanceof Float) {
	    html = "<I><FONT SIZE=-1>(Float)</FONT></I>" + obj.toString();
    } else if (obj instanceof Character) {
	    html = "<I><FONT SIZE=-1>(Character)</FONT></I>" + obj.toString();
    } else {
	    // If object has a toHtml() method then call it,
	    // else print nothing.
	    try {
    		Class objClass = obj.getClass();
    		Method toHtmlMethod = objClass.getMethod("toHtml", null);
		    html = new String ("<I><FONT SIZE=-1>(Object)" + toHtmlMethod.invoke(obj, null) + "</FONT></I>");
	    } catch (Exception e) {
		    html = "<I><FONT SIZE=-1>(Object)</FONT></I>";
	    }
    }
  	return html;
  }
    
  private String formatFieldAsHtml(Object fieldObj) {
  	String html;
    if (fieldObj instanceof KeywordValueTable) {
      html = ((KeywordValueTable)fieldObj).toHtml();
    } else if (fieldObj.getClass().isArray()) {
      html = formatArrayAsHtml(fieldObj);
    } else {
      html = formatObjectAsHtml(fieldObj);
    }
  	return html;
  }
}
