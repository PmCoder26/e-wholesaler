package com.parimal.e_wholesaler.worker_service.controllers;

import com.parimal.e_wholesaler.worker_service.dtos.*;
import com.parimal.e_wholesaler.worker_service.services.WorkerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/worker")
@AllArgsConstructor
@PreAuthorize(value = "hasAnyRole('OWNER', 'WORKER')")
public class WorkerController {

    private final WorkerService workerService;


    @PostMapping
    public WorkerDTO createWorker(
            @RequestBody @Valid WorkerRequestDTO requestDTO
    ) {
        return workerService.createWorker(requestDTO);
    }

    @PostMapping(path = "/worker-count")
    public Long getWorkerCount(
            HttpServletRequest request,
            @RequestBody
            List<Long> shopIdList
    ) {
        return workerService.getWorkerCount(shopIdList);
    }

    @PostMapping(path = "/shops")
    public List<ShopAndWorkersDTO> getWorkersByShopIdList(
            HttpServletRequest request,
            @RequestBody
            List<Long> shopIdList
    ) {
        return workerService.getWorkersByShopIdList(shopIdList);
    }

    @PutMapping
    public WorkerDTO updateWorker(
            @RequestBody @Valid
            WorkerUpdateRequestDTO requestDTO
    ) {
        return workerService.updateWorker(requestDTO);
    }

    @GetMapping(path = "/{id}")
    public WorkerDTO getWorkerById(
            @PathVariable Long id
    ) {
        return workerService.getWorkerById(id);
    }

    @GetMapping(path = "/{workerId}/shop/{shopId}")
    public DataDTO<Boolean> workerExistsByIdAndShopId(
            @PathVariable Long workerId,
            @PathVariable Long shopId
    ) {
        return workerService.workerExistsByIdAndShopId(workerId, shopId);
    }
 
    @DeleteMapping
    public MessageDTO deleteWorkerById(
            @RequestBody @Valid WorkerDeleteRequestDTO requestDTO
    ) {
        return workerService.deleteWorkerById(requestDTO);
    }

    @GetMapping(path = "/internal/shops/{shopId}")
    public ShopAndWorkersDTO getWorkersByShopId(
            HttpServletRequest request,
            @PathVariable
            Long shopId
    ) {
        return workerService.getWorkersByShopId(shopId);
    }

}
