# HacBokGS 🍱  
남서울대학교 학식/매점 통합 주문 & 캠퍼스 서비스 안드로이드 앱

## 1. 개요 (Overview)

본 프로젝트는 남서울대학교 학식·매점 환경을 대상으로,
사용자 경험과 운영 효율을 동시에 개선하기 위해 설계된 **엔드투엔드 통합 서비스 시스템**이다.

시스템은 다음 두 영역으로 구성된다.

* Android 기반 사용자 앱 (HacBokGS)
* 관리자 웹 대시보드 (HacBokGS_web)

별도의 전통적인 백엔드 서버 없이, Firebase(Firestore, Authentication, Storage)를 중심으로
실시간 데이터 동기화, 주문 처리, 운영 자동화까지 수행하는 **Serverless 아키텍처**를 구축했다.

또한 Python 기반 크롤링 서버와 관리자 웹을 연동하여
운영 데이터를 자동으로 수집·갱신하는 **데이터 파이프라인**을 구현했다.

---

## 2. 기술 스택 (Tech Stack)

### Android Client

* Language: Kotlin, Java
* Architecture: MVVM, Repository Pattern, Singleton
* Core API: Handler, Looper, ValueAnimator, ActivityLifecycleCallbacks, Canvas, PathMeasure
* BaaS: Firebase (Firestore, Authentication, Storage)
* Library: Glide, LiveData, ViewBinding

### Admin Web

* Frontend: Vanilla JavaScript (ES6+), HTML5, CSS3
* Architecture: Iframe 기반 SPA, Background Polling (setInterval)

### Automation & Data Pipeline

* Language: Python
* Framework: Flask
* Crawling: Selenium, WebDriver Manager

---

## 3. 시스템 핵심 기능 (Key Features)

### 3.1 사용자 인증 및 온보딩 (Android)

* Google Sign-In 기반 간편 로그인 및 세션 유지
* Firestore 사용자 존재 여부에 따른 상태 기반 라우팅

  * 기존 사용자 -> 메인 대시보드
  * 신규 사용자 -> 온보딩 화면
* 정규표현식을 활용한 클라이언트 입력값 유효성 검증

---

### 3.2 실시간 주문 및 다중 상점 처리 (Android)

* Firestore SnapshotListener 기반 실시간 주문 상태 동기화
* 하나의 주문 내 다중 상점 데이터를 통합 관리
* 모든 상점의 complete 상태를 기반으로 최종 완료 여부 판단

---

### 3.3 조리 상태 감지 및 팝업 큐잉 시스템

* 다중 상점 조리 상태 실시간 감시

* complete 상태 변경 시 사용자에게 즉시 팝업 알림 제공

* UI 생명주기 문제 해결을 위한 Queue 기반 재시도 로직 구현

  * Activity 미존재 / 백그라운드 상태 -> Pending Queue 저장
  * Handler 기반 재시도

* 중복 방지 로직

  * 로컬 Map + Firestore popupShown 필드 이중 검증

---

### 3.4 결제 및 데이터 정합성 보장

* Firestore Batch Write 기반 원자적 주문 처리

  * 주문 생성 + 매장 전달 + 장바구니 초기화
* Transaction 기반 주문 번호 생성 (동시성 제어)

---

### 3.5 메인 대시보드 및 UX 고도화

* 실시간 혼잡도 UI (ValueAnimator 기반 애니메이션)
* 베스트 메뉴 집계 및 상위 랭킹 출력
* 광고 배너 슬라이드 (Handler 기반 주기적 변경)
* 사용자 의사결정을 돕는 데이터 기반 UI 제공

---

### 3.6 지도 및 커스텀 UI 렌더링

* Canvas + PathMeasure 기반 경로 애니메이션
* atan2 기반 캐릭터 회전 및 방향 제어
* 경로 시각화를 통한 직관적 UX 구현

---

### 3.7 관리자 웹 시스템 (Admin Dashboard)

#### 사용자 관리

* 전체 사용자 목록 조회 및 관리

#### 주문 관리

* 전체 주문 통합 조회
* 매장별 주문 필터링 (사장님 페이지)
* 주문 상태 변경 (접수 → 조리중 → 완료)

#### 광고 관리

* 광고 등록 / 수정 / 삭제
* 만료 광고 자동 이동 (ing → end)

