package gov.va.med.lom.javaUtils.classloader;

import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;

/*
 * ClassLoaderStrategy provides a Strategy pattern interface
 * for Java 2's ClassLoader scheme.
 */
public interface ClassLoaderStrategy extends Serializable {
  
  // Config file keys
  public static final String STRATEGY = "Strategy";
  public static final String ARGS = "Args";

  /*
   * Return byte array (which will be turned into a Class instance
   * via ClassLoader.defineClass) for class
   */
  public byte[] findClassBytes(String className);

  /*
   * Return URL for resource given by resourceName
   */
  public URL findResourceURL(String resourceName);

  /*
   * Return Enumeration of resources corresponding to resourceName.
   */
  public Enumeration findResourcesEnum(String resourceName);

  /*
   * Return full path to native library given by the name libraryName.
   */
  public String findLibraryPath(String libraryName);

  /*
   * Return a list of all class names in the directory, jar file, hashtable, table, etc.
   */
  public String[] getClassNames();

  /*
   * Return a list of all classes in the default directory, jar file, hashtable, table, etc.
   */
  public Class[] getClasses(String classPath) throws ClassNotFoundException;
  
  /*
   * Return a list of all classes in the source path directory, jar file, hashtable, table, etc.
   */
  public Class[] getClasses(String sourcePath, String classPath) throws ClassNotFoundException;

}
