package com.vinicius.food_delivery_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ItemPedidoRequest(

        @NotBlank
        String nome,

        @NotNull
        @Positive
        Integer quantidade,

        @NotNull
        @Positive
        BigDecimal preco
) {
}
