package com.example.courseenrollmentsystem.service;

import com.example.courseenrollmentsystem.dto.UserAccountDTO;

import java.util.List;
import java.util.Optional;


public interface UserAccountService {

    UserAccountDTO create(UserAccountDTO dto, String rawPassword);

    Optional<UserAccountDTO> findById(Long id);

    Optional<UserAccountDTO> findByUsername(String username);

    List<UserAccountDTO> findAll();

    UserAccountDTO update(UserAccountDTO dto);

    void delete(Long id);
}

