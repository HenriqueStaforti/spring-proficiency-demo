package com.example.crud.controller;

import com.example.crud.dto.LoginDTO;
import com.example.crud.dto.RegisterDTO;
import com.example.crud.dto.TokenDTO;
import com.example.crud.service.AuthService;
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
