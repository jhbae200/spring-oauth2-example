package com.example.oauth2example.controller;

import com.example.oauth2example.dto.WellKnownDTO;
import com.example.oauth2example.service.WellKnownService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
public class WellKnownController {
    private final WellKnownService wellKnownService;

    @GetMapping("/jwks.json")
    public ResponseEntity<Map<String, Object>> getJwks() {
        return ResponseEntity.ok(wellKnownService.getJwksJSON());
    }

    @GetMapping("/openid-configuration")
    public ResponseEntity<WellKnownDTO.OpenIdConfiguration> getOpenIdConfiguration() {
        return ResponseEntity.ok(wellKnownService.getOpenIdConfiguration());
    }
}
