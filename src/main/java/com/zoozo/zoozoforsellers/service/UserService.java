package com.zoozo.zoozoforsellers.service;

import com.zoozo.zoozoforsellers.entity.Product;
import com.zoozo.zoozoforsellers.entity.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    User getUserById(String nic);

    List<User> getAllUsers();

    List<User> getAllAdmins();

    User updateUserById(User user);

    void removeUser(String nic);

    User banUser(User user);
}
