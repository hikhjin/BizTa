package com.api.bizta.Place;

import com.api.bizta.Place.model.GetPlaceInfo;
import com.api.bizta.config.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.api.bizta.config.BaseResponseStatus.*;


@Service
public class PlaceProvider {
    private final PlaceDao placeDao;

    @Autowired
    public PlaceProvider(PlaceDao placeDao){
        this.placeDao = placeDao;
    }
    public GetPlaceInfo getPlaceInfo(int placeIdx) throws BaseException {

        GetPlaceInfo placeInfo;
        try {
            placeInfo = placeDao.getPlaceInfo(placeIdx);
        }
        catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(placeInfo == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return placeInfo;
    }
}
