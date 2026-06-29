package com.example.distAPI.config;

import com.example.distAPI.security.JwtAuthFilter;
import com.example.distAPI.security.JwtService;
import com.example.distAPI.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder; 

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    // Libera o login e o Swagger para todos
                    .requestMatchers("/api/v1/auth/login").permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    
                    .requestMatchers(HttpMethod.POST, "/api/v1/vendedores").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/clientes").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/produtos/**")
                    .hasAnyRole("ADMIN","VENDEDOR")

                    .requestMatchers(HttpMethod.DELETE, "/api/v1/produtos/**")
                    .hasRole("ADMIN")

                    .requestMatchers("/api/v1/administradores/**")
                    .hasRole("ADMIN")
                    
                    // Todo o resto precisa de autenticação (Token)
                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/v3/api-docs/**",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**"
        );
    }
}