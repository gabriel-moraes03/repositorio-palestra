package com.exemplo.treinadores.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BasqueteRequest(
        @NotNull(message = "pessoaId é obrigatório") Long pessoaId,
        @NotNull(message = "esporteId é obrigatório") Long esporteId,
        LocalDate dataInicio,
        String registroFederacao
) implements TreinadorRequest {
}
