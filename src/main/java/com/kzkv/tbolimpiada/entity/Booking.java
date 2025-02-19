package com.kzkv.tbolimpiada.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
public class Booking {

	@Id
	private UUID id;
	private UUID routeId;
	private String phone;
	private String email;

}
