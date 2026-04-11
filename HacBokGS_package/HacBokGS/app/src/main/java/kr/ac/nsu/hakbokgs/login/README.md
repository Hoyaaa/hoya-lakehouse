# Firebase 기반 Google 로그인 및 사용자 온보딩 시스템

## Overview

본 모듈은 Android 애플리케이션에서 Firebase Authentication과 Google Sign-In API를 활용하여 **간편하고 안전한 사용자 인증 시스템**을 제공합니다.

로그인 이후 Firestore와 연동하여 사용자 상태를 판별하고,
신규 사용자에게는 추가 정보를 입력받는 **온보딩(Onboarding) 프로세스**를 구현했습니다.

---

## Tech Stack

* Language: Java
* Platform: Android (SDK)
* Backend: Firebase Authentication, Cloud Firestore
* API: Google Sign-In API

---

## Key Features

### 1. Google Single Sign-On (SSO)

* 구글 계정 기반 원클릭 로그인
* 기존 로그인 세션 유지 및 자동 로그인 지원

### 2. 사용자 상태 기반 라우팅

* Firestore `users/{email}` 기준 사용자 존재 여부 확인
* 기존 회원 → MainActivity 이동
* 신규 회원 → 추가 정보 입력 화면(UserInformation) 이동

### 3. 사용자 정보 수집 및 검증

* 생년월일: 8자리 정규식 검증
* 전화번호: 11자리 정규식 검증
* 필수 입력값 및 약관 동의 체크
* 예외 상황 시 Toast 메시지 제공

### 4. 비동기 처리 기반 UX 개선

* Firebase Auth 및 Firestore 통신 시 ProgressBar 적용
* 사용자에게 명확한 로딩 상태 제공

---

## Architecture Flow

```
GoogleLogin Activity 진입
    ↓
기존 로그인 세션 확인 (자동 로그인)
    ↓
Google Sign-In 인증 (ID Token 발급)
    ↓
Firebase Authentication 인증
    ↓
Firestore 사용자 조회 (users/{email})
    ↓
[기존 사용자] → MainActivity
[신규 사용자] → UserInformation Activity
    ↓
(신규 사용자)
추가 정보 입력 및 검증
    ↓
Firestore 저장
    ↓
MainActivity 이동
```

---

## Technical Details

### GoogleLogin.java

**역할:** Google 인증 + Firebase Auth 연동 + 사용자 라우팅

* `onCreate()`

  * 기존 로그인 세션 확인 → 자동 로그인 처리

* `onActivityResult()`

  * Google 로그인 결과 수신 → Firebase 인증으로 전달

* `firebaseAuthWithGoogle()`

  * ID Token → Firebase Credential 변환 후 로그인 처리

* `checkUserInFirestore()`

  * Firestore `users` 컬렉션 조회
  * 사용자 존재 여부에 따라 화면 분기

* `showLoading()` / `hideLoading()`

  * 인증 및 DB 조회 중 로딩 UI 처리

---

### UserInformation.java

**역할:** 신규 사용자 정보 입력 및 저장

* `loadUserName()`

  * Firebase Auth 사용자 이메일 UI 바인딩

* `saveUserData()`

  * 입력값 검증 로직

    * 공백 체크
    * 생년월일 / 전화번호 정규식 검증
    * 약관 동의 여부 확인

* `saveDataToFirestore()`

  * 사용자 정보를 Firestore에 저장 (`users/{email}`)
  * 저장 성공 시 MainActivity로 이동

---
