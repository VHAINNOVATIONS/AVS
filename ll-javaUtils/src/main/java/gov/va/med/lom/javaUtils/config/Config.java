package gov.va.med.lom.javaUtils.config;

import java.lang.reflect.*;

/*
 * Config is essentially a KeywordValueTable used for recursive
 * storage of data derived from a config file.  
 */

public class Config extends KeywordValueTable {

  private static final int	GET_ALL = -1;
  private ConfigFile configFile = null;

  public Config() {
	  super();
  }

  public Config(KeywordValueTable kvt) {
  	super();
	  String[] keys = kvt.keys();
	  for (int i = 0; i < keys.length; i++) {
	    try {
		    set(keys[i], kvt.get(keys[i]));
	    } catch (KeywordValueException e) {
        System.err.println(e.getMessage());
        throw new FatalExceptionError(e);
	    }
    }
  }

  public Config(KeywordValueTable kvt, ConfigFile configFile) {
  	this(kvt);
    this.configFile = configFile;
  }

  public Config(ConfigFile configFile) {
  	this();
    this.configFile = configFile;
  }

  protected KeywordValueTable newSection() {
    return new Config(configFile);
  }

  public ConfigFile getConfigFile() {
    return configFile;
  }

  void setConfigFile(ConfigFile configFile) {
    this.configFile = configFile;
  }

  public synchronized Config getConfig(String keyword) throws KeywordValueException {
    return (Config) getSection(keyword);
  }

  public synchronized KeywordValueTable getSection(String keyword) throws KeywordValueException {
    KeywordValueTable kvt = super.getSection(keyword);
	  if (kvt == null) {
	    return null;
    }
	  if (kvt instanceof Config) {
      return kvt;
    } else {
	    return new Config(kvt, configFile);
    }
  }

  public boolean containsKey(String key) {
  	boolean result = false;
	  try {
	    result = super.containsKey(key);
    } catch (KeywordValueException e) {
      System.err.println(e.getMessage());
	    result = false;
    }
	  return result;
  }

  public int containsCount(String key) throws ConfigException {
  	Object valObj = null;
	  try {
	    valObj = get(key);	
	    if (valObj == null) 
        return -1;
	    return Array.getLength(valObj);
    } catch (KeywordValueException e) {
      System.err.println(e.getMessage());
      throw new ConfigException(e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    }
  	if (valObj == null) 
      return -1;
	  return 1;
  }

  public boolean isArray(String key) throws ConfigException {
    Object valObj = null;
    try {
      valObj = get(key);
      if (valObj == null)
        throw new ConfigException("Key \"" + key + "\" not found.");
        // Attempt array access. This will fail if not an array.
      Array.getLength(valObj);
      return true;
    } catch (KeywordValueException e) {
      System.err.println(e.getMessage());
      throw new ConfigException(e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      return false;
    }
  }       

  private final long[] getLongsInternal(String key, int count) throws ConfigException {
  	Object obj;
	  try {
	    obj = get(key);
    } catch (KeywordValueException e) {
      System.err.println(e.getMessage());
	    throw new ConfigException(e.getMessage());
    }
	  if (obj == null) {
	    throw new ConfigException(ConfigException.NOT_FOUND,
		                            "Key \"" + key + "\" not found in configuration.");
    }
	  long[] la = null;
	  if (obj.getClass().isArray()) {
	    int len = Array.getLength(obj);
	    la = new long[len];
	    for (int i=0; i<len; i++) {
		    try {
		      la[i] = (long) Long.parseLong(Array.get(obj,i).toString());
        } catch (Throwable e) {
          System.err.println(e.getMessage());
		      throw new ConfigException("Element " + i + " is not a long.");
        }
	    }
    } else {
	    la = new long[1];
	    try {
		    la[0] = Long.parseLong(obj.toString());
      } catch (Throwable e) {
        System.err.println(e.getMessage());
		    throw new ConfigException("Element 0 is not a long.");
      }
    }
	  if ((count != GET_ALL) && (la.length != count)) {
	    throw new ConfigException(ConfigException.COUNT,
		                            "Key \"" + key
		                            + "\" has " + la.length + " elements. (expected "
		                            + count + ")");
    }
	  return la;
  }

  public long getLong(String key) throws ConfigException {
	  return (getLongsInternal(key, 1))[0];
  }

  public long getLong(String key, long defaultValue) throws ConfigException {
	  try {
	    return (getLongsInternal(key, 1))[0];
    } catch (ConfigException e) {
	    if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
	    }
	    return defaultValue;
    }
  }

  public long[] getLongs(String key) throws ConfigException  {
	  return getLongsInternal(key, GET_ALL);
  }

  public long[] getLongs(String key, long[] defaultValue) throws ConfigException {
	  try {
	    return getLongsInternal(key, GET_ALL);
    } catch (ConfigException e) {
      System.err.println(e.getMessage());
	    if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
	    }
    return defaultValue;
    }
  }

