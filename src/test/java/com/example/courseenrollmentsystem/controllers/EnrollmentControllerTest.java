package com.example.courseenrollmentsystem.controllers;

import com.example.courseenrollmentsystem.controller.EnrollmentController;
import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dto.EnrollmentDTO;
import com.example.courseenrollmentsystem.dto.StudentDTO;
import com.example.courseenrollmentsystem.service.CourseService;
import com.example.courseenrollmentsystem.service.EnrollmentService;
import com.example.courseenrollmentsystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private EnrollmentController enrollmentController;

    private MockMvc mockMvc;

    private StudentDTO student1;
    private Course course1;
    private EnrollmentDTO enrollment1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();

        student1 = new StudentDTO();
        student1.setId(1L);
        student1.setFullName("John Doe");
        student1.setEmail("john@example.com");

        course1 = new Course();
        course1.setId(1L);
        course1.setCode("CS101");
        course1.setTitle("Intro to CS");
        course1.setCapacity(50);

        enrollment1 = new EnrollmentDTO();
        enrollment1.setId(1L);
        enrollment1.setStudentId(student1.getId());
        enrollment1.setCourseId(course1.getId());
        enrollment1.setEnrolledAt(LocalDateTime.now());
    }

    @Test
    void testCreateForm() throws Exception {
        when(studentService.findAll()).thenReturn(Collections.singletonList(student1));
        when(courseService.findAll()).thenReturn(Collections.singletonList(course1));

        mockMvc.perform(get("/enrollments/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("enrollments/create"))
                .andExpect(model().attributeExists("enrollment"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    void testCreateEnrollmentSuccess() throws Exception {
        mockMvc.perform(post("/enrollments/create")
                        .param("studentId", "1")
                        .param("courseId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/enrollments"));

        verify(enrollmentService, times(1)).create(any(EnrollmentDTO.class));
    }

    @Test
    void testCreateEnrollmentFailure() throws Exception {
        when(studentService.findAll()).thenReturn(Collections.singletonList(student1));
        when(courseService.findAll()).thenReturn(Collections.singletonList(course1));

        doThrow(new RuntimeException("Course capacity reached"))
                .when(enrollmentService).create(any(EnrollmentDTO.class));

        mockMvc.perform(post("/enrollments/create")
                        .param("studentId", "1")
                        .param("courseId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("enrollments/create"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("enrollment"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    void testListEnrollments() throws Exception {
        when(enrollmentService.findAll()).thenReturn(Arrays.asList(
                new Enrollment(1L, null, null, LocalDateTime.now(), null, null, null, null)
        ));

        mockMvc.perform(get("/enrollments"))
                .andExpect(status().isOk())
                .andExpect(view().name("enrollments/list"))
                .andExpect(model().attributeExists("enrollments"));

        verify(enrollmentService, times(1)).findAll();
    }

    @Test
    void testEditEnrollmentForm() throws Exception {
        when(enrollmentService.findById(1L)).thenReturn(Optional.of(
                new Enrollment(1L, null, null, LocalDateTime.now(), null, null, null, null)
        ));
        when(studentService.findAll()).thenReturn(Collections.singletonList(student1));
        when(courseService.findAll()).thenReturn(Collections.singletonList(course1));

        mockMvc.perform(get("/enrollments/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("enrollments/edit"))
                .andExpect(model().attributeExists("enrollment"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    void testUpdateEnrollmentSuccess() throws Exception {
        mockMvc.perform(post("/enrollments/edit/1")
                        .param("studentId", "1")
                        .param("courseId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/enrollments"));

        verify(enrollmentService, times(1)).update(any(EnrollmentDTO.class));
    }

    @Test
    void testUpdateEnrollmentFailure() throws Exception {
        doThrow(new RuntimeException("Some error"))
                .when(enrollmentService).update(any(EnrollmentDTO.class));

        when(enrollmentService.findById(1L)).thenReturn(Optional.of(
                new Enrollment(1L, null, null, LocalDateTime.now(), null, null, null, null)
        ));
        when(studentService.findAll()).thenReturn(Collections.singletonList(student1));
        when(courseService.findAll()).thenReturn(Collections.singletonList(course1));

        mockMvc.perform(post("/enrollments/edit/1")
                        .param("studentId", "1")
                        .param("courseId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("enrollments/edit"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("enrollment"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    void testDeleteEnrollment() throws Exception {
        mockMvc.perform(get("/enrollments/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/enrollments"));

        verify(enrollmentService, times(1)).delete(1L);
    }
}
