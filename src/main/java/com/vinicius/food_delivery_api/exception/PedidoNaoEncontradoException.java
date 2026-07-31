package com.vinicius.food_delivery_api.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException(Long id) {
        super("Pedido nao encontrado: " + id);
    }
}
