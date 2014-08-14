package gov.va.med.lom.javaUtils.classloader;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

public class CompositeClassLoader extends ClassLoader implements ClassLoaderStrategy {

  private Vector loaders = new Vector();

  public CompositeClassLoader() {
    this(CompositeClassLoader.class.getClassLoader(), null);
  }

  public CompositeClassLoader(ClassLoaderStrategy[] loaders) {
    this(CompositeClassLoader.class.getClassLoader(), loaders);
  }

  public CompositeClassLoader(ClassLoader parent) {
    this(parent, null);
  }

  public CompositeClassLoader(ClassLoader parent, ClassLoaderStrategy[] strategies) {
    // Establish parent ClassLoader relationship
    //
    super(CompositeClassLoader.class.getClassLoader());

    // Copy over ClassLoaderStrategy instances (if any)
    if (loaders != null && strategies.length > 0) {
      for (int i=0; i< strategies.length; i++) {
        this.loaders.addElement(strategies[i]);
      }
    }
  }

  public void addLoader(ClassLoaderStrategy cls) {
    loaders.addElement(cls);
  }

  public Enumeration enumLoaders() {
    return loaders.elements();
  }

  public void removeLoader(ClassLoaderStrategy cls) {
    loaders.remove(cls);
  }

  /*
   * Return byte array (which will be turned into a Class instance
   * via ClassLoader.defineClass) for class
   */
  public byte[] findClassBytes(String className) {
    byte[] bytecode = null;

    for (Enumeration en = enumLoaders();en.hasMoreElements();) {
      ClassLoaderStrategy strat = (ClassLoaderStrategy)en.nextElement();
      bytecode = strat.findClassBytes(className);

      if (bytecode != null) {
        return bytecode;
      }
    }
    return bytecode;
  }

  /*
   * Return URL for resource given by resourceName
   */
  public URL findResourceURL(String resourceName) {
    URL resource = null;

    for (Enumeration en = enumLoaders();en.hasMoreElements();) {
      ClassLoaderStrategy strat = (ClassLoaderStrategy)en.nextElement();
      resource = strat.findResourceURL(resourceName);

      if (resource != null) {
        return resource;
      }
    }

    return resource;
  }

  /*
   * Return Enumeration of resources corresponding to resourceName.
   */
  public Enumeration findResourcesEnum(String resourceName) {
    Enumeration resourceEnum = null;

    for (Enumeration en = enumLoaders(); en.hasMoreElements();) {
      ClassLoaderStrategy strat = (ClassLoaderStrategy)en.nextElement();
      resourceEnum = strat.findResourcesEnum(resourceName);

      if (resourceEnum != null) {
        return resourceEnum;
      }
    }

    return resourceEnum;
  }

  /*
   * Return full path to native library given by the name libraryName.
   */
  public String findLibraryPath(String libraryName) {
    String libPath = null;

    for (Enumeration en = enumLoaders(); en.hasMoreElements(); ) {
      ClassLoaderStrategy strat = (ClassLoaderStrategy)en.nextElement();
      libPath = strat.findLibraryPath(libraryName);

      if (libPath != null) {
        return libPath;
      }
    }

    return libPath;
  }

  /*
   * Find the class bytecode; defers to the Strategy's
   * findClassBytes method.
   */
  protected Class findClass(String name) throws ClassNotFoundException {
    byte[] classBytes = findClassBytes(name);

    if (classBytes == null) {
      throw new ClassNotFoundException();
    }

    return defineClass(name, classBytes, 0, classBytes.length);
  }

  public String[] getClassNames() {
    return null; // not yet implemented
  }

  public Class[] getClasses(String classPath) throws ClassNotFoundException {
    return null; // not yet implemented
  }
  
  public Class[] getClasses(String sourcePath, String classPath) throws ClassNotFoundException {
    return null; // not yet implemented
  }  

  // Test driver
  public static void main(String[] args) throws Exception {
    // Build the array of Strategy instances
    ClassLoaderStrategy classLoaderArray[] = new ClassLoaderStrategy[2];

    // Build a FileSystemClassLoader
    classLoaderArray[0] = new FileSystemClassLoader(
                              System.getProperties().getProperty("testing.dir"));

    // Build a HashtableClassLoader
    classLoaderArray[1] = new HashtableClassLoader();
    FileInputStream fis = new FileInputStream("/Hello.class");
    int ct = fis.available();
    byte[] Hello_bytes = new byte[ct];
    fis.read(Hello_bytes);
    ((HashtableClassLoader)classLoaderArray[1]).putClass("Hello", Hello_bytes);

    // Build the CompositeClassLoader made up of those two
    CompositeClassLoader ccl = new CompositeClassLoader(classLoaderArray);

    // Load one from the HashtableClassLoader
    Object o1 = ccl.loadClass("Hello").newInstance();

    // Load one from the FileSystemClassLoader (cmd-line param)
    if (args.length > 0) {
      Object o2 = ccl.loadClass(args[0]).newInstance();
    }
  }
}
