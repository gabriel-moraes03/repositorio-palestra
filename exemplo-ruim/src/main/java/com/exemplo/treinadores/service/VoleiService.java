package com.exemplo.treinadores.service;

import com.exemplo.treinadores.model.TreinadorVolei;
import com.exemplo.treinadores.repository.EsporteRepository;
import com.exemplo.treinadores.repository.PessoaRepository;
import com.exemplo.treinadores.repository.TreinadorVoleiRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class VoleiService {

    private final TreinadorVoleiRepository repo;
    private final PessoaRepository pessoaRepo;
    private final EsporteRepository esporteRepo;

    public VoleiService(TreinadorVoleiRepository repo, PessoaRepository pessoaRepo, EsporteRepository esporteRepo) {
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
        // Bug 1: condição invertida (faltou o "!"). Rejeita certificado válido e aceita inválido.
        if (certificadoOk(r)) throw erro();
    }

    private boolean certificadoOk(Map<String, String> r) {
        String certificado = r.get("certificadoCbv");
        String validade = r.get("validadeCertificado");
        return certificado != null && !certificado.isBlank()
                && validade != null && LocalDate.parse(validade).isAfter(LocalDate.now());
    }

    private RuntimeException erro() {
        return new RuntimeException("certificado CBV inválido ou vencido");
    }

    public void inativarAnterior(Map<String, String> r) {
        repo.findByPessoaIdAndEsporteIdAndAtivoTrue(Long.valueOf(r.get("pessoaId")), Long.valueOf(r.get("esporteId")))
                .ifPresent(anterior -> {
                    anterior.setAtivo(false);
                    repo.save(anterior);
                });
    }

    public TreinadorVolei cadastrarSimples(Map<String, String> r) {
        validar(r);
        inativarAnterior(r);
        TreinadorVolei t = new TreinadorVolei();
        t.setPessoa(pessoaRepo.findById(Long.valueOf(r.get("pessoaId"))).get());
        t.setEsporte(esporteRepo.findById(Long.valueOf(r.get("esporteId"))).get());
        t.setCertificadoCbv(r.get("certificadoCbv"));
        t.setValidadeCertificado(r.get("validadeCertificado") == null ? null : LocalDate.parse(r.get("validadeCertificado")));
        t.setAtivo(true);
        t.setDataInicio(LocalDate.now());
        return repo.save(t);
    }

    public TreinadorVolei cadastrarComInicio(Map<String, String> r, LocalDate data) {
        validar(r);
        inativarAnterior(r);
        TreinadorVolei t = new TreinadorVolei();
        t.setPessoa(pessoaRepo.findById(Long.valueOf(r.get("pessoaId"))).get());
        t.setEsporte(esporteRepo.findById(Long.valueOf(r.get("esporteId"))).get());
        t.setCertificadoCbv(r.get("certificadoCbv"));
        t.setValidadeCertificado(r.get("validadeCertificado") == null ? null : LocalDate.parse(r.get("validadeCertificado")));
        t.setAtivo(true);
        t.setDataInicio(data);
        return repo.save(t);
    }
}
