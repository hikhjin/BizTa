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
    private String startDate;
    private String endDate;
    private int companionCnt;
    private String interest1;
    private String interest2;
    private String interest3;
    private String interest4;
    private String interest5;
}
