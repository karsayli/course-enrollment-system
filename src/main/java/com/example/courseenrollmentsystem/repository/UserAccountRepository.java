package com.example.courseenrollmentsystem.repository;

import com.example.courseenrollmentsystem.dataModel.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findByStudentId(Long studentId);

    List<UserAccount> findAll();

    boolean existsByUsername(String username);
}
