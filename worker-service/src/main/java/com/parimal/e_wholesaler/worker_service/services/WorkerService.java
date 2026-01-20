package com.parimal.e_wholesaler.worker_service.services;

import com.parimal.e_wholesaler.worker_service.advices.ApiResponse;
import com.parimal.e_wholesaler.worker_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.worker_service.dtos.*;
import com.parimal.e_wholesaler.worker_service.entities.WorkerEntity;
import com.parimal.e_wholesaler.worker_service.exceptions.MyException;
import com.parimal.e_wholesaler.worker_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.worker_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.worker_service.repositories.WorkerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final ShopFeignClient shopFeignClient;
    private final ModelMapper modelMapper;


    @PreAuthorize(value = "hasAuthority('WORKER_CREATE')")
    public WorkerDTO createWorker(WorkerRequestDTO requestDTO) {
        shopExistenceCheck(requestDTO.getShopId());
        boolean workerExists = workerRepository.existsByMobNo(requestDTO.getMobNo());
        if(workerExists) {
            throw new ResourceAlreadyExistsException("Worker with mobile number: " + requestDTO.getMobNo() + " already exists.");
        }
        WorkerEntity toSave = modelMapper.map(requestDTO, WorkerEntity.class);
        WorkerEntity saved = workerRepository.save(toSave);
        return modelMapper.map(saved, WorkerDTO.class);
    }

    @PreAuthorize(value = "hasAuthority('WORKER_VIEW')")
    public WorkerDTO getWorkerById(Long id) {
        WorkerEntity worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker with id: " + id + " not found."));
        return modelMapper.map(worker, WorkerDTO.class);
    }

    @PreAuthorize(value = "hasRole('OWNER')")
    public Long getWorkerCount(List<Long> shopIdList) {
        AtomicLong workerCount = new AtomicLong(0L);
        shopIdList
                .forEach(shopId -> workerCount.addAndGet(workerRepository.countByShopId(shopId)));
        return workerCount.get();
    }

    @PreAuthorize(value = "hasRole('OWNER')")
    public List<ShopAndWorkersDTO> getWorkersByShopIdList(List<Long> shopIdList) {
        return shopIdList.stream()
                .map(shopId -> {
                    List<WorkerEntity> workers = workerRepository.findByShopId(shopId);
                    List<WorkerDTO> workerDTOS = workers.stream()
                            .map(workerEntity -> modelMapper.map(workerEntity, WorkerDTO.class))
                            .toList();
                    return new ShopAndWorkersDTO(shopId, workerDTOS);
                }).toList();
    }

    @PreAuthorize(value = "hasAuthority('WORKER_DELETE')")
    public MessageDTO deleteWorkerById(WorkerDeleteRequestDTO requestDTO) {
        boolean workerExists = workerRepository.existsByIdAndShopId(requestDTO.getWorkerId(), requestDTO.getShopId());

        if(workerExists) {
            workerRepository.deleteById(requestDTO.getWorkerId());
            return new MessageDTO("Worker deleted successfully");
        }

        throw new ResourceNotFoundException("Worker with id: " + requestDTO.getWorkerId() + " not found or permission for this worker denied.");
    }

    public DataDTO<Boolean> workerExistsByIdAndShopId(Long workerId, Long shopId) {
        boolean workerExists = workerRepository.existsByIdAndShopId(workerId, shopId);
        return new DataDTO<>(workerExists);
    }

    @PreAuthorize(value = "hasAuthority('WORKER_UPDATE')")
    public WorkerDTO updateWorker(WorkerUpdateRequestDTO requestDTO) {
        WorkerEntity worker = workerRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found."));
        worker.setName(requestDTO.getName());
        worker.setSalary(requestDTO.getSalary());
        worker.setMobNo(requestDTO.getMobNo());
        worker.setAddress(requestDTO.getAddress());
        worker.setCity(requestDTO.getCity());
        worker.setState(requestDTO.getState());
        WorkerEntity saved = workerRepository.save(worker);
        return modelMapper.map(saved, WorkerDTO.class);
    }

    public WorkerDataResponseDTO getWorkerDataByMobNo(String mobNo) {
        WorkerEntity worker = workerRepository.findByMobNo(mobNo)
                .orElseThrow(() -> new ResourceNotFoundException("Worker-id not found"));
        return new WorkerDataResponseDTO(worker.getId(), worker.getShopId());
    }

    @PreAuthorize(value = "hasRole('OWNER')")
    public ShopAndWorkersDTO getWorkersByShopId(Long shopId) {
        List<WorkerEntity> workers = workerRepository.findAllByShopId(shopId);
        List<WorkerDTO> workerDTOList=  workers.stream()
                .map(worker -> modelMapper.map(worker, WorkerDTO.class))
                .toList();
        return new ShopAndWorkersDTO(shopId, workerDTOList);
    }

    public Boolean workerExistsByMobNo(String mobNo) {
        return workerRepository.existsByMobNo(mobNo);
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

}
