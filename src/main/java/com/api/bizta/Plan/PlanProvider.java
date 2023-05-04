package com.api.bizta.Plan;

import com.api.bizta.Plan.PlanDao;
import com.api.bizta.config.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.DATABASE_ERROR;
import static com.api.bizta.config.BaseResponseStatus.REQUESTED_DATA_FAIL_TO_EXIST;


@Service
public class PlanProvider {
    private final PlanDao planDao;

    @Autowired
    public PlanProvider(PlanDao planDao){
        this.planDao = planDao;
    }


}
