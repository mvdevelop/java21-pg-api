package com.exemplo.api.security;

import com.exemplo.api.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço de geração e validação de tokens JWT.
 * Utiliza HS256 com secret key externalizada via variável de ambiente.
 *
 * @see <a href="https://github.com/jwtk/jjwt">JJWT Documentation</a>
 */
@Slf4j
@Service
public class JwtTokenService {

    @Value("${JWT_SECRET:ChangeThisSecretKeyInProductionToSomethingLongAndRandom123!}")
    private String secret;

    @Value("${JWT_EXPIRATION:86400000}")
    private Long jwtExpiration;

    /**
     * Gera token JWT com claims do usuário
     */
    public String generateToken(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return buildToken(usuario, new Date(System.currentTimeMillis() + jwtExpiration));
    }

    private String buildToken(Usuario usuario, Date expiryDate) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nomeCompleto", usuario.getNomeCompleto());
        claims.put("perfis", usuario.getPerfis());

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .signWith(getSignKey())
                .compact();
    }

    /**
     * Extrai username (subject) do token
     */
    public String extractUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extrai claim específico do token
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Retorna a SecretKey para assinatura JWT
     */
    private SecretKey getSignKey() {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Valida token JWT contra usuário
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
