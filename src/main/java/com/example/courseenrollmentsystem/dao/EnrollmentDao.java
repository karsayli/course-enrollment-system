package com.example.courseenrollmentsystem.dao;

import com.example.courseenrollmentsystem.dataModel.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentDao {

    Enrollment save(Enrollment enrollment);

    Optional<Enrollment> findById(Long id);

    List<Enrollment> findAll();

    boolean existsByStudentAndCourse(Long studentId, Long courseId);

    List<Enrollment> findByStudent(Long studentId);

    List<Enrollment> findByCourse(Long courseId);

    void deleteById(Long id);
}

