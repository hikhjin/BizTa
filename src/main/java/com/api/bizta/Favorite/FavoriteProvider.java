package com.api.bizta.Favorite;

import com.api.bizta.Favorite.model.GetFavoritesInfo;
import com.api.bizta.Place.model.GetPlaces;
import com.api.bizta.Plan.PlanDao;
import com.api.bizta.config.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.DATABASE_ERROR;
import static com.api.bizta.config.BaseResponseStatus.REQUESTED_DATA_FAIL_TO_EXIST;

@Service
public class FavoriteProvider {
    private final FavoriteDao favoriteDao;

    public FavoriteProvider(FavoriteDao favoriteDao){
        this.favoriteDao = favoriteDao;
    }

    public int checkPlaceExist(int placeIdx) throws BaseException{
        try{
            return favoriteDao.checkPlaceExist(placeIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkFavoriteExist(int placeIdx, int userIdx) throws BaseException{
        try{
            return favoriteDao.checkFavoriteExist(placeIdx, userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFavoritesInfo> getFavorites(int userIdx, String sort) throws BaseException{

        List<GetFavoritesInfo> favorites;
        try{
            favorites = favoriteDao.getFavorites(userIdx, sort);
        }catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

        if(favorites == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return favorites;
    }
}
