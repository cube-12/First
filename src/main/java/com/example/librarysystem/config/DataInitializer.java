package com.example.librarysystem.config;

import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@library.com");
                admin.setFirstName("Admin");
                admin.setLastName("Root");
                admin.setRole("ADMIN");
                admin.setActive(true);
                userRepository.save(admin);
                logger.info("默认管理员账号已创建: admin / admin123");
            }
        };
    }
}
