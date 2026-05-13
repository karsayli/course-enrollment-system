package com.example.courseenrollmentsystem.controllers;


import com.example.courseenrollmentsystem.controller.StudentController;
import com.example.courseenrollmentsystem.dto.StudentDTO;
import com.example.courseenrollmentsystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    private StudentDTO student1;
    private StudentDTO student2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configure simple view resolver to prevent circular view path errors
        ViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(studentController)
                .setViewResolvers(viewResolver)
                .build();

        student1 = new StudentDTO();
        student1.setId(1L);
        student1.setFullName("John Doe");
        student1.setEmail("john@example.com");

        student2 = new StudentDTO();
        student2.setId(2L);
        student2.setFullName("Jane Doe");
        student2.setEmail("jane@example.com");
    }

    @Test
    void testListStudents() throws Exception {
        when(studentService.findAll()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/list"))
                .andExpect(model().attributeExists("students"));

        verify(studentService, times(2)).findAll(); // once for println, once for model
    }

    @Test
    void testCreateForm() throws Exception {
        mockMvc.perform(get("/students/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/create"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    void testCreateStudent() throws Exception {
        mockMvc.perform(post("/students/create")
                        .param("fullName", "John Doe")
                        .param("email", "john@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).create(any(StudentDTO.class));
    }

    @Test
    void testEditForm() throws Exception {
        when(studentService.findById(1L)).thenReturn(Optional.of(student1));

        mockMvc.perform(get("/students/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/edit"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("student", student1));
    }

    @Test
    void testUpdateStudent() throws Exception {
        mockMvc.perform(post("/students/edit/1")
                        .param("fullName", "John Updated")
                        .param("email", "john.updated@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).update(any(StudentDTO.class));
    }

    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(get("/students/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).delete(1L);
    }
}
