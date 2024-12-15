package com.golovkin.websocket.service;

import com.golovkin.websocket.model.User;
import com.golovkin.websocket.model.UserStatus;
import com.golovkin.websocket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImp userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a user object for testing
        user = new User();
        user.setNickName("testUser");
        user.setUserStatus(UserStatus.ONLINE);
    }

    @Test
    void saveUser() {
        // Test that the saveUser method correctly sets user status and calls save

        userService.saveUser(user);

        // Verify that userRepository.save was called once
        verify(userRepository, times(1)).save(user);

        // Ensure that the user status is set to ONLINE
        assertEquals(UserStatus.ONLINE, user.getUserStatus());
    }

    @Test
    void getUsersByStatus() {
        // Test that getUsersByStatus returns users by the given status

        User user1 = new User();
        user1.setNickName("user1");
        user1.setUserStatus(UserStatus.ONLINE);

        User user2 = new User();
        user2.setNickName("user2");
        user2.setUserStatus(UserStatus.OFFLINE);

        // Mock the repository method to return a list of users
        when(userRepository.findAllByUserStatus(UserStatus.ONLINE))
                .thenReturn(Arrays.asList(user1, user));

        // Call the service method
        var users = userService.getUsersByStatus(UserStatus.ONLINE);

        // Verify the repository method was called once
        verify(userRepository, times(1)).findAllByUserStatus(UserStatus.ONLINE);

        // Assert that the result matches
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void disconnectUser() {
        // Test that disconnectUser sets the status to OFFLINE and saves the user

        User storedUser = new User();
        storedUser.setNickName("testUser");
        storedUser.setUserStatus(UserStatus.ONLINE);

        // Mock the repository to return the storedUser
        when(userRepository.findById("testUser")).thenReturn(Optional.of(storedUser));

        userService.disconnectUser(user);

        // Verify that the user status is changed to OFFLINE
        assertEquals(UserStatus.OFFLINE, storedUser.getUserStatus());

        // Verify that the userRepository.save was called to save the updated user
        verify(userRepository, times(1)).save(storedUser);
    }

    @Test
    void disconnectUser_UserNotFound() {
        // Test that disconnectUser does nothing when user is not found in the repository

        // Mock the repository to return empty for user with nickName "nonExistingUser"
        when(userRepository.findById("nonExistingUser")).thenReturn(Optional.empty());

        userService.disconnectUser(user);

        // Verify that save method was not called because user was not found
        verify(userRepository, never()).save(any());
    }
}
