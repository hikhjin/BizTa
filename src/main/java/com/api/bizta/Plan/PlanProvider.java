package com.api.bizta.Plan;

import com.api.bizta.Place.model.*;
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

    public List<PlanIdx> getPlanIdxes(int userIdx) throws BaseException {
        List<PlanIdx> planIdxes;
        try {
            planIdxes = planDao.getPlanIdxes(userIdx);
        }
        catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
        if(planIdxes.size() == 0) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);

        return planIdxes;
    }

    // plan 전체 조회
    public  List<PlanInfo> getPlans(int userIdx) throws BaseException{
        try{
            List<PlanIdx> planIdxes = getPlanIdxes(userIdx);
            List<PlanInfo> planInfos = planDao.getPlans(userIdx, planIdxes);
            return planInfos;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 특정 plan 조회
    public PlanInfo getPlanInfo(int placeIdx) throws BaseException {

        PlanInfo planInfo;
        try {
            planInfo = planDao.getPlanInfo(placeIdx);
        }
        catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(planInfo == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return planInfo;
    }


    // 특정 plan 추천 목록 조회 (3개)
    public List<GetPlaces> getRecommendations(int planIdx) throws BaseException {
        List<GetPlaces> recommendations;
        try {
            List<Interest> subCategories = planDao.getSubCategories(planIdx);
            recommendations = planDao.getRecommendations(subCategories);
        }
        catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(recommendations == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return recommendations;
    }


}
