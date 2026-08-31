/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.security;

import app.service.AktivniKorisniciService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author PC
 */
@Component
public class JwtFilter extends OncePerRequestFilter{

    private final JwtUtil jwtUtil;
    private final AktivniKorisniciService aktivniKorisniciService;

    public JwtFilter(JwtUtil jwtUtil, AktivniKorisniciService aktivniKorisniciService) {
        this.jwtUtil = jwtUtil;
        this.aktivniKorisniciService = aktivniKorisniciService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(jwtUtil.validateToken(token)){
                String korisnickoIme = jwtUtil.extractKorisnickoIme(token);
                aktivniKorisniciService.zabeleziAktivnost(korisnickoIme);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(korisnickoIme, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
