package com.example.courseenrollmentsystem.dto;

import com.example.courseenrollmentsystem.dataModel.enums.EnrollmentStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentDTO {

    private Long id;

    private Long studentId;
    private Long courseId;

    private LocalDateTime enrolledAt;

    private EnrollmentStatus status;

    private LocalDateTime droppedAt;
    private LocalDateTime completedAt;

    private String finalGrade;
}
