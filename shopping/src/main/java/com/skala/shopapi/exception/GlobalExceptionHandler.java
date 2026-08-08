/*
전역 예외 처리 핸들러
작성자 : 정희중
작성일 : 2026-08-08

컨트롤러에서 빠져나온 예외를 가로채 Response 형태로 변환한다.
HTTP 상태 코드는 여기서 결정된다.

1. ResponseException  : Error enum 에 정의된 상태 코드로 응답
2. ParameterException : 400, 누락/유효하지 않은 필드명을 메시지로 전달
3. Exception          : 그 외 전부. 스프링이 상태를 정해둔 예외는 그 상태를 따르고,
                        정말 예상 밖의 예외만 로그를 남기고 500 으로 응답
*/

package com.skala.shopapi.exception;

import com.skala.shopapi.common.Response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 의도적으로 던진 예외 - Error enum 의 상태 코드를 그대로 사용
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<Response<Void>> handleResponseException(ResponseException e) {
        return ResponseEntity
                .status(e.getError().getStatus())
                .body(Response.fail(e.getError()));
    }

    // 파라미터 검증 실패 - 어떤 필드가 문제인지 메시지에 담겨 있음
    @ExceptionHandler(ParameterException.class)
    public ResponseEntity<Response<Void>> handleParameterException(ParameterException e) {
        return ResponseEntity
                .status(Error.INVALID_PARAMETER.getStatus())
                .body(Response.fail(e.getMessage()));
    }

    // 그 외 전부
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleUnexpected(Exception e) {
        // 405, 400 등 스프링이 이미 상태를 정해준 예외는 500 으로 덮지 않게함
        if (e instanceof ErrorResponse errorResponse) {
            return ResponseEntity
                    .status(errorResponse.getStatusCode())
                    .body(Response.fail(e.getMessage()));
        }

        log.error("처리되지 않은 예외 발생", e);
        return ResponseEntity
                .status(Error.INTERNAL_ERROR.getStatus())
                .body(Response.fail(Error.INTERNAL_ERROR));
    }
}
