package gov.va.med.lom.javaUtils.crypto;

import java.math.BigInteger;
import javax.crypto.KeyGenerator;
import java.security.*;
import java.security.spec.*;
import javax.crypto.spec.*;

public class DSAKeyGen {

  private static String algorithm = "DES";
  private DSAPublicKeySpec dsaPublicKeySpec;
  private DSAPrivateKeySpec dsaPrivateKeySpec;
  private KeyGenerator kg;
  
  public DSAKeyGen() {
    try {
      kg = KeyGenerator.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  public void setDSAPublicKeySpec(BigInteger y, BigInteger p,
                                  BigInteger q, BigInteger g) {
    dsaPublicKeySpec = new DSAPublicKeySpec(y, p, q, g);
  }

  public void setDSAPrivateKeySpec(BigInteger x, BigInteger p,
                                   BigInteger q, BigInteger g) {
    dsaPrivateKeySpec = new DSAPrivateKeySpec(x, p, q, g);
  }

  public Key getKey() {
  	PublicKey pub = null; 
  	try {
	    KeyFactory kf = KeyFactory.getInstance("DSA");
      KeyPairGenerator kpg = java.security.KeyPairGenerator.getInstance("DSA");
      kpg.initialize(512);
      KeyPair kp = kpg.generateKeyPair();
      dsaPublicKeySpec = (DSAPublicKeySpec)kf.getKeySpec(kp.getPublic(), DSAPublicKeySpec.class);
	    pub = kf.generatePublic(dsaPublicKeySpec);
  	} catch(NoSuchAlgorithmException nsae) {
  		return null;
   	} catch(InvalidKeySpecException ikse) {
  		return null;
  	}
   	return pub;
  }

  public Key getSecretKey(String keyData) {
    return getSecretKey(keyData.getBytes());
  }

  public Key getSecretKey(byte[] keyData) {
    return new SecretKeySpec(keyData, algorithm);
  }

  public static void main(String[] args) {
  	DSAKeyGen keyGen = new DSAKeyGen();
    Key k = keyGen.getKey();
    System.out.println(k);
  }
}