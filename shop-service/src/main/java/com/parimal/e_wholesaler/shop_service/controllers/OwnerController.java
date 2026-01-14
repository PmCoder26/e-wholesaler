package com.parimal.e_wholesaler.shop_service.controllers;

import com.parimal.e_wholesaler.shop_service.dtos.*;
import com.parimal.e_wholesaler.shop_service.dtos.owner.*;
import com.parimal.e_wholesaler.shop_service.dtos.product.*;
import com.parimal.e_wholesaler.shop_service.dtos.sales.DailyRevenueDTO;
import com.parimal.e_wholesaler.shop_service.dtos.sales.SalesRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopEditRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.ShopAndWorkersDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.WorkerDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.WorkerRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.WorkerUpdateRequestDTO;
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

    @PostMapping(path = "/id")
    public Long getOwnerIdByMobNo(
            HttpServletRequest request,
            @RequestBody String mobNo
    ) {
        return ownerService.getOwnerIdByMobNo(request, mobNo);
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
    public List<ShopDTO> getShopsByOwnerId(
            HttpServletRequest request,
            @PathVariable
            Long ownerId
    ) {
        return ownerService.getShopsByOwnerId(request, ownerId);
    }

    @PostMapping(path = "/{ownerId}/shop")
    public ShopResponseDTO createShop(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopRequestDTO requestDTO
    ) {
        return shopService.createShop(request, ownerId, requestDTO);
    }

    @PutMapping(path = "/{ownerId}/shop")
    public ShopDTO updateShop(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopEditRequestDTO requestDTO
    ) {
        return shopService.updateShop(request, ownerId, requestDTO);
    }

    @GetMapping(path = "/{ownerId}/shop/{shopId}/products")
    public List<ShopProductDTO> getProductsByShopId(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId
    ) {
        return shopService.getProductsByShopId(request, ownerId, shopId);
    }

    @GetMapping(path = "/{ownerId}/shop-workers")
    public List<ShopAndWorkersDTO> getShopWorkers(
            HttpServletRequest request,
            @PathVariable
            Long ownerId
    ) {
        return ownerService.getShopWorkersByOwnerId(request, ownerId);
    }

    @GetMapping(path = "/{ownerId}/daily-revenue")
    public List<DailyRevenueDTO> getDailyRevenue(
            HttpServletRequest request,
            @PathVariable
            Long ownerId
    ) {
        return shopService.getDailyRevenue(request, ownerId);
    }

    @PostMapping(path = "/shops/worker")
    public WorkerDTO addWorker(
            HttpServletRequest request,
            @RequestBody @Valid
            WorkerRequestDTO requestDTO
    ) {
        return shopService.addWorker(request, requestDTO);
    }

    @PostMapping(path = "/shops/worker/update")
    public WorkerDTO updateWorker(
            HttpServletRequest request,
            @RequestBody @Valid
            WorkerUpdateRequestDTO requestDTO
    ) {
        return shopService.updateWorker(request, requestDTO);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO deleteOwnerById(
            HttpServletRequest request,
            @PathVariable
            Long id
    ) {
        return ownerService.deleteOwnerById(request, id);
    }

    @PostMapping(path = "/{ownerId}/shop/products/shop-sub-product")
    public ShopSubProductResponseDTO addShopSubProduct(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopSubProductRequestDTO requestDTO
    ) {
        return shopService.addShopSubProduct(request, ownerId,requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shop/products/shop-sub-product")
    public MessageDTO removeShopSubProduct(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            RequestDTO requestDTO
    ) {
        return shopService.removeShopSubProduct(request, ownerId, requestDTO);
    }

    @PutMapping(path = "/{ownerId}/shop/products/shop-sub-product")
    public MessageDTO updateShopSubProduct(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopSubProductUpdateRequestDTO requestDTO
    ) {
        return shopService.updateShopSubProduct(request, ownerId, requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shop/products/product")
    public MessageDTO removeProductByShopIdAndProductName(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ProductRemoveRequestDTO requestDTO
    ) {
        return shopService.removeProductByShopIdAndProductName(request, ownerId, requestDTO);
    }

    @GetMapping(path = "/{ownerId}/shop/{shopId}/workers")
    public ShopAndWorkersDTO getWorkersByShopId(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId
    ) {
        return shopService.getWorkersByShopId(request, ownerId, shopId);
    }



}
