package com.parimal.e_wholesaler.user_service.controllers;

import com.parimal.e_wholesaler.user_service.dtos.SignupRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.SignupResponseDTO;
import com.parimal.e_wholesaler.user_service.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping(path = "/signup")
    public SignupResponseDTO signup(
            @RequestBody @Valid
            SignupRequestDTO requestDTO
    ) {
        return userService.signup(requestDTO);
    }


}
