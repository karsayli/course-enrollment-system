package com.example.courseenrollmentsystem.controllers;

import com.example.courseenrollmentsystem.controller.CourseController;
import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dto.CourseDTO;
import com.example.courseenrollmentsystem.dto.InstructorDTO;
import com.example.courseenrollmentsystem.service.CourseService;
import com.example.courseenrollmentsystem.service.InstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private InstructorService instructorService;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;

    private CourseDTO course1;
    private InstructorDTO instructor1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // initialize @Mock and @InjectMocks
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();

        instructor1 = InstructorDTO.builder()
                .id(1L)
                .fullName("Alice Smith")
                .email("alice@example.com")
                .department("CS")
                .hireDate(LocalDate.of(2020, 1, 15))
                .build();

        course1 = CourseDTO.builder()
                .id(1L)
                .code("CS101")
                .title("Intro to CS")
                .capacity(50)
                .semester("Spring 2025")
                .ectsCredits(5)
                .description("Basic CS course")
                .instructorId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testListCourses() throws Exception {
        when(courseService.findAll()).thenReturn(Arrays.asList(
                new Course(1L, "CS101", "Intro to CS", 50, "Spring 2025", 5, "Basic CS course",
                        LocalDateTime.now(), LocalDateTime.now(), null, null),
                new Course(2L, "MATH101", "Calculus I", 40, "Spring 2025", 6, "Intro to Calculus",
                        LocalDateTime.now(), LocalDateTime.now(), null, null)
        ));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/list"))
                .andExpect(model().attributeExists("courses"));

        verify(courseService, times(1)).findAll();
    }

    @Test
    void testCreateForm() throws Exception {
        when(instructorService.findAll()).thenReturn(Collections.singletonList(instructor1));

        mockMvc.perform(get("/courses/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/create"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("instructors"));
    }

    @Test
    void testCreateCourse() throws Exception {
        mockMvc.perform(post("/courses/create")
                        .param("code", "CS101")
                        .param("title", "Intro to CS")
                        .param("capacity", "50")
                        .param("semester", "Spring 2025")
                        .param("ectsCredits", "5")
                        .param("description", "Basic CS course")
                        .param("instructorId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).create(any(CourseDTO.class));
    }

    @Test
    void testEditForm() throws Exception {
        when(courseService.findById(1L)).thenReturn(Optional.of(course1));
        when(instructorService.findAll()).thenReturn(Collections.singletonList(instructor1));

        mockMvc.perform(get("/courses/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/edit"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("instructors"))
                .andExpect(model().attribute("course", course1));
    }

    @Test
    void testUpdateCourse() throws Exception {
        mockMvc.perform(post("/courses/edit/1")
                        .param("code", "CS101")
                        .param("title", "Intro to CS Updated")
                        .param("capacity", "50")
                        .param("semester", "Spring 2025")
                        .param("ectsCredits", "5")
                        .param("description", "Updated description")
                        .param("instructorId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).update(any(CourseDTO.class));
    }

    @Test
    void testDeleteCourse() throws Exception {
        mockMvc.perform(get("/courses/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).delete(1L);
    }
}
