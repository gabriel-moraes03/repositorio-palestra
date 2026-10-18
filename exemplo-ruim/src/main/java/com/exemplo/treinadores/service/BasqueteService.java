package com.exemplo.treinadores.service;

import com.exemplo.treinadores.model.TreinadorBasquete;
import com.exemplo.treinadores.repository.EsporteRepository;
import com.exemplo.treinadores.repository.PessoaRepository;
import com.exemplo.treinadores.repository.TreinadorBasqueteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class BasqueteService {

    private final TreinadorBasqueteRepository repo;
    private final PessoaRepository pessoaRepo;
    private final EsporteRepository esporteRepo;

    public BasqueteService(TreinadorBasqueteRepository repo, PessoaRepository pessoaRepo, EsporteRepository esporteRepo) {
        this.repo = repo;
        this.pessoaRepo = pessoaRepo;
        this.esporteRepo = esporteRepo;
    }

    public void validar(Map<String, String> r) {
        if (!pessoaRepo.existsById(Long.valueOf(r.get("pessoaId")))) {
            throw new RuntimeException("pessoa não encontrada");
        }
        if (!esporteRepo.existsById(Long.valueOf(r.get("esporteId")))) {
            throw new RuntimeException("esporte não encontrado");
        }
        if (!registroOk(r)) throw erro();
    }

    private boolean registroOk(Map<String, String> r) {
        String registro = r.get("registroFederacao");
        return registro != null && !registro.isBlank();
    }

    private RuntimeException erro() {
        return new RuntimeException("registro da federação obrigatório");
    }

    public void inativarAnterior(Map<String, String> r) {
        // Bug 2: a CAUSA é a transação ausente (o update em inativarAtivos não tem @Transactional).
        // O que ESCONDEU o erro foi este catch vazio: o anterior continua ativo e ninguém vê.
        try {
            repo.inativarAtivos(Long.valueOf(r.get("pessoaId")), Long.valueOf(r.get("esporteId")));
        } catch (Exception e) {
        }
    }

    public TreinadorBasquete cadastrarSimples(Map<String, String> r) {
        validar(r);
        inativarAnterior(r);
        TreinadorBasquete t = new TreinadorBasquete();
        t.setPessoa(pessoaRepo.findById(Long.valueOf(r.get("pessoaId"))).get());
        t.setEsporte(esporteRepo.findById(Long.valueOf(r.get("esporteId"))).get());
        t.setRegistroFederacao(r.get("registroFederacao"));
        t.setAtivo(true);
        t.setDataInicio(LocalDate.now());
        return repo.save(t);
    }

    public TreinadorBasquete cadastrarComInicio(Map<String, String> r, LocalDate data) {
        validar(r);
        inativarAnterior(r);
        TreinadorBasquete t = new TreinadorBasquete();
        t.setPessoa(pessoaRepo.findById(Long.valueOf(r.get("pessoaId"))).get());
        t.setEsporte(esporteRepo.findById(Long.valueOf(r.get("esporteId"))).get());
        t.setRegistroFederacao(r.get("registroFederacao"));
        t.setAtivo(true);
        t.setDataInicio(data);
        return repo.save(t);
    }
}
