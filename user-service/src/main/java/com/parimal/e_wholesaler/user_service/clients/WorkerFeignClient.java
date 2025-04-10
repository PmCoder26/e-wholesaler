package com.parimal.e_wholesaler.user_service.clients;

import com.parimal.e_wholesaler.user_service.advices.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "worker-service", path = "/workers/worker")
public interface WorkerFeignClient {

    @PostMapping(path = "/id")
    ApiResponse<Long> getWorkerIdByMobNo(@RequestHeader("Authorization") String token, String mobNo);

}
