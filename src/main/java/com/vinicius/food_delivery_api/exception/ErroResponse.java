package com.vinicius.food_delivery_api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResponse(

        LocalDateTime timestamp,
        int status,
        String mensagem,
        String path,
        Map<String, String> campos
) {

    public ErroResponse(int status, String mensagem, String path) {
        this(LocalDateTime.now(), status, mensagem, path, null);
    }

    public ErroResponse(int status, String mensagem, String path, Map<String, String> campos) {
        this(LocalDateTime.now(), status, mensagem, path, campos);
    }
}
