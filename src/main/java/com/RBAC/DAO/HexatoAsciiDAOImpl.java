package com.RBAC.DAO;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class HexatoAsciiDAOImpl implements HexatoAsciiDAO {

	@Override
	public String hexToAscii(String hexStr) {
	    StringBuilder output = new StringBuilder("");
	    for (int i = 0; i < hexStr.length(); i += 2) {
	        String str = hexStr.substring(i, i + 2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	    return output.toString();
	}

	@Override
	public String asciiToHex(String asciiValue)
	{
	    char[] chars = asciiValue.toCharArray();
	    StringBuffer hex = new StringBuffer();
	    for (char element : chars) {
	        hex.append(Integer.toHexString(element));
	    }
	    return hex.toString();
	}


	@Override
	public Cipher EncryptionSHA256Algo(String enckey)
	{
	    try
	    {
	    	String KeySpec="dc0da04af8fee58593442bf834b30739";
	    	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        KeySpec spec = new PBEKeySpec(enckey.toCharArray(), hex(KeySpec),1000,256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey,  new IvParameterSpec(hex(KeySpec)));
	        return cipher;
	     }
	    catch (Exception e)
	    {
	        System.out.println("Error while encrypting: " + e.toString());
	    }
	    return null;
	}

	@Override
	public String decrypt(String strToDecrypt, String enckey,HttpSession session) {
	    try
	    {
	    	String KeySpec = session.getAttribute("KeySpec").toString();
	    	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); //PBKDF2WithHmacSHA256
	        KeySpec spec = new PBEKeySpec(enckey.toCharArray(), hex(KeySpec),1000,256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, secretKey,  new IvParameterSpec(hex(KeySpec)));

	        return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
	    }
	    catch (Exception e) {
	        System.out.println("Error while decrypting: " + e.getMessage());
	    }
	    return null;
	}

	public static byte[] hex(String str) {
		try {
            return Hex.decodeHex(str.toCharArray());
        }
        catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

	@Override
	public String getAlphaNumericString()
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }


	@Override
	public String decrypt(String strToDecrypt, String enckey) {
	    try
	    {
	    	String KeySpec = "dc0da04af8fee58593442bf834b30739";//session.getAttribute("KeySpec").toString();
	    	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        KeySpec spec = new PBEKeySpec(enckey.toCharArray(), hex(KeySpec),1000,256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, secretKey,  new IvParameterSpec(hex(KeySpec)));
	        return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
	    }
	    catch (Exception e) {
	        System.out.println("Error while decrypting: " + e.toString());
	    }
	    return null;
	}


	@Override
	public String decrypt_new(String strToDecrypt) {
	    try
	    {
	    	String KeySpec = "dc0da04af8fee58593442bf834b30739";//session.getAttribute("KeySpec").toString();
	    	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        KeySpec spec = new PBEKeySpec(strToDecrypt.toCharArray(), hex(KeySpec),1000,256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, secretKey,  new IvParameterSpec(hex(KeySpec)));
	    	return new String(Base64.decodeBase64(strToDecrypt));
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	        System.out.println("Error while decrypting: " + e.getMessage());
	    }
	    return null;
	}
}