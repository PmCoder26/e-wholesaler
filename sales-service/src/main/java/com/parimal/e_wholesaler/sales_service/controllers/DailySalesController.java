package com.parimal.e_wholesaler.sales_service.controllers;

import com.parimal.e_wholesaler.sales_service.dtos.*;
import com.parimal.e_wholesaler.sales_service.services.DailySalesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/sale")
@AllArgsConstructor
public class DailySalesController {

    private final DailySalesService salesService;


    @PostMapping
    public SalesResponseDTO createDailySales(
            HttpServletRequest request,
            @RequestBody @Valid SalesRequestDTO requestDTO
    ) {
        return salesService.createDailySales(request, requestDTO);
    }

    @PostMapping(path = "/sales-amount")
    public Double calculateSalesAmount(
            HttpServletRequest request,
            @RequestBody List<Long> shopIdList
    ) {
        return salesService.calculateSalesAmount(request, shopIdList);
    }

    @PostMapping(path = "/shops")
    public List<PairDTO<Long, Double>> getSalesByShopIdList(
            HttpServletRequest request,
            @RequestBody
            List<Long> shopIdList
    ) {
        return salesService.getSalesByShopIdList(request, shopIdList);
    }

    @GetMapping(path = "/{id}")
    public DailySalesDTO getDailySalesById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        return salesService.getDailySalesById(request, id);
    }

    @DeleteMapping
    public MessageDTO deleteDailySalesById(
            HttpServletRequest request,
            @RequestBody @Valid RequestDTO requestDTO
    ) {
        return salesService.deleteSalesById(request, requestDTO);
    }

    @PatchMapping
    public SalesResponseDTO updateDailySales(
            HttpServletRequest request,
            @RequestBody @Valid SalesUpdateRequestDTO requestDTO
    ) {
        return salesService.updateDailySales(request, requestDTO);
    }

    @PutMapping(path = "/update-sales")
    public MessageDTO updateDailySalesAfterOrder(
            HttpServletRequest request,
            @RequestBody @Valid SalesUpdateRequestDTO2 requestDTO
    ) {
        return salesService.updateDailySalesAfterOrder(request, requestDTO);
    }

}
