package gov.va.med.lom.javaUtils.config;

import java.io.*;
import java.util.*;

/*
 * ConfigFile is used to manipulate configuration (.conf) files.
 *
 * Presents all configuration elements in the form of a keyed
 * table.  Configuration elements are grouped by string "keys" according
 * to the function they configure.  The syntax is described more formally
 * below.
 * 
 * 	stream ::= entry | stream entry
 * 	entry ::= key "=" value_list | comment | blank
 * 	value_list ::= value | value_list "," value
 * 	value ::= fragment | value "+" fragment
 * 	fragment ::= key | quoted_string
 * 	quoted_string ::= (C/C++ style quoted string)
 * 	key ::= (A string matching [A-Za-z_\./][A-Za-z0-9_-\./]*)
 * 	comment ::= "#" (any text up to a newline)
 * 	blank ::= (A line containing only white space)
 * 
 * In addition to the above syntax, some additional semantic rules apply.
 * The operator "+" concatenates the fragment immediately to the left
 * and to the right of it.  Thus ab + cd results in
 * abcd.  The operator "," terminates one element and begins
 * the next element.  Thus, the line val = hello, world
 * creates a configuration entry with the key "val" and the two elements
 * "hello", and "world".  If the characters "+", ",", or "\" occur at
 * the end of a line, then the entry is continued on the next line.
 * Trailing and leading whitespaces are ignored, and multiple whitespace
 * characters are converted by default into a single space.  The "+"
 * operator leaves no whitespace between operands.
 * Finally, within quoted strings, C-style backslash escapes are
 * recognized.  These include "\n" for newline, "\t" for tab, etc.
 * An example configuration input file is given below.
 * 
 *	#==============================================================
 *	# Sample.config
 *	#==============================================================
 *	LDAP_SERVERS[] =  "server1.ldap.gov:338",
 *			  "server2.ldap.gov:1000"
 *	USER_TIMEOUT =  3600  # seconds.  Comments can follow on same line.
 *	STRANGE_ENTRY = "This is a long long long long long " +
 *			"long long line.  Notice how the \"+\" " +
 *			"operator is used to break this line up " +
 *			"into more than one line in the config file." ,
 *			"And this is the second element."
 *	# etc.
 */

public class ConfigFile {
  private Config config;
  private Vector order;
  private Hashtable comments;
  private File file = null;
  public static final String TRAILING_COMMENT = "ConfigFileTrailingComment";
  private static final char[] digits = {
  	'0', '1', '2', '3', '4', '5', '6', '7',
	  '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  public ConfigFile() {
	  config = new Config();
	  order = new Vector();
	  comments = new Hashtable();
  }

  public ConfigFile(InputStream inputStream) throws ConfigException {
  	config = new Config();
  	order = new Vector();
  	comments = new Hashtable();
  	ConfigParser parser = new ConfigParser(inputStream);
  	try {
	    parser.process(this);
    } catch (ParseException e) {
	    throw new ConfigException(ConfigException.SYNTAX, e.getMessage());
    }
  }

  public ConfigFile(File configFile) throws ConfigException, IOException {
    this(new FileInputStream(configFile));
    this.file = configFile;
    config.setConfigFile(this);
  }

  public ConfigFile(KeywordValueTable kvt) throws ConfigException {
  	config = new Config(kvt);
  	order = new Vector();
  	comments = new Hashtable();
  }

  public Config getConfig() {
  	return config;
  }

  public String getComment(String key) {
  	return (String)comments.get(key);
  }

  public void addEntry(String key, String[] values, String comment) throws KeywordValueException {

  	// Don't add an actual config entry for the trailing comment
	  if (!key.equals(TRAILING_COMMENT)) {
	    config.set(key, values);
	    if (!order.contains(key)) {
	        order.addElement(key);
	    }
    }
	  comments.put(key, comment);
  }

