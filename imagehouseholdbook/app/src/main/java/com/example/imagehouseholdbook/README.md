# 프로젝트명: OCR 기반 영수증 자동 인식 및 클라우드 가계부 시스템

## 1. 개요 (Overview)

본 프로젝트는 사용자가 촬영한 영수증 이미지를 자동으로 분석하여 상품명, 가격, 수량 등의 정보를 추출하고 이를 클라우드에 저장하는 **지능형 가계부 애플리케이션**입니다.

Google ML Kit과 외부 OCR API를 결합하여 이미지 기반 데이터를 구조화된 텍스트로 변환하고, Firebase 및 Google Sheets에 동시 저장하는 데이터 파이프라인을 구축했습니다.

이를 통해 수동 입력 중심의 기존 가계부 방식에서 벗어나 **자동화된 데이터 수집 및 관리 환경**을 구현했습니다.

---

## 2. 기술 스택 (Tech Stack)

* **Language**: Kotlin
* **Platform**: Android SDK
* **Architecture**: MVVM Pattern, Repository Pattern, Singleton Pattern
* **Cloud / Backend**: Firebase (Authentication, Firestore, Storage), Google Sheets API v4
* **Vision / OCR**: Google ML Kit Document Scanner, Naver OCR API
* **Network**: Retrofit2, OkHttp3
* **Concurrency**: Kotlin Coroutines, LifecycleScope
* **UI**: RecyclerView, ViewBinding

---

## 3. 핵심 기능 (Key Features)

### 3.1. 영수증 이미지 스캔 및 OCR 자동화

* ML Kit Document Scanner를 활용하여 영수증 영역 자동 인식 및 보정
* 촬영된 이미지를 Firebase Storage에 업로드
* OCR API를 통해 텍스트 데이터 추출 및 구조화
* 상품명, 단가, 수량 등 핵심 데이터 자동 파싱

---

### 3.2. 실시간 분석 상태 추적 시스템

* Firestore `receiptJobs` 컬렉션 기반 작업 상태 관리
* SnapshotListener를 통한 실시간 상태 반영

  * 업로드 중 → 분석 중 → 완료
* 서버 OCR 처리 완료 시 자동 후속 로직 실행

---

### 3.3. Google Sheets 기반 외부 데이터 동기화

* Firebase 데이터와 별도로 Google Sheets에 데이터 이중 저장
* 시트 자동 생성 및 헤더 구성 로직 구현
* 사용자 계정 기반 OAuth2 인증 적용
* 데이터 활용성을 고려한 외부 협업 환경 제공

---

### 3.4. 데이터 그룹화 및 UI 최적화

* 구매 날짜 + 가맹점 기준 데이터 그룹화
* 다수 상품 데이터를 대표 텍스트 형태로 요약

  * 예: “상품A 외 N건”
* RecyclerView 기반 리스트 구성으로 가독성 향상

---

### 3.5. 사용자 인증 및 권한 관리

* Google Sign-In 기반 간편 로그인
* Firebase Authentication 연동
* Google Sheets 접근 권한에 대한 런타임 권한 검증 처리

---

## 4. 시스템 흐름 (Flow)

```id="ocr-flow-01"
[사용자 입력]
영수증 촬영 -> 이미지 보정 및 업로드

-> Firebase Storage 저장
-> Firestore Job 생성

-> OCR 서버 처리
-> 결과 Firestore 업데이트

-> 앱에서 실시간 상태 감지
-> 데이터 파싱 및 UI 반영

-> Google Sheets API 호출
-> 외부 시트 데이터 저장
```

---

## 5. 컴포넌트 구성 (Component Breakdown)

### 1) CameraActivity.kt

* 영수증 촬영 및 이미지 스캔
* Firebase Storage 업로드
* OCR 작업 상태 실시간 구독

---

### 2) SheetService.kt

* Google Sheets API 연동
* 시트 생성 및 데이터 기록
* OAuth 기반 인증 처리

---

### 3) MainActivity.kt

* 전체 영수증 데이터 리스트 표시
* 그룹화 및 UI 가공 로직 처리

---

### 4) DetailActivity.kt

* 개별 영수증 상세 데이터 표시
* 상품 단위 정보 출력

---

### 5) LoginActivity / StartActivity

* Google 로그인 및 Firebase 인증 처리
* 사용자 초기 데이터 생성

---

### 6) NaverOcrInterface.kt

* OCR API 통신을 위한 Retrofit 인터페이스
* 서버 요청/응답 구조 정의

---

### 7) HouseholdData.kt

* 데이터 모델 정의
* UI 그룹화 모델 포함

---

## 6. 기술적 성과 및 문제 해결 (Highlight for Resume)

* **OCR 기반 데이터 자동화 시스템 구축**

  * 이미지 → 텍스트 → 구조화 데이터까지 이어지는 파이프라인 구현

* **비동기 처리 최적화**

  * Coroutine + LifecycleScope를 활용하여 네트워크 작업과 UI 스레드 분리

* **실시간 상태 기반 아키텍처 설계**

  * Firestore SnapshotListener 기반 Job-Status 패턴 도입

* **데이터 정합성 확보**

  * 가변 길이 영수증 데이터를 DTO 구조로 관리하여 안정적인 처리

* **외부 API 통합 경험**

  * Firebase + Google Sheets + OCR API를 통합한 멀티 클라우드 구조 설계

* **확장성 고려 설계**

  * 데이터 모델 및 서비스 계층 분리를 통한 유지보수성 확보

---

## 7. 확장 가능성 (Future Improvements)

* OCR 정확도 향상을 위한 후처리 알고리즘 개선
* 카테고리 자동 분류 (식비, 교통비 등) 기능 추가
* 통계 및 시각화 기능 (월별 소비 분석) 확장
* 오프라인 저장 및 동기화 기능 추가

---
