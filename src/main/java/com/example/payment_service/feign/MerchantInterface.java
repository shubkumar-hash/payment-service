package com.example.payment_service.feign;

import com.example.payment_service.dto.MerchantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MERCHANT-SERVICE")
public interface MerchantInterface{

    @GetMapping("/api/merchants/{id}")
    MerchantResponse getMerchant(@PathVariable("id") UUID id);

}
