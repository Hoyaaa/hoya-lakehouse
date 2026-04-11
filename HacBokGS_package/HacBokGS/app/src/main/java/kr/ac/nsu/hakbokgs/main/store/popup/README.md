# 안드로이드 실시간 다중 상점 조리 상태 감지 및 팝업 시스템

## 1. Overview

본 모듈은 Firebase Cloud Firestore의 실시간 동기화 기능을 기반으로, 다중 상점의 주문(조리) 상태를 백그라운드에서 지속적으로 모니터링하고, 상태 변화 이벤트를 사용자에게 즉시 전달하는 이벤트 기반 알림 시스템입니다.

특히 Android의 Activity 생명주기와 비동기 콜백 간의 불일치로 발생할 수 있는 UI 예외를 해결하기 위해, **UI 상태 인식 + 이벤트 큐잉 + 재시도 로직**을 결합한 안정적인 팝업 처리 아키텍처를 설계했습니다.

---

## 2. Tech Stack

* Language: Kotlin
* Platform: Android SDK
* Backend: Firebase Cloud Firestore, Firebase Authentication
* Concurrency: Handler, Looper (Main Thread)
* UI: AlertDialog (Custom Layout)

---

## 3. Key Features

### 1. 실시간 다중 상점 상태 감지

* `addSnapshotListener` 기반 이벤트 스트리밍
* 주문 내 여러 상점 상태를 독립적으로 추적
* 서버 상태 변경 즉시 클라이언트 반영

---

### 2. Lifecycle Safe Popup Queueing System

* Activity 상태 미준비 시 즉시 UI 호출하지 않음
* Pending Queue에 이벤트 저장
* Handler 기반 재시도 (Main Thread 보장)

---

### 3. 중복 알림 방지 (Dual Validation)

* Local Memory Map + Firestore 필드 (`popupShown`) 이중 체크
* 동일 이벤트 중복 발생 차단
* 네트워크 지연 및 재연결 상황 대응

---

### 4. 세션 복구 기반 안정성 확보

* 앱 재시작 시 기존 주문 상태 복구
* Snapshot Listener 재등록
* 이벤트 누락 방지

---

## 4. Architecture Flow

```id="popup-flow-01"
앱 실행 / 주문 발생
    ->
CookingStateRecovery 실행
    ->
기존 주문 조회
    ->
MultiStoreCookingWatcher 등록
    ->
Firestore Snapshot Listener 활성화
    ->
상태 변경 감지 (complete: false -> true)
    ->
중복 체크 (Local + Remote)
    ->
UI 상태 확인 (CurrentActivityProvider)
    ->
[가능] -> 즉시 Popup 출력
    ->
[불가능] -> Pending Queue 저장
    ->
Handler 재시도 (1초 주기)
    ->
UI 준비 완료
    ->
Popup 출력 및 DB 업데이트
```

---

## 5. Technical Specification

## 5.1 Core Observer Layer

### MultiStoreCookingWatcher.kt

**역할**
실시간 데이터 변화 감지 및 팝업 트리거를 관리하는 핵심 옵저버

**구현 상세**

* Firestore Listener 등록 및 해제 관리
* 상태 추적 Map 구조

  * `listenerRegistrations`
  * `previousCompleteMap`
  * `shownPopupStores`
* 상태 변화 비교 기반 이벤트 발생

  * false → true transition 감지
* 메모리 누수 방지 (Listener lifecycle 관리)

---

## 5.2 Recovery Layer

### CookingStateRecovery.kt

**역할**
앱 재시작 시 감지 상태 복구

**구현 상세**

* FirebaseAuth 기반 사용자 식별
* Firestore 주문 컬렉션 조회
* 기존 주문에 대한 Watcher 재등록
* 실시간 감지 환경 재구성

---

## 5.3 UI Queue Processing Layer

### Popup Queue Mechanism

**구조**

* `pendingPopups` 리스트 기반 큐
* Handler + MainLooper 기반 재시도

**동작 방식**

1. UI 사용 불가 상태 감지
2. 이벤트 큐에 저장
3. 일정 주기 재시도
4. UI 가능 시점에 실행

---

## 5.4 UI Layer

### CookingStatePopup.kt

**역할**
사용자에게 조리 완료 상태 시각화

**구현 상세**

* AlertDialog + Custom Layout
* `setCancelable(false)` 적용
* 사용자 인지 강제 UX 설계

---

## 6. Core Technical Concepts

* Event-driven Architecture
* Firestore Real-time Streaming
* Lifecycle-aware UI handling
* Asynchronous Queue Processing
* Idempotent Event Handling (중복 방지)

---

## 7. Design Considerations

* UI Thread 안전성 확보 (MainLooper 강제)
* Activity Lifecycle 기반 UI 호출 제한
* 네트워크 지연 및 앱 상태 변화 대응
* 이벤트 유실 방지 구조 설계
* 중복 이벤트 제거 로직

---

## 8. Highlight for Resume

* Firestore SnapshotListener 기반 실시간 이벤트 처리 시스템 설계
* Android Lifecycle 문제를 해결하기 위한 Queue 기반 UI 처리 로직 구현
* Handler + MainLooper를 활용한 안정적인 비동기 UI 제어
* 로컬 캐시 + 원격 DB를 활용한 이중 검증 기반 중복 이벤트 방지
* 앱 재시작 시 이벤트 감지를 복구하는 Recovery 시스템 구현

---

## 9. Improvement Ideas

* Coroutine + Flow 기반 비동기 구조 개선
* WorkManager 기반 백그라운드 처리 확장
* FCM 푸시 알림과 연동 (앱 비활성 상태 대응)
* 이벤트 우선순위 큐(Priority Queue) 도입
* 로그 기반 이벤트 추적 시스템 구축

---
