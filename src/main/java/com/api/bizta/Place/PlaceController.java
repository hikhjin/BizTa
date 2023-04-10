package com.api.bizta.Place;

import com.api.bizta.Place.model.GetPlaceInfo;
import com.api.bizta.Place.model.GetPlaceReservation;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {
    @Autowired
    private final PlaceProvider placeProvider;
    private final PlaceService placeService;

    public PlaceController(PlaceProvider placeProvider, PlaceService placeService) {
        this.placeProvider = placeProvider;
        this.placeService = placeService;
    }

    @ResponseBody
    @GetMapping
    public BaseResponse<List<GetPlaceInfo>> getPlaceInfos(){
        try{
            List<GetPlaceInfo> placeInfos = placeProvider.getPlaceInfos();

            return new BaseResponse<>(placeInfos);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{placeIdx}") //특정 장소 정보 조회 /places/1
    public BaseResponse<GetPlaceInfo> getPlaceInfo(@PathVariable("placeIdx") int placeIdx) {
        try {
            GetPlaceInfo placeInfo = placeProvider.getPlaceInfo(placeIdx);
            return new BaseResponse<>(placeInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{placeIdx}/reservation") //특정 장소 사이트 주소, 연락처(reservation) 조회 /places/1/reservation
    public BaseResponse<GetPlaceReservation> getPlaceReservation(@PathVariable("placeIdx") int placeIdx) {
        try {
            GetPlaceReservation placeReservation = placeProvider.getPlaceReservation(placeIdx);
            return new BaseResponse<>(placeReservation);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}