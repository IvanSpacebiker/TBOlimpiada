package com.kzkv.tbolimpiada.entity;

import com.kzkv.tbolimpiada.entity.converter.EncryptionConverter;
import jakarta.persistence.*;
import lombok.*;

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
