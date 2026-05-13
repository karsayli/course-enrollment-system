package com.example.courseenrollmentsystem.repository;

import com.example.courseenrollmentsystem.dataModel.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String code);

    List<Course> findBySemester(String semester);

    List<Course> findByInstructorId(Long instructorId);

    @Query("""
        SELECT c 
        FROM Course c 
        WHERE SIZE(c.enrollments) >= c.capacity
    """)
    List<Course> findFullCourses();
}

