package com.example.payment_service.dto;

import com.example.payment_service.enums.TransactionStatus;
import lombok.Data;

@Data
public class BankResponse {

    private TransactionStatus status;
    private String bankReference;
}
