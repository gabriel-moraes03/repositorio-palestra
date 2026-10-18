package com.exemplo.treinadores.mapper;

import com.exemplo.treinadores.dto.TreinadorResponse;
import com.exemplo.treinadores.model.Treinador;
import org.springframework.stereotype.Component;

// Mapper manual: entidade -> resposta. Um método serve para os três esportes.
@Component
public class TreinadorMapper {

    public TreinadorResponse paraResposta(Treinador t) {
        return new TreinadorResponse(
                t.getId(),
                t.getPessoa().getId(),
                t.getPessoa().getNome(),
                t.getEsporte().getNome(),
                t.isAtivo(),
                t.getDataInicio()
        );
    }
}
