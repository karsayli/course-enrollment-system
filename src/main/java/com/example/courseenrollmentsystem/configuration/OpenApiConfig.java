package com.example.courseenrollmentsystem.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI courseEnrollmentSystemOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local development server");

        Server dockerServer = new Server();
        dockerServer.setUrl("http://localhost:8080");
        dockerServer.setDescription("Docker server");

        Contact contact = new Contact();
        contact.setEmail("example@university.edu");
        contact.setName("Course Enrollment System Team");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Course Enrollment System API")
                .version("1.0.0")
                .contact(contact)
                .description("REST API for Course Enrollment Management System. " +
                        "This API allows you to manage students, courses, instructors, and enrollments.")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, dockerServer));
    }
}

