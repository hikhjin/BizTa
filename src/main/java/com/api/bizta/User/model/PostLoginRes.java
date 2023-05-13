package com.api.bizta.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostLoginRes {
    private int userIdx;
    private String id;
    private String password;
    private String email;
    private String nickName;
    private GetTokenRes getTokenRes;

    public PostLoginRes(String id, String password, String email, String nickName, GetTokenRes getTokenRes){
        this.id = id;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.getTokenRes = getTokenRes;
    }

    public PostLoginRes(int userIdx, String nickName){
        this.userIdx = userIdx;
        this.nickName = nickName;
    }
}
