
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Encryptor {

	private static SecretKeySpec key;
	private static Cipher aes;
	private static boolean initiated = false;
	
	public static void encryptorInitiation() {

		String passphrase = "p2ona2sdfa3w9023rkadfz1";
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return; //Unreachable
		}
		digest.update(passphrase.getBytes());
		key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
		
		
		try {
			Encryptor.aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return; //Unreachable
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return; //Unreachable
		}
	}
	
	/**
	 * HashSHA256
	 * @param password the password that should be hashed.
	 * @return a hashed hex encoded version of the string, returns null in case of error.
	 */
	public static String hashSHA256(String password) {
		if (!initiated) {
			Encryptor.encryptorInitiation();
			initiated = true;
		}
        try {
        	MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
 
			byte byteData[] = md.digest();
			return bytesToHex(byteData);
        } catch (NoSuchAlgorithmException e) {
        	System.out.println("Error: " + e);
        	return null;
        }
	}

	//TODO Make sense of this function
   private static String bytesToHex(byte[] b) {
      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      StringBuffer buf = new StringBuffer();
      for (int j=0; j<b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();
   }
	
	public static byte[] encrypt(String cleartext) {
		if (!initiated) {
			Encryptor.encryptorInitiation();
			initiated = true;
		}
		try {
			Encryptor.aes.init(Cipher.ENCRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null; //Unreachable
		}
		byte[] ciphertext;
		try {
			ciphertext = Encryptor.aes.doFinal(cleartext.getBytes());
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null; //Unreachable
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null; //Unreachable
		}
		return ciphertext;
	}
	
	public static String decrypt(byte[] ciphertext) {
		if (!initiated) {
			Encryptor.encryptorInitiation();
			initiated = true;
		}
		try {
			Encryptor.aes.init(Cipher.DECRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null; //Unreachable
		}
		String cleartext;
		try {
			cleartext = new String(Encryptor.aes.doFinal(ciphertext));
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null; //Unreachable
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null; //Unreachable
		}
		return cleartext;
		
	}
	
}
