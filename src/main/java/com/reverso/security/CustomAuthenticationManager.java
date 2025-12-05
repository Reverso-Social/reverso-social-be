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
        
        System.out.println("🔍 Buscando usuario con email: " + email);
        
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                System.err.println("Usuario no encontrado: " + email);
                return new BadCredentialsException("Correo electrónico o contraseña inválidos");
            });
        
        System.out.println("Usuario encontrado: " + user.getEmail());
        System.out.println("Hash en BD: " + user.getPassword().substring(0, 20) + "...");
        
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        System.out.println(" ¿Contraseña coincide?: " + passwordMatches);
        
        if (!passwordMatches) {
            System.err.println("Contraseña incorrecta para: " + email);
            throw new BadCredentialsException("Correo electrónico o contraseña inválidos");
        }
        
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        
        System.out.println("Autenticación exitosa para: " + email + " con rol: " + user.getRole());
        
        return new UsernamePasswordAuthenticationToken(
            userDetails, 
            password,
            userDetails.getAuthorities()
        );
    }
}