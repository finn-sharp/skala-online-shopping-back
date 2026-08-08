package com.skala.shopapi.exception;

import lombok.Getter;

@Getter
public class ParameterException extends RuntimeException {
    private final String[] fields;

    public ParameterException(String... fields) {
        super("필수 파라미터가 누락되었거나 유효하지 않습니다: " + String.join(", ", fields));
        this.fields = fields;
    }
}