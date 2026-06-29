package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.LoginDTO;
import com.example.distAPI.model.entity.Usuario;
import com.example.distAPI.security.JwtService;
import com.example.distAPI.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO credentials) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getSenha())
        );
        
        Usuario usuario = (Usuario) usuarioService.loadUserByUsername(credentials.getEmail()); 
        String token = jwtService.gerarToken(usuario);
        
        return Map.of("token", token);
    }
}