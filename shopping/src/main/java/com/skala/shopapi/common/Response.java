/*
API 공통 응답 템플릿
작성자 : 정희중
작성일 : 2026-08-08

구성
1. success  : 요청 성공 여부
2. message  : 실패 사유 (성공 시 응답에서 생략됨)
3. data     : 응답 본문 (없을 시 응답에서 생략됨)

사용
- 성공(데이터 O) : Response.success(productDto)
- 성공(데이터 X) : Response.success()
- 실패(Error)    : Response.fail(Error.NOT_FOUND)
- 실패(직접 작성) : Response.fail("productName: 상품명은 필수입니다")

주의
- HTTP 상태 코드는 이 클래스가 아닌 ResponseEntity 가 결정.
  실패 응답은 컨트롤러에서 직접 만들지 말고 예외를 던져
  GlobalExceptionHandler 가 상태 코드와 함께 처리하도록 해야함.
*/

package com.skala.shopapi.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skala.shopapi.exception.Error; // java.lang.Error 와 이름이 겹치므로 반드시 명시적으로 import

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private final boolean success;
    private final String message;
    private final T data;

    private Response(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 성공 - 데이터 있음
    public static <T> Response<T> success(T data) {
        return new Response<>(true, null, data);
    }

    // 성공 - 데이터 없음 (예: 삭제 완료)
    public static Response<Void> success() {
        return new Response<>(true, null, null);
    }

    // 실패 - Error 상수 기반
    public static Response<Void> fail(Error error) {
        return new Response<>(false, error.getMessage(), null);
    }

    // 실패 - 직접 작성한 메시지 (검증 실패 등 상황별 메시지가 필요할 때)
    public static Response<Void> fail(String message) {
        return new Response<>(false, message, null);
    }
}
