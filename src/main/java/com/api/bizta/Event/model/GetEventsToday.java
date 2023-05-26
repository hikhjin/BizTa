package com.api.bizta.Event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEventsToday {
    private int planIdx;
    private int userIdx;
    private String title;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
}