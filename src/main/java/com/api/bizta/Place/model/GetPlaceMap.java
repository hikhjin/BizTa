package com.api.bizta.Place.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlaceMap {
    private int placeIdx;
    private String address;
    private float lat;
    private float lon;
//    private String key;
}
