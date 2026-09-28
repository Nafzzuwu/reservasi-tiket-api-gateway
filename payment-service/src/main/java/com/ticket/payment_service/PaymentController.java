package com.ticket.payment_service;

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

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final List<Map<String, Object>> payments = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(3);

    public PaymentController() {
        payments.add(new HashMap<>(Map.of(
            "id", 1,
            "bookingId", 1,
            "amount", 120000,
            "status", "SUCCESS",
            "paymentMethod", "QRIS"
        )));
        payments.add(new HashMap<>(Map.of(
            "id", 2,
            "bookingId", 2,
            "amount", 100000,
            "status", "SUCCESS",
            "paymentMethod", "BANK_TRANSFER"
        )));
    }

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return payments;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return payments.stream()
            .filter(p -> Objects.equals(p.get("id"), id))
            .findFirst()
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Payment with id " + id + " not found")));
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> request) {
        Object bookingId = request.get("bookingId");
        Object amount = request.get("amount");
        Object paymentMethod = request.getOrDefault("paymentMethod", "QRIS");

        if (bookingId == null || amount == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "bookingId and amount are required"
            ));
        }

        Map<String, Object> newPayment = new HashMap<>();
        newPayment.put("id", idGenerator.getAndIncrement());
        newPayment.put("bookingId", bookingId);
        newPayment.put("amount", amount);
        newPayment.put("paymentMethod", paymentMethod);
        newPayment.put("status", "SUCCESS");

        payments.add(newPayment);

        return ResponseEntity.status(HttpStatus.CREATED).body(newPayment);
    }
}
