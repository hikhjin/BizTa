package com.api.bizta.Plan.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPlansReq {
    private int userIdx;
    private String country;
    private String city;
    private String hotel;
    private String transport;
    private String from;
    private String to;
    private int headCount;
    private String category;
}
