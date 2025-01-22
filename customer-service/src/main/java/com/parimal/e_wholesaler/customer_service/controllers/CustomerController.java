package com.parimal.e_wholesaler.customer_service.controllers;

import com.parimal.e_wholesaler.customer_service.dtos.CustomerDTO;
import com.parimal.e_wholesaler.customer_service.dtos.CustomerRequestDTO;
import com.parimal.e_wholesaler.customer_service.dtos.CustomerResponseDTO;
import com.parimal.e_wholesaler.customer_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.customer_service.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
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
            HttpServletRequest request,
            @RequestBody @Valid CustomerRequestDTO requestDTO
    ) {
        return customerService.createCustomer(request, requestDTO);
    }

    @GetMapping(path = "/{id}")
    public CustomerDTO getCustomerById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        return customerService.getCustomerById(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO removeCustomerById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        return customerService.deleteCustomerById(request, id);
    }

}
