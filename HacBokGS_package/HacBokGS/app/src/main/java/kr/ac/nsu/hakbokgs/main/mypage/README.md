# 안드로이드 마이페이지 및 사용자 프로필 관리 모듈

## 1. Overview

본 모듈은 Android 애플리케이션 내에서 사용자 프로필 데이터를 조회, 수정 및 관리하는 마이페이지 시스템입니다.

Firebase Authentication, Cloud Firestore, Cloud Storage를 연동하여
사용자 인증 → 데이터 조회 → 이미지 업로드 → DB 동기화까지 이어지는 **End-to-End 사용자 관리 파이프라인**을 구현했습니다.

특히, 비동기 처리 흐름을 안정적으로 구성하여 **데이터 일관성과 사용자 경험(UX)**을 동시에 고려한 구조로 설계했습니다.

---

## 2. Tech Stack

* Language: Java
* Platform: Android (SDK)
* Backend: Firebase Authentication, Cloud Firestore, Cloud Storage
* Library: Glide

---

## 3. Key Features

### 1. 프로필 이미지 업로드 파이프라인

* 갤러리 이미지 선택 → Firebase Storage 업로드
* 다운로드 URL 생성 → Firestore에 동기화
* UI 즉시 반영

### 2. 사용자 데이터 비동기 바인딩

* Firebase Auth 기반 현재 사용자 식별
* Firestore에서 사용자 데이터 조회
* Glide를 통한 이미지 로딩 및 캐싱

### 3. 사용자 정보 실시간 수정

* 커스텀 다이얼로그 기반 이름 변경
* Firestore 업데이트 후 UI 즉시 반영

### 4. 통합 네비게이션 및 외부 라우팅

* 앱 내부 Activity 간 이동 구조
* 외부 URL(Intent.ACTION_VIEW) 연결 지원

---

## 4. Architecture Flow

```id="mypage-flow-01"
MypageActivity 진입
    ->
Firebase Auth 사용자 확인
    ->
Firestore 사용자 데이터 조회
    ->
UI 바인딩 (이름 + 프로필 이미지)
    ->
[사용자 인터랙션]
    ->
프로필 이미지 클릭
    ->
갤러리 호출 (ActivityResultLauncher)
    ->
이미지 선택
    ->
Firebase Storage 업로드
    ->
Download URL 획득
    ->
Firestore 업데이트
    ->
UI 즉시 반영
    ->
이름 변경 버튼 클릭
    ->
다이얼로그 입력
    ->
Firestore 업데이트
    ->
UI 갱신
```

---

## 5. Technical Specification

### MypageActivity.java

**역할**
사용자 프로필 데이터 관리 및 UI 인터랙션을 담당하는 메인 컨트롤러

---

### 구현 상세

#### 1. Activity Result API 적용

* `ActivityResultLauncher` 사용
* 기존 `startActivityForResult` 대체
* Lifecycle-safe 구조로 메모리 누수 방지

---

#### 2. 비동기 처리 체이닝

* 이미지 업로드 흐름

  * `putFile()` → 업로드
  * `getDownloadUrl()` → URL 획득
  * Firestore 업데이트

* 단계별 비동기 연결 구조 설계

* 실패 가능 지점 분리 및 예외 대응

---

#### 3. 데이터 동기화 전략

* Storage와 Firestore 간 데이터 일관성 유지
* URL을 단일 소스로 관리
* UI는 Firestore 기준으로 렌더링

---

#### 4. 이미지 렌더링 최적화

* Glide 활용

  * 비동기 로딩
  * 캐싱 적용
  * circleCrop 변환

* placeholder 이미지 적용

  * 네트워크 지연 대응

---

#### 5. 커스텀 다이얼로그

* LayoutInflater 기반 커스텀 뷰 적용
* 입력값 검증 후 Firestore 반영
* UI 즉시 업데이트

---

## 6. Design Considerations

* 인증(Auth), 저장(Storage), DB(Firestore) 역할 분리
* 비동기 처리 흐름을 단계별로 분리하여 안정성 확보
* UI는 항상 DB 기준으로 동기화하여 데이터 일관성 유지
* 최신 Android API 적용으로 Lifecycle 안정성 확보

---

## 7. Core Technical Concepts

* Firebase 서비스 간 데이터 흐름 설계
* 비동기 콜백 체이닝 구조
* Activity Lifecycle-safe API 활용
* 이미지 캐싱 및 렌더링 최적화
* UI 상태와 데이터 상태 동기화

---

## 8. Highlight for Resume

* Firebase Authentication, Firestore, Storage를 연동한 사용자 관리 시스템 구현
* 이미지 업로드 → URL 생성 → DB 반영까지의 비동기 파이프라인 설계
* ActivityResult API를 활용한 최신 Android 아키텍처 적용
* Glide 기반 이미지 로딩 최적화 및 UX 개선
* 사용자 데이터 변경 시 실시간 UI 동기화 구조 구현

---

## 9. Improvement Ideas

* 이미지 압축 및 리사이징 처리 추가
* 프로필 이미지 버전 관리 전략 적용
* MVVM + LiveData 구조로 리팩토링
* 업로드 실패 시 롤백 처리 (트랜잭션 유사 구조)
* Firebase Security Rules 강화

---
