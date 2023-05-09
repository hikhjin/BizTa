package com.api.bizta.Place.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPlaceReview {
    private int placeIdx;
    private int reviewIdx;
    private String nickName;
    private float rating;
    private String content;
}
