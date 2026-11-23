package com.merida.tecnm.market_backend;

import com.merida.tecnm.market_backend.persistence.crud.ClienteCrudRepository;
import com.merida.tecnm.market_backend.persistence.entity.Cliente;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class MarketBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner initTestData(ClienteCrudRepository clienteCrudRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			try {
				String realHash1234 = passwordEncoder.encode("1234");

				// Actualiza a cualquier cliente en la BD que tenga contraseña nula con un hash válido de "1234"
				clienteCrudRepository.findAll().forEach(cliente -> {
					if (cliente.getPassword() == null || cliente.getPassword().isBlank()) {
						cliente.setPassword(realHash1234);
						clienteCrudRepository.save(cliente);
					}
				});

				// Asegura la existencia de un usuario de prueba con correo test@market.com y pass 1234
				Optional<Cliente> byEmail = clienteCrudRepository.findByCorreoElectronico("test@market.com");
				if (byEmail.isEmpty()) {
					Cliente testCliente = new Cliente();
					testCliente.setId("CLI-001");
					testCliente.setNombre("Usuario");
					testCliente.setApellidos("Prueba");
					testCliente.setCorreoElectronico("test@market.com");
					testCliente.setPassword(realHash1234);
					clienteCrudRepository.save(testCliente);
				}
			} catch (Exception ignored) {
				// Ignorar si la base de datos no está disponible durante pruebas unitarias
			}
		};
	}
}
