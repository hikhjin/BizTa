package com.api.bizta.Review;

import com.api.bizta.Review.model.GetReviewInfo;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public BaseResponse<List<GetReviewInfo>> getReviewInfos(@PathVariable("placeIdx") int placeIdx, @RequestParam(defaultValue = "latest") String sort, @RequestParam(defaultValue = "descending") String order){
        try{
            List<GetReviewInfo> reviewInfos = reviewProvider.getReviewInfos(placeIdx);

            if(sort.equals("latest") && order.equals("ascending")){ // 오래된순
                return new BaseResponse<>(reviewInfos);
            }else if(sort.equals("popularity") && order.equals("descending")){ // 인기순
                Collections.sort(reviewInfos, (o1, o2) -> {
                    if(o1.getRating() > o2.getRating()){
                        return -1;
                    }else{
                        return 1;
                    }
                });

                return new BaseResponse<>(reviewInfos);
            }else if(sort.equals("popularity") && order.equals("ascending")){ // 인기 낮은순
                Collections.sort(reviewInfos, (o1, o2) -> {
                    if(o1.getRating() > o2.getRating()){
                        return 1;
                    }else{
                        return -1;
                    }
                });

                return new BaseResponse<>(reviewInfos);
            }
            // default = 최신순
            Collections.sort(reviewInfos, (o1, o2) -> o2.getReviewIdx() - o1.getReviewIdx());

            return new BaseResponse<>(reviewInfos);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
