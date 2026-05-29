package com.ia.para.devs.skybook.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ia.para.devs.skybook.dto.BookingRequestDTO;
import com.ia.para.devs.skybook.dto.BookingResponseDTO;
import com.ia.para.devs.skybook.model.AirplaneSeatEntity;
import com.ia.para.devs.skybook.model.BookingEntity;
import com.ia.para.devs.skybook.model.UserEntity;
import com.ia.para.devs.skybook.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela lógica de negócio de reservas de poltronas.
 * Delega a gestão de poltronas ao {@link AirplaneSeatService} e a gestão
 * de usuários ao {@link UserService}, mantendo responsabilidade única.
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AirplaneSeatService airplaneSeatService;
    private final UserService userService;

    /**
     * Cria reservas para as poltronas informadas no request.
     * <p>
     * Regras de negócio:
     * <ul>
     *   <li>Todas as poltronas devem estar disponíveis antes de qualquer reserva ser criada.</li>
     *   <li>Após a reserva, o status de cada poltrona é atualizado para indisponível.</li>
     *   <li>Se o e-mail do passageiro já existir, o usuário existente é reutilizado.</li>
     * </ul>
     *
     * @param request DTO com dados do passageiro e códigos das poltronas desejadas
     * @return lista de {@link BookingResponseDTO} com os detalhes de cada reserva criada
     */
    @Transactional
    public List<BookingResponseDTO> createBookings(BookingRequestDTO request) {
        List<AirplaneSeatEntity> seats = airplaneSeatService.findAndValidateAvailableSeats(request.getSeatCodes());
        UserEntity user = userService.resolveOrCreate(request.getPassengerName(), request.getPassengerEmail());
        LocalDateTime now = LocalDateTime.now();

        return seats.stream()
                .map(seat -> bookSeat(user, seat, now))
                .toList();
    }

    /**
     * Reserva uma única poltrona: marca como indisponível, persiste a reserva e retorna o DTO.
     *
     * @param user     usuário que está realizando a reserva
     * @param seat     poltrona a ser reservada
     * @param bookedAt data e hora da reserva
     * @return {@link BookingResponseDTO} com os detalhes da reserva criada
     */
    private BookingResponseDTO bookSeat(UserEntity user, AirplaneSeatEntity seat, LocalDateTime bookedAt) {
        airplaneSeatService.markAsUnavailable(seat);
        BookingEntity booking = persistBooking(user, seat, bookedAt);
        return toResponseDTO(booking, seat, user);
    }

    /**
     * Cria e persiste a entidade de reserva.
     *
     * @param user     usuário da reserva
     * @param seat     poltrona reservada
     * @param bookedAt data e hora da reserva
     * @return entidade {@link BookingEntity} persistida
     */
    private BookingEntity persistBooking(UserEntity user, AirplaneSeatEntity seat, LocalDateTime bookedAt) {
        BookingEntity booking = new BookingEntity();
        booking.setUser(user);
        booking.setSeat(seat);
        booking.setBookedAt(bookedAt);
        return bookingRepository.save(booking);
    }

    /**
     * Converte a entidade de reserva para o DTO de resposta.
     *
     * @param booking reserva persistida
     * @param seat    poltrona reservada
     * @param user    usuário da reserva
     * @return {@link BookingResponseDTO} com os dados da reserva
     */
    private BookingResponseDTO toResponseDTO(BookingEntity booking, AirplaneSeatEntity seat, UserEntity user) {
        return new BookingResponseDTO(
                booking.getId(),
                seat.getCode(),
                seat.getPrice(),
                user.getName(),
                booking.getBookedAt());
    }
}
