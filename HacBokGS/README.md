# HacBokGS 🍱  
남서울대학교 학식/매점 통합 주문 & 캠퍼스 서비스 안드로이드 앱

> Firebase 기반 실시간 주문, 혼잡도 표시, 게시판 기능을 가진 **캠퍼스 통합 서비스 앱**입니다.  
> 이 저장소는 4인 팀으로 진행한 졸업 작품 중, 제가 **팀장으로 참여한 포트폴리오 프로젝트**입니다.

---

## 📌 프로젝트 소개

**HacBokGS**는 남서울대학교 학생들을 위한 **캠퍼스 통합 서비스**를 목표로 하는 안드로이드 앱입니다.  
별도의 백엔드 서버 없이 **Firebase(Firestore + Auth)** 를 사용하여 다음 기능을 제공합니다.

- 학식·매점 메뉴 조회 및 주문
- 실시간 조리 상태 & 혼잡도(좌석 상황) 표시
- Google 계정 기반 로그인 & 사용자 프로필 관리
- Q&A / 건의 게시판
- 광고/배너 노출 및 링크 이동
- 지도/길찾기(캠퍼스 내 위치 안내)

학생이 자주 사용하는 기능들을 하나의 앱에서 처리할 수 있도록 설계했습니다.

---

## ✨ 주요 기능

### 1. 인증 & 사용자 정보

- **Google Sign-In + Firebase Auth 연동**
- 최초 로그인 시:
  - Firestore에 사용자 문서가 없으면 프로필 입력 화면으로 이동
  - 이름, 학번 등 기본 정보를 입력 후 저장
- 기존 사용자:
  - 바로 메인 대시보드로 이동

### 2. 메인 대시보드

- 오늘 날짜 및 간단 안내
- **광고 배너 슬라이드**
  - Firestore `advertising/ing/posts` 컬렉션 기반 이미지 배너
  - Glide로 이미지 로딩, 일정 간격으로 슬라이드 전환
- **베스트 메뉴 & 대기 팀 수**
  - `best_menu` 컬렉션에서 날짜별 인기 메뉴 조회
  - `order_management/{storeId}/count` 컬렉션으로 각 매장 대기 팀 수 표시
- **실시간 혼잡도**
  - `store_order/congestion/{date}/eating` 문서 기반으로 좌석 혼잡도 표시
- 주문, 게시판, 주문내역, 마이페이지, 지도 등 주요 기능으로 이동하는 허브 역할

### 3. 메뉴 & 주문

- 매장 목록(버거, 국밥, 덮밥 등)에서 매장 선택
- 매장별 메뉴 리스트, 옵션 선택
- 장바구니에 메뉴 추가
- 결제 화면에서 전체 주문 내역 및 금액 확인
- Firestore `users/{userId}/orders/{orderId}` 문서를 생성하며 주문 정보 저장

### 4. 장바구니 & 주문 내역

- **장바구니**
  - `BasketViewModel` + `BasketDataRepository` + Firestore 구조
  - `users/{userId}/basket` 서브컬렉션에 장바구니 항목 저장
  - 수량 변경, 항목 삭제, 총 금액 계산
- **주문 내역**
  - `users/{userId}/orders` 서브컬렉션에 주문 이력 관리
  - 주문별 매장/메뉴/총 금액/상태 표시
  - 주문 상세 화면에서 메뉴 리스트, 픽업 번호 등 확인

### 5. 조리 상태 실시간 감시 & 팝업

- `MultiStoreCookingWatcher`가 `users/{userId}/orders/{orderId}` 문서 실시간 감시
- 매장별 `complete` 값이 `false → true`로 변경되면:
  - 사용자에게 “조리 완료” 팝업 표시
  - 재노출 방지를 위해 해당 매장의 `popupShown` 필드를 `true`로 업데이트
- 앱 재실행 시:
  - `CookingStateRecovery`가 미완료 주문들을 다시 탐색하고 Watcher 재등록

### 6. 게시판 기능

- 카테고리별 게시판(Q&A, 건의사항 등) 제공
- Firestore `bulletin_board/{category}/board` 컬렉션 사용
- `addSnapshotListener`로 실시간으로 글 목록 갱신
- 글 작성 / 상세 보기 / 목록 기능

### 7. 광고 & 지도

- **광고**
  - `advertising/ing/posts` 하위 문서에서 광고 이미지 및 링크 가져오기
  - Glide로 이미지 표시, 클릭 시 브라우저/웹뷰로 이동
- **지도**
  - 캠퍼스 내 특정 지점까지 길찾기/경로 안내
  - 커스텀 뷰 `RouteLineView`를 이용해 경로를 선으로 시각화

---

## 🧱 기술 스택

### Android

- **언어**: Java + Kotlin (점진적으로 Kotlin 비중 확대)
- **아키텍처**
  - 전체: Activity 중심
  - 일부 기능: MVVM (ViewModel + LiveData + Repository)
