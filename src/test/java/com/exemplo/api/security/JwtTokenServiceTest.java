package com.exemplo.api.security;

import com.exemplo.api.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService();
        // Usar reflection para injetar secret
        var secretField = assertDoesNotThrow(() -> JwtTokenService.class.getDeclaredField("secret"));
        secretField.setAccessible(true);
        assertDoesNotThrow(() -> secretField.set(jwtTokenService, "TestSecretKeyForJWTToken123!@#LongEnough"));
    }

    @Test
    void deveGerarTokenValido() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setNomeCompleto("Test User");
        usuario.setPassword("hashed");

        Authentication auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

        String token = jwtTokenService.generateToken(auth);

        assertNotNull(token);
        assertTrue(token.length() > 50);
        assertTrue(token.contains("."));
    }

    @Test
    void deveExtrairUsernameDoToken() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setNomeCompleto("Test User");
        usuario.setPassword("hashed");

        Authentication auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        String token = jwtTokenService.generateToken(auth);

        String username = jwtTokenService.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void deveValidarTokenComUsuarioCorreto() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setNomeCompleto("Test User");
        usuario.setPassword("hashed");

        Authentication auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        String token = jwtTokenService.generateToken(auth);

        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("hashed")
                .authorities("ROLE_USER")
                .build();

        assertTrue(jwtTokenService.isTokenValid(token, userDetails));
    }

    @Test
    void naoDeveValidarTokenComUsuarioIncorreto() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setNomeCompleto("Test User");
        usuario.setPassword("hashed");

        Authentication auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        String token = jwtTokenService.generateToken(auth);

        UserDetails userDetails = User.builder()
                .username("differentuser")
                .password("hashed")
                .authorities("ROLE_USER")
                .build();

        assertFalse(jwtTokenService.isTokenValid(token, userDetails));
    }
}
