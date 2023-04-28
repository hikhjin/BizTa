//package com.api.bizta.User;
//
//import com.api.bizta.User.model.User;
//import com.api.bizta.config.BaseException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import static com.api.bizta.config.BaseResponseStatus.*;
//
//@Slf4j
//@Service
//public class UserProvider {
//
//    private final UserDao userDao;
//
//    @Autowired
//    public UserProvider(UserDao userDao) {
//        this.userDao = userDao;
//    }
//
//    public User retrieveByEmail(String email) throws BaseException {
//        if (checkEmail(email) == 0)
//            throw new BaseException(USERS_EMPTY_USER_EMAIL);
//        try {
//            return userDao.selectByEmail(email);
//        } catch (Exception e) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//    }
//
//    public User retrieveById(Long userIdx) throws BaseException {
//        if (checkUserIdx(userIdx) == 0)
//            throw new BaseException(USERS_EMPTY_USER_ID);
//        try {
//            return userDao.selectById(userIdx);
//        } catch (Exception e) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public int checkEmail(String email) throws BaseException {
//        try {
//            return userDao.checkEmail(email);
//        } catch (Exception exception) {
//            log.warn(exception.getMessage());
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public int checkId(String id) throws BaseException{
//        try{
//            return userDao.checkId(id);
//        }catch (Exception e){
//            log.warn(e.getMessage());
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//
//    public int checkUserIdx(Long userIdx) throws BaseException {
//        try {
//            return userDao.checkUserIdx(userIdx);
//        } catch (Exception exception) {
//            log.warn(exception.getMessage());
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//}

package com.api.bizta.User;

import com.api.bizta.Review.ReviewDao;
import com.api.bizta.Review.model.GetReviewInfo;
import com.api.bizta.config.BaseException;
import com.api.bizta.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.*;

@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService){
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    // 아이디 중복 확인
    public int checkId(String id) throws BaseException {
        try{
            return userDao.checkId(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 유저 존재 여부 확인
    public int checkUserExist(int userIdx) throws BaseException{
        //validation check
        try{
            return userDao.checkUserExist(userIdx);
        } catch (Exception exception){
            throw new BaseException(EMPTY_ID);
        }
    }

    // 이메일 중복 확인
    public int checkEmail(String email) throws BaseException {
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}