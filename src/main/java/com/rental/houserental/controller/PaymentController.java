package com.rental.houserental.controller;

import com.rental.houserental.config.EsewaSignatureUtil;
import com.rental.houserental.entity.Payment;
import com.rental.houserental.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${esewa.secret.key}")
    private String secretKey;

    @Value("${esewa.product.code}")
    private String productCode;

    // =========================
    // INITIATE PAYMENT
    // =========================
    @PostMapping("/initiate-v2")
    public Map<String, Object> initiateV2(@RequestBody Map<String, Object> request) {
        String transactionUuid = UUID.randomUUID().toString();

        // 1. Standardize calculations
        double amount = Double.parseDouble(request.get("amount").toString());
        double tax = 0.0;
        double totalAmount = amount + tax;

        // 2. Format specifically for eSewa (No decimals for whole numbers)
        // This ensures "120.0" becomes "120"
        String totalAmountStr = String.format("%.0f", totalAmount);
        String amountStr = String.format("%.0f", amount);
        String taxStr = "0";

        // 3. Save to Database
        Payment payment = new Payment();
        payment.setTransactionId(transactionUuid);
        payment.setAmount(totalAmount);
        payment.setStatus("PENDING");
        paymentRepository.save(payment);

        // 4. Create Signature String (NO SPACES after commas)
        String signatureData = "total_amount=" + totalAmountStr +
                ",transaction_uuid=" + transactionUuid +
                ",product_code=" + productCode;

        String signature = EsewaSignatureUtil.generateSignature(signatureData, secretKey);

        // 5. Prepare Response
        Map<String, Object> response = new HashMap<>();
        response.put("transaction_uuid", transactionUuid);
        response.put("amount", amountStr);
        response.put("tax_amount", taxStr);
        response.put("total_amount", totalAmountStr);
        response.put("product_code", productCode);
        response.put("signature", signature);

        return response;
    }

    // =========================
    // MARK SUCCESS
    // =========================
    @PostMapping("/success")
    public Map<String, Object> markSuccess(@RequestParam String transactionId) {

        Optional<Payment> optionalPayment = paymentRepository.findByTransactionId(transactionId);

        Map<String, Object> res = new HashMap<>();

        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();

            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            res.put("message", "Payment SUCCESS");
            res.put("status", "SUCCESS");
        } else {
            res.put("message", "Payment not found");
            res.put("status", "ERROR");
        }

        return res;
    }

    // =========================
    // MARK FAILED
    // =========================
    @PostMapping("/failed")
    public Map<String, Object> markFailed(@RequestParam String transactionId) {

        Payment payment = paymentRepository.findByTransactionId(transactionId).get();

        Map<String, Object> res = new HashMap<>();

        if (payment != null) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);

            res.put("message", "Payment FAILED");
            res.put("status", "FAILED");
        } else {
            res.put("message", "Payment not found");
            res.put("status", "ERROR");
        }

        return res;
    }

    // =========================
    // GET ALL PAYMENTS
    // =========================
    @GetMapping("/all")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // =========================
    // GET PAYMENT BY DB ID
    // =========================
    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentRepository.findById(id).orElse(null);
    }
}