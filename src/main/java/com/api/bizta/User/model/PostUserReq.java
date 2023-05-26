package com.api.bizta.User.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUserReq {
    private String id;
    private String nickName;
    private String password;
    private String email;
    private String birth;
    private String country;
    //private int emailCheck;

    public PostUserReq(String id, String nickName, String password, String email){
        this.id = id;
        this.nickName = nickName;
        this.password = password;
        this.email = email;
    }
}