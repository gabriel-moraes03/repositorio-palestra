package com.exemplo.treinadores.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

// Um método para cada combinação de filtro. Cada filtro novo = mais métodos.
// A ordem é sempre por nome, fixa na consulta.
@Repository
public class TreinadorRepository {

    @PersistenceContext
    private EntityManager em;

    public List findByEsporte(String esporte) {
        if (esporte.equals("futebol")) {
            return em.createQuery("select t from TreinadorFutebol t where t.esporte.nome = :esporte order by t.pessoa.nome")
                    .setParameter("esporte", esporte).getResultList();
        } else if (esporte.equals("volei")) {
            return em.createQuery("select t from TreinadorVolei t where t.esporte.nome = :esporte order by t.pessoa.nome")
                    .setParameter("esporte", esporte).getResultList();
        } else if (esporte.equals("basquete")) {
            return em.createQuery("select t from TreinadorBasquete t where t.esporte.nome = :esporte order by t.pessoa.nome")
                    .setParameter("esporte", esporte).getResultList();
        }
        return new ArrayList<>();
    }

    public List findByEsporteAndAtivo(String esporte, boolean ativo) {
        if (esporte.equals("futebol")) {
            return em.createQuery("select t from TreinadorFutebol t where t.esporte.nome = :esporte and t.ativo = :ativo order by t.pessoa.nome")
                    .setParameter("esporte", esporte).setParameter("ativo", ativo).getResultList();
        } else if (esporte.equals("volei")) {
            return em.createQuery("select t from TreinadorVolei t where t.esporte.nome = :esporte and t.ativo = :ativo order by t.pessoa.nome")
                    .setParameter("esporte", esporte).setParameter("ativo", ativo).getResultList();
        } else if (esporte.equals("basquete")) {
            return em.createQuery("select t from TreinadorBasquete t where t.esporte.nome = :esporte and t.ativo = :ativo order by t.pessoa.nome")
                    .setParameter("esporte", esporte).setParameter("ativo", ativo).getResultList();
        }
        return new ArrayList<>();
    }

    // nível só existe no futebol
    public List findByEsporteAndNivelAndAtivo(String esporte, String nivel, boolean ativo) {
        return em.createQuery("select t from TreinadorFutebol t where t.esporte.nome = :esporte and t.nivelLicenca = :nivel and t.ativo = :ativo order by t.pessoa.nome")
                .setParameter("esporte", esporte).setParameter("nivel", nivel).setParameter("ativo", ativo).getResultList();
    }

    public List findByNomeAndEsporteAndNivel(String nome, String esporte, String nivel) {
        return em.createQuery("select t from TreinadorFutebol t where t.pessoa.nome = :nome and t.esporte.nome = :esporte and t.nivelLicenca = :nivel order by t.pessoa.nome")
                .setParameter("nome", nome).setParameter("esporte", esporte).setParameter("nivel", nivel).getResultList();
    }
}
