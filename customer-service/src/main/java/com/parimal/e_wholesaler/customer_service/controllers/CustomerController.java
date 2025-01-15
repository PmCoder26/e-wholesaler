package com.parimal.e_wholesaler.customer_service.controllers;

import com.parimal.e_wholesaler.customer_service.dtos.CustomerDTO;
import com.parimal.e_wholesaler.customer_service.dtos.CustomerRequestDTO;
import com.parimal.e_wholesaler.customer_service.dtos.CustomerResponseDTO;
import com.parimal.e_wholesaler.customer_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.customer_service.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/customer")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping
    public CustomerResponseDTO createCustomer(
            @RequestBody @Valid CustomerRequestDTO requestDTO
    ) {
        return customerService.createCustomer(requestDTO);
    }

    @GetMapping(path = "/{id}")
    public CustomerDTO getCustomerById(
            @PathVariable Long id
    ) {
        return customerService.getCustomerById(id);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO removeCustomerById(
            @PathVariable Long id
    ) {
        return customerService.deleteCustomerById(id);
    }

}
