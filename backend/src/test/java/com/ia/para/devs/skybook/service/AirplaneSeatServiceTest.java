package com.ia.para.devs.skybook.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

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

    // -------------------------------------------------------------------------
    // listAllSeats
    // -------------------------------------------------------------------------

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
    // findAndValidateAvailableSeats
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("findAndValidateAvailableSeats → retorna poltronas disponíveis")
    void findAndValidateAvailableSeats_shouldReturnAvailableSeats() {
        AirplaneSeatEntity seat = buildSeat(1L, "1A", new BigDecimal("198.89"), true);
        when(airplaneSeatRepository.findByCode("1A")).thenReturn(Optional.of(seat));

        List<AirplaneSeatEntity> result = airplaneSeatService.findAndValidateAvailableSeats(List.of("1A"));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("1A");
    }

    @Test
    @DisplayName("findAndValidateAvailableSeats → lança 404 quando poltrona não é encontrada")
    void findAndValidateAvailableSeats_shouldThrow404_whenSeatNotFound() {
        when(airplaneSeatRepository.findByCode("99Z")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> airplaneSeatService.findAndValidateAvailableSeats(List.of("99Z")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Poltrona não encontrada");
    }

    @Test
    @DisplayName("findAndValidateAvailableSeats → lança 409 quando poltrona está indisponível")
    void findAndValidateAvailableSeats_shouldThrow409_whenSeatIsUnavailable() {
        AirplaneSeatEntity unavailable = buildSeat(5L, "5A", new BigDecimal("110.00"), false);
        when(airplaneSeatRepository.findByCode("5A")).thenReturn(Optional.of(unavailable));

        assertThatThrownBy(() -> airplaneSeatService.findAndValidateAvailableSeats(List.of("5A")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Poltrona indisponível");
    }

    @Test
    @DisplayName("findAndValidateAvailableSeats → lança 409 se qualquer poltrona do lote estiver indisponível")
    void findAndValidateAvailableSeats_shouldThrow409_whenAnyOfMultipleSeatsIsUnavailable() {
        AirplaneSeatEntity available = buildSeat(1L, "1A", new BigDecimal("198.89"), true);
        AirplaneSeatEntity unavailable = buildSeat(2L, "1B", new BigDecimal("198.89"), false);

        when(airplaneSeatRepository.findByCode("1A")).thenReturn(Optional.of(available));
        when(airplaneSeatRepository.findByCode("1B")).thenReturn(Optional.of(unavailable));

        assertThatThrownBy(() -> airplaneSeatService.findAndValidateAvailableSeats(List.of("1A", "1B")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Poltrona indisponível");
    }

    // -------------------------------------------------------------------------
    // markAsUnavailable
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("markAsUnavailable → persiste poltrona com available=false")
    void markAsUnavailable_shouldSetAvailableFalseAndSave() {
        AirplaneSeatEntity seat = buildSeat(1L, "1A", new BigDecimal("198.89"), true);
        when(airplaneSeatRepository.save(any(AirplaneSeatEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        AirplaneSeatEntity result = airplaneSeatService.markAsUnavailable(seat);

        assertThat(result.getAvailable()).isFalse();

        ArgumentCaptor<AirplaneSeatEntity> captor = ArgumentCaptor.forClass(AirplaneSeatEntity.class);
        verify(airplaneSeatRepository).save(captor.capture());
        assertThat(captor.getValue().getAvailable()).isFalse();
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
