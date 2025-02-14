package com.parimal.e_wholesaler.sales_service.services;

import com.netflix.spectator.impl.AtomicDouble;
import com.parimal.e_wholesaler.sales_service.advices.ApiResponse;
import com.parimal.e_wholesaler.sales_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.sales_service.dtos.*;
import com.parimal.e_wholesaler.sales_service.entities.DailySalesEntity;
import com.parimal.e_wholesaler.sales_service.exceptions.MyException;
import com.parimal.e_wholesaler.sales_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.sales_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.sales_service.repositories.SalesRepository;
import com.parimal.e_wholesaler.sales_service.utils.SalesUpdate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DailySalesService {

    private final SalesRepository salesRepository;
    private final ModelMapper modelMapper;
    private final ShopFeignClient shopFeignClient;


    public SalesResponseDTO createDailySales(HttpServletRequest request, SalesRequestDTO requestDTO) {
        shopExistenceCheck(requestDTO.getShopId());
        boolean dailySalesExists = salesRepository.existsByCreatedAtAndShopId(LocalDate.now(), requestDTO.getShopId());
        if(!dailySalesExists) {
            DailySalesEntity toSave = modelMapper.map(requestDTO, DailySalesEntity.class);
            DailySalesEntity saved = salesRepository.save(toSave);
            return modelMapper.map(saved, SalesResponseDTO.class);
        }
        throw new RuntimeException("Daily sales for today already exists.");
    }

    public Double calculateSalesAmount(HttpServletRequest request, List<Long> shopIdList) {
        AtomicDouble totalSalesAmount = new AtomicDouble();
        shopIdList
                .forEach(shopId ->
                        totalSalesAmount.addAndGet(salesRepository.findAmountByShopIdAndCreatedAt(shopId, LocalDate.now()))
                );
        return totalSalesAmount.get();
    }

    public DailySalesDTO getDailySalesById(HttpServletRequest request, Long id) {
        DailySalesEntity dailySales = salesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Daily sales with id: " + id + " not found."));
        return modelMapper.map(dailySales, DailySalesDTO.class);
    }

    public MessageDTO deleteSalesById(HttpServletRequest request, RequestDTO requestDTO) {
        shopExistenceCheck(requestDTO.getShopId());
        boolean dailySalesExists = salesRepository.existsById(requestDTO.getSalesId());
        if(dailySalesExists) {
            salesRepository.deleteById(requestDTO.getSalesId());
            return new MessageDTO("Daily sales deleted successfully.");
        }
        throw new ResourceNotFoundException("Daily sales with id: " + requestDTO.getSalesId() + " not found.");
    }

    public SalesResponseDTO updateDailySales(HttpServletRequest request, SalesUpdateRequestDTO requestDTO) {
        DailySalesEntity dailySales = salesRepository.findById(requestDTO.getSalesId())
                .orElseThrow(() -> new ResourceNotFoundException("Daily sales with id: " + requestDTO.getSalesId() + " not found."));
        if(requestDTO.getUpdateMode().equals(SalesUpdate.CREDIT)) {
            dailySales.setAmount(dailySales.getAmount() + requestDTO.getAmount());
        }
        else {
            if(dailySales.getAmount() <= requestDTO.getAmount()) {
                throw new RuntimeException("Insufficient balance.");
            }
            dailySales.setAmount(dailySales.getAmount() - requestDTO.getAmount());
        }
        DailySalesEntity updated = salesRepository.save(dailySales);
        return modelMapper.map(updated, SalesResponseDTO.class);
    }

    public MessageDTO updateDailySalesAfterOrder(HttpServletRequest request, SalesUpdateRequestDTO2 requestDTO) {
        DailySalesEntity dailySales = salesRepository.findByCreatedAtAndShopId(LocalDate.now(), requestDTO.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Daily sales with shop-id: " + requestDTO.getShopId() + " not found."));
        if(requestDTO.getUpdateMode().equals(SalesUpdate.CREDIT)) {
            dailySales.setAmount(dailySales.getAmount() + requestDTO.getAmount());
        }
        else {
            if(dailySales.getAmount() <= requestDTO.getAmount()) {
                throw new RuntimeException("Insufficient balance.");
            }
            dailySales.setAmount(dailySales.getAmount() - requestDTO.getAmount());
        }
        salesRepository.save(dailySales);
        return new MessageDTO("Daily sales updated successfully.");
    }


    private void shopExistenceCheck(Long shopId) {
        ApiResponse<DataDTO<Boolean>> shopExistsData = shopFeignClient.shopExistsById(shopId);
        if(shopExistsData.getData() == null) {
            throw new MyException(shopExistsData.getError());
        }
        if(!shopExistsData.getData().getData()) {
            throw new ResourceNotFoundException("Shop with id: " + shopId + " not found.");
        }
    }

}
