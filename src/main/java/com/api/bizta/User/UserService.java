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
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService, Environment env) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
        this.env = env;
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
