package com.hazelcast.sample.ecommerce.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ServiceController {
    static Double userCredits = 10000.0;
    static Integer stock = 10;
    static Map<String, Map<String,Object>> params = new HashMap<>();

    @GetMapping("/user-credits")
    public Double getUserCredits() {
        return userCredits;
    }

    @PostMapping("/user-credits")
    public void updateUserCredits(@RequestHeader String transactionId, @RequestParam Double amount) {
        Map<String, Object> trxParams = getOrCreateTrxParams(transactionId);
        trxParams.put("amount", amount);
        params.put(transactionId, trxParams);
        userCredits += amount;
    }

    @PostMapping("/user-credits-compensate")
    public void updateUserCreditsCompensate(@RequestHeader String transactionId) {
        Double amount = (Double) params.get(transactionId).get("amount");
        userCredits -= amount;
    }

    @GetMapping("/stock")
    public Integer getStock() {
        return stock;
    }

    @PostMapping("/stock")
    public void updateStock(@RequestHeader String transactionId, @RequestParam Integer quantity) {
        Map<String, Object> trxParams = getOrCreateTrxParams(transactionId);
        trxParams.put("quantity", quantity);
        params.put(transactionId, trxParams);
        stock += quantity;
    }

    @PostMapping("/stock-compensate")
    public void updateStockCompensate(@RequestHeader String transactionId) {
        Integer quantity = (Integer) params.get(transactionId).get("quantity");
        stock -= quantity;
    }

    @PostMapping("/bank-transaction")
    public ResponseEntity<Object> bankTransaction(@RequestHeader String transactionId) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .header("transactionId", transactionId)
                             .body(null);
    }

    @PostMapping("/bank-transaction-compensate")
    public ResponseEntity<Object> bankTransactionCompensate(@RequestHeader String transactionId) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    private Map<String, Object> getOrCreateTrxParams(@RequestHeader String transactionId) {
        return params.containsKey(transactionId) ? params
                .get(transactionId) : new HashMap<>();
    }
}
