package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.Receipt;
import org.example.model.ResponseDTO;
import org.example.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService){
        this.receiptService = receiptService;
    }

    @PostMapping("/process")
    public ResponseEntity<?> processReceipt(@Valid @RequestBody Receipt receipt) {

        String id = receiptService.processReceipt(receipt);
        return ResponseEntity.ok(new ResponseDTO(id, 0));

    }

    @GetMapping("/{id}/points")
    public ResponseEntity<?> getPoints(@PathVariable String id) {
        if (id == null || id.trim().isEmpty() || id.contains(" ")) {
            return ResponseEntity.badRequest().body(Map.of("message", "The receipt is invalid. Please verify input."));
        }

        Integer points = receiptService.getPoints(id);

        if (points == null) {
            return ResponseEntity.status(404).body(Map.of("error", "No receipt found for that ID"));
        }

        return ResponseEntity.ok(Map.of("points", points));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid receipt data. Please verify input.");

        Map<String, String> details = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.put(error.getField(), error.getDefaultMessage());
        }
        response.put("details", details);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid receipt data. Please verify input.");
        response.put("details", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}
