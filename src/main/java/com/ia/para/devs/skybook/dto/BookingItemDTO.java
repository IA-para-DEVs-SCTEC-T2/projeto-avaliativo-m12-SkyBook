package com.ia.para.devs.skybook.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO que representa um item individual dentro do resumo de reservas.
 * Contém os dados de uma única poltrona reservada.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingItemDTO {

    /** Identificador único da reserva. */
    Long bookingId;

    /** Código da poltrona reservada (ex: 1A, 3C). */
    String seatCode;

    /** Preço individual da poltrona reservada. */
    BigDecimal seatPrice;

    /** Data e hora em que a reserva foi realizada. */
    LocalDateTime bookedAt;
}
