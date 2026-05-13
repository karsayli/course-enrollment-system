package com.example.courseenrollmentsystem.dao;

import com.example.courseenrollmentsystem.dataModel.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    Student save(Student student);

    Optional<Student> findById(Long id);

    Optional<Student> findByEmail(String email);

    List<Student> findAll();

    void deleteById(Long id);
}
