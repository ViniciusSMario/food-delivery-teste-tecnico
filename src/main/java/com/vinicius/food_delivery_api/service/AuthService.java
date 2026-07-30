package com.vinicius.food_delivery_api.service;

import com.vinicius.food_delivery_api.dto.AuthResponse;
import com.vinicius.food_delivery_api.dto.LoginRequest;
import com.vinicius.food_delivery_api.dto.UsuarioRequest;
import com.vinicius.food_delivery_api.entity.Usuario;
import com.vinicius.food_delivery_api.exception.EmailJaCadastradoException;
import com.vinicius.food_delivery_api.repository.UsuarioRepository;
import com.vinicius.food_delivery_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse registrar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailJaCadastradoException(request.email());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));

        Usuario salvo = usuarioRepository.save(usuario);
        return new AuthResponse(jwtService.gerarToken(salvo.getEmail()));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        return new AuthResponse(jwtService.gerarToken(request.email()));
    }
}
