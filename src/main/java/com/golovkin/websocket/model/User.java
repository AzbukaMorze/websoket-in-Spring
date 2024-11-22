package com.golovkin.websocket.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    String id;

    String nickName;
    String fullName;
    Status status;
}
