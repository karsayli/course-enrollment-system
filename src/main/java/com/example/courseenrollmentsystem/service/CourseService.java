package com.example.courseenrollmentsystem.service;

import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dto.CourseDTO;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    CourseDTO create(CourseDTO dto);

    Optional<CourseDTO> findById(Long id);

    Optional<CourseDTO> findByCode(String code);

    List<Course> findAll();

    List<CourseDTO> findByInstructor(Long instructorId);

    List<CourseDTO> findFullCourses(); // Complex query: courses that reached capacity

    CourseDTO update(CourseDTO dto);

    void delete(Long id);
}
