package com.ticket.seat_service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return List.of(
            Map.of("id", 1, "seatNumber", "12A", "scheduleId", 1, "status", "BOOKED"),
            Map.of("id", 2, "seatNumber", "12B", "scheduleId", 1, "status", "AVAILABLE"),
            Map.of("id", 3, "seatNumber", "14A", "scheduleId", 2, "status", "AVAILABLE"),
            Map.of("id", 4, "seatNumber", "14B", "scheduleId", 2, "status", "BOOKED")
        );
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable int id) {
        return Map.of("id", id, "seatNumber", "12A", "scheduleId", 1, "status", "BOOKED");
    }
}
