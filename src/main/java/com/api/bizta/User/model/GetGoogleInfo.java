package com.api.bizta.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetGoogleInfo {
    private String id;
    private String nickName;
    private String password;
    private String email;
    private GetTokenRes getTokenRes;
    private String refreshToken;

    public GetGoogleInfo(String id, String nickName, String password, String email, GetTokenRes getTokenRes){
        this.id = id;
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.getTokenRes = getTokenRes;
    }
}
