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

        @org.springframework.beans.factory.annotation.Value("${app.cors.allowed-origins}")
        private String allowedOrigins;

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
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers("/uploads/**").permitAll()
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/download-leads").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/download-leads/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/download-leads/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/service-categories/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/service-features/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/service-categories/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.PUT, "/api/service-categories/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/service-categories/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/services/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.PUT, "/api/services/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/services/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/service-features/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.PUT, "/api/service-features/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/service-features/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/resources/public").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/resources/**").authenticated()
                                                .requestMatchers(HttpMethod.POST, "/api/resources/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.PATCH, "/api/resources/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/resources/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/resource-downloads")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.GET, "/api/resource-downloads/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/resource-downloads/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/contacts").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/contacts").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PATCH, "/api/contacts/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/contacts/**").hasRole("ADMIN")
                                                .requestMatchers("/api/users/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/blogposts/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/blogposts/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.PUT, "/api/blogposts/**")
                                                .hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers(HttpMethod.DELETE, "/api/blogposts/**")
                                                .hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterAfter(authorizationFilter, JWTAuthenticationFilter.class);
                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));
                configuration.setAllowedMethods(Arrays.asList(
                                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
                configuration.setAllowedHeaders(Arrays.asList(
                                "Authorization",
                                "Content-Type",
                                "X-Requested-With",
                                "Accept",
                                "Origin",
                                "Access-Control-Request-Method",
                                "Access-Control-Request-Headers"));
                configuration.setAllowCredentials(true);
                configuration.setExposedHeaders(Arrays.asList(
                                "Authorization",
                                "Access-Control-Allow-Origin",
                                "Access-Control-Allow-Credentials"));
                configuration.setMaxAge(3600L);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
