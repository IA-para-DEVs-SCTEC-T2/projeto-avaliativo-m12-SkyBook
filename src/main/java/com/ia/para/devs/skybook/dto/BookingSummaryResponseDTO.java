package com.ia.para.devs.skybook.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO de saída para o resumo consolidado das reservas realizadas.
 * Contém a lista de poltronas reservadas com seus preços individuais
 * e o valor total calculado.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingSummaryResponseDTO {

    /** Lista de poltronas reservadas com código e preço individual. */
    List<BookingResponseDTO> bookings;

    /** Valor total somado de todas as poltronas reservadas. */
    BigDecimal totalAmount;
}
