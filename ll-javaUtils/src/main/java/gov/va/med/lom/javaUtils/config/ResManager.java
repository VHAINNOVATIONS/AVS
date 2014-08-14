package gov.va.med.lom.javaUtils.config;

import java.util.*;
import java.text.MessageFormat;


/*
 * Some helper functions for handeling i18n issues. One instance of this class
 * should be created for each resource bundle.
 *
 * The ResManager is created by a call to getResourceManager()
 * the parameter is the name of the package that contails the Res class.
 * e.g. ResManager rez = getResourceBundle("gov.va.med.lom.appserver.mypackagename");
 *
 * To use the ResManager make a call to any of the format()
 * methods. In the default resource bundle the key is the same as the value.
 * So to display "I am 2 years old" call rez.format("I am {0} years old",2);
 * If the string "I am {0} years old" is in the bundle the value is returned.
 */

public class ResManager {

  // The ResourceBundle for this locale.
  private ResourceBundle bundle;
  private String bundleName;

  static private Hashtable bundles = new Hashtable();


  private ResManager(String packageName) {
    bundleName = packageName + ".Res";
  }

  /*
   * Returns a resource manager assocated with the package name.
   * An instance of the Res class is created the first time the method is
   * called.
   */
  public static ResManager getResManager(Class clazz) {
    String packageName = clazz.getName();
    int lastDot = packageName.lastIndexOf('.');
    if (lastDot != -1)
      packageName = packageName.substring(0,lastDot);
    return getResManager(packageName);
  }

  /*
   * Returns a resource manager assocated with the package name.
   * An instance of the Res class is created the first time the method is
   * called.
   */
  public static ResManager getResManager(String packageName) {
    ResManager rez = (ResManager) bundles.get(packageName);
    if (rez==null) {
      rez = new ResManager(packageName);
      bundles.put(packageName,rez);
    }
    return rez;
  }

  private String getString(String key) {
    // just return the key for now.
    return key;
  }

  /*
   * Returns a string that has been obtained from the resource manager
   */
  public String format(String key) {
    return getString(key);
  }

  /*
   * Returns a string that has been obtained from the resource manager then
   * formatted using the passed parameters.
   */
  public String format(String pattern, Object o0) {
    return MessageFormat.format(getString(pattern), new Object[] {o0});
  }

  /*
   * Returns a string that has been obtained from the resource manager then
   * formatted using the passed parameters.
   */
  public String format(String pattern, Object o0, Object o1) {
    return MessageFormat.format(getString(pattern), new Object[] {o0,o1});
  }

  /*
   * Returns a string that has been obtained from the resource manager then
   * formatted using the passed parameters.
   */
  public String format(String pattern, Object o0, Object o1, Object o2) {
    return MessageFormat.format(getString(pattern), new Object[] {o0,o1,o2});
  }

  /*
   * Returns a string that has been obtained from the resource manager then
   * formatted using the passed parameters.
   */
  public String format(String pattern, Object o0, Object o1, Object o2, Object o3) {
    return MessageFormat.format(getString(pattern), new Object[] {o0,o1,o2,o3});
  }
}

