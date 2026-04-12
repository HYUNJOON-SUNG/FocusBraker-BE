# FocusBraker

공부 중 방해 요소를 화면 위에 띄워주는 스터디 오버레이 앱의 백엔드 서버입니다.

## 기술 스택

- Java 17 / Spring Boot 4.0.5
- MySQL / Spring Data JPA / Hibernate
- Lombok
- SpringDoc OpenAPI (Swagger UI)
- Gradle

## 프로젝트 구조

```
com.gachon_likelion.focusbraker/
├── FocusbrakerApplication.java
├── domain/
│   ├── user/           # 유저 (device UUID 기반)
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── service/
│   ├── session/        # 오버레이 세션 + 리포트
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── service/
│   └── event/          # 방해 이벤트
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── repository/
│       └── service/
└── global/
    ├── common/         # BaseEntity, ApiResponse
    ├── config/         # JpaConfig, SwaggerConfig
    ├── enums/          # DistractionType, SessionStatus
    └── exception/      # CustomException, GlobalExceptionHandler
```

## 실행 방법

### 1. 환경변수 설정

`.env.example`을 복사해서 `.env`를 생성하고 본인 환경에 맞게 수정:

```bash
cp .env.example .env
```

```env
DB_URL=jdbc:mysql://localhost:3306/focusbraker
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

### 2. MySQL 데이터베이스 생성

```sql
CREATE DATABASE focusbraker;
```

### 3. 빌드 및 실행

```bash
./gradlew build
./gradlew bootRun
```

### 4. Swagger UI

서버 실행 후 http://localhost:8080/swagger-ui.html 접속

## 의존성

| 의존성 | 용도 |
|--------|------|
| spring-boot-starter-data-jpa | JPA / Hibernate |
| spring-boot-starter-webmvc | REST API |
| spring-boot-starter-validation | DTO 유효성 검증 |
| springdoc-openapi-starter-webmvc-ui | Swagger UI |
| lombok | 보일러플레이트 제거 |
| mysql-connector-j | MySQL 드라이버 |
