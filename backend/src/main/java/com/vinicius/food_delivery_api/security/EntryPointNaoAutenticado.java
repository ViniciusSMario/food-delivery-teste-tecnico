package com.vinicius.food_delivery_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinicius.food_delivery_api.exception.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Requisicao sem token, ou com token invalido, e barrada pelo filtro do Spring Security
 * antes de chegar a qualquer controller. Por isso o @RestControllerAdvice nao a alcanca,
 * e o corpo do 401 precisa ser escrito aqui para seguir o mesmo padrao dos demais erros.
 */
@Component
@RequiredArgsConstructor
public class EntryPointNaoAutenticado implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException excecao) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErroResponse corpo = new ErroResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Autenticacao necessaria",
                request.getRequestURI());

        objectMapper.writeValue(response.getWriter(), corpo);
    }
}
