package com.kzkv.tbolimpiada.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private UUID ticketId;
	private String phone;
	private String email;

}
