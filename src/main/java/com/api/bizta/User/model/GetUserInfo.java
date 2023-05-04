package com.api.bizta.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfo {
    private int userIdx;
    private String id;
    private String password;
    private String email;
    private String nickName;
}
