package com.example.courseenrollmentsystem.dataModel;

import com.example.courseenrollmentsystem.dataModel.enums.InstructorRank;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Instructor {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String department;

    @Column(unique = true)
    private String email;

    private String officeRoom;

    private LocalDate hireDate;

    @Enumerated(EnumType.STRING)
    private InstructorRank rank;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses = new ArrayList<>();
}

