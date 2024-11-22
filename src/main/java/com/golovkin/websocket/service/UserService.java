package com.golovkin.websocket.service;

import com.golovkin.websocket.model.Status;
import com.golovkin.websocket.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserService {

    User saveUser(User user);
    User getUser(String id);
    void deleteUser(String id);
    User updateUser(String id, Status status);
    List<User> getAllUsers();
    List<User> getUsersByStatus(Status status);
}
