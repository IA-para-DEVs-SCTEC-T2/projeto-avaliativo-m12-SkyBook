package com.ia.para.devs.skybook.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

import com.ia.para.devs.skybook.dto.BookingItemDTO;
import com.ia.para.devs.skybook.dto.BookingRequestDTO;
import com.ia.para.devs.skybook.dto.BookingResponseDTO;
import com.ia.para.devs.skybook.dto.BookingSummaryResponseDTO;
import com.ia.para.devs.skybook.service.BookingService;

/**
 * Testes unitários para {@link BookingController}.
 */
@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    @DisplayName("createBookings → retorna 201 com lista de reservas criadas")
    void createBookings_shouldReturn201WithBookingList() {
        BookingRequestDTO request = new BookingRequestDTO("João", "joao@email.com", List.of("1A", "1B"));
        List<BookingResponseDTO> serviceResponse = List.of(
                new BookingResponseDTO(100L, "1A", new BigDecimal("198.89"), "João", LocalDateTime.now()),
                new BookingResponseDTO(101L, "1B", new BigDecimal("198.89"), "João", LocalDateTime.now())
        );
        when(bookingService.createBookings(request)).thenReturn(serviceResponse);

        ResponseEntity<List<BookingResponseDTO>> response = bookingController.createBookings(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getBookingId()).isEqualTo(100L);
        assertThat(response.getBody().get(0).getSeatCode()).isEqualTo("1A");
        assertThat(response.getBody().get(1).getSeatCode()).isEqualTo("1B");

        verify(bookingService).createBookings(request);
    }

    @Test
    @DisplayName("createBookings → retorna 201 com reserva única")
    void createBookings_shouldReturn201WithSingleBooking() {
        BookingRequestDTO request = new BookingRequestDTO("Maria", "maria@email.com", List.of("5A"));
        List<BookingResponseDTO> serviceResponse = List.of(
                new BookingResponseDTO(200L, "5A", new BigDecimal("110.00"), "Maria", LocalDateTime.now())
        );
        when(bookingService.createBookings(request)).thenReturn(serviceResponse);

        ResponseEntity<List<BookingResponseDTO>> response = bookingController.createBookings(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getPassengerName()).isEqualTo("Maria");
        assertThat(response.getBody().get(0).getSeatPrice()).isEqualByComparingTo("110.00");

        verify(bookingService).createBookings(request);
    }

    @Test
    @DisplayName("createBookings → delega para o service exatamente uma vez")
    void createBookings_shouldDelegateToServiceOnce() {
        BookingRequestDTO request = new BookingRequestDTO("Carlos", "carlos@email.com", List.of("3C"));
        when(bookingService.createBookings(request)).thenReturn(List.of());

        bookingController.createBookings(request);

        verify(bookingService).createBookings(request);
    }

    // -------------------------------------------------------------------------
    // Cenários: GET /bookings/summary
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getSummary → retorna 200 com lista de reservas e valor total")
    void getSummary_shouldReturn200WithBookingsAndTotal() {
        List<BookingItemDTO> items = List.of(
                new BookingItemDTO(1L, "1A", new BigDecimal("198.89"), LocalDateTime.now()),
                new BookingItemDTO(2L, "5A", new BigDecimal("110.00"), LocalDateTime.now())
        );
        BookingSummaryResponseDTO summary = new BookingSummaryResponseDTO(
                "João", "joao@email.com", new BigDecimal("308.89"), items);
        when(bookingService.getSummary("joao@email.com")).thenReturn(summary);

        ResponseEntity<BookingSummaryResponseDTO> response = bookingController.getSummary("joao@email.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPassengerName()).isEqualTo("João");
        assertThat(response.getBody().getPassengerEmail()).isEqualTo("joao@email.com");
        assertThat(response.getBody().getBookings()).hasSize(2);
        assertThat(response.getBody().getTotalAmount()).isEqualByComparingTo("308.89");

        verify(bookingService).getSummary("joao@email.com");
    }

    @Test
    @DisplayName("getSummary → retorna 200 com lista vazia e total zero quando não há reservas")
    void getSummary_shouldReturn200WithEmptyListAndZeroTotal() {
        BookingSummaryResponseDTO summary = new BookingSummaryResponseDTO(
                "João", "joao@email.com", BigDecimal.ZERO, List.of());
        when(bookingService.getSummary("joao@email.com")).thenReturn(summary);

        ResponseEntity<BookingSummaryResponseDTO> response = bookingController.getSummary("joao@email.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBookings()).isEmpty();
        assertThat(response.getBody().getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);

        verify(bookingService).getSummary("joao@email.com");
    }
}
