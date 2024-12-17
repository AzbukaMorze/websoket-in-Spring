package com.golovkin.websocket.controller;

import com.golovkin.websocket.model.User;
import com.golovkin.websocket.model.UserStatus;
import com.golovkin.websocket.repository.UserRepository;
import com.golovkin.websocket.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser() throws Exception {
        // Prepare the mock behavior for the userRepository and passwordEncoder
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.empty()); // No existing user
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        mockMvc.perform(post("/register")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"password\":\"password123\"}"))
                .andExpect(status().isOk());

        // Verify that the passwordEncoder was used
        verify(passwordEncoder, times(1)).encode(user.getPassword());
    }

    @Test
    void addUser() throws Exception {
        // Create a test user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        // Mock the saveUser method (if it’s void, use doNothing)
        doNothing().when(userService).saveUser(any(User.class));

        // Perform the WebSocket message
        mockMvc.perform(post("/user.addUser")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"password\":\"encodedPassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void disconnect() throws Exception {
        // Mock the userService to do nothing when disconnectUser is called
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        doNothing().when(userService).disconnectUser(any(User.class));  // Mocking void method

        mockMvc.perform(post("/user.disconnectUser")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"password\":\"encodedPassword\"}"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void findConnectedUsers() throws Exception {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user1");
        user1.setUserStatus(UserStatus.ONLINE);
        users.add(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setUserStatus(UserStatus.ONLINE);
        users.add(user2);

        when(userService.getUsersByStatus(UserStatus.ONLINE)).thenReturn(users);

        mockMvc.perform(post("/users")
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }
}
