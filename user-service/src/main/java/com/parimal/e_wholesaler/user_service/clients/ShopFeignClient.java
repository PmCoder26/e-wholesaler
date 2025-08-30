package com.parimal.e_wholesaler.user_service.clients;

import com.parimal.e_wholesaler.user_service.advices.ApiResponse;
import com.parimal.e_wholesaler.user_service.dtos.OwnerRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.OwnerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "shop-service", path = "/shops/owner")
public interface ShopFeignClient {

    @PostMapping
    ApiResponse<OwnerResponseDTO> createOwner(OwnerRequestDTO requestDTO);

    @PostMapping(path = "/id")
    ApiResponse<Long> getOwnerIdByUsername(@RequestHeader("Authorization") String token, String mobNo);

}
