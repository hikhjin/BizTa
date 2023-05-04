package com.api.bizta.Plan;

import com.api.bizta.Plan.model.*;
import com.api.bizta.config.BaseException;
import com.api.bizta.utils.JwtService;
import org.springframework.stereotype.Service;

import static com.api.bizta.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PlanService {

    private final PlanDao planDao;
    private final PlanProvider planProvider;
    private final JwtService jwtService;

    public PlanService(PlanDao planDao, PlanProvider planProvider, JwtService jwtService) {
        this.planDao = planDao;
        this.planProvider = planProvider;
        this.jwtService = jwtService;
    }

    // plan 추가
    public PostPlansRes createPlan(int userIdx, PostPlansReq postPlansReq) throws BaseException{
        try {
            int planIdx = planDao.insertPlanInfo(userIdx, postPlansReq); // interest 제외 plan 추가
            for (PostInterestReq interest : postPlansReq.getInterests()){ //interest 추가
                planDao.insertPlanInterest(planIdx, interest);
            }
            return new PostPlansRes(planIdx);
        } catch (Exception exception) {
            System.out.println(exception); // 에러 콘솔창 출력
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
