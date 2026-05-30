package com.ia.para.devs.skybook.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO de saída para o resumo consolidado das reservas de um passageiro.
 * Contém os dados do passageiro, a lista de poltronas reservadas
 * e o valor total calculado.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingSummaryResponseDTO {

    /** Nome do passageiro. */
    String passengerName;

    /** E-mail do passageiro. */
    String passengerEmail;

    /** Valor total somado de todas as poltronas reservadas. */
    BigDecimal totalAmount;

    /** Lista de poltronas reservadas com código, preço individual e data da reserva. */
    List<BookingItemDTO> bookings;
}
