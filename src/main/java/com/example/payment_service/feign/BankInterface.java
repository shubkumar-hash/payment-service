package com.example.payment_service.feign;

import com.example.payment_service.dto.BankResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "BANK-SIMULATOR-SERVICE")
public interface BankInterface {

    @PostMapping("/bank/pay")
    BankResponse processPayment();
}
