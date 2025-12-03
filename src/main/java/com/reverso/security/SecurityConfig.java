package com.reverso.security;

import com.reverso.security.filter.JWTAuthenticationFilter;
import com.reverso.security.filter.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    
    private final CustomAuthenticationManager authenticationManager;
    
    public SecurityConfig(CustomAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        

        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(authenticationManager);
        JWTAuthorizationFilter authorizationFilter = new JWTAuthorizationFilter();
        
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/h2-console/**").permitAll()
                

                .requestMatchers("/api/auth/**").permitAll()
   

                .requestMatchers(HttpMethod.GET, "/api/service-categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/service-features/**").permitAll()
                

                .requestMatchers(HttpMethod.POST, "/api/service-categories/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.PUT, "/api/service-categories/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.DELETE, "/api/service-categories/**").hasRole("ADMIN")
                

                .requestMatchers(HttpMethod.POST, "/api/services/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.PUT, "/api/services/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.DELETE, "/api/services/**").hasRole("ADMIN")
                

                .requestMatchers(HttpMethod.POST, "/api/service-features/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.PUT, "/api/service-features/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.DELETE, "/api/service-features/**").hasRole("ADMIN")
                

                .requestMatchers(HttpMethod.GET, "/api/resources/public").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/resources/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/resources/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.PATCH, "/api/resources/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.DELETE, "/api/resources/**").hasRole("ADMIN")
                

                .requestMatchers(HttpMethod.POST, "/api/resource-downloads").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/resource-downloads/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.DELETE, "/api/resource-downloads/**").hasRole("ADMIN")
                

                .requestMatchers(HttpMethod.POST, "/api/contacts").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/contacts/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.PATCH, "/api/contacts/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.DELETE, "/api/contacts/**").hasRole("ADMIN")


                .requestMatchers("/api/users/**").hasRole("ADMIN")
                

                .anyRequest().authenticated()
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(authorizationFilter, JWTAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        

        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:5174",
            "http://localhost:3000",
            "http://localhost:4200"
        ));
        

        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        

        configuration.setAllowedHeaders(Arrays.asList("*"));
        

        configuration.setAllowCredentials(true);
        

        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}