package com.parimal.e_wholesaler.worker_service.controllers;

import com.parimal.e_wholesaler.worker_service.dtos.*;
import com.parimal.e_wholesaler.worker_service.services.WorkerService;
import jakarta.servlet.http.HttpServletRequest;
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
            HttpServletRequest request,
            @RequestBody @Valid WorkerRequestDTO requestDTO
    ) {
        return workerService.createWorker(request, requestDTO);
    }

    @GetMapping(path = "/{id}")
    public WorkerDTO getWorkerById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        return workerService.getWorkerById(request, id);
    }

    @GetMapping(path = "/{workerId}/shop/{shopId}")
    public DataDTO<Boolean> workerExistsByIdAndShopId(
            HttpServletRequest request,
            @PathVariable Long workerId,
            @PathVariable Long shopId
    ) {
        return workerService.workerExistsByIdAndShopId(request, workerId, shopId);
    }

    @DeleteMapping
    public MessageDTO deleteWorkerById(
            HttpServletRequest request,
            @RequestBody @Valid DeleteRequestDTO requestDTO
    ) {
        return workerService.deleteWorkerById(request, requestDTO);
    }

}
