package com.parimal.e_wholesaler.shop_service.dtos.worker;

import com.parimal.e_wholesaler.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDTO {

    private Long id;

    private String name;

    private Gender gender;

    private String mobNo;

    private String address;

    private String city;

    private String state;

    private Long shopId;

    private Double salary;

}
