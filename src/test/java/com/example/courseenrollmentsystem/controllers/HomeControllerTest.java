package com.example.courseenrollmentsystem.controllers;
import com.example.courseenrollmentsystem.controller.HomeController;
import com.example.courseenrollmentsystem.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HomeControllerTest {

    @Mock
    private UserAccountService userService;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Add a simple view resolver to prevent circular view path
        ViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(homeController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testHomePageAsGuest() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("username", "Guest"))
                .andExpect(model().attribute("dashboardMessage", "Please login"));
    }
}
