package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.dtos.DataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "worker-service", path = "/workers/worker")
public interface WorkerFeignClient {

    @PostMapping(path = "/worker-count")
    ApiResponse<Long> getWorkerCount(@RequestBody List<Long> shopIdList);

}
