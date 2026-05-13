package com.example.courseenrollmentsystem.service;

import com.example.courseenrollmentsystem.dto.StudentDTO;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    StudentDTO create(StudentDTO dto);

    Optional<StudentDTO> findById(Long id);

    Optional<StudentDTO> findByEmail(String email);

    List<StudentDTO> findAll();

    StudentDTO update(StudentDTO dto);

    void delete(Long id);
}