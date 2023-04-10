package com.api.bizta.Review;

import com.api.bizta.Review.model.GetReviewInfos;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
public class ReviewController {

    @Autowired
    private final ReviewProvider reviewProvider;
    private final ReviewService reviewService;
    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
    }

    @ResponseBody
    @GetMapping("/{placeIdx}/reviews")
    public BaseResponse<List<GetReviewInfos>> getReviewInfos(@PathVariable("placeIdx") int placeIdx){
        try{
            List<GetReviewInfos> reviewInfos = reviewProvider.getReviewInfos(placeIdx);
            return new BaseResponse<>(reviewInfos);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
