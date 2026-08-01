package com.vinicius.food_delivery_api.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String CABECALHO = "Authorization";
    private static final String PREFIXO = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = extrairToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            autenticar(token, request);
        }

        filterChain.doFilter(request, response);
    }

    private String extrairToken(HttpServletRequest request) {
        String cabecalho = request.getHeader(CABECALHO);
        if (cabecalho == null || !cabecalho.startsWith(PREFIXO)) {
            return null;
        }
        return cabecalho.substring(PREFIXO.length());
    }

    private void autenticar(String token, HttpServletRequest request) {
        try {
            String email = jwtService.extrairEmail(token);
            UserDetails usuario = userDetailsService.loadUserByUsername(email);

            if (jwtService.tokenValido(token, usuario)) {
                var autenticacao = new UsernamePasswordAuthenticationToken(
                        usuario, null, usuario.getAuthorities());
                autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            }
        } catch (JwtException | UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
        }
    }
}
