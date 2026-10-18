package com.exemplo.treinadores.service;

import com.exemplo.treinadores.dto.BasqueteRequest;
import com.exemplo.treinadores.erro.ErroDeDominio;
import com.exemplo.treinadores.model.TreinadorBasquete;
import com.exemplo.treinadores.repository.EsporteRepository;
import com.exemplo.treinadores.repository.PessoaRepository;
import com.exemplo.treinadores.repository.TreinadorRepository;
import org.springframework.stereotype.Service;

@Service
public class BasqueteService extends TreinadorService<TreinadorBasquete, BasqueteRequest> {

    public BasqueteService(TreinadorRepository repo, PessoaRepository pessoas, EsporteRepository esportes) {
        super(TreinadorBasquete.class, repo, pessoas, esportes);
    }

    @Override
    protected void validarEspecifico(BasqueteRequest req) {
        if (req.registroFederacao() == null || req.registroFederacao().isBlank()) {
            throw new ErroDeDominio("Registro da federação obrigatório", "registroFederacao");
        }
    }

    @Override
    protected TreinadorBasquete criar(BasqueteRequest req) {
        TreinadorBasquete t = new TreinadorBasquete();
        t.setRegistroFederacao(req.registroFederacao());
        return t;
    }
}
