package com.impal.portal.inventory.service;

import com.impal.portal.exception.DuplicateSerialNumberException;
import com.impal.portal.exception.UnauthorizedAccessException;
import com.impal.portal.inventory.dto.SystemDetailsRequestDto;
import com.impal.portal.inventory.entity.SystemDetails;
import com.impal.portal.inventory.repository.SystemDetailsRepository;
import com.impal.portal.user.Role;
import com.impal.portal.user.entity.User;
import com.impal.portal.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SystemDetailsService {

    private final SystemDetailsRepository systemRepo;
    private final UserRepository userRepository;

    public SystemDetailsService(SystemDetailsRepository systemRepo,
                                UserRepository userRepository) {
        this.systemRepo = systemRepo;
        this.userRepository = userRepository;
    }

    // ➕ Add system (EDP only)
    public SystemDetails addSystem(SystemDetailsRequestDto dto, String username) {

        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 Role check
        if (loggedInUser.getRole() != Role.EDP) {
            throw new UnauthorizedAccessException(
                    "Only EDP users can add system details");
        }

        // 🔁 Serial number uniqueness
        if (systemRepo.existsBySerialNumber(dto.getSerialNumber())) {
            throw new DuplicateSerialNumberException(
                    "System with this serial number already exists");
        }

        SystemDetails system = new SystemDetails();
        system.setEmployeeCode(dto.getEmployeeCode());
        system.setEmployeeName(dto.getEmployeeName());
        system.setDesignation(dto.getDesignation());
        system.setSystemType(dto.getSystemType());
        system.setSerialNumber(dto.getSerialNumber());
        system.setProcessor(dto.getProcessor());
        system.setRam(dto.getRam());
        system.setStorage(dto.getStorage());
        system.setOs(dto.getOs());

        // 🔒 Branch & audit
        system.setBranch(loggedInUser.getBranch());
        system.setCreatedBy(loggedInUser);
        system.setCreatedAt(LocalDateTime.now());

        return systemRepo.save(system);
    }

    // 📋 List systems (branch-wise)
    public List<SystemDetails> getSystems(String username) {

        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return systemRepo.findByBranch(loggedInUser.getBranch());
    }
}
