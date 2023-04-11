package com.api.bizta.User;

import com.api.bizta.Review.ReviewDao;
import com.api.bizta.Review.model.GetReviewInfo;
import com.api.bizta.config.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.DATABASE_ERROR;
import static com.api.bizta.config.BaseResponseStatus.REQUESTED_DATA_FAIL_TO_EXIST;

@Service
public class UserProvider {

    private final UserDao userDao;

    @Autowired
    public UserProvider(UserDao userDao){
        this.userDao = userDao;
    }
}
