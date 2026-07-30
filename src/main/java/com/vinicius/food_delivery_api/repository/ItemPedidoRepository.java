package com.vinicius.food_delivery_api.repository;

import com.vinicius.food_delivery_api.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
