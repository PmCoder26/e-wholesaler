package com.parimal.e_wholesaler.worker_service.services;

import com.parimal.e_wholesaler.worker_service.advices.ApiResponse;
import com.parimal.e_wholesaler.worker_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.worker_service.dtos.*;
import com.parimal.e_wholesaler.worker_service.entities.WorkerEntity;
import com.parimal.e_wholesaler.worker_service.exceptions.MyException;
import com.parimal.e_wholesaler.worker_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.worker_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.worker_service.exceptions.UnAuthorizedAccessException;
import com.parimal.e_wholesaler.worker_service.repositories.WorkerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final ShopFeignClient shopFeignClient;
    private final ModelMapper modelMapper;


    public WorkerResponseDTO createWorker(WorkerRequestDTO requestDTO) {
        shopExistenceCheck(requestDTO.getShopId());
        boolean workerExists = workerRepository.existsByMobNo(requestDTO.getMobNo());
        if(workerExists) {
            throw new ResourceAlreadyExistsException("Worker with mobile number: " + requestDTO.getMobNo() + " already exists.");
        }
        WorkerEntity toSave = modelMapper.map(requestDTO, WorkerEntity.class);
        WorkerEntity saved = workerRepository.save(toSave);
        return modelMapper.map(saved, WorkerResponseDTO.class);
    }

    public WorkerDTO getWorkerById(Long id) {
        WorkerEntity worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker with id: " + id + " not found."));
        return modelMapper.map(worker, WorkerDTO.class);
    }

    public MessageDTO deleteWorkerById(DeleteRequestDTO requestDTO) {
        shopExistenceCheck(requestDTO.getShopId());
        WorkerEntity worker = workerRepository.findById(requestDTO.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker with id: " + requestDTO.getWorkerId() + " not found."));
        if(requestDTO.getShopId().equals(worker.getShopId())) {
            workerRepository.deleteById(requestDTO.getWorkerId());
            return new MessageDTO("Worker deleted successfully.");
        }
        throw new UnAuthorizedAccessException("Worker doesn't belong to the shop with id: " + requestDTO.getShopId());
    }

    private void shopExistenceCheck(Long shopId) {
        ApiResponse<DataDTO<Boolean>> shopExistsData = shopFeignClient.shopExistsById(shopId);
        if(shopExistsData.getData() == null) {
            throw new MyException(shopExistsData.getError().getMessage());
        }
        if(!shopExistsData.getData().getData()) {
            throw new ResourceNotFoundException("Shop with id: " + shopId + " not found.");
        }
    }

    public DataDTO<Boolean> workerExistsByIdAndShopId(Long workerId, Long shopId) {
        boolean workerExists = workerRepository.existsByIdAndShopId(workerId, shopId);
        return new DataDTO<>(workerExists);
    }
}
