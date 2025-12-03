package com.reverso.security;

import com.reverso.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public CustomAuthenticationManager(UserRepository userRepository, 
                                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("Correo electrónico o contraseña inválidos"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Correo electrónico o contraseña inválidos");
        }
        
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        
        return new UsernamePasswordAuthenticationToken(
            userDetails, 
            password,
            userDetails.getAuthorities()
        );
    }
}