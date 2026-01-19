package com.parimal.e_wholesaler.user_service.clients;

import com.parimal.e_wholesaler.user_service.advices.ApiResponse;
import com.parimal.e_wholesaler.user_service.dtos.OwnerRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.OwnerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "shop-service", path = "/shops/owner/auth")
public interface ShopFeignClient {

    @PostMapping
    ApiResponse<OwnerResponseDTO> createOwner(OwnerRequestDTO requestDTO);

    @PostMapping(path = "/id")
    ApiResponse<Long> getOwnerIdByUsername(String mobNo);

}
