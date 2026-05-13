package com.example.courseenrollmentsystem.dao.daoImplementation;

import com.example.courseenrollmentsystem.dao.CourseDao;
import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.repository.CourseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseDaoImpl implements CourseDao {

    private final CourseRepository repo;

    public CourseDaoImpl(CourseRepository repo) {
        this.repo = repo;
    }

    @Override
    public Course save(Course course) {
        return repo.save(course);
    }

    @Override
    public Optional<Course> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Course> findByCode(String code) {
        return repo.findByCode(code);
    }

    @Override
    public List<Course> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Course> findByInstructor(Long instructorId) {
        return repo.findByInstructorId(instructorId);
    }

    @Override
    public List<Course> findFullCourses() {
        return repo.findFullCourses();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}

