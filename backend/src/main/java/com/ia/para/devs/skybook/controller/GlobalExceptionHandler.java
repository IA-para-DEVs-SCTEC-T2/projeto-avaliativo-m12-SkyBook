package com.ia.para.devs.skybook.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.ia.para.devs.skybook.dto.ErrorResponseDTO;

/**
 * Handler global de exceções da API.
 * Captura erros lançados pelos controllers e retorna respostas padronizadas via {@link ErrorResponseDTO}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura {@link ResponseStatusException} lançadas pelo serviço
     * (ex: poltrona não encontrada ou indisponível) e retorna o status HTTP correspondente.
     *
     * @param ex exceção capturada
     * @return {@link ResponseEntity} com {@link ErrorResponseDTO} e o status HTTP da exceção
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return ResponseEntity
                .status(status)
                .body(new ErrorResponseDTO(status.name(), ex.getReason(), LocalDateTime.now()));
    }

    /**
     * Captura exceções genéricas não tratadas e retorna status 500.
     *
     * @param ex exceção capturada
     * @return {@link ResponseEntity} com {@link ErrorResponseDTO} e status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage(), LocalDateTime.now()));
    }
}
