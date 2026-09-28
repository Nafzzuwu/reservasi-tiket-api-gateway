package com.ticket.user_service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return List.of(
            Map.of("id", 1, "name", "Budi Santoso", "email", "budi@example.com", "phone", "081234567890"),
            Map.of("id", 2, "name", "Siti Aminah", "email", "siti@example.com", "phone", "081298765432")
        );
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable int id) {
        return Map.of("id", id, "name", "Budi Santoso", "email", "budi@example.com", "phone", "081234567890");
    }
}
