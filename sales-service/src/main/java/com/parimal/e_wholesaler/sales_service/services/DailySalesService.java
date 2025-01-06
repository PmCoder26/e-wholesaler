package com.parimal.e_wholesaler.sales_service.services;

import com.parimal.e_wholesaler.sales_service.dtos.DailySalesDTO;
import com.parimal.e_wholesaler.sales_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.sales_service.dtos.SalesRequestDTO;
import com.parimal.e_wholesaler.sales_service.dtos.SalesResponseDTO;
import com.parimal.e_wholesaler.sales_service.entities.DailySalesEntity;
import com.parimal.e_wholesaler.sales_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.sales_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.sales_service.repositories.SalesRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DailySalesService {

    private final SalesRepository salesRepository;
    private final ModelMapper modelMapper;


    public SalesResponseDTO createDailySales(SalesRequestDTO requestDTO) {
        boolean dailySalesExists = salesRepository.existsByCreatedAtAndShopId(LocalDate.now(), requestDTO.getShopId());
        if(!dailySalesExists) {
            DailySalesEntity toSave = modelMapper.map(requestDTO, DailySalesEntity.class);
            DailySalesEntity saved = salesRepository.save(toSave);
            return modelMapper.map(saved, SalesResponseDTO.class);
        }
        throw new ResourceAlreadyExistsException("Daly sales for today already exists.");
    }

    public DailySalesDTO getDailySalesById(Long id) {
        DailySalesEntity dailySales = salesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Daily sales with id: " + id + " not found."));
        return modelMapper.map(dailySales, DailySalesDTO.class);
    }

    public MessageDTO deleteSalesById(Long id) {
        boolean dailySalesExists = salesRepository.existsById(id);
        if(dailySalesExists) {
            salesRepository.deleteById(id);
            return new MessageDTO("Daily sales deleted successfully.");
        }
        throw new ResourceNotFoundException("Daily sales with id: " + id + " not found.");
    }

}
