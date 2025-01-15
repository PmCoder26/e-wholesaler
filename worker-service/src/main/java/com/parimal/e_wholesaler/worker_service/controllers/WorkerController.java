package com.parimal.e_wholesaler.worker_service.controllers;

import com.parimal.e_wholesaler.worker_service.dtos.*;
import com.parimal.e_wholesaler.worker_service.services.WorkerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/worker")
@AllArgsConstructor
public class WorkerController {

    private final WorkerService workerService;


    @PostMapping
    public WorkerResponseDTO createWorker(
            @RequestBody @Valid WorkerRequestDTO requestDTO
    ) {
        return workerService.createWorker(requestDTO);
    }

    @GetMapping(path = "{id}")
    public WorkerDTO getWorkerById(
            @PathVariable Long id
    ) {
        return workerService.getWorkerById(id);
    }

    @DeleteMapping
    public MessageDTO deleteWorkerById(
            @RequestBody @Valid DeleteRequestDTO requestDTO
            ) {
        return workerService.deleteWorkerById(requestDTO);
    }

}
