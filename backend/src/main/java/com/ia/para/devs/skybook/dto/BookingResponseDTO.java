package com.ia.para.devs.skybook.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO de saída para representação de uma reserva criada.
 * Retornado após a criação bem-sucedida de uma reserva individual.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponseDTO {

    /** Identificador único da reserva. */
    Long bookingId;

    /** Código da poltrona reservada (ex: 1A, 3C). */
    String seatCode;

    /** Preço da poltrona reservada. */
    BigDecimal seatPrice;

    /** Nome do passageiro que realizou a reserva. */
    String passengerName;

    /** Data e hora em que a reserva foi realizada. */
    LocalDateTime bookedAt;
}
