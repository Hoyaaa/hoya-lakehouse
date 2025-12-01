# HacBokGS_package  
남서울대학교 학식·매점 통합 서비스 (모바일 앱 + 관리자 웹)

> 이 저장소는 **학생용 모바일 애플리케이션(HacBokGS)** 과  
> **운영자용 관리자 웹(HacBokGS_web)** 을 하나의 패키지로 모아 놓은 프로젝트입니다.  
> 4인 팀 졸업 작품으로 개발되었으며,  
> **모바일 앱은 팀 프로젝트(팀장으로 전 과정 참여)**,  
> **관리자 웹은 제가 단독 설계·개발**했습니다.

---

## 📁 폴더 구조

```
HacBokGS_package/
├── HacBokGS/        # 학생이 사용하는 안드로이드 애플리케이션 (Android 앱)
├── HacBokGS_web/    # 운영자가 사용하는 관리자 웹페이지 (Admin Web)
└── README.md        # 현재 문서 (통합 개요/설명)
HacBokGS/
→ 남서울대학교 학생이 사용하는 모바일 앱(안드로이드) 프로젝트

HacBokGS_web/
→ 학식/매점/학복관 관리자, 학교 담당자가 사용하는 웹 기반 관리자 대시보드
```

## 🌐 전체 서비스 개요
```
HacBokGS는 남서울대학교 구성원을 위한 캠퍼스 통합 플랫폼입니다.

학생(사용자) 는
→ HacBokGS 모바일 앱으로
→ 메뉴 조회·주문, 조리 상태 확인, 게시판, 지도, 광고, 혼잡도 등을 이용합니다.

관리자(운영자) 는
→ HacBokGS_web 관리자 웹으로
→ 사용자·주문·메뉴·광고·게시판·혼잡도 데이터를 관리/조정합니다.

두 시스템 모두 Firebase Cloud Firestore / Storage 를 공통 백엔드로 사용하며,
동일한 데이터를 실시간으로 공유합니다.
```

### 🏗 통합 아키텍처

```
    subgraph Client
        A[HacBokGS<br/>학생용 Android 앱]
        B[HacBokGS_web<br/>관리자 웹 페이지]
    end

    subgraph Backend[Firebase Backend]
        C[Cloud Firestore<br/>users, orders, bulletin_board,<br/>advertising, order_management, congestion...]
        D[Firebase Storage<br/>광고/메뉴 이미지]
    end

    A <--> C
    B <--> C
    B <--> D
    A <--> D

공통 백엔드

Cloud Firestore: 사용자, 주문, 장바구니, 게시판, 광고, 혼잡도 등 데이터 저장

Firebase Storage: 광고 이미지, 메뉴 이미지 등 파일 저장

양방향 실시간 연동

앱과 관리자 웹 모두 Firestore를 실시간 리스닝 →
한쪽에서 수정하면 다른 쪽에 바로 반영
```

### 📱 HacBokGS (모바일 앱) – 학생용
```
1. 목적
남서울대학교 학생이 한 앱 안에서:

학식/매점 메뉴 확인

주문 & 조리 상태 확인

혼잡도 확인

공지/광고/게시판 등
을 이용할 수 있는 캠퍼스 통합 앱

2. 주요 기능
Google 로그인 + Firebase Auth

학교 이메일 기반 계정 로그인

최초 로그인 시 프로필(이름, 학번 등) 입력

메뉴 조회 & 주문

매장 목록(버거, 한식, 분식 등)에서 매장 선택

매장별 메뉴 리스트, 옵션 선택

장바구니 담기 → 결제 → 주문 생성

users/{userId}/orders/{orderId} 에 주문 정보 저장

조리 상태 & 완료 팝업

storeOrders.{storeId}.complete 필드 실시간 감시

매장에서 완료 처리 시 → 앱에서 “조리 완료” 팝업 표시

popupShown 으로 중복 팝업 방지

게시판 (Q&A / 건의)

bulletin_board/{category}/board 기반

글 작성, 목록 조회, 상세 열람

광고 & 베스트 메뉴 & 혼잡도

광고: advertising/ing/posts 기반 배너 슬라이드

베스트 메뉴: best_menu/{date} 기반 인기 메뉴 표시

혼잡도: store_order/congestion/{date}/eating 기반 좌석/인원 표시

지도/길찾기

교내 위치 안내 및 간단한 경로 표시 (RouteLineView 커스텀 뷰 사용)

3. 기술 스택 (앱)
Android (Java + Kotlin 혼용)

MVVM (일부 화면: 장바구니, 주문 내역 등)

ViewModel, LiveData, ViewBinding

Firebase Auth / Firestore

Glide (이미지 로딩)
```

