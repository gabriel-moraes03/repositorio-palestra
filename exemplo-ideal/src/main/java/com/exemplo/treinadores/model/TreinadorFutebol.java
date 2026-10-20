package com.exemplo.treinadores.model;

import jakarta.persistence.Entity;

@Entity
public class TreinadorFutebol extends Treinador {

    private String nivelLicenca;

    @Override
    public String habilitacao() {
        return "Licença " + nivelLicenca;
    }

    public String getNivelLicenca() { return nivelLicenca; }
    public void setNivelLicenca(String nivelLicenca) { this.nivelLicenca = nivelLicenca; }
}
