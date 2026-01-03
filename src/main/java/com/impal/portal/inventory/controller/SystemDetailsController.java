package com.impal.portal.inventory.controller;

import com.impal.portal.inventory.dto.SystemDetailsRequestDto;
import com.impal.portal.inventory.entity.SystemDetails;
import com.impal.portal.inventory.service.SystemDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/systems")
public class SystemDetailsController {

    private final SystemDetailsService systemDetailsService;

    public SystemDetailsController(SystemDetailsService systemDetailsService) {
        this.systemDetailsService = systemDetailsService;
    }

    // ➕ ADD SYSTEM
    @PostMapping
    public ResponseEntity<SystemDetails> addSystem(
            @RequestBody SystemDetailsRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        SystemDetails saved =
                systemDetailsService.addSystem(dto, userDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    // 📋 LIST SYSTEMS
    @GetMapping
    public ResponseEntity<List<SystemDetails>> getSystems(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<SystemDetails> systems =
                systemDetailsService.getSystems(userDetails.getUsername());

        return ResponseEntity.ok(systems);
    }
}
