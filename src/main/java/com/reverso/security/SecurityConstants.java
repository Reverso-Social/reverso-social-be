package com.reverso.security;

public class SecurityConstants {
    
    public static final String SECRET_KEY = "reverso_social_secret_key_2024_$secure$key$for$production$use$feminist$consulting";
    public static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_URL = "/api/auth/login";
    
    private SecurityConstants() {
    }
}
