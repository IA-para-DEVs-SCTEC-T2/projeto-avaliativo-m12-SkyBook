package com.ia.para.devs.skybook.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ia.para.devs.skybook.model.UserEntity;
import com.ia.para.devs.skybook.repository.UserRepository;

/**
 * Testes unitários para {@link UserService}.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("resolveOrCreate → retorna usuário existente quando e-mail já está cadastrado")
    void resolveOrCreate_shouldReturnExistingUser_whenEmailExists() {
        UserEntity existing = buildUser(1L, "João", "joao@email.com");
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(existing));

        UserEntity result = userService.resolveOrCreate("João", "joao@email.com");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("João");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("resolveOrCreate → cria novo usuário quando e-mail não existe")
    void resolveOrCreate_shouldCreateNewUser_whenEmailNotFound() {
        UserEntity newUser = buildUser(2L, "Maria", "maria@email.com");
        when(userRepository.findByEmail("maria@email.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(newUser);

        UserEntity result = userService.resolveOrCreate("Maria", "maria@email.com");

        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Maria");
        verify(userRepository).save(any(UserEntity.class));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private UserEntity buildUser(Long id, String name, String email) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
