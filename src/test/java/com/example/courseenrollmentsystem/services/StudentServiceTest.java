package com.example.courseenrollmentsystem.services;


import com.example.courseenrollmentsystem.dao.StudentDao;
import com.example.courseenrollmentsystem.dataModel.Student;
import com.example.courseenrollmentsystem.dataModel.enums.StudentStatus;
import com.example.courseenrollmentsystem.dto.StudentDTO;
import com.example.courseenrollmentsystem.service.serviceImplementation.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private StudentServiceImpl studentService;

    // Helper method
    private Student buildStudent(Long id, String name, String email) {
        return new Student(
                id,
                name,
                email,
                2,
                LocalDate.of(2000, 1, 1),
                "Male",
                StudentStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>()
        );
    }

    @Test
    void testCreateStudent() {
        StudentDTO dto = new StudentDTO();
        dto.setFullName("John Doe");
        dto.setEmail("john@example.com");

        Student saved = buildStudent(1L, "John Doe", "john@example.com");

        when(studentDao.save(any(Student.class))).thenReturn(saved);

        StudentDTO result = studentService.create(dto);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("john@example.com", result.getEmail());

        verify(studentDao, times(1)).save(any(Student.class));
    }

    @Test
    void testFindById() {
        Student student = buildStudent(1L, "Jane Doe", "jane@example.com");

        when(studentDao.findById(1L)).thenReturn(Optional.of(student));

        Optional<StudentDTO> found = studentService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Jane Doe", found.get().getFullName());
    }

    @Test
    void testUpdateStudent() {
        StudentDTO dto = new StudentDTO();
        dto.setId(1L);
        dto.setFullName("Updated Name");
        dto.setEmail("updated@example.com");

        Student existing = buildStudent(1L, "Old Name", "old@example.com");

        when(studentDao.findById(1L)).thenReturn(Optional.of(existing));
        when(studentDao.save(any(Student.class))).thenReturn(existing);

        StudentDTO result = studentService.update(dto);

        assertEquals("Updated Name", result.getFullName());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentDao).deleteById(1L);

        studentService.delete(1L);

        verify(studentDao, times(1)).deleteById(1L);   // ← NOW WORKS
    }

    @Test
    void testListStudents() {
        List<Student> students = Arrays.asList(
                buildStudent(1L, "Alice", "alice@example.com"),
                buildStudent(2L, "Bob", "bob@example.com")
        );

        when(studentDao.findAll()).thenReturn(students);

        List<StudentDTO> list = studentService.findAll();

        assertEquals(2, list.size());
        assertEquals("Alice", list.get(0).getFullName());
        assertEquals("Bob", list.get(1).getFullName());
    }
}