### 🖥 HacBokGS_web (관리자 웹) – 운영자용
```
1. 목적
운영자/매장/학교 담당자 가:

사용자, 주문, 메뉴, 광고, 게시판, 혼잡도 데이터를

브라우저에서 직접 관리할 수 있도록 하는 관리자 대시보드

2. 주요 기능
사용자 관리 (user.html)

users 컬렉션 전체 조회

가입자 목록 확인 (이메일, 이름, 학번 등)

주문 관리 (jumun.html + 매장별 *_boss.html)

전체 주문 조회, 매장별 주문 조회

주문 상태 변경 (접수/조리중/완료)

완료 처리 시 앱의 조리 완료 팝업과 연동

매장별 사장님 페이지(예: kkuihankki/kkui_boss.html)

광고 관리 (advertising.html)

광고 등록 (제목, 링크, 만료일, 이미지 업로드)

advertising/ing/posts → 만료 시 advertising/end/posts 로 이동

앱 메인 배너에 노출되는 광고 제어

게시판 관리 (bullentin_board.html)

학생 게시글 목록 조회

부적절한 게시글 삭제(모더레이션)

오늘의 메뉴 관리 (todaymenu.html + 매장별 메뉴 페이지)

매장별 오늘의 메뉴 추가/수정/삭제

앱에서 노출되는 실제 판매 메뉴를 제어

혼잡도 모니터링 (congestion.html)

store_order/congestion/{date}/eating 데이터 기반

현재 이용 인원/혼잡도 확인

3. 기술 스택 (웹)
HTML, CSS, JavaScript

Firebase Web SDK (Firestore, Storage)

별도 서버 없이 정적 웹 + Firebase 구조
```

### 🔗 데이터 연동 구조 (요약)
```
영역	앱에서 사용	관리자 웹에서 사용	Firestore 경로 예시
사용자 정보	로그인 후 프로필 표시	user.html 에서 전체 목록 조회	users/{userId}
장바구니	메뉴 담기/결제 전 확인	(관리자 직접 접근 X)	users/{userId}/basket/{basketItemId}
주문	주문 내역/조리 상태/팝업	jumun.html, *_boss.html	users/{userId}/orders/{orderId}
게시판	글 작성/목록/상세	bullentin_board.html 에서 모니터링	bulletin_board/{category}/board/{postId}
광고	메인 배너 슬라이드	advertising.html에서 등록/관리	advertising/ing/posts/{adId}, advertising/end/...
베스트 메뉴	오늘의 인기 메뉴 표시	(통계/조회용)	best_menu/{dateDocId}
대기 팀 수	메인 화면 매장별 대기 표시	주문 처리 이후 관리자 측에서 갱신	order_management/{storeId}/count/{dateDocId}
혼잡도	메인 화면 혼잡도 ProgressBar 표시	congestion.html에서 실시간 모니터	store_order/congestion/{dateDocId}/eating
```

### 🚀 실행 방법 (요약)
```
실제 실행을 위해서는 Firebase 프로젝트 설정 및
google-services.json(앱) / 웹용 Firebase 설정이 필요합니다.

1. HacBokGS (Android 앱)
HacBokGS_package/HacBokGS 폴더를 Android Studio로 엽니다.

Firebase 콘솔에서 프로젝트 생성 후:

Android 앱 등록 (applicationId 일치)

google-services.json 파일을 HacBokGS/app/ 에 복사

Gradle Sync 실행

실제 기기 또는 에뮬레이터에 앱 실행

2. HacBokGS_web (관리자 웹)
HacBokGS_package/HacBokGS_web 폴더를 웹 서버(또는 Firebase Hosting)에 배포

간단히는 live-server, http-server 같은 정적 서버 사용 가능

각 HTML에서 사용하는 Firebase 설정이 실제 Firebase 프로젝트와 일치해야 함

배포 후 관리자 URL로 접속하여 기능 확인
```

### 👥 팀 구성 & 나의 역할
```
4명이서 진행한 졸업 작품 팀 프로젝트이며,
모바일 앱(HacBokGS)은 팀 프로젝트로, 저는 팀장으로 전 과정에 깊게 참여했고,
관리자 웹(HacBokGS_web)은 제가 단독으로 설계·개발했습니다.

🧑‍🤝‍🧑 팀 구성
인원: 4명

전체 주제: 남서울대학교 학식·매점 통합 서비스 (앱 + 관리자 웹)

나의 포지션:

팀장(Leader)

Android & Firebase를 중심으로 한 풀스택 역할

관리자 웹(HacBokGS_web) 단독 개발자
```

