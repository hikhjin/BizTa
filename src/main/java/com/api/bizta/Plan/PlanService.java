package com.api.bizta.Plan;

import com.api.bizta.Plan.model.*;
import com.api.bizta.config.BaseException;
import com.api.bizta.utils.JwtService;
import org.springframework.stereotype.Service;

import static com.api.bizta.config.BaseResponseStatus.*;

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
    public PostPlanRes createPlan(int userIdx, PlanInfo planInfo) throws BaseException{
        try {
            int planIdx = planDao.insertPlanInfo(userIdx, planInfo); // interest 제외 plan 추가
            for (Interest interest : planInfo.getInterests()){ //interest 추가
                planDao.insertPlanInterest(planIdx, interest);
            }
            return new PostPlanRes(planIdx);
        } catch (Exception exception) {
            System.out.println(exception); // 에러 콘솔창 출력
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // plan 수정
    public void modifyPlan(int planIdx, PlanInfo planInfo) throws BaseException{

        try{
            int result = planDao.modifyPlanInfo(planIdx, planInfo);
            planDao.deleteInterest(planIdx); // 기존 interest 삭제
            for (Interest interest : planInfo.getInterests()){
                result = planDao.insertPlanInterest(planIdx, interest);
            }
            if(result == 0) throw new BaseException(MODIFY_FAIL_PLAN);
        }
        catch (Exception exception) {
            System.out.println(exception); // 에러 콘솔창 출력
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /*
    // interest 이미 선택됐는지 확인
    public int checkInterest(String interest) throws BaseException {
        try{
            return planDao.checkInterest(interest);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

     */

    // plan 삭제
    public void deletePlan(int planIdx) throws BaseException{

        try{
            int result = planDao.deletePlan(planIdx);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_PLAN);
            }
        }
        catch (Exception exception) {
            System.out.println(exception); // 에러 콘솔창 출력
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
