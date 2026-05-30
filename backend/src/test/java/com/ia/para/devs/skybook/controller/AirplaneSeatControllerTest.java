package com.ia.para.devs.skybook.controller;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ia.para.devs.skybook.dto.AirplaneSeatResponseDTO;
import com.ia.para.devs.skybook.service.AirplaneSeatService;

/**
 * Testes unitários para {@link AirplaneSeatController}.
 */
@ExtendWith(MockitoExtension.class)
class AirplaneSeatControllerTest {

    @Mock
    private AirplaneSeatService airplaneSeatService;

    @InjectMocks
    private AirplaneSeatController airplaneSeatController;

    @Test
    @DisplayName("listAllSeats → retorna 200 com lista de poltronas")
    void listAllSeats_shouldReturn200WithSeatList() {
        List<AirplaneSeatResponseDTO> seats = List.of(
                new AirplaneSeatResponseDTO(1L, "1A", new BigDecimal("198.89"), true),
                new AirplaneSeatResponseDTO(2L, "1B", new BigDecimal("198.89"), false)
        );
        when(airplaneSeatService.listAllSeats()).thenReturn(seats);

        ResponseEntity<List<AirplaneSeatResponseDTO>> response = airplaneSeatController.listAllSeats();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
        assertThat(response.getBody().get(0).getCode()).isEqualTo("1A");
        assertThat(response.getBody().get(0).getAvailable()).isTrue();
        assertThat(response.getBody().get(1).getAvailable()).isFalse();

        verify(airplaneSeatService).listAllSeats();
    }

    @Test
    @DisplayName("listAllSeats → retorna 200 com lista vazia quando não há poltronas")
    void listAllSeats_shouldReturn200WithEmptyList_whenNoSeatsExist() {
        when(airplaneSeatService.listAllSeats()).thenReturn(List.of());

        ResponseEntity<List<AirplaneSeatResponseDTO>> response = airplaneSeatController.listAllSeats();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();

        verify(airplaneSeatService).listAllSeats();
    }

    @Test
    @DisplayName("listAllSeats → delega para o service exatamente uma vez")
    void listAllSeats_shouldDelegateToServiceOnce() {
        when(airplaneSeatService.listAllSeats()).thenReturn(List.of());

        airplaneSeatController.listAllSeats();

        verify(airplaneSeatService).listAllSeats();
    }
}
