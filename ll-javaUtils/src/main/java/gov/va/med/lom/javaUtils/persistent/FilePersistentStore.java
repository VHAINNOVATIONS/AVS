package gov.va.med.lom.javaUtils.persistent;

import java.io.*;
import java.util.NoSuchElementException;
import java.lang.reflect.Constructor;

/*
 * File system implementation of PersistentStore.
 */
public class FilePersistentStore implements PersistentStore {

  private final String DEFAULT_DIR_PROPERTY = "user.dir";
  private final String FILE_SEPARATOR_PROPERTY = "file.separator";
  private final String SERIAL_SUFFIX = ".srl"; // suffix for serialized objects.

  /*
   * Inner class that implements keys enumeration for this object.
   */
  private class FilePersistentStoreKeys implements java.util.Enumeration {

    private String [] keys;
    private int idx = 0;

    /*
     * Constructor.
     */
    FilePersistentStoreKeys() {
      keys = new String[0];
    }

    /*
     * Constructor.
     */
    FilePersistentStoreKeys(String [] keys) {
      this.keys = keys;
    }


    /*
     * Tests if this enumeration contains more keys.
     */
    public boolean hasMoreElements() {
      return (idx < keys.length);
    }

    /*
     * Returns the next key of this enumeration.
     */
    public Object nextElement() throws NoSuchElementException {
      if (!hasMoreElements())
        throw new java.util.NoSuchElementException("no more elements");
      String k = keys[idx];
      idx++;
      return k;
    }
  }

  /*
   * Inner class that implements a filename filter.
   */
  private class FileStoreFilter implements FilenameFilter {
    private String suffix = "";
    FileStoreFilter(String suffix) {
        this.suffix = suffix;
    }
    public boolean accept(File dir, String name) {
        return name.endsWith(suffix);
    }
  }


  // Path separator used by operating system.
  private String fileSeparator;

  // Directory (pathname) where objects are to be stored/retrieved.  
  // The default is the current directory.
  private String storeDirectory;

  // The class loader used when reading in serialized data.
  private Constructor objectInputStreamConstructor;

  // The loader that should be used to load serialized data.
  private ClassLoader loader;

  /*
   * Public constructor.  Sets the storeDirectory,the directory (repository) where objects
   * are stored/retreived to /tmp.
   */
  public FilePersistentStore()
    throws PersistentStoreException {
    init(null, null);
  }

  /*
   * Public constructor.  Sets the storeDirectory, the directory (repository) where objects
   * are stored/retreived to /tmp.
   */
  public FilePersistentStore(ClassLoader loader)
    throws IOException, SecurityException, PersistentStoreException {
    init(null, loader);
  }

  /*
   * Public constructor that allows one to specify
   * the directory (repository)
   * where objects are stored/retrieved.
   * If the specified directory doesn't exist then it is created.
   */
  public FilePersistentStore(String storeDirectory)
    throws PersistentStoreException {
    init(storeDirectory, null);
  }

  /*
   * Public constructor that allows one to specify the directory (repository)
   * where objects are stored/retrieved. If the specified directory doesn't exist then it is created.
   */
  public FilePersistentStore(String storeDirectory, ClassLoader loader)
    throws PersistentStoreException {
    init(storeDirectory, loader);
  }

  /*
   * Gets a reference to the object input stream constructor.
   */
  private void init(String storeDirectory, ClassLoader loader) throws PersistentStoreException {
    try {
      this.loader = loader;
      fileSeparator = System.getProperty(FILE_SEPARATOR_PROPERTY);
      if (storeDirectory == null) {
        setStoreDirectory(fileSeparator + "tmp");
      } else {
        setStoreDirectory(storeDirectory);
      }
    } catch (Exception e) {
      throw new PersistentStoreException(e);
    }
  }

  /*
   * Sets the location (directory) from where objects are
   * retrieved/archived.  If the specified directory doesn't
   * exist then it is created.
   */
  private void setStoreDirectory(String storeDirectory) throws IOException, SecurityException {
    File f = new File(storeDirectory);
    if (f.exists() && !f.isDirectory()) {
      throw new IOException(storeDirectory + " already exists but is not a directory.");
    }
    if (!f.exists() && !f.mkdirs()) {
      throw new IOException("Unable to create directory " + storeDirectory);
    }
    this.storeDirectory = storeDirectory;
  }

  /*
   * Returns the location where objects are stored/retrieved.
   */
  public String getStoreDirectory() {
    return this.storeDirectory;
  }

  /*
   * Method that calculates the absolute path to the stored object.
   */
  private String filename(String key) {
    return storeDirectory + fileSeparator + convertKeyToHex(key) + SERIAL_SUFFIX;
  }

