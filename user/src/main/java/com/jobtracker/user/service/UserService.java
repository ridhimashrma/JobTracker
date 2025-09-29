package com.jobtracker.user.service;

import com.jobtracker.user.entity.User;
import com.jobtracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Transactional
    public User createUser(User user) {
        return repo.save(user);
    }

    public User getUserByEmail(String email) {
        return repo.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }
}