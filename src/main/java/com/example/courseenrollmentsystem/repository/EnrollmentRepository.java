package com.example.courseenrollmentsystem.repository;

import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dataModel.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Check if student enrolled in a course
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findAll();

    // List all courses of a student
    List<Enrollment> findByStudentId(Long studentId);

    // List all students enrolled in a course
    List<Enrollment> findByCourseId(Long courseId);

    // Query for complex business requirements
    @Query("""
        SELECT e 
        FROM Enrollment e 
        JOIN FETCH e.course 
        JOIN FETCH e.student 
        WHERE e.status = :status
    """)
    List<Enrollment> findAllWithStatus(@Param("status") EnrollmentStatus status);
}
