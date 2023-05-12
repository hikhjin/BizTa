package com.api.bizta.Plan;
import com.api.bizta.Plan.model.*;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.config.BaseResponseStatus;
import com.api.bizta.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.api.bizta.config.BaseResponseStatus.INVALID_USER_JWT;

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
    public ResponseEntity<PostPlanRes> createPlan(@RequestBody PlanInfo planInfo) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(planInfo.getUserIdx() != userIdxByJwt){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }

            PostPlanRes postPlanRes = planService.createPlan(planInfo.getUserIdx(), planInfo);
            return new ResponseEntity<>(postPlanRes, HttpStatus.OK);
        } catch (BaseException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    // plan 수정
    @ResponseBody
    @PatchMapping("/{planIdx}")
    public ResponseEntity<String> modifyPlan(@PathVariable("planIdx") int planIdx, @RequestBody PlanInfo planInfo) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(planInfo.getUserIdx() != userIdxByJwt){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }
            planService.modifyPlan(planIdx, planInfo);
            String result = "Successfully modified plan.";
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (BaseException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    // plan 삭제
    @ResponseBody
    @PatchMapping("/{planIdx}/status")
    public ResponseEntity<String> deletePlan(@PathVariable("planIdx") int planIdx) {
        try{
            /*
            int userIdxByJwt = jwtService.getUserIdx();
            if(postPlansReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // jwt 확인
            }

             */

            planService.deletePlan(planIdx);
            String result = "Successfully deleted plan.";
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch(BaseException e){
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    // 특정 plan 조회
    @ResponseBody
    @GetMapping("/{planIdx}")
    public ResponseEntity<PlanInfo> getPlanInfo(@PathVariable ("planIdx") int planIdx) {
        try {
            PlanInfo planInfo = planProvider.getPlanInfo(planIdx);
            return new ResponseEntity<>(planInfo, HttpStatus.OK);
        } catch (BaseException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

}