- **UI**
  - AndroidX AppCompat
  - ConstraintLayout, RecyclerView
  - ViewBinding
- **이미지 로딩**
  - Glide

### Firebase

- **Firebase Authentication**
  - Google 계정 로그인
- **Cloud Firestore**
  - 사용자 정보, 장바구니, 주문, 게시판, 광고, 혼잡도 등 모든 데이터 저장
  - SnapshotListener를 통한 실시간 업데이트

### 기타

- Google Sign-In (Google Play Services)
- Kotlin `@Parcelize`를 이용한 Parcelable 데이터 모델

---

## 🏗 아키텍처 개요

### 구조 요약

- 단일 Android 앱 모듈: `HacBokGS/app`
- **백엔드 서버 없이 Firebase가 곧 서버 역할**
- Activity 중심 UI에, 상태 관리가 중요한 영역(장바구니/주문/조리 상태)에 MVVM 패턴 적용

### 주요 설계 포인트

- **MVVM**
  - 장바구니: `BasketViewModel` + `BasketDataRepository`
  - 주문 내역: `OrderViewModel` + `UserOrderDataRepository`
- **Repository 패턴**
  - Firestore 접근 로직을 ViewModel과 분리
- **Observer 패턴**
  - Firestore `addSnapshotListener`로 실시간 데이터 감지
- **Application 레벨 라이프사이클**
  - `MyApplication`에서 `ActivityLifecycleCallbacks` 등록
  - 현재 활성 Activity 추적, 조리 상태 팝업 표시 타이밍 제어

---

## 🗂 Firestore 컬렉션 구조 (요약)

```text
1) users 컬렉션
   - users/{userId}
     - 프로필 필드
       - email, name, studentId, phone, photoUrl, createdAt, ...
     - basket 서브컬렉션
       - users/{userId}/basket/{basketItemId}
         - storeId, storeName
         - menuId, menuName
         - price, quantity
         - options
         - createdAt
     - orders 서브컬렉션
       - users/{userId}/orders/{orderId}
         - createdAt, totalPrice, totalCount, status, pickupNumber
         - storeOrders (매장별 주문 정보 Map)
           - {storeId}.storeName
           - {storeId}.complete
           - {storeId}.popupShown
           - {storeId}.menus[] (menuId, menuName, price, quantity, options ...)

2) bulletin_board 컬렉션
   - bulletin_board/{category}
     - board 서브컬렉션
       - bulletin_board/{category}/board/{postId}
         - title, content
         - authorId, authorName
         - createdAt, updatedAt
         - commentCount, viewCount

3) advertising 컬렉션
   - advertising/ing
     - posts 서브컬렉션
       - advertising/ing/posts/{adId}
         - title, imageUrl, url
         - expireDate, createdAt, priority (선택)

4) best_menu 컬렉션
   - best_menu/{dateDocId} (예: "2025-12-01")
     - 해당 날짜의 베스트 메뉴 집계 정보
     - 메뉴별 voteCount, storeId, menuName 등의 필드 포함

5) order_management 컬렉션
   - order_management/{storeId}
     - count 서브컬렉션
       - order_management/{storeId}/count/{dateDocId}
         - waitingTeamCount
         - updatedAt

6) store_order 컬렉션 (혼잡도 정보)
   - store_order/congestion/{dateDocId}/eating
     - currentPeople (현재 이용 인원)
     - level (혼잡도 단계)
     - updatedAt
```
## 🔄 화면 흐름 (Flow Chart)
```
flowchart TD

    A[앱 실행] --> B[GoogleLogin (구글 로그인)]

    B -->|로그인 성공 & 기존 사용자| C[MainActivity (메인 대시보드)]
    B -->|로그인 성공 & 신규 사용자| D[UserInformation (프로필 입력)]
    D --> C

    C --> R[RestaurantListActivity (매장 목록)]
    C --> CB[ChatBoardHomeActivity (게시판)]
    C --> AD[AdActivity 또는 브라우저 (광고 상세)]
    C --> OH[OrderHistoryActivity (주문 내역)]
    C --> MP[MypageActivity (마이페이지)]
    C --> MAP[MapActivity (지도/길찾기)]

    R --> SM[StoreMainActivity / MenuList (매장별 화면)]
    SM --> BSK[BasketActivity (장바구니)]
    BSK --> PAY[PayActivity (결제)]
    PAY --> LOAD[LoadingActivity (주문 생성)]
    LOAD --> ODC[OrderDetailCompleteActivity (주문 완료)]

    ODC --> C
    
👥 역할 기반 주문 시퀀스 (사용자 / 매장 / 서버 관점)
```
sequenceDiagram
    participant User as 사용자
    participant App as 안드로이드 앱(HacBokGS)
    participant Server as 서버(Firebase Firestore)
    participant Store as 매장 시스템(포스/주방 화면)

    User->>App: 메뉴 탐색 및 옵션 선택
    User->>App: "주문하기" / 결제 진행

    App->>Server: users/{userId}/orders/{orderId} 문서 생성
    Server-->>App: 주문 생성 성공 응답

    Note over Server,Store: 매장 측 시스템은 Firestore를<br/>실시간 리스닝한다고 가정

    Server-->>Store: 새 주문 데이터 전달
    Store-->>Store: 새 주문 확인·조리 시작

    Store->>Server: 주문 상태 = "cooking" 등으로 업데이트
    Server-->>App: 주문 상태 변경 이벤트
    App-->>User: 주문 내역 화면에 "조리 중" 표시

    Store->>Server: 주문 상태 = "complete", popupShown=false
    Server-->>App: 상태 변경 이벤트(complete = true)
    App-->>User: "조리 완료" 팝업 표시
    App->>Server: popupShown = true 로 업데이트
    
