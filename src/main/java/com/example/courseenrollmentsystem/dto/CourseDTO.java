package com.example.courseenrollmentsystem.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO {

    private Long id;

    private String code;

    private String title;

    private Integer capacity;

    private String semester;

    private Integer ectsCredits;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long instructorId;

    // Enrollment IDs only
    private List<Long> enrollmentIds;
}
