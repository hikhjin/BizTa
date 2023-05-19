package com.api.bizta.Place;

import com.api.bizta.Place.model.*;
import com.api.bizta.config.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.*;


@Service
public class PlaceProvider {
    private final PlaceDao placeDao;

    @Autowired
    public PlaceProvider(PlaceDao placeDao){
        this.placeDao = placeDao;
    }

    public List<GetPlaces> getPlaces(String category) throws BaseException{

        List<GetPlaces> places;
        try{
            places = placeDao.getPlaces(category);
        }catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(places == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return places;
    }

    public GetPlaceInfo getPlaceInfo(int placeIdx) throws BaseException {

        GetPlaceInfo placeInfo;
        try {
            placeInfo = placeDao.getPlaceInfo(placeIdx);
            List<String> placeImgUrls = placeDao.getPlaceImgUrls(placeIdx);

            placeInfo.setImgUrls(placeImgUrls);
        }
        catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(placeInfo == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return placeInfo;
    }

    public GetPlaceReservation getPlaceReservation(int placeIdx) throws BaseException {

        GetPlaceReservation placeReservation;
        try {
            placeReservation = placeDao.getPlaceReservation(placeIdx);
        }
        catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(placeReservation == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return placeReservation;
    }

    public List<GetPlaceReview> getPlaceReview(int placeIdx) throws BaseException {

        List<GetPlaceReview> placeReview;
        try {
            placeReview = placeDao.getPlaceReview(placeIdx);
        }
        catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

        return placeReview;
    }

    public GetPlaceMap getPlaceMap(int placeIdx) throws BaseException {

        GetPlaceMap placeMap;
        try {
            placeMap = placeDao.getPlaceMap(placeIdx);
        }
        catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(placeMap == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return placeMap;
    }
}
