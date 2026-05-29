package com.ia.para.devs.skybook.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO de saída para representação de erros da API.
 * Retornado pelo {@link com.ia.para.devs.skybook.controller.GlobalExceptionHandler}
 * em situações de erro tratado.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponseDTO {

    /** Status HTTP do erro (ex: NOT_FOUND, CONFLICT). */
    String status;

    /** Mensagem descritiva do erro. */
    String message;

    /** Data e hora em que o erro ocorreu. */
    LocalDateTime timestamp;
}
