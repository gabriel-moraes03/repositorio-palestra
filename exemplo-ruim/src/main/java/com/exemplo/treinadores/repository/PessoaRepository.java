package com.exemplo.treinadores.repository;

import com.exemplo.treinadores.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
