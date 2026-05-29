package com.ia.para.devs.skybook.service;

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

import com.ia.para.devs.skybook.dto.AirplaneSeatResponseDTO;
import com.ia.para.devs.skybook.model.AirplaneSeatEntity;
import com.ia.para.devs.skybook.repository.AirplaneSeatRepository;

/**
 * Testes unitários para {@link AirplaneSeatService}.
 */
@ExtendWith(MockitoExtension.class)
class AirplaneSeatServiceTest {

    @Mock
    private AirplaneSeatRepository airplaneSeatRepository;

    @InjectMocks
    private AirplaneSeatService airplaneSeatService;

    @Test
    @DisplayName("listAllSeats → retorna todos os assentos convertidos para DTO")
    void listAllSeats_shouldReturnAllSeatsAsDTOs() {
        AirplaneSeatEntity seat1 = buildSeat(1L, "1A", new BigDecimal("198.89"), true);
        AirplaneSeatEntity seat2 = buildSeat(2L, "1B", new BigDecimal("198.89"), false);
        when(airplaneSeatRepository.findAll()).thenReturn(List.of(seat1, seat2));

        List<AirplaneSeatResponseDTO> result = airplaneSeatService.listAllSeats();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getCode()).isEqualTo("1A");
        assertThat(result.get(0).getPrice()).isEqualByComparingTo("198.89");
        assertThat(result.get(0).getAvailable()).isTrue();
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getCode()).isEqualTo("1B");
        assertThat(result.get(1).getAvailable()).isFalse();

        verify(airplaneSeatRepository).findAll();
    }

    @Test
    @DisplayName("listAllSeats → retorna lista vazia quando repositório não possui assentos")
    void listAllSeats_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        when(airplaneSeatRepository.findAll()).thenReturn(List.of());

        List<AirplaneSeatResponseDTO> result = airplaneSeatService.listAllSeats();

        assertThat(result).isEmpty();
        verify(airplaneSeatRepository).findAll();
    }

    @Test
    @DisplayName("listAllSeats → disponibilidade false é preservada no DTO")
    void listAllSeats_shouldMapUnavailabilityCorrectly() {
        AirplaneSeatEntity seat = buildSeat(10L, "2C", new BigDecimal("149.90"), false);
        when(airplaneSeatRepository.findAll()).thenReturn(List.of(seat));

        List<AirplaneSeatResponseDTO> result = airplaneSeatService.listAllSeats();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAvailable()).isFalse();
        assertThat(result.get(0).getPrice()).isEqualByComparingTo("149.90");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private AirplaneSeatEntity buildSeat(Long id, String code, BigDecimal price, boolean available) {
        AirplaneSeatEntity seat = new AirplaneSeatEntity();
        seat.setId(id);
        seat.setCode(code);
        seat.setPrice(price);
        seat.setAvailable(available);
        return seat;
    }
}
