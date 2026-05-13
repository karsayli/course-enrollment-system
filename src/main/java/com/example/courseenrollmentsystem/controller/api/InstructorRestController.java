package com.example.courseenrollmentsystem.controller.api;

import com.example.courseenrollmentsystem.dto.InstructorDTO;
import com.example.courseenrollmentsystem.service.InstructorService;
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

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
@Tag(name = "Instructors", description = "Instructor management API")
public class InstructorRestController {

    private final InstructorService instructorService;

    @GetMapping
    @Operation(summary = "Get all instructors", description = "Returns a list of all instructors")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        List<InstructorDTO> instructors = instructorService.findAll();
        return ResponseEntity.ok(instructors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get instructor by ID", description = "Returns an instructor by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor found"),
            @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
    public ResponseEntity<InstructorDTO> getInstructorById(
            @Parameter(description = "Instructor ID", required = true) @PathVariable Long id) {
        return instructorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get instructor by email", description = "Returns an instructor by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor found"),
            @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
    public ResponseEntity<InstructorDTO> getInstructorByEmail(
            @Parameter(description = "Instructor email", required = true) @PathVariable String email) {
        return instructorService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new instructor", description = "Creates a new instructor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instructor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<InstructorDTO> createInstructor(
            @Parameter(description = "Instructor data", required = true) @Valid @RequestBody InstructorDTO instructorDTO) {
        InstructorDTO created = instructorService.create(instructorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update instructor", description = "Updates an existing instructor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor updated successfully"),
            @ApiResponse(responseCode = "404", description = "Instructor not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<InstructorDTO> updateInstructor(
            @Parameter(description = "Instructor ID", required = true) @PathVariable Long id,
            @Parameter(description = "Updated instructor data", required = true) @Valid @RequestBody InstructorDTO instructorDTO) {
        instructorDTO.setId(id);
        InstructorDTO updated = instructorService.update(instructorDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete instructor", description = "Deletes an instructor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Instructor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
    public ResponseEntity<Void> deleteInstructor(
            @Parameter(description = "Instructor ID", required = true) @PathVariable Long id) {
        instructorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

