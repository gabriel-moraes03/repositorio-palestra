package com.exemplo.treinadores.controller;

import com.exemplo.treinadores.repository.TreinadorBasqueteRepository;
import com.exemplo.treinadores.repository.TreinadorFutebolRepository;
import com.exemplo.treinadores.repository.TreinadorRepository;
import com.exemplo.treinadores.repository.TreinadorVoleiRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/treinadores")
public class TreinadorController {

    private final TreinadorRepository repo;
    private final TreinadorFutebolRepository futebolRepo;
    private final TreinadorVoleiRepository voleiRepo;
    private final TreinadorBasqueteRepository basqueteRepo;

    public TreinadorController(TreinadorRepository repo, TreinadorFutebolRepository futebolRepo,
                               TreinadorVoleiRepository voleiRepo, TreinadorBasqueteRepository basqueteRepo) {
        this.repo = repo;
        this.futebolRepo = futebolRepo;
        this.voleiRepo = voleiRepo;
        this.basqueteRepo = basqueteRepo;
    }

    // procura nas três tabelas, uma depois da outra
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        if (futebolRepo.findById(id).isPresent()) return ResponseEntity.ok(futebolRepo.findById(id).get());
        if (voleiRepo.findById(id).isPresent()) return ResponseEntity.ok(voleiRepo.findById(id).get());
        if (basqueteRepo.findById(id).isPresent()) return ResponseEntity.ok(basqueteRepo.findById(id).get());
        return ResponseEntity.notFound().build();
    }

    // Escada de if/else: uma combinação de filtros por ramo. O sort é recebido e ignorado.
    @GetMapping
    public List listar(@RequestParam(required = false) String esporte,
                       @RequestParam(required = false) Boolean ativo,
                       @RequestParam(required = false) String nome,
                       @RequestParam(required = false) String nivel,
                       @RequestParam(required = false) String sort) {
        if (nome != null && esporte != null && nivel != null) {
            return repo.findByNomeAndEsporteAndNivel(nome, esporte, nivel);
        } else if (esporte != null && nivel != null && ativo != null) {
            return repo.findByEsporteAndNivelAndAtivo(esporte, nivel, ativo);
        } else if (esporte != null && ativo != null) {
            return repo.findByEsporteAndAtivo(esporte, ativo);
        } else if (esporte != null) {
            return repo.findByEsporte(esporte);
        } else {
            // combinação não prevista (ex.: só "nome"): devolve tudo, sem avisar
            List todos = new ArrayList<>();
            todos.addAll(futebolRepo.findAll());
            todos.addAll(voleiRepo.findAll());
            todos.addAll(basqueteRepo.findAll());
            return todos;
        }
    }
}
