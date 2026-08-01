package com.vinicius.food_delivery_api.controller;

import com.vinicius.food_delivery_api.dto.AtualizarStatusRequest;
import com.vinicius.food_delivery_api.dto.PedidoRequest;
import com.vinicius.food_delivery_api.dto.PedidoResponse;
import com.vinicius.food_delivery_api.service.PedidoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(@RequestBody @Valid PedidoRequest request) {
        return pedidoService.criar(request);
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return pedidoService.listar();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @PutMapping("/{id}/status")
    public PedidoResponse atualizarStatus(@PathVariable Long id,
                                          @RequestBody @Valid AtualizarStatusRequest request) {
        return pedidoService.atualizarStatus(id, request);
    }
}
