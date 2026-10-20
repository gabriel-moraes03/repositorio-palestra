package com.exemplo.treinadores.controller;

import com.exemplo.treinadores.dto.TreinadorResponse;
import com.exemplo.treinadores.mapper.TreinadorMapper;
import com.exemplo.treinadores.service.TreinadorConsultaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/treinadores")
public class TreinadorController {

    private final TreinadorConsultaService consulta;
    private final TreinadorMapper mapper;

    public TreinadorController(TreinadorConsultaService consulta, TreinadorMapper mapper) {
        this.consulta = consulta;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public TreinadorResponse buscar(@PathVariable Long id) {
        return mapper.paraResposta(consulta.buscar(id));
    }

    // Um método para qualquer esporte: o objeto decide (sobrescrita)
    @GetMapping("/{id}/habilitacao")
    public String habilitacao(@PathVariable Long id) {
        return consulta.buscar(id).habilitacao();
    }

    // Ex.: /treinadores?esporte=futebol&ativo=true&nome=ana&sort=nome,desc
    @GetMapping
    public List<TreinadorResponse> listar(@RequestParam Map<String, String> params) {
        Map<String, String> filtros = new HashMap<>(params);
        String sort = filtros.remove("sort");
        return consulta.listar(filtros, sort).stream().map(mapper::paraResposta).toList();
    }
}
