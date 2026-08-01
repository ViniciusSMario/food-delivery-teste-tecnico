package com.vinicius.food_delivery_api.repository;

import com.vinicius.food_delivery_api.entity.Pedido;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Override
    @EntityGraph(attributePaths = "itens")
    List<Pedido> findAll();

    @Override
    @EntityGraph(attributePaths = "itens")
    Optional<Pedido> findById(Long id);
}
