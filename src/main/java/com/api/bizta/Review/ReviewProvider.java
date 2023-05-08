package com.api.bizta.Review;

import com.api.bizta.Review.model.GetReviewInfo;
import com.api.bizta.Review.model.GetReviewsInfo;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.*;

@Service
public class ReviewProvider {

    private final ReviewDao reviewDao;

    @Autowired
    public ReviewProvider(ReviewDao reviewDao){
        this.reviewDao = reviewDao;
    }

    public List<GetReviewsInfo> getReviewsInfo(int placeIdx, String sort, String order) throws BaseException{

        List<GetReviewsInfo> reviewInfos;

        try{
            reviewInfos = reviewDao.getReviewsInfo(placeIdx);
        }catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(reviewInfos == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);

        if(sort.equals("latest") && order.equals("ascending")){ // 오래된순
            return reviewInfos;
        }else if(sort.equals("popularity") && order.equals("descending")){ // 인기순
            Collections.sort(reviewInfos, (o1, o2) -> {
                if(o1.getRating() > o2.getRating()){
                    return -1;
                }else{
                    return 1;
                }
            });
            return reviewInfos;
        }else if(sort.equals("popularity") && order.equals("ascending")){ // 인기 낮은순
            Collections.sort(reviewInfos, (o1, o2) -> {
                if(o1.getRating() > o2.getRating()){
                    return 1;
                }else{
                    return -1;
                }
            });
            return reviewInfos;
        }
        // default = 최신순
        Collections.sort(reviewInfos, (o1, o2) -> o2.getReviewIdx() - o1.getReviewIdx());

        return reviewInfos;
    }

    public GetReviewInfo getReviewInfo(int reviewIdx) throws BaseException{

        GetReviewInfo reviewInfo;
        try{
            reviewInfo = reviewDao.getReviewInfo(reviewIdx);
        }catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

        if(reviewInfo == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);

        return reviewInfo;
    }

    public int checkPlaceExist(int placeIdx) throws BaseException{
        try{
            return reviewDao.checkPlaceExist(placeIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkAlreadyExist(int userIdx, int placeIdx) throws BaseException{
        try{
            return reviewDao.checkAlreadyExist(userIdx, placeIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkReviewExist(int reviewIdx) throws BaseException{
        try{
            return reviewDao.checkReviewExist(reviewIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
