package com.example.courseenrollmentsystem.dao.daoImplementation;

import com.example.courseenrollmentsystem.dao.InstructorDao;
import com.example.courseenrollmentsystem.dataModel.Instructor;
import com.example.courseenrollmentsystem.repository.InstructorRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InstructorDaoImpl implements InstructorDao {

    private final InstructorRepository repo;

    public InstructorDaoImpl(InstructorRepository repo) {
        this.repo = repo;
    }

    @Override
    public Instructor save(Instructor instructor) {
        return repo.save(instructor);
    }

    @Override
    public Optional<Instructor> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Instructor> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public List<Instructor> findAll() {
        return repo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}

