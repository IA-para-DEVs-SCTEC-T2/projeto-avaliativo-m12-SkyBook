package com.ia.para.devs.skybook.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ia.para.devs.skybook.dto.BookingRequestDTO;
import com.ia.para.devs.skybook.dto.BookingResponseDTO;
import com.ia.para.devs.skybook.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controller responsável pelos endpoints de reserva de poltronas.
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Gerenciamento de reservas de poltronas")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Cria reservas para uma ou mais poltronas disponíveis.
     * Após a reserva, o status de cada poltrona é atualizado para indisponível.
     *
     * @param request DTO com dados do passageiro e IDs das poltronas a reservar
     * @return lista de {@link BookingResponseDTO} com os detalhes de cada reserva criada
     */
    @PostMapping
    @Operation(
        summary = "Reservar poltronas",
        description = "Cria reservas para uma ou mais poltronas disponíveis e atualiza seu status para indisponível"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reservas criadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Poltrona não encontrada"),
        @ApiResponse(responseCode = "409", description = "Poltrona indisponível para reserva")
    })
    public ResponseEntity<List<BookingResponseDTO>> createBookings(@RequestBody BookingRequestDTO request) {
        List<BookingResponseDTO> response = bookingService.createBookings(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
