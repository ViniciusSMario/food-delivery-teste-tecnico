package com.vinicius.food_delivery_api.dto;

import java.math.BigDecimal;

public record ItemPedidoResponse(

        Long id,
        String nome,
        Integer quantidade,
        BigDecimal preco
) {
}
