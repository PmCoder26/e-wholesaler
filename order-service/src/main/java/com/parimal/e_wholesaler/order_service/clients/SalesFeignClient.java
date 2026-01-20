package com.parimal.e_wholesaler.order_service.clients;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.order_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.order_service.dtos.SalesUpdateRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sales-service", path = "/sales/sale")
public interface SalesFeignClient {

    @PutMapping(path = "/update-sales")
    ApiResponse<MessageDTO> updateDailySales(@RequestBody SalesUpdateRequestDTO requestDTO);

}
