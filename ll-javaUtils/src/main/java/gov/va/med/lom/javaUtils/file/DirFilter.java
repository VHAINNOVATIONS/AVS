package gov.va.med.lom.javaUtils.file;

import java.io.File;
import java.io.FilenameFilter;

public class DirFilter implements FilenameFilter {
  private String afn;

  DirFilter(String afn) {
    this.afn = afn;
  }

  // strip path information
  public boolean accept(File dir, String name) {
    String f = new File(name).getName();
    return f.indexOf(afn) != -1;
  }

}