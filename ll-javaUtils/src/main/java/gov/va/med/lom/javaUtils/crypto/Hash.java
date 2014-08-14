package gov.va.med.lom.javaUtils.crypto;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import gov.va.med.lom.javaUtils.misc.ByteUtils;

public class Hash {

  public static String getHash(byte[] buffer, 
                               String alg) throws NoSuchAlgorithmException  {
    MessageDigest algorithm = MessageDigest.getInstance(alg);
    return computeHash(algorithm, buffer);    
  }
  
  public static String getHash(String filename, 
                               String alg) throws IOException,
                                                  NoSuchAlgorithmException  {
    byte[] buffer = ByteUtils.getFileBytes(filename);
    return getHash(buffer, alg);
  }


  public static String getSHAHash(byte[] buffer) throws NoSuchAlgorithmException  {
    return getHash(buffer, "SHA-1");    
  }
  
  public static String getSHAHash(String filename) throws IOException,
                                                          NoSuchAlgorithmException  {
    byte[] buffer = ByteUtils.getFileBytes(filename);
    return getSHAHash(buffer);
  }

  public static String getMD5Hash(byte[] buffer) throws IOException,
                                                        NoSuchAlgorithmException {
    return getHash(buffer, "MD5");    
  }

  public static String getMD5Hash(String filename) throws IOException,
                                                          NoSuchAlgorithmException  {
    byte[] buffer = ByteUtils.getFileBytes(filename);
    return getMD5Hash(buffer);
  }

  private static String computeHash(MessageDigest algorithm,
                                    byte[] buffer) throws NoSuchAlgorithmException  {
    algorithm.reset();
    algorithm.update(buffer);
    byte digest[] = algorithm.digest();
    StringBuffer hexString = new StringBuffer();
    for (int i=0;i<digest.length;i++) {
      hexString.append(ByteUtils.hexDigit(digest[i]));
    }
    return hexString.toString();
  }

  public static void main(String args[]) throws Exception {
    if (args.length == 0) {
      System.err.println("java Hash [-a algorithm] [file]...");
      System.exit(-1);
    }
    int len = args.length;
    String alg = null;
    int i = 0;
    try {
      if (args[i].equals("-a")) {
        alg = args[++i];
        i++;
      }
      for (int j = i; j < len; j++) {
        String filename = args[j];
        String digest = null;
        if (alg != null)
          digest = getHash(filename, alg);
        else
          digest = getMD5Hash(filename);
        System.out.println(digest + "  " + filename);
      }
    } catch (NoSuchAlgorithmException nsae) {
      System.err.println("Invalid algorithm: " + nsae.getMessage());
      System.exit(-2);
    } catch (IOException ioe) {
      System.err.println("File error: " + ioe.getMessage());
    }
  }
}