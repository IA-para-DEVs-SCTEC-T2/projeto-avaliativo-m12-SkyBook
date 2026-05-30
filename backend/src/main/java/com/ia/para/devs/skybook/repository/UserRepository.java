package com.ia.para.devs.skybook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ia.para.devs.skybook.model.UserEntity;

/**
 * Repositório Spring Data JPA para acesso e persistência de {@link UserEntity}.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param email e-mail do usuário
     * @return {@link Optional} contendo o usuário, se encontrado
     */
    Optional<UserEntity> findByEmail(String email);
}
