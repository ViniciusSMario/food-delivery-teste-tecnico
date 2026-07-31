package com.vinicius.food_delivery_api.repository;

import com.vinicius.food_delivery_api.entity.Pedido;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {"cliente", "itens"})
    List<Pedido> findByClienteEmail(String email);

    @EntityGraph(attributePaths = {"cliente", "itens"})
    Optional<Pedido> findByIdAndClienteEmail(Long id, String email);
}
