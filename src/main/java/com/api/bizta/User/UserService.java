//package com.api.bizta.User;
//
//import com.api.bizta.User.model.User;
//import com.api.bizta.config.BaseException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//import static com.api.bizta.config.BaseResponseStatus.DATABASE_ERROR;
//import static com.api.bizta.config.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;
//
//@Slf4j
//@Service
//public class UserService {
//
//    private final UserProvider userProvider;
//    private final UserDao userDao;
//
//    @Autowired
//    public UserService( UserProvider userProvider, UserDao userDao) {
//        this.userProvider = userProvider;
//        this.userDao = userDao;
//    }
//
//    public User createUser(User user) throws BaseException {
//        if (userProvider.checkEmail(user.getEmail()) == 1)
//            throw new BaseException(POST_USERS_EXISTS_EMAIL);
//        try {
//            return this.userDao.insertUser(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//    }
//
//}
//
package com.api.bizta.User;

import com.api.bizta.User.model.*;
import com.api.bizta.config.BaseException;
import com.api.bizta.utils.JwtService;
import com.api.bizta.utils.SHA256;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.api.bizta.config.BaseResponseStatus.*;

@Service
public class UserService {
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }


    // user 회원가입
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 이메일 중복 확인
        if(userProvider.checkEmail(postUserReq.getEmail()) == 1){
            throw new BaseException(EXISTS_EMAIL);
        }
        // 아이디 중복 확인
        if(userProvider.checkId(postUserReq.getId()) == 1){
            throw new BaseException(EXISTS_ID);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            System.out.println("service userIdx: " + userIdx);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt, userIdx);

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // user 로그인
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        GetUserInfo getUserInfo = userDao.getUserInfo(postLoginReq);

        String pwd;
        try {
            //암호화
            pwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        // 비밀번호 확인
        if (!pwd.equals(getUserInfo.getPassword())) {
            throw new BaseException(FAILED_TO_LOGIN);
        }

        if (!getUserInfo.getPassword().equals(pwd)) {
            // jwt userIdx 확인
            if (getUserInfo.getUserIdx() != jwtService.getUserIdx()) {
                throw new BaseException(FAILED_TO_LOGIN);
            }
        }
        return new PostLoginRes(getUserInfo.getUserIdx(), getUserInfo.getId(),
                getUserInfo.getEmail(), getUserInfo.getNickName());
    }
}
