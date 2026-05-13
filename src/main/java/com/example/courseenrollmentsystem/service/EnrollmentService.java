package com.example.courseenrollmentsystem.service;

import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dto.EnrollmentDTO;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {

    EnrollmentDTO create(EnrollmentDTO dto);

    Optional<Enrollment> findById(Long id);

    List<Enrollment> findAll();

    List<EnrollmentDTO> findByStudent(Long studentId);

    List<EnrollmentDTO> findByCourse(Long courseId);

    boolean existsByStudentAndCourse(Long studentId, Long courseId);

    EnrollmentDTO update(EnrollmentDTO dto);

    void delete(Long id);
}
