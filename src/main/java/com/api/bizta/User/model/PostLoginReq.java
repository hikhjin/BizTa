package com.api.bizta.User.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginReq {
    private String id;
    private String password;
}