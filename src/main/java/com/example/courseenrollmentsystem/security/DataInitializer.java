package com.example.courseenrollmentsystem.security;

import com.example.courseenrollmentsystem.dataModel.UserAccount;
import com.example.courseenrollmentsystem.dataModel.enums.UserRole;
import com.example.courseenrollmentsystem.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // Check if admin already exists
        if (userAccountRepository.findByUsername("admin").isEmpty()) {
            UserAccount userAccount = new UserAccount();
            userAccount.setUsername("admin");
            userAccount.setPasswordHash(passwordEncoder.encode("admin123"));
            userAccount.setRole(UserRole.ADMIN);
            userAccount.setEnabled(true);
            userAccount.setCreatedAt(LocalDateTime.now());
            userAccount.setUpdatedAt(LocalDateTime.now());
            userAccountRepository.save(userAccount);
            System.out.println("Admin user created: admin/admin123");
        }

        // Check if subadmin already exists
        if (userAccountRepository.findByUsername("subadmin").isEmpty()) {
            UserAccount userAccount = new UserAccount();
            userAccount.setUsername("subadmin");
            userAccount.setPasswordHash(passwordEncoder.encode("subadmin123"));
            userAccount.setRole(UserRole.SUBADMIN);
            userAccount.setEnabled(true);
            userAccount.setCreatedAt(LocalDateTime.now());
            userAccount.setUpdatedAt(LocalDateTime.now());
            userAccountRepository.save(userAccount);
            System.out.println("Subadmin user created: subadmin/subadmin123");
        }

        System.out.println("Default users initialization completed");
    }
}

