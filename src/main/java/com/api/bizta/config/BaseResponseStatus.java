package com.api.bizta.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // user 회원가입
    EMPTY_ID(false,2004, "아이디를 입력해주세요."),
    EXISTS_ID(false,2005,"중복된 아이디입니다."),
    EMPTY_NICKNAME(false,2006,"닉네임을 입력해주세요."),
    EXISTS_NICKNAME(false,2007,"중복된 닉네임입니다."),
    EMPTY_EMAIL(false,2008, "이메일을 입력해주세요."),
    INVALID_EMAIL(false,2009, "이메일 형식을 확인해주세요."),
    EXISTS_EMAIL(false,2010,"중복된 이메일입니다."),
    FAIL_VERIFICATION_EMAIL(false,2011,"이메일 인증이 되지 않았습니다."),
    EMPTY_PASSWORD(false,2012, "비밀번호를 입력해주세요."),
    INVALID_PASSWORD(false,2013, "비밀번호 형식을 확인해주세요."),





    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    REQUESTED_DATA_FAIL_TO_EXIST(false, 4002, "요청한 데이터가 존재하지 않습니다.");



    // 5000 : 필요시 만들어서 쓰세요

    // 6000 : 필요시 만들어서 쓰세요

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
