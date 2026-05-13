package com.example.courseenrollmentsystem.controller.api;

import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dto.EnrollmentDTO;
import com.example.courseenrollmentsystem.service.EnrollmentService;
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
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Enrollment management API (Many-to-Many: Student-Course association)")
public class EnrollmentRestController {

    private final EnrollmentService enrollmentService;

    @GetMapping
    @Operation(summary = "Get all enrollments", description = "Returns a list of all enrollments")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.findAll();
        List<EnrollmentDTO> enrollmentDTOs = enrollments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(enrollmentDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Returns an enrollment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment found"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(
            @Parameter(description = "Enrollment ID", required = true) @PathVariable Long id) {
        return enrollmentService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get enrollments by student", 
            description = "Returns all enrollments (courses) for a specific student (Many-to-Many association)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStudent(
            @Parameter(description = "Student ID", required = true) @PathVariable Long studentId) {
        List<EnrollmentDTO> enrollments = enrollmentService.findByStudent(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get enrollments by course", 
            description = "Returns all enrollments (students) for a specific course (Many-to-Many association)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourse(
            @Parameter(description = "Course ID", required = true) @PathVariable Long courseId) {
        List<EnrollmentDTO> enrollments = enrollmentService.findByCourse(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/exists/student/{studentId}/course/{courseId}")
    @Operation(summary = "Check if enrollment exists", 
            description = "Checks if a student is enrolled in a specific course")
    @ApiResponse(responseCode = "200", description = "Check completed")
    public ResponseEntity<Boolean> checkEnrollmentExists(
            @Parameter(description = "Student ID", required = true) @PathVariable Long studentId,
            @Parameter(description = "Course ID", required = true) @PathVariable Long courseId) {
        boolean exists = enrollmentService.existsByStudentAndCourse(studentId, courseId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    @Operation(summary = "Create a new enrollment", 
            description = "Creates a new enrollment (enrolls a student in a course - Many-to-Many association)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Enrollment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or capacity reached"),
            @ApiResponse(responseCode = "404", description = "Student or Course not found")
    })
    public ResponseEntity<EnrollmentDTO> createEnrollment(
            @Parameter(description = "Enrollment data", required = true) @Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        try {
            EnrollmentDTO created = enrollmentService.create(enrollmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update enrollment", description = "Updates an existing enrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<EnrollmentDTO> updateEnrollment(
            @Parameter(description = "Enrollment ID", required = true) @PathVariable Long id,
            @Parameter(description = "Updated enrollment data", required = true) @Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        enrollmentDTO.setId(id);
        EnrollmentDTO updated = enrollmentService.update(enrollmentDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete enrollment", 
            description = "Deletes an enrollment (unenrolls a student from a course - Many-to-Many association)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<Void> deleteEnrollment(
            @Parameter(description = "Enrollment ID", required = true) @PathVariable Long id) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EnrollmentDTO toDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent() != null ? enrollment.getStudent().getId() : null);
        dto.setCourseId(enrollment.getCourse() != null ? enrollment.getCourse().getId() : null);
        dto.setEnrolledAt(enrollment.getEnrolledAt());
        dto.setStatus(enrollment.getStatus());
        dto.setDroppedAt(enrollment.getDroppedAt());
        dto.setCompletedAt(enrollment.getCompletedAt());
        dto.setFinalGrade(enrollment.getFinalGrade());
        return dto;
    }
}

