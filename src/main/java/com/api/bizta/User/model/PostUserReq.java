package com.api.bizta.User.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String id;
    private String nickName;
    private String password;
    private String email;
    private String birth;
    private String country;
    //private int emailCheck;
}