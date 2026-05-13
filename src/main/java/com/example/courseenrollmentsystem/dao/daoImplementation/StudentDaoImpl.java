package com.example.courseenrollmentsystem.dao.daoImplementation;


import com.example.courseenrollmentsystem.dao.StudentDao;
import com.example.courseenrollmentsystem.dataModel.Student;
import com.example.courseenrollmentsystem.repository.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentDaoImpl implements StudentDao {

    private final StudentRepository repo;

    public StudentDaoImpl(StudentRepository repo) {
        this.repo = repo;
    }

    @Override
    public Student save(Student student) {
        return repo.save(student);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public List<Student> findAll() {
        return repo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}

