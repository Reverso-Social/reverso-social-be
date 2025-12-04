package com.reverso.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reverso.dto.request.LoginRequest;
import com.reverso.dto.response.JwtResponse;
import com.reverso.security.CustomAuthenticationManager;
import com.reverso.security.SecurityConstants;
import com.reverso.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; (Lo usaremos??) 
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    
    private final CustomAuthenticationManager authenticationManager;
    private String filterProcessesUrl = SecurityConstants.LOGIN_URL;
    
    public JWTAuthenticationFilter(CustomAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    public void setFilterProcessesUrl(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        
        if (!request.getRequestURI().equals(filterProcessesUrl) || 
            !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            LoginRequest credentials = new ObjectMapper()
                .readValue(request.getInputStream(), LoginRequest.class);
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                credentials.getEmail(),
                credentials.getPassword()
            );
            
            Authentication authResult = authenticationManager.authenticate(authentication);
            
            successfulAuthentication(request, response, authResult);
            
        } catch (AuthenticationException e) {
            unsuccessfulAuthentication(request, response, e);
        }
    }
    
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authResult) throws IOException {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        
        String role = authResult.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElse("ROLE_USER")
            .replace("ROLE_", "");
        
        String token = JWT.create()
            .withSubject(authResult.getName())
            .withClaim("role", role)
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET_KEY));
        
        JwtResponse jwtResponse = JwtResponse.builder()
            .token(token)
            .email(authResult.getName())
            .role(role)
            .fullName(userDetails.getUser().getFullName())
            .build();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(jwtResponse));
        response.getWriter().flush();
    }
    
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException failed) throws IOException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
            "{\"error\": \"" + failed.getMessage() + "\"}"
        );
        response.getWriter().flush();
    }
}
