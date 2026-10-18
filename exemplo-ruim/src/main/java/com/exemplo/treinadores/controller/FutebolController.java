package com.exemplo.treinadores.controller;

import com.exemplo.treinadores.service.FutebolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/treinadores/futebol")
public class FutebolController {

    private final FutebolService service;

    public FutebolController(FutebolService service) {
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
            // formato de erro 1: 400 com texto puro
            return ResponseEntity.badRequest().body("erro: " + e.getMessage());
        }
    }
}
