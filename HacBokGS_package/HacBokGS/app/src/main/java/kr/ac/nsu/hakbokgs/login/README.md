# [cite_start]Firebase 기반 구글 로그인 및 사용자 온보딩 시스템 [cite: 3]

## [cite_start]개요 (Overview) [cite: 3]
[cite_start]본 모듈은 Android 애플리케이션에서 Firebase Authentication 과 Google Sign-In API 를 활용하여 안전하고 간편한 사용자 인증을 제공합니다. [cite: 3] [cite_start]로그인 성공 후 Firebase Firestore 와 연동하여 신규 가입자와 기존 회원을 구분하고, 신규 가입자에게는 추가 정보(이름, 생년월일, 연락처) 입력을 유도하는 온보딩(Onboarding) 프로세스 를 구현했습니다. [cite: 4]

## [cite_start]기술 스택 (Tech Stack) [cite: 5]
* [cite_start]Language: Java [cite: 5]
* [cite_start]Platform: Android (SDK) [cite: 5]
* [cite_start]BaaS (Backend as a Service): Firebase Authentication, Cloud Firestore [cite: 5]
* [cite_start]API: Google Sign-In API [cite: 5]

## [cite_start]주요 기능 (Key Features) [cite: 5]
1.  [cite_start]**Google Single Sign-On (SSO)**: 구글 계정을 이용한 원클릭 로그인 및 자동 로그인 지원. [cite: 5]
2.  [cite_start]**신규/기존 사용자 라우팅 (User Routing)**: 로그인된 이메일을 기반으로 Firestore 데이터베이스를 조회하여, 기존 회원은 메인 화면으로, 신규 회원은 정보 입력 화면으로 분기 처리. [cite: 6]
3.  **사용자 정보 수집 및 유효성 검사 (Data Validation)**: 
    * [cite_start]생년월일(8자리) 및 전화번호(11자리) 정규식 기반 검증. [cite: 7]
    * [cite_start]필수 약관 동의 체크 및 예외 처리(Toast 메시지). [cite: 8]
4.  [cite_start]**비동기 처리 및 UX 개선**: 네트워크 통신 시 ProgressBar를 활용하여 사용자에게 직관적인 로딩 상태(Loading UI) 제공. [cite: 8]

---

## [cite_start]시나리오 및 아키텍처 흐름 (Flow) [cite: 9]
1.  [cite_start]GoogleLogin 화면 진입 -> 기존 로그인 세션 확인 (세션 존재 시 자동 로그인). [cite: 9]
2.  [cite_start]구글 로그인 버튼 클릭 -> GoogleSignInClient를 통해 인증 토큰(ID Token) 발급. [cite: 10]
3.  [cite_start]Firebase Auth 연동 -> 발급받은 토큰으로 Firebase Credential 인증. [cite: 11]
4.  [cite_start]Firestore 회원 검증 ( users/{userEmail} ) [cite: 12]
    * [cite_start]데이터 있음 (기존 회원): MainActivity로 이동. [cite: 12]
    * [cite_start]데이터 없음 (신규 회원): UserInformation 화면으로 이동. [cite: 13]
5.  [cite_start]UserInformation (신규 회원) -> 추가 정보 입력 및 유효성 검사 -> Firestore에 데이터 저장 -> MainActivity로 이동. [cite: 13]

---

## [cite_start]기술 명세 (Technical Specification) [cite: 14]

### [cite_start]1. GoogleLogin.java [cite: 14]
[cite_start]**역할**: Google Sign-In API와 Firebase Auth를 연결하여 사용자 인증을 담당하고 회원의 상태에 따라 라우팅을 수행하는 클래스. [cite: 14]
* `onCreate()` / `onActivityResult()`: 
    * [cite_start]이전에 로그인한 계정(getLastSignedInAccount)이 있는지 확인하여 자동 로그인 을 처리합니다. [cite: 15]
    * [cite_start]구글 로그인 Intent 결과를 받아와 성공 시 Firebase 인증 프로세스로 넘깁니다. [cite: 16]
* `firebaseAuthWithGoogle()`: 
    * [cite_start]구글에서 발급받은 idToken을 GoogleAuthProvider에 전달하여 Firebase 사용자 세션을 생성합니다. [cite: 17]
* `checkUserInFirestore()`: 
    * [cite_start]Firebase 인증에 성공한 사용자의 이메일을 Document ID로 삼아 Firestore의 users 컬렉션을 조회합니다. [cite: 18]
    * [cite_start]비동기 리스너(addOnSuccessListener)를 활용해 데이터 존재 여부에 따라 화면 전환(MapsToMain 또는 MapsToUserInformation)을 수행합니다. [cite: 19]
* **UX/UI 처리** (`showLoading`, `hideLoading`): 
    * [cite_start]인증 및 DB 조회 과정에서 발생할 수 있는 지연 시간 동안 ProgressBar를 노출하여 앱의 응답성을 보완합니다. [cite: 20]

### [cite_start]2. UserInformation.java [cite: 21]
[cite_start]**역할**: 신규 사용자의 추가 프로필 정보를 수집하고 유효성을 검사한 뒤 Firestore에 저장하는 클래스. [cite: 21]
* `onCreate()` & `loadUserName()`: 
    * [cite_start]Firebase Auth를 통해 현재 로그인된 사용자의 이메일을 가져와 UI에 바인딩합니다. [cite: 22]
* `saveUserData()`: 
    * [cite_start]사용자가 입력한 데이터의 무결성을 검증하는 비즈니스 로직을 포함합니다. [cite: 23]
    * [cite_start]isEmpty() 체크, 정규식(matches("\d{8}"), matches("\d{11}"))을 이용한 형식 검사, 약관 동의(isChecked()) 여부를 검증합니다. [cite: 24]
* `saveDataToFirestore()`: 
    * [cite_start]검증이 완료된 데이터를 HashMap 형태로 구성하여 Firestore의 users 컬렉션에 사용자 이메일을 키(Document ID)로 하여 set() 병합 저장합니다. [cite: 25]
    * [cite_start]저장 성공 시 즉각적인 UI 업데이트(사용자 이름 표시) 및 MainActivity로의 전환을 수행합니다. [cite: 26]

---
