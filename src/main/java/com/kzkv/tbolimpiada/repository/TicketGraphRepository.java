package com.kzkv.tbolimpiada.repository;

import com.kzkv.tbolimpiada.entity.TicketGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketGraphRepository extends JpaRepository<TicketGraph, UUID> {
}
