package com.exemplo.treinadores.erro;

import com.fasterxml.jackson.annotation.JsonInclude;

// O único formato de erro da API. "campo" só aparece quando existe.
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(int status, String mensagem, String campo) {

    public static ApiError de(ErroDeDominio e) {
        return new ApiError(e.getStatus(), e.getMessage(), e.getCampo());
    }
}
