package com.ia.para.devs.skybook.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.ia.para.devs.skybook.dto.BookingRequestDTO;
import com.ia.para.devs.skybook.dto.BookingResponseDTO;
import com.ia.para.devs.skybook.dto.BookingSummaryResponseDTO;
import com.ia.para.devs.skybook.model.AirplaneSeatEntity;
import com.ia.para.devs.skybook.model.BookingEntity;
import com.ia.para.devs.skybook.model.UserEntity;
import com.ia.para.devs.skybook.repository.BookingRepository;

/**
 * Testes unitários para {@link BookingService}.
 * Valida apenas a lógica de orquestração de reservas.
 * Cenários de validação de poltronas são cobertos em {@link AirplaneSeatServiceTest}.
 * Cenários de criação/reutilização de usuário são cobertos em {@link UserServiceTest}.
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private AirplaneSeatService airplaneSeatService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingService bookingService;

    // -------------------------------------------------------------------------
    // Cenário 1: reserva de poltrona disponível
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("createBookings → cria reserva e retorna DTO com dados corretos")
    void createBookings_shouldCreateBookingAndReturnDTO() {
        AirplaneSeatEntity seat = buildSeat(1L, "1A", new BigDecimal("198.89"));
        UserEntity user = buildUser(10L, "João", "joao@email.com");
        BookingEntity savedBooking = buildBooking(100L, user, seat);

        when(airplaneSeatService.findAndValidateAvailableSeats(List.of("1A"))).thenReturn(List.of(seat));
        when(airplaneSeatService.markAsUnavailable(seat)).thenReturn(seat);
        when(userService.resolveOrCreate("João", "joao@email.com")).thenReturn(user);
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(savedBooking);

        BookingRequestDTO request = new BookingRequestDTO("João", "joao@email.com", List.of("1A"));
        List<BookingResponseDTO> result = bookingService.createBookings(request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSeatCode()).isEqualTo("1A");
        assertThat(result.get(0).getPassengerName()).isEqualTo("João");
        assertThat(result.get(0).getSeatPrice()).isEqualByComparingTo("198.89");

        verify(airplaneSeatService).markAsUnavailable(seat);
        verify(bookingRepository).save(any(BookingEntity.class));
    }

    // -------------------------------------------------------------------------
    // Cenário 3: reserva de múltiplas poltronas
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("createBookings → reserva múltiplas poltronas e retorna lista com todas")
    void createBookings_shouldBookMultipleSeats_andReturnAll() {
        AirplaneSeatEntity seat1 = buildSeat(1L, "1A", new BigDecimal("198.89"));
        AirplaneSeatEntity seat2 = buildSeat(2L, "1B", new BigDecimal("198.89"));
        AirplaneSeatEntity seat3 = buildSeat(5L, "5A", new BigDecimal("110.00"));
        UserEntity user = buildUser(10L, "João", "joao@email.com");

        when(airplaneSeatService.findAndValidateAvailableSeats(List.of("1A", "1B", "5A")))
                .thenReturn(List.of(seat1, seat2, seat3));
        when(airplaneSeatService.markAsUnavailable(any(AirplaneSeatEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userService.resolveOrCreate("João", "joao@email.com")).thenReturn(user);
        when(bookingRepository.save(any(BookingEntity.class))).thenAnswer(inv -> {
            BookingEntity b = inv.getArgument(0);
            b.setId(System.nanoTime());
            return b;
        });

        BookingRequestDTO request = new BookingRequestDTO("João", "joao@email.com", List.of("1A", "1B", "5A"));
        List<BookingResponseDTO> result = bookingService.createBookings(request);

        assertThat(result).hasSize(3);
        verify(airplaneSeatService, times(3)).markAsUnavailable(any(AirplaneSeatEntity.class));
        verify(bookingRepository, times(3)).save(any(BookingEntity.class));
    }

    @Test
    @DisplayName("createBookings → delega validação de poltronas ao AirplaneSeatService")
    void createBookings_shouldDelegateSeatValidationToAirplaneSeatService() {
        AirplaneSeatEntity seat = buildSeat(1L, "1A", new BigDecimal("198.89"));
        UserEntity user = buildUser(10L, "João", "joao@email.com");

        when(airplaneSeatService.findAndValidateAvailableSeats(List.of("1A"))).thenReturn(List.of(seat));
        when(airplaneSeatService.markAsUnavailable(seat)).thenReturn(seat);
        when(userService.resolveOrCreate("João", "joao@email.com")).thenReturn(user);
        when(bookingRepository.save(any(BookingEntity.class))).thenAnswer(inv -> {
            BookingEntity b = inv.getArgument(0);
            b.setId(1L);
            return b;
        });

        bookingService.createBookings(new BookingRequestDTO("João", "joao@email.com", List.of("1A")));

        verify(airplaneSeatService).findAndValidateAvailableSeats(List.of("1A"));
    }

    @Test
    @DisplayName("createBookings → delega resolução de usuário ao UserService")
    void createBookings_shouldDelegateUserResolutionToUserService() {
        AirplaneSeatEntity seat = buildSeat(1L, "1A", new BigDecimal("198.89"));
        UserEntity user = buildUser(10L, "Maria", "maria@email.com");

        when(airplaneSeatService.findAndValidateAvailableSeats(List.of("1A"))).thenReturn(List.of(seat));
        when(airplaneSeatService.markAsUnavailable(seat)).thenReturn(seat);
        when(userService.resolveOrCreate("Maria", "maria@email.com")).thenReturn(user);
        when(bookingRepository.save(any(BookingEntity.class))).thenAnswer(inv -> {
            BookingEntity b = inv.getArgument(0);
            b.setId(1L);
            return b;
        });

        bookingService.createBookings(new BookingRequestDTO("Maria", "maria@email.com", List.of("1A")));

        verify(userService).resolveOrCreate("Maria", "maria@email.com");
    }

    // -------------------------------------------------------------------------
    // Cenários: getSummary
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getSummary → retorna resumo com nome, email, reservas e total calculado")
    void getSummary_shouldReturnSummaryWithPassengerDataAndTotal() {
        UserEntity user = buildUser(10L, "João", "joao@email.com");
        AirplaneSeatEntity seat1 = buildSeat(1L, "1A", new BigDecimal("198.89"));
        AirplaneSeatEntity seat2 = buildSeat(5L, "5A", new BigDecimal("110.00"));
        BookingEntity booking1 = buildBooking(100L, user, seat1);
        BookingEntity booking2 = buildBooking(101L, user, seat2);

        when(userService.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserEmail("joao@email.com")).thenReturn(List.of(booking1, booking2));

        BookingSummaryResponseDTO result = bookingService.getSummary("joao@email.com");

        assertThat(result.getPassengerName()).isEqualTo("João");
        assertThat(result.getPassengerEmail()).isEqualTo("joao@email.com");
        assertThat(result.getBookings()).hasSize(2);
        assertThat(result.getTotalAmount()).isEqualByComparingTo("308.89");
        assertThat(result.getBookings().get(0).getSeatCode()).isEqualTo("1A");
        assertThat(result.getBookings().get(1).getSeatCode()).isEqualTo("5A");
    }

    @Test
    @DisplayName("getSummary → retorna lista vazia e total zero quando usuário não tem reservas")
    void getSummary_shouldReturnEmptyBookingsAndZeroTotalWhenNoBookings() {
        UserEntity user = buildUser(10L, "João", "joao@email.com");

        when(userService.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserEmail("joao@email.com")).thenReturn(List.of());

        BookingSummaryResponseDTO result = bookingService.getSummary("joao@email.com");

        assertThat(result.getPassengerName()).isEqualTo("João");
        assertThat(result.getBookings()).isEmpty();
        assertThat(result.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("getSummary → lança 404 quando e-mail não encontrado")
    void getSummary_shouldThrow404WhenEmailNotFound() {
        when(userService.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getSummary("naoexiste@email.com"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuário não encontrado");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private AirplaneSeatEntity buildSeat(Long id, String code, BigDecimal price) {
        AirplaneSeatEntity seat = new AirplaneSeatEntity();
        seat.setId(id);
        seat.setCode(code);
        seat.setPrice(price);
        seat.setAvailable(true);
        return seat;
    }

    private UserEntity buildUser(Long id, String name, String email) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private BookingEntity buildBooking(Long id, UserEntity user, AirplaneSeatEntity seat) {
        BookingEntity booking = new BookingEntity();
        booking.setId(id);
        booking.setUser(user);
        booking.setSeat(seat);
        booking.setBookedAt(java.time.LocalDateTime.now());
        return booking;
    }
}
