# 안드로이드 주간 식단표(오늘의 메뉴) 조회 모듈

## 1. Overview

본 모듈은 Android 애플리케이션에서 현재 날짜를 기준으로 해당 주차(월~금)의 식단 정보를 Firebase Cloud Firestore로부터 동적으로 조회하여 제공하는 기능입니다.

날짜 계산 로직과 상태 기반 UI를 결합하여, 사용자가 요일별 식단을 직관적으로 탐색할 수 있도록 설계되었으며,
데이터 조회부터 가공, UI 반영까지의 흐름을 일관된 구조로 구성한 **동적 데이터 렌더링 시스템**입니다.

---

## 2. Tech Stack

* Language: Kotlin
* Platform: Android SDK
* Backend: Firebase Cloud Firestore
* UI Architecture: DataBinding, ViewBinding, ObservableField

---

## 3. Key Features

### 1. 날짜 기반 동적 데이터 조회

* `LocalDate`, `Calendar`를 활용한 현재 날짜 계산
* 월(Month) + 주차(Week) 기반 Firestore 문서 식별
* 예: `"4th_2week_menu"`

### 2. 주간 식단 데이터 비동기 로딩

* Firestore 단일 문서 조회 구조
* 월~금 데이터 일괄 수신 후 클라이언트에서 가공

### 3. 상태 기반 UI 렌더링

* 선택된 요일 상태 관리
* 버튼 및 화살표 입력에 따라 UI 동기화
* ObservableField 기반 자동 UI 업데이트

### 4. 데이터 가공 및 포맷팅

* 콤마(,) 기준 문자열 분리
* 줄바꿈 처리로 가독성 개선
* 사용자 친화적 텍스트 변환

---

## 4. Architecture Flow

```id="menu-flow-01"
TodayMenuActivity 진입
    ->
현재 날짜 계산 (Month, Week, Day)
    ->
Firestore 문서 ID 생성
    ->
데이터 요청
    ->
주간 메뉴 데이터 수신
    ->
문자열 파싱 및 DTO 변환
    ->
현재 요일 기준 UI 초기화
    ->
[사용자 인터랙션]
    ->
요일 버튼 클릭
    ->
선택 상태 변경
    ->
메뉴 텍스트 갱신
    ->
좌/우 화살표 클릭
    ->
요일 순환 이동
    ->
UI 업데이트
```

---

## 5. Technical Specification

### 1. TodayMenu.kt

**역할**
요일별 식단 데이터를 관리하는 DTO(Data Transfer Object)

**구현 상세**

* Kotlin `data class` 사용
* 불변 객체 설계 (Immutability)
* 필드 구성

  * id
  * dayOfWeek
  * menu (가공된 문자열)

---

### 2. TodayMenuActivity.kt

**역할**
데이터 조회, 상태 관리, UI 렌더링을 담당하는 메인 컨트롤러

---

### 구현 상세

#### 1. 날짜 및 시간 처리

* `ZoneId.of("Asia/Seoul")` 명시
* 디바이스 설정과 무관한 일관된 날짜 계산
* 주차 계산 로직 기반 문서 ID 생성

---

#### 2. Firestore 비동기 통신

* 단일 문서 조회 방식 (`collection -> document`)
* `addOnSuccessListener` 기반 데이터 처리
* 네트워크 결과를 리스트로 변환

---

#### 3. 데이터 파싱 및 가공

* 문자열 split(",") 처리
* 줄바꿈 포맷팅 적용
* UI 출력 최적화

---

#### 4. View State 관리

* 요일 번호 ↔ 버튼 View 매핑 (Map 구조)
* `selectBtnState()`로 상태 일괄 제어
* 조건문 최소화 및 코드 간결화

---

#### 5. DataBinding 적용

* `ObservableField` 기반 데이터 바인딩
* UI 자동 갱신 구조
* View와 로직 간 결합도 감소

---

## 6. Core Technical Concepts

* 날짜 기반 데이터 식별 및 동적 쿼리
* 비동기 데이터 처리 및 UI 동기화
* 상태 기반 UI 관리 (State-driven UI)
* 문자열 데이터 가공 및 포맷팅
* DataBinding을 활용한 반응형 UI

---

## 7. Design Considerations

* 날짜 계산 로직을 통해 **자동화된 데이터 조회 구조 설계**
* 단일 문서 조회 방식으로 **네트워크 비용 최소화**
* 상태 기반 UI 구조로 **확장성과 유지보수성 확보**
* 데이터 가공 로직을 분리하여 **가독성 향상**

---

## 8. Highlight for Resume

* 날짜 기반 동적 쿼리를 활용한 Firestore 데이터 조회 시스템 구현
* ObservableField 기반 상태 관리로 UI 자동 갱신 구조 설계
* 문자열 데이터 파싱 및 포맷팅을 통한 사용자 경험 개선
* Map 기반 View 상태 관리로 코드 단순화 및 유지보수성 향상
* 비동기 데이터 처리와 UI 동기화를 결합한 구조 설계

---

## 9. Improvement Ideas

* 캐싱 전략 적용 (로컬 DB / Room)
* 주말 데이터 확장 및 UI 대응
* Paging 또는 Lazy Loading 적용
* MVVM + ViewModel 구조로 리팩토링
* Firestore 데이터 구조 정규화

---
