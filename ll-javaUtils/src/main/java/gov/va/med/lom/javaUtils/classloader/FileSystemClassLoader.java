package gov.va.med.lom.javaUtils.classloader;

import java.io.*;
import java.net.URL;
import java.util.Vector;

public class FileSystemClassLoader extends ClassLoader implements ClassLoaderStrategy {

  private String root = null;

  /*
   * Default constructor uses the home directory of the JDK as its
   * root in the filesystem.
   */
  public FileSystemClassLoader() throws FileNotFoundException {
    this(FileSystemClassLoader.class.getClassLoader(),
         System.getProperties().getProperty("java.home"));
  }

  /*
   * Constructor taking a String indicating the point on the local
   * filesystem to take as the root in the filesystem.
   */
  public FileSystemClassLoader(String root) throws FileNotFoundException {
    this(FileSystemClassLoader.class.getClassLoader(),root);
  }

  /*
   * Default constructor uses the home directory of the JDK as its
   * root in the filesystem.
   */
  public FileSystemClassLoader(ClassLoader parent) throws FileNotFoundException {
    this(parent, System.getProperties().getProperty("java.home"));
  }

  /*
   * Constructor taking a String indicating the point on the local
   * filesystem to take as the root in the filesystem.
   */
  public FileSystemClassLoader(ClassLoader parent, String root) throws FileNotFoundException {
    // Ensure we defer to parent appropriately
    //
    super(parent);

    // Test to make sure root is a legitimate directory on the
    // local filesystem
    //
    File f = new File(root);
    if (f.isDirectory())
      this.root = root;
    else
      throw new FileNotFoundException();
  }

  /*
   * Return byte array (which will be turned into a Class instance
   * via ClassLoader.defineClass) for class
   */
  public byte[] findClassBytes(String className) {
    try {
      // Assume that 'name' follows standard Java
      // package-to-directory naming conventions, where each
      // "." represents a directory separator character
      // (backslash on Windows, slash on Unix, colon on Mac).
      //
      String pathName = root + File.separatorChar +
          className.replace('.', File.separatorChar) +
          ".class";

      // Try to open the file and read in its contents
      //
      FileInputStream inFile =
          new FileInputStream(pathName);
      byte[] classBytes = new byte[inFile.available()];
      inFile.read(classBytes);

      return classBytes;
    } catch (java.io.IOException ioEx) {
      return null;
    }
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
   * Attempt to find the bytecode given for the class name from a
   * file on disk. Will not look along CLASSPATH, nor in .jar files
   */
  public Class findClass(String name) throws ClassNotFoundException {
    byte[] classBytes = findClassBytes(name);

    if (classBytes==null) {
      throw new ClassNotFoundException();
    }
    else {
      return defineClass(name, classBytes, 0, classBytes.length);
    }
  }

  public String[] getClassNames() {
    File path = new File(root);
    String[] fileNames = path.list(new DirFilter(".class"));
    return fileNames;
  }

  public Class[] getClasses(String classPath) throws ClassNotFoundException {
    return getClasses(root, classPath);
  }
  
  public Class[] getClasses(String sourcePath, String classPath) {
    Vector classesVect = new Vector();
    File path = new File(sourcePath);
    String[] fileNames = path.list(new DirFilter(".class"));
    if (fileNames == null)
      return new Class[] {};
    byte[] classBytes = null;
    for (int i = 0; i < fileNames.length; i++) {
      StringBuffer sb = new StringBuffer(fileNames[i]);
      sb.delete(sb.indexOf(".class"), sb.length());  
      String filename = sourcePath + String.valueOf(File.separatorChar) + fileNames[i];
      File file = new File(filename);
      if (file.exists()) {
        try {
          FileInputStream inFile = new FileInputStream(file);
          classBytes = new byte[inFile.available()];
          inFile.read(classBytes);
          try {
            Class c = findLoadedClass(classPath + '.' + sb.toString());
            if(c == null) {
              c = defineClass(classPath + '.' + sb.toString(), classBytes, 0, classBytes.length);
            }
            classesVect.add(c);
          } catch(NoClassDefFoundError e) {}
        } catch (java.io.IOException ioEx) {}        
      }
    }
    Class[] classes = new Class[classesVect.size()];
    for (int i = 0; i < classesVect.size(); i++)
      classes[i] = (Class)classesVect.get(i);
    return classes;
  }  

  class DirFilter implements FilenameFilter {
    String afn;
    DirFilter(String afn) {
      this.afn = afn;
    }
    // strip path information
    public boolean accept(File dir, String name) {
      String f = new File(name).getName();
      return f.indexOf(afn) != -1;
    }
  }

  // Test driver
  public static void main(String[] args) throws Exception {
    FileSystemClassLoader fscl = new FileSystemClassLoader(args[0]);
    Class[] classes = fscl.getClasses(args[1]);
    for (int i = 0; i < classes.length; i++)
      System.out.println(classes[i]);
  }
}