package com.exemplo.treinadores.service;

import com.exemplo.treinadores.erro.ErroDeDominio;
import com.exemplo.treinadores.filtro.Ordenacao;
import com.exemplo.treinadores.filtro.TreinadorFiltro;
import com.exemplo.treinadores.model.Treinador;
import com.exemplo.treinadores.repository.TreinadorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

// Consultas que valem para todos os esportes de uma vez.
@Service
@Transactional(readOnly = true)
public class TreinadorConsultaService {

    private final TreinadorRepository repo;
    private final TreinadorFiltro filtro;
    private final Ordenacao ordenacao;

    public TreinadorConsultaService(TreinadorRepository repo, TreinadorFiltro filtro, Ordenacao ordenacao) {
        this.repo = repo;
        this.filtro = filtro;
        this.ordenacao = ordenacao;
    }

    public Treinador buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> ErroDeDominio.naoEncontrado("Treinador " + id + " não encontrado"));
    }

    // Um método só para qualquer combinação de filtros
    public List<Treinador> listar(Map<String, String> filtros, String sort) {
        Specification<Treinador> spec = filtro.criar(filtros);
        Sort ordem = ordenacao.criar(sort);
        return repo.findAll(spec, ordem);
    }
}
