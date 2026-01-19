package com.parimal.e_wholesaler.user_service.clients;

import com.parimal.e_wholesaler.user_service.advices.ApiResponse;
import com.parimal.e_wholesaler.user_service.dtos.WorkerDataResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "worker-service", path = "/workers/worker/auth")
public interface WorkerFeignClient {

    @PostMapping(path = "/exists")
    ApiResponse<Boolean> workerExistsByMobNo(String mobNo);

    @PostMapping("/data")
    ApiResponse<WorkerDataResponseDTO> getWorkerDataByMobNo(String mobNo);

}
