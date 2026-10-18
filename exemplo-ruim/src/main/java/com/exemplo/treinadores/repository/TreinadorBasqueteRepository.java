package com.exemplo.treinadores.repository;

import com.exemplo.treinadores.model.TreinadorBasquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TreinadorBasqueteRepository extends JpaRepository<TreinadorBasquete, Long> {

    // "Otimização": inativa direto no banco, sem buscar antes.
    // Um update precisa de transação, e ninguém abriu uma (falta @Transactional).
    @Modifying
    @Query("update TreinadorBasquete t set t.ativo = false "
            + "where t.pessoa.id = :pessoaId and t.esporte.id = :esporteId and t.ativo = true")
    void inativarAtivos(Long pessoaId, Long esporteId);
}
