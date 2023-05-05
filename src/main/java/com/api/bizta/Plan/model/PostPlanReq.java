package com.api.bizta.Plan.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPlanReq {
    private int userIdx;
    private String country;
    private String city;
    private String hotel;
    private String transport;
    private String startDate;
    private String endDate;
    private int companionCnt;
    private List<PostInterestReq> interests;
}
