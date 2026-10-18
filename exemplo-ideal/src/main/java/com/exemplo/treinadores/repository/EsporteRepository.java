package com.exemplo.treinadores.repository;

import com.exemplo.treinadores.model.Esporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EsporteRepository extends JpaRepository<Esporte, Long> {

    Optional<Esporte> findByNome(String nome);
}
