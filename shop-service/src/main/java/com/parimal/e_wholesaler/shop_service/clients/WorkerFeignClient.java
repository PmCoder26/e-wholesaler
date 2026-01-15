package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.dtos.worker.ShopAndWorkersDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.WorkerDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.WorkerRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.WorkerUpdateRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "worker-service", path = "/workers/worker")
public interface WorkerFeignClient {

    @PostMapping(path = "/worker-count")
    ApiResponse<Long> getWorkerCount(@RequestBody List<Long> shopIdList);

    @PostMapping(path = "/shops")
    ApiResponse<List<ShopAndWorkersDTO>> getWorkersByShopIdList(List<Long> shopIdList);

    @PostMapping
    ApiResponse<WorkerDTO> addWorker(WorkerRequestDTO requestDTO);

    @PutMapping
    ApiResponse<WorkerDTO> updateWorker(WorkerUpdateRequestDTO requestDTO);

    @GetMapping(path = "/internal/shops/{shopId}")
    ApiResponse<ShopAndWorkersDTO> getWorkersByShopId(@PathVariable Long shopId);

}
