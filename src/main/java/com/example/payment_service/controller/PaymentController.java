package com.example.payment_service.controller;

import com.example.payment_service.enums.PaymentStatus;
import com.example.payment_service.model.Payment;
import com.example.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@RequestHeader("X-Merchant-Id") UUID merchantId, @RequestHeader("Idempotency-Key") String key, @RequestBody Payment payment){

        Payment createdPayment = paymentService.createPayment(merchantId,key, payment);

        return ResponseEntity.ok(createdPayment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id){

        Payment payment = paymentService.getPayment(id);

        return ResponseEntity.ok(payment);
    }

    @PostMapping("/process/{id}")
    public ResponseEntity<String> processPayment(@PathVariable Long id){

        PaymentStatus result = paymentService.processPayment(id);

        return ResponseEntity.ok(result.toString());
    }
}