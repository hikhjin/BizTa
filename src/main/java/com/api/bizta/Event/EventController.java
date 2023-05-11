package com.api.bizta.Event;

import com.api.bizta.Event.model.*;
import com.api.bizta.Place.model.GetPlaces;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.utils.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventProvider eventProvider;
    private final EventService eventService;
    private final JwtService jwtService;

    public EventController(EventProvider eventProvider, EventService eventService, JwtService jwtService) {
        this.eventProvider = eventProvider;
        this.eventService = eventService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<PostEventRes> makeEvent(@RequestBody PostEventReq postEventReq){

        try{
            if(postEventReq.getUserIdx() != jwtService.getUserIdx()){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }

            if (postEventReq.getTitle().isBlank()) {
                return ResponseEntity.status(EMPTY_TITLE.getCode()).build();
            }

            if(postEventReq.getDate().isBlank()){
                return ResponseEntity.status(EMPTY_DATE.getCode()).build();
            }

            // 중복되는 시간 있는지 확인하는 로직은 안에
            PostEventRes postEventRes = eventService.makeEvent(postEventReq);

            return new ResponseEntity<>(postEventRes, HttpStatus.OK);
        }catch (BaseException e){
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    // 이벤트 조회
    @ResponseBody
    @GetMapping("/{eventIdx}")
    public ResponseEntity<GetEventInfo> getEventInfo(@PathVariable("eventIdx") int eventIdx) {
        try {
            GetEventInfo eventInfo = eventProvider.getEventInfo(eventIdx);
            if(eventInfo.getUserIdx() != jwtService.getUserIdx()){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }
            return new ResponseEntity<>(eventInfo, HttpStatus.OK);
        } catch (BaseException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    // event 수정
    @ResponseBody
    @PatchMapping("/{eventIdx}")
    public ResponseEntity<String> modifyEvent(@PathVariable("eventIdx") int eventIdx, @RequestBody PatchEventReq patchEventReq){
        try{
            if(patchEventReq.getUserIdx() != jwtService.getUserIdx()){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }

            String eventRes = eventService.modifyEvent(eventIdx, patchEventReq);
            return new ResponseEntity<>(eventRes, HttpStatus.OK);
        }catch (BaseException e){
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    // event 삭제
    @ResponseBody
    @PatchMapping("/{eventIdx}/status")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventIdx") int eventIdx){

        try{
            int userIdx = eventProvider.getEventInfo(eventIdx).getUserIdx();
            if(userIdx != jwtService.getUserIdx()){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }
            String deleteRes = eventService.deleteEvent(eventIdx);
            return new ResponseEntity<>(deleteRes, HttpStatus.OK);
        }catch (BaseException e){
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    // event 전체 조회
    @ResponseBody
    @GetMapping
    public ResponseEntity<List<GetEventsInfo>> getEventsInfo(@RequestParam int planIdx){
        try{
            List<GetEventsInfo> eventsInfo = eventProvider.getEventsInfo(planIdx);
            if(eventsInfo.get(0).getUserIdx() != jwtService.getUserIdx()){
                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
            }
            return new ResponseEntity<>(eventsInfo, HttpStatus.OK);
        }catch(BaseException e){
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

//    @ResponseBody
//    @GetMapping("/google")
//    public BaseResponse<> getGoogleEvents(){
//        // Google Calendar API 클라이언트 인스턴스 생성
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
//
//        Calendar calendar = new Calendar.Builder(httpTransport, jsonFactory, credential)
//                .setApplicationName("YOUR_APP_NAME")
//                .build();
//
//        // Calendar API 호출하여 기본 캘린더의 이벤트 목록 가져오기
//        Events events = calendar.events().list("primary").execute();
//
//        // 이벤트 목록 출력
//        List<Event> items = events.getItems();
//        for (Event event : items) {
//            System.out.printf("%s (%s)\n", event.getSummary(), event.getStart().getDateTime());
//        }
//    }


}
