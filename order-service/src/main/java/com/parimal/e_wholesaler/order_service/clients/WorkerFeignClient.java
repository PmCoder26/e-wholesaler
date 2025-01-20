package com.parimal.e_wholesaler.order_service.clients;

import com.parimal.e_wholesaler.order_service.advices.ApiResponse;
import com.parimal.e_wholesaler.order_service.dtos.DataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "worker-service", path = "/workers/worker")
public interface WorkerFeignClient {

    @GetMapping(path = "/{workerId}/shop/{shopId}")
    ApiResponse<DataDTO<Boolean>> workerExistsByIdAndShopId(@PathVariable Long workerId, @PathVariable Long shopId);


}
