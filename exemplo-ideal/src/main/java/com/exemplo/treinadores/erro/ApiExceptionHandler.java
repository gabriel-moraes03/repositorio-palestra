package com.exemplo.treinadores.erro;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Um lugar só para transformar exceção em resposta. Controllers não têm try/catch.
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ErroDeDominio.class)
    public ResponseEntity<ApiError> erroDeDominio(ErroDeDominio e) {
        ApiError erro = ApiError.de(e);
        return ResponseEntity.status(erro.status()).body(erro);
    }

    // Erros do Bean Validation (@NotNull etc.) saem no mesmo formato
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacao(MethodArgumentNotValidException e) {
        FieldError campo = e.getBindingResult().getFieldError();
        return ResponseEntity.badRequest().body(new ApiError(400, campo.getDefaultMessage(), campo.getField()));
    }
}
