package com.ticket.booking_service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final List<Map<String, Object>> bookings = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(3);
    private final RestClient restClient;

    public BookingController() {
        this.restClient = RestClient.builder()
            .baseUrl("http://localhost:8102")
            .build();

        // Dummy data
        bookings.add(new HashMap<>(Map.of(
            "id", 1,
            "scheduleId", 1,
            "seatNumber", "12A",
            "userId", 1,
            "status", "CONFIRMED"
        )));
        bookings.add(new HashMap<>(Map.of(
            "id", 2,
            "scheduleId", 2,
            "seatNumber", "14B",
            "userId", 2,
            "status", "CONFIRMED"
        )));
    }

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return bookings;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return bookings.stream()
            .filter(b -> Objects.equals(b.get("id"), id))
            .findFirst()
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Booking with id " + id + " not found")));
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> request) {
        Object scheduleIdObj = request.get("scheduleId");
        Object seatNumberObj = request.get("seatNumber");
        Object userIdObj = request.get("userId");

        if (scheduleIdObj == null || seatNumberObj == null || userIdObj == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "scheduleId, seatNumber, and userId are required"
            ));
        }

        // Panggil schedule-service memakai RestClient untuk memvalidasi jadwal
        Map<?, ?> schedule;
        try {
            schedule = restClient.get()
                .uri("/api/schedules/{id}", scheduleIdObj)
                .retrieve()
                .body(Map.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "Schedule validation failed: " + e.getMessage()
            ));
        }

        if (schedule == null || schedule.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "Schedule with id " + scheduleIdObj + " not found"
            ));
        }

        // Simpan booking di memori
        Map<String, Object> newBooking = new HashMap<>();
        newBooking.put("id", idGenerator.getAndIncrement());
        newBooking.put("scheduleId", scheduleIdObj);
        newBooking.put("seatNumber", seatNumberObj);
        newBooking.put("userId", userIdObj);
        newBooking.put("schedule", schedule);
        newBooking.put("status", "CONFIRMED");

        bookings.add(newBooking);

        return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
    }
}
