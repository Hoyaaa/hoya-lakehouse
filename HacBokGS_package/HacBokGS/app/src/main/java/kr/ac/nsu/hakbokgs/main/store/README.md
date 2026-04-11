# 안드로이드 다중 상점 통합 메인 및 실시간 대기열 팝업 모듈

## 1. Overview

본 모듈은 Android 애플리케이션의 메인 진입점으로서, 다수의 상점 정보를 통합 관리하고 사용자에게 실시간 대기열 정보를 제공하는 네비게이션 허브 시스템입니다.

사용자가 상점에 진입하기 전에 Firebase Cloud Firestore를 통해 현재 주문 밀집도(대기열)를 사전 조회할 수 있도록 설계되어, 단순 탐색을 넘어 **사용자의 선택을 지원하는 데이터 기반 UX(Data-driven UX)**를 제공합니다.

또한 Glide 커스텀 모듈을 통해 Firebase Cloud Storage 이미지 로딩 파이프라인을 최적화하여 렌더링 성능과 코드 단순성을 동시에 확보했습니다.

---

## 2. Tech Stack

* Language: Java, Kotlin
* Platform: Android SDK
* Backend: Firebase Cloud Firestore, Firebase Cloud Storage
* Library: Glide, FirebaseUI-Storage
* UI: Custom Dialog, Bottom Navigation

---

## 3. Key Features

### 1. 실시간 대기열 기반 의사결정 UX

* 상점 진입 전 대기 팀 수 제공
* 사용자 선택 비용 감소 (혼잡도 판단)
* 데이터 기반 UX 설계

---

### 2. Firestore 단일 문서 기반 빠른 조회

* `order_management` 문서 단건 조회
* 불필요한 리스트 조회 제거
* 네트워크 비용 최소화

---

### 3. Glide 커스텀 이미지 로딩 파이프라인

* StorageReference → Glide 직접 로딩
* URL 변환 과정 제거
* 캐싱 자동 적용

---

### 4. 커스텀 다이얼로그 UX

* Activity 전환 전 인터셉트 UI
* 투명 배경 + 라운드 처리
* 사용자 행동 유도 (진입 여부 선택)

---

### 5. 통합 네비게이션 허브

* 주요 도메인 간 중앙 라우팅
* 일관된 이동 흐름 제공

---

## 4. Architecture Flow

```id="restaurant-flow-01"
RestaurantListActivity 진입
    ->
Firebase 초기화 검증
    ->
사용자 상점 클릭
    ->
Firestore 단일 문서 조회 (대기열)
    ->
데이터 수신
    ->
[성공]
    -> 대기열 정보 파싱
    -> Custom Dialog 생성
    -> 사용자 선택
        -> [입장] -> Activity 전환
        -> [취소] -> Dialog 종료

    ->
[실패]
    -> 예외 메시지 출력 ("대기 정보 오류")
```

---

## 5. Technical Specification

## 5.1 Image Pipeline Layer

### MyAppGlideModule.kt

**역할**
Glide와 Firebase Storage를 연결하는 커스텀 이미지 로딩 모듈

**구현 상세**

* `@GlideModule` 기반 자동 코드 생성
* `registerComponents()` 오버라이드
* `StorageReference → InputStream` 변환 등록
* FirebaseImageLoader.Factory 적용

**핵심 효과**

* 이미지 로딩 코드 단순화
* 네트워크 처리 최적화
* 캐싱 자동 적용

---

## 5.2 Main Controller Layer

### RestaurantListActivity.java

**역할**
상점 목록 UI, 데이터 통신, 팝업 처리, 네비게이션 제어

---

### 주요 구현 포인트

#### 1. 비동기 데이터 처리

* `addOnSuccessListener`
* `addOnFailureListener`
* 성공/실패 분기 명확화

---

#### 2. 커스텀 다이얼로그 제어

* `requestWindowFeature()`
* `setBackgroundDrawable(TRANSPARENT)`
* 커스텀 레이아웃 인플레이트

---

#### 3. UI 안정성 확보

* dismiss() 명시 호출
* cancelable 설정
* Window Leak 방지

---

#### 4. 예외 처리

* null 데이터 대응
* 네트워크 실패 대응
* 사용자 메시지 분리

---

## 6. Core Technical Concepts

* Data-driven UX 설계
* Firestore 단건 조회 최적화
* Glide 커스터마이징 (AppGlideModule)
* Activity 전환 전 인터셉트 UI 패턴
* 비동기 UI 상태 제어

---

## 7. Design Considerations

* 사용자 행동 흐름 최소화 (클릭 → 정보 → 결정)
* 네트워크 요청 최소화 (단건 조회)
* UI 전환 전 정보 제공
* 이미지 로딩 비용 최적화
* 예외 상황에서도 UX 유지

---

## 8. Highlight for Resume

* Firebase Storage와 Glide를 통합한 커스텀 이미지 로딩 파이프라인 설계
* Firestore 단일 문서 조회 기반 실시간 대기열 시스템 구현
* Activity 전환 이전에 데이터를 제공하는 인터셉트 UI 구조 설계
* 커스텀 다이얼로그 기반 사용자 의사결정 UX 구현
* 비동기 통신 환경에서의 예외 처리 및 메모리 안정성 확보

---

## 9. Improvement Ideas

* 실시간 Listener 기반 대기열 자동 갱신
* Paging 적용으로 상점 리스트 확장
* Skeleton UI 적용 (로딩 UX 개선)
* 캐싱 전략 고도화 (DiskCacheStrategy 커스터마이징)
* 사용자 위치 기반 상점 정렬 기능 추가

---
