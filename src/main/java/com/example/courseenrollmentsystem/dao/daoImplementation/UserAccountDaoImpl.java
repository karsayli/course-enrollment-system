package com.example.courseenrollmentsystem.dao.daoImplementation;

import com.example.courseenrollmentsystem.dao.UserAccountDao;
import com.example.courseenrollmentsystem.dataModel.UserAccount;
import com.example.courseenrollmentsystem.repository.UserAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserAccountDaoImpl implements UserAccountDao {

    private final UserAccountRepository repo;

    public UserAccountDaoImpl(UserAccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserAccount save(UserAccount account) {
        return repo.save(account);
    }

    @Override
    public Optional<UserAccount> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<UserAccount> findByStudentId(Long studentId) {
        return repo.findByStudentId(studentId);
    }

    @Override
    public List<UserAccount> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repo.existsByUsername(username);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
