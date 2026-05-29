package com.ia.para.devs.skybook.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ia.para.devs.skybook.dto.BookingRequestDTO;
import com.ia.para.devs.skybook.dto.BookingResponseDTO;
import com.ia.para.devs.skybook.model.AirplaneSeatEntity;
import com.ia.para.devs.skybook.model.BookingEntity;
import com.ia.para.devs.skybook.model.UserEntity;
import com.ia.para.devs.skybook.repository.AirplaneSeatRepository;
import com.ia.para.devs.skybook.repository.BookingRepository;
import com.ia.para.devs.skybook.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela lógica de negócio de reservas de poltronas.
 * Valida disponibilidade, cria ou reutiliza o usuário e persiste as reservas.
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final AirplaneSeatRepository airplaneSeatRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

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
     * @param request DTO com dados do passageiro e IDs das poltronas desejadas
     * @return lista de {@link BookingResponseDTO} com os detalhes de cada reserva criada
     * @throws ResponseStatusException 409 se alguma poltrona já estiver reservada
     * @throws ResponseStatusException 404 se algum ID de poltrona não for encontrado
     */
    @Transactional
    public List<BookingResponseDTO> createBookings(BookingRequestDTO request) {
        List<AirplaneSeatEntity> seats = resolveAndValidateSeats(request.getSeatIds());

        UserEntity user = resolveUser(request.getPassengerName(), request.getPassengerEmail());

        List<BookingResponseDTO> responses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (AirplaneSeatEntity seat : seats) {
            seat.setAvailable(false);
            airplaneSeatRepository.save(seat);

            BookingEntity booking = new BookingEntity();
            booking.setUser(user);
            booking.setSeat(seat);
            booking.setBookedAt(now);
            bookingRepository.save(booking);

            responses.add(new BookingResponseDTO(
                    booking.getId(),
                    seat.getCode(),
                    seat.getPrice(),
                    user.getName(),
                    booking.getBookedAt()));
        }

        return responses;
    }

    /**
     * Busca as poltronas pelos IDs fornecidos e valida que todas estão disponíveis.
     *
     * @param seatIds lista de IDs das poltronas
     * @return lista de entidades de poltrona validadas
     * @throws ResponseStatusException 404 se algum ID não for encontrado
     * @throws ResponseStatusException 409 se alguma poltrona já estiver reservada
     */
    private List<AirplaneSeatEntity> resolveAndValidateSeats(List<Long> seatIds) {
        List<AirplaneSeatEntity> seats = new ArrayList<>();

        for (Long seatId : seatIds) {
            AirplaneSeatEntity seat = airplaneSeatRepository.findById(seatId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Poltrona não encontrada: id=" + seatId));

            if (!seat.getAvailable()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Poltrona indisponível: " + seat.getCode());
            }

            seats.add(seat);
        }

        return seats;
    }

    /**
     * Retorna o usuário existente pelo e-mail ou cria um novo caso não exista.
     *
     * @param name  nome do passageiro
     * @param email e-mail do passageiro
     * @return entidade {@link UserEntity} persistida
     */
    private UserEntity resolveUser(String name, String email) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            UserEntity newUser = new UserEntity();
            newUser.setName(name);
            newUser.setEmail(email);
            return userRepository.save(newUser);
        });
    }
}
