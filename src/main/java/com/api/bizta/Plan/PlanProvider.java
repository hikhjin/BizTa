package com.api.bizta.Plan;

import com.api.bizta.Place.model.GetPlaceInfo;
import com.api.bizta.Plan.model.*;
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

    // 특정 plan 조회
    public GetPlanInfo getPlanInfo(int placeIdx) throws BaseException {

        GetPlanInfo getPlanInfo;
        try {
            getPlanInfo = planDao.getPlanInfo(placeIdx);
        }
        catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(getPlanInfo == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return getPlanInfo;
    }


}
