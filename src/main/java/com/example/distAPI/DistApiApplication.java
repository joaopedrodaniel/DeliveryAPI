package com.example.distAPI;

import com.example.distAPI.model.entity.Administrador;
import com.example.distAPI.model.repository.AdministradorRepository;
import com.example.distAPI.service.AdministradorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class DistApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistApiApplication.class, args);
	}

@Bean
	CommandLineRunner initDatabase(
			com.example.distAPI.model.repository.AdministradorRepository adminRepo, 
			org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
		
		return args -> {
			var adminOptional = adminRepo.findByEmail("master@admin.com");
			if (adminOptional.isEmpty()) {
				com.example.distAPI.model.entity.Administrador admin = new com.example.distAPI.model.entity.Administrador();
				admin.setNome("Master Admin");
				admin.setEmail("master@admin.com");
				admin.setSenha(passwordEncoder.encode("admin123"));
				admin.setTemAcesso(true);
				admin.setPerfil(com.example.distAPI.model.entity.PerfilUsuario.ADMIN);
				adminRepo.save(admin);
				System.out.println("Administrador MASTER criado com sucesso! (master@admin.com / admin123)");
			} else {
				com.example.distAPI.model.entity.Administrador admin = adminOptional.get();
				if (!passwordEncoder.matches("admin123", admin.getSenha())) {
					admin.setSenha(passwordEncoder.encode("admin123"));
					admin.setTemAcesso(true);
					adminRepo.save(admin);
					System.out.println("Administrador MASTER existente atualizado com senha admin123.");
				}
			}
		};
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}