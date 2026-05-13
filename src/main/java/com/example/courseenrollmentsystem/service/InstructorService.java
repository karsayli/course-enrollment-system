package com.example.courseenrollmentsystem.service;

import com.example.courseenrollmentsystem.dto.InstructorDTO;

import java.util.List;
import java.util.Optional;

public interface InstructorService {

    InstructorDTO create(InstructorDTO dto);

    Optional<InstructorDTO> findById(Long id);

    Optional<InstructorDTO> findByEmail(String email);

    List<InstructorDTO> findAll();

    InstructorDTO update(InstructorDTO dto);

    void delete(Long id);
}