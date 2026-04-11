# HacBokGS_web 🍱  
남서울대학교 학식·매점 통합 서비스 관리자 웹

> **HacBokGS_web** 은 남서울대학교 학식/매점 통합 앱(**HacBokGS**)을  
> 운영하기 위한 **관리자 전용 웹 대시보드**입니다.  
> 이 프로젝트는 졸업 작품 중 **관리자 웹 파트**이며, 본인은 이 웹을 **단독 설계·개발**했습니다.

---

## 📌 프로젝트 개요

- **프로젝트명**: HacBokGS_web (HacBokGS 관리자 웹)
- **역할**:  
  - 학식/매점 운영 담당자, 학복관(학생복지관) 관리자, 학교 관계자가 사용하는 **백오피스 도구**
- **핵심 목표**
  - 남서울대학교 학생용 앱(HacBokGS)에 노출되는 **메뉴, 주문, 광고, 게시판, 혼잡도**를  
    별도 설치 없이 **브라우저에서 관리**할 수 있도록 지원
  - 별도 서버 없이 **Firebase(Firestore + Storage)** 만으로 관리 시스템 구성

---

## ✨ 주요 기능

### 1. 사용자 관리 (`user.html`)

- Firestore `users` 컬렉션 조회
- 가입자 목록(이메일, 이름, 학번 등)을 표 형태로 확인
- 운영자는 전체 사용자 현황을 한눈에 파악 가능

---

### 2. 주문 관리

#### 2-1. 전체 주문 관리 (`jumun.html`)

- 모든 매장의 주문 내역을 한 화면에서 조회
- 주문별 정보 표시
  - 주문 ID, 사용자, 매장, 주문 시간, 메뉴 요약, 상태(접수/조리중/완료)
- 주문 상태 변경
  - 조리 진행에 따라 상태를 `접수 → 조리중 → 완료` 로 변경
  - 완료 처리 시, 학생 앱에서 **조리 완료 팝업**과 연동

#### 2-2. 매장별 주문 관리 (사장님 페이지, 예: `kkuihankki/kkui_boss.html`)

- 특정 매장(예: 꾸이한끼, 태산김치찜 등)의 주문만 필터링해서 표시
- 매장별 전용 화면에서 주문 상태 관리 (사장님/매니저용)
- 날짜별, 주문별 리스트로 간단하고 빠르게 처리 가능

---

### 3. 광고 관리 (`advertising.html`)

- 앱 메인 화면에 노출되는 **배너 광고** 관리
- 기능
  - 광고 등록: 제목, 링크(URL), 만료일, 이미지 업로드
  - 광고 목록 조회: 진행 중/종료된 광고 확인
  - 수정/삭제
- 광고 데이터 구조
  - 진행 중: `advertising/ing/posts/{adId}`
  - 종료된 광고: `advertising/end/posts/{adId}`
- 만료일이 지난 광고는 **자동으로 종료 영역으로 이동**하는 로직 포함

---

### 4. 게시판 관리 (`bullentin_board.html`)

- 학생들이 앱에서 작성한 **Q&A / 건의사항** 게시글 관리
- 기능
  - 카테고리별 게시글 목록 조회
  - 제목/작성자/작성일/조회수/댓글 수 확인
  - 부적절한 게시글 삭제(모더레이션)

---

### 5. 오늘의 메뉴 관리 (`todaymenu.html` 및 매장별 메뉴 페이지)

- 매장별 **당일 판매 메뉴**를 등록/수정/삭제
- 기능
  - 매장 선택 후 오늘의 메뉴 목록 조회
  - 메뉴 추가 (메뉴명, 가격, 카테고리, 설명, 이미지 등)
  - 메뉴 수정 및 삭제
- 예:  
  - `taesankimchijjim/taesan_del.html`  
    → 태산김치찜 매장의 메뉴 삭제 전용 화면

---

### 6. 혼잡도 모니터링 (`congestion.html`)

- 학식관/매장의 **실시간 혼잡도** 확인
- Firestore `store_order/congestion/{date}/eating` 문서 기반
- 기능
  - 현재 이용 인원(`currentPeople`) 표시
  - 혼잡도 단계 (여유/보통/혼잡/만석 등) 표시
  - 운영자가 시간대별 혼잡도 흐름을 파악해 인원 배치/운영 전략에 활용

---

