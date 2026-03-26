package com.renteasy.config;

import com.renteasy.model.Role;
import com.renteasy.model.User;
import com.renteasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User tenant = User.builder()
                    .name("Test Tenant")
                    .email("tenant@test.com")
                    .password(passwordEncoder.encode("password"))
                    .phone("1234567890")
                    .role(Role.TENANT)
                    .build();
            userRepository.save(tenant);

            User owner = User.builder()
                    .name("Test Owner")
                    .email("owner@test.com")
                    .password(passwordEncoder.encode("password"))
                    .phone("0987654321")
                    .role(Role.OWNER)
                    .build();
            userRepository.save(owner);

            System.out.println("Initialized database with test users: tenant@test.com / password and owner@test.com / password");
        }
    }
}
