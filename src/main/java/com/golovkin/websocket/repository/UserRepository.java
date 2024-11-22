package com.golovkin.websocket.repository;

import com.golovkin.websocket.model.Status;
import com.golovkin.websocket.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAllByStatus(Status status);

}