## 🏗 아키텍처 & 데이터 구조

### 1. 전반 구조

```
flowchart LR

    subgraph AdminClient[Client]
        W[HacBokGS_web<br/>관리자 웹 (HTML+JS)]
    end

    subgraph Firebase[Firebase Backend]
        F[Cloud Firestore]
        S[Firebase Storage]
    end

    W <--> F
    W <--> S
백엔드 서버 없음

모든 데이터는 Firebase Firestore / Storage를 통해 직접 읽고 쓰기

앱(HacBokGS)과 데이터 공유

모바일 앱에서 사용하는 컬렉션을 그대로 관리자 웹에서 함께 사용

```
### 2. 핵심 Firestore 컬렉션
```
컬렉션 경로	설명
users/{userId}	사용자 기본 정보
users/{userId}/orders/{orderId}	사용자별 주문 내역
bulletin_board/{category}/board/{id}	게시판 글
advertising/ing/posts/{adId}	진행 중 광고
advertising/end/posts/{adId}	종료된 광고
best_menu/{dateDocId}	날짜별 베스트 메뉴 통계
order_management/{storeId}/count/{d}	매장별 대기 팀 수
store_order/congestion/{date}/eating	특정 날짜의 혼잡도(현재 인원 등)
```

### 🔧 기술 스택
```
Frontend

HTML5, CSS

순수 JavaScript

Backend

Firebase Cloud Firestore

Firebase Storage

기타

Firebase Web SDK (모듈형 / CDN)

정적 호스팅 (Firebase Hosting 또는 기타 Static Server)
```

### 📁 폴더 구조
```
HacBokGS_web/
├── master_main.html         # 관리자 대시보드(메인 허브 페이지)
├── user.html                # 사용자 관리 화면
├── jumun.html               # 전체 주문 관리 화면
├── advertising.html         # 광고 관리 화면
├── bullentin_board.html     # 게시판 관리 화면
├── todaymenu.html           # 오늘의 메뉴 메인 관리 화면
├── congestion.html          # 혼잡도 모니터링 화면
├── kkuihankki/
│   └── kkui_boss.html       # 꾸이한끼 매장 주문 관리(사장님 페이지)
├── taesankimchijjim/
│   └── taesan_del.html      # 태산김치찜 메뉴 삭제 전용 페이지
└── js/
    ├── master.js            # 공통 기능 및 Firebase 초기화/라우팅
    ├── user.js              # 사용자 목록 조회 로직
    ├── advertising.js       # 광고 등록/만료 처리 로직
    ├── congestion.js        # 혼잡도 조회/표시 로직
    └── (기타 매장/주문 관련 JS 파일들)

```

### 👤 개발자 역할 (나의 기여)
```
HacBokGS_web 은 이 졸업 작품에서 제가 단독으로 설계 · 디자인 · 개발 · 오류 수정까지 담당한 파트입니다.

기획 & 요구사항 정의

학식/매점 운영자, 학복관 관리자 입장에서
“어떤 데이터를, 어떤 화면에서, 어떻게 관리하면 편한지” 요구사항 정리

앱(HacBokGS)과의 연동을 고려해 관리자 웹 기능 범위 정의
(사용자/주문/광고/게시판/메뉴/혼잡도)

설계

관리자 대시보드(master_main.html)와 각 기능 페이지 구조 설계

Firestore 컬렉션/문서 구조를 앱과 공유하는 방식으로 설계

주문/광고/게시판/혼잡도 등 주요 기능에 대한 데이터 흐름 & UI 플로우 설계

프론트엔드 개발

HTML/CSS/JS로 관리자 UI 직접 구현

user.html, jumun.html, advertising.html, bullentin_board.html,
todaymenu.html, congestion.html 및 매장별 페이지 구현

운영자가 비개발자라는 전제 하에, 단순하고 직관적인 UI 지향

Firebase 연동

Firestore/Storage에 대한 read/write 쿼리 작성

실시간 리스너(SnapshotListener)를 활용해 주문/게시판/혼잡도 자동 반영

광고 만료 처리, 대기 팀 수 관리 등 운영 로직 구현

오류 수정 & 운영 고려

실제 동작 테스트를 통해 시나리오별 버그 수정

실수로 데이터를 잘못 삭제/수정하는 상황을 최소화하기 위한 UI/동작 개선
```
