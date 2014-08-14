package	gov.va.med.lom.javaUtils.file;

import	java.io.*;

/*
 * Class for creating and manage a temporary directory.
 */

class TempDir {
  File dir;

  /* dirPath is the path of directory to generate the temporary file in.
   * baseName is the prefix name of the directory.
   */
  public TempDir (String path, String baseName) throws IOException {
    int dirNum = 0;
     
    // Generate a unique directory.
    while (true) {
      dir = new File (path, baseName + "." + System.currentTimeMillis () +
                      "." + dirNum + ".tmp");
      if (dir.mkdirs ()) {
        break;
      }
      if (!dir.exists ()) {
        throw new IOException ("no permission to create directory " +dir.getAbsolutePath ());
      }
      dirNum++;
      if (dirNum >= 25) {
        throw new IOException ("failed to create tmp directory named in the " +
                               "form \"" + dir.getAbsolutePath () +
                               "\" after " + dirNum + " trys");
      }
    }
  }

 
  public File file () {
    return dir;
  }
  
  void recursiveDelete (File dirPath) {
    String [] ls = dirPath.list ();

    for (int idx = 0; idx < ls.length; idx++) {
      File file = new File (dirPath, ls [idx]);
      if (file.isDirectory ())
        recursiveDelete (file);
      file.delete ();
    }
  }


  public void delete () throws IOException {
    recursiveDelete (dir);
    if (dir.exists ()) {
      throw new IOException ("Unable to delete directory hierarchy \"" +
                             dir.getAbsolutePath () + "\"");
    }
  }

  public File mkdirs (String path) throws IOException {
    File newDir = new File (dir.getAbsolutePath (), path);
    newDir.mkdirs ();
    if (!newDir.exists ()) {
      throw new IOException ("Unable to create directory \"" +
                             dir.getAbsolutePath () + "\"");
    }
    return newDir;
  }
}
