package com.parimal.e_wholesaler.sales_service.controllers;

import com.parimal.e_wholesaler.sales_service.dtos.DailySalesDTO;
import com.parimal.e_wholesaler.sales_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.sales_service.dtos.SalesRequestDTO;
import com.parimal.e_wholesaler.sales_service.dtos.SalesResponseDTO;
import com.parimal.e_wholesaler.sales_service.services.DailySalesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/sales")
@AllArgsConstructor
public class DailySalesController {

    private final DailySalesService salesService;


    @PostMapping
    public SalesResponseDTO createDailySales(
            @RequestBody
            SalesRequestDTO requestDTO
    ) {
        return salesService.createDailySales(requestDTO);
    }

    @GetMapping(path = "{id}")
    public DailySalesDTO getDailySalesById(
            @PathVariable
            Long id
    ) {
        return salesService.getDailySalesById(id);
    }

    @DeleteMapping(path = "{id}")
    public MessageDTO deleteDailySalesById(
            @PathVariable
            Long id
    ) {
        return salesService.deleteSalesById(id);
    }

}
