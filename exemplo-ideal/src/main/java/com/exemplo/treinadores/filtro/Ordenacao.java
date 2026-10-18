package com.exemplo.treinadores.filtro;

import com.exemplo.treinadores.erro.ErroDeDominio;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Map;

// Chave aceita na URL -> propriedade da entidade. Formato: "nome" ou "nome,desc".
@Component
public class Ordenacao {

    private final Map<String, String> permitidas = Map.of(
            "nome", "pessoa.nome",
            "dataInicio", "dataInicio"
    );

    public Sort criar(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by("id");
        }
        String[] partes = sort.split(",");
        String propriedade = permitidas.get(partes[0]);
        if (propriedade == null) {
            throw new ErroDeDominio("Ordenação desconhecida: " + partes[0], "sort");
        }
        if (partes.length == 1 || partes[1].equals("asc")) {
            return Sort.by(propriedade).ascending();
        }
        if (partes[1].equals("desc")) {
            return Sort.by(propriedade).descending();
        }
        throw new ErroDeDominio("Direção de ordenação deve ser asc ou desc", "sort");
    }
}
