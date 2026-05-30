package com.ia.para.devs.skybook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ia.para.devs.skybook.model.AirplaneSeatEntity;

/**
 * Repositório Spring Data JPA para acesso e persistência de {@link com.ia.para.devs.skybook.model.AirplaneSeatEntity}.
 */
@Repository
public interface AirplaneSeatRepository extends JpaRepository<AirplaneSeatEntity, Long> {

    /**
     * Busca uma poltrona pelo seu código (ex: "1A", "3C").
     *
     * @param code código da poltrona
     * @return {@link Optional} contendo a poltrona, se encontrada
     */
    Optional<AirplaneSeatEntity> findByCode(String code);
}
