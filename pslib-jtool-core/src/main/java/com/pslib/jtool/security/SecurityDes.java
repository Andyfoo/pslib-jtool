package com.pslib.jtool.security;

/** 
 * 概要:DES加密算法，兼容PHP的解密 
 */

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.pslib.jtool.security.base64.Base64;

public class SecurityDes {
	private String KEY = "aDH3de22"; // key最长为8
	/** array mapping hex value (0-15) to corresponding hex digit (0-9a-f). */
	private final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public SecurityDes() {

	}

	public SecurityDes(String k) {
		setKey(k);
	}

	public void setKey(String k) {
		KEY = k;
	}

	public byte[] desEncrypt(byte[] plainText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(KEY.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data);
		return encryptedData;
	}

	public byte[] desDecrypt(byte[] encryptText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(KEY.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);
		byte encryptedData[] = encryptText;
		byte decryptedData[] = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	// 加密
	public byte[] encrypt(String input) throws Exception {
		return desEncrypt(input.getBytes());
	}

	// 解密
	public String decrypt(byte[] input) throws Exception {
		return new String(desDecrypt(input));
	}

	public byte[] decrypt_bin(String input) {
		try {
			return desDecrypt(input.getBytes());
		} catch (Exception e) {
			return "".getBytes();
		}
	}

	// 加密 base64
	public String encrypt_base64(String input) {
		try {
			return base64Encode(desEncrypt(input.getBytes()));
		} catch (Exception e) {
			return "";
		}
	}

	// 解密base64
	public String decrypt_base64(String input) {
		try {
			byte[] result = base64Decode(input);
			return new String(desDecrypt(result));
		} catch (Exception e) {
			return "";
		}
	}

	// 加密hex
	public String encrypt_hex(String input) {
		try {
			return bin2hex(desEncrypt(input.getBytes()));
		} catch (Exception e) {
			return "";
		}

	}

	// 解密hex
	public String decrypt_hex(String input) {
		try {
			byte[] result = hex2bin(input);
			return new String(desDecrypt(result));
		} catch (Exception e) {
			return "";
		}
	}

	public String bin2hex(byte[] ba) {
		int length = ba.length;
		char[] buf = new char[length * 2];
		for (int i = 0, j = 0, k; i < length;) {
			k = ba[i++];
			buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
			buf[j++] = HEX_DIGITS[k & 0x0F];
		}
		return new String(buf);
	}

	public byte[] hex2bin(String hex) {
		int len = hex.length();
		byte[] buf = new byte[((len + 1) / 2)];

		int i = 0, j = 0;
		if ((len % 2) == 1)
			buf[j++] = (byte) hexDigit(hex.charAt(i++));

		while (i < len) {
			buf[j++] = (byte) ((hexDigit(hex.charAt(i++)) << 4) | hexDigit(hex.charAt(i++)));
		}
		return buf;
	}

	public int hexDigit(char ch) {
		if (ch >= '0' && ch <= '9')
			return ch - '0';
		if (ch >= 'A' && ch <= 'F')
			return ch - 'A' + 10;
		if (ch >= 'a' && ch <= 'f')
			return ch - 'a' + 10;

		return (0); // any other char is treated as 0
	}

	public String base64Encode(byte[] s) {
		return new String(Base64.encode(s));
	}

	public byte[] base64Decode(String s) {
		return Base64.decode(s.getBytes());
	}

	public static void main(String args[]) {
		try {
			SecurityDes des = new SecurityDes();
			String src = "汉字aaaaa12汉字aaaaa12汉字aaaaa12汉字aaaaa12";
			des.setKey("abcdefgh");
			System.out.println(des.encrypt_base64(src));
			System.out.println(des.decrypt_base64(des.encrypt_base64(src)));

			System.out.println(des.encrypt_hex(src));
			System.out.println(des.decrypt_hex(des.encrypt_hex(src)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}