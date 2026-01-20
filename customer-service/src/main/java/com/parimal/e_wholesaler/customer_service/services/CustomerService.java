package com.parimal.e_wholesaler.customer_service.services;

import com.parimal.e_wholesaler.common.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.customer_service.dtos.CustomerDTO;
import com.parimal.e_wholesaler.customer_service.dtos.CustomerRequestDTO;
import com.parimal.e_wholesaler.customer_service.dtos.CustomerResponseDTO;
import com.parimal.e_wholesaler.customer_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.customer_service.entities.CustomerEntity;
import com.parimal.e_wholesaler.customer_service.repositories.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;


    public CustomerResponseDTO createCustomer(HttpServletRequest request, CustomerRequestDTO requestDTO) {
        boolean customerExists = customerRepository.findByMobNo(requestDTO.getMobNo());
        if(!customerExists) {
            CustomerEntity toSave = modelMapper.map(requestDTO, CustomerEntity.class);
            CustomerEntity saved = customerRepository.save(toSave);
            return modelMapper.map(saved, CustomerResponseDTO.class);
        }
        throw new ResourceAlreadyExistsException("Customer already exists with the provided mobile number.");
    }

    public CustomerDTO getCustomerById(HttpServletRequest request, Long id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found."));
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public MessageDTO deleteCustomerById( HttpServletRequest request, Long id) {
        boolean customerExists = customerRepository.existsById(id);
        if(customerExists) {
             customerRepository.deleteById(id);
             return new MessageDTO("Customer removed successfully.");
        }
        throw new ResourceNotFoundException("Customer with id: " + id + " not found.");
    }

}