#### 게시판 관리

* 카테고리별 게시글 조회 및 삭제

#### 메뉴 관리

* 매장별 오늘의 메뉴 CRUD

#### 혼잡도 모니터링

* 실시간 이용 인원 및 혼잡도 단계 시각화

---

### 3.8 운영 자동화 및 데이터 파이프라인

* 관리자 로그인 시 Python 크롤러 자동 실행

* 외부 채널 식단표 자동 수집 및 Firestore 적재

* 백그라운드 스케줄러 (Vanilla JS)

  * 광고 만료 감지 및 상태 변경
  * Firebase Storage 파일 마이그레이션
  * 매장별 주문 및 베스트 메뉴 집계

---

### 3.9 혼잡도 분석 알고리즘

* 단순 대기열 기반이 아닌 시간 가중치 적용
* 최근 결제 시간 기준 체류 확률 모델링

  * 10분 단위 가중치 적용
* 실제 매장 이용 인원 추정

---

## 4. 아키텍처 흐름 (Architecture Flow)

### 사용자 앱 흐름

로그인 -> 사용자 상태 확인 -> 온보딩 또는 메인 진입
-> 실시간 데이터 수신 (혼잡도 / 광고 / 주문)
-> 메뉴 선택 및 주문 -> 결제 (Batch)
-> 조리 상태 실시간 감지 -> 완료 팝업 출력

---

### 관리자 웹 흐름

로그인 -> 크롤링 서버 트리거
-> 식단 데이터 자동 업데이트
-> 백그라운드 스케줄러 실행
-> 광고 만료 처리 및 파일 이동
-> 주문 데이터 집계 및 혼잡도 계산

---

## 5. 아키텍처 설계 특징

* Serverless 구조 (Firebase 중심)
* MVVM + Repository 기반 Android 구조
* Observer 패턴 (SnapshotListener)
* Application 레벨 Lifecycle 관리
* 비동기 이벤트 큐잉 시스템
* 프레임워크 없이 구현한 SPA 관리자 웹

---

## 6. 개발자 역할 (Contribution)

### 프로젝트 역할

* 팀장 (4인 팀 프로젝트)
* Android + Firebase + 관리자 웹 전반 설계 및 개발 주도

---

### 기획 및 설계

* 캠퍼스 통합 서비스 아이디어 제안
* 전체 기능 정의 및 우선순위 설정
* 사용자/운영자 시나리오 설계
* Firestore 데이터 구조 설계

---

### Android 개발

* 로그인, 메인, 주문, 장바구니, 게시판, 지도 전체 구현
* MVVM + Repository 적용
* 실시간 주문 및 팝업 시스템 구현
* 커스텀 UI 및 애니메이션 구현

---

### 관리자 웹 개발 (단독)

* 전체 관리자 시스템 설계 및 구현
* 주문 / 광고 / 게시판 / 메뉴 / 혼잡도 관리 기능 개발
* Firebase 직접 연동 구조 설계

---

### 데이터 및 백엔드(Firebase)

* Firestore 데이터 모델링
* CRUD 및 실시간 동기화 로직 구현
* Batch / Transaction 기반 데이터 정합성 설계

---

### 자동화 시스템 구축

* Python Selenium 크롤러 개발
* 관리자 웹과 연동한 자동 데이터 파이프라인 구축

---

### 품질 관리 및 협업

* 주요 기능 디버깅 및 안정화
* 코드 리팩터링 및 구조 개선
* Git 협업 관리 및 일정 조율
* 발표 자료 및 데모 시나리오 구성

---

## 7. 기술적 성과 (Highlight for Resume)

* 안드로이드 Lifecycle 기반 비동기 이벤트 제어 시스템 설계
* 다중 상점 주문 구조 및 실시간 상태 동기화 아키텍처 구현
* Queue 기반 팝업 재시도 로직으로 UI 안정성 확보
* Firebase만으로 구축한 Serverless 전체 서비스 설계
* Python 크롤링 + Web 대시보드 연동 자동화 파이프라인 구현
* 시간 가중치 기반 혼잡도 추론 알고리즘 설계
* 프레임워크 없이 Vanilla JS로 관리자 SPA 구축
* Firestore Transaction / Batch를 활용한 데이터 무결성 확보

---
