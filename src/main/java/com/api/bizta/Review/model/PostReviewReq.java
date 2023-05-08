package com.api.bizta.Review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostReviewReq {
    private int userIdx;
    private int rating;
    private String content;
}
