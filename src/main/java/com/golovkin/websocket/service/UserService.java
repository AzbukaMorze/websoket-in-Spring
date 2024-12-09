package com.golovkin.websocket.service;

import com.golovkin.websocket.model.UserStatus;
import com.golovkin.websocket.model.User;

import java.util.List;

public interface UserService {

    void saveUser(User user);
    User getUser(String id);
    void deleteUser(String id);
    User updateUser(String id, UserStatus userStatus);
    List<User> getAllUsers();
    List<User> getUsersByStatus(UserStatus userStatus);
    void disconnectUser(User user);
}
