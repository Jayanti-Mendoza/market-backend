package com.merida.tecnm.market_backend.domain.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank()) {
            String token = extractToken(authHeader);

            if (token != null && !token.isBlank()) {
                try {
                    String username = jwtUtil.extractUsername(token);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        if (jwtUtil.isTokenValid(token, username)) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            username,
                                            null,
                                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                                    );
                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );
                            SecurityContext context = SecurityContextHolder.createEmptyContext();
                            context.setAuthentication(authentication);
                            SecurityContextHolder.setContext(context);
                            log.debug("Usuario '{}' autenticado correctamente para [{}]", username, request.getRequestURI());
                        } else {
                            log.warn("Token JWT inválido o expirado para usuario '{}' en [{}]", username, request.getRequestURI());
                        }
                    }
                } catch (ExpiredJwtException ex) {
                    log.warn("Token JWT expirado en petición [{}]: {}", request.getRequestURI(), ex.getMessage());
                } catch (SignatureException | SecurityException ex) {
                    log.warn("Firma de Token JWT no válida en [{}]: {}", request.getRequestURI(), ex.getMessage());
                } catch (MalformedJwtException ex) {
                    log.warn("Token JWT malformado en [{}]: {}", request.getRequestURI(), ex.getMessage());
                } catch (Exception ex) {
                    log.error("Error al procesar el token JWT en [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    public static String extractToken(String authHeader) {
        if (authHeader == null) {
            return null;
        }
        String token = authHeader.trim();
        // Remove repeated "Bearer " or "bearer " prefixes (e.g. if entered in Swagger)
        while (token.regionMatches(true, 0, "Bearer ", 0, 7)) {
            token = token.substring(7).trim();
        }
        // Handle case if entire JSON response {"token":"..."} was pasted into Swagger
        if (token.contains("\"token\"")) {
            int idx = token.indexOf("\"token\"");
            int startQuote = token.indexOf('"', idx + 7);
            if (startQuote != -1) {
                int nextQuote = token.indexOf('"', startQuote + 1);
                if (nextQuote != -1) {
                    token = token.substring(startQuote + 1, nextQuote).trim();
                }
            }
        }
        // Remove enclosing quotes if pasted with quotes
        if (token.startsWith("\"") && token.endsWith("\"") && token.length() > 1) {
            token = token.substring(1, token.length() - 1).trim();
        }
        return token;
    }
}
