package com.example.courseenrollmentsystem.dao;

import com.example.courseenrollmentsystem.dataModel.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {

    Course save(Course course);

    Optional<Course> findById(Long id);

    Optional<Course> findByCode(String code);

    List<Course> findAll();

    List<Course> findByInstructor(Long instructorId);

    List<Course> findFullCourses(); // Complex query: courses that reached capacity

    void deleteById(Long id);
}

