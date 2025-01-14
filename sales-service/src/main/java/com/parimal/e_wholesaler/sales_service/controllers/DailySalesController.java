package com.parimal.e_wholesaler.sales_service.controllers;

import com.parimal.e_wholesaler.sales_service.dtos.*;
import com.parimal.e_wholesaler.sales_service.services.DailySalesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/sales")
@AllArgsConstructor
public class DailySalesController {

    private final DailySalesService salesService;


    @PostMapping
    public SalesResponseDTO createDailySales(
            @RequestBody @Valid SalesRequestDTO requestDTO
    ) throws Exception {
        return salesService.createDailySales(requestDTO);
    }

    @GetMapping(path = "{id}")
    public DailySalesDTO getDailySalesById(
            @PathVariable Long id
    ) {
        return salesService.getDailySalesById(id);
    }

    @DeleteMapping
    public MessageDTO deleteDailySalesById(
            @RequestBody @Valid RequestDTO requestDTO
    ) throws Exception {
        return salesService.deleteSalesById(requestDTO);
    }

    @PatchMapping
    public SalesResponseDTO updateDailySales(
            @RequestBody @Valid SalesUpdateRequestDTO requestDTO
    ) throws Exception {
        return salesService.updateDailySales(requestDTO);
    }

}
