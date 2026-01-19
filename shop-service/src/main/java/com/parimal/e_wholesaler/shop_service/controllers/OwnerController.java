package com.parimal.e_wholesaler.shop_service.controllers;

import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerHomeDTO;
import com.parimal.e_wholesaler.shop_service.dtos.product.*;
import com.parimal.e_wholesaler.shop_service.dtos.sales.DailyRevenueDTO;
import com.parimal.e_wholesaler.shop_service.dtos.sales.SalesRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopEditRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.*;
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


    @PostMapping(path = "/shop/sales")
    public MessageDTO createSalesForShop(
            @RequestBody
            SalesRequestDTO requestDTO
    ) {
        return ownerService.createDailySales(requestDTO.getShopId());
    }

    @GetMapping(path = "/{id}")
    public OwnerDTO getOwnerById(
            @PathVariable
            Long id
    ) {
        return ownerService.getOwnerById(id);
    }

    @GetMapping(path = "/{ownerId}/home")
    public OwnerHomeDTO getHomeDetails(
            @PathVariable
            Long ownerId
    ) {
        return ownerService.getHomeDetails(ownerId);
    }

    @GetMapping(path = "/{ownerId}/shops")
    public List<ShopDTO> getShopsByOwnerId(
            @PathVariable
            Long ownerId
    ) {
        return ownerService.getShopsByOwnerId(ownerId);
    }

    @PostMapping(path = "/{ownerId}/shop")
    public ShopResponseDTO createShop(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopRequestDTO requestDTO
    ) {
        return shopService.createShop(ownerId, requestDTO);
    }

    @PutMapping(path = "/{ownerId}/shop")
    public ShopDTO updateShop(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopEditRequestDTO requestDTO
    ) {
        return shopService.updateShop(ownerId, requestDTO);
    }

    @GetMapping(path = "/{ownerId}/shop/{shopId}/products")
    public List<ShopProductDTO> getProductsByShopId(
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId
    ) {
        return shopService.getProductsByShopId(ownerId, shopId);
    }

    @GetMapping(path = "/{ownerId}/shop-workers")
    public List<ShopAndWorkersDTO> getShopWorkers(
            @PathVariable
            Long ownerId
    ) {
        return ownerService.getShopWorkersByOwnerId(ownerId);
    }

    @GetMapping(path = "/{ownerId}/daily-revenue")
    public List<DailyRevenueDTO> getDailyRevenue(
            @PathVariable
            Long ownerId
    ) {
        return shopService.getDailyRevenue(ownerId);
    }

    @PostMapping(path = "/{ownerId}/shops/worker")
    public WorkerDTO addWorker(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            WorkerRequestDTO requestDTO
    ) {
        return shopService.addWorker(ownerId,requestDTO);
    }

    @PutMapping(path = "/{ownerId}/shops/worker")
    public WorkerDTO updateWorker(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            WorkerUpdateRequestDTO requestDTO
    ) {
        return shopService.updateWorker(ownerId,requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shops/worker")
    public MessageDTO deleteWorkerById(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            WorkerDeleteRequestDTO requestDTO
    ) {
            return shopService.deleteWorkerById(ownerId, requestDTO);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO deleteOwnerById(
            @PathVariable
            Long id
    ) {
        return ownerService.deleteOwnerById(id);
    }

    @PostMapping(path = "/{ownerId}/shop/products/shop-sub-product")
    public ShopSubProductResponseDTO addShopSubProduct(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopSubProductRequestDTO requestDTO
    ) {
        return shopService.addShopSubProduct(ownerId,requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shop/products/shop-sub-product")
    public MessageDTO removeShopSubProduct(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            RequestDTO requestDTO
    ) {
        return shopService.removeShopSubProduct(ownerId, requestDTO);
    }

    @PutMapping(path = "/{ownerId}/shop/products/shop-sub-product")
    public MessageDTO updateShopSubProduct(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopSubProductUpdateRequestDTO requestDTO
    ) {
        return shopService.updateShopSubProduct(ownerId, requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shop/products/product")
    public MessageDTO removeProductByShopIdAndProductName(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ProductRemoveRequestDTO requestDTO
    ) {
        return shopService.removeProductByShopIdAndProductName(ownerId, requestDTO);
    }

    @GetMapping(path = "/{ownerId}/shop/{shopId}/workers")
    public ShopAndWorkersDTO getWorkersByShopId(
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId
    ) {
        return shopService.getWorkersByShopId(ownerId, shopId);
    }



}
