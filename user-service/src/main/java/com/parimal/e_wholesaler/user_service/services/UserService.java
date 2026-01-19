package com.parimal.e_wholesaler.user_service.services;

import com.parimal.e_wholesaler.user_service.advices.ApiResponse;
import com.parimal.e_wholesaler.user_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.user_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.user_service.dtos.OwnerRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.OwnerResponseDTO;
import com.parimal.e_wholesaler.user_service.dtos.SignupRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.SignupResponseDTO;
import com.parimal.e_wholesaler.user_service.entities.UserEntity;
import com.parimal.e_wholesaler.user_service.exceptions.MyException;
import com.parimal.e_wholesaler.user_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.user_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.user_service.repositories.UserRepository;
import com.parimal.e_wholesaler.user_service.utils.UserType;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final ShopFeignClient shopFeignClient;
    private final WorkerFeignClient workerFeignClient;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found."));
    }

    public SignupResponseDTO signup(SignupRequestDTO requestDTO) {
        boolean userExists = userRepository.existsByUsername(requestDTO.getUsername());
        if(userExists) {
            throw new ResourceAlreadyExistsException("User with username: " + requestDTO.getUsername() + " already exists.");
        }
        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        UserEntity toSave = modelMapper.map(requestDTO, UserEntity.class);

        if(toSave.getUserType() == UserType.OWNER) {
            OwnerRequestDTO ownerRequestDTO = new OwnerRequestDTO(
                    requestDTO.getName(),
                    requestDTO.getGender(),
                    requestDTO.getMobNo(),
                    requestDTO.getAddress(),
                    requestDTO.getCity(),
                    requestDTO.getState()
            );

            ApiResponse<OwnerResponseDTO> ownerResponse = shopFeignClient.createOwner(ownerRequestDTO);
            if(ownerResponse.getError() != null) throw new MyException(ownerResponse.getError());
        }
        else if(toSave.getUserType() == UserType.WORKER) {
            ApiResponse<Boolean> workerResponse = workerFeignClient.workerExistsByMobNo(toSave.getUsername());
            if(workerResponse.getError() != null) throw new MyException(workerResponse.getError());
            if(!workerResponse.getData()) throw new ResourceNotFoundException("Worker not found.");
        }

        UserEntity saved = userRepository.save(toSave);

        return modelMapper.map(saved, SignupResponseDTO.class);
    }

    public Object findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }
}

