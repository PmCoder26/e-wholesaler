package com.parimal.e_wholesaler.worker_service.dtos;

import com.parimal.e_wholesaler.worker_service.utils.Gender;
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

    private Long mobNo;

    private String address;

    private String city;

    private String state;

    private Long shopId;

    private Double salary;

}
