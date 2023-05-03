package com.api.bizta.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginRes {
    private int userIdx;
    private String id;
    private String password;
    private String email;
    private String nickName;
    private GetTokenRes getTokenRes;

    public PostLoginRes(int userIdx, String id, String email, String nickName){
        this.userIdx = userIdx;
        this.id = id;
        this.email = email;
        this.nickName = nickName;
    }

    public PostLoginRes(String id, String password, String email, String nickName, GetTokenRes getTokenRes){
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.getTokenRes = getTokenRes;
    }
}
