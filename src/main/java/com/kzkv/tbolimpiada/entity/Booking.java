package com.kzkv.tbolimpiada.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import com.kzkv.tbolimpiada.entity.converter.EncryptionConverter;

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

	@OneToOne
	private Ticket ticket;

	@Convert(converter = EncryptionConverter.class)
	private String phone;

	@Convert(converter = EncryptionConverter.class)
	private String email;

}
