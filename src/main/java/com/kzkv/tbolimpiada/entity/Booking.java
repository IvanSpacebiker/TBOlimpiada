package com.kzkv.tbolimpiada.entity;

import com.kzkv.tbolimpiada.entity.converter.EncryptionConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NonNull
	@OneToOne
	private Ticket ticket;

	@Convert(converter = EncryptionConverter.class)
	private String phone;

	@Convert(converter = EncryptionConverter.class)
	private String email;

}
