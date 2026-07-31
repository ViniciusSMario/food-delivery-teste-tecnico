package com.vinicius.food_delivery_api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ----- 400 -----

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException e,
                                                        HttpServletRequest request) {
        Map<String, String> campos = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.put(erro.getField(), erro.getDefaultMessage()));

        return construir(HttpStatus.BAD_REQUEST, "Dados invalidos", request, campos);
    }

    /**
     * Corpo que o Jackson nao consegue desserializar. O caso mais comum aqui e um
     * valor que nao existe no enum, como {"status": "VOANDO"}.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarCorpoInvalido(HttpMessageNotReadableException e,
                                                            HttpServletRequest request) {
        if (e.getCause() instanceof InvalidFormatException causa && causa.getTargetType().isEnum()) {
            String aceitos = Arrays.stream(causa.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            return construir(HttpStatus.BAD_REQUEST,
                    "Valor invalido: '" + causa.getValue() + "'. Valores aceitos: " + aceitos, request);
        }

        return construir(HttpStatus.BAD_REQUEST, "Corpo da requisicao invalido", request);
    }

    /** Parametro de URL com tipo errado, como /pedidos/abc onde se espera um numero. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarTipoInvalido(MethodArgumentTypeMismatchException e,
                                                           HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST,
                "Valor invalido para o parametro '" + e.getName() + "'", request);
    }

    // ----- 401 -----

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErroResponse> tratarCredenciaisInvalidas(HttpServletRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, "E-mail ou senha invalidos", request);
    }

    // ----- 404 -----

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarUsuarioNaoEncontrado(UsuarioNaoEncontradoException e,
                                                                   HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarPedidoNaoEncontrado(PedidoNaoEncontradoException e,
                                                                  HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    /** Rota que nao existe. Sem isso, cairia no handler generico e viraria 500. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> tratarRotaNaoEncontrada(HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "Recurso nao encontrado", request);
    }

    // ----- 409 -----

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResponse> tratarEmailJaCadastrado(EmailJaCadastradoException e,
                                                                HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    // ----- 500 -----

    /**
     * Rede de seguranca. Registra o erro completo no log do servidor e devolve uma
     * mensagem generica, para nao expor detalhes internos da aplicacao ao cliente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroInterno(Exception e, HttpServletRequest request) {
        log.error("Erro nao tratado em {} {}", request.getMethod(), request.getRequestURI(), e);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", request);
    }

    private ResponseEntity<ErroResponse> construir(HttpStatus status, String mensagem,
                                                   HttpServletRequest request) {
        return construir(status, mensagem, request, null);
    }

    private ResponseEntity<ErroResponse> construir(HttpStatus status, String mensagem,
                                                   HttpServletRequest request,
                                                   Map<String, String> campos) {
        ErroResponse corpo = new ErroResponse(
                status.value(), mensagem, request.getRequestURI(), campos);
        return ResponseEntity.status(status).body(corpo);
    }
}
