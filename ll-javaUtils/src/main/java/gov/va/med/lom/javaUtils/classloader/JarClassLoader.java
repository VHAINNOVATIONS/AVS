package gov.va.med.lom.javaUtils.classloader;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.*;
import java.util.jar.*;
import java.util.*;

/*
 * A class loader for loading jar files, both local and remote.
 */
public class JarClassLoader extends ClassLoader implements ClassLoaderStrategy {
  private Hashtable sizes;
  private Hashtable jarContents;
  private String jarFileName;

  public JarClassLoader() throws FileNotFoundException {
    //
  }  

  public JarClassLoader(String jarFileName) throws FileNotFoundException {
    this.jarFileName = jarFileName;
    loadClasses();
  }

  /*
   * Return byte array (which will be turned into a Class instance
   * via ClassLoader.defineClass) for class
   */
  public byte[] findClassBytes(String className) {
    // transform the name to a path
    String classPath = className.replace('.', '/') + ".class";
    byte[] classBytes = (byte[])jarContents.get(classPath);
    return classBytes;
  }

  /*
   * Return URL for resource given by resourceName
   */
  public URL findResourceURL(String resourceName) {
    return null;
  }

  /*
   * Return Enumeration of resources corresponding to resourceName.
   */
  public java.util.Enumeration findResourcesEnum(String resourceName) {
    return null;
  }

  /*
   * Return full path to native library given by the name libraryName.
   */
  public String findLibraryPath(String libraryName) {
    return null;
  }

  /*
   * Looks among the contents of the jar file (cached in memory)
   * and tries to find and define a class, given its name.
   *
   * className = the name of the class
   * returns: a Class object representing our class
   * exception: ClassNotFoundException - the jar file did not contain a class named className
   */
  public Class findClass(String className) throws ClassNotFoundException {
    byte[] classBytes = findClassBytes(className);
    if (classBytes == null)
      throw new ClassNotFoundException();
    return defineClass(className, classBytes, 0, classBytes.length);
  }

  /*
   * Load the classes
   */
  public void loadClasses() {
    try {
      // get sizes of all files in the jar file
      sizes = new Hashtable();
      ZipFile zf = new ZipFile(jarFileName);
      Enumeration e = zf.entries();
      while (e.hasMoreElements()) {
        ZipEntry ze = (ZipEntry)e.nextElement();
        String entryName = ze.getName();
        sizes.put(entryName, new Integer((int)ze.getSize()));
      }
      zf.close();
      // get contents of the jar file and place each entry in a hashtable for later use
      jarContents = new Hashtable();
      JarInputStream jis = new JarInputStream(new BufferedInputStream(new FileInputStream(jarFileName)));
      JarEntry je;
      while ((je = jis.getNextJarEntry()) != null) {
        String name = je.getName();
        // get entry size from the entry or from our hashtable
        int size;
        if ((size = (int)je.getSize()) < 0)
          size = ((Integer)sizes.get(name)).intValue();
        // read the entry
        byte[] ba = new byte[size];
        int bytes_read = 0;
        while (bytes_read != size) {
          int r = jis.read(ba, bytes_read, size - bytes_read);
          if (r < 0)
            break;
          bytes_read += r;
        }
        jarContents.put(name, ba);
      }
      jis.close();
    } catch (IOException ioe) {
      System.err.println(ioe.getMessage());
    }
  }

  public String[] getClassNames() {
    Vector classesVect = new Vector();
    try {
      ZipFile zf = new ZipFile(jarFileName);
      Enumeration e = zf.entries();
      while (e.hasMoreElements()) {
        ZipEntry ze = (ZipEntry)e.nextElement();
        String name = ze.getName();
        if (name.endsWith(".class"))
          classesVect.add(name);
      }
    } catch (IOException ioe) {
      System.err.println(ioe.getMessage());
    }
    String[] classNames = new String[classesVect.size()];
    for (int i = 0; i < classesVect.size(); i++)
      classNames[i] = (String)classesVect.get(i);
    return classNames;
  }

  public Class[] getClasses(String classPath) throws ClassNotFoundException {
    return getClasses(jarFileName, classPath);
  }
  
  public Class[] getClasses(String sourcePath, String classPath) throws ClassNotFoundException {
    this.jarFileName = sourcePath;
    String className = classPath.replace('.','/');
    loadClasses();
    Vector classesVect = new Vector();
    Enumeration e = jarContents.keys();
    while (e.hasMoreElements()) {
      String name = (String)e.nextElement();
      if (name.endsWith(".class") && name.startsWith(className)) {
        StringBuffer sb = new StringBuffer(name.replace('/', '.'));
        sb.delete(sb.indexOf(".class"), sb.length());
        byte[] classBytes = (byte[])jarContents.get(name);
        try {
          Class cls = defineClass(sb.toString(), classBytes, 0, classBytes.length);
          classesVect.add(cls);
        } catch(Exception ex) {
          ex.printStackTrace();
          return null;
        } 
      }
    }
    Class[] classes = new Class[classesVect.size()];
    for (int i = 0; i < classes.length; i++)
      classes[i] = (Class)classesVect.get(i);
    return classes;
  }  
}
