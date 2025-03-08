package com.kzkv.tbolimpiada.entity.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
class EncryptionConverterTest {

	@Test
	void testEncryptionDecryption() {
		EncryptionConverter converter = new EncryptionConverter() {
			@Override
			public String getSecretKeyBase64() {
				return Base64.getEncoder().encodeToString("mysecretkey12345".getBytes());
			}

			@Override
			public String getAlgorithm() {
				return "AES";
			}
		};

		String original = "SensitiveData123";

		String encrypted = converter.convertToDatabaseColumn(original);
		String decrypted = converter.convertToEntityAttribute(encrypted);

		assertThat(decrypted).isEqualTo(original);
		assertThat(encrypted).isNotEqualTo(original);
	}

	@Test
	void testEncryptionFailure() {
		EncryptionConverter invalidConverter = new EncryptionConverter() {
			@Override
			public String getSecretKeyBase64() {
				return "invalidkey";
			}

			@Override
			public String getAlgorithm() {
				return "AES";
			}
		};

		Throwable thrown = catchThrowable(() ->
				invalidConverter.convertToDatabaseColumn("test")
		);

		assertThat(thrown)
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Error encrypting data");
	}
}