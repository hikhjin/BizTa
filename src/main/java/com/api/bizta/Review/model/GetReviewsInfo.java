package com.api.bizta.Review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewsInfo {
    private int reviewIdx;
    private int placeIdx;
    private String nickName;
    private float rating;
    private String content;

}
