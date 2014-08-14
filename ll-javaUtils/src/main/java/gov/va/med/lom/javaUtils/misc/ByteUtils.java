package gov.va.med.lom.javaUtils.misc;

import java.io.*;

public class ByteUtils {

  public static byte[] getFileBytes(String filename) throws IOException  {
    FileInputStream fis = new FileInputStream(filename);
    BufferedInputStream bis = new BufferedInputStream(fis);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int ch;
    while ((ch = bis.read()) != -1) {
      baos.write(ch);
    }
    byte[] buffer = baos.toByteArray();  
    return buffer;  
  }

  public static byte[] getBytes(String str) throws IOException {
    StringReader sr = new StringReader(str);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int ch;
    while ((ch = sr.read()) != -1) {
      baos.write(ch);
    }
    byte[] buffer = baos.toByteArray();  
    return buffer;  
  }

  public static byte[] getBytes(String[] strArray) throws IOException {
    StringBuffer sb = new StringBuffer();
    for(int i = 0; i < strArray.length; i++) {
      sb.append(strArray[i]);
      if (i < strArray.length-1)
        sb.append(" ");
    }
    return getBytes(sb.toString());
  }

  public static String hexDigit(byte x) {
    StringBuffer sb = new StringBuffer();
    char c;
    // First nibble
    c = (char) ((x >> 4) & 0xf);
    if (c > 9) {
      c = (char) ((c - 10) + 'a');
    } else {
      c = (char) (c + '0');
    }
    sb.append (c);
    // Second nibble
    c = (char) (x & 0xf);
    if (c > 9) {
      c = (char)((c - 10) + 'a');
    } else {
      c = (char)(c + '0');
    }
    sb.append (c);
    return sb.toString();
  }


}