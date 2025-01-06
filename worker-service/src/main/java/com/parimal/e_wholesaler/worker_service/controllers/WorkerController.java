package com.parimal.e_wholesaler.worker_service.controllers;

import com.parimal.e_wholesaler.worker_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.worker_service.dtos.WorkerDTO;
import com.parimal.e_wholesaler.worker_service.dtos.WorkerRequestDTO;
import com.parimal.e_wholesaler.worker_service.dtos.WorkerResponseDTO;
import com.parimal.e_wholesaler.worker_service.services.WorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/worker")
@AllArgsConstructor
public class WorkerController {

    private final WorkerService workerService;


    @PostMapping
    public WorkerResponseDTO createWorker(
            @RequestBody
            WorkerRequestDTO requestDTO
    ) {
        return workerService.createWorker(requestDTO);
    }

    @GetMapping(path = "{id}")
    public WorkerDTO getWorkerById(
            @PathVariable
            Long id
    ) {
        return workerService.getWorkerById(id);
    }

    @DeleteMapping(path = "{id}")
    public MessageDTO deleteWorkerById(
            @PathVariable
            Long id
    ) {
        return workerService.deleteWorkerById(id);
    }

}
