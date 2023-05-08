package com.api.bizta.Review;

import com.api.bizta.Event.model.PatchEventReq;
import com.api.bizta.Event.model.PostEventRes;
import com.api.bizta.Place.PlaceDao;
import com.api.bizta.Review.model.PatchReviewReq;
import com.api.bizta.Review.model.PostReviewReq;
import com.api.bizta.Review.model.PostReviewRes;
import com.api.bizta.config.BaseException;
import org.springframework.stereotype.Service;

import static com.api.bizta.config.BaseResponseStatus.*;

@Service
public class ReviewService {

    private final ReviewProvider reviewProvider;
    private final ReviewDao reviewDao;
    private final PlaceDao placeDao;

    public ReviewService(ReviewProvider reviewProvider, ReviewDao reviewDao, PlaceDao placeDao){
        this.reviewProvider = reviewProvider;
        this.reviewDao = reviewDao;
        this.placeDao = placeDao;
    }

    // place에 대한 review 작성
    public PostReviewRes makeReview(int placeIdx, PostReviewReq postReviewReq) throws BaseException {
        // place 존재 여부
        if(reviewProvider.checkPlaceExist(placeIdx) == 0){
            throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        }
        // 유저가 place에 대해 이미 작성 했는지 판단
        if(reviewProvider.checkAlreadyExist(postReviewReq.getUserIdx(), placeIdx) != 0){
            throw new BaseException(ALREADY_WRITTEN);
        }

        try{
            // 리뷰 작성 로직
            int reviewIdx = reviewDao.makeReview(placeIdx, postReviewReq);

            // 리뷰 작성 후 place의 reviewCnt 증가 및 grade 계산
            placeDao.updateCntAndGrade(placeIdx);

            return new PostReviewRes(reviewIdx);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String modifyReview(int reviewIdx, int placeIdx, PatchReviewReq patchReviewReq) throws BaseException{

        // 수정하는 review가 존재하는지
        if(reviewProvider.checkReviewExist(reviewIdx) == 0){
            throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        }

        try{
            reviewDao.modifyReview(reviewIdx, patchReviewReq);
            placeDao.updateCntAndGrade(placeIdx);
            return "Successfully modified review.";
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String deleteReview(int reviewIdx, int placeIdx) throws BaseException{
        try{
            // review 존재 여부
            if(reviewProvider.checkReviewExist(reviewIdx) == 0){
                throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
            }

            reviewDao.deleteReview(reviewIdx);
            placeDao.updateCntAndGrade(placeIdx);
            return "Successfully deleted review.";
        }catch (Exception e){
            throw new BaseException(DELETE_FAIL_EVENT);
        }
    }
}
