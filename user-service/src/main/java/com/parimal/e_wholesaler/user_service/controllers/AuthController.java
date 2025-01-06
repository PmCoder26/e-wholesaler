package com.parimal.e_wholesaler.user_service.controllers;

import com.parimal.e_wholesaler.user_service.dtos.LoginRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.LoginResponseDTO;
import com.parimal.e_wholesaler.user_service.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping(path = "/login")
    public LoginResponseDTO login(
            @RequestBody
            LoginRequestDTO requestDTO
    ) {
        return authService.login(requestDTO);
    }

}
