package com.api.bizta.Event;

import com.api.bizta.Event.model.PatchEventReq;
import com.api.bizta.Event.model.PostEventReq;
import com.api.bizta.Event.model.PostEventRes;
import com.api.bizta.config.BaseException;
import com.api.bizta.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.api.bizta.config.BaseResponseStatus.*;

@Service
public class EventService {

    private final EventDao eventDao;
    private final EventProvider eventProvider;
    private final JwtService jwtService;
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public EventService(EventDao eventDao, EventProvider eventProvider, JwtService jwtService, Environment env) {
        this.eventDao = eventDao;
        this.eventProvider = eventProvider;
        this.jwtService = jwtService;
        this.env = env;
    }

    public PostEventRes makeEvent(PostEventReq postEventReq) throws BaseException {
        if(eventProvider.checkDuplicateTime(postEventReq.getPlanIdx(), postEventReq.getUserIdx(), postEventReq.getDate(), postEventReq.getStartTime(), postEventReq.getEndTime()) != 0){
            throw new BaseException(DUPLICATE_TIME);
        }

        try{
            int eventIdx = eventDao.makeEvent(postEventReq);
            System.out.println("service eventIdx: " + eventIdx);
            return new PostEventRes(eventIdx);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // event 수정
    public String modifyEvent(int eventIdx, PatchEventReq patchEventReq) throws BaseException{
        if(eventProvider.checkDuplicateTime(patchEventReq.getPlanIdx(), patchEventReq.getUserIdx(), patchEventReq.getDate(),
                patchEventReq.getStartTime(), patchEventReq.getEndTime()) != 0){
            throw new BaseException(DUPLICATE_TIME);
        }

        // 수정하는 event가 존재하는지
        if(eventProvider.checkEventExist(eventIdx) != 1){
            throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        }

        try{
            eventDao.modifyEvent(eventIdx, patchEventReq);
            return "Successfully modified event.";
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
