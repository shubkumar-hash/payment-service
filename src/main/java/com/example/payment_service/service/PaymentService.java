package com.example.payment_service.service;

import com.example.payment_service.dto.BankResponse;
import com.example.payment_service.enums.PaymentStatus;
import com.example.payment_service.enums.TransactionStatus;
import com.example.payment_service.exception.PaymentNotFoundException;
import com.example.payment_service.feign.BankInterface;
import com.example.payment_service.model.Payment;
import com.example.payment_service.model.Transaction;
import com.example.payment_service.repository.PaymentRepository;
import com.example.payment_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final BankInterface bankInterface;

    public Payment createPayment(String key, Payment payment){

        Optional<Payment> existingPayment =
                paymentRepository.findByIdempotencyKey(key);

        if(existingPayment.isPresent()){
            return existingPayment.get();
        }

        payment.setIdempotencyKey(key);
        payment.setStatus(PaymentStatus.CREATED);
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Payment getPayment(Long id){

        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
    }

    public PaymentStatus processPayment(Long paymentId){

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        BankResponse bankResponse = bankInterface.processPayment();

        TransactionStatus txnStatus = bankResponse.getStatus();

        if(txnStatus == TransactionStatus.SUCCESS){
            payment.setStatus(PaymentStatus.SUCCESS);
        }
        else{
            payment.setStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);

        Transaction tx = new Transaction();
        tx.setPaymentId(paymentId);
        tx.setBankReference(bankResponse.getBankReference());
        tx.setStatus(txnStatus);
        tx.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(tx);

        return payment.getStatus();
    }
}