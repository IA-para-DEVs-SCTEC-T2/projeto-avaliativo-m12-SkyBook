package com.ia.para.devs.skybook.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ia.para.devs.skybook.dto.AirplaneSeatResponseDTO;
import com.ia.para.devs.skybook.service.AirplaneSeatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controller responsável pelos endpoints de gerenciamento de poltronas da aeronave.
 */
@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
@Tag(name = "Seats", description = "Gerenciamento de poltronas da aeronave")
public class AirplaneSeatController {

    private final AirplaneSeatService airplaneSeatService;

    /**
     * Lista todas as poltronas da aeronave com seu código, preço e status de disponibilidade.
     *
     * @return lista de {@link AirplaneSeatResponseDTO} com todas as poltronas cadastradas
     */
    @GetMapping("/listSeats")
    @Operation(summary = "Listar poltronas", description = "Retorna todas as poltronas com código, preço e status de disponibilidade")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de poltronas retornada com sucesso")
    })
    public ResponseEntity<List<AirplaneSeatResponseDTO>> listAllSeats() {
        return ResponseEntity.ok(airplaneSeatService.listAllSeats());
    }
}
