# 안드로이드 장바구니 및 다중 상점 통합 결제 모듈

## 1. Overview

본 모듈은 Android 애플리케이션에서 사용자가 여러 상점의 상품을 하나의 장바구니에 담고, 수량 및 옵션을 조절한 뒤 통합 결제를 수행할 수 있는 커머스 시스템입니다.

MVVM 아키텍처를 기반으로 UI와 비즈니스 로직을 분리하였으며,
Firebase Firestore의 Batch 및 Transaction을 활용하여 **다중 데이터 업데이트 상황에서도 데이터 정합성과 동시성 안정성을 보장**하도록 설계했습니다.

---

## 2. Tech Stack

* Language: Kotlin
* Platform: Android SDK
* Architecture: MVVM
* Backend: Firebase Authentication, Cloud Firestore
* UI: RecyclerView (Nested), ListAdapter, LiveData

---

## 3. Key Features

### 1. 상점별 장바구니 그룹화

* `groupBy`를 활용한 상점 단위 데이터 분리
* Nested RecyclerView 구조로 계층적 UI 구성

### 2. 실시간 가격 및 수량 계산

* 옵션 가격 + 기본 가격 합산 로직
* LiveData 기반 상태 변경 즉시 UI 반영
* 총 주문 금액 및 수량 자동 갱신

### 3. 데이터 정합성을 보장하는 결제 처리

* Firestore Batch Write 사용
* 주문 생성, 상점 전달, 장바구니 삭제를 하나의 단위로 처리
* 실패 시 전체 롤백 보장

### 4. 동시성 제어 기반 주문 번호 생성

* Firestore Transaction 활용
* 다중 사용자 환경에서 중복 없는 순차 ID 생성

---

## 4. Architecture Flow

```id="cart-flow-01"
BasketActivity 진입
    ->
ViewModel → Repository 호출
    ->
Firestore 장바구니 데이터 로드
    ->
상점별 groupBy 처리
    ->
LiveData 바인딩
    ->
Nested RecyclerView 렌더링
    ->
[사용자 인터랙션]
    ->
수량 변경 / 삭제
    ->
ViewModel → Firestore 업데이트
    ->
DiffUtil 기반 UI 갱신
    ->
결제 버튼 클릭
    ->
PayActivity 진입
    ->
주문 데이터 재구성
    ->
Transaction → 주문 번호 생성
    ->
Batch Write 실행
    ->
주문 저장 + 상점 전달 + 장바구니 삭제
    ->
결제 완료 → 영수증 화면 이동
```

---

## 5. Technical Specification

## 5.1 Presentation Layer (MVVM)

### BasketActivity.kt

**역할**
UI 렌더링 및 사용자 입력 처리

**구현 상세**

* LiveData observe → UI 자동 갱신
* 결제 버튼 상태 및 총 금액 표시
* Adapter 데이터 전달

---

### BasketViewModel.kt

**역할**
장바구니 상태 관리 및 비즈니스 로직 처리

**구현 상세**

* 장바구니 데이터 로드
* 수량 변경 / 삭제 처리
* 전체 금액 및 수량 계산 로직
* 상태 기반 UI 업데이트

---

### Adapter 구조

#### BasketAdapter (Outer)

* 상점 단위 리스트 렌더링
* 내부 RecyclerView 포함

#### BasketStoreAdapter (Inner)

* ListAdapter + DiffUtil 적용
* 변경된 아이템만 업데이트

**옵션 처리**

* LinearLayout에 View 동적 추가
* 복잡한 옵션 UI 구성 대응

---

## 5.2 Data Layer

### BasketDataRepository.kt

**역할**
Firestore 데이터 접근 계층

**구현 상세**

* users/{userId}/basket CRUD 처리
* 콜백 기반 데이터 전달

---

### PayActivity.kt

**역할**
결제 처리 및 데이터 분산 저장

**구현 상세**

* 장바구니 데이터를 상점별로 재구성
* Firestore Batch Write 수행

  * 사용자 주문 저장
  * 상점 주문 전달
  * 장바구니 초기화

---

### OrderNumManage.kt

**역할**
주문 번호 생성 유틸리티

**구현 상세**

* Firestore Transaction 사용
* 현재 번호 조회 → +1 → 저장
* 원자적 처리로 중복 방지

---

## 5.3 Data Model

### BasketMenu.kt

* Firestore Document 매핑 DTO
* Serializable 지원
* 필드 구성

  * storeName
  * menuName
  * basePrice
  * quantity
  * options (List)

---

## 6. Core Technical Concepts

* MVVM 기반 상태 관리
* Nested RecyclerView 구조 설계
* DiffUtil 기반 리스트 최적화
* Firestore Batch / Transaction 처리
* 동시성 제어 (Race Condition 대응)

---

## 7. Design Considerations

* UI와 데이터 로직 분리로 유지보수성 향상
* Batch 처리로 다중 DB 작업의 원자성 확보
* Transaction을 통한 동시성 문제 해결
* 상태 기반 UI 설계로 반응형 구조 구현
* 데이터 가공을 ViewModel에서 처리하여 View 단순화

---

## 8. Highlight for Resume

* MVVM 아키텍처 기반 커머스 장바구니 시스템 설계 및 구현
* Nested RecyclerView + DiffUtil을 활용한 리스트 성능 최적화
* Firestore Batch Write를 통한 다중 데이터 원자적 처리 구현
* Transaction 기반 주문 번호 생성으로 동시성 문제 해결
* 실시간 가격 계산 및 상태 기반 UI 자동 갱신 구조 구현

---

## 9. Improvement Ideas

* 결제 실패 시 롤백 및 재시도 로직 강화
* Kotlin Flow / Coroutines 기반 비동기 처리 개선
* 주문 상태 관리 (Pending, Completed 등) 추가
* 서버 사이드 검증 (Cloud Functions) 도입
* 결제 API (PG 연동) 확장

---
