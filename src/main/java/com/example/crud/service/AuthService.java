package com.example.crud.service;

import com.example.crud.dto.AuditEventRequestDTO;
import com.example.crud.dto.LoginDTO;
import com.example.crud.dto.RegisterDTO;
import com.example.crud.dto.TokenDTO;
import com.example.crud.enums.AuditAction;
import com.example.crud.model.UserEntity;
import com.example.crud.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuditIntegrationService auditIntegrationService;

    public AuthService(AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       JwtService jwtService,
                       AuditIntegrationService auditIntegrationService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.auditIntegrationService = auditIntegrationService;
    }

    public TokenDTO login(LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        String token = jwtService.generateToken(userDetails.getUsername(), roles);
        Instant expiresAt = jwtService.getExpiryFromNow();

        sendAuditUserEvent(userDetails.getUsername(), AuditAction.LOGIN);

        return new TokenDTO(token, expiresAt);
    }

    @Transactional
    public void register(RegisterDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserEntity user = UserEntity.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .build();
        if (user.getRoles() == null) {
            user.setRoles(new java.util.HashSet<>());
        }
        user.getRoles().add("ROLE_USER");
        userRepository.save(user);

        sendAuditUserEvent(dto.username(), AuditAction.REGISTER);
    }

    private void sendAuditUserEvent(String username, AuditAction action) {
        AuditEventRequestDTO auditEventRequestDTO = AuditEventRequestDTO.builder()
                .entityType("User")
                .entityId(username)
                .username(username)
                .action(action)
                .source("crud-security")
                .timestamp(Instant.now())
                .build();

        auditIntegrationService.sendAuditEvent(auditEventRequestDTO);
    }
}
