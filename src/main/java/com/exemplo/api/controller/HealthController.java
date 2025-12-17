
// src/main/java/com/exemplo/api/controller/HealthController.java
package com.exemplo.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
    
    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("API Java 21 is running");
    }
}
