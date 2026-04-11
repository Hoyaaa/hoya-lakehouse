# 안드로이드 메인 대시보드 및 전역 상태 관리 모듈

## 1. Overview

본 모듈은 Android 애플리케이션의 메인 진입점(MainActivity)과 전역 상태 관리자(MyApplication)로 구성된 핵심 시스템으로, 사용자 인터페이스 렌더링과 백그라운드 상태 관리를 동시에 수행합니다.

실시간 데이터 스트리밍, 비동기 집계 처리, UI 애니메이션, 그리고 애플리케이션 생명주기를 고려한 전역 복구 시스템을 결합하여 **단일 화면을 넘어 앱 전체 상태를 통제하는 중앙 오케스트레이션 계층**으로 설계되었습니다.

---

## 2. Tech Stack

* Language: Java, Kotlin
* Platform: Android SDK
* Backend: Firebase Cloud Firestore, Firebase Authentication
* Core API: ValueAnimator, Handler, Looper, ActivityLifecycleCallbacks
* Library: Glide

---

## 3. Key Features

### 1. 실시간 혼잡도 스트리밍 + 애니메이션 UI

* Firestore Snapshot Listener 기반 데이터 수신
* ValueAnimator를 활용한 부드러운 상태 전환
* 색상 및 게이지 동적 변화

---

### 2. 다중 비동기 데이터 집계 (Best Menu)

* 여러 컬렉션 병렬 조회
* 모든 요청 완료 시점 동기화
* 상위 데이터 정렬 및 추출

---

### 3. 전역 상태 복구 시스템 (Application Level)

* Activity 생명주기와 분리된 상태 관리
* Handler 기반 주기적 루프
* 이벤트 유실 방지

---

### 4. 배너 광고 비동기 슬라이드쇼

* Handler 기반 순환 구조
* 생명주기 대응 콜백 제거
* 메모리 누수 방지

---

### 5. 중앙 네비게이션 허브

* 주요 도메인 기능으로의 진입점
* 일관된 사용자 흐름 제공

---

## 4. Architecture Flow

```id="main-flow-01"
앱 실행
    ->
MyApplication 초기화
    ->
ActivityLifecycleCallbacks 등록
    ->
전역 상태 복구 루프 시작
    ->
MainActivity 진입
    ->
UI 초기화 및 이벤트 바인딩
    ->
[병렬 처리]
    ->
혼잡도 Listener 등록
    ->
베스트 메뉴 데이터 요청
    ->
광고 데이터 요청
    ->
데이터 수신
    ->
UI 렌더링 및 애니메이션 적용
    ->
사용자 인터랙션 처리
```

---

## 5. Technical Specification

## 5.1 Main UI Layer

### MainActivity.java

**역할**
메인 대시보드 UI 렌더링 및 데이터 기반 인터랙션 제어

---

### 주요 구현 포인트

#### 1. 실시간 혼잡도 애니메이션

* `ValueAnimator` 기반 보간 처리
* ProgressBar width 동적 변경
* 색상 단계별 변경 (여유 → 혼잡)

---

#### 2. 비동기 데이터 집계 구조

* 다중 Firestore 요청 병렬 실행
* 완료 카운터 기반 동기화
* Comparator 기반 정렬 처리

---

#### 3. UI 구성 최적화

* GridLayout 기반 데이터 출력
* 상위 N개 데이터만 필터링
* 불필요 렌더링 최소화

---

#### 4. 광고 슬라이드쇼 처리

* `Handler.postDelayed` 반복 실행
* 4초 주기 순환
* onDestroy 시 콜백 제거

---

## 5.2 Application Layer

### MyApplication.kt

**역할**
앱 전역 상태 관리 및 백그라운드 복구 시스템

---

### 주요 구현 포인트

#### 1. Activity Lifecycle 추적

* `registerActivityLifecycleCallbacks` 등록
* 현재 Activity 상태 추적

---

#### 2. 전역 Polling Loop 설계

* `Handler + MainLooper` 기반 반복 실행
* 1초 주기 상태 체크
* 재귀 Runnable 구조

---

#### 3. 복구 조건 제어

* 사용자 인증 상태 확인
* Activity 준비 상태 확인
* 조건 만족 시에만 기능 실행

---

#### 4. 안정성 강화

* Null Context 접근 방지
* UI 호출 시점 제어
* Crash 방지 설계

---

## 6. Core Technical Concepts

* Application Level State Management
* Lifecycle-aware Architecture
* Asynchronous Aggregation Pattern
* Real-time Data Streaming (Firestore)
* UI Animation Synchronization
* Polling 기반 복구 메커니즘

---

## 7. Design Considerations

* UI와 백그라운드 로직 완전 분리
* Activity 생명주기와 독립적인 상태 관리
* 비동기 데이터 동기화 시점 제어
* 메모리 누수 및 크래시 방지
* 사용자 경험(UX)과 성능 균형

---

## 8. Highlight for Resume

* Application 레벨에서 동작하는 전역 상태 관리 및 복구 시스템 설계
* Firestore 기반 실시간 데이터 스트리밍과 UI 애니메이션 동기화 구현
* 다중 비동기 요청을 집계하는 동기화 메커니즘 설계
* Activity Lifecycle을 고려한 안정적인 UI 호출 제어
* Handler 기반 반복 구조를 활용한 Fault-Tolerant 시스템 구축

---

## 9. Improvement Ideas

* Handler → Coroutine + Flow 구조 전환
* 실시간 데이터 캐싱 레이어 추가
* ViewModel 기반 상태 관리 통합
* 이벤트 기반 구조(Event Bus / Flow)로 리팩토링
* 대시보드 데이터 Lazy Loading 적용

---
