package com.api.bizta.Place.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlaces {
    private int placeIdx;
    private String name;
    private String imgUrl;
    private float grade;
    private int reviewCnt;
}
