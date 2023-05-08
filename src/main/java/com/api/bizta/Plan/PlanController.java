package com.api.bizta.Plan;
import com.api.bizta.Plan.model.PostPlansReq;
import com.api.bizta.Plan.model.PostPlansRes;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.config.BaseResponseStatus;
import com.api.bizta.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {
    @Autowired
    private final PlanProvider planProvider;
    @Autowired
    private final PlanService planService;
    @Autowired
    private final JwtService jwtService;

    public PlanController(PlanProvider planProvider, PlanService planService, JwtService jwtService) {
        this.planProvider = planProvider;
        this.planService = planService;
        this.jwtService = jwtService;
    }

    // plan 추가
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPlansRes> createPlan(@RequestBody PostPlansReq postPlansReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(postPlansReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // jwt 확인
            }

            PostPlansRes postPlanRes = planService.createPlan(postPlansReq.getUserIdx(), postPlansReq);
            return new BaseResponse<>(postPlanRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // plan 삭제
    @ResponseBody
    @PatchMapping("/{planIdx}/status")
    public BaseResponse<String> deletePlan(@PathVariable("planIdx") int planIdx) {
        try{
            /*
            int userIdxByJwt = jwtService.getUserIdx();
            if(postPlansReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // jwt 확인
            }

             */
            planService.deletePlan(planIdx);
            String result = "Successfully deleted plan.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
