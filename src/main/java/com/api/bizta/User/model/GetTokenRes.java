package com.api.bizta.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTokenRes {
    private String type;
    private String accessToken;
}
