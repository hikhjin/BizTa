package com.api.bizta.Event;

import com.api.bizta.Event.model.GetEventInfo;
import com.api.bizta.Event.model.GetEventsInfo;
import com.api.bizta.Event.model.GetEventsToday;
import com.api.bizta.Place.model.GetPlaceInfo;
import com.api.bizta.Plan.model.PlanIdx;
import com.api.bizta.Plan.model.PlanInfo;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public int checkDuplicateTimeNotEventIdx(int eventIdx, int planIdx, int userIdx, String date, String startTime, String endTime) throws BaseException {
        try{
            return eventDao.checkDuplicateTimeNotEventIdx(eventIdx, planIdx, userIdx, date, startTime, endTime);
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

    public List<GetEventsInfo> getEventsInfo(int planIdx) throws BaseException{
        try{
            return eventDao.getEventsInfo(planIdx);
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public  List<GetEventsInfo> getEventsToday(int userIdx) throws BaseException{
        try{
            List<PlanIdx> planIdxes = getPlanIdxes(userIdx);
            List<GetEventsInfo> eventsToday = eventDao.getEventsToday(userIdx, planIdxes);
            return eventsToday;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<PlanIdx> getPlanIdxes(int userIdx) throws BaseException {
        List<PlanIdx> planIdxes;
        try {
            planIdxes = eventDao.getPlanIdxes(userIdx);
        }
        catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
        if(planIdxes.size() == 0) throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);

        return planIdxes;
    }

}
