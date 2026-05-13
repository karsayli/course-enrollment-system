package com.example.courseenrollmentsystem.repository;

import com.example.courseenrollmentsystem.dataModel.Instructor;
import com.example.courseenrollmentsystem.dataModel.enums.InstructorRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Optional<Instructor> findByEmail(String email);

    List<Instructor> findByDepartment(String department);

    List<Instructor> findByRank(InstructorRank rank);
}

