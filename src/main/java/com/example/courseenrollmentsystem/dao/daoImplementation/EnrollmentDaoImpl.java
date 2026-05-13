package com.example.courseenrollmentsystem.dao.daoImplementation;

import com.example.courseenrollmentsystem.dao.EnrollmentDao;
import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.repository.EnrollmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EnrollmentDaoImpl implements EnrollmentDao {

    private final EnrollmentRepository repo;

    public EnrollmentDaoImpl(EnrollmentRepository repo) {
        this.repo = repo;
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        return repo.save(enrollment);
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Enrollment> findAll() {
        return repo.findAll();
    }

    @Override
    public boolean existsByStudentAndCourse(Long studentId, Long courseId) {
        return repo.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public List<Enrollment> findByStudent(Long studentId) {
        return repo.findByStudentId(studentId);
    }

    @Override
    public List<Enrollment> findByCourse(Long courseId) {
        return repo.findByCourseId(courseId);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
