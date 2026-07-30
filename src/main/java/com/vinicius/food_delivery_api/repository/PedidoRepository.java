package com.vinicius.food_delivery_api.repository;

import com.vinicius.food_delivery_api.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
