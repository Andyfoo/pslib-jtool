package com.pslib.jtool.security;

/** 
 * 概要:3DES加密算法，兼容PHP的解密 
 * java和php对等的3DES加密算法，ECB的加密模式没有CBC安全，iv是初始向量相当于种子。
 */

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.pslib.jtool.security.base64.Base64;

public class SecurityAes {
	private String KEY = "VK6Qbp4tVhDCiDK8"; // 长度16
	private String IV = "0102030405060708"; //

	/** array mapping hex value (0-15) to corresponding hex digit (0-9a-f). */
	private final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final String MCRYPT_TRIPLE = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	// private static final String TRANSFORMATION = "AES/ECB/NoPadding";

	public SecurityAes() {

	}

	public SecurityAes(String k) {
		setKey(k);
	}

	public void setKey(String k) {
		KEY = k;
	}

	public void setIV(String k) {
		IV = k;
	}

	private SecretKey getKey() throws Exception {
		return new SecretKeySpec(KEY.getBytes(), MCRYPT_TRIPLE);
	}

	public byte[] desEncrypt(byte[] plainText) throws Exception {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, getKey(), iv);
		return cipher.doFinal(plainText);
	}

	public byte[] desDecrypt(byte[] encryptText) throws Exception {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, getKey(), iv);
		return cipher.doFinal(encryptText);
	}

	public static byte[] generateSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keygen = KeyGenerator.getInstance(MCRYPT_TRIPLE);
		return keygen.generateKey().getEncoded();
	}

	public static byte[] randomIVBytes() {
		Random ran = new Random();
		byte[] bytes = new byte[8];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = (byte) (ran.nextInt(9) + 48);
		}
		System.out.println(new String(bytes));
		return bytes;
	}

	// 加密
	public byte[] encrypt(String input) throws Exception {
		return desEncrypt(input.getBytes());
	}

	// 解密
	public String decrypt(byte[] input) throws Exception {
		return new String(desDecrypt(input));
	}

	public byte[] decrypt_bin(String input) throws Exception {
		return desDecrypt(input.getBytes());
	}

	// 加密 base64
	public String encrypt_base64(String input) throws Exception {
		return base64Encode(desEncrypt(input.getBytes()));
	}

	// 解密base64
	public String decrypt_base64(String input) throws Exception {
		byte[] result = base64Decode(input);
		return new String(desDecrypt(result), "utf-8");
	}

	// 加密hex
	public String encrypt_hex(String input) throws Exception {
		return bin2hex(desEncrypt(input.getBytes()));
	}

	// 解密hex
	public String decrypt_hex(String input) throws Exception {
		byte[] result = hex2bin(input);
		return new String(desDecrypt(result));
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
			SecurityAes aes = new SecurityAes();
			String src = "中文123abc";

			aes.setKey("abcdefghabcdefgh");
			aes.setIV("1111222233334444");
			// des.setKey(new String(generateSecretKey()));
			System.out.println(aes.base64Encode(src.getBytes()));
			System.out.println(aes.encrypt_base64(src));
			System.out.println(aes.decrypt_base64(aes.encrypt_base64(src)));

			System.out.println(aes.encrypt_hex(src));
			System.out.println(aes.decrypt_hex(aes.encrypt_hex(src)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}