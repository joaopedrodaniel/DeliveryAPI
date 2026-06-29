package com.example.distAPI.service;

import com.example.distAPI.model.repository.AdministradorRepository;
import com.example.distAPI.model.repository.ClienteRepository;
import com.example.distAPI.model.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final AdministradorRepository adminRepo;
    private final ClienteRepository clienteRepo;
    private final VendedorRepository vendedorRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return adminRepo.findByEmail(email)
            .map(u -> (UserDetails) u)
            .orElseGet(() -> clienteRepo.findByEmail(email)
                .map(u -> (UserDetails) u)
                .orElseGet(() -> vendedorRepo.findByEmail(email)
                    .map(u -> (UserDetails) u) 
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email))
                )
            );
    }
}