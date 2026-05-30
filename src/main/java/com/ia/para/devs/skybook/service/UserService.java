package com.ia.para.devs.skybook.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ia.para.devs.skybook.model.UserEntity;
import com.ia.para.devs.skybook.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela lógica de negócio relacionada aos usuários.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Retorna o usuário existente pelo e-mail ou cria um novo caso não exista.
     *
     * @param name  nome do passageiro
     * @param email e-mail do passageiro
     * @return entidade {@link UserEntity} persistida
     */
    public UserEntity resolveOrCreate(String name, String email) {
        return userRepository.findByEmail(email).orElseGet(() -> createUser(name, email));
    }

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param email e-mail do usuário
     * @return {@link Optional} contendo o usuário, se encontrado
     */
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Cria e persiste um novo usuário.
     *
     * @param name  nome do usuário
     * @param email e-mail do usuário
     * @return entidade {@link UserEntity} persistida
     */
    private UserEntity createUser(String name, String email) {
        UserEntity newUser = new UserEntity();
        newUser.setName(name);
        newUser.setEmail(email);
        return userRepository.save(newUser);
    }
}
