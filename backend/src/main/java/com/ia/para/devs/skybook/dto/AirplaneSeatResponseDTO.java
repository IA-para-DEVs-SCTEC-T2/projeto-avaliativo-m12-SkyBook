package com.ia.para.devs.skybook.dto;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO de saída para representação de uma poltrona da aeronave.
 * Utilizado nos endpoints de listagem para evitar exposição direta da entidade JPA.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirplaneSeatResponseDTO {

    /** Identificador único da poltrona. */
    Long id;

    /** Código da poltrona (ex: 1A, 2B). */
    String code;

    /** Preço da poltrona. */
    BigDecimal price;

    /** Indica se a poltrona está disponível para reserva. */
    Boolean available;
}
