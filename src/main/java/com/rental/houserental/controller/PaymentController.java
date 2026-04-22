package com.rental.houserental.controller;

import com.rental.houserental.config.EsewaSignatureUtil;
import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.entity.Admin;
import com.rental.houserental.entity.Payment;
import com.rental.houserental.entity.Property;
import com.rental.houserental.mapper.PaymentCalculator;
import com.rental.houserental.repository.AdminRepository;
import com.rental.houserental.repository.PaymentRepository;
import com.rental.houserental.repository.PropertyRepository;
import com.rental.houserental.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private CustomUserDetails customUserDetails;


    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PaymentCalculator paymentCalculator;

    @Value("${esewa.secret.key}")
    private String secretKey;

    @Value("${esewa.product.code}")
    private String productCode;

    @Value("${app.admin.email}")
    private String adminEmail;

    // INITIATE PAYMENT
    @PostMapping("/initiate-v2/{id}")
    public Map<String, Object> initiateV2(@PathVariable Long id) {
        Optional<Property> optionalProperty = propertyRepository.findById(id);
        Optional<Admin> admin=adminRepository.findByEmail(adminEmail);


        // Check if property exists
        if (optionalProperty.isEmpty()) {
            throw new RuntimeException("Property not found with id: " + id);
        }

        Property property = optionalProperty.get();

        String transactionUuid = UUID.randomUUID().toString();

        // Calculate amount
        double amount = paymentCalculator.calculation(property.getPrice());
        double tax = 0.0;
        double totalAmount = amount + tax;

        // Format values for eSewa
        String totalAmountStr = String.format("%.0f", totalAmount);
        String amountStr = String.format("%.0f", amount);
        String taxStr = "0";

        // Save payment in database
        Payment payment = new Payment();
        payment.setTransactionId(transactionUuid);
        payment.setAmount(totalAmount);
        payment.setStatus("PENDING");
        payment.setPropertyId(property.getId());
        payment.setOwner(property.getOwner());
        payment.setPropertyName(property.getTitle());
        payment.setAdmin(admin.get());
        payment.setPayTo(adminRepository.findByEmail(adminEmail).get().getFullName());
        payment.setPayerName(property.getOwner().getFullName());
        paymentRepository.save(payment);

        // Generate signature
        String signatureData = "total_amount=" + totalAmountStr +
                ",transaction_uuid=" + transactionUuid +
                ",product_code=" + productCode;

        String signature = EsewaSignatureUtil.generateSignature(signatureData, secretKey);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("transaction_uuid", transactionUuid);
        response.put("amount", amountStr);
        response.put("tax_amount", taxStr);
        response.put("total_amount", totalAmountStr);
        response.put("product_code", productCode);
        response.put("signature", signature);
        response.put("propertyId", property.getId());
        response.put("propertyName", property.getTitle());

        return response;
    }

    // MARK PAYMENT SUCCESS
    @PostMapping("/success")
    public Map<String, Object> handlePaymentSuccess(
            @RequestParam("transactionId") String transactionId,
            @RequestBody Map<String, String> payload
    ) {

        String referenceId = payload.get("reference_id"); // keep same key as frontend

        Optional<Payment> optionalPayment = paymentRepository.findByTransactionId(transactionId);
        Optional<Property> optionalProperty=propertyRepository.findById(optionalPayment.get().getPropertyId());
        Property property=optionalProperty.get();

        Map<String, Object> res = new HashMap<>();

        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();

            payment.setStatus("SUCCESS"); // or use status from payload
            payment.setReferenceId(referenceId); // make sure this field exists in entity

            paymentRepository.save(payment);
            property.setPayment_status(true);
            propertyRepository.save(property);

            res.put("message", "Payment SUCCESS");
            res.put("status", "SUCCESS");


        } else {
            res.put("message", "Payment not found");
            res.put("status", "ERROR");
        }

        return res;
    }

    // MARK PAYMENT FAILED
    @PostMapping("/failed")
    public ResponseEntity<Map<String, Object>> markFailed(
            @RequestParam String transactionId
    ) {

        Optional<Payment> optionalPayment = paymentRepository.findByTransactionId(transactionId);

        Map<String, Object> res = new HashMap<>();

        if (optionalPayment.isEmpty()) {
            res.put("message", "Payment not found");
            res.put("status", "ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        Payment payment = optionalPayment.get();

        // Optional: prevent overwriting SUCCESS payment
        if ("SUCCESS".equalsIgnoreCase(payment.getStatus())) {
            res.put("message", "Payment already marked as SUCCESS");
            res.put("status", "IGNORED");
            return ResponseEntity.ok(res);
        }

        payment.setStatus("FAILED");
        paymentRepository.save(payment);

        res.put("message", "Payment FAILED");
        res.put("status", "FAILED");

        return ResponseEntity.ok(res);
    }

    // GET ALL PAYMENTS
    @GetMapping("/all")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // GET PAYMENT BY ID
    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @GetMapping("get-by-owner")
    public ResponseEntity<?> getPaymentByOwner(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user=customUserDetails.loadUserByToken(authHeader);
        List<Payment> payments=paymentRepository.findByOwner_Id(user.getId());
        return new ResponseEntity<>(payments,HttpStatus.OK);
    }


}