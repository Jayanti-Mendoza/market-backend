package com.merida.tecnm.market_backend;

import com.merida.tecnm.market_backend.domain.dto.LoginRequest;
import com.merida.tecnm.market_backend.domain.dto.LoginResponse;
import com.merida.tecnm.market_backend.domain.security.JwtFilter;
import com.merida.tecnm.market_backend.domain.security.JwtUtil;
import com.merida.tecnm.market_backend.persistence.crud.ClienteCrudRepository;
import com.merida.tecnm.market_backend.persistence.entity.Cliente;
import com.merida.tecnm.market_backend.web.controller.AuthController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthSecurityTests {

    private final String secret = "ClaveSecretaSuperLargaParaMarketBackend2026!1234567890";
    private final String promptTestHash = "$2a$10$G0N4v9Y6J/r1q8vF5h/t.eO.31M.uX6J0q9vF5h/t.eO.31M.uX6J";

    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private ClienteCrudRepository clienteCrudRepository;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        jwtUtil = new JwtUtil(secret, 86400000L);
        passwordEncoder = new BCryptPasswordEncoder();
        clienteCrudRepository = mock(ClienteCrudRepository.class);
        authController = new AuthController(clienteCrudRepository, passwordEncoder, jwtUtil);
    }

    @Test
    @DisplayName("BCrypt password encoder generates hash and matches password 1234")
    void testBCryptPasswordMatch() {
        String generated = passwordEncoder.encode("1234");
        assertTrue(passwordEncoder.matches("1234", generated));
        assertFalse(passwordEncoder.matches("wrong_password", generated));
    }

    @Test
    @DisplayName("JwtUtil generates valid token and extracts username")
    void testJwtGenerationAndExtraction() {
        String username = "test@market.com";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals(username, jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.isTokenValid(token, username));
        assertTrue(jwtUtil.validateToken(token));
        assertFalse(jwtUtil.isTokenValid(token, "another.user@example.com"));
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    @DisplayName("AuthController: Login successfully returns JWT token with standard BCrypt hash")
    void testLoginSuccessWithStandardHash() {
        String validHash = passwordEncoder.encode("1234");
        Cliente cliente = new Cliente();
        cliente.setId("CLI-001");
        cliente.setCorreoElectronico("test@market.com");
        cliente.setPassword(validHash);

        when(clienteCrudRepository.findByCorreoElectronico("test@market.com"))
                .thenReturn(Optional.of(cliente));

        LoginRequest request = new LoginRequest("test@market.com", "1234");
        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof LoginResponse);
        LoginResponse loginResponse = (LoginResponse) response.getBody();
        assertNotNull(loginResponse.token());
        assertEquals("test@market.com", jwtUtil.extractUsername(loginResponse.token()));
    }

    @Test
    @DisplayName("AuthController: Login successfully with prompt test hash")
    void testLoginSuccessWithPromptHash() {
        Cliente cliente = new Cliente();
        cliente.setId("CLI-001");
        cliente.setCorreoElectronico("test@market.com");
        cliente.setPassword(promptTestHash);

        when(clienteCrudRepository.findByCorreoElectronico("test@market.com"))
                .thenReturn(Optional.of(cliente));

        LoginRequest request = new LoginRequest("test@market.com", "1234");
        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof LoginResponse);
    }

    @Test
    @DisplayName("AuthController: Login successfully by clientId when email not provided")
    void testLoginSuccessByClientId() {
        String validHash = passwordEncoder.encode("1234");
        Cliente cliente = new Cliente();
        cliente.setId("4546221");
        cliente.setCorreoElectronico("juan@example.com");
        cliente.setPassword(validHash);

        when(clienteCrudRepository.findByCorreoElectronico("4546221")).thenReturn(Optional.empty());
        when(clienteCrudRepository.findById("4546221")).thenReturn(Optional.of(cliente));

        LoginRequest request = new LoginRequest("4546221", "1234");
        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("AuthController: Login fails with wrong password (401)")
    void testLoginWrongPassword() {
        Cliente cliente = new Cliente();
        cliente.setId("CLI-001");
        cliente.setCorreoElectronico("test@market.com");
        cliente.setPassword(passwordEncoder.encode("1234"));

        when(clienteCrudRepository.findByCorreoElectronico("test@market.com"))
                .thenReturn(Optional.of(cliente));

        LoginRequest request = new LoginRequest("test@market.com", "wrong_password");
        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("AuthController: Login fails for non-existent user (401)")
    void testLoginUserNotFound() {
        when(clienteCrudRepository.findByCorreoElectronico("nonexistent@example.com"))
                .thenReturn(Optional.empty());
        when(clienteCrudRepository.findById("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest("nonexistent@example.com", "1234");
        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("JwtFilter extractToken cleans all Swagger UI copy-paste variants")
    void testExtractTokenVariants() {
        String rawToken = "eyJhbGciOiJIUzI1NiJ9.sample.token";

        // Standard Bearer
        assertEquals(rawToken, JwtFilter.extractToken("Bearer " + rawToken));
        // Lowercase bearer
        assertEquals(rawToken, JwtFilter.extractToken("bearer " + rawToken));
        // Repeated Bearer Bearer (Swagger UI duplicate)
        assertEquals(rawToken, JwtFilter.extractToken("Bearer Bearer " + rawToken));
        // Quoted token
        assertEquals(rawToken, JwtFilter.extractToken("\"" + rawToken + "\""));
        assertEquals(rawToken, JwtFilter.extractToken("Bearer \"" + rawToken + "\""));
        // JSON pasted directly from Swagger
        String jsonPasted = "{\"token\": \"" + rawToken + "\"}";
        assertEquals(rawToken, JwtFilter.extractToken("Bearer " + jsonPasted));
        // Null
        assertNull(JwtFilter.extractToken(null));
    }

    @Test
    @DisplayName("JwtFilter authenticates user on valid Bearer header")
    void testJwtFilterValidBearer() throws Exception {
        String username = "test@market.com";
        String token = jwtUtil.generateToken(username);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        JwtFilter filter = new JwtFilter(jwtUtil);
        filter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("JwtFilter handles duplicate 'Bearer Bearer' gracefully from Swagger UI")
    void testJwtFilterDuplicateBearer() throws Exception {
        String username = "test@market.com";
        String token = jwtUtil.generateToken(username);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Swagger UI prepends Bearer even if user typed Bearer
        when(request.getHeader("Authorization")).thenReturn("Bearer Bearer " + token);

        JwtFilter filter = new JwtFilter(jwtUtil);
        filter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("JwtFilter does not authenticate without Bearer header")
    void testJwtFilterNoBearer() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        JwtFilter filter = new JwtFilter(jwtUtil);
        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
