package com.ia.para.devs.skybook.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ia.para.devs.skybook.dto.AirplaneSeatResponseDTO;
import com.ia.para.devs.skybook.model.AirplaneSeatEntity;
import com.ia.para.devs.skybook.repository.AirplaneSeatRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela lógica de negócio relacionada às poltronas da aeronave.
 */
@Service
@RequiredArgsConstructor
public class AirplaneSeatService {

    private final AirplaneSeatRepository airplaneSeatRepository;

    /**
     * Retorna todas as poltronas cadastradas no sistema, convertidas para DTO.
     *
     * @return lista de {@link AirplaneSeatResponseDTO} com código, preço e disponibilidade de cada poltrona
     */
    public List<AirplaneSeatResponseDTO> listAllSeats() {
        return airplaneSeatRepository.findAll().stream()
                .map(seat -> new AirplaneSeatResponseDTO(
                        seat.getId(),
                        seat.getCode(),
                        seat.getPrice(),
                        seat.getAvailable()))
                .toList();
    }

    /**
     * Busca as poltronas pelos códigos fornecidos e valida que todas estão disponíveis.
     *
     * @param seatCodes lista de códigos das poltronas (ex: "1A", "3C")
     * @return lista de entidades de poltrona validadas e disponíveis
     * @throws org.springframework.web.server.ResponseStatusException 404 se algum código não for encontrado
     * @throws org.springframework.web.server.ResponseStatusException 409 se alguma poltrona já estiver reservada
     */
    public List<AirplaneSeatEntity> findAndValidateAvailableSeats(List<String> seatCodes) {
        return seatCodes.stream()
                .map(this::findAvailableSeat)
                .toList();
    }

    /**
     * Marca a poltrona como indisponível e persiste a alteração.
     *
     * @param seat poltrona a ser marcada como indisponível
     * @return entidade {@link AirplaneSeatEntity} atualizada
     */
    public AirplaneSeatEntity markAsUnavailable(AirplaneSeatEntity seat) {
        seat.setAvailable(false);
        return airplaneSeatRepository.save(seat);
    }

    /**
     * Busca uma poltrona pelo código e valida que está disponível.
     *
     * @param code código da poltrona
     * @return entidade {@link AirplaneSeatEntity} disponível
     * @throws org.springframework.web.server.ResponseStatusException 404 se não encontrada
     * @throws org.springframework.web.server.ResponseStatusException 409 se indisponível
     */
    private AirplaneSeatEntity findAvailableSeat(String code) {
        AirplaneSeatEntity seat = airplaneSeatRepository.findByCode(code)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Poltrona não encontrada: " + code));

        if (!seat.getAvailable()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Poltrona indisponível: " + seat.getCode());
        }

        return seat;
    }
}
