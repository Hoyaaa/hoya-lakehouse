# Firebase Firestore 기반 동적 광고 게시판 시스템

## Overview

본 모듈은 Android 애플리케이션 내에서 Firebase Cloud Firestore를 활용하여 광고 데이터를 동적으로 조회 및 렌더링하는 광고 게시판 시스템입니다.

광고 상태값(진행 중 / 종료)에 따라 데이터를 필터링하여 제공하며, RecyclerView와 Glide를 기반으로 **성능 최적화된 리스트 UI**를 구현했습니다.

또한, 외부 링크 이동 시 URL 유효성 검증 및 예외 처리 로직을 적용하여 **안정성과 사용자 경험(UX)**을 동시에 고려한 구조로 설계되었습니다.

---

## Tech Stack

* Language: Java
* Platform: Android (SDK)
* Backend: Firebase Cloud Firestore
* Library: RecyclerView, Glide

---

## Key Features

### 1. 상태 기반 동적 데이터 조회

* Firestore 컬렉션에서 상태값("ing", "end") 기준 필터링
* 클라이언트 단에서 조건 분기 없이 쿼리 파라미터로 제어
* 동일 로직 재사용 가능한 구조 설계

### 2. 고성능 리스트 렌더링

* RecyclerView 기반 ViewHolder 패턴 적용
* Glide를 통한 이미지 비동기 로딩 및 캐싱
* 스크롤 성능 및 메모리 사용 최적화

### 3. 안전한 외부 링크 처리

* URL null 체크 및 프로토콜(http/https) 검증
* 잘못된 URL 접근 시 예외 처리
* 앱 크래시 방지 및 사용자 피드백 제공

### 4. 유연한 네비게이션 구조

* 하단 메뉴 기반 Activity 라우팅
* 주요 기능(홈, 채팅, 마이페이지) 간 명확한 이동 구조

---

## Architecture Flow

```
AdActivity 진입
    ->
기본 상태값(ing) 기준 Firestore 데이터 요청
    ->
비동기 콜백으로 데이터 수신
    ->
Advertisement 객체로 파싱
    ->
AdAdapter에 데이터 전달
    ->
RecyclerView UI 렌더링
    ->
[사용자 인터랙션]
    ->
필터 버튼 클릭 -> 상태값 변경 후 재조회
    ->
광고 클릭 -> URL 검증 후 외부 브라우저 실행
```

---

## Technical Specification

### 1. Advertisement.java

**역할**: Firestore 문서를 매핑하는 DTO 클래스

**구현 상세**

* 필드 구성

  * title (String)
  * url (String)
  * expiration (Timestamp)
  * imageUrl (String)
* Firestore 데이터 구조와 1:1 매핑
* 데이터 전달 및 유지보수 용이성 확보

---

### 2. AdAdapter.java

**역할**: 데이터와 UI를 연결하는 RecyclerView Adapter

**구현 상세**

* `onBindViewHolder()`

  * Glide를 활용한 이미지 비동기 로딩
  * Placeholder 적용으로 UX 개선

* `setClickListener()`

  * URL 유효성 검증

    * null 체크
    * http/https 프로토콜 검사
  * try-catch 기반 예외 처리
  * Toast 메시지로 사용자 피드백 제공

* `updateList()`

  * 데이터 교체 시 안전한 리스트 갱신
  * notifyDataSetChanged()로 UI 동기화

---

### 3. AdActivity.java

**역할**: 광고 게시판의 메인 컨트롤러

**구현 상세**

* Firestore 비동기 통신

  * 컬렉션 계층 구조 기반 데이터 조회
  * addOnCompleteListener를 통한 결과 처리
  * 실패 시 로그 기록 및 예외 대응

* 상태 기반 데이터 요청

  * fetchAdvertisements(status) 형태로 함수 분리
  * 버튼 이벤트에 따라 상태값만 변경하여 재사용성 확보

* UI 이벤트 처리

  * 필터 버튼 클릭 이벤트
  * 하단 네비게이션 Activity 전환

---

## Design Considerations

* 데이터 상태값 기반 분리를 통해 **확장 가능한 구조 설계**
* 네트워크 요청과 UI 로직을 분리하여 **유지보수성 향상**
* 비동기 처리 흐름을 명확히 하여 **UI 블로킹 최소화**
* 클라이언트 단 검증을 통해 **서비스 안정성 강화**

---

## Highlight for Resume

* Firebase Firestore 기반 NoSQL 데이터 구조 설계 및 상태 기반 쿼리 로직 구현
* RecyclerView + Glide를 활용한 대용량 리스트 렌더링 최적화
* URL 검증 및 예외 처리 로직을 통한 앱 크래시 방지 및 안정성 확보
* 비동기 데이터 처리 구조 설계로 사용자 경험(UX) 개선
* 재사용 가능한 데이터 조회 구조(fetchAdvertisements) 설계

---

## Improvement Ideas

* Paging 적용을 통한 대용량 데이터 처리 개선
* DiffUtil 적용으로 UI 업데이트 성능 개선
* MVVM 아키텍처 및 LiveData 적용
* Firestore Security Rules 강화
* 광고 클릭 로그 수집 (Analytics 연동)

---
