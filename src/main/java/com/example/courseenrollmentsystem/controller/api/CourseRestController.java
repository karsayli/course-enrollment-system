package com.example.courseenrollmentsystem.controller.api;

import com.example.courseenrollmentsystem.dto.CourseDTO;
import com.example.courseenrollmentsystem.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management API")
public class CourseRestController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all courses", description = "Returns a list of all courses")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        // Note: findAll() returns List<Course>, we need to convert manually
        // For now, returning empty list - this should be fixed in service layer
        // or we can create a helper method
        List<CourseDTO> courses = courseService.findAll().stream()
                .map(c -> {
                    CourseDTO dto = new CourseDTO();
                    dto.setId(c.getId());
                    dto.setCode(c.getCode());
                    dto.setTitle(c.getTitle());
                    dto.setCapacity(c.getCapacity());
                    dto.setSemester(c.getSemester());
                    dto.setEctsCredits(c.getEctsCredits());
                    dto.setDescription(c.getDescription());
                    dto.setCreatedAt(c.getCreatedAt());
                    dto.setUpdatedAt(c.getUpdatedAt());
                    if (c.getInstructor() != null) {
                        dto.setInstructorId(c.getInstructor().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Returns a course by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseDTO> getCourseById(
            @Parameter(description = "Course ID", required = true) @PathVariable Long id) {
        return courseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get course by code", description = "Returns a course by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseDTO> getCourseByCode(
            @Parameter(description = "Course code", required = true) @PathVariable String code) {
        return courseService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Get courses by instructor", description = "Returns all courses taught by an instructor")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<CourseDTO>> getCoursesByInstructor(
            @Parameter(description = "Instructor ID", required = true) @PathVariable Long instructorId) {
        List<CourseDTO> courses = courseService.findByInstructor(instructorId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/full")
    @Operation(summary = "Get full courses", description = "Returns courses that have reached capacity (complex JPQL query)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<CourseDTO>> getFullCourses() {
        List<CourseDTO> fullCourses = courseService.findFullCourses();
        return ResponseEntity.ok(fullCourses);
    }

    @PostMapping
    @Operation(summary = "Create a new course", description = "Creates a new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CourseDTO> createCourse(
            @Parameter(description = "Course data", required = true) @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO created = courseService.create(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course", description = "Updates an existing course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CourseDTO> updateCourse(
            @Parameter(description = "Course ID", required = true) @PathVariable Long id,
            @Parameter(description = "Updated course data", required = true) @Valid @RequestBody CourseDTO courseDTO) {
        courseDTO.setId(id);
        CourseDTO updated = courseService.update(courseDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course", description = "Deletes a course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "Course ID", required = true) @PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

