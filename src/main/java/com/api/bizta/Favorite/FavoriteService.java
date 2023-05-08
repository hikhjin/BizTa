package com.api.bizta.Favorite;

import com.api.bizta.config.BaseException;
import com.api.bizta.utils.JwtService;
import org.springframework.stereotype.Service;

import static com.api.bizta.config.BaseResponseStatus.DATABASE_ERROR;
import static com.api.bizta.config.BaseResponseStatus.REQUESTED_DATA_FAIL_TO_EXIST;

@Service
public class FavoriteService {

    private final FavoriteDao favoriteDao;
    private final FavoriteProvider favoriteProvider;
    private final JwtService jwtService;

    public FavoriteService(FavoriteDao favoriteDao, FavoriteProvider favoriteProvider, JwtService jwtService) {
        this.favoriteDao = favoriteDao;
        this.favoriteProvider = favoriteProvider;
        this.jwtService = jwtService;
    }

    // favorite 클릭
    public String clickFavorite(int placeIdx, int userIdx) throws BaseException{
        try{
            // place 존재 여부
            if(favoriteProvider.checkPlaceExist(placeIdx) == 0){
                throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
            }
            // favorite DB에 있는지 확인하기
            // 있을 때 status 변경 및 place의 favoriteCnt 증가 및 감소
            if(favoriteProvider.checkFavoriteExist(placeIdx, userIdx) == 1){
                favoriteDao.changeFavoriteStatus(placeIdx, userIdx);
            }
            // 없을 때 insert
            else{
                favoriteDao.addFavorite(placeIdx, userIdx);
            }
            return "Successfully add/deleted favorite.";
        } catch (Exception e){
            System.out.println(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
