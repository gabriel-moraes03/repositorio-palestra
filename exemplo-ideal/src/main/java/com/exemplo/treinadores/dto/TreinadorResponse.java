package com.exemplo.treinadores.dto;

import java.time.LocalDate;

public record TreinadorResponse(
        Long id,
        Long pessoaId,
        String nome,
        String esporte,
        boolean ativo,
        LocalDate dataInicio
) {
}
