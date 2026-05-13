package com.example.courseenrollmentsystem.dao;

import com.example.courseenrollmentsystem.dataModel.Instructor;

import java.util.List;
import java.util.Optional;

public interface InstructorDao {

    Instructor save(Instructor instructor);

    Optional<Instructor> findById(Long id);

    Optional<Instructor> findByEmail(String email);

    List<Instructor> findAll();

    void deleteById(Long id);
}

