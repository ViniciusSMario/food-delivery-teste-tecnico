package com.vinicius.food_delivery_api.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("E-mail ja cadastrado: " + email);
    }
}
