package com.golovkin.websocket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @Indexed(unique = true)
    String nickName;

    String fullName;

    @JsonProperty("userStatus")
    UserStatus userStatus;

    @Indexed
    private String username;

    private String password;

    private boolean active;

}
