package com.example.courseenrollmentsystem.service.serviceImplementation;

import com.example.courseenrollmentsystem.dao.InstructorDao;
import com.example.courseenrollmentsystem.dao.StudentDao;
import com.example.courseenrollmentsystem.dao.UserAccountDao;
import com.example.courseenrollmentsystem.dataModel.Instructor;
import com.example.courseenrollmentsystem.dataModel.Student;
import com.example.courseenrollmentsystem.dataModel.UserAccount;
import com.example.courseenrollmentsystem.service.UserAccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import com.example.courseenrollmentsystem.dto.UserAccountDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountDao dao;
    private final StudentDao studentDao;
    private final InstructorDao instructorDao;


    @Override
    public UserAccountDTO create(UserAccountDTO dto, String rawPassword) {
        UserAccount user = new UserAccount();
        user.setUsername(dto.getUsername());
        // Ideally hash the password here
        user.setPasswordHash(rawPassword);
        user.setRole(dto.getRole());
        // Set student if studentId is provided
        if (dto.getStudentId() != null) {
            Student student = studentDao.findById(dto.getStudentId())
                    .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + dto.getStudentId()));
            user.setStudent(student);
        }
        // Set instructor if instructorId is provided
        if (dto.getInstructorId() != null) {
            Instructor instructor = instructorDao.findById(dto.getInstructorId())
                    .orElseThrow(() -> new EntityNotFoundException("Instructor not found with id: " + dto.getInstructorId()));
            user.setInstructor(instructor);
        }
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        UserAccount saved = dao.save(user);
        dto.setId(saved.getId());
        dto.setCreatedAt(saved.getCreatedAt());
        dto.setUpdatedAt(saved.getUpdatedAt());
        return dto;
    }

    @Override
    public Optional<UserAccountDTO> findById(Long id) {
        return dao.findById(id).map(this::toDTO);
    }

    @Override
    public Optional<UserAccountDTO> findByUsername(String username) {
        return dao.findByUsername(username).map(this::toDTO);
    }

    @Override
    public List<UserAccountDTO> findAll() {
        return dao.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserAccountDTO update(UserAccountDTO dto) {
        Optional<UserAccount> existing = dao.findById(dto.getId());
        if (existing.isEmpty()) throw new RuntimeException("User not found");

        UserAccount user = existing.get();
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        user.setEnabled(dto.isEnabled());
        user.setUpdatedAt(LocalDateTime.now());

        UserAccount saved = dao.save(user);
        return toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        dao.deleteById(id);
    }

    private UserAccountDTO toDTO(UserAccount user) {
        return UserAccountDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .studentId(user.getStudent() != null ? user.getStudent().getId() : null)
                .instructorId(user.getInstructor() != null ? user.getInstructor().getId() : null)
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
