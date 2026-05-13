package com.example.courseenrollmentsystem.dto;

import com.example.courseenrollmentsystem.dataModel.enums.InstructorRank;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorDTO {

    private Long id;

    private String fullName;
    private String department;

    private String email;

    private String officeRoom;

    private LocalDate hireDate;

    private InstructorRank rank;

    // Course IDs only → no entity lists
    private List<Long> courseIds;
}
