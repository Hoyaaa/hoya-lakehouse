# 안드로이드 실시간 주문 내역 및 다중 상점 상세 조회 모듈

## 1. Overview

본 모듈은 Android 애플리케이션에서 사용자의 주문 이력을 조회하고, 하나의 주문에 포함된 다중 상점의 메뉴 및 옵션, 그리고 조리 상태를 실시간으로 확인할 수 있는 주문 관리 시스템입니다.

MVVM 아키텍처와 Repository 패턴을 기반으로 데이터 계층과 UI 계층을 분리하였으며, Firestore의 Snapshot Listener를 활용하여 **서버 상태 변화가 클라이언트 UI에 즉시 반영되는 실시간 데이터 파이프라인**을 구축했습니다.

---

## 2. Tech Stack

* Language: Kotlin, Java
* Platform: Android SDK
* Architecture: MVVM, Repository Pattern
* Backend: Firebase Cloud Firestore, Firebase Authentication
* UI: RecyclerView (Nested), LiveData

---

## 3. Key Features

### 1. 실시간 주문 상태 동기화

* `addSnapshotListener` 기반 실시간 데이터 수신
* 상태 변경 시 LiveData 자동 갱신
* UI 즉시 반영 (No Refresh)

### 2. 다중 상점 주문 상태 통합

* 여러 상점 주문을 하나의 트랜잭션처럼 표현
* 모든 상점 `complete == true`일 경우 최종 완료 처리
* 부분 완료 상태까지 표현 가능

### 3. 계층형 데이터 모델링 및 렌더링

* 주문 → 상점 → 메뉴 → 옵션 구조
* DTO 기반 데이터 정규화
* Nested RecyclerView로 계층 구조 시각화

### 4. 단방향 데이터 흐름 아키텍처

* Repository → ViewModel → UI 흐름 유지
* UI는 데이터 소비만 수행
* 비즈니스 로직 완전 분리

---

## 4. Architecture Flow

```id="order-flow-01"
OrderHistoryActivity 진입
    ->
ViewModel 호출
    ->
Repository Snapshot Listener 등록
    ->
Firestore 데이터 변경 감지
    ->
변경된 문서 파싱 (DTO 변환)
    ->
ViewModel 연산 처리
    ->
LiveData 업데이트
    ->
UI 자동 갱신
    ->
[사용자 인터랙션]
    ->
주문 클릭
    ->
OrderDetailCompleteActivity 진입
    ->
상점별 데이터 렌더링
    ->
메뉴 및 옵션 Nested UI 출력
```

---

## 5. Technical Specification

## 5.1 Data & Domain Layer

### OrderMenu.kt (DTO Model)

**역할**
Firestore 계층형 데이터를 표현하는 도메인 모델

**구성**

* OrderData
* StoreOrder
* OrderMenu
* Option

**특징**

* Kotlin data class 기반
* 불변 객체 설계
* 계층 구조 명확화

---

### UserOrderDataRepository.kt

**역할**
Firestore 통신 및 데이터 파싱 담당

**구현 상세**

* Snapshot Listener 등록
* 변경 타입 구분 처리

  * ADDED
  * MODIFIED
  * REMOVED
* Raw Map → DTO 변환 (`parseOrderData`)
* 불필요한 전체 리렌더링 방지

---

### OrderViewModel.kt

**역할**
데이터 상태 관리 및 비즈니스 로직 처리

**구현 상세**

* 주문 리스트 상태 관리 (LiveData)
* 총 금액 계산 (옵션 포함)
* 전체 조리 완료 여부 판단
* UI 전달용 데이터 가공

---

## 5.2 UI Layer

### OrderHistoryActivity & Adapter

**역할**
주문 이력 리스트 화면

**구현 상세**

* 최신순 정렬
* 요약 텍스트 생성

  * "상점 외 n개"
  * "메뉴 외 n개"
* 썸네일 및 금액 표시

---

### OrderDetailCompleteActivity

**역할**
주문 상세 정보 및 상태 표시

**구현 상세**

* 주문 단위 상세 트리 출력
* 전체 완료 상태에 따라 삭제 기능 활성화

---

### Adapter 구조

#### OrderDetailAdapter (Outer)

* 상점 단위 렌더링
* 조리 상태 표시 (색상 기반)
* 내부 RecyclerView 포함

#### OrderStoreAdapter (Inner)

* 메뉴 및 옵션 렌더링
* 옵션 View 동적 생성 (addView)

---

## 6. Core Technical Concepts

* Firestore 실시간 동기화 구조
* MVVM + Repository 패턴
* 계층형 데이터 파싱 및 모델링
* Nested RecyclerView 구조 설계
* 상태 기반 UI 업데이트 (Reactive UI)

---

## 7. Design Considerations

* 실시간 데이터 반영으로 UX 개선
* 변경된 데이터만 처리하여 성능 최적화
* 계층형 구조를 DTO로 명확히 분리
* ViewModel 중심 상태 관리로 UI 단순화
* 데이터 흐름 단방향 유지

---

## 8. Highlight for Resume

* Firestore Snapshot Listener 기반 실시간 데이터 동기화 시스템 구현
* MVVM + Repository 패턴을 활용한 아키텍처 설계 및 책임 분리
* 다중 상점 주문 상태를 통합 처리하는 비즈니스 로직 구현
* 계층형 NoSQL 데이터를 DTO로 정규화 및 파싱 로직 설계
* Nested RecyclerView를 활용한 복잡한 UI 구조 렌더링 구현

---

## 9. Improvement Ideas

* Paging3 적용으로 대용량 주문 처리 최적화
* DiffUtil 적용으로 리스트 업데이트 성능 개선
* Flow / Coroutine 기반 비동기 처리 개선
* 주문 상태 변경 로그 히스토리 관리
* Cloud Functions 기반 서버 검증 추가

---
