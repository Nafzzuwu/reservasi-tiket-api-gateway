package com.ticket.notification_service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return List.of(
            Map.of("id", 1, "userId", 1, "message", "Tiket rute Jember - Surabaya berhasil dipesan", "channel", "EMAIL", "status", "SENT"),
            Map.of("id", 2, "userId", 2, "message", "Pembayaran tiket berhasil dikonfirmasi", "channel", "WHATSAPP", "status", "SENT")
        );
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable int id) {
        return Map.of("id", id, "userId", 1, "message", "Tiket rute Jember - Surabaya berhasil dipesan", "channel", "EMAIL", "status", "SENT");
    }
}
