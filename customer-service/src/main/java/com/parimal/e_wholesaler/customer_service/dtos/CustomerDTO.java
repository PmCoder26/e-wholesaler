package com.parimal.e_wholesaler.customer_service.dtos;

import com.parimal.e_wholesaler.customer_service.utils.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Long id;

    private String name;

    private Gender gender;

    private Long mobNo;

    private String address;

    private String city;

    private String state;

}
