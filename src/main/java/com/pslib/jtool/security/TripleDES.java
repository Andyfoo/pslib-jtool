package com.pslib.jtool.security;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.pslib.jtool.security.base64.Base64;

public class TripleDES {
	static {
		// Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}

	private static final String MCRYPT_TRIPLEDES = "DESede";
	private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";

	public static byte[] decrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES);
		SecretKey sec = keyFactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec IvParameters = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);
		return cipher.doFinal(data);
	}

	public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES);
		SecretKey sec = keyFactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec IvParameters = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, sec, IvParameters);
		return cipher.doFinal(data);
	}

	public static byte[] generateSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keygen = KeyGenerator.getInstance(MCRYPT_TRIPLEDES);
		return keygen.generateKey().getEncoded();
	}

	public static byte[] randomIVBytes() {
		Random ran = new Random();
		byte[] bytes = new byte[8];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = (byte) ran.nextInt(Byte.MAX_VALUE + 1);
		}
		return bytes;
	}

	public static String base64Encode(byte[] s) {
		return new String(Base64.encode(s));
	}

	public static void main(String args[]) throws Exception {
		String plainText = "汉字aaaaa12汉字aaaaa12汉字aaaaa12汉字aaaaa12";
		final byte[] secretBytes = "abcdefghabcdefghabcdefgh".getBytes();// TripleDES.generateSecretKey();

		final byte[] ivbytes = TripleDES.randomIVBytes();
		System.out.println("plain text: " + plainText);
		byte[] encrypt = TripleDES.encrypt(plainText.getBytes(), secretBytes, ivbytes);
		System.out.println("cipher text: " + base64Encode(encrypt));
		System.out.println("decrypt text: " + new String(TripleDES.decrypt(encrypt, secretBytes, ivbytes), "UTF-8"));
	}

}