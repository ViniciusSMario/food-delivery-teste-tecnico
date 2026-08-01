package com.vinicius.food_delivery_api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey chave;
    private final long expiracaoEmMillis;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expiracaoEmMillis) {
        this.chave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracaoEmMillis = expiracaoEmMillis;
    }

    public String gerarToken(String email) {
        Date agora = new Date();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email)
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + expiracaoEmMillis))
                .signWith(chave)
                .compact();
    }

    /**
     * Extrai o e-mail do token. Lanca JwtException se a assinatura for invalida
     * ou se o token estiver expirado.
     */
    public String extrairEmail(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean tokenValido(String token, UserDetails usuario) {
        return extrairEmail(token).equals(usuario.getUsername());
    }
}
