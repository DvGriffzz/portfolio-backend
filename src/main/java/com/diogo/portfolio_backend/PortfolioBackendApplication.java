package com.diogo.portfolio_backend;

import com.diogo.portfolio_backend.admin.AdminUser;
import com.diogo.portfolio_backend.admin.AdminUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PortfolioBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(AdminUserRepository repo, PasswordEncoder encoder,
								@Value("${admin.username}") String username,
								@Value("${admin.password}") String password) {
		return args -> {
			if (repo.findByUsername(username).isEmpty()) {
				AdminUser admin = new AdminUser();
				admin.setUsername(username);
				admin.setPassword(encoder.encode(password));
				repo.save(admin);
				System.out.println("✅ Admin user created: " + username);
			}
		};
	}
}
