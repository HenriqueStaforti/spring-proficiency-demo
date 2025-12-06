package com.example.crud.controller.auth;

import com.example.crud.dto.user.LoginDTO;
import com.example.crud.dto.user.RegisterDTO;
import com.example.crud.dto.user.TokenDTO;
import com.example.crud.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO body) {
        TokenDTO token = authService.login(body);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO body) {
        authService.register(body);
        return ResponseEntity.status(201).build();
    }
}