  private final int[] getIntsInternal(String key, int count) throws ConfigException {
	  Object obj;
	  try {
	    obj = get(key);
    } catch (KeywordValueException e) {
	    throw new ConfigException(e.getMessage());
    }
	  if (obj == null) {
	    throw new ConfigException(ConfigException.NOT_FOUND,
	                             "Key \"" + key
	                             + "\" not found in configuration.");
    }
	  int[] ia = null;
	  if (obj.getClass().isArray()) {
	    int len = Array.getLength(obj);
	    ia = new int[len];
	    for (int i=0; i<len; i++) {
  		  try {
	  	    ia[i] = (int)Integer.parseInt(Array.get(obj,i).toString());
        } catch (Throwable e) {
          System.err.println(e.getMessage());
		      throw new ConfigException("Element " + i + " is not an integer.");
        }
	    }
    } else {
	    ia = new int[1];
	    try {
		    ia[0] = Integer.parseInt(obj.toString());
	    } catch (Throwable e) {
        System.err.println(e.getMessage());
		    throw new ConfigException("Element 0 is not an integer.");
	    }
    }
	  if ((count != GET_ALL) && (ia.length != count)) {
	    throw new ConfigException(ConfigException.COUNT,
		                            "Key \"" + key
		                            + "\" has " + ia.length + " elements. (expected "
		                            + count + ")");
    }
	  return ia;
  }

  public int getInt(String key) throws ConfigException {
	  return (getIntsInternal(key, 1))[0];
  }

