package com.example.courseenrollmentsystem.service.serviceImplementation;

import com.example.courseenrollmentsystem.dao.CourseDao;
import com.example.courseenrollmentsystem.dao.InstructorDao;
import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dataModel.Instructor;
import com.example.courseenrollmentsystem.dto.CourseDTO;
import com.example.courseenrollmentsystem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseDao dao;
    private final InstructorDao instructorDao;

    @Override
    public CourseDTO create(CourseDTO dto) {
        Course c = new Course();
        c.setCode(dto.getCode());
        c.setTitle(dto.getTitle());
        c.setCapacity(dto.getCapacity());
        c.setSemester(dto.getSemester());
        c.setEctsCredits(dto.getEctsCredits());
        c.setDescription(dto.getDescription());
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());

        if (dto.getInstructorId() != null) {
            Instructor instructor = instructorDao.findById(dto.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
            c.setInstructor(instructor);
        }

        Course saved = dao.save(c);
        return toDTO(saved);
    }

    @Override
    public Optional<CourseDTO> findById(Long id) {
        return dao.findById(id).map(this::toDTO);
    }

    @Override
    public Optional<CourseDTO> findByCode(String code) {
        return dao.findByCode(code).map(this::toDTO);
    }

    @Override
    public List<Course> findAll() {
        return dao.findAll();
    }

    @Override
    public List<CourseDTO> findByInstructor(Long instructorId) {
        return dao.findByInstructor(instructorId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> findFullCourses() {
        return dao.findFullCourses().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CourseDTO update(CourseDTO dto) {
        Course c = dao.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Course not found"));
        c.setCode(dto.getCode());
        c.setTitle(dto.getTitle());
        c.setCapacity(dto.getCapacity());
        c.setSemester(dto.getSemester());
        c.setEctsCredits(dto.getEctsCredits());
        c.setDescription(dto.getDescription());
        c.setUpdatedAt(LocalDateTime.now());

        if (dto.getInstructorId() != null) {
            Instructor instructor = instructorDao.findById(dto.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
            c.setInstructor(instructor);
        }

        return toDTO(dao.save(c));
    }

    @Override
    public void delete(Long id) {
        dao.deleteById(id);
    }

    private CourseDTO toDTO(Course c) {
        List<Long> enrollmentIds = c.getEnrollments().stream().map(Enrollment::getId).toList();
        return CourseDTO.builder()
                .id(c.getId())
                .code(c.getCode())
                .title(c.getTitle())
                .capacity(c.getCapacity())
                .semester(c.getSemester())
                .ectsCredits(c.getEctsCredits())
                .description(c.getDescription())
                .instructorId(c.getInstructor() != null ? c.getInstructor().getId() : null)
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .enrollmentIds(enrollmentIds)
                .build();
    }
}
