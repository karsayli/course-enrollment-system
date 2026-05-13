package com.example.courseenrollmentsystem.service.serviceImplementation;

import com.example.courseenrollmentsystem.dao.CourseDao;
import com.example.courseenrollmentsystem.dao.EnrollmentDao;
import com.example.courseenrollmentsystem.dao.StudentDao;
import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dataModel.Student;
import com.example.courseenrollmentsystem.dto.EnrollmentDTO;
import com.example.courseenrollmentsystem.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentDao dao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    @Override
    public EnrollmentDTO create(EnrollmentDTO dto) {
        Student student = studentDao.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseDao.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Capacity check
        if (course.getEnrollments().size() >= course.getCapacity()) {
            throw new RuntimeException("Course capacity reached");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(dto.getStatus());
        enrollment.setFinalGrade(dto.getFinalGrade());

        Enrollment saved = dao.save(enrollment);
        return toDTO(saved);
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public List<Enrollment> findAll() {
        return dao.findAll();
    }

    @Override
    public List<EnrollmentDTO> findByStudent(Long studentId) {
        return dao.findByStudent(studentId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDTO> findByCourse(Long courseId) {
        return dao.findByCourse(courseId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean existsByStudentAndCourse(Long studentId, Long courseId) {
        return dao.existsByStudentAndCourse(studentId, courseId);
    }

    @Override
    public EnrollmentDTO update(EnrollmentDTO dto) {
        Enrollment e = dao.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (dto.getStatus() != null) e.setStatus(dto.getStatus());
        e.setFinalGrade(dto.getFinalGrade());
        e.setEnrolledAt(dto.getEnrolledAt());
        e.setDroppedAt(dto.getDroppedAt());
        e.setCompletedAt(dto.getCompletedAt());

        return toDTO(dao.save(e));
    }

    @Override
    public void delete(Long id) {
        dao.deleteById(id);
    }

    private EnrollmentDTO toDTO(Enrollment e) {
        return EnrollmentDTO.builder()
                .id(e.getId())
                .studentId(e.getStudent().getId())
                .courseId(e.getCourse().getId())
                .enrolledAt(e.getEnrolledAt())
                .status(e.getStatus())
                .droppedAt(e.getDroppedAt())
                .completedAt(e.getCompletedAt())
                .finalGrade(e.getFinalGrade())
                .build();
    }
}
