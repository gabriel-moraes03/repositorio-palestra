package com.exemplo.treinadores.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import java.time.LocalDate;

// Mesmos campos de TreinadorFutebol e TreinadorBasquete, copiados. Sem classe base.
@Entity
public class TreinadorVolei {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treinador_seq")
    @SequenceGenerator(name = "treinador_seq", sequenceName = "treinador_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Pessoa pessoa;

    @ManyToOne
    private Esporte esporte;

    private boolean ativo;
    private LocalDate dataInicio;
    private String certificadoCbv;
    private LocalDate validadeCertificado;

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
    public String getCertificadoCbv() { return certificadoCbv; }
    public void setCertificadoCbv(String certificadoCbv) { this.certificadoCbv = certificadoCbv; }
    public LocalDate getValidadeCertificado() { return validadeCertificado; }
    public void setValidadeCertificado(LocalDate validadeCertificado) { this.validadeCertificado = validadeCertificado; }
}
