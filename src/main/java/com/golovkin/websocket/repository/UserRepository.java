package com.golovkin.websocket.repository;

import com.golovkin.websocket.model.UserStatus;
import com.golovkin.websocket.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends MongoRepository<User, String> {
    List<User> findAllByUserStatus(UserStatus userStatus);
    Optional<User> findByUsername(String username);

}