  public int getInt(String key, int defaultValue) throws ConfigException {
	  try {
	    return (getIntsInternal(key, 1))[0];
    } catch (ConfigException e) {
      System.err.println(e.getMessage());
      if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
	    }
	    return defaultValue;
    }
  }

  public int[] getInts(String key) throws ConfigException {
	  return getIntsInternal(key, GET_ALL);
  }

  public int[] getInts(String key, int[] defaultValue) throws ConfigException {
	  try {
	    return getIntsInternal(key, GET_ALL);
    } catch (ConfigException e) {
      System.err.println(e.getMessage());
	    if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
      }
	    return defaultValue;
    }
  }

  private final String[] getStringsInternal(String key, int count) throws ConfigException {
	  Object obj;
	  try {
	    obj = get(key);
    } catch (KeywordValueException e) {
      System.err.println(e.getMessage());
	    throw new ConfigException(e.getMessage());
    }
	  if (obj == null) {
	    throw new ConfigException(ConfigException.NOT_FOUND,
		                            "Key \"" + key
		                            + "\" not found in configuration.");
    }
	  String[] sa = null;
	  if (obj.getClass().isArray()) {
	    int len = Array.getLength(obj);
	    sa = new String[len];
	    for (int i=0; i<len; i++) {
		  try {
		    sa[i] = Array.get(obj,i).toString();
      } catch (Throwable e) {
        System.err.println(e.getMessage());
  		  throw new ConfigException("Element " + i + " is not a String.");
      }
	  }
    } else {
	    sa = new String[1];
	    try {
		    sa[0] = obj.toString();
	    } catch (Throwable e) {
        System.err.println(e.getMessage());
		    throw new ConfigException("Element 0 is not a String.");
	    }
    }
	  if ((count != GET_ALL) && (sa.length != count)) {
	    throw new ConfigException(ConfigException.COUNT,
		                            "Key \"" + key
		                            + "\" has " + sa.length + " elements. (expected "
		                            + count + ")");
    }
	  return sa;
  }


  public String getString(String key) throws ConfigException {
	  return (getStringsInternal(key, 1))[0];
  }

  public String getString(String key, String defaultValue) throws ConfigException {
	  try {
	    return (getStringsInternal(key, 1))[0];
    } catch (ConfigException e) {
	    if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
	    }
	    return defaultValue;
    }
  }

  public String[] getStrings(String key) throws ConfigException {
	  return getStringsInternal(key, GET_ALL);
  }

  public String[] getStrings(String key, String[] defaultValue) throws ConfigException {
	  try {
	    return getStringsInternal(key, GET_ALL);
    } catch (ConfigException e) {
	    if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
      }
	    return defaultValue;
    }
  }

  private final boolean[] getBooleansInternal(String key, int count) throws ConfigException {
	  Object obj;
	  try {
	    obj = get(key);
    } catch (KeywordValueException e) {
      System.err.println(e.getMessage());
	    throw new ConfigException(e.getMessage());
    }
	  if (obj == null) {
	    throw new ConfigException(ConfigException.NOT_FOUND,
		                            "Key \"" + key
		                            + "\" not found in configuration.");
    }
	  boolean[] ba = null;
	  if (obj.getClass().isArray()) {
	    int len = Array.getLength(obj);
	    ba = new boolean[len];
	    for (int i=0; i<len; i++) {
		    try {
		      ba[i] = Boolean.valueOf(
		      Array.get(obj,i).toString().toLowerCase()).booleanValue();
        } catch (Throwable e) {
          System.err.println(e.getMessage());
		      throw new ConfigException("Element " + i + " is not a boolean.");
        }
	    }
    } else {
	    ba = new boolean[1];
	    try {
        ba[0] = Boolean.valueOf(obj.toString().toLowerCase()).booleanValue();
	    } catch (Throwable e) {
        System.err.println(e.getMessage());
		    throw new ConfigException("Element 0 is not a boolean.");
	    }
    }
	  if ((count != GET_ALL) && (ba.length != count)) {
	    throw new ConfigException(ConfigException.COUNT,
		                            "Key \"" + key
		                            + "\" has " + ba.length + " elements. (expected "
		                            + count + ")");
    }
	  return ba;
  }

  public boolean getBoolean(String key) throws ConfigException {
	  return (getBooleansInternal(key, 1))[0];
  }

  public boolean getBoolean(String key, boolean defaultValue) throws ConfigException {
	  try {
	    return(getBooleansInternal(key, 1)[0]);
    } catch (ConfigException e) {
      System.err.println(e.getMessage());
	    if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
	    }
	  return defaultValue;
    }
  }

  public boolean[] getBooleans(String key) throws ConfigException {
	  return getBooleansInternal(key, GET_ALL);
  }

  public boolean[] getBooleans(String key, boolean[] defaultValue) throws ConfigException {
	  try {
	    return getBooleansInternal(key, GET_ALL);
    } catch (ConfigException e) {
      System.err.println(e.getMessage());
	    if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
	    }
     return defaultValue;
    }
  }
 
  private final double[] getDoublesInternal(String key, int count) throws ConfigException {
	  Object obj;
	  try {
	    obj = get(key);
    } catch (KeywordValueException e) {
      System.err.println(e.getMessage());
	    throw new ConfigException(e.getMessage());
    }
	  if (obj == null) {
	    throw new ConfigException(ConfigException.NOT_FOUND,
		                            "Key \"" + key
		                            + "\" not found in configuration.");
    }
	  double[] da = null;
	  if (obj.getClass().isArray()) {
	    int len = Array.getLength(obj);
	    da = new double[len];
	    for (int i=0; i<len; i++) {
		    try {
		      da[i] = Double.valueOf(Array.get(obj,i).toString()).doubleValue();
        } catch (Throwable e) {
          System.err.println(e.getMessage());
		      throw new ConfigException("Element " + i + " is not a double.");
        }
	    }
    } else {
	    da = new double[1];
	    try {
		    da[0] = Double.valueOf(obj.toString()).doubleValue();
	    } catch (Throwable e) {
        System.err.println(e.getMessage());
		    throw new ConfigException("Element 0 is not a long.");
	    }
    }
	  if ((count != GET_ALL) && (da.length != count)) {
	    throw new ConfigException(ConfigException.COUNT,
		                            "Key \"" + key
		                            + "\" has " + da.length + " elements. (expected "
		                            + count + ")");
    }
	  return da;
  }

  public double getDouble(String key) throws ConfigException {
	  return (getDoublesInternal(key, 1))[0];
  }

  public double getDouble(String key, double defaultValue) throws ConfigException {
	  try {
	    return(getDoublesInternal(key, 1)[0]);
    } catch (ConfigException e) {
      System.err.println(e.getMessage());
	    if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
	    }
	  return defaultValue;
    }
  }
 
  public double[] getDoubles(String key) throws ConfigException {
	  return getDoublesInternal(key, GET_ALL);
  }

  public double[] getDoubles(String key, double[] defaultValue) throws ConfigException {
	  try {
	    return getDoublesInternal(key, GET_ALL);
    } catch (ConfigException e) {
      System.err.println(e.getMessage());
	    if (e.reason != ConfigException.NOT_FOUND) {
		    throw e;
	    }
      return defaultValue;
    }
  }
}

