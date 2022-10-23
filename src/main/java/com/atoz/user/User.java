package com.atoz.user;

import lombok.Data;

@Data
public class User {
    String userId;
    String password;
    String nickname;
    String email;
}
