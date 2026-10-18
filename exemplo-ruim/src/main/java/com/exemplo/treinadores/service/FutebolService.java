package com.exemplo.treinadores.service;

import com.exemplo.treinadores.model.TreinadorFutebol;
import com.exemplo.treinadores.repository.EsporteRepository;
import com.exemplo.treinadores.repository.PessoaRepository;
import com.exemplo.treinadores.repository.TreinadorFutebolRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class FutebolService {

    private final TreinadorFutebolRepository repo;
    private final PessoaRepository pessoaRepo;
    private final EsporteRepository esporteRepo;

    public FutebolService(TreinadorFutebolRepository repo, PessoaRepository pessoaRepo, EsporteRepository esporteRepo) {
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
        if (!nivelOk(r)) throw erro();
    }

    private boolean nivelOk(Map<String, String> r) {
        String nivel = r.get("nivelLicenca");
        return nivel != null && List.of("C", "B", "A", "PRO").contains(nivel);
    }

    private RuntimeException erro() {
        return new RuntimeException("nível de licença inválido");
    }

    public void inativarAnterior(Map<String, String> r) {
        repo.findByPessoaIdAndEsporteIdAndAtivoTrue(Long.valueOf(r.get("pessoaId")), Long.valueOf(r.get("esporteId")))
                .ifPresent(anterior -> {
                    anterior.setAtivo(false);
                    repo.save(anterior);
                });
    }

    public TreinadorFutebol cadastrarSimples(Map<String, String> r) {
        validar(r);
        inativarAnterior(r);
        TreinadorFutebol t = new TreinadorFutebol();
        t.setPessoa(pessoaRepo.findById(Long.valueOf(r.get("pessoaId"))).get());
        t.setEsporte(esporteRepo.findById(Long.valueOf(r.get("esporteId"))).get());
        t.setNivelLicenca(r.get("nivelLicenca"));
        t.setAtivo(true);
        t.setDataInicio(LocalDate.now());
        return repo.save(t);
    }

    // Mesma ideia de cadastrarSimples, com outro nome e o corpo copiado. Só muda a data.
    public TreinadorFutebol cadastrarComInicio(Map<String, String> r, LocalDate data) {
        validar(r);
        inativarAnterior(r);
        TreinadorFutebol t = new TreinadorFutebol();
        t.setPessoa(pessoaRepo.findById(Long.valueOf(r.get("pessoaId"))).get());
        t.setEsporte(esporteRepo.findById(Long.valueOf(r.get("esporteId"))).get());
        t.setNivelLicenca(r.get("nivelLicenca"));
        t.setAtivo(true);
        t.setDataInicio(data);
        return repo.save(t);
    }
}
