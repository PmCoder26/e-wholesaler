package com.parimal.e_wholesaler.shop_service.controllers;

import com.parimal.e_wholesaler.shop_service.dtos.*;
import com.parimal.e_wholesaler.shop_service.dtos.owner.*;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopEditRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.services.OwnerService;
import com.parimal.e_wholesaler.shop_service.services.ShopService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/owner")
@AllArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final ShopService shopService;


    @PostMapping
    public OwnerResponseDTO createOwner(
            HttpServletRequest request,
            @RequestBody @Valid
            OwnerRequestDTO requestDTO
    ) {
        return ownerService.createOwner(request, requestDTO);
    }

    @PostMapping(path = "/shop/sales")
    public MessageDTO createSalesForShop(
            HttpServletRequest request,
            @RequestBody
            SalesRequestDTO requestDTO
    ) {
        return ownerService.createDailySales(request, requestDTO.getShopId());
    }

    @GetMapping(path = "/{id}")
    public OwnerDTO getOwnerById(
            HttpServletRequest request,
            @PathVariable
            Long id
    ) {
        return ownerService.getOwnerById(request, id);
    }

    @GetMapping(path = "/{ownerId}/home")
    public OwnerHomeDTO getHomeDetails(
            HttpServletRequest request,
            @PathVariable
            Long ownerId
    ) {
        return ownerService.getHomeDetails(request, ownerId);
    }

    @GetMapping(path = "/{ownerId}/shops")
    public List<ShopDTO> getShopsById(
            HttpServletRequest request,
            @PathVariable
            Long ownerId
    ) {
        return ownerService.getShopsById(request, ownerId);
    }

    @PostMapping(path = "/shop")
    public ShopResponseDTO createShop(
            HttpServletRequest request,
            @RequestBody @Valid
            ShopRequestDTO requestDTO
    ) {
        return shopService.createShop(request, requestDTO);
    }

    @PutMapping(path = "/shop")
    public ShopDTO updateShop(
            HttpServletRequest request,
            @RequestBody @Valid
            ShopEditRequestDTO requestDTO
    ) {
        return shopService.updateShop(request, requestDTO);
    }

    @GetMapping(path = "/shop/{shopId}/products")
    public List<ShopProductDTO> getProductsByShopId(
            HttpServletRequest request,
            @PathVariable
            Long shopId
    ) {
        return shopService.getProductsByShopId(request, shopId);
    }

    @GetMapping(path = "/{ownerId}/daily-revenue")
    public List<DailyRevenueDTO> getDailyRevenue(
            HttpServletRequest request,
            @PathVariable
            Long ownerId
    ) {
        return shopService.getDailyRevenue(request, ownerId);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO deleteOwnerById(
            HttpServletRequest request,
            @PathVariable
            Long id
    ) {
        return ownerService.deleteOwnerById(request, id);
    }



}
