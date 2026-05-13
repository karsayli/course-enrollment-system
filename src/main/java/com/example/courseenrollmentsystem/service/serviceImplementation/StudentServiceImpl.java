package com.example.courseenrollmentsystem.service.serviceImplementation;

import com.example.courseenrollmentsystem.dao.StudentDao;
import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dataModel.Student;
import com.example.courseenrollmentsystem.dto.StudentDTO;
import com.example.courseenrollmentsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentDao dao;

    @Override
    public StudentDTO create(StudentDTO dto) {
        Student student = new Student();
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setYearOfStudy(dto.getYearOfStudy());
        student.setBirthDate(dto.getBirthDate());
        student.setGender(dto.getGender());
        student.setStatus(dto.getStatus());
        student.setCreatedAt(java.time.LocalDateTime.now());
        student.setUpdatedAt(java.time.LocalDateTime.now());

        Student saved = dao.save(student);
        return toDTO(saved);
    }

    @Override
    public Optional<StudentDTO> findById(Long id) {
        return dao.findById(id).map(this::toDTO);
    }

    @Override
    public Optional<StudentDTO> findByEmail(String email) {
        return dao.findByEmail(email).map(this::toDTO);
    }

    @Override
    public List<StudentDTO> findAll() {
        return dao.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public StudentDTO update(StudentDTO dto) {
        Student student = dao.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setYearOfStudy(dto.getYearOfStudy());
        student.setBirthDate(dto.getBirthDate());
        student.setGender(dto.getGender());
        student.setStatus(dto.getStatus());
        student.setUpdatedAt(java.time.LocalDateTime.now());

        Student saved = dao.save(student);
        return toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        dao.deleteById(id);
    }

    private StudentDTO toDTO(Student s) {
        List<Long> enrollmentIds = s.getEnrollments().stream()
                .map(Enrollment::getId)
                .toList();

        return StudentDTO.builder()
                .id(s.getId())
                .fullName(s.getFullName())
                .email(s.getEmail())
                .yearOfStudy(s.getYearOfStudy())
                .birthDate(s.getBirthDate())
                .gender(s.getGender())
                .status(s.getStatus())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .enrollmentIds(enrollmentIds)
                .build();
    }
}
