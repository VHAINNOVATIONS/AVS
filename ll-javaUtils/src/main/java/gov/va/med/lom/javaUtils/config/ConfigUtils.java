package gov.va.med.lom.javaUtils.config;

import java.io.File;

  /*
   * Misc. config file methods
   */

public class ConfigUtils {

  private static ResManager rez = ResManager.getResManager("gov.va.med.lom.appserver.util.config");

  private ConfigUtils() {
  }

  public static String myGetString(Config cfg, String key) throws ConfigException {
    return myGetString(cfg, key, null, true);
  }

  public static String myGetString(Config cfg, String key, String prefix) throws ConfigException {
    return myGetString(cfg, key, prefix, true);
  }

  public static String[] myGetStrings(Config t,String key, String prefix) throws ConfigException {
    return myGetStrings(t, key, prefix, true);
  }

  public static String myGetString(Config cfg, String key,
                            String prefix, boolean required) throws ConfigException {
    String s = null;
    try {
      s = cfg.getString(key);
    } catch (ConfigException e) {
    }
    if ((s == null) && required) {
      if (prefix != null)
        prefix += ".";
      String msg = rez.format("Error reading config file. Key {0} not found",(prefix+key));
      throw new ConfigException(msg);
    }
    return s;
  }

  public static String[] myGetStrings(Config t, String key,
                               String prefix, boolean required) throws ConfigException {
    String[] s = null;
    try {
        s = t.getStrings(key);
    } catch (ConfigException e) {
      System.err.println(e.getMessage());
    }
    if ((s == null) && required) {
        if (prefix != null)
            prefix += ".";
        String msg = rez.format("Error reading config file. Key {0} not found",(prefix+key));
      throw new ConfigException(msg);
    }
    return s;
  }

  public static Config myGetSection(Config cfg, String key) throws ConfigException {
    return myGetSection(cfg, key, true);
  }

  public static Config myGetSection(Config cfg, String key, boolean required) throws ConfigException {
    Config cfg2 = null;
    try {
      cfg2 = (Config) cfg.getSection(key);
    } catch (ConfigException e) {
      System.err.println("ERROR: " + e.getMessage());
    } catch (Exception e) {
      String rezKey = "Internal error parsing config file (section {0}).";
      String msg = rez.format(rezKey,key);
      System.err.println(msg);
    }
    if ((cfg2 == null) && required) {
      String rezKey = "Illegal config file. Section {0} not found.";
      String msg = rez.format(rezKey,key);
      System.err.println(msg);
      throw new ConfigException(msg);
    }
    return cfg2;
  }

  public static String trim(String s) {
    if (s == null)
      return null;
    int len = s.length();
    while(len > 0) {
      char c = s.charAt(0);
      if ((c == ' ') || (c == '\t')) {
        s = s.substring(1);
        len--;
      } else
        break;
    }
    while(len > 0) {
      char c = s.charAt(len - 1);
      if ((c == ' ') || (c == '\t')) {
        s = s.substring(0, len - 1);
        len--;
      } else
        break;
    }
    return s;
  }

  public static String findReplace(String originalText,String findText, String replaceText) {
    StringBuffer textBuf = new StringBuffer(originalText);
    int findTextLen = findText.length();
    int originalTextLen = originalText.length();
    int index = 0;
    while((index + findTextLen) < originalTextLen) {
      String sub = textBuf.substring(index,index + findTextLen);
      if(sub.compareToIgnoreCase(findText) == 0) {
        textBuf.replace(index,index + findTextLen,replaceText);
        originalTextLen = textBuf.length();
        index += replaceText.length();
      }
      index++;
    }
    return textBuf.toString();
  }

  public static String abstractPathToSystemPath(String path) {
    String sep = File.separator;
    return findReplace(path,".",sep);
  }

  public static void printMe(String text) {
    System.out.println(text);
  }
}
