package com.impal.portal.config;

import com.impal.portal.user.Role;
import com.impal.portal.user.entity.User;
import com.impal.portal.user.repository.UserRepository;
import com.impal.portal.branch.entity.Branch;
import com.impal.portal.branch.repository.BranchRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           BranchRepository branchRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {

        // 1️⃣ Ensure Branch exists
        Branch branch = branchRepository.findById(1L)
                .orElseGet(() -> {
                    Branch b = new Branch();
                    b.setBranchCode("HYD");
                    b.setBranchName("Hyderabad");
                    b.setLocation("Hyderabad");
                    return branchRepository.save(b);
                });

        // 2️⃣ Create EDP user if not exists
        if (userRepository.findByUsername("edp1").isEmpty()) {

            User user = new User();
            user.setUsername("edp1");
            user.setPassword(passwordEncoder.encode("impal@123"));
            user.setRole(Role.EDP);
            user.setActive(true);
            user.setBranch(branch);

            userRepository.save(user);

            System.out.println("✅ Default EDP user created");
        }
    }
}
