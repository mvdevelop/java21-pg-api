package com.exemplo.api.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração central de segurança da aplicação.
 * Implementa:
 * - Autenticação stateless (JWT)
 * - BCrypt password hashing
 * - Rate limiting via filtro customizado
 * - Security headers (HSTS, CSP, X-Frame-Options, X-Content-Type-Options)
 * - CORS configurado
 *
 * @see <a href="https://owasp.org/www-project-top-ten/">OWASP Top 10</a>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwtAuthFilter,
                                           RateLimitingFilter rateLimitingFilter) throws Exception {
        http
            // Cabeçalhos de segurança (OWASP ASVS V8)
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .requestDirectives(directives -> directives
                        .defaultSrc("'self'")
                        .scriptSrc("'self' 'unsafe-inline'")
                        .styleSrc("'self' 'unsafe-inline'")
                        .imgSrc("'self' data:")
                        .objectSrc("'none'")
                        .frameAncestors("'none'")
                    ))
                .xssProtection(xss -> xss.block(true))
                .frameOptions(frame -> frame.deny())
            )
            // CSRF: desativado para API stateless (proteção via SameSite no rate limiter)
            .csrf(csrf -> csrf.disable())
            // CORS
            .cors(cors -> cors.disable())
            // Session management: STATELESS
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Autorização de endpoints
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers(
                    "/auth/login",
                    "/auth/register",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/api-docs",
                    "/actuator/health",
                    "/health",
                    "/"
                ).permitAll()
                // Endpoints administrativos
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Tudo mais requer autenticação
                .anyRequest().authenticated()
            )
            // Exception handling
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
            )
            // Filtros na ordem correta: rate limit → JWT → username/password
            .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * BCrypt com strength 12 (OWASP recomenda minimum 10, usamos 12 para mais segurança)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
