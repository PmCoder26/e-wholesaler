package com.parimal.e_wholesaler.user_service.services;

import com.parimal.e_wholesaler.user_service.advices.ApiResponse;
import com.parimal.e_wholesaler.user_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.user_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.user_service.dtos.LoginRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.LoginResponseDTO;
import com.parimal.e_wholesaler.user_service.entities.UserEntity;
import com.parimal.e_wholesaler.user_service.exceptions.MyException;
import com.parimal.e_wholesaler.user_service.utils.UserType;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    private final ShopFeignClient shopFeignClient;
    private final WorkerFeignClient workerFeignClient;


    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Long userTypeId = null;
        String transactionToken = jwtService.generateTransactionToken(user.getId());
        if(user.getUserType().equals(UserType.OWNER)) {
            ApiResponse<Long> userTypeIdResponse = shopFeignClient.getOwnerIdByUsername(transactionToken, user.getUsername());
            if(userTypeIdResponse.getError() != null) throw new MyException(userTypeIdResponse.getError());
            userTypeId = userTypeIdResponse.getData();
        }
        else if(user.getUserType().equals(UserType.WORKER)) {
            ApiResponse<Long> userTypeIdResponse = workerFeignClient.getWorkerIdByMobNo(transactionToken, user.getUsername());
            if(userTypeIdResponse.getError() != null) throw new MyException(userTypeIdResponse.getError());
            userTypeId = userTypeIdResponse.getData();
        }
        return new LoginResponseDTO(accessToken, refreshToken, user.getUserType(), userTypeId);
    }

}
