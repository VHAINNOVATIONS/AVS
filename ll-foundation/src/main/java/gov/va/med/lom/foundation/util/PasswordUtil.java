package gov.va.med.lom.foundation.util;

import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides temporary password generation and other utilities for users.
 */
public class PasswordUtil {
	private static final Log logger = LogFactory.getLog(PasswordUtil.class);
	/**
	 * required minimum length of the password *
	 */
	private static final int PASSWORD_LENGTH = 8;

	/**
	 * list of English alphabets
	 */
	private static final char[] LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g',
	                                       'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
	                                       'v', 'w', 'x', 'y', 'z'};

	/**
	 * list of special characters to be included in the password
	 */
	private static final char[] SPECIAL_CHARS = {'!', '@', '#', '$',
	                                             '%', '&', '*', '|', '?'};

	/**
	 * constant number to be used as modulus to figure out whether to capitalize
	 * the letter or not. It is also used to select an alphabetic letter, a number,
	 * or a special character.
	 */
	private static final int MODULUS = 2;

	/**
	 * Generates a password for user following the algorithm below:
	 * <ol><li>total length of the password = 8
	 * <li>At least one numeric character
	 * <li>At least one alphabetic character
	 * <li>At least one special character
	 * </ol>
	 *
	 * @return String Generated password in clear text.
	 */
	public static String generateUserPassword() {
		boolean digit = false;
		boolean alpha = false;
		boolean special = false;
		int mode = new Double(Math.random() * 10).intValue();

		StringBuffer sb = new StringBuffer(PASSWORD_LENGTH + 1);
		for(int i = 0; i < PASSWORD_LENGTH - 1; i++) {
			mode = mode % MODULUS;
			if(mode == 0) {
				sb.append(getRandomLetter());
				alpha = true;
			}
			else {
				sb.append(getRandomDigit());
				digit = true;
			}
		}

		if(alpha == false) {
			sb.append(getRandomLetter());
		}
		else if(digit == false) {
			sb.append(getRandomDigit());
		}

		if(special == false) {
			sb.append(getRandomSpecialChar());
		}

		return sb.toString();
	}

	public final static String digest(String credentials, String algorithm) {
		return digest(credentials, algorithm, null);
	}

	public final static String digest(String credentials, String algorithm,
	                                  String encoding) {

		try {
			// Obtain a new message digest with "digest" encryption
			MessageDigest md =
			    (MessageDigest)MessageDigest.getInstance(algorithm).clone();

			// encode the credentials
			// Should use the digestEncoding, but that's not a static field
			if(encoding == null) {
				md.update(credentials.getBytes());
			}
			else {
				md.update(credentials.getBytes(encoding));
			}

			// Digest the credentials and return as hexadecimal
			return convert(md.digest());
		}
		catch(Exception ex) {
			logger.error(ex);
			return credentials;
		}
	}

	public static final boolean isValidPassword(String clearTextPassword, String hashedPassword, String algorithm) {
		String hash = digest(clearTextPassword, algorithm);
		return hashedPassword.equals(hash);
	}

	/**
	 * Convert a byte array into a printable format containing a
	 * String of hexadecimal digit characters (two per byte).
	 *
	 * @param bytes Byte array representation
	 */
	public static String convert(byte bytes[]) {

		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for(int i = 0; i < bytes.length; i++) {
			sb.append(convertDigit((int)(bytes[i] >> 4)));
			sb.append(convertDigit((int)(bytes[i] & 0x0f)));
		}
		return (sb.toString());

	}

	/*************** private methods. *****************/
	/**
	 * [Private] Convert the specified value (0 .. 15) to the corresponding
	 * hexadecimal digit.
	 *
	 * @param value Value to be converted
	 */
	private static char convertDigit(int value) {

		value &= 0x0f;
		if(value >= 10)
			return ((char)(value - 10 + 'a'));
		else
			return ((char)(value + '0'));

	}

	/**
	 * Generates a random alphabetic letter
	 *
	 * @return Char Randomly selected character.
	 */
	private static char getRandomLetter() {
		int index = new Double(Math.random() * LETTERS.length).intValue();
		char randChar = LETTERS[index];
		return (index % MODULUS == 0) ? Character.toUpperCase(randChar) : randChar;
	}

	/**
	 * Generates a random integral number
	 *
	 * @return int The value of the randomly generated number.
	 */
	private static int getRandomDigit() {
		int num = new Double(Math.random() * 10).intValue();
		return num;
	}

	/**
	 * Selects a randon secial character from the predefined list
	 *
	 * @return char Randomly selected special character
	 */
	private static char getRandomSpecialChar() {
		int specialIndex = new Double(Math.random() * SPECIAL_CHARS.length).intValue();
		char specialChar = SPECIAL_CHARS[specialIndex];
		return specialChar;
	}

	/**
	 * The test driver for generating username and password.
	 *
	 * @param args Command-line arguments
	 */
	public static void main(String[] args) {
		// generate 10 random passwords
		int numPasswords = 10;
		for(int i = 0; i < numPasswords; i++) {
			logger.info("Generated Password[" + (i + 1) + "]: " + PasswordUtil.generateUserPassword());
		}

	}
}
