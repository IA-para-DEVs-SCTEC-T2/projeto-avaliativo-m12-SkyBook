package com.ia.para.devs.skybook.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ia.para.devs.skybook.dto.AirplaneSeatResponseDTO;
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
}
