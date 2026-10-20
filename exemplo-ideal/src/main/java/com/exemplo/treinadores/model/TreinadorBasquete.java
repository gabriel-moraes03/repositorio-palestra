package com.exemplo.treinadores.model;

import jakarta.persistence.Entity;

@Entity
public class TreinadorBasquete extends Treinador {

    private String registroFederacao;

    @Override
    public String habilitacao() {
        return "Registro " + registroFederacao;
    }

    public String getRegistroFederacao() { return registroFederacao; }
    public void setRegistroFederacao(String registroFederacao) { this.registroFederacao = registroFederacao; }
}
