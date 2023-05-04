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
import com.api.bizta.User.model.GetGoogleInfo;
import com.api.bizta.User.model.GetTokenRes;
import com.api.bizta.config.BaseException;
import com.api.bizta.utils.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.*;

@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService, Environment env){
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.env = env;
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

    public GetGoogleInfo getGoogleInfo(String method, String code, String registrationId) {
        String accessToken = getAccessToken(method, code, registrationId);

        JsonNode userResourceNode = getUserResource(accessToken, registrationId);

        String id = userResourceNode.get("id").asText();
        String nickname = userResourceNode.get("name").asText();
        // 편의?를 위해 구글 회원가입할 때 비밀번호는 email로 설정함
        String password = userResourceNode.get("email").asText();
        String email = userResourceNode.get("email").asText();
        GetTokenRes getTokenRes = new GetTokenRes("Bearer", accessToken);

        return new GetGoogleInfo(id, nickname, password, email, getTokenRes);
    }

    private String getAccessToken(String method, String authorizationCode, String registrationId) {
        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
        String redirectUri = "";
        if(method.equals("login")){
            redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri-login");
        }else{
            redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri-auth");
        }
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}