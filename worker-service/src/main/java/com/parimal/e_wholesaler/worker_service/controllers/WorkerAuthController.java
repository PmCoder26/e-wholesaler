package com.parimal.e_wholesaler.worker_service.controllers;

import com.parimal.e_wholesaler.worker_service.dtos.WorkerDataResponseDTO;
import com.parimal.e_wholesaler.worker_service.services.WorkerService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/worker/auth")
@AllArgsConstructor
@PreAuthorize(value = "hasRole('USER_SERVICE')")
public class WorkerAuthController {

    private final WorkerService workerService;

    @PostMapping(path = "/exists")
    public Boolean workerExistsByMobNo(
            @RequestBody
            String mobNo
    ) {
        return workerService.workerExistsByMobNo(mobNo);
    }

    @PostMapping(path = "/data")
    public WorkerDataResponseDTO getWorkerDataByMobNo(
            @RequestBody String mobNo
    ) {
        return workerService.getWorkerDataByMobNo(mobNo);
    }

}
