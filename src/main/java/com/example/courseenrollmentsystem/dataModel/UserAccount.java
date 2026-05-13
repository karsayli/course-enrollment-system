package com.example.courseenrollmentsystem.dataModel;

import com.example.courseenrollmentsystem.dataModel.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_account")
public class UserAccount {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;   // ADMIN, SUBADMIN

    // Optional links
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean enabled = true;
}

