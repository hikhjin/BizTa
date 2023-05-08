
package com.api.bizta.User;

import com.api.bizta.User.model.*;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.utils.JwtService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;

import static com.api.bizta.config.BaseResponseStatus.*;
import static com.api.bizta.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserProvider userProvider;
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
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
            PostUserRes postUserRes;
            postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 일반 login
    @ResponseBody
    @PostMapping("/log-in")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq) {
        try {
            if (postLoginReq.getId() == null || postLoginReq.getPassword() == null) {
                return new BaseResponse<>(FAILED_TO_LOGIN);
            }
            PostLoginRes loginUser = userService.login(postLoginReq);
            return new BaseResponse<>(loginUser);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // google login
    @GetMapping("/{method}/google")
    public RedirectView googleLoginUri(@PathVariable String method){
        String url = "https://accounts.google.com/o/oauth2/auth?client_id=356448383900-kt44mvojcmdqji36q3ad66r8gtp6am5r.apps.googleusercontent.com&";
        url += "redirect_uri=http://localhost:8080/users/" + method + "/oauth2/code/google&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile%20https://www.googleapis.com/auth/calendar";

        return new RedirectView(url);
    }

    // google login
    @GetMapping(value = "/{method}/oauth2/code/{registrationId}", produces = "application/json")
    public BaseResponse<PostLoginRes> oauthLogin (@PathVariable String method, @RequestParam String code, @PathVariable String registrationId) {
        GetGoogleInfo getGoogleInfo = userProvider.getGoogleInfo(method, code, registrationId);

        PostLoginRes postLoginRes;
        if(method.equals("login")){
            PostLoginReq postLoginReq = new PostLoginReq(getGoogleInfo.getId(), getGoogleInfo.getPassword());
            try {
                postLoginRes = userService.login(postLoginReq);
                postLoginRes.setGetTokenRes(getGoogleInfo.getGetTokenRes());
                return new BaseResponse<>(postLoginRes);
            } catch (BaseException e) {
                return new BaseResponse<>(e.getStatus());
            }
        }

        postLoginRes = new PostLoginRes(getGoogleInfo.getId(), getGoogleInfo.getPassword(), getGoogleInfo.getEmail(), getGoogleInfo.getNickName(), getGoogleInfo.getGetTokenRes());
        return new BaseResponse<>(postLoginRes);
    }
}