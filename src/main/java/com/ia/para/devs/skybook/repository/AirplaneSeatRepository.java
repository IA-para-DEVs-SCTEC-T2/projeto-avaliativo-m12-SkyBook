package com.ia.para.devs.skybook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ia.para.devs.skybook.model.AirplaneSeatEntity;

/**
 * Repositório Spring Data JPA para acesso e persistência de {@link com.ia.para.devs.skybook.model.AirplaneSeatEntity}.
 */
@Repository
public interface AirplaneSeatRepository extends JpaRepository<AirplaneSeatEntity, Long> {
}
