package com.ia.para.devs.skybook.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO de entrada para criação de uma ou mais reservas de poltronas.
 * Recebe os dados do passageiro e os códigos das poltronas desejadas.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDTO {

    /** Nome do passageiro. */
    String passengerName;

    /** E-mail do passageiro. */
    String passengerEmail;

    /**
     * Lista de códigos das poltronas a serem reservadas (ex: "1A", "3C").
     * Deve conter ao menos um elemento.
     */
    List<String> seatCodes;
}
