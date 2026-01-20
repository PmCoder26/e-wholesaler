package com.parimal.e_wholesaler.user_service.controllers;

import com.parimal.e_wholesaler.user_service.dtos.SignupRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.SignupResponseDTO;
import com.parimal.e_wholesaler.user_service.dtos.SignupWorkerRequestDTO;
import com.parimal.e_wholesaler.user_service.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
@PreAuthorize(value = "hasRole('USER_SERVICE')")
public class UserController {

    private final UserService userService;


    @PostMapping(path = "/owner/signup")
    public SignupResponseDTO signupOwner(
            @RequestBody @Valid
            SignupRequestDTO requestDTO
    ) {
        return userService.signupOwner(requestDTO);
    }

    @PostMapping(path = "/worker/signup")
    public SignupResponseDTO signupWorker(
            @RequestBody @Valid
            SignupWorkerRequestDTO requestDTO
    ) {
        return userService.signupWorker(requestDTO);
    }


}
