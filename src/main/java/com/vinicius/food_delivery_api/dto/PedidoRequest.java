package com.vinicius.food_delivery_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PedidoRequest(

        @NotBlank
        String enderecoEntrega,

        @NotEmpty
        @Valid
        List<ItemPedidoRequest> itens
) {
}
