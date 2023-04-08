package com.api.bizta.Review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewInfo {
    private int placeIdx;
    private int userIdx;
    private float rating;
    private String content;
}
