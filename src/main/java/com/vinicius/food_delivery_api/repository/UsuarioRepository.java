package com.vinicius.food_delivery_api.repository;

import com.vinicius.food_delivery_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
