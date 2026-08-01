package com.vinicius.food_delivery_api.service;

import com.vinicius.food_delivery_api.dto.AtualizarStatusRequest;
import com.vinicius.food_delivery_api.dto.ItemPedidoRequest;
import com.vinicius.food_delivery_api.dto.ItemPedidoResponse;
import com.vinicius.food_delivery_api.dto.PedidoRequest;
import com.vinicius.food_delivery_api.dto.PedidoResponse;
import com.vinicius.food_delivery_api.entity.ItemPedido;
import com.vinicius.food_delivery_api.entity.Pedido;
import com.vinicius.food_delivery_api.entity.StatusPedido;
import com.vinicius.food_delivery_api.exception.PedidoNaoEncontradoException;
import com.vinicius.food_delivery_api.repository.PedidoRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public PedidoResponse criar(PedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setCliente(request.cliente());
        pedido.setEnderecoEntrega(request.enderecoEntrega());
        pedido.setStatus(StatusPedido.RECEBIDO);
        pedido.setDataCriacao(LocalDateTime.now());
        request.itens().forEach(item -> adicionarItem(pedido, item));

        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return toResponse(buscarOuFalhar(id));
    }

    @Transactional
    public PedidoResponse atualizarStatus(Long id, AtualizarStatusRequest request) {
        Pedido pedido = buscarOuFalhar(id);
        pedido.setStatus(request.status());

        return toResponse(pedidoRepository.save(pedido));
    }

    private Pedido buscarOuFalhar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));
    }

    private void adicionarItem(Pedido pedido, ItemPedidoRequest request) {
        ItemPedido item = new ItemPedido();
        item.setNome(request.nome());
        item.setQuantidade(request.quantidade());
        item.setPreco(request.preco());
        item.setPedido(pedido);
        pedido.getItens().add(item);
    }

    private PedidoResponse toResponse(Pedido pedido) {
        List<ItemPedidoResponse> itens = pedido.getItens().stream()
                .map(item -> new ItemPedidoResponse(
                        item.getId(), item.getNome(), item.getQuantidade(), item.getPreco()))
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente(),
                pedido.getEnderecoEntrega(),
                pedido.getStatus(),
                pedido.getDataCriacao(),
                itens);
    }
}
