package com.api.bizta.Review;

import com.api.bizta.Review.model.*;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ResponseBody
    @PostMapping("/{placeIdx}")
    public ResponseEntity<PostReviewRes> makeReview(@PathVariable("placeIdx") int placeIdx, @RequestBody PostReviewReq postReviewReq){
        try{
            if(jwtService.getUserIdx() != postReviewReq.getUserIdx()){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }

            if(postReviewReq.getRating() == 0){
                return ResponseEntity.status(EMPTY_RATING.getCode()).build();
            }

            if(postReviewReq.getContent().isBlank()){
                return ResponseEntity.status(EMPTY_CONTENT.getCode()).build();
            }

            PostReviewRes reviewRes = reviewService.makeReview(placeIdx, postReviewReq);
            return new ResponseEntity<>(reviewRes, HttpStatus.OK);

        }catch (BaseException e){
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    // review 수정
    @ResponseBody
    @PatchMapping("/{reviewIdx}")
    public ResponseEntity<String> modifyReview(@PathVariable("reviewIdx") int reviewIdx, @RequestBody PatchReviewReq patchReviewReq){
        try{
            if(patchReviewReq.getUserIdx() != jwtService.getUserIdx()){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }

            GetReviewInfo getReviewInfo = reviewProvider.getReviewInfo(reviewIdx);
            String reviewRes = reviewService.modifyReview(reviewIdx, getReviewInfo.getPlaceIdx(), patchReviewReq);
            return new ResponseEntity<>(reviewRes, HttpStatus.OK);
        }catch (BaseException e){
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    // review 삭제
    @ResponseBody
    @PatchMapping("/{reviewIdx}/status")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewIdx") int reviewIdx){
        try{
            GetReviewInfo getReviewInfo = reviewProvider.getReviewInfo(reviewIdx);
            if(getReviewInfo.getUserIdx() != jwtService.getUserIdx()){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }

            String deleteRes = reviewService.deleteReview(reviewIdx, getReviewInfo.getPlaceIdx());
            return new ResponseEntity<>(deleteRes, HttpStatus.OK);
        }catch (BaseException e){
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }
}
