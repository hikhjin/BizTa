package com.api.bizta.Favorite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetFavoritesInfo {
    private int placeIdx;
    private String name;
    private String imgUrl;
    private int reviewCnt;
    private float grade;
}
