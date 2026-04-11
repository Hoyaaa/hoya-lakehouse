# 안드로이드 커스텀 지도 및 동적 경로 애니메이션 모듈

## 1. Overview

본 모듈은 Android 애플리케이션 내에서 특정 공간(캠퍼스 등)의 지도를 기반으로 사용자에게 시각적인 길 안내를 제공하는 커스텀 UI/UX 시스템입니다.

Canvas 기반 커스텀 View와 경로 계산 로직을 결합하여, 사용자가 선택한 목적지까지의 경로를 실시간으로 렌더링하고, 캐릭터가 해당 경로를 따라 이동하는 애니메이션을 동기화하여 제공합니다.

단순한 지도 표시를 넘어, **경로 시각화 + 객체 이동 + 애니메이션 동기화**를 통합 설계한 모듈입니다.

---

## 2. Tech Stack

* Language: Kotlin
* Platform: Android SDK (API Level 21+)
* Graphics: Canvas API (Custom View)
* Animation: ValueAnimator, AnimationDrawable
* Geometry: Path, PathMeasure

---

## 3. Key Features

### 1. 동적 경로 생성 및 렌더링

* 다수의 좌표(PointF)를 기반으로 Path 생성
* PathMeasure를 통해 전체 경로 길이 계산
* 진행률 기반으로 선이 그려지는 애니메이션 구현

### 2. 경로 기반 캐릭터 이동 애니메이션

* ValueAnimator를 이용한 위치 보간(interpolation)
* PathMeasure.getPosTan으로 실시간 좌표 및 방향 계산
* atan2 기반 회전 처리로 자연스러운 이동 구현

### 3. 프레임 기반 보행 애니메이션

* AnimationDrawable을 활용한 캐릭터 보행 효과
* 이동 애니메이션과 프레임 애니메이션 동기화

### 4. 모듈화된 팝업 시스템

* GenericDialogFragment를 통한 UI 재사용
* 건물별 상세 정보 화면을 동적으로 렌더링

---

## 4. Architecture Flow

```id="map-flow-01"
MapActivity 진입
    ->
지도 및 UI 초기화
    ->
사용자 목적지 선택
    ->
경로 좌표 리스트 생성
    ->
Path 구성 및 길이 계산
    ->
ValueAnimator 시작
    ->
[애니메이션 진행]
    ->
RouteLineView -> 경로 점진적 렌더링
    ->
캐릭터 -> 경로 따라 이동 + 회전
    ->
애니메이션 종료
    ->
캐릭터 숨김
    ->
GenericDialogFragment 호출
```

---

## 5. Technical Specification

### 1. MapActivity.kt

**역할**: 경로 계산 및 애니메이션 제어를 담당하는 메인 컨트롤러

**구현 상세**

* 좌표 계산

  * `getCenterPoint()`를 통한 동적 중심 좌표 추출
  * 목적지별 경로를 PointF 리스트로 구성

* 애니메이션 제어

  * ValueAnimator로 전체 경로 길이 기반 진행률 계산
  * PathMeasure.getPosTan을 통해 위치 및 방향 추출

* 회전 처리

  * `atan2(dy, dx)`를 활용하여 자연스러운 방향 회전 구현

---

### 2. RouteLineView.kt

**역할**: 경로를 시각적으로 렌더링하는 Custom View

**구현 상세**

* Path 생성 및 길이 측정
* `updateProgress(fraction)`로 외부 애니메이션과 동기화
* `onDraw()`에서 PathMeasure.getSegment() 사용

  * 전체 경로 중 일부만 잘라서 렌더링
* invalidate() 호출로 실시간 UI 업데이트

---

### 3. WalkingAnimation.kt

**역할**: 보행 캐릭터 애니메이션 생성 유틸리티

**구현 상세**

* AnimationDrawable 구성

  * 발걸음 프레임 이미지 순환
  * 반복 애니메이션 설정

* 디바이스 대응

  * dp → px 변환으로 해상도 대응

---

### 4. GenericDialogFragment.kt

**역할**: 재사용 가능한 모듈형 다이얼로그

**구현 상세**

* newInstance 패턴으로 Layout ID 전달
* 다양한 UI를 단일 클래스에서 처리
* 공통 닫기 로직 구현으로 중복 코드 제거

---

## 6. Core Technical Concepts

* PathMeasure 기반 경로 길이 계산 및 위치 보간
* 좌표 기반 그래픽 렌더링(Canvas)
* 애니메이션 동기화 (경로 + 캐릭터)
* 벡터 방향 계산 (atan2 활용)
* Custom View 라이프사이클 이해

---

## 7. Design Considerations

* 애니메이션과 UI 렌더링을 분리하여 **유지보수성 확보**
* Path 기반 설계를 통해 **확장 가능한 경로 시스템 구현**
* 진행률(fraction) 기반 제어로 **유연한 애니메이션 처리**
* 재사용 가능한 Dialog 구조로 **UI 모듈화**

---

## 8. Highlight for Resume

* Canvas 기반 Custom View를 활용한 경로 렌더링 시스템 구현
* PathMeasure를 이용한 좌표 계산 및 애니메이션 동기화 처리
* ValueAnimator와 Geometry 계산을 결합한 객체 이동 로직 설계
* 프레임 애니메이션과 위치 애니메이션을 통합한 UX 구현
* 모듈화된 UI 구조 설계로 재사용성 및 유지보수성 향상

---

## 9. Improvement Ideas

* 실제 지도 API (Google Maps 등)와 연동
* 경로 자동 탐색 알고리즘 적용 (A*, Dijkstra)
* Bézier Curve 기반 부드러운 경로 개선
* FPS 최적화 및 하드웨어 가속 활용
* 사용자 위치 기반 실시간 경로 업데이트

---
