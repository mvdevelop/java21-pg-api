package com.exemplo.api.controller;

import com.exemplo.api.dto.LoginRequestDTO;
import com.exemplo.api.dto.LoginResponseDTO;
import com.exemplo.api.dto.RegisterRequestDTO;
import com.exemplo.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de autenticação (login e registro).
 *
 * Endpoints públicos - protegidos pelo RateLimitingFilter.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de login e registro")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login de usuário", description = "Autentica usuário e retorna token JWT")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Registro de usuário", description = "Cria novo usuário e retorna token JWT")
    public ResponseEntity<LoginResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        LoginResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }
}
