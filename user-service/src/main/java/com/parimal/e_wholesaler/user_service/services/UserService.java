package com.parimal.e_wholesaler.user_service.services;

import com.parimal.e_wholesaler.user_service.dtos.SignupRequestDTO;
import com.parimal.e_wholesaler.user_service.dtos.SignupResponseDTO;
import com.parimal.e_wholesaler.user_service.entities.UserEntity;
import com.parimal.e_wholesaler.user_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.user_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


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
        UserEntity saved = userRepository.save(toSave);
        return modelMapper.map(saved, SignupResponseDTO.class);
    }

}

