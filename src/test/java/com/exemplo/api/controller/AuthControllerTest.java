package com.exemplo.api.controller;

import com.exemplo.api.dto.LoginRequestDTO;
import com.exemplo.api.dto.LoginResponseDTO;
import com.exemplo.api.dto.RegisterRequestDTO;
import com.exemplo.api.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRealizarLoginComSucesso() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("testuser", "password123");
        LoginResponseDTO response = new LoginResponseDTO("jwt-token-123", "Bearer", "testuser");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void deveRetornar400QuandoDadosLoginInvalidos() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("", "password123"); // username vazio

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRealizarRegisterComSucesso() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("newuser", "New User", "password123");
        LoginResponseDTO response = new LoginResponseDTO("jwt-token-123", "Bearer", "newuser");

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.username").value("newuser"));
    }
}
