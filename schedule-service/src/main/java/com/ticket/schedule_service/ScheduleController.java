package com.ticket.schedule_service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return List.of(
            Map.of("id", 1, "route", "Jember - Surabaya", "time", "08:00", "price", 120000),
            Map.of("id", 2, "route", "Jember - Malang", "time", "10:00", "price", 100000)
        );
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable int id) {
        return Map.of("id", id, "route", "Jember - Surabaya", "time", "08:00");
    }
}