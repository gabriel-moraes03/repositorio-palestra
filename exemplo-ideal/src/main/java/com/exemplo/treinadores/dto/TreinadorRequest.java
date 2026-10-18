package com.exemplo.treinadores.dto;

import java.time.LocalDate;

// O que todo pedido de cadastro tem, qualquer que seja o esporte.
public interface TreinadorRequest {

    Long pessoaId();

    Long esporteId();

    LocalDate dataInicio();
}
