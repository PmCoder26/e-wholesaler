package com.parimal.e_wholesaler.sales_service.services;

import com.parimal.e_wholesaler.sales_service.advices.ApiResponse;
import com.parimal.e_wholesaler.sales_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.sales_service.dtos.*;
import com.parimal.e_wholesaler.sales_service.entities.DailySalesEntity;
import com.parimal.e_wholesaler.sales_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.sales_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.sales_service.repositories.SalesRepository;
import com.parimal.e_wholesaler.sales_service.utils.SalesUpdateMode;
import jakarta.ws.rs.sse.Sse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DailySalesService {

    private final SalesRepository salesRepository;
    private final ModelMapper modelMapper;
    private final ShopFeignClient shopFeignClient;


    public SalesResponseDTO createDailySales(SalesRequestDTO requestDTO) throws Exception {
        shopExistenceCheck(requestDTO.getShopId());
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

    public MessageDTO deleteSalesById(RequestDTO requestDTO) throws Exception {
        shopExistenceCheck(requestDTO.getShopId());
        boolean dailySalesExists = salesRepository.existsById(requestDTO.getSalesId());
        if(dailySalesExists) {
            salesRepository.deleteById(requestDTO.getSalesId());
            return new MessageDTO("Daily sales deleted successfully.");
        }
        throw new ResourceNotFoundException("Daily sales with id: " + requestDTO.getSalesId() + " not found.");
    }

    public SalesResponseDTO updateDailySales(SalesUpdateRequestDTO requestDTO) throws Exception {
        DailySalesEntity dailySales = salesRepository.findById(requestDTO.getSalesId())
                .orElseThrow(() -> new ResourceNotFoundException("Daily sales with id: " + requestDTO.getSalesId() + " not found."));
        if(requestDTO.getUpdateMode().equals(SalesUpdateMode.CREDIT)) {
            dailySales.setAmount(dailySales.getAmount() + requestDTO.getAmount());
        }
        else {
            if(dailySales.getAmount() <= requestDTO.getAmount()) {
                throw new Exception("Insufficient balance.");
            }
            dailySales.setAmount(dailySales.getAmount() - requestDTO.getAmount());
        }
        DailySalesEntity updated = salesRepository.save(dailySales);
        return modelMapper.map(updated, SalesResponseDTO.class);
    }

    private void shopExistenceCheck(Long shopId) throws Exception {
        ApiResponse<DataDTO<Boolean>> shopExistsData = shopFeignClient.shopExistsById(shopId);
        if(shopExistsData.getData() == null) {
            throw new Exception(shopExistsData.getError().getMessage());
        }
        if(!shopExistsData.getData().getData()) {
            throw new ResourceNotFoundException("Shop with id: " + shopId + " not found.");
        }
    }

}
