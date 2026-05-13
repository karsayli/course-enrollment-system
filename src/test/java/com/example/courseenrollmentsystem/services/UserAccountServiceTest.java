package com.example.courseenrollmentsystem.services;

import com.example.courseenrollmentsystem.dao.InstructorDao;
import com.example.courseenrollmentsystem.dao.StudentDao;
import com.example.courseenrollmentsystem.dao.UserAccountDao;
import com.example.courseenrollmentsystem.dataModel.Instructor;
import com.example.courseenrollmentsystem.dataModel.Student;
import com.example.courseenrollmentsystem.dataModel.UserAccount;
import com.example.courseenrollmentsystem.dataModel.enums.UserRole;
import com.example.courseenrollmentsystem.dto.UserAccountDTO;
import com.example.courseenrollmentsystem.service.serviceImplementation.UserAccountServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserAccountServiceTest {

    private UserAccountDao dao;
    private StudentDao studentDao;
    private InstructorDao instructorDao;
    private UserAccountServiceImpl service;

    @BeforeEach
    void setup() {
        dao = mock(UserAccountDao.class);
        studentDao = mock(StudentDao.class);
        instructorDao = mock(InstructorDao.class);
        service = new UserAccountServiceImpl(dao, studentDao, instructorDao);
    }

    /* -------------------------------------------------------------------------
       CREATE
    ------------------------------------------------------------------------- */

    @Test
    void testCreateUserWithStudent() {
        Student student = new Student();
        student.setId(1L);

        UserAccountDTO dto = UserAccountDTO.builder()
                .username("john@example.com")
                .role(UserRole.ADMIN)
                .studentId(1L)
                .build();

        UserAccount saved = new UserAccount();
        saved.setId(10L);
        saved.setUsername("john@example.com");
        saved.setRole(UserRole.ADMIN);
        saved.setStudent(student);
        saved.setCreatedAt(LocalDateTime.now());
        saved.setUpdatedAt(LocalDateTime.now());

        when(studentDao.findById(1L)).thenReturn(Optional.of(student));
        when(dao.save(any(UserAccount.class))).thenReturn(saved);

        UserAccountDTO result = service.create(dto, "password123");

        assertNotNull(result.getId());
        assertEquals("john@example.com", result.getUsername());
        assertEquals(1L, result.getStudentId());
        assertEquals(UserRole.ADMIN, result.getRole());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(dao).save(any(UserAccount.class));
    }

    @Test
    void testCreateUserWithInstructor() {
        Instructor instructor = new Instructor();
        instructor.setId(2L);

        UserAccountDTO dto = UserAccountDTO.builder()
                .username("prof@example.com")
                .role(UserRole.SUBADMIN)
                .instructorId(2L)
                .build();

        UserAccount saved = new UserAccount();
        saved.setId(20L);
        saved.setUsername("prof@example.com");
        saved.setRole(UserRole.SUBADMIN);
        saved.setInstructor(instructor);
        saved.setCreatedAt(LocalDateTime.now());
        saved.setUpdatedAt(LocalDateTime.now());

        when(instructorDao.findById(2L)).thenReturn(Optional.of(instructor));
        when(dao.save(any(UserAccount.class))).thenReturn(saved);

        UserAccountDTO result = service.create(dto, "pass123");

        assertEquals("prof@example.com", result.getUsername());
        assertEquals(2L, result.getInstructorId());
        assertEquals(UserRole.SUBADMIN, result.getRole());
    }

    @Test
    void testCreateUserFailsWhenStudentNotFound() {
        UserAccountDTO dto = UserAccountDTO.builder()
                .username("john@example.com")
                .role(UserRole.ADMIN)
                .studentId(999L)
                .build();

        when(studentDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto, "pwd"));
    }

    @Test
    void testCreateUserFailsWhenInstructorNotFound() {
        UserAccountDTO dto = UserAccountDTO.builder()
                .username("prof@example.com")
                .role(UserRole.SUBADMIN)
                .instructorId(999L)
                .build();

        when(instructorDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto, "pwd"));
    }

    /* -------------------------------------------------------------------------
       FINDERS
    ------------------------------------------------------------------------- */

    @Test
    void testFindById() {
        UserAccount user = new UserAccount();
        user.setId(1L);
        user.setUsername("test@example.com");
        when(dao.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserAccountDTO> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getUsername());
    }

    @Test
    void testFindByUsername() {
        UserAccount user = new UserAccount();
        user.setId(1L);
        user.setUsername("test@example.com");
        when(dao.findByUsername("test@example.com")).thenReturn(Optional.of(user));

        Optional<UserAccountDTO> result = service.findByUsername("test@example.com");
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testFindAll() {
        UserAccount u1 = new UserAccount(); u1.setId(1L); u1.setUsername("a@example.com");
        UserAccount u2 = new UserAccount(); u2.setId(2L); u2.setUsername("b@example.com");

        when(dao.findAll()).thenReturn(List.of(u1, u2));

        List<UserAccountDTO> list = service.findAll();
        assertEquals(2, list.size());
    }

    /* -------------------------------------------------------------------------
       UPDATE
    ------------------------------------------------------------------------- */

    @Test
    void testUpdateUserSuccessfully() {
        UserAccount existing = new UserAccount();
        existing.setId(1L);
        existing.setUsername("old@example.com");
        existing.setRole(UserRole.ADMIN);
        existing.setEnabled(true);

        UserAccountDTO dto = UserAccountDTO.builder()
                .id(1L)
                .username("new@example.com")
                .role(UserRole.SUBADMIN)
                .enabled(false)
                .build();

        when(dao.findById(1L)).thenReturn(Optional.of(existing));
        when(dao.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserAccountDTO updated = service.update(dto);

        assertEquals("new@example.com", updated.getUsername());
        assertEquals(UserRole.SUBADMIN, updated.getRole());
        assertFalse(updated.isEnabled());
    }

    @Test
    void testUpdateFailsWhenUserNotFound() {
        UserAccountDTO dto = UserAccountDTO.builder().id(999L).build();
        when(dao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(dto));
    }

    /* -------------------------------------------------------------------------
       DELETE
    ------------------------------------------------------------------------- */

    @Test
    void testDelete() {
        service.delete(1L);
        verify(dao).deleteById(1L);
    }
}

