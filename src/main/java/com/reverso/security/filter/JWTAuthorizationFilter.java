package com.reverso.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.reverso.security.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain)
            throws ServletException, IOException {
        
        // ⭐ IMPORTANTE: Ignorar endpoints públicos
        String path = request.getRequestURI();
        if (shouldNotFilter(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String header = request.getHeader(SecurityConstants.HEADER_STRING);
        
        // Si no hay header, continuar (los permisos se validan en SecurityConfig)
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String token = header.replace(SecurityConstants.TOKEN_PREFIX, "");
            
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET_KEY))
                .build()
                .verify(token);
            
            String email = decodedJWT.getSubject();
            String role = decodedJWT.getClaim("role").asString();
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                email,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
            response.getWriter().flush();
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean shouldNotFilter(String path) {
        return path.startsWith("/api/auth/") ||
               path.startsWith("/h2-console/") ||
               (path.startsWith("/api/service-categories") && isGetRequest()) ||
               (path.startsWith("/api/services") && isGetRequest()) ||
               (path.startsWith("/api/service-features") && isGetRequest()) ||
               path.equals("/api/resources/public") ||
               (path.startsWith("/api/contacts") && isPostRequest());
    }
    
    private boolean isGetRequest() {
        return true; 
    }
    
    private boolean isPostRequest() {
        return true; 
    }
}