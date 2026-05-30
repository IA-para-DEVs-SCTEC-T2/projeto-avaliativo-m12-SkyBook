package com.ia.para.devs.skybook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ia.para.devs.skybook.model.BookingEntity;

/**
 * Repositório Spring Data JPA para acesso e persistência de {@link BookingEntity}.
 */
@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    /**
     * Busca todas as reservas associadas ao e-mail do usuário.
     *
     * @param email e-mail do usuário
     * @return lista de {@link BookingEntity} do usuário
     */
    List<BookingEntity> findByUserEmail(String email);
}
