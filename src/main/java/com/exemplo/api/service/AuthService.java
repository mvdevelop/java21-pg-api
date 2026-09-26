package com.exemplo.api.service;

import com.exemplo.api.dto.LoginRequestDTO;
import com.exemplo.api.dto.LoginResponseDTO;
import com.exemplo.api.dto.RegisterRequestDTO;
import com.exemplo.api.model.Usuario;
import com.exemplo.api.repository.UsuarioRepository;
import com.exemplo.api.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

/**
 * Serviço de autenticação e registro de usuários.
 *
 * Responsabilidades:
 * - Login com validação de credenciais
 * - Registro de novos usuários com password hashing
 * - Geração de tokens JWT
 *
 * @see <a href="https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html">OWASP Auth Cheat Sheet</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    /**
     * Autentica usuário e retorna token JWT.
     *
     * @param request username + password
     * @return LoginResponseDTO com token JWT
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Tentativa de login para usuário: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtTokenService.generateToken(authentication);

        log.info("Login bem-sucedido para usuário: {}", usuario.getUsername());

        return new LoginResponseDTO(
                token,
                "Bearer",
                usuario.getUsername()
        );
    }

    /**
     * Registra novo usuário com password hashing via BCrypt.
     *
     * @param request username, nomeCompleto, password
     * @return LoginResponseDTO com token JWT
     */
    public LoginResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username já existe: " + request.getUsername());
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setNomeCompleto(request.getNomeCompleto());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        // Perfil padrão: USER; ADMIN só via DB direto ou migração
        usuario.setPerfis(Set.of("USER"));

        Usuario saved = usuarioRepository.save(usuario);
        log.info("Novo usuário registrado: {}", saved.getUsername());

        // Auto-login: gera token imediatamente após registro
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                saved, null, saved.getAuthorities()
        );
        String token = jwtTokenService.generateToken(authentication);

        return new LoginResponseDTO(token, "Bearer", saved.getUsername());
    }
}
