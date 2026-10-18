package com.exemplo.treinadores.model;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class TreinadorVolei extends Treinador {

    private String certificadoCbv;
    private LocalDate validadeCertificado;

    public String getCertificadoCbv() { return certificadoCbv; }
    public void setCertificadoCbv(String certificadoCbv) { this.certificadoCbv = certificadoCbv; }
    public LocalDate getValidadeCertificado() { return validadeCertificado; }
    public void setValidadeCertificado(LocalDate validadeCertificado) { this.validadeCertificado = validadeCertificado; }
}
