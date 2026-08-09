# Skala Online Shopping - Backend

간단한 쇼핑 백엔드 서비스(SPRING BOOT)입니다. 이 저장소는 실무 수준의 개발자 문서를 기준으로 구성되어 있으며, 로컬 개발, 테스트, 배포에 필요한 가이드와 예제를 포함합니다.

## 목차
- 프로젝트 개요
- 특징
- 기술 스택
- 요구 사항
- 빠른 시작
- 설정 및 환경변수
- 데이터베이스 및 초기 데이터
- 주요 API 예제
- 테스트
- 배포 가이드
- 기여 가이드
- 라이선스

## 프로젝트 개요

`shopping` 폴더는 Spring Boot 기반의 RESTful 백엔드 애플리케이션입니다. 고객, 상품, 주문 관련 엔드포인트와 서비스/레포지토리 계층을 포함하며, 간단한 인증 및 권한(Role) 기반 처리를 지원합니다.

컨트롤러 주요 위치: [src/main/java/com/skala/shopapi/controller](src/main/java/com/skala/shopapi/controller)

## 특징

- RESTful API 설계
- 계층화된 서비스 구조 (`controller` → `service` → `repository`)
- 환경별 프로파일(application-dev.yml / application-prod.yml)
- 샘플 데이터 스크립트 포함

## 기술 스택

- Java 21+ (프로젝트 설정에 맞게 조정)
- Spring Boot
- Gradle (wrapper 포함)

## 요구 사항

- JDK 21 이상
- Gradle wrapper 사용 (프로젝트에 포함)
- 로컬 DB(MySQL, PostgreSQL 등) — `application-*.yml`에 맞게 설정

## 빠른 시작

루트에서 빌드 및 실행(개발용):

```bash
cd shopping
./gradlew build
./gradlew bootRun
```

또는 빌드된 JAR으로 실행:

```bash
./gradlew bootJar
java -jar build/libs/*-SNAPSHOT.jar
```

서버 기본 포트는 `8080`입니다 (환경에 따라 변경 가능). 설정 파일: [src/main/resources/application.yml](src/main/resources/application.yml)

## 설정 및 환경변수

환경별 설정 파일(예):
- `src/main/resources/application.yml` (공통)
- `src/main/resources/application-dev.yml` (개발)
- `src/main/resources/application-prod.yml` (운영)

중요 환경변수/설정 예시:

- `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password` — DB 연결 설정
- `spring.profiles.active` — 활성 프로파일 (dev/prod)

프로덕션 배포 전에는 `application-prod.yml`을 적절히 구성하세요.

## 데이터베이스 및 초기 데이터

프로젝트에는 샘플 SQL 파일이 포함되어 있습니다: [src/main/resources/sample.sql](src/main/resources/sample.sql) 및 [src/main/resources/admin.sql](src/main/resources/admin.sql).

로컬에서 테스트 데이터가 필요하면 위 SQL을 DB에 적용하세요.

## 주요 API 예제

다음은 일부 엔드포인트 예제입니다. (더 많은 엔드포인트는 컨트롤러 소스 참조)

- 고객 생성 (예시):

```bash
curl -X POST \\
  http://localhost:8080/api/customers \\
  -H 'Content-Type: application/json' \\
  -d '{
    "customerId": "test1",
    "customerPassword": "string",
    "customerPoint": 0.1,
    "role": "USER"
  }'
```

- 로그인 및 인증 등의 예제는 `controller`와 관련 DTO를 확인하세요: [src/main/java/com/skala/shopapi/data/dto](src/main/java/com/skala/shopapi/data/dto)

## 테스트

유닛/통합 테스트 실행:

```bash
cd shopping
./gradlew test
```

테스트 리포트 및 결과는 `build/reports/tests` 경로를 확인하세요.

## 배포 가이드 (간단)

1. `application-prod.yml`을 준비하고, 필요한 환경변수를 서버에 설정합니다.
2. `./gradlew bootJar`로 JAR 생성
3. 시스템 서비스(예: systemd) 또는 컨테이너로 실행

Docker 예시(간단):

```dockerfile
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
```

## 기여 가이드

1. 이슈(버그/기능)를 생성합니다.
2. 브랜치 생성: `feature/<간단한-설명>`
3. 코드 스타일을 유지하고, 테스트를 추가하세요.
4. PR을 생성하고 작업 내용을 설명하세요.

## 라이선스
MIT
