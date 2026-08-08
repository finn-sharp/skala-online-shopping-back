/*
예외처리 에러 Enum
작성자 : 정희중
작성일 : 2026-08-08

Enum 종류
1. Not_Found : 요청한 데이터 없음
2. Data_Duplicated : 이미 존재하는 데이터
3. Invalid_Parameter : 잘못된 요청값
4. Internal_Error : 서버 내부 오류
*/

package com.skala.shopapi.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Error {
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 데이터를 찾을 수 없습니다."),
    DATA_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 데이터입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 요청값입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}