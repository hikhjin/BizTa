//package com.api.bizta.User;
//import com.api.bizta.User.dto.GetUserRes;
//import com.api.bizta.User.model.User;
//import com.api.bizta.config.BaseException;
//import com.api.bizta.config.BaseResponse;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Slf4j
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    private final UserProvider userProvider;
//
//    @Autowired
//    public UserController(UserProvider userProvider) {
//        this.userProvider = userProvider;
//    }
//
//    @GetMapping("")
//    public BaseResponse<GetUserRes> getProfile(HttpServletRequest request) {
//        try {
//            Long userIdx = Long.parseLong(request.getAttribute("userIdx").toString());
//            User user = userProvider.retrieveById(userIdx);
//            GetUserRes getUserRes = new GetUserRes(user.getNickname(), user.getId(), user.getEmail());
//            return new BaseResponse<>(getUserRes);
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }
//}
package com.api.bizta.User;

import com.api.bizta.User.model.*;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.api.bizta.config.BaseResponseStatus.*;
import static com.api.bizta.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;
    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // 유저 회원가입
    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // 이메일 빈칸 확인
        if (postUserReq.getEmail() == null) {
            return new BaseResponse<>(EMPTY_EMAIL);
        }
        // 아이디 빈칸 확인
        if (postUserReq.getId() == null) {
            return new BaseResponse<>(EMPTY_ID);
        }
        // 닉네임 빈칸 확인
        if (postUserReq.getNickName() == null) {
            return new BaseResponse<>(EMPTY_NICKNAME);
        }
        // 비밀번호 빈칸 확인
        if (postUserReq.getPassword() == null) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }
        // 이메일 정규표현식 확인 ( email@~.~ )
        if (!isRegexEmail(postUserReq.getEmail())) {
            return new BaseResponse<>(INVALID_EMAIL);
        }

        // 이메일 중복 확인은 [Service - Provider - Dao] 에서 합니다.
        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}