📁 프로젝트 구조 (개요)
실제 패키지 구조에 맞게 수정하여 사용하시면 됩니다.

text
```
HacBokGS/
 └── app/
     ├── src/main/java/kr/ac/nsu/hakbokgs/
     │   ├── main/           # GoogleLogin, MainActivity, MyApplication 등
     │   ├── menu/           # 매장/메뉴 관련 액티비티 & ViewModel
     │   ├── basket/         # 장바구니 ViewModel / Repository
     │   ├── order/          # 주문 내역, 조리 상태 감시 로직
     │   ├── board/          # 게시판(리스트, 작성, 상세)
     │   ├── advertising/    # 광고 리스트 & 배너
     │   └── map/            # 지도 화면, RouteLineView
     ├── src/main/res/       # 레이아웃, 이미지, 문자열 리소스
     └── build.gradle
👤 나의 역할 (기여 내용)

4명이서 진행한 졸업 작품 팀 프로젝트이며, 저는 팀장(Leader) 으로 참여했습니다.
아이디어 제안부터 기획·설계·디자인·개발·오류 수정까지 프로젝트 전반에 걸쳐 기여했습니다.

팀 구성

4인 팀 졸업 작품 프로젝트

역할: 팀장 / 안드로이드 및 Firebase를 중심으로 한 풀스택 성격의 개발 담당

아이디어 & 기획

“학식/매점 주문 + 혼잡도 + 게시판 + 광고 + 지도”를 하나로 묶는 캠퍼스 통합 서비스 아이디어 제안

요구사항 정리, 주요 사용자 시나리오 정의 (주문 흐름, 조리 완료 알림, 게시판 활용 등)

졸업 작품 발표를 위한 기능 범위 및 우선순위 결정

설계

전체 화면 흐름(Flow Chart) 설계

Firestore 컬렉션/문서 구조 설계 (users, orders, basket, bulletin_board, advertising, 혼잡도 등)

주문/장바구니/조리 상태 감시 흐름 및 팝업 로직 설계

Activity 구조와 MVVM(ViewModel + Repository) 적용 구간 설계

프론트엔드 (Android 앱)

Google 로그인 및 Firebase Auth 연동 구현

메인 대시보드(광고 배너, 베스트 메뉴, 혼잡도 표시, 주요 네비게이션) 구현

메뉴/장바구니/결제/주문 내역 화면 구현

게시판 목록/작성/상세 화면 구현

지도/경로 표시를 위한 RouteLineView 등 UI 개발

전반적인 화면 디자인 방향(레이아웃 구조, 컬러 톤, 공통 컴포넌트) 참여

백엔드(Firebase) & 데이터 연동

Firestore 컬렉션 구조 및 문서 스키마 설계

장바구니, 주문, 게시판, 광고, 혼잡도 등 데이터 CRUD 로직 구현

BasketDataRepository, UserOrderDataRepository, MultiStoreCookingWatcher 등 Repository/Watcher 구현

Firestore SnapshotListener 기반 실시간 동기화 로직 구현

디자인

전체 UI/UX 컨셉 논의 및 반영

주요 화면 레이아웃, 네비게이션 구조, 배너/리스트/버튼 스타일 결정에 참여

팀원과 협업하여 사용자 입장에서 자연스러운 플로우가 되도록 UX 조정

오류 수정 & 품질 관리

빌드 에러, 런타임 크래시, 데이터 동기화 문제 등 디버깅

주문/장바구니/조리 상태 팝업 등 복잡한 흐름의 버그 수정

코드 리팩터링, 중복 제거, 간단한 코드 리뷰 및 팀원 지원

프로젝트 관리

주간 진행 상황 공유, 작업 분담 및 일정 관리

Git 기반 협업(브랜치 전략, 충돌 해결 등) 주도

중간/최종 발표 자료 준비 및 데모 시나리오 구성
