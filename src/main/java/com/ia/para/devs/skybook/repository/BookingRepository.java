package com.ia.para.devs.skybook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ia.para.devs.skybook.model.BookingEntity;

/**
 * Repositório Spring Data JPA para acesso e persistência de {@link BookingEntity}.
 */
@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
}
