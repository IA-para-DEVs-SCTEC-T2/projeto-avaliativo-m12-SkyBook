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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.ia.para.devs.skybook.dto.BookingRequestDTO;
import com.ia.para.devs.skybook.dto.BookingResponseDTO;
import com.ia.para.devs.skybook.model.AirplaneSeatEntity;
import com.ia.para.devs.skybook.model.BookingEntity;
import com.ia.para.devs.skybook.model.UserEntity;
import com.ia.para.devs.skybook.repository.AirplaneSeatRepository;
import com.ia.para.devs.skybook.repository.BookingRepository;
import com.ia.para.devs.skybook.repository.UserRepository;

/**
 * Testes unitários para {@link BookingService}.
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private AirplaneSeatRepository airplaneSeatRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    // -------------------------------------------------------------------------
    // Cenário 1: reserva de poltrona disponível
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("createBookings → cria reserva e marca poltrona como indisponível")
    void createBookings_shouldCreateBookingAndMarkSeatUnavailable() {
        AirplaneSeatEntity seat = buildSeat(1L, "1A", new BigDecimal("198.89"), true);
        UserEntity user = buildUser(10L, "João", "joao@email.com");
        BookingEntity savedBooking = buildBooking(100L, user, seat);

        when(airplaneSeatRepository.findByCode("1A")).thenReturn(Optional.of(seat));
        when(airplaneSeatRepository.save(seat)).thenReturn(seat);
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(savedBooking);

        BookingRequestDTO request = new BookingRequestDTO("João", "joao@email.com", List.of("1A"));
        List<BookingResponseDTO> result = bookingService.createBookings(request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSeatCode()).isEqualTo("1A");
        assertThat(result.get(0).getPassengerName()).isEqualTo("João");
        assertThat(result.get(0).getSeatPrice()).isEqualByComparingTo("198.89");

        ArgumentCaptor<AirplaneSeatEntity> seatCaptor = ArgumentCaptor.forClass(AirplaneSeatEntity.class);
        verify(airplaneSeatRepository).save(seatCaptor.capture());
        assertThat(seatCaptor.getValue().getAvailable()).isFalse();
    }

    @Test
    @DisplayName("createBookings → cria usuário novo quando e-mail não existe")
    void createBookings_shouldCreateNewUser_whenEmailNotFound() {
        AirplaneSeatEntity seat = buildSeat(2L, "2B", new BigDecimal("149.90"), true);
        UserEntity newUser = buildUser(20L, "Maria", "maria@email.com");
        BookingEntity savedBooking = buildBooking(101L, newUser, seat);

        when(airplaneSeatRepository.findByCode("2B")).thenReturn(Optional.of(seat));
        when(airplaneSeatRepository.save(seat)).thenReturn(seat);
        when(userRepository.findByEmail("maria@email.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(newUser);
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(savedBooking);

        BookingRequestDTO request = new BookingRequestDTO("Maria", "maria@email.com", List.of("2B"));
        List<BookingResponseDTO> result = bookingService.createBookings(request);

        assertThat(result).hasSize(1);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("createBookings → reutiliza usuário existente pelo e-mail")
    void createBookings_shouldReuseExistingUser_whenEmailExists() {
        AirplaneSeatEntity seat = buildSeat(3L, "3C", new BigDecimal("149.90"), true);
        UserEntity existingUser = buildUser(30L, "Carlos", "carlos@email.com");
        BookingEntity savedBooking = buildBooking(102L, existingUser, seat);

        when(airplaneSeatRepository.findByCode("3C")).thenReturn(Optional.of(seat));
        when(airplaneSeatRepository.save(seat)).thenReturn(seat);
        when(userRepository.findByEmail("carlos@email.com")).thenReturn(Optional.of(existingUser));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(savedBooking);

        BookingRequestDTO request = new BookingRequestDTO("Carlos", "carlos@email.com", List.of("3C"));
        bookingService.createBookings(request);

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    // -------------------------------------------------------------------------
    // Cenário 2: tentativa de reserva de poltrona indisponível
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("createBookings → lança 409 quando poltrona está indisponível")
    void createBookings_shouldThrow409_whenSeatIsUnavailable() {
        AirplaneSeatEntity unavailableSeat = buildSeat(5L, "5A", new BigDecimal("110.00"), false);

        when(airplaneSeatRepository.findByCode("5A")).thenReturn(Optional.of(unavailableSeat));

        BookingRequestDTO request = new BookingRequestDTO("Ana", "ana@email.com", List.of("5A"));

        assertThatThrownBy(() -> bookingService.createBookings(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Poltrona indisponível");

        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("createBookings → lança 404 quando poltrona não é encontrada")
    void createBookings_shouldThrow404_whenSeatNotFound() {
        when(airplaneSeatRepository.findByCode("99Z")).thenReturn(Optional.empty());

        BookingRequestDTO request = new BookingRequestDTO("Pedro", "pedro@email.com", List.of("99Z"));

        assertThatThrownBy(() -> bookingService.createBookings(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Poltrona não encontrada");

        verify(bookingRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // Cenário 3: reserva de múltiplas poltronas
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("createBookings → reserva múltiplas poltronas e retorna lista com todas")
    void createBookings_shouldBookMultipleSeats_andReturnAll() {
        AirplaneSeatEntity seat1 = buildSeat(1L, "1A", new BigDecimal("198.89"), true);
        AirplaneSeatEntity seat2 = buildSeat(2L, "1B", new BigDecimal("198.89"), true);
        AirplaneSeatEntity seat3 = buildSeat(5L, "5A", new BigDecimal("110.00"), true);
        UserEntity user = buildUser(10L, "João", "joao@email.com");

        when(airplaneSeatRepository.findByCode("1A")).thenReturn(Optional.of(seat1));
        when(airplaneSeatRepository.findByCode("1B")).thenReturn(Optional.of(seat2));
        when(airplaneSeatRepository.findByCode("5A")).thenReturn(Optional.of(seat3));
        when(airplaneSeatRepository.save(any(AirplaneSeatEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(BookingEntity.class))).thenAnswer(inv -> {
            BookingEntity b = inv.getArgument(0);
            b.setId(System.nanoTime());
            return b;
        });

        BookingRequestDTO request = new BookingRequestDTO("João", "joao@email.com", List.of("1A", "1B", "5A"));
        List<BookingResponseDTO> result = bookingService.createBookings(request);

        assertThat(result).hasSize(3);
        verify(airplaneSeatRepository, times(3)).save(any(AirplaneSeatEntity.class));
        verify(bookingRepository, times(3)).save(any(BookingEntity.class));

        assertThat(seat1.getAvailable()).isFalse();
        assertThat(seat2.getAvailable()).isFalse();
        assertThat(seat3.getAvailable()).isFalse();
    }

    @Test
    @DisplayName("createBookings → lança 409 se qualquer poltrona do lote estiver indisponível")
    void createBookings_shouldThrow409_whenAnyOfMultipleSeatsIsUnavailable() {
        AirplaneSeatEntity available = buildSeat(1L, "1A", new BigDecimal("198.89"), true);
        AirplaneSeatEntity unavailable = buildSeat(2L, "1B", new BigDecimal("198.89"), false);

        when(airplaneSeatRepository.findByCode("1A")).thenReturn(Optional.of(available));
        when(airplaneSeatRepository.findByCode("1B")).thenReturn(Optional.of(unavailable));

        BookingRequestDTO request = new BookingRequestDTO("João", "joao@email.com", List.of("1A", "1B"));

        assertThatThrownBy(() -> bookingService.createBookings(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Poltrona indisponível");

        verify(bookingRepository, never()).save(any());
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
