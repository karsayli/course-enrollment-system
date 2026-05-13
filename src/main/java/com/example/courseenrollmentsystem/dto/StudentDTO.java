package com.example.courseenrollmentsystem.dto;

import com.example.courseenrollmentsystem.dataModel.enums.StudentStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {

    private Long id;

    private String fullName;

    private String email;

    private Integer yearOfStudy;

    private LocalDate birthDate;

    private String gender;

    private StudentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Enrollment IDs only
    private List<Long> enrollmentIds;
}
