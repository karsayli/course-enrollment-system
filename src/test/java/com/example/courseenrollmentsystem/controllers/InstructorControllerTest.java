package com.example.courseenrollmentsystem.controllers;

import com.example.courseenrollmentsystem.controller.InstructorController;
import com.example.courseenrollmentsystem.dto.InstructorDTO;
import com.example.courseenrollmentsystem.service.InstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InstructorControllerTest {

    @Mock
    private InstructorService instructorService;

    @InjectMocks
    private InstructorController instructorController;

    private MockMvc mockMvc;

    private InstructorDTO instructor1;
    private InstructorDTO instructor2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(instructorController).build();

        instructor1 = InstructorDTO.builder()
                .id(1L)
                .fullName("Alice Smith")
                .email("alice@example.com")
                .department("Computer Science")
                .hireDate(LocalDate.of(2020, 1, 15))
                .build();

        instructor2 = InstructorDTO.builder()
                .id(2L)
                .fullName("Bob Johnson")
                .email("bob@example.com")
                .department("Mathematics")
                .hireDate(LocalDate.of(2019, 5, 10))
                .build();
    }

    @Test
    void testListInstructors() throws Exception {
        when(instructorService.findAll()).thenReturn(Arrays.asList(instructor1, instructor2));

        mockMvc.perform(get("/instructors"))
                .andExpect(status().isOk())
                .andExpect(view().name("instructors/list"))
                .andExpect(model().attributeExists("instructors"));

        verify(instructorService, times(1)).findAll();
    }

    @Test
    void testCreateForm() throws Exception {
        mockMvc.perform(get("/instructors/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("instructors/create"))
                .andExpect(model().attributeExists("instructor"));
    }

    @Test
    void testCreateInstructor() throws Exception {
        mockMvc.perform(post("/instructors/create")
                        .param("fullName", "Charlie Brown")
                        .param("email", "charlie@example.com")
                        .param("department", "Physics"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/instructors"));

        verify(instructorService, times(1)).create(any(InstructorDTO.class));
    }

    @Test
    void testEditForm() throws Exception {
        when(instructorService.findById(1L)).thenReturn(Optional.of(instructor1));

        mockMvc.perform(get("/instructors/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("instructors/edit"))
                .andExpect(model().attributeExists("instructor"))
                .andExpect(model().attribute("instructor", instructor1));
    }

    @Test
    void testUpdateInstructor() throws Exception {
        mockMvc.perform(post("/instructors/edit/1")
                        .param("fullName", "Alice Updated")
                        .param("email", "alice.updated@example.com")
                        .param("department", "CS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/instructors"));

        verify(instructorService, times(1)).update(any(InstructorDTO.class));
    }

    @Test
    void testDeleteInstructor() throws Exception {
        mockMvc.perform(get("/instructors/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/instructors"));

        verify(instructorService, times(1)).delete(1L);
    }
}
