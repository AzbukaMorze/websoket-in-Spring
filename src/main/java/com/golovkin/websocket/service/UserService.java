package com.golovkin.websocket.service;

import com.golovkin.websocket.model.UserStatus;
import com.golovkin.websocket.model.User;

import java.util.List;

public interface UserService {

    void saveUser(User user);
    List<User> getUsersByStatus(UserStatus userStatus);
    void disconnectUser(User user);
}
