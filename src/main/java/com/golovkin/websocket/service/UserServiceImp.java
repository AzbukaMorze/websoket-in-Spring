package com.golovkin.websocket.service;

import com.golovkin.websocket.model.UserStatus;
import com.golovkin.websocket.model.User;
import com.golovkin.websocket.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public void saveUser(User user) {
//        if (user.getId() == null || user.getId().isEmpty()) {
//            user.setId(UUID.randomUUID().toString()); // Generate a UUID if no ID is provided
//        }
        user.setUserStatus(UserStatus.ONLINE);
        userRepository.save(user);
    }

    @Override
    public List<User> getUsersByStatus(UserStatus userStatus) {
        return userRepository.findAllByUserStatus(userStatus);
    }

    @Override
    public void disconnectUser(User user) {
        var storedUser = userRepository.findById(user.getNickName()).orElse(null);
        if (storedUser != null) {
            storedUser.setUserStatus(UserStatus.OFFLINE);
            userRepository.save(storedUser);
        }
    }


}

