package com.parimal.e_wholesaler.user_service.services;

import com.parimal.e_wholesaler.user_service.dtos.LoginRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.LoginResponseDTO;
import com.parimal.e_wholesaler.user_service.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;


    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new LoginResponseDTO(accessToken, refreshToken);
    }

    

}
