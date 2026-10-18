package com.exemplo.treinadores.service;

import com.exemplo.treinadores.dto.TreinadorRequest;
import com.exemplo.treinadores.erro.ErroDeDominio;
import com.exemplo.treinadores.model.Treinador;
import com.exemplo.treinadores.repository.EsporteRepository;
import com.exemplo.treinadores.repository.PessoaRepository;
import com.exemplo.treinadores.repository.TreinadorRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// T = tipo de treinador, R = tipo de pedido. Cada esporte só diz o que é diferente.
@Transactional
public abstract class TreinadorService<T extends Treinador, R extends TreinadorRequest> implements BaseService<T, Long> {

    private final Class<T> tipo;
    protected final TreinadorRepository repo;
    private final PessoaRepository pessoas;
    private final EsporteRepository esportes;

    protected TreinadorService(Class<T> tipo, TreinadorRepository repo, PessoaRepository pessoas, EsporteRepository esportes) {
        this.tipo = tipo;
        this.repo = repo;
        this.pessoas = pessoas;
        this.esportes = esportes;
    }

    // ---- BaseService<T, Long> ----

    @Override
    public T buscar(Long id) {
        return repo.findById(id)
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .orElseThrow(() -> ErroDeDominio.naoEncontrado("Treinador " + id + " não encontrado"));
    }

    @Override
    public List<T> listar() {
        return repo.findByTipo(tipo).stream().map(tipo::cast).toList();
    }

    @Override
    public T cadastrar(T treinador) {
        return salvar(treinador);
    }

    @Override
    public void inativar(Long id) {
        T treinador = buscar(id);
        treinador.setAtivo(false);
        repo.save(treinador);
    }

    // ---- cadastro a partir do pedido ----

    // Sobrecarga que delega: sem data de início, começa hoje.
    public T cadastrar(R req) {
        return cadastrar(req, LocalDate.now());
    }

    // Template Method: a ordem dos passos é fixa e igual para todos os esportes.
    public T cadastrar(R req, LocalDate inicio) {
        validarComum(req);
        validarEspecifico(req);
        inativarAnterior(req);
        T novo = criar(req);
        preencherComum(novo, req, inicio);
        return salvar(novo);
    }

    // Os dois passos que cada esporte sobrescreve
    protected abstract void validarEspecifico(R req);

    protected abstract T criar(R req);

    protected void validarComum(R req) {
        if (!pessoas.existsById(req.pessoaId())) {
            throw new ErroDeDominio("Pessoa não encontrada", "pessoaId");
        }
        if (!esportes.existsById(req.esporteId())) {
            throw new ErroDeDominio("Esporte não encontrado", "esporteId");
        }
    }

    protected void inativarAnterior(R req) {
        for (Treinador anterior : repo.findByPessoaIdAndEsporteIdAndAtivoTrue(req.pessoaId(), req.esporteId())) {
            inativar(anterior.getId());
        }
    }

    protected T salvar(T treinador) {
        return repo.save(treinador);
    }

    private void preencherComum(T novo, R req, LocalDate inicio) {
        novo.setPessoa(pessoas.findById(req.pessoaId()).orElseThrow());
        novo.setEsporte(esportes.findById(req.esporteId()).orElseThrow());
        novo.setAtivo(true);
        novo.setDataInicio(inicio);
    }
}
