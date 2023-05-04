package com.api.bizta.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetGoogleInfo {
    private String id;
    private String nickName;
    private String password;
    private String email;
    private GetTokenRes getTokenRes;
}
