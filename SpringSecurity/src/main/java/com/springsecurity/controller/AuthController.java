package com.springsecurity.controller;

import com.springsecurity.config.JwtAuthenticationFilter;
import com.springsecurity.domain.dto.JwtAuthenticationResponse;
import com.springsecurity.domain.dto.SignInRequest;
import com.springsecurity.domain.dto.SignUpRequest;
import com.springsecurity.service.AuthenticationService;
import com.springsecurity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }



    @GetMapping("/get1")
    public ResponseEntity<UserService> getUserService(){
        return ResponseEntity.ok(userService);
    }

    @GetMapping("/get2")
    public ResponseEntity <JwtAuthenticationFilter> getFilter(){
        return ResponseEntity.ok(jwtAuthenticationFilter);
    }
}

