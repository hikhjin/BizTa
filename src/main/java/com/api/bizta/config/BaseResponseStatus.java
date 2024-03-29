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

    // event
    EMPTY_TITLE(false, 2014, "이벤트 제목을 입력해주세요."),
    EMPTY_DATE(false, 2015, "이벤트 날짜를 입력해주세요."),
    DUPLICATE_TIME(false, 2016, "같은 날에 중복되는 시간이 있습니다."),
    DELETE_FAIL_EVENT(false, 2017, "이벤트 삭제에 실패했습니다."),

    // review
    EMPTY_RATING(false, 2018, "1점부터 5점까지 평점을 매겨주세요."),
    EMPTY_CONTENT(false, 2019, "리뷰를 입력해주세요."),
    ALREADY_WRITTEN(false, 2020, "이미 리뷰를 작성하셨습니다."),


    // plan
    DELETE_FAIL_PLAN(false, 2100, "plan 삭제에 실패하였습니다."),
    MODIFY_FAIL_PLAN(false, 2101, "plan 수정에 실패하였습니다."),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    REQUESTED_DATA_FAIL_TO_EXIST(false, 4002, "요청한 데이터가 존재하지 않습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),

//    //[PATCH] /users/{userIdx}
//    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
//
//    DELETE_USER_FAIL(false, 4008, "유저 정보 삭제에 실패하였습니다."),
//
//    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
//    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    GOOGLE_EVENTS_IMPORT_ERROR(false, 5000, "구글 이벤트 조회에 실패하였습니다.");

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
