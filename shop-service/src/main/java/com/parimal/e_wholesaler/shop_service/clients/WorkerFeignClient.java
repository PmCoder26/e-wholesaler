package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.dtos.ShopAndWorkersDTO;
import com.parimal.e_wholesaler.shop_service.dtos.WorkerRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.WorkerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "worker-service", path = "/workers/worker")
public interface WorkerFeignClient {

    @PostMapping(path = "/worker-count")
    ApiResponse<Long> getWorkerCount(@RequestBody List<Long> shopIdList);

    @PostMapping(path = "/shops")
    ApiResponse<List<ShopAndWorkersDTO>> getWorkersByShopIdList(List<Long> shopIdList);

    @PostMapping
    ApiResponse<WorkerResponseDTO> addWorker(WorkerRequestDTO requestDTO);

    @PostMapping(path = "/update")
    ApiResponse<WorkerResponseDTO> updateWorker(WorkerRequestDTO requestDTO);

}
