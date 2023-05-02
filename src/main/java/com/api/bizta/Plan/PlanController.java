package com.api.bizta.Plan;

import com.api.bizta.Place.PlaceProvider;
import com.api.bizta.Place.PlaceService;
import com.api.bizta.Place.model.GetPlaceInfo;
import com.api.bizta.Place.model.GetPlaceReservation;
import com.api.bizta.Place.model.GetPlaces;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {
    @Autowired
    private final PlanProvider planProvider;
    private final PlanService planService;

    public PlanController(PlanProvider planProvider, PlanService planService) {
        this.planProvider = planProvider;
        this.planService = planService;
    }


}
