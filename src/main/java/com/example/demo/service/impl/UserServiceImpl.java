package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private User user; //mock user that logged in the system

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser() {
        if(this.user == null) {
            User user = new User();
            user.setUsername("test");
            user.setPassword("test");
            this.user = userRepository.save(user);
        }

        return this.user;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
