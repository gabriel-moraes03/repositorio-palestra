package com.exemplo.treinadores.service;

import com.exemplo.treinadores.dto.FutebolRequest;
import com.exemplo.treinadores.erro.ErroDeDominio;
import com.exemplo.treinadores.model.TreinadorFutebol;
import com.exemplo.treinadores.repository.EsporteRepository;
import com.exemplo.treinadores.repository.PessoaRepository;
import com.exemplo.treinadores.repository.TreinadorRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FutebolService extends TreinadorService<TreinadorFutebol, FutebolRequest> {

    private static final Set<String> NIVEIS = Set.of("C", "B", "A", "PRO");

    public FutebolService(TreinadorRepository repo, PessoaRepository pessoas, EsporteRepository esportes) {
        super(TreinadorFutebol.class, repo, pessoas, esportes);
    }

    @Override
    protected void validarEspecifico(FutebolRequest req) {
        if (req.nivelLicenca() == null || !NIVEIS.contains(req.nivelLicenca())) {
            throw new ErroDeDominio("Nível de licença inválido. Use C, B, A ou PRO", "nivelLicenca");
        }
    }

    @Override
    protected TreinadorFutebol criar(FutebolRequest req) {
        TreinadorFutebol t = new TreinadorFutebol();
        t.setNivelLicenca(req.nivelLicenca());
        return t;
    }
}
