package com.parimal.e_wholesaler.shop_service.controllers.owner;

import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.sales.DailyRevenueDTO;
import com.parimal.e_wholesaler.shop_service.dtos.sales.SalesRequestDTO;
import com.parimal.e_wholesaler.shop_service.services.owner.OwnerSalesService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/owner")
@PreAuthorize(value = "hasRole('OWNER')")
@AllArgsConstructor
public class OwnerSalesController {

    private final OwnerSalesService ownerSalesService;

    @PostMapping(path = "/shop/sales")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public MessageDTO createSalesForShop(
            @RequestBody
            SalesRequestDTO requestDTO
    ) {
        return ownerSalesService.createDailySales(requestDTO.getShopId());
    }

    @GetMapping(path = "/{ownerId}/daily-revenue")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public List<DailyRevenueDTO> getDailyRevenue(
            @PathVariable
            Long ownerId
    ) {
        return ownerSalesService.getDailyRevenue(ownerId);
    }

}
