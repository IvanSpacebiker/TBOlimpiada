package com.kzkv.tbolimpiada.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
@RequiredArgsConstructor
@Getter
public class EncryptionConverter implements AttributeConverter<String, String> {

	@Value("${encryption.secret}")
	private String secretKeyBase64;

	@Value("${encryption.algorithm}")
	private String algorithm;

	public SecretKey getSecretKey() {
		byte[] decodedKey = Base64.getDecoder().decode(getSecretKeyBase64());
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, getAlgorithm());
	}

	@Override
	public String convertToDatabaseColumn(String attribute) {
		try {
			Cipher cipher = Cipher.getInstance(getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
			byte[] encryptedBytes = cipher.doFinal(attribute.getBytes());
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException("Error encrypting data", e);
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		try {
			Cipher cipher = Cipher.getInstance(getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
			byte[] decodedBytes = Base64.getDecoder().decode(dbData);
			return new String(cipher.doFinal(decodedBytes));
		} catch (Exception e) {
			throw new RuntimeException("Error decrypting data", e);
		}
	}
}