### 📱 모바일 앱(HacBokGS)에서의 나의 역할
```
아이디어 & 기획

“학식/매점 주문 + 혼잡도 + 게시판 + 광고 + 지도”를 하나로 묶는 캠퍼스 통합 서비스 아이디어 제안

학생/관리자 양쪽 관점에서 요구사항 및 사용자 시나리오 정의

졸업작품 발표 범위 및 핵심 기능 우선순위 설정

설계

앱 전체 화면 플로우(로그인 → 메인 → 주문/게시판/지도 → 결제/주문완료) 설계

Firestore 컬렉션/문서 구조 초안 설계 (users, orders, basket, bulletin_board, advertising, congestion 등)

주문/장바구니/조리 상태 감시/팝업 로직 설계

Activity 구조 및 MVVM(ViewModel + Repository) 적용 구간 정의

디자인 & UX

메인 대시보드, 주문 플로우, 게시판, 지도 등 주요 화면 UX 설계에 참여

레이아웃 구조, 기본 컬러톤, 공통 UI 컴포넌트 방향 정의

개발

Google 로그인 + Firebase Auth 연동

장바구니/주문/주문내역 핵심 로직 구현 (ViewModel + Repository)

조리 상태 감시(MultiStoreCookingWatcher) 및 조리 완료 팝업 로직 구현

혼잡도, 광고 배너, 베스트 메뉴 표시 등 메인 화면 주요 기능 구현

일부 화면/기능의 코드 작성 및 팀원 코드 리뷰

오류 수정

빌드/런타임 에러, 데이터 동기화 이슈 디버깅

복잡한 흐름(주문 → 조리완료 → 팝업 → 주문내역)의 버그 수정

코드 리팩터링 및 중복 제거 주도
```

### 🖥 관리자 웹(HacBokGS_web)에서의 나의 역할
```
HacBokGS_web은 제가 단독으로 설계 · 디자인 · 개발 · 오류 수정까지 담당했습니다.

기획 & 설계

관리자/매장/학교 담당자가 어떤 데이터를 어떻게 관리해야 하는지 도출

관리자 대시보드(아이콘 기반 메뉴), 각 기능 화면(user, jumun, advertising, bulletin, todaymenu, congestion) 설계

앱과 공유하는 Firestore 구조를 고려하여, 관리자 웹에서 다뤄야 할 컬렉션/필드 정의

프론트엔드 개발

master_main.html : 관리자 대시보드 레이아웃 및 네비게이션 구현

user.html : users 컬렉션 조회 화면 구현

jumun.html, 매장별 *_boss.html : 주문 관리/사장님 페이지 구현

advertising.html : 광고 등록/수정/삭제 UI 및 이미지 업로드 연동

bullentin_board.html : 게시판 모니터링 및 삭제 기능 구현

todaymenu.html, 매장별 메뉴 페이지 : 오늘의 메뉴 CRUD 화면 구현

congestion.html : 혼잡도(이용 인원) 시각화 화면 구현

Firebase 연동

Firestore/Storage와 직접 통신하는 JavaScript 코드 작성

실시간 리스닝을 통한 주문/게시판/혼잡도 데이터 반영

광고 만료 로직 (진행중 → 종료 컬렉션 이동) 구현

디자인 & UX

운영자가 쉽게 사용할 수 있도록 테이블 기반 + 버튼/아이콘 중심 UI 설계

복잡한 설정 없이 “입력 → 클릭 → 결과 확인” 흐름 위주로 구성

오류 수정 & 유지보수

관리자 페이지 전반의 버그 수정 및 UI/동작 개선

Firestore 쿼리/보안 규칙과 맞지 않는 부분 점검 및 수정
```

### 📌 마무리
HacBokGS_package 는

학생용 HacBokGS 모바일 앱 과

운영자용 HacBokGS_web 관리자 웹 을
하나의 Firebase 백엔드 위에 통합적으로 설계·구현한 프로젝트입니다.

이 README는 두 프로젝트의 관계, 아키텍처, 주요 기능, 실행 방법,
그리고 특히 제가 어떤 역할을 했는지를 한 번에 파악할 수 있도록 작성되었습니다.
