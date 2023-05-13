package com.api.bizta.Event;

import com.api.bizta.Event.model.GetGoogleEvents;
import com.api.bizta.Event.model.PatchEventReq;
import com.api.bizta.Event.model.PostEventReq;
import com.api.bizta.Event.model.PostEventRes;
import com.api.bizta.config.BaseException;
import com.api.bizta.utils.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.api.bizta.config.BaseResponseStatus.*;

@Service
public class EventService {

    private final EventDao eventDao;
    private final EventProvider eventProvider;
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    // 사용자 기본 캘린더의 이벤트 가져오는 주소(primary가 기본)
    private final String GOOGLE_EVENTS_URI = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

    @Autowired
    public EventService(EventDao eventDao, EventProvider eventProvider, Environment env) {
        this.eventDao = eventDao;
        this.eventProvider = eventProvider;
        this.env = env;
    }

    public PostEventRes makeEvent(PostEventReq postEventReq) throws BaseException {
        if(eventProvider.checkDuplicateTime(postEventReq.getPlanIdx(), postEventReq.getUserIdx(), postEventReq.getDate(), postEventReq.getStartTime(), postEventReq.getEndTime()) != 0){
            throw new BaseException(DUPLICATE_TIME);
        }

        try{
            int eventIdx = eventDao.makeEvent(postEventReq);
            return new PostEventRes(eventIdx);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // event 수정
    public String modifyEvent(int eventIdx, PatchEventReq patchEventReq) throws BaseException{

        // 수정하는 event가 존재하는지
        if(eventProvider.checkEventExist(eventIdx) != 1){
            throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
        }

        if(eventProvider.checkDuplicateTimeNotEventIdx(eventIdx, patchEventReq.getPlanIdx(), patchEventReq.getUserIdx(), patchEventReq.getDate(),
                patchEventReq.getStartTime(), patchEventReq.getEndTime()) != 0){
            throw new BaseException(DUPLICATE_TIME);
        }

        try{
            eventDao.modifyEvent(eventIdx, patchEventReq);
            return "Successfully modified event.";
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // event 삭제
    public String deleteEvent(int eventIdx) throws BaseException{
        try{
            // event 존재 여부
            if(eventProvider.checkEventExist(eventIdx) == 0){
                throw new BaseException(REQUESTED_DATA_FAIL_TO_EXIST);
            }

            eventDao.deleteEvent(eventIdx);
            return "Successfully deleted event.";
        }catch (Exception e){
            throw new BaseException(DELETE_FAIL_EVENT);
        }
    }

    // 구글 이벤트 받아오기
    // https://kimcoder.tistory.com/338 참고

    public List<GetGoogleEvents> getGoogleEvents(String accessToken) throws BaseException{
        try {
            List<GetGoogleEvents> googleEvents = new ArrayList<>();
            GetGoogleEvents googleEvent;

            String googleEventsJson = "";

            URL url = new URL(GOOGLE_EVENTS_URI +
                    "?access_token=" + accessToken
            );

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while((line = br.readLine()) != null){
                googleEventsJson += line;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode events = objectMapper.readTree(googleEventsJson).get("items");

            for(JsonNode event : events){
                googleEvent = new GetGoogleEvents();
                if (event.has("summary")) {
                    googleEvent.setTitle(event.get("summary").asText());
                } else {
                    googleEvent.setTitle("");
                }
                int tIdx = event.get("start").get("dateTime").asText().indexOf("T");
                googleEvent.setDate(event.get("start").get("dateTime").asText().substring(0, tIdx));
                googleEvent.setStartTime(event.get("start").get("dateTime").asText().substring(tIdx+1, tIdx+9));
                googleEvent.setEndTime(event.get("end").get("dateTime").asText().substring(tIdx+1, tIdx+9));
                if (event.has("description")) {
                    googleEvent.setDescription(event.get("description").asText());
                } else {
                    googleEvent.setDescription("");
                }

                googleEvents.add(googleEvent);
            }

            return googleEvents;

        } catch(Exception e) {
            throw new BaseException(GOOGLE_EVENTS_IMPORT_ERROR);
        }
    }
}
