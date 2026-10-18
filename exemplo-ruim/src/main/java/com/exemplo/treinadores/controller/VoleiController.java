package com.exemplo.treinadores.controller;

import com.exemplo.treinadores.service.VoleiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/treinadores/volei")
public class VoleiController {

    private final VoleiService service;

    public VoleiController(VoleiService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Map<String, String> r) {
        try {
            if (r.get("dataInicio") != null) {
                return ResponseEntity.ok(service.cadastrarComInicio(r, LocalDate.parse(r.get("dataInicio"))));
            }
            return ResponseEntity.ok(service.cadastrarSimples(r));
        } catch (Exception e) {
            // formato de erro 2: 200 (!) com um Map
            return ResponseEntity.ok(Map.of("msg", e.getMessage()));
        }
    }
}
