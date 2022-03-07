package com.example.partner.demo.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;

public class EncryptDecryptUtils {

	private static String publicKeyData = "Publickey-fe74ee51-0a1a-44f6-96c5-bc2b1dbd7a54.pem";

	@Autowired
	public static String encryptMessage(String message) throws NoSuchAlgorithmException, InvalidKeySpecException,
			IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
		
		return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));

	}

	private static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		File file = new File("src/main/resources/" + publicKeyData);

		String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
		
		String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----\n", "").replace("-----END PUBLIC KEY-----", "");
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		byte[] decoded = Base64.getMimeDecoder().decode(publicKeyPEM);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
		
		return keyFactory.generatePublic(keySpec);
	}

}
