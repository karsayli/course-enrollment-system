package com.example.courseenrollmentsystem.services;

import com.example.courseenrollmentsystem.dao.InstructorDao;
import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dataModel.Instructor;
import com.example.courseenrollmentsystem.dataModel.enums.InstructorRank;
import com.example.courseenrollmentsystem.dto.InstructorDTO;
import com.example.courseenrollmentsystem.service.serviceImplementation.InstructorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class InstructorServiceTest {

    @Mock
    private InstructorDao dao;

    @InjectMocks
    private InstructorServiceImpl service;

    // Helper to build an Instructor entity
    private Instructor buildInstructor(Long id, String name, String email) {
        Instructor i = new Instructor();
        i.setId(id);
        i.setFullName(name);
        i.setDepartment("Computer Science");
        i.setEmail(email);
        i.setOfficeRoom("B-204");
        i.setHireDate(LocalDate.of(2020, 1, 15));
        i.setRank(InstructorRank.ASSISTANT_PROFESSOR);

        List<Course> courses = new ArrayList<>();
        Course c = new Course();
        c.setId(10L);
        courses.add(c);

        i.setCourses(courses);

        return i;
    }

    // Helper to build an InstructorDTO
    private InstructorDTO buildDTO() {
        return InstructorDTO.builder()
                .id(1L)
                .fullName("Updated Name")
                .department("Mathematics")
                .email("updated@example.com")
                .officeRoom("C-301")
                .hireDate(LocalDate.of(2024, 5, 10))
                .rank(InstructorRank.ASSISTANT_PROFESSOR)
                .courseIds(List.of(10L))
                .build();
    }

    @Test
    void testCreateInstructor() {
        InstructorDTO dto = InstructorDTO.builder()
                .fullName("John Smith")
                .department("Engineering")
                .email("john@example.com")
                .officeRoom("A-101")
                .hireDate(LocalDate.of(2022, 3, 1))
                .rank(InstructorRank.ASSISTANT_PROFESSOR)
                .build();

        Instructor saved = buildInstructor(1L, "John Smith", "john@example.com");

        when(dao.save(any(Instructor.class))).thenReturn(saved);

        InstructorDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("John Smith", result.getFullName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals(InstructorRank.ASSISTANT_PROFESSOR, saved.getRank());  // matches helper

        verify(dao, times(1)).save(any(Instructor.class));
    }

    @Test
    void testFindById() {
        Instructor instructor = buildInstructor(1L, "Jane Doe", "jane@example.com");
        when(dao.findById(1L)).thenReturn(Optional.of(instructor));

        Optional<InstructorDTO> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Jane Doe", result.get().getFullName());
        assertEquals("jane@example.com", result.get().getEmail());
    }

    @Test
    void testFindByEmail() {
        Instructor instructor = buildInstructor(1L, "Alex Ray", "alex@example.com");
        when(dao.findByEmail("alex@example.com")).thenReturn(Optional.of(instructor));

        Optional<InstructorDTO> result = service.findByEmail("alex@example.com");

        assertTrue(result.isPresent());
        assertEquals("Alex Ray", result.get().getFullName());
    }

    @Test
    void testFindAll() {
        List<Instructor> list = Arrays.asList(
                buildInstructor(1L, "A", "a@example.com"),
                buildInstructor(2L, "B", "b@example.com")
        );

        when(dao.findAll()).thenReturn(list);

        List<InstructorDTO> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getFullName());
        assertEquals("B", result.get(1).getFullName());
    }

    @Test
    void testUpdateInstructor() {
        Instructor existing = buildInstructor(1L, "Old Name", "old@example.com");
        InstructorDTO dto = buildDTO();

        when(dao.findById(1L)).thenReturn(Optional.of(existing));
        when(dao.save(existing)).thenReturn(existing);

        InstructorDTO updated = service.update(dto);

        assertEquals("Updated Name", updated.getFullName());
        assertEquals("updated@example.com", updated.getEmail());
        assertEquals("C-301", updated.getOfficeRoom());
        assertEquals(InstructorRank.ASSISTANT_PROFESSOR, updated.getRank());

        verify(dao, times(1)).save(existing);
    }

    @Test
    void testDeleteInstructor() {
        doNothing().when(dao).deleteById(1L);

        service.delete(1L);

        verify(dao, times(1)).deleteById(1L);
    }
}

