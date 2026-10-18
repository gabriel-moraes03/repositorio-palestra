package com.exemplo.treinadores.controller;

import com.exemplo.treinadores.dto.FutebolRequest;
import com.exemplo.treinadores.dto.TreinadorResponse;
import com.exemplo.treinadores.mapper.TreinadorMapper;
import com.exemplo.treinadores.model.TreinadorFutebol;
import com.exemplo.treinadores.service.FutebolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/treinadores/futebol")
public class FutebolController {

    private final FutebolService service;
    private final TreinadorMapper mapper;

    public FutebolController(FutebolService service, TreinadorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // Sem try/catch: erro vira ApiError no ApiExceptionHandler
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TreinadorResponse cadastrar(@Valid @RequestBody FutebolRequest req) {
        TreinadorFutebol criado = req.dataInicio() == null
                ? service.cadastrar(req)
                : service.cadastrar(req, req.dataInicio());
        return mapper.paraResposta(criado);
    }
}
