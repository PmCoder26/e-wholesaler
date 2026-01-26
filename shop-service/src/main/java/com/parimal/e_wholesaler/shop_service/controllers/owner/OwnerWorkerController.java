package com.parimal.e_wholesaler.shop_service.controllers.owner;

import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.*;
import com.parimal.e_wholesaler.shop_service.services.owner.OwnerWorkerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/owner")
@PreAuthorize(value = "hasRole('OWNER')")
@AllArgsConstructor
public class OwnerWorkerController {

    private final OwnerWorkerService ownerWorkerService;


    @GetMapping(path = "/{ownerId}/shop-workers")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public List<ShopAndWorkersDTO> getShopWorkers(
            @PathVariable
            Long ownerId
    ) {
        return ownerWorkerService.getShopWorkersByOwnerId(ownerId);
    }

    @PostMapping(path = "/{ownerId}/shops/worker")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public WorkerDTO addWorker(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            WorkerRequestDTO requestDTO
    ) {
        return ownerWorkerService.addWorker(ownerId,requestDTO);
    }

    @PutMapping(path = "/{ownerId}/shops/worker")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public WorkerDTO updateWorker(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            WorkerUpdateRequestDTO requestDTO
    ) {
        return ownerWorkerService.updateWorker(ownerId,requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shops/worker")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public MessageDTO deleteWorkerById(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            WorkerDeleteRequestDTO requestDTO
    ) {
        return ownerWorkerService.deleteWorkerById(ownerId, requestDTO);
    }

    @GetMapping(path = "/{ownerId}/shop/{shopId}/workers")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public ShopAndWorkersDTO getWorkersByShopId(
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId
    ) {
        return ownerWorkerService.getWorkersByShopId(ownerId, shopId);
    }

}
