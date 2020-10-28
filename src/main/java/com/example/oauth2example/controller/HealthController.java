package com.example.oauth2example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthController {
    @RequestMapping("/health/liveness")
    public ResponseEntity getHeathLiveness() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/health/readiness")
    public ResponseEntity getHeathReadiness() {
        return ResponseEntity.ok().build();
    }
}
