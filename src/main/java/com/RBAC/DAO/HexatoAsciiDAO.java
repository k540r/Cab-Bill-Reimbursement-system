package com.RBAC.DAO;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;

public interface HexatoAsciiDAO {

	public String hexToAscii(String hexStr);
	public  String asciiToHex(String asciiValue);
	public Cipher EncryptionSHA256Algo(String enckey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException ;
	
	public String decrypt(String strToDecrypt, String enckey,HttpSession session);
	public String getAlphaNumericString();

	public String decrypt(String strToDecrypt, String secret);

	public String decrypt_new(String strToDecrypt);


}