  public void addEntry(String key, String value, String comment) throws KeywordValueException {

	  // Don't add an actual config entry for the trailing comment
	  if (!key.equals(TRAILING_COMMENT)) {
      config.set(key, value);
	    if (!order.contains(key)) {
  	    order.addElement(key);
      }
    }
	  comments.put(key, comment);
  }

  public void removeEntry(String key) throws KeywordValueException {
  	// There is no config entry for the trailing comment
	  if (!key.equals(TRAILING_COMMENT)) {
	    config.remove(key);
	    order.removeElement(key);
    }
	  comments.remove(key);
  }

  public File getFile() {
    return file;
  }

  public void write() throws IOException, FileNotFoundException {
    if (file == null) {
      throw new FileNotFoundException("No file associated with this object");
    }
    FileOutputStream out = new FileOutputStream(file);
    write(out);
    out.close();
  }

  public void write(OutputStream outputStream) {
  	PrintWriter out = new PrintWriter(outputStream, true);
  	boolean isArray = false;
  	String key;
  	String comment;
  	String[] values;
  	Hashtable remaining = new Hashtable();
  	String[] remainingkeys = config.leafKeys();

	  // Set up the remaining keys list
	  // The value doesn't matter, just the key
	  for (int i = 0; i < remainingkeys.length; i++) {
	    remaining.put(remainingkeys[i], "X");
    }

	  // Do all the entries for which we have comments
	  for (int i = 0; i < order.size(); i++) {
	    key = (String) order.elementAt(i);
	    comment = (String) comments.get(key);
	    isArray = false;
	    try {
	      Object o = config.get(key);
        if (o == null) {
          continue;
        }
		    isArray = o.getClass().isArray();
		    if (isArray) {
		      Object[] oa = (Object[])o;
          if ((oa.length > 0) && (oa[0] instanceof java.lang.String)) {
            values = (String[]) o;
          } else {
		        values = new String[oa.length];
			      for (int k = 0; k < oa.length; k++) {
			        values[k] = oa[k].toString();
            }
          }
        } else {
          values = new String[1];
          if (o instanceof java.lang.String) {
            values[0] = (String) o;
          } else {
			      values[0] = o.toString();
          }
        }
	    } catch (KeywordValueException e) {
		    values = null;
	    }
	    // write out entry
	    if ((values == null) || (values.length == 0)) {
		    if ((comment != null) && !(comment.equals(""))) {
  		    if (comment.endsWith("\n")) {
		        out.print(comment);
          } else {
		        out.println(comment);
          }
        }
		    out.print(key);
		    if (isArray) 
          out.print("[]");
		    out.println(" =");
      } else {
		    if ((comment != null) && !(comment.equals(""))) {
  		    if (comment.endsWith("\n")) {
	    	    out.print(comment);
          } else {
		        out.println(comment);
          }
        }
		    if (isArray) {
		      out.print(key + "[] = " + quoteStr(values[0]));
        } else {
		      out.print(key + " = " + quoteStr(values[0]));
        }
		    for (int j = 1; j < values.length; j++) {
	        out.print(", " + quoteStr(values[j]));
        }
		    out.println();
      }
	    remaining.remove(key);
    }

	  // Do the trailing comment
	  comment = (String) comments.get(TRAILING_COMMENT);
	  if ((comment != null) && !(comment.equals(""))) {
	    if (comment.endsWith("\n")) {
	      out.print(comment);
      } else {
  	    out.println(comment);
      }
    }
	  remaining.remove(TRAILING_COMMENT);

    // The new keys are in a hash table with no order
    // sort them here so that they will be grouped correctly
    // in the conf file. It makes it easier to read.
    int i=0;
    String lastWord = "";
    while (i != remainingkeys.length) {
      key = remainingkeys[i];
      i++;
	    // Do the remaining config entries
      if (remaining.get(key) != null) {
  	    isArray = false;
        if (!key.startsWith(lastWord)) {
          out.println("");
        }
        int dot = key.indexOf('.');
        if (dot == -1)
          dot = key.length();
          lastWord = key.substring(0,dot);
  	      try {
	          Object o = config.get(key);
            if (o == null) {
              continue;
            }
		        isArray = o.getClass().isArray();
		        if (isArray) {
		          Object[] oa = (Object[])o;
              if (oa[0] instanceof java.lang.String) {
                values = (String[]) o;
              } else {
		            values = new String[oa.length];
			          for (int k = 0; k < oa.length; k++) {
			            values[k] = oa[k].toString();
                }
              }
            } else {
              values = new String[1];
              if (o instanceof java.lang.String) {
                values[0] = (String) o;
              } else {
			          values[0] = o.toString();
              }
            }
          } catch (KeywordValueException e) {
		        values = null;
          }
	        // write out entry
	        if ((values == null) || (values.length == 0)) {
		        out.println(key + " =");
          } else {
		      if (isArray) {
		        out.print(key + "[] = " + quoteStr(values[0]));
          } else {
		        out.print(key + " = " + quoteStr(values[0]));
          }
      		for (int j = 1; j < values.length; j++) {
		        out.print(key + ", " + quoteStr(values[j]));
          }
		      out.println();
        }
      }
    }
  }


  private boolean containsWhiteSpace(String str) {
  	if (str.indexOf(" ") != -1) {
	    return true;
    } else if (str.indexOf("\t") != -1) {
	    return true;
    }
	  return false;
  }

  private static final String quoteStr(String s) {
    if ((s == null) || (s.length() < 1)) 
      return "";
	  char[] chars = s.toCharArray();
	  StringBuffer sb = new StringBuffer();
	  boolean needQuotes = false;
	  for (int i=0; i<chars.length; i++) {
	    switch (chars[i]) {
  		  // Chars that get special backquotes
		    case '\n':
	  	    needQuotes = true;
		      sb.append("\\n");
		      break;
		    case '\b':
		      needQuotes = true;
		      sb.append("\\b");
		      break;
		    case '\r':
  		    needQuotes = true;
	  	    sb.append("\\r");
		      break;
		    case '\f':
  		    needQuotes = true;
		      sb.append("\\f");
		      break;
		    case '"':
  		    needQuotes = true;
		      sb.append("\\\"");
		      break;
		    case '\\':
  		    needQuotes = true;
		      sb.append("\\\\");
		      break;

  		  // Chars that cause the string to be enclosed in
		    // double quotes.
		    case '\t': case ' ': case '!': case '#': case '$':
		    case '%': case '&': case '\'': case '(': case ')':
		    case '*': case '+': case ',': case '/': case ':':
		    case ';': case '<': case '=': case '>': case '?':
		    case '[': case ']': case '^': case '`': case '{':
		    case '|': case '}': case '~':
		      needQuotes = true;
		      sb.append(chars[i]);
		      break;

		    // All other characters.
		    default:
  		    if ((chars[i] < ' ') || (chars[i] == 0x7f)) {
		      	needQuotes = true;
			      int ival = (int) chars[i];
			      sb.append('\\');
			      sb.append(digits[(ival & 0xc0) >> 6]);
			      sb.append(digits[(ival & 0x38) >> 3]);
			      sb.append(digits[(ival & 0x07)]);
          } else if (chars[i] > 0x7f) {
			      needQuotes = true;
			      int ival = (int) chars[i];
			      sb.append("\\u");
			      sb.append(digits[(ival & 0xf000) >> 12]);
			      sb.append(digits[(ival & 0x0f00) >> 8]);
			      sb.append(digits[(ival & 0x00f0) >> 4]);
			      sb.append(digits[(ival & 0x000f)]);
          } else {
			      sb.append(chars[i]);
          }
      }
    }
	  if (needQuotes) {
	    return( "\"" + sb.toString() + "\"");
    }
	  return sb.toString();
  }

}