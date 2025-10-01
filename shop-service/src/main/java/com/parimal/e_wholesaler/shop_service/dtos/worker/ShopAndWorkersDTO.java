package com.parimal.e_wholesaler.shop_service.dtos.worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopAndWorkersDTO {

    private Long shopId;

    private String shopName;

    private List<WorkerDTO> workerList;

}
