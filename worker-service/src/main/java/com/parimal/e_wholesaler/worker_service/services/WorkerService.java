package com.parimal.e_wholesaler.worker_service.services;

import com.parimal.e_wholesaler.worker_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.worker_service.dtos.WorkerDTO;
import com.parimal.e_wholesaler.worker_service.dtos.WorkerRequestDTO;
import com.parimal.e_wholesaler.worker_service.dtos.WorkerResponseDTO;
import com.parimal.e_wholesaler.worker_service.entities.WorkerEntity;
import com.parimal.e_wholesaler.worker_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.worker_service.repositories.WorkerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final ModelMapper modelMapper;


    public WorkerResponseDTO createWorker(WorkerRequestDTO requestDTO) {
        return null;
    }

    public WorkerDTO getWorkerById(Long id) {
        WorkerEntity worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker with id: " + id + " not found."));
        return modelMapper.map(worker, WorkerDTO.class);
    }

    public MessageDTO deleteWorkerById(Long id) {
        boolean workerExists = workerRepository.existsById(id);
        if(workerExists) {
            workerRepository.deleteById(id);
            return new MessageDTO("Worker deleted successfully.");
        }
        throw new ResourceNotFoundException("Worker with id: " + id + " not found.");
    }

}
