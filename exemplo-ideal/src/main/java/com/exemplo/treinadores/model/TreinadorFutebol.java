package com.exemplo.treinadores.model;

import jakarta.persistence.Entity;

@Entity
public class TreinadorFutebol extends Treinador {

    private String nivelLicenca;

    public String getNivelLicenca() { return nivelLicenca; }
    public void setNivelLicenca(String nivelLicenca) { this.nivelLicenca = nivelLicenca; }
}
