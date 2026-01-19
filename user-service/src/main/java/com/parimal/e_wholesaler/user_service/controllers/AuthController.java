package com.parimal.e_wholesaler.user_service.controllers;

import com.parimal.e_wholesaler.user_service.dtos.LoginRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.LoginResponseDTO;
import com.parimal.e_wholesaler.user_service.dtos.RefreshAccessTokenRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.RefreshAccessTokenResponseDTO;
import com.parimal.e_wholesaler.user_service.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@AllArgsConstructor
@PreAuthorize(value = "hasRole('USER_SERVICE')")
public class AuthController {

    private final AuthService authService;


    @PostMapping(path = "/login")
    public LoginResponseDTO login(
            @RequestBody
            @Valid
            LoginRequestDTO requestDTO
    ) {
        return authService.login(requestDTO);
    }

    @PostMapping(path = "/refresh-token")
    public RefreshAccessTokenResponseDTO refreshAccessToken(
            @RequestBody
            @Valid
            RefreshAccessTokenRequestDTO requestDTO
    ) {
        return authService.refreshAccessToken(requestDTO);
    }

}