  /*
   * Hexadecimal characters corresponding to each half byte value.
   */
  private static final char[] HexChars = {
  '0', '1', '2', '3', '4', '5', '6', '7',
  '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


  /*
   * Converts an arbitrary string to ASCII hexadecimal string
   * form, with two hex characters corresponding to each byte.  The
   * length of the resultant string in characters will be twice the
   * length of the original string.
   */
  private String convertKeyToHex(String key) {
    byte [] bytes = key.getBytes();
    StringBuffer sb = new StringBuffer();
    int i;
    for (i=0; i < bytes.length; i++) {
	    sb.append(HexChars[(bytes[i] >> 4) & 0xf]);
	    sb.append(HexChars[bytes[i] & 0xf]);
    }
    return new String(sb);
  }

  /*
   * Converts an arbitrary ASCII hexadecimal string,
   * with two hex characters corresponding to each byte, into
   * a string.
   */
  private String convertHexToKey(String hex) {
    char [] chars = new char[hex.length()];
    hex.getChars(0, hex.length(), chars, 0);
    StringBuffer sb = new StringBuffer();
    for (int i=0; i<chars.length/2; i++) {
      int idx = 2*i;
      sb.append((char)((hexCharToByte(chars[idx])<<4) + (hexCharToByte(chars[idx+1]))));
    }
    return new String(sb);
  }

  /*
   * Method that maps a hexadecimal character to a byte value.
   */
  private byte hexCharToByte(char c) {
    switch (c) {
    case '1':
        return (byte)1;
    case '2':
        return (byte)2;
    case '3':
        return (byte)3;
    case '4':
        return (byte)4;
    case '5':
        return (byte)5;
    case '6':
        return (byte)6;
    case '7':
        return (byte)7;
    case '8':
        return (byte)8;
    case '9':
        return (byte)9;
    case 'a':
    case 'A':
        return (byte)10;
    case 'b':
    case 'B':
        return (byte)11;
    case 'c':
    case 'C':
        return (byte)12;
    case 'd':
    case 'D':
        return (byte)13;
    case 'e':
    case 'E':
        return (byte)14;
    case 'f':
    case 'F':
        return (byte)15;
    case '0':
    default:
        return (byte)0;
    }
  }

  /*
   * Method to store and object (persistent).
   */
  public void store(String key, Serializable obj)
    throws PersistentStoreException {
    if (exists(key))
      throw new PersistentStoreException("An object is already stored as " + key);
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(filename(key));
      ObjectOutputStream objOut = new ObjectOutputStream(out);
      objOut.writeObject(obj);
      objOut.flush();
      out.close();
    }
    catch (Exception ex) {
      if (out != null) {
        try { out.close(); } catch (Exception e) {}
      }
      delete(key);
      throw new PersistentStoreException(ex);
    }
  }
  
  /*
   * Method to retrieve a stored object.
   */
  public Object retrieve(String key) throws PersistentStoreException {
    if (!exists(key)) {
      return null;
    }
    try {
      FileInputStream in = new FileInputStream(filename(key));
      LoaderObjectInputStream objIn = new LoaderObjectInputStream(in, loader);
      Object obj = objIn.readObject();
      objIn.close();
      return obj;
    }
    catch (Exception ex) {
      throw new PersistentStoreException(ex);
    }
  }

  /*
   * Method to query if an an object is stored.
   */
  public boolean exists(String key)
    throws PersistentStoreException {
    File f = new File(filename(key));
    return f.exists();
  }

  /*
   * Method to simultaneously retrieve and remove an
   * object from persistent store.  If an object is not
   * stored under key, then null is returned.
   */
  public Object remove(String key) throws PersistentStoreException {
    Object obj = retrieve(key);
    delete(key);
    return obj;
  }

  /*
   * Method to delete a a key.  Any objects stored under
   * key are also removed.  If key is not defined, then
   * this method does nothing.
   */
  public void delete(String key) {
    File f = new File(filename(key));
    if (f.exists()) {
      f.delete();
    }
  }

  /*
   * Method that returns an enumration of the keys
   * of this persistent store.
   */
  public java.util.Enumeration keys() throws PersistentStoreException {
    File dir = new File(storeDirectory);
    FileStoreFilter filter = new FileStoreFilter(SERIAL_SUFFIX);
    String [] hexKeys = dir.list(filter);
    String [] keys = new String [hexKeys.length];
    for (int idx=0; idx<hexKeys.length; idx++) {
      // Strip off the suffix
      int suffixIdx = hexKeys[idx].lastIndexOf(SERIAL_SUFFIX);
      if (suffixIdx >= 0)
        hexKeys[idx] = hexKeys[idx].substring(0, suffixIdx);
        // Get the real key name
      keys[idx] = convertHexToKey(hexKeys[idx]);
    }
    return new FilePersistentStoreKeys(keys);
  }
}

// Internal class that can load objects with the load we specify.
class LoaderObjectInputStream extends ObjectInputStream {
  private ClassLoader loader;
  public LoaderObjectInputStream(InputStream in, ClassLoader loader) throws IOException, StreamCorruptedException {
    super(in);
    this.loader = loader;
  }

  /*
   * Subclasses may implement this method to allow classes to be
   * fetched from an alternate source. 
   *
   * The corresponding method in ObjectOutputStream is
   * annotateClass.  This method will be invoked only once for each
   * unique class in the stream.  This method can be implemented by
   * subclasses to use an alternate loading mechanism but must
   * return a Class object.  Once returned, the serialVersionUID of the
   * class is compared to the serialVersionUID of the serialized class.
   * If there is a mismatch, the deserialization fails and an exception
   * is raised. 
   *
   * By default the class name is resolved relative to the class
   * that called readObject. 
   */
  protected Class resolveClass(ObjectStreamClass v) throws IOException, ClassNotFoundException {
    if (loader != null) {
      return loader.loadClass(v.getName());
    } else {
      return super.resolveClass(v);
    }
  }

}
