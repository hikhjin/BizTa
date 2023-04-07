package com.api.bizta.Place.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlaceInfo {
    private int placeIdx;
    private String name;
    private String category;
    private String imgUrl;
    private String address;
    private String description;
    private float grade;
    private int reviewCnt;
}
