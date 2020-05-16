package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {
    User getUser();
    User saveUser(User user);
}
