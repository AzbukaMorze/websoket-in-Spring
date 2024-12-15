package com.golovkin.websocket.controller;

import com.golovkin.websocket.model.User;
import com.golovkin.websocket.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.golovkin.websocket.model.UserStatus.OFFLINE;
import static com.golovkin.websocket.model.UserStatus.ONLINE;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void findConnectedUsers() throws Exception {
        // Arrange
        User user = new User("testNick", "Test User", ONLINE); // Pass UserStatus here

        List<User> users = List.of(user);
        when(userService.getUsersByStatus(any())).thenReturn(users);

        // Act and Assert
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickName").value("testNick"))
                .andExpect(jsonPath("$[0].fullName").value("Test User"))
                .andExpect(jsonPath("$[0].userStatus").value("ONLINE"));

        // Verify service call
        verify(userService, times(1)).getUsersByStatus(any());
    }
}
