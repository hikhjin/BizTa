package com.api.bizta.Event;

import com.api.bizta.Event.model.*;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.utils.JwtService;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("")
    public BaseResponse<PostEventRes> makeEvent(@RequestBody PostEventReq postEventReq){

        try{
            if(postEventReq.getUserIdx() != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (postEventReq.getTitle().isBlank()) {
                return new BaseResponse<>(EMPTY_TITLE);
            }

            if(postEventReq.getDate().isBlank()){
                return new BaseResponse<>(EMPTY_DATE);
            }

            // 중복되는 시간 있는지 확인하는 로직은 안에
            PostEventRes postEventRes = eventService.makeEvent(postEventReq);
            return new BaseResponse<>(postEventRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 이벤트 조회
    @ResponseBody
    @GetMapping("/{eventIdx}")
    public BaseResponse<GetEventInfo> getEventInfo(@PathVariable("eventIdx") int eventIdx) {
        try {
            GetEventInfo eventInfo = eventProvider.getEventInfo(eventIdx);
            return new BaseResponse<>(eventInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{eventIdx}")
    public BaseResponse<String> modifyEvent(@PathVariable("eventIdx") int eventIdx, @RequestBody PatchEventReq patchEventReq){
        try{
            if(patchEventReq.getUserIdx() != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (patchEventReq.getTitle().isBlank()) {
                return new BaseResponse<>(EMPTY_TITLE);
            }

            if(patchEventReq.getDate().isBlank()){
                return new BaseResponse<>(EMPTY_DATE);
            }

            String eventRes = eventService.modifyEvent(eventIdx, patchEventReq);
            return new BaseResponse<>(eventRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
