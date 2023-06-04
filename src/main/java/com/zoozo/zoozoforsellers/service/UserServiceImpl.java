package com.zoozo.zoozoforsellers.service;

import com.zoozo.zoozoforsellers.entity.User;
import com.zoozo.zoozoforsellers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(String nic) {
        return userRepository.findById(nic).get();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByRole("USER");
    }

    @Override
    public List<User> getAllAdmins() {
        return userRepository.findAllByRole("ADMIN");
    }

    @Override
    public User updateUserById(User user) {
        return userRepository.save(user);
    }

    @Override
    public void removeUser(String nic) {
        userRepository.deleteById(nic);
    }

    @Override
    public User banUser(User user) {
        return userRepository.save(user);
    }

}
