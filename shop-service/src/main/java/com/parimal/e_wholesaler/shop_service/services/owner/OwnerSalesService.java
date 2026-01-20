package com.parimal.e_wholesaler.shop_service.services.owner;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.clients.OrderFeignClient;
import com.parimal.e_wholesaler.shop_service.clients.SalesFeignClient;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.PairDTO;
import com.parimal.e_wholesaler.shop_service.dtos.sales.DailyRevenueDTO;
import com.parimal.e_wholesaler.shop_service.dtos.sales.SalesRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.sales.SalesResponseDTO;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.common.exceptions.MyException;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.repositories.OwnerRepository;
import com.parimal.e_wholesaler.shop_service.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class OwnerSalesService {

    private final OwnerRepository ownerRepository;
    private final ShopRepository shopRepository;

    private final SalesFeignClient salesFeignClient;
    private final OrderFeignClient orderFeignClient;


    public MessageDTO createDailySales(Long shopId) {
        ApiResponse<SalesResponseDTO> salesResponse = salesFeignClient.createSalesForShop(new SalesRequestDTO(shopId));
        if(salesResponse.getError() != null) {
            throw new MyException(salesResponse.getError());
        }
        return new MessageDTO("Daily sales created for shop-id: " + salesResponse.getData().getId());
    }

    public List<DailyRevenueDTO> getDailyRevenue(Long ownerId) {
        boolean ownerExists = ownerRepository.existsById(ownerId);
        if(!ownerExists) throw new ResourceNotFoundException("Owner with id: " + ownerId + " not found.");
        List<ShopEntity> shopList = shopRepository.findByOwner_Id(ownerId);
        if(shopList.isEmpty()) return null;

        List<Long> shopIdList = shopList.stream()
                .map(ShopEntity::getId)
                .toList();

        ApiResponse<List<PairDTO<Long, Double>>> dailySalesResponse = salesFeignClient.getSalesByShopIdList(shopIdList);
        if(dailySalesResponse.getError() != null) throw new MyException(dailySalesResponse.getError());

        ApiResponse<List<PairDTO<Long, Long>>> dailyOrdersCountResponse = orderFeignClient.getDailyOrdersCountByShopIdList(shopIdList);
        if(dailyOrdersCountResponse.getError() != null)  throw new MyException(dailyOrdersCountResponse.getError());

        List<PairDTO<Long, Double>> salesList = dailySalesResponse.getData();
        List<PairDTO<Long, Long>> orderCountList = dailyOrdersCountResponse.getData();
        List<DailyRevenueDTO> revenueDTOList = new LinkedList<>();
        for (int x = 0; x < shopIdList.size(); x++) {
            PairDTO<Long, Double> sales = salesList.get(x);
            PairDTO<Long, Long> orderCount = orderCountList.get(x);
            ShopEntity shop = shopList.get(x);
            DailyRevenueDTO dailyRevenue = new DailyRevenueDTO(
                    shop.getName(),
                    shop.getCity(),
                    sales.getData2(),
                    orderCount.getData2());
            revenueDTOList.add(dailyRevenue);
        }
        return revenueDTOList;
    }

}
