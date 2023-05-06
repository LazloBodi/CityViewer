package com.lazlob.cityviewer.controllers;

import com.lazlob.cityviewer.models.dtos.Credentials;
import com.lazlob.cityviewer.models.dtos.TokenResponse;
import com.lazlob.cityviewer.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @Operation(summary = "Login to get a authentication token.")
    @ApiResponse(responseCode = "200", description = "Logged in successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials")
    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid Credentials credentials) {
        return authService.login(credentials.getUsername(), credentials.getPassword());
    }

}