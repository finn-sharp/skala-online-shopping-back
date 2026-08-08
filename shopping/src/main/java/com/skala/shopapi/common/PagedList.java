/*
페이징 목록 응답 템플릿
작성자 : 정희중
작성일 : 2026-08-08

구성
1. items      : 조회된 목록
2. totalCount : 페이징 이전 전체 건수
3. offset     : 요청한 시작 위치
4. count      : 요청한 개수
5. hasNext    : 다음 페이지 존재 여부

사용
- PagedList.of(page, offset, count)

참고
- Spring Data 의 Page 를 그대로 응답으로 내보내면 pageable, sort 등
  라이브러리 내부 구조가 그대로 노출되고 버전에 따라 형태가 바뀐다.
  이 클래스로 한 겹 감싸 응답 형태를 우리가 통제한다.
- findAll(Pageable) 은 Page 를 만들면서 COUNT 쿼리를 이미 실행하므로
  totalCount 를 담는다고 해서 쿼리가 늘지는 않는다.
*/

package com.skala.shopapi.common;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PagedList<T> {

    private final List<T> items;
    private final long totalCount;
    private final int offset;
    private final int count;
    private final boolean hasNext;

    private PagedList(List<T> items, long totalCount, int offset, int count, boolean hasNext) {
        this.items = items;
        this.totalCount = totalCount;
        this.offset = offset;
        this.count = count;
        this.hasNext = hasNext;
    }

    // 조회 결과(Page)와 요청값(offset, count)으로 생성
    public static <T> PagedList<T> of(Page<T> page, int offset, int count) {
        return new PagedList<>(
                page.getContent(),
                page.getTotalElements(),
                offset,
                count,
                page.hasNext());
    }
}
