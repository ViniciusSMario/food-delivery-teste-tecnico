package com.vinicius.food_delivery_api.dto;

import com.vinicius.food_delivery_api.entity.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(

        @NotNull
        StatusPedido status
) {
}
