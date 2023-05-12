package com.api.bizta.Place.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlaceInfo {
    private int placeIdx;
    private String name;
    private String category;
//    private String imgUrl;
    private List<String> imgUrls;
    private String address;
    private String description;
    private float grade;
//    private boolean favoriteCnt;
    private int reviewCnt;
}
