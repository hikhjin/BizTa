package com.api.bizta.Review;

import com.api.bizta.Review.model.GetReviewInfo;
import com.api.bizta.config.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.*;

@Service
public class ReviewProvider {

    private final ReviewDao reviewDao;

    @Autowired
    public ReviewProvider(ReviewDao reviewDao){
        this.reviewDao = reviewDao;
    }

    public List<GetReviewInfo> getReviewInfos(int placeIdx) throws BaseException{

        List<GetReviewInfo> reviewInfos;
        try{
            reviewInfos = reviewDao.getReviewInfos(placeIdx);
        }catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(reviewInfos == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return reviewInfos;
    }
}