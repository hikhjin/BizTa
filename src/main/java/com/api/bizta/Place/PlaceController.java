package com.api.bizta.Place;

import com.api.bizta.Place.model.*;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {

    private String apiKey = "AIzaSyDjQwiz2pO8MaE6Y6mC_PllLyloxVTHG_E";
    @Autowired
    private final PlaceProvider placeProvider;
    private final PlaceService placeService;

    public PlaceController(PlaceProvider placeProvider, PlaceService placeService) {
        this.placeProvider = placeProvider;
        this.placeService = placeService;
    }

//    @ResponseBody
//    @GetMapping // 전체 장소 조회(default는 카테고리 분류 X)
//    public BaseResponse<GetPlaces> getPlaces(@RequestParam(required = false, defaultValue = "") String category){
//        try{
//            List<GetPlaces> places = placeProvider.getPlaces(category);
//
//            return new BaseResponse<>(places);
//        }catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }

    @ResponseBody
    @GetMapping // 전체 장소 조회(default는 카테고리 분류 X)
    public ResponseEntity<List<GetPlaces>> getPlaces(@RequestParam(required = false, defaultValue = "") String category){
        try{
            List<GetPlaces> places = placeProvider.getPlaces(category);

            return new ResponseEntity<>(places, HttpStatus.OK);
        }catch(BaseException e){
//            return new BaseResponse<>((exception.getStatus()));
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    @ResponseBody
    @GetMapping("/{placeIdx}") //특정 장소 정보 조회 /places/1
    public ResponseEntity<GetPlaceInfo> getPlaceInfo(@PathVariable("placeIdx") int placeIdx) {
        try {
            GetPlaceInfo placeInfo = placeProvider.getPlaceInfo(placeIdx);
            return new ResponseEntity<>(placeInfo, HttpStatus.OK);
        } catch (BaseException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    @ResponseBody
    @GetMapping("/{placeIdx}/reservation") //특정 장소 사이트 주소, 연락처(reservation) 조회 /places/1/reservation
    public ResponseEntity<GetPlaceReservation> getPlaceReservation(@PathVariable("placeIdx") int placeIdx) {
        try {
            GetPlaceReservation placeReservation = placeProvider.getPlaceReservation(placeIdx);
            return new ResponseEntity<>(placeReservation, HttpStatus.OK);
        } catch (BaseException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    @ResponseBody
    @GetMapping("/{placeIdx}/detail") //특정 장소 사이트 주소, 연락처(reservation) 조회 /places/1/reservation
    public ResponseEntity<GetPlaceDetail> getPlaceDetail(@PathVariable("placeIdx") int placeIdx) {
        try {
            GetPlaceDetail placeDetail = placeProvider.getPlaceDetail(placeIdx);
            return new ResponseEntity<>(placeDetail, HttpStatus.OK);
        } catch (BaseException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }



    @ResponseBody
    @GetMapping("/{placeIdx}/review")
    public ResponseEntity<List<GetPlaceReview>> getPlaceReview(@PathVariable("placeIdx") int placeIdx) {
        try {
            List<GetPlaceReview> placeReview = placeProvider.getPlaceReview(placeIdx);
            return new ResponseEntity<>(placeReview, HttpStatus.OK);
        } catch (BaseException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    @ResponseBody
    @GetMapping("/{placeIdx}/map")
    public ResponseEntity<GetPlaceMap> getPlaceMap(@PathVariable("placeIdx") int placeIdx) {
        try {
            GetPlaceMap placeMap = placeProvider.getPlaceMap(placeIdx);

            return new ResponseEntity<>(placeMap, HttpStatus.OK);
        } catch (BaseException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

}
