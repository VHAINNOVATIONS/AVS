package gov.va.med.lom.javaUtils.crypto;

import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;

public class Crypto {

  private static String algorithm = "DES";
  private static SecretKey key = null;
  private static final int ENCRYPT = 0;
  private static final int DECRYPT = 1;
  private Cipher cipher = null;

  public Crypto() throws Exception {
    cipher = Cipher.getInstance(algorithm);
    key = KeyGenerator.getInstance(algorithm).generateKey();
  }

  public Crypto(String keyFile) throws Exception {
    cipher = Cipher.getInstance(algorithm);
    key = readKey(keyFile);
  }

  public byte[] encrypt(String input) throws InvalidKeyException, 
                                             BadPaddingException,
                                             IllegalBlockSizeException {
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] inputBytes = input.getBytes();
    return cipher.doFinal(inputBytes);
  }

  public String decrypt(byte[] encryptionBytes) throws InvalidKeyException, 
                                                       BadPaddingException,
                                                       IllegalBlockSizeException {
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
    return new String(recoveredBytes);
  }

  public String readEncrypted(String filename) throws InvalidKeyException, 
                                                      FileNotFoundException,
                                                      IOException {
    cipher.init(Cipher.DECRYPT_MODE, key); 
    CipherInputStream in = new CipherInputStream(new FileInputStream(filename), cipher);
    byte[] inputBytes = new byte[in.available()];
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    StringBuffer input = new StringBuffer();
    String line;
    while((line = br.readLine()) != null) {
      input.append(line + "\n");
    }
    br.close();
    return input.toString();
  }

  public void writeEncrypted(String output, String filename) throws InvalidKeyException, 
                                                                    FileNotFoundException,
                                                                    IOException {
    cipher.init(Cipher.ENCRYPT_MODE, key);
    CipherOutputStream out = new CipherOutputStream(new FileOutputStream(filename), cipher);
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
    pw.println(output);
    pw.flush();
    pw.close();
  }

  public String readUnencrypted(String filename) throws FileNotFoundException,
                                                        IOException {
    StringBuffer input = new StringBuffer();
    BufferedReader in = new BufferedReader(new FileReader(filename));
    String line;
    while((line = in.readLine()) != null) {
      input.append(line + "\n");
    }
    in.close();
    return input.toString();
  }

  public void writeUnencrypted(String output, String filename) throws FileNotFoundException,
                                                                      IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(filename));
    out.write(output);
    out.flush();
    out.close();
  }
 
  public void writeKey(String filename) throws InvalidKeySpecException, 
                                               NoSuchAlgorithmException, 
                                               ClassNotFoundException, 
                                               FileNotFoundException,
                                               IOException {
    SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
    Class spec = Class.forName("javax.crypto.spec.DESKeySpec");
    DESKeySpec ks = (DESKeySpec)skf.getKeySpec(key, spec);
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
    oos.writeObject(ks.getKey());
  }

  public SecretKey readKey(String filename) throws InvalidKeyException,  
                                                   InvalidKeySpecException, 
                                                   NoSuchAlgorithmException, 
                                                   ClassNotFoundException, 
                                                   FileNotFoundException,
                                                   IOException {
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
    DESKeySpec ks = new DESKeySpec((byte[]) ois.readObject());
    SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
    return skf.generateSecret(ks);
  }

  public static void printUsage() {
    System.out.println("Crypto Usage:\n");
    System.out.println("-genkey keyfile");
    System.out.println("-encrypt infile outfile [keyfile]");
    System.out.println("-decrypt infile outfile [keyfile]");
  }

  public static void main(String[] args) throws Exception {
    Crypto crypto = null;
    if (args.length == 2) {
      if (args[0].equals("-genkey")) {
        crypto = new Crypto();  
        crypto.writeKey(args[1]);
      }
    } else if ((args.length == 3) || (args.length == 4)) {
      if (args.length == 3)
        crypto = new Crypto();  
      else
        crypto = new Crypto(args[3]);  
      String input = null;
      if (args[0].equals("-decrypt")) {
        // read in encrypted file and write to unencrypted file
        input = crypto.readEncrypted(args[1]);
        crypto.writeUnencrypted(input, args[2]);
      } else if (args[0].equals("-encrypt")) {
        // read in unencrypted file and write to encrypted file
        input = crypto.readUnencrypted(args[1]);
        crypto.writeEncrypted(input, args[2]);
      } else 
        Crypto.printUsage();
    } else {
      Crypto.printUsage();
    }
  }
}
