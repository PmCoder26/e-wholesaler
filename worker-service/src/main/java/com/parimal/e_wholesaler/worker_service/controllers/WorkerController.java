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
public class WorkerController {

    private final WorkerService workerService;


    @PostMapping
    @PreAuthorize(value = "hasAuthority('WORKER_CREATE')")
    public WorkerDTO createWorker(
            @RequestBody @Valid WorkerRequestDTO requestDTO
    ) {
        return workerService.createWorker(requestDTO);
    }

    @PostMapping(path = "/worker-count")
    @PreAuthorize(value = "hasAuthority('WORKER_VIEW')")
    public Long getWorkerCount(
            @RequestBody
            List<Long> shopIdList
    ) {
        return workerService.getWorkerCount(shopIdList);
    }

    @PostMapping(path = "/shops")
    @PreAuthorize(value = "hasAuthority('WORKER_VIEW')")
    public List<ShopAndWorkersDTO> getWorkersByShopIdList(
            @RequestBody
            List<Long> shopIdList
    ) {
        return workerService.getWorkersByShopIdList(shopIdList);
    }

    @PutMapping
    @PreAuthorize(value = "hasAuthority('WORKER_UPDATE')")
    public WorkerDTO updateWorker(
            @RequestBody @Valid
            WorkerUpdateRequestDTO requestDTO
    ) {
        return workerService.updateWorker(requestDTO);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize(value = "hasAuthority('WORKER_VIEW')")
    public WorkerDTO getWorkerById(
            @PathVariable Long id
    ) {
        return workerService.getWorkerById(id);
    }

    @GetMapping(path = "/{workerId}/shop/{shopId}")
    @PreAuthorize(value = "hasAuthority('WORKER_VIEW')")
    public DataDTO<Boolean> workerExistsByIdAndShopId(
            @PathVariable Long workerId,
            @PathVariable Long shopId
    ) {
        return workerService.workerExistsByIdAndShopId(workerId, shopId);
    }
 
    @DeleteMapping
    @PreAuthorize(value = "hasAuthority('WORKER_DELETE')")
    public MessageDTO deleteWorkerById(
            @RequestBody @Valid WorkerDeleteRequestDTO requestDTO
    ) {
        return workerService.deleteWorkerById(requestDTO);
    }

    @GetMapping(path = "/internal/shops/{shopId}")
    @PreAuthorize(value = "hasAuthority('WORKER_VIEW')")
    public ShopAndWorkersDTO getWorkersByShopId(
            @PathVariable
            Long shopId
    ) {
        return workerService.getWorkersByShopId(shopId);
    }

}
