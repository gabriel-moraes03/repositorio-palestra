package com.exemplo.treinadores.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

// Campos comuns ficam aqui, uma vez só.
// JOINED: tabela "treinador" com o comum + uma tabela por esporte só com o campo próprio.
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Treinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pessoa pessoa;

    @ManyToOne(optional = false)
    private Esporte esporte;

    private boolean ativo;
    private LocalDate dataInicio;

    // Sobrescrita: cada esporte descreve a própria habilitação.
    // Quem chama não precisa saber qual é o esporte.
    public abstract String habilitacao();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }
    public Esporte getEsporte() { return esporte; }
    public void setEsporte(Esporte esporte) { this.esporte = esporte; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
}
