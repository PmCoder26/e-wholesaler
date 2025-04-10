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
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@AllArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final ShopFeignClient shopFeignClient;
    private final ModelMapper modelMapper;


    public WorkerResponseDTO createWorker(HttpServletRequest request, WorkerRequestDTO requestDTO) {
        shopExistenceCheck(requestDTO.getShopId());
        boolean workerExists = workerRepository.existsByMobNo(requestDTO.getMobNo());
        if(workerExists) {
            throw new ResourceAlreadyExistsException("Worker with mobile number: " + requestDTO.getMobNo() + " already exists.");
        }
        WorkerEntity toSave = modelMapper.map(requestDTO, WorkerEntity.class);
        WorkerEntity saved = workerRepository.save(toSave);
        return modelMapper.map(saved, WorkerResponseDTO.class);
    }

    public WorkerDTO getWorkerById(HttpServletRequest request, Long id) {
        WorkerEntity worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker with id: " + id + " not found."));
        return modelMapper.map(worker, WorkerDTO.class);
    }

    public Long getWorkerCount(HttpServletRequest request, List<Long> shopIdList) {
        AtomicLong workerCount = new AtomicLong(0L);
        shopIdList
                .forEach(shopId -> workerCount.addAndGet(workerRepository.countByShopId(shopId)));
        return workerCount.get();
    }

    public List<ShopAndWorkersDTO> getWorkersByShopIdList(HttpServletRequest request, List<Long> shopIdList) {
        List<ShopAndWorkersDTO> shopAndWorkers = shopIdList.stream()
                .map(shopId -> {
                    List<WorkerEntity> workers = workerRepository.findByShopId(shopId);
                    List<WorkerDTO> workerDTOS = workers.stream()
                            .map(workerEntity -> modelMapper.map(workerEntity, WorkerDTO.class))
                            .toList();
                    return new ShopAndWorkersDTO(shopId, "", workerDTOS);
                }).toList();
        return shopAndWorkers;
    }

    public MessageDTO deleteWorkerById(HttpServletRequest request, DeleteRequestDTO requestDTO) {
        shopExistenceCheck(requestDTO.getShopId());
        WorkerEntity worker = workerRepository.findById(requestDTO.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker with id: " + requestDTO.getWorkerId() + " not found."));
        if(requestDTO.getShopId().equals(worker.getShopId())) {
            workerRepository.deleteById(requestDTO.getWorkerId());
            return new MessageDTO("Worker deleted successfully.");
        }
        throw new UnAuthorizedAccessException("Worker doesn't belong to the shop with id: " + requestDTO.getShopId());
    }

    public DataDTO<Boolean> workerExistsByIdAndShopId(HttpServletRequest request, Long workerId, Long shopId) {
        boolean workerExists = workerRepository.existsByIdAndShopId(workerId, shopId);
        return new DataDTO<>(workerExists);
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

    public WorkerDTO updateWorker(HttpServletRequest request, WorkerRequestDTO requestDTO) {
        WorkerEntity worker = workerRepository.findByMobNo(requestDTO.getMobNo())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found."));
        worker.setName(requestDTO.getName());
        worker.setSalary(requestDTO.getSalary());
        worker.setAddress(requestDTO.getAddress());
        worker.setCity(requestDTO.getCity());
        WorkerEntity saved = workerRepository.save(worker);
        return modelMapper.map(saved, WorkerDTO.class);
    }

    public Long getWorkerIdByMobNo(HttpServletRequest request, String mobNo) {
        return workerRepository.findIdByMobNo(mobNo)
                .orElseThrow(() -> new ResourceNotFoundException("Worker-id with mobile number: " + mobNo + " not found"));
    }

}
