package com.vinicius.food_delivery_api.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(String email) {
        super("Usuario nao encontrado: " + email);
    }
}
