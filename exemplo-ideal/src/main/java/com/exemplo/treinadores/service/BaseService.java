package com.exemplo.treinadores.service;

import java.util.List;

// Contrato genérico: serve para qualquer entidade T com id do tipo ID.
public interface BaseService<T, ID> {

    T buscar(ID id);

    List<T> listar();

    T cadastrar(T t);

    void inativar(ID id);
}
