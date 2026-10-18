package com.exemplo.treinadores.repository;

import com.exemplo.treinadores.model.Treinador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TreinadorRepository extends JpaRepository<Treinador, Long>, JpaSpecificationExecutor<Treinador> {

    List<Treinador> findByPessoaIdAndEsporteIdAndAtivoTrue(Long pessoaId, Long esporteId);

    // só os treinadores de uma subclasse (ex.: TreinadorFutebol)
    @Query("select t from Treinador t where type(t) = :tipo")
    List<Treinador> findByTipo(Class<? extends Treinador> tipo);
}
