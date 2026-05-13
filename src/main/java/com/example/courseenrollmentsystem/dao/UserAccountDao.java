package com.example.courseenrollmentsystem.dao;

import com.example.courseenrollmentsystem.dataModel.UserAccount;

import java.util.List;
import java.util.Optional;

public interface UserAccountDao {

    UserAccount save(UserAccount account);

    Optional<UserAccount> findById(Long id);

    Optional<UserAccount> findByStudentId(Long studentId);

    List<UserAccount> findAll();

    Optional<UserAccount> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteById(Long id);
}
