package com.vinicius.food_delivery_api.dto;

import com.vinicius.food_delivery_api.entity.StatusPedido;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(

        Long id,
        String cliente,
        String enderecoEntrega,
        StatusPedido status,
        LocalDateTime dataCriacao,
        List<ItemPedidoResponse> itens
) {
}
