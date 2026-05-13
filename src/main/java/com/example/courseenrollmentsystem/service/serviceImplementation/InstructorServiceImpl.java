package com.example.courseenrollmentsystem.service.serviceImplementation;

import com.example.courseenrollmentsystem.dao.InstructorDao;
import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dataModel.Instructor;
import com.example.courseenrollmentsystem.dto.InstructorDTO;
import com.example.courseenrollmentsystem.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorDao dao;

    @Override
    public InstructorDTO create(InstructorDTO dto) {
        Instructor i = new Instructor();
        i.setFullName(dto.getFullName());
        i.setDepartment(dto.getDepartment());
        i.setEmail(dto.getEmail());
        i.setOfficeRoom(dto.getOfficeRoom());
        i.setHireDate(dto.getHireDate());
        i.setRank(dto.getRank());

        Instructor saved = dao.save(i);
        return toDTO(saved);
    }

    @Override
    public Optional<InstructorDTO> findById(Long id) {
        return dao.findById(id).map(this::toDTO);
    }

    @Override
    public Optional<InstructorDTO> findByEmail(String email) {
        return dao.findByEmail(email).map(this::toDTO);
    }

    @Override
    public List<InstructorDTO> findAll() {
        return dao.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public InstructorDTO update(InstructorDTO dto) {
        Instructor i = dao.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Instructor not found"));
        i.setFullName(dto.getFullName());
        i.setDepartment(dto.getDepartment());
        i.setEmail(dto.getEmail());
        i.setOfficeRoom(dto.getOfficeRoom());
        i.setHireDate(dto.getHireDate());
        i.setRank(dto.getRank());

        return toDTO(dao.save(i));
    }

    @Override
    public void delete(Long id) {
        dao.deleteById(id);
    }

    private InstructorDTO toDTO(Instructor i) {
        List<Long> courseIds = i.getCourses().stream().map(Course::getId).toList();
        return InstructorDTO.builder()
                .id(i.getId())
                .fullName(i.getFullName())
                .department(i.getDepartment())
                .email(i.getEmail())
                .officeRoom(i.getOfficeRoom())
                .hireDate(i.getHireDate())
                .rank(i.getRank())
                .courseIds(courseIds)
                .build();
    }
}
