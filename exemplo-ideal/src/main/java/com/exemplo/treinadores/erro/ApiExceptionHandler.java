package com.exemplo.treinadores.erro;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Um lugar só para transformar exceção em resposta.
// Controllers não têm try/catch.
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ErroDeDominio.class)
    public ResponseEntity<ApiError> tratar(ErroDeDominio e) {
        ApiError erro = ApiError.de(e);
        return ResponseEntity.status(erro.status()).body(erro);
    }

    // Erros do Bean Validation (@NotNull etc.) saem no mesmo formato
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacao(
            MethodArgumentNotValidException e) {
        // Pode ser erro de campo ou do objeto inteiro (sem campo)
        ObjectError primeiro = e.getBindingResult().getAllErrors().get(0);
        String campo = primeiro instanceof FieldError f ? f.getField() : null;
        ApiError erro = new ApiError(
                400, primeiro.getDefaultMessage(), campo);
        return ResponseEntity.badRequest().body(erro);
    }
}
