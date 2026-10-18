package com.exemplo.treinadores.service;

import com.exemplo.treinadores.dto.VoleiRequest;
import com.exemplo.treinadores.erro.ErroDeDominio;
import com.exemplo.treinadores.model.TreinadorVolei;
import com.exemplo.treinadores.repository.EsporteRepository;
import com.exemplo.treinadores.repository.PessoaRepository;
import com.exemplo.treinadores.repository.TreinadorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VoleiService extends TreinadorService<TreinadorVolei, VoleiRequest> {

    public VoleiService(TreinadorRepository repo, PessoaRepository pessoas, EsporteRepository esportes) {
        super(TreinadorVolei.class, repo, pessoas, esportes);
    }

    @Override
    protected void validarEspecifico(VoleiRequest req) {
        if (!certificadoOk(req)) {
            throw new ErroDeDominio("Certificado CBV ausente ou vencido", "certificadoCbv");
        }
    }

    private boolean certificadoOk(VoleiRequest req) {
        return req.certificadoCbv() != null && !req.certificadoCbv().isBlank()
                && req.validadeCertificado() != null && req.validadeCertificado().isAfter(LocalDate.now());
    }

    @Override
    protected TreinadorVolei criar(VoleiRequest req) {
        TreinadorVolei t = new TreinadorVolei();
        t.setCertificadoCbv(req.certificadoCbv());
        t.setValidadeCertificado(req.validadeCertificado());
        return t;
    }
}
