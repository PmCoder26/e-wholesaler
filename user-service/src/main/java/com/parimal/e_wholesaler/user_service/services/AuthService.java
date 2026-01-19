package com.parimal.e_wholesaler.user_service.services;

import com.parimal.e_wholesaler.user_service.advices.ApiResponse;
import com.parimal.e_wholesaler.user_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.user_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.user_service.dtos.*;
import com.parimal.e_wholesaler.user_service.entities.UserEntity;
import com.parimal.e_wholesaler.user_service.exceptions.MyException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.parimal.e_wholesaler.user_service.utils.UserType.OWNER;
import static com.parimal.e_wholesaler.user_service.utils.UserType.WORKER;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final ShopFeignClient shopFeignClient;
    private final WorkerFeignClient workerFeignClient;


    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long userTypeId = null;
        Map<String, Object> extraClaims = new HashMap<>();

        if(user.getUserType().equals(OWNER)) {
            ApiResponse<Long> userTypeIdResponse = shopFeignClient.getOwnerIdByUsername(user.getUsername());
            if(userTypeIdResponse.getError() != null) throw new MyException(userTypeIdResponse.getError());
            userTypeId = userTypeIdResponse.getData();
            extraClaims.put("user_type", OWNER.name());
            extraClaims.put("owner_id", userTypeId);
        }
        else if(user.getUserType().equals(WORKER)) {
            ApiResponse<WorkerDataResponseDTO> workerResponse = workerFeignClient.getWorkerDataByMobNo(user.getUsername());
            if(workerResponse.getError() != null) throw new MyException(workerResponse.getError());
            userTypeId = workerResponse.getData().getWorkerId();
            extraClaims.put("user_type", WORKER.name());
            extraClaims.put("worker_id", userTypeId);
            extraClaims.put("shop_id", workerResponse.getData().getShopId());
        }

        String accessToken = jwtService.generateAccessToken(user, extraClaims);
        String refreshToken = jwtService.generateRefreshToken(user, extraClaims);

        return new LoginResponseDTO(accessToken, refreshToken, user.getUserType(), userTypeId);
    }

    public RefreshAccessTokenResponseDTO refreshAccessToken(RefreshAccessTokenRequestDTO requestDTO) {
        String username = jwtService.getUsernameFromToken(requestDTO.getRefreshToken());
        UserEntity user = (UserEntity) userService.loadUserByUsername(username);
        Map<String, Object> extraClaims = new HashMap<>();

        if(user.getUserType().equals(OWNER)) {
            ApiResponse<Long> userTypeIdResponse = shopFeignClient.getOwnerIdByUsername(user.getUsername());
            if(userTypeIdResponse.getError() != null) throw new MyException(userTypeIdResponse.getError());
            extraClaims.put("owner_id", userTypeIdResponse.getData());
            extraClaims.put("user_type", OWNER.name());
        }
        else if(user.getUserType().equals(WORKER)) {
            ApiResponse<WorkerDataResponseDTO> workerResponse = workerFeignClient.getWorkerDataByMobNo(user.getUsername());
            if(workerResponse.getError() != null) throw new MyException(workerResponse.getError());
            extraClaims.put("worker_id", workerResponse.getData().getWorkerId());
            extraClaims.put("user_type", WORKER.name());
            extraClaims.put("shop_id", workerResponse.getData().getShopId());
        }

        String newAccessToken = jwtService.generateAccessToken(user, extraClaims);

        return new RefreshAccessTokenResponseDTO(newAccessToken);
    }

}
