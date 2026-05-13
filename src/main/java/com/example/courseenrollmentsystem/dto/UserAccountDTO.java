package com.example.courseenrollmentsystem.dto;

import com.example.courseenrollmentsystem.dataModel.enums.UserRole;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountDTO {

    private Long id;

    private String username;

    private UserRole role;

    // linked entities represented by IDs
    private Long studentId;
    private Long instructorId;

    private boolean enabled;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
