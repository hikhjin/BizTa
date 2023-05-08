package com.api.bizta.Review;

import com.api.bizta.Review.model.GetReviewsInfo;
import com.api.bizta.Review.model.PatchReviewReq;
import com.api.bizta.Review.model.PostReviewReq;
import com.api.bizta.Review.model.PostReviewRes;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private final ReviewProvider reviewProvider;
    private final ReviewService reviewService;
    private final JwtService jwtService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    // 리뷰 전체 조회(/reviews/1?sort=latest&order=descending) 이런식으로 호출
    @ResponseBody
    @GetMapping("/{placeIdx}")
    public BaseResponse<List<GetReviewsInfo>> getReviewsInfo(@PathVariable("placeIdx") int placeIdx, @RequestParam(defaultValue = "latest") String sort, @RequestParam(defaultValue = "descending") String order){
        try{
            List<GetReviewsInfo> reviewInfos = reviewProvider.getReviewsInfo(placeIdx, sort, order);

            return new BaseResponse<>(reviewInfos);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/{placeIdx}")
    public BaseResponse<PostReviewRes> makeReview(@PathVariable("placeIdx") int placeIdx, @RequestBody PostReviewReq postReviewReq){
        try{
            if(jwtService.getUserIdx() != postReviewReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(postReviewReq.getRating() == 0){
                return new BaseResponse<>(EMPTY_RATING);
            }

            if(postReviewReq.getContent().isBlank()){
                return new BaseResponse<>(EMPTY_CONTENT);
            }

            PostReviewRes reviewRes = reviewService.makeReview(placeIdx, postReviewReq);
            return new BaseResponse<>(reviewRes);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // review 수정
    @ResponseBody
    @PatchMapping("/{reviewIdx}")
    public BaseResponse<String> modifyReview(@PathVariable("placeIdx") int placeIdx, @PathVariable("reviewIdx") int reviewIdx, @RequestBody PatchReviewReq patchReviewReq){
        try{
            if(patchReviewReq.getUserIdx() != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String reviewRes = reviewService.modifyReview(reviewIdx, placeIdx, patchReviewReq);
            return new BaseResponse<>(reviewRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // review 삭제
    @ResponseBody
    @PatchMapping("/{reviewIdx}/status")
    public BaseResponse<String> deleteReview(@PathVariable("placeIdx") int placeIdx, @PathVariable("reviewIdx") int reviewIdx){
        try{
            int userIdx = reviewProvider.getReviewInfo(reviewIdx).getUserIdx();
            if(userIdx != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String deleteRes = reviewService.deleteReview(reviewIdx, placeIdx);
            return new BaseResponse<>(deleteRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
