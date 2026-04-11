# HacBokGS 통합 플랫폼

남서울대학교 학식·매점 실시간 주문 및 운영 관리 시스템

---

## 1. 개요 (Overview)

**HacBokGS**는 남서울대학교 학식 및 매점 이용을 위한
**모바일 앱 + 관리자 웹 + 데이터 자동화 파이프라인**이 결합된 통합 플랫폼입니다.

* 사용자는 Android 앱을 통해 주문, 커뮤니티, 메뉴 조회 기능을 이용
* 관리자는 웹 대시보드를 통해 주문, 광고, 게시판, 혼잡도 등을 실시간 관리
* Firebase 기반 서버리스 구조로 전체 시스템 구성

특히 본 프로젝트는 **실시간 데이터 처리, 운영 자동화, 다중 시스템 연동**을 목표로 설계되었습니다.

---

## 2. 프로젝트 구성 (System Composition)

```
[Android App (HacBokGS)]
    -> 주문 / 커뮤니티 / 메뉴 / 광고

[Admin Web (HacBokGS_web)]
    -> 주문 / 사용자 / 광고 / 게시판 / 혼잡도 관리

[Data Pipeline]
    -> Python (Flask + Selenium 크롤링)

[Backend]
    -> Firebase (Firestore + Storage + Auth)
```

---

## 3. 기술 스택 (Tech Stack)

### Android Client

* Java, Kotlin
* MVVM, Repository Pattern
* RecyclerView, LiveData, DataBinding
* Firebase SDK

### Admin Web

* HTML5, CSS3, Vanilla JavaScript
* Firebase Web SDK
* Background Scheduler (setInterval)

### Backend (Serverless)

* Firebase Authentication
* Firebase Cloud Firestore
* Firebase Storage

### Data Pipeline

* Python, Flask
* Selenium (크롤링 자동화)

---

## 4. 핵심 기능 (Core Features)

### 4.1 실시간 주문 및 결제 시스템

* 장바구니 → 결제 → 다중 상점 분산 처리
* Firestore Batch 기반 원자적 데이터 처리
* Transaction 기반 주문 번호 채번
* 주문 상태 실시간 동기화

---

### 4.2 관리자 웹 (HacBokGS_web)

#### 사용자 관리

* `users` 컬렉션 기반 전체 사용자 조회 및 관리

#### 주문 관리

* 전체 주문 통합 조회 (`jumun.html`)
* 매장별 주문 관리 (사장님 전용 페이지)
* 상태 변경: 접수 → 조리중 → 완료
* 완료 시 앱과 연동된 실시간 알림

#### 광고 관리

* 광고 등록 / 수정 / 삭제
* 만료일 기반 자동 상태 전환 (ing → end)
* Storage 이미지 마이그레이션 처리

#### 게시판 관리

* 카테고리별 게시글 조회 및 삭제
* 댓글 포함 계층형 데이터 관리

#### 오늘의 메뉴 관리

* 매장별 메뉴 CRUD
* 앱과 실시간 데이터 연동

#### 혼잡도 모니터링

* 현재 이용 인원 기반 혼잡도 계산
* 실시간 상태 시각화

---

### 4.3 실시간 상태 감지 및 알림 시스템

* Firestore SnapshotListener 기반 이벤트 감지
* 조리 완료 시 팝업 알림
* UI 상태 기반 큐잉(Queue) 처리
* 앱 재시작 시 상태 복구 로직

---

### 4.4 데이터 자동화 파이프라인

* 관리자 로그인 → 크롤링 트리거
* Selenium 기반 식단 데이터 수집
* Firestore 자동 적재

---

### 4.5 실시간 데이터 처리 및 분석

* 혼잡도 계산 알고리즘 (시간 가중치 기반)
* 매장별 베스트 메뉴 집계
* 다중 컬렉션 병렬 처리 및 통합

---

## 5. 아키텍처 특징 (Architecture Highlights)

### 서버리스 구조

* 별도 백엔드 없이 Firebase로 전체 시스템 구성

### 실시간 데이터 흐름

* SnapshotListener 기반 UI 자동 갱신

### 데이터 정합성 보장

* Batch + Transaction 조합으로 동시성 문제 해결

### 비동기 이벤트 제어

* Activity Lifecycle 기반 안전한 UI 처리
* Handler + Queue 기반 재시도 로직

---

## 6. 데이터 흐름 (Data Flow)

```
사용자 주문
-> Firestore 저장
-> 상점별 데이터 분산

상점 상태 변경
-> Firestore 업데이트
-> 실시간 리스너 감지
-> 사용자 앱 UI 자동 갱신

관리자 작업
-> Web에서 직접 DB 조작
-> 앱과 즉시 동기화

크롤링 서버
-> 외부 데이터 수집
-> Firestore 반영
```

---

## 7. 나의 역할 (Contribution)

본 프로젝트에서 **관리자 웹(HacBokGS_web)을 단독으로 설계 및 개발**했습니다.

### 기획

* 운영자(학복관, 매장 관리자) 관점에서 요구사항 정의
* 앱과 연동되는 관리자 기능 범위 설계

### 설계

* Firestore 데이터 구조 설계 (앱과 공유)
* 주문 / 광고 / 게시판 / 혼잡도 데이터 흐름 설계

### 개발

* HTML / CSS / JavaScript 기반 관리자 UI 전체 구현
* Firebase Firestore / Storage 연동 로직 구현
* 실시간 데이터 처리 및 비동기 로직 구현

### 운영 및 안정성 개선

* 데이터 삭제/수정 오류 방지 UX 설계
* 실시간 동기화 테스트 및 예외 처리 강화

---

## 8. 기술적 성과 (Technical Highlights)

* Firebase 기반 실시간 시스템 설계 및 구현
* Transaction / Batch 활용한 데이터 정합성 확보
* 다중 비동기 처리 및 상태 동기화 구조 설계
* 서버리스 환경에서 운영 자동화 시스템 구축
* 웹 + 모바일 + 크롤링까지 통합한 풀스택 구조 경험

---
