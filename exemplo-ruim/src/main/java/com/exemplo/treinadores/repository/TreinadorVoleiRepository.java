package com.exemplo.treinadores.repository;

import com.exemplo.treinadores.model.TreinadorVolei;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreinadorVoleiRepository extends JpaRepository<TreinadorVolei, Long> {

    Optional<TreinadorVolei> findByPessoaIdAndEsporteIdAndAtivoTrue(Long pessoaId, Long esporteId);
}
