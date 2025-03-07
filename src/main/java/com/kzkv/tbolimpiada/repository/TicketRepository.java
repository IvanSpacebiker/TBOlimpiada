package com.kzkv.tbolimpiada.repository;

import com.kzkv.tbolimpiada.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID>, JpaSpecificationExecutor<Ticket> {
	@Query("SELECT DISTINCT t.departure FROM Ticket t ORDER BY t.departure")
	List<String> findUniqueDepartures();

	@Query("SELECT DISTINCT t.arrival FROM Ticket t ORDER BY t.arrival")
	List<String> findUniqueArrivals();
}
