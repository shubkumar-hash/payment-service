package com.example.payment_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MerchantResponse {
    public enum MerchantStatus {
        PENDING,
        ACTIVE,
        SUSPENDED,
        BLOCKED,
        TERMINATED
    }
    public enum PlanType {
        FREE,
        PREMIUM
    }
    private UUID merchantId;
    private String businessName;
    private String email;
    private MerchantStatus status;
    private PlanType planType;
}
