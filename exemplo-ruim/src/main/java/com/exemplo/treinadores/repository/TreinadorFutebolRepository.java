package com.exemplo.treinadores.repository;

import com.exemplo.treinadores.model.TreinadorFutebol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreinadorFutebolRepository extends JpaRepository<TreinadorFutebol, Long> {

    Optional<TreinadorFutebol> findByPessoaIdAndEsporteIdAndAtivoTrue(Long pessoaId, Long esporteId);
}
