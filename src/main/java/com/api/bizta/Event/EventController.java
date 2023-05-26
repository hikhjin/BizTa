package com.api.bizta.Event;

import com.api.bizta.Event.model.*;
import com.api.bizta.Place.model.GetPlaces;
import com.api.bizta.Plan.model.PlanIdx;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.utils.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public ResponseEntity<String> makeEvent(@RequestBody List<PostEventReq> postEventReq){

        try{
            // 인증
//            if(postEventReq.get(0).getUserIdx() != jwtService.getUserIdx()){
//                return ResponseEntity.status(INVALID_USER_JWT.getCode()).build();
//            }

//            if (postEventReq.getTitle().isBlank()) {
//                return ResponseEntity.status(EMPTY_TITLE.getCode()).build();
//            }
//
//            if(postEventReq.getDate().isBlank()){
//                return ResponseEntity.status(EMPTY_DATE.getCode()).build();
//            }

            // 중복되는 시간 있는지 확인하는 로직은 안에
            eventService.makeEvent(postEventReq);

            return new ResponseEntity<>("Good", HttpStatus.OK);
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

    @ResponseBody
    @GetMapping("/today")
    public ResponseEntity<List<GetEventsInfo>> getEventsToday(){
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetEventsInfo> eventsToday = eventProvider.getEventsToday(userIdx);
            return new ResponseEntity<>(eventsToday, HttpStatus.OK);
        }catch (BaseException e){
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
            return ResponseEntity.status(httpStatus).build();
        }
    }

    @ResponseBody
    @GetMapping("/google")
    public ResponseEntity<List<GetGoogleEvents>> getGoogleEvents(@RequestHeader("Authorization") String authorizationHeader){
         try {
             List<GetGoogleEvents> googleEvents;

             String accessToken = authorizationHeader.replace("Bearer ", "");

             googleEvents = eventService.getGoogleEvents(accessToken);

             return new ResponseEntity<>(googleEvents, HttpStatus.OK);
         }catch (BaseException e){
             HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().getCode());
             return ResponseEntity.status(httpStatus).build();
         }
    }

}
