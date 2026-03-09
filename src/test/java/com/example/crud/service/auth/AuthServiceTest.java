package com.example.crud.service.auth;

import com.example.crud.dto.user.LoginDTO;
import com.example.crud.dto.user.RegisterDTO;
import com.example.crud.dto.user.TokenDTO;
import com.example.crud.enums.audit.AuditAction;
import com.example.crud.entity.user.UserEntity;
import com.example.crud.infrastructure.security.jwt.JwtService;
import com.example.crud.repository.user.UserRepository;
import com.example.crud.service.audit.AuditIntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuditIntegrationService auditIntegrationService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthService authService;

    private LoginDTO sampleLoginDTO;
    private RegisterDTO sampleRegisterDTO;
    private UserEntity sampleUser;
    private TokenDTO sampleTokenDTO;
    private Instant sampleExpiry;

    @BeforeEach
    void setUp() {
        sampleLoginDTO = new LoginDTO("testuser", "password123");
        sampleRegisterDTO = new RegisterDTO("newuser", "password123");
        
        sampleUser = UserEntity.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .roles(Set.of("ROLE_USER"))
                .createdAt(Instant.now())
                .build();

        sampleExpiry = Instant.now().plusSeconds(3600);
        sampleTokenDTO = new TokenDTO("sample.jwt.token", sampleExpiry);
    }

    @Test
    void login_shouldAuthenticateAndReturnToken_whenCredentialsAreValid() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetails.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(jwtService.generateToken(eq("testuser"), any(Set.class))).thenReturn("sample.jwt.token");
        when(jwtService.getExpiryFromNow()).thenReturn(sampleExpiry);
        doNothing().when(auditIntegrationService).sendAuditEvent(any());

        // Act
        TokenDTO result = authService.login(sampleLoginDTO);

        // Assert
        assertNotNull(result);
        assertEquals("sample.jwt.token", result.token());
        assertEquals(sampleExpiry, result.expiresAt());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken("testuser", Set.of("ROLE_USER"));
        verify(jwtService).getExpiryFromNow();
        verify(auditIntegrationService).sendAuditEvent(any());
    }

    @Test
    void login_shouldSetSecurityContext_whenAuthenticationSucceeds() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetails.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(jwtService.generateToken(eq("testuser"), any(Set.class))).thenReturn("sample.jwt.token");
        when(jwtService.getExpiryFromNow()).thenReturn(sampleExpiry);
        doNothing().when(auditIntegrationService).sendAuditEvent(any());

        // Act
        authService.login(sampleLoginDTO);

        // Assert
        Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(contextAuth);
        assertEquals(authentication, contextAuth);
    }

    @Test
    void register_shouldCreateUser_whenUsernameIsAvailable() {
        // Arrange
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(sampleUser);
        doNothing().when(auditIntegrationService).sendAuditEvent(any());

        // Act
        assertDoesNotThrow(() -> authService.register(sampleRegisterDTO));

        // Assert
        verify(userRepository).existsByUsername("newuser");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(argThat(user -> 
            user.getUsername().equals("newuser") &&
            user.getPassword().equals("encodedPassword") &&
            user.getRoles().contains("ROLE_USER")
        ));
        verify(auditIntegrationService).sendAuditEvent(any());
    }

    @Test
    void register_shouldThrowIllegalArgumentException_whenUsernameAlreadyExists() {
        // Arrange
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);
        RegisterDTO duplicateRegisterDTO = new RegisterDTO("existinguser", "password123");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authService.register(duplicateRegisterDTO)
        );
        
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository).existsByUsername("existinguser");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder, auditIntegrationService);
    }

    @Test
    void register_shouldAssignDefaultRole_whenUserHasNoRoles() {
        // Arrange
        UserEntity userToSave = UserEntity.builder()
                .username("newuser")
                .password("encodedPassword")
                .roles(null) // Explicitly null to test default assignment
                .build();
                
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        doNothing().when(auditIntegrationService).sendAuditEvent(any());

        // Act
        authService.register(sampleRegisterDTO);

        // Assert
        verify(userRepository).save(argThat(user -> 
            user.getRoles() != null &&
            user.getRoles().contains("ROLE_USER") &&
            user.getRoles().size() == 1
        ));
    }
}
