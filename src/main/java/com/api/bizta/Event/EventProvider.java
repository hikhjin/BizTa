package com.api.bizta.Event;

import com.api.bizta.Event.model.GetEventInfo;
import com.api.bizta.Place.model.GetPlaceInfo;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.api.bizta.config.BaseResponseStatus.*;

@Service
public class EventProvider {

    private EventDao eventDao;
    private JwtService jwtService;

    @Autowired
    public EventProvider(EventDao eventDao, JwtService jwtService) {
        this.eventDao = eventDao;
        this.jwtService = jwtService;
    }

    public GetEventInfo getEventInfo(int eventIdx) throws BaseException {

        GetEventInfo eventInfo;
        try {
            eventInfo = eventDao.getEventInfo(eventIdx);
        }
        catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

        if(eventInfo.getUserIdx() != jwtService.getUserIdx()){
            throw new BaseException(INVALID_USER_JWT);
        }

        if(eventInfo == null) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        return eventInfo;
    }

    // 요청 받은 event 시간과 기존 event 시간이 겹치는지 확인
    public int checkDuplicateTime(int planIdx, int userIdx, String date, String startTime, String endTime) throws BaseException {
        try{
            return eventDao.checkDuplicateTime(planIdx, userIdx, date, startTime, endTime);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEventExist(int eventIdx) throws BaseException{
        try{
            return eventDao.checkEventExist(eventIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
