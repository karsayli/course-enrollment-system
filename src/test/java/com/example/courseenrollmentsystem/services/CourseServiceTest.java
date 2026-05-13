package com.example.courseenrollmentsystem.services;

import com.example.courseenrollmentsystem.dao.CourseDao;
import com.example.courseenrollmentsystem.dao.InstructorDao;
import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dataModel.Instructor;
import com.example.courseenrollmentsystem.dto.CourseDTO;
import com.example.courseenrollmentsystem.service.serviceImplementation.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourseServiceTest {

    @Mock
    private CourseDao courseDao;

    @Mock
    private InstructorDao instructorDao;

    @InjectMocks
    private CourseServiceImpl service;

    // ---- Helpers ---------------------------------------------------------

    private Instructor buildInstructor(Long id, String name) {
        Instructor i = new Instructor();
        i.setId(id);
        i.setFullName(name);
        i.setEmail(name.toLowerCase() + "@example.com");
        return i;
    }

    private Course buildCourse(Long id, String code) {
        Course c = new Course();
        c.setId(id);
        c.setCode(code);
        c.setTitle("Sample Course");
        c.setCapacity(30);
        c.setSemester("SPRING 2025");
        c.setEctsCredits(6);
        c.setDescription("Testing course");

        c.setCreatedAt(LocalDateTime.now().minusDays(3));
        c.setUpdatedAt(LocalDateTime.now().minusDays(1));

        Enrollment e = new Enrollment();
        e.setId(100L);
        c.setEnrollments(List.of(e));

        return c;
    }

    private CourseDTO buildDTO() {
        return CourseDTO.builder()
                .id(1L)
                .code("CS101")
                .title("Updated Course")
                .capacity(40)
                .semester("FALL 2025")
                .ectsCredits(8)
                .description("Updated Description")
                .instructorId(5L)
                .build();
    }

    // ---- Tests -----------------------------------------------------------

    @Test
    void testCreateCourse() {
        Instructor instructor = buildInstructor(5L, "Dr Smith");

        CourseDTO dto = CourseDTO.builder()
                .code("CS202")
                .title("Data Structures")
                .capacity(50)
                .semester("FALL 2025")
                .ectsCredits(6)
                .description("Core CS course")
                .instructorId(5L)
                .build();

        Course saved = buildCourse(1L, "CS202");
        saved.setInstructor(instructor);

        when(instructorDao.findById(5L)).thenReturn(Optional.of(instructor));
        when(courseDao.save(any(Course.class))).thenReturn(saved);

        CourseDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("CS202", result.getCode());
        assertEquals("Sample Course", result.getTitle());
        assertEquals(5L, result.getInstructorId());

        verify(courseDao, times(1)).save(any(Course.class));
    }

    @Test
    void testFindById() {
        Course course = buildCourse(1L, "CS301");

        when(courseDao.findById(1L)).thenReturn(Optional.of(course));

        Optional<CourseDTO> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("CS301", result.get().getCode());
    }

    @Test
    void testFindByCode() {
        Course course = buildCourse(1L, "ENG101");

        when(courseDao.findByCode("ENG101")).thenReturn(Optional.of(course));

        Optional<CourseDTO> result = service.findByCode("ENG101");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testFindAll() {
        List<Course> list = Arrays.asList(
                buildCourse(1L, "AAA"),
                buildCourse(2L, "BBB")
        );

        when(courseDao.findAll()).thenReturn(list);

        List<Course> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("AAA", result.get(0).getCode());
    }

    @Test
    void testFindByInstructor() {
        List<Course> list = Arrays.asList(
                buildCourse(1L, "CS1"),
                buildCourse(2L, "CS2")
        );

        when(courseDao.findByInstructor(10L)).thenReturn(list);

        List<CourseDTO> result = service.findByInstructor(10L);

        assertEquals(2, result.size());
        assertEquals("CS1", result.get(0).getCode());
        assertEquals("CS2", result.get(1).getCode());
    }

    @Test
    void testUpdateCourse() {
        Course existing = buildCourse(1L, "OLD101");
        Instructor instructor = buildInstructor(5L, "Dr Updated");

        CourseDTO dto = buildDTO(); // contains updated values

        when(courseDao.findById(1L)).thenReturn(Optional.of(existing));
        when(instructorDao.findById(5L)).thenReturn(Optional.of(instructor));
        when(courseDao.save(existing)).thenReturn(existing);

        CourseDTO updated = service.update(dto);

        assertEquals("CS101", updated.getCode());
        assertEquals("Updated Course", updated.getTitle());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(5L, updated.getInstructorId());
    }

    @Test
    void testDeleteCourse() {
        doNothing().when(courseDao).deleteById(1L);

        service.delete(1L);

        verify(courseDao, times(1)).deleteById(1L);
    }
}

