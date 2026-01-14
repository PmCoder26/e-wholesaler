package com.parimal.e_wholesaler.worker_service.controllers;

import com.parimal.e_wholesaler.worker_service.dtos.*;
import com.parimal.e_wholesaler.worker_service.services.WorkerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/worker")
@AllArgsConstructor
public class WorkerController {

    private final WorkerService workerService;


    @PostMapping
    public WorkerDTO createWorker(
            HttpServletRequest request,
            @RequestBody @Valid WorkerRequestDTO requestDTO
    ) {
        return workerService.createWorker(request, requestDTO);
    }

    @PostMapping(path = "/worker-count")
    public Long getWorkerCount(
            HttpServletRequest request,
            @RequestBody
            List<Long> shopIdList
    ) {
        return workerService.getWorkerCount(request, shopIdList);
    }

    @PostMapping(path = "/shops")
    public List<ShopAndWorkersDTO> getWorkersByShopIdList(
            HttpServletRequest request,
            @RequestBody
            List<Long> shopIdList
    ) {
        return workerService.getWorkersByShopIdList(request, shopIdList);
    }

    @PostMapping(path = "/update")
    public WorkerDTO updateWorker(
            HttpServletRequest request,
            @RequestBody @Valid
            WorkerUpdateRequestDTO requestDTO
    ) {
        return workerService.updateWorker(request, requestDTO);
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

    @PostMapping(path = "/id")
    public Long getWorkerIdByMobNo(
            HttpServletRequest request,
            @RequestBody String mobNo
    ) {
        return workerService.getWorkerIdByMobNo(request, mobNo);
    }
 
    @DeleteMapping
    public MessageDTO deleteWorkerById(
            HttpServletRequest request,
            @RequestBody @Valid DeleteRequestDTO requestDTO
    ) {
        return workerService.deleteWorkerById(request, requestDTO);
    }

    @GetMapping(path = "/internal/shops/{shopId}")
    public ShopAndWorkersDTO getWorkersByShopId(
            HttpServletRequest request,
            @PathVariable
            Long shopId
    ) {
        return workerService.getWorkersByShopId(request, shopId);
    }

}
