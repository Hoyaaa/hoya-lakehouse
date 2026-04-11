# 안드로이드 커뮤니티 게시판 및 실시간 투표 모듈

## 1. Overview

본 모듈은 Android 애플리케이션 내에서 사용자 간 소통을 위한 커뮤니티 게시판 시스템으로, Firebase Cloud Firestore를 기반으로 설계되었습니다.

다중 카테고리 게시판 구조와 함께 실시간 데이터 동기화, 투표 시스템, 댓글 기능을 통합 구현하였으며, 특히 Firestore Transaction을 활용하여 **동시성 문제를 해결한 실시간 투표 처리 로직**을 핵심 기능으로 설계했습니다.

---

## 2. Tech Stack

* Language: Java
* Platform: Android SDK (API Level 21+)
* Backend / DB: Firebase Cloud Firestore
* Authentication: Firebase Authentication

---

## 3. Key Features

### 1. 다중 카테고리 게시판 구조

* 카테고리별 컬렉션 분리 (`chat_qna`, `chat_lunch`)
* 데이터 독립성 확보 및 확장 용이한 구조 설계

### 2. 실시간 데이터 동기화

* `addSnapshotListener` 기반 실시간 UI 반영
* 게시글, 댓글, 투표 상태 변경 즉시 반영
* 별도의 새로고침 없이 사용자 경험 개선

### 3. 트랜잭션 기반 투표 시스템

* Firestore Transaction을 활용한 원자적 처리
* 동시 투표 시 데이터 정합성 유지
* Race Condition 방지

### 4. 순차적 문서 ID 생성 로직

* NoSQL 환경에서 Auto-Increment 기능 대체
* 최신 `board_n` 조회 후 +1 방식으로 ID 생성
* `board_{n}` 형태의 가독성 높은 식별자 설계

### 5. 동적 UI 구성

* 투표 옵션 입력 필드 동적 생성
* 체크박스 상태에 따른 UI 가시성 제어
* 사용자 입력 흐름 중심 인터페이스 설계

---

## 4. Architecture Flow

```id="8kmz7a"
게시판 홈 진입 (ChatBoardHomeActivity)
    ->
카테고리 선택
    ->
게시글 목록 조회 (ChatListActivity)
    ->
Firestore 실시간 데이터 수신
    ->
게시글 클릭
    ->
게시글 상세 진입 (ChatDetailActivity)
    ->
[사용자 인터랙션]
    ->
댓글 작성 -> Firestore 저장 -> 실시간 반영
    ->
투표 참여 -> Transaction 처리 -> 결과 즉시 반영
    ->
글 작성 버튼 클릭
    ->
게시글 작성 (ChatWriteActivity)
    ->
Firestore 저장 -> 목록 자동 업데이트
```

---

## 5. Technical Specification

### 1. ChatBoardHomeActivity.java

**역할**: 커뮤니티 기능의 진입 및 카테고리 라우팅

**구현 상세**

* 카테고리 선택 이벤트 처리
* Intent 기반 게시판 분기
* 하단 네비게이션을 통한 주요 화면 이동

---

### 2. ChatListActivity.java

**역할**: 게시글 목록 조회 및 실시간 렌더링

**구현 상세**

* `orderBy("registration", DESC)` 기반 최신순 정렬
* `addSnapshotListener`를 통한 실시간 리스트 갱신
* 게시글 선택 시 document ID 및 category 전달

---

### 3. ChatWriteActivity.java

**역할**: 게시글 및 투표 생성

**구현 상세**

* 동적 UI 구성

  * 체크박스 상태에 따라 투표 UI 활성화
  * EditText 동적 추가를 통한 옵션 확장

* 순차 ID 생성

  * 최신 문서 조회 -> `board_n + 1`
  * 커스텀 Document ID 생성

* 입력 데이터 검증

  * 필수값 체크 및 예외 처리

---

### 4. ChatDetailActivity.java

**역할**: 게시글 상세, 투표, 댓글 처리

**구현 상세**

* 투표 UI 렌더링

  * 총 투표수 기반 백분율 계산
  * ProgressBar 및 TextView 동기화

* 중복 투표 방지

  * `voteUserMap` 활용
  * 사용자 이메일 기준 투표 여부 확인

* 트랜잭션 처리

  * `runTransaction()` 기반 원자적 업데이트
  * count 증가 + 사용자 기록 동시 처리

* 댓글 시스템

  * 하위 컬렉션(`comments`) 구조 사용
  * 실시간 리스너 기반 자동 갱신

---

## 6. Database Schema

### Collection 구조

```
bulletin_board
    ->
{category}
    ->
board
    ->
board_{n}
```

### Document 필드

* board_n (Number)
* title (String)
* content (String)
* user (String)
* registration (Timestamp)
* isVote (Boolean)

### Vote 관련 필드 (Optional)

* voteTitle (String)
* voteDeadline (Timestamp)
* voteOptions (Array<Map>)

  * option (String)
  * count (Number)
* voteUserMap (Map)

  * key: user email
  * value: selected index

### SubCollection: comments

* text (String)
* timestamp (Timestamp)

---

## 7. Design Considerations

* Firestore 구조를 활용한 **확장 가능한 게시판 설계**
* Transaction 기반 처리로 **데이터 정합성 보장**
* 실시간 리스너 활용으로 **UX 최적화**
* NoSQL 환경에서의 식별자 관리 문제 해결
* UI와 데이터 흐름을 분리하여 **유지보수성 확보**

---

## 8. Highlight for Resume

* Firebase Firestore 기반 실시간 데이터 동기화 시스템 구현
* Transaction을 활용한 동시성 제어 및 데이터 정합성 확보
* NoSQL 환경에서의 커스텀 Auto-Increment 로직 설계
* 동적 UI 구성 및 사용자 중심 인터페이스 설계
* 댓글 및 투표 기능을 포함한 커뮤니티 서비스 아키텍처 구현

---

## 9. Improvement Ideas

* Paging3 적용을 통한 게시글 로딩 최적화
* MVVM 아키텍처 및 LiveData / ViewModel 적용
* 투표 마감 스케줄링 (Cloud Functions)
* 좋아요 기능 및 인기 게시글 정렬 추가
* Firestore Index 최적화

---
