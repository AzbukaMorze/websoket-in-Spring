package com.golovkin.websocket.service;

import com.golovkin.websocket.model.Status;
import com.golovkin.websocket.model.User;
import com.golovkin.websocket.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
//        if (user.getId() == null || user.getId().isEmpty()) {
//            user.setId(UUID.randomUUID().toString()); // Generate a UUID if no ID is provided
//        }
        user.setStatus(Status.ONLINE);
        return userRepository.save(user);
    }

    @Override
    public User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(String id, Status status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setStatus(status);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByStatus(Status status) {
        return userRepository.findAllByStatus(status);
    }

    @Override
    public void disconnectUser(User user) {
        User storedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));
        storedUser.setStatus(Status.OFFLINE);
        userRepository.save(storedUser);
    }

}

