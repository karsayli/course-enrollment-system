package com.example.courseenrollmentsystem.repository;

import com.example.courseenrollmentsystem.dataModel.Student;
import com.example.courseenrollmentsystem.dataModel.enums.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    List<Student> findByStatus(StudentStatus status);

    List<Student> findByYearOfStudy(Integer yearOfStudy);
}

