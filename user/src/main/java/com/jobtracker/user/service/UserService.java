package com.jobtracker.user.service;

import com.jobtracker.user.entity.User;
import com.jobtracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    // Store login status for users (simple in-memory map)
    private Map<String, Boolean> loginStatus = new HashMap<>();

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

    // ---- LOGIN TRACKING METHODS ----

    // Set login status
    public void setLoggedIn(String email, boolean status) {
        loginStatus.put(email, status);
    }

    // Check if user is logged in
    public boolean isLoggedIn(String email) {
        return loginStatus.getOrDefault(email, false);
    }
}
