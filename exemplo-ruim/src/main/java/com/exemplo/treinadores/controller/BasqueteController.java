package com.exemplo.treinadores.controller;

import com.exemplo.treinadores.service.BasqueteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/treinadores/basquete")
public class BasqueteController {

    private final BasqueteService service;

    public BasqueteController(BasqueteService service) {
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
            // formato de erro 3: stack trace no console e 500 sem corpo
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
