# 안드로이드 글로벌 Activity 생명주기 관리 모듈

## 1. Overview

본 모듈은 Android 애플리케이션 전역에서 현재 활성화된(Activity Foreground) 화면을 추적하고 안전하게 접근할 수 있도록 설계된 생명주기 관리 유틸리티입니다.

`Application.ActivityLifecycleCallbacks`를 활용하여 모든 Activity의 상태 변화를 감지하고, 싱글톤 객체를 통해 현재 Activity를 중앙에서 관리합니다.

특히, UI Context 접근이 제한되는 환경(네트워크 콜백, 백그라운드 서비스, 푸시 처리 등)에서 발생할 수 있는 **메모리 누수 및 잘못된 Context 참조 문제를 방지**하는 데 초점을 두었습니다.

---

## 2. Tech Stack

* Language: Kotlin
* Platform: Android (SDK)
* Architecture: Singleton Pattern
* Core API: Application.ActivityLifecycleCallbacks

---

## 3. Key Features

### 1. 전역 Activity 참조 제공

* `getCurrentActivity()`를 통해 현재 Foreground Activity 접근 가능
* Dialog, Navigation, UI 이벤트 트리거에 활용 가능

### 2. Activity 생명주기 자동 추적

* `onActivityStarted`, `onActivityResumed` 기반 상태 업데이트
* 사용자와 상호작용 가능한 Activity 자동 식별

### 3. 메모리 누수 방지 설계

* `isFinishing` 조건 기반 참조 해제
* Activity 종료 시점에만 null 처리
* Static Context 참조로 인한 Leak 방지

### 4. 안전한 싱글톤 구조

* Kotlin `object`를 활용한 Thread-safe 구현
* 애플리케이션 전역에서 일관된 상태 유지

---

## 4. Architecture Flow

```id="flow-act-01"
Application 시작
    ->
registerActivityLifecycleCallbacks 등록
    ->
Activity 상태 변화 발생
    ->
onActivityStarted / onActivityResumed 호출
    ->
currentActivity 업데이트
    ->
[전역 호출]
    ->
getCurrentActivity() 사용
    ->
UI 처리 또는 Context 활용
    ->
Activity 종료
    ->
onActivityStopped / onActivityDestroyed 호출
    ->
isFinishing 확인 후 참조 해제
```

---

## 5. Technical Specification

### CurrentActivityProvider.kt

**역할**
애플리케이션 내 모든 Activity 생명주기를 감지하고, 현재 활성 Activity를 중앙에서 관리하는 싱글톤 객체

---

### 구현 상세

#### 1. Singleton (Kotlin Object)

* JVM 레벨에서 단일 인스턴스 보장
* 멀티스레드 환경에서도 안전한 접근 가능
* 불필요한 객체 생성 방지

---

#### 2. Lifecycle Callback 처리

* `onActivityResumed(activity)`

  * 사용자와 상호작용 가능한 시점
  * currentActivity 최신 상태로 갱신

* `onActivityStarted(activity)`

  * Activity가 화면에 표시되기 시작하는 시점
  * 보조적으로 상태 업데이트 수행

---

#### 3. 메모리 관리 전략

* `onActivityStopped(activity)`

  * Activity가 화면에서 사라질 때 호출
  * `isFinishing == true` 조건에서만 참조 해제

* `onActivityDestroyed(activity)`

  * Activity 완전 종료 시점 보조 처리

---

#### 4. Global Access API

* `getCurrentActivity()`

  * nullable Activity 반환
  * 호출부에서 null-safe 처리 유도
  * 잘못된 Context 접근 방지

---

## 6. Design Considerations

* Activity Context를 직접 전달하지 않고 **중앙 관리 구조** 채택
* 생명주기 기반 제어로 **잘못된 참조 타이밍 문제 해결**
* 종료 조건(isFinishing)을 활용하여 **불필요한 null 처리 방지**
* Singleton 구조로 **접근성과 일관성 확보**

---

## 7. Use Cases

* 네트워크 응답 후 Dialog 표시
* 푸시 알림 클릭 시 현재 화면 기반 네비게이션 처리
* 글로벌 Toast / Snackbar 처리
* 서비스 레이어에서 UI 트리거 필요 시

---

## 8. Highlight for Resume

* Application 레벨 Lifecycle Callback을 활용한 Activity 상태 추적 구현
* Singleton 기반 글로벌 Context 관리 구조 설계
* Activity 생명주기 이해를 바탕으로 메모리 누수 방지 로직 구현
* UI Context 접근이 제한된 환경에서의 안전한 처리 구조 설계

---

## 9. Improvement Ideas

* WeakReference 적용으로 메모리 안정성 강화
* LifecycleObserver 기반 구조로 확장
* Jetpack Navigation과 연동
* Foreground Activity Stack 관리 기능 추가

---
