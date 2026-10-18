package com.exemplo.treinadores.filtro;

import com.exemplo.treinadores.erro.ErroDeDominio;
import com.exemplo.treinadores.model.Treinador;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Map;

// Tabela de regras: chave do filtro -> Specification. Filtro novo = uma linha nova na tabela.
@Component
public class TreinadorFiltro {

    interface Regra {
        Specification<Treinador> criar(String valor);
    }

    private final Map<String, Regra> regras = Map.of(
            "esporte", valor -> (root, query, cb) ->
                    cb.equal(cb.lower(root.get("esporte").get("nome")), valor.toLowerCase()),
            "ativo", valor -> {
                boolean ativo = paraBoolean(valor);
                return (root, query, cb) -> cb.equal(root.get("ativo"), ativo);
            },
            "nome", valor -> (root, query, cb) ->
                    cb.like(cb.lower(root.get("pessoa").get("nome")), "%" + valor.toLowerCase() + "%")
    );

    // Junta com AND as regras pedidas. Chave desconhecida é erro, nunca é ignorada.
    public Specification<Treinador> criar(Map<String, String> params) {
        Specification<Treinador> spec = (root, query, cb) -> cb.conjunction();
        for (Map.Entry<String, String> param : params.entrySet()) {
            Regra regra = regras.get(param.getKey());
            if (regra == null) {
                throw new ErroDeDominio("Filtro desconhecido: " + param.getKey(), param.getKey());
            }
            spec = spec.and(regra.criar(param.getValue()));
        }
        return spec;
    }

    private static boolean paraBoolean(String valor) {
        if (!valor.equals("true") && !valor.equals("false")) {
            throw new ErroDeDominio("ativo deve ser true ou false", "ativo");
        }
        return Boolean.parseBoolean(valor);
    }
}
