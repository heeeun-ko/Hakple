<p align="center">
  <img src="images/logo.png" width="150" />
</p>

<h1 align="center">Hakple</h1>

> 학원생을 위한 커뮤니티 플랫폼

**Hakple**는 학원 수강생들이 소통하고, 질문하고, 정보를 공유할 수 있는 커뮤니티 플랫폼입니다.  
주요 기능으로는 회원가입, 게시판(질문/인기), 공지사항 열람, 댓글/좋아요 기능과, 실시간 알림 서비스 등이 있습니다.

## 👥 팀원 소개

 | 박주호 | 김명수 | 고희은 | 도상원 | 황지윤 |
 |:--------:|:--------:|:--------:|:--------:|:--------:|
 | <img src="https://github.com/JAWSP.png" alt="박주호" width="150"> | <img src="https://github.com/Kim-ms527.png" alt="김명수" width="150"> | <img src="https://github.com/heeeun-ko.png" alt="고희은" width="150"> | <img src="https://github.com/dark2138.png" alt="도상원" width="150"> | <img src="https://github.com/jiyuuuuun.png" alt="황지윤" width="150"> |
 | BE | BE | BE | BE | BE |
 | 인증, 인가 | 유저 등록 | 유저 프로필, 배포 | 게시글 | 댓글, 관리자 |
 | [GitHub](https://github.com/JAWSP) | [GitHub](https://github.com/Kim-ms527) | [GitHub](https://github.com/heeeun-ko) | [GitHub](https://github.com/dark2138) | [GitHub](https://github.com/jiyuuuuun) |

---

## 📸 UI 스크린샷

- 메인 페이지 화면

  <p align="center">
    <img width="1251" alt="메인 페이지 화면" src="https://github.com/user-attachments/assets/265aac14-88cc-4774-9f2c-c2cdfc1bceb2" />
  </p>

<details>
  <summary>홈 페이지 화면</summary>

  <p align="center">
    <img width="1251" alt="홈 페이지 화면" src="https://github.com/user-attachments/assets/a48552c1-e9c3-40fe-823d-824ee4e7d524" />
  </p>
</details>

<details>
  <summary>로그인 & 회원가입 화면</summary>

  <div align="center">
    <img width="600" alt="로그인 화면" src="https://github.com/user-attachments/assets/7b68630f-74ec-47b1-85f5-b0b613621a85" />
    <img width="600" alt="회원가입 화면" src="https://github.com/user-attachments/assets/0994a336-3164-4e3d-b34c-c0425abaabc8" />
  </div>
</details>

<details>
  <summary>글쓰기 페이지 & 알림 표시 화면 </summary>

  <p align="center">
    <img width="1251" alt="글쓰기 페이지 & 알림 표시 화면" src="https://github.com/user-attachments/assets/25271109-a443-40f0-b640-832b86605dfc" />
  </p>
</details>

<details>
  <summary>상세 글 페이지 화면 </summary>

  <p align="center">
    <img width="1251" alt="상세 글  페이지 화면" src="https://github.com/user-attachments/assets/3e7e442d-bfce-439b-b418-90a35d62ed3f" />
  </p>
</details>

<details>
  <summary>게시물 페이지 화면</summary>

  <p align="center">
    <img width="1251" alt="게시물 페이지 화면" src="https://github.com/user-attachments/assets/aa368789-dce8-4a25-b904-7c82f8fccc1a" />
  </p>
</details>

<details>
  <summary>캘린더 페이지 화면</summary>

  <p align="center">
    <img width="1251" alt="캘린더 페이지 화면" src="https://github.com/user-attachments/assets/63ff4ce3-76eb-41da-b0ce-ca3d967af17e" />
  </p>
</details>

<details>
  <summary>마이 페이지 화면</summary>

  <p align="center">
    <img width="1251" alt="마이 페이지 화면" src="https://github.com/user-attachments/assets/e6183e6a-f698-4952-80c8-db63b67a09b2" />
  </p>
</details>

<details>
  <summary>관리자 대시보드 화면</summary>

  <p align="center">
    <img width="1251" alt="마이 페이지 화면" src="https://github.com/user-attachments/assets/d9fb5eef-e43a-4bd2-a273-31d9568e9f94" />
  </p>
</details>


---

## 🛠️ 주요 기능

- **회원 관리**: 회원가입, 로그인, 프로필 수정
- **게시판**: 질문/답변, 자유게시판 기능
- **공지사항**: 학원 공지사항 등록 및 열람
- **댓글 및 좋아요**: 소통 강화
- **알림 기능**: 새로운 소식 실시간 알림

---

## 🏗️ 기술 스택

| 분야 | 기술 |
|:---|:---|
| Backend | Java 21, Spring Boot |
| Frontend | JavaScript, React, Next.js |
| Database | MySQL, Redis, S3 |
| DevOps | Docker, Terraform, AWS (EC2, RDS) |
| CI/CD | GitHub Actions |

---

## 🚀 Getting Started

### Backend (Spring Boot)
```bash
# 1. 프로젝트 클론
git clone https://github.com/golden-dobakhe/hakple.git
cd hakple/backend

# 2. 환경변수 설정
cp src/main/resources/application.yml.example src/main/resources/application.yml
# (application.yml을 자신의 DB/환경에 맞게 수정하세요)

# 3. 서버 실행
./gradlew bootRun
```
### Frontend (Next.js)
```bash
cd hakple/frontend

# 의존성 설치
npm install

# 개발 서버 실행
npm run dev

```
---

## 🛠️ 주요 기능 상세

- 회원 관리
  - 회원가입 / 로그인 (JWT 인증)
  - 프로필 수정 (휴대폰 번호, 프로필 이미지 변경)
- 게시판
  - 자유 게시판 / 인기 게시판 분리
  - 글 작성, 수정, 삭제
- 공지사항
  - 관리자 공지 등록
  - 사용자 공지 열람
- 댓글 / 좋아요
  - 게시글 댓글 작성 및 삭제
  - 게시글 좋아요 기능
- 알림
  - 댓글, 좋아요, 공지사항에 대한 실시간 알림
- 캘린더
  - 사용자 맞춤 캘린더 제공
  - 일정 추가,삭제,수정 기능
  - 일정 알림 기능
- 관리자
  - 학원 관리
  - 관리자 관리
  - 회원 목록 조회 등등
 
---

## :open_file_folder: Project Structure

```markdown
backend
└── src
    └── main
        └── java
            └── com
                └── golden_dobakhe
                    └── HakPle
                        ├── config           # AWS S3 설정 등
                        ├── domain           # 핵심 도메인 로직
                        │   ├── notification # 알림
                        │   ├── post         # 게시글 (board, comment, like, report)
                        │   ├── resource     # 리소스 (image)
                        │   └── user         # 사용자 (admin, exception, myInfo, user)
                        ├── global           # 전역 설정 (entity, exception, Status enum 등)
                        └── security         # Spring Security 관련 (config, controller, dto, exception, jwt, OAuth, service, utils
```

---
## 🧱 Git 컨벤션

### 📍 브랜치 전략

- **main**: 운영 배포용 브랜치  
- **develop**: 다음 출시 버전을 개발하는 브랜치 (오류 없는 코드만 push)  
- **기능 브랜치 규칙**:
```ardunio
master
develop-fe
develop-be
release/*
hotfix/*
feat/{issue-number}-{feature-name}
```

**예시:**  
`feat/#27-게시판 CRUD`

---

### 📌 Git-Flow 전략

| 브랜치    | 설명                                |
|-----------|-------------------------------------|
| `main`    | 제품 출시용 안정화 브랜치           |
| `develop` | 통합 개발 브랜치 (기능 병합 후 테스트) |
| `feat/*`  | 기능 개발 브랜치 (develop에서 분기)  |

---

### 📍 커밋 메시지 컨벤션
```php-template
<타입>: <이슈번호(optional)> <변경 요약>
```

**예시:**  
`Feat: #3 게시판 생성 기능 추가`

---

### ✅ 커밋 타입 목록

| 타입       | 설명                              |
|------------|-----------------------------------|
| `Feat`     | 새로운 기능 추가                  |
| `Fix`      | 버그 수정                         |
| `Docs`     | 문서 수정                         |
| `Style`    | 코드 포맷 수정 (세미콜론 등)     |
| `Refactor` | 리팩토링 (기능 변경 없음)         |
| `Test`     | 테스트 코드 추가/수정             |
| `Chore`    | 빌드 설정, 패키지 등 기타 변경    |
| `Remove`   | 사용하지 않는 코드/파일 제거      |
| `Rename`   | 파일 또는 폴더명 변경             |

---

 
## 🌐 배포 주소
- [Hakple 웹사이트 링크](https://www.hakple.site)

---

## 🎥 데모 영상
- [Hakple 데모 영상](https://youtu.be/fph2-jl0f7Q)



멋쟁이사자처럼 부트캠프 백엔드JAVA 프로젝트입니다
