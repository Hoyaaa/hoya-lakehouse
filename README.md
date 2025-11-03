# hoya-lakehouse

**A unified lakehouse of my IT & finance projects — from data pipelines to insights.**

[![Python](https://img.shields.io/badge/Python-3.x-blue)](https://www.python.org/) [![Kotlin](https://img.shields.io/badge/Kotlin-Android-orange)](https://kotlinlang.org/) [![Java](https://img.shields.io/badge/Java-8%2B-red)](https://www.java.com/) [![JavaScript](https://img.shields.io/badge/JavaScript-ES6-yellow)](https://developer.mozilla.org/en-US/docs/Web/JavaScript) [![HTML5](https://img.shields.io/badge/HTML5-orange)](https://developer.mozilla.org/en-US/docs/Glossary/HTML5) [![CSS3](https://img.shields.io/badge/CSS3-blue)](https://developer.mozilla.org/en-US/docs/Web/CSS) [![Flask](https://img.shields.io/badge/Flask-microframework-lightgrey)](https://flask.palletsprojects.com/) [![Selenium](https://img.shields.io/badge/Selenium-Web%20Automation-brightgreen)](https://www.selenium.dev/) [![Firebase](https://img.shields.io/badge/Firebase-Backend-yellow)](https://firebase.google.com/) [![Android](https://img.shields.io/badge/Android-App-green)](https://developer.android.com/) [![Node.js](https://img.shields.io/badge/Node.js-runtime-brightgreen)](https://nodejs.org/) [![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI%2FCD-black)](https://github.com/features/actions) [![Figma](https://img.shields.io/badge/Figma-Design-purple)](https://www.figma.com/)

> 데이터 분석가 지망생 최성호(Hoyaaa)의 포트폴리오 겸 프로젝트 관리소.
> **핵심만 담았습니다.** 상세 설명은 각 프로젝트 폴더의 README에서 확인하세요.

---

## 🧭 Table of Contents

* [Intro](#intro)
* [About This Repository](#about-this-repository)
* [Who Am I](#who-am-i)
* [Certifications](#certifications)
* [Tech Stack](#tech-stack)
* [Architecture (Lakehouse)](#architecture-lakehouse)
* [Project Index](#project-index)

  * [1) 데이터 정제 & 웹 크롤링](#1-데이터-정제--웹-크롤링)
  * [2) 학복관 그 선배 (스마트 캠퍼스 복합 플랫폼)](#2-학복관-그-선배-스마트-캠퍼스-복합-플랫폼)
* [Repository Structure](#repository-structure)
* [Getting Started](#getting-started)
* [Contact](#contact)
* [License](#license)

---

## Intro

데이터·IT·금융 프로젝트를 **Lakehouse** 개념으로 모아 한눈에 보여줍니다.
수집 → 정제 → 저장 → 분석 → 서비스의 흐름에 맞춰, 실행 가능한 예제와 결과물을 제공합니다.

## About This Repository

* 목적: 제가 수행한 프로젝트들을 **데이터 중심**으로 정리하고, 재사용 가능한 형태로 공개
* 범위: Data Engineering · Data Analytics · Quant/Finance · ML · App/Web
* 특징: 프로젝트 간 **공통 데이터 레이어**와 **재현 가능한 실행 가이드** 제공

## Who Am I

* **최성호 (Hoyaaa)** · 데이터 분석가(志)
* 키워드: 데이터 파이프라인 · 웹/앱 프로토타이핑 · 자동화 · 금융/캠퍼스 도메인

## Certifications

보유/준비 현황을 정리했습니다.

* ✅ **MOS PowerPoint 2016**
* 🚧 **ADsP (데이터분석 준전문가)** 준비중
* 🚧 **SQLD** 준비중
* 🚧 **빅데이터 분석기사** 준비중
* 🚧 **정보처리기사** 준비중
* 🚧 **TOEIC Speaking** 준비중
* 🚧 **컴퓨터활용능력 1급** 준비중
* 🚧 **DAoP (데이터 아키텍처 전문가)** 준비중

> *🚧 = In progress / 준비중*

## Tech Stack

**Languages**

* Python, SQL, Java, Kotlin, JavaScript, HTML/CSS, JSON

**Frameworks & Libraries**

* Flask, Selenium, Android (MVVM), Node.js, Firebase SDK,
* (Analytics/ML) pandas, numpy, scikit-learn (필요 시 추가)

**Platforms & DB**

* **Firebase (Firestore, Authentication, Storage)**, GitHub

**Tools**

* Spyder, VS Code, Android Studio, Figma, Postman

**AI/LLM (실험)**

* GPT, Gemini (프로토타입 보조 및 문서화/아이디어 도출)

## Architecture (Lakehouse)

* **Raw Zone**: 웹 크롤링/스크래핑 원천 데이터, 수집 로그
* **Processed Zone**: 정제·전처리 데이터셋 (중복 제거, 결측/이상치 처리)
* **Serving Zone**: 분석 리포트/대시보드, 모델 아웃풋, 앱/웹에서 소비 가능한 API/JSON

```text
source -> ingestion -> cleanse/transform -> store -> analyze/model -> serve (app/web)
```

> 각 프로젝트는 공통 규칙(데이터 폴더, 노트북 규칙, 실행 스크립트)을 따릅니다.

## Project Index

### 1) 데이터 정제 & 웹 크롤링

**핵심**: 다양한 출처에서 안정적으로 데이터를 모아 **정제 가능한 형태**로 저장.

* **Tech**: Python, Selenium/requests/BeautifulSoup, pandas · Spyder
* **산출물**: 수집 스크립트, 정제 노트북, `raw → processed` 데이터 샘플
* **더보기**: `data-engineering/crawling-cleaning/`

---

### 2) 학복관 그 선배 (스마트 캠퍼스 복합 플랫폼)

**핵심**: 모바일 주문·알림·혼잡도·식단 자동수집을 **하나의 서비스로** 통합.

* **System**: Android 앱 · 관리자 웹 · Flask 크롤링 서버 · Firebase 백엔드
* **Tech**: Kotlin/Java, XML, Python(Flask+Selenium), JS/HTML/CSS, Node.js, Firebase, Figma
* **더보기**: `projects/hakbok-sunbae/`

---

### 2) 학복관 그 선배 (스마트 캠퍼스 복합 플랫폼)

**한 줄 소개**: 모바일 주문·조리 완료 알림·혼잡도 예측·식단 자동 수집을 통합한 **캠퍼스 식당 플랫폼**.

* **핵심 기능**

  * 모바일 주문 및 상태 확인 (앱)
  * 조리 완료 **실시간 알림**
  * 시간대별 **혼잡도 예측/시각화**
  * 식단 정보 **자동 크롤링** → 앱 표시
  * 광고/커뮤니티(공지·게시판) 모듈
* **System**: Android 사용자 앱 · 관리자 웹 · Flask 크롤링 서버 · Firebase 백엔드(Firestore/Storage/Auth)
* **Tech**: Kotlin/Java, XML(안드로이드), Node.js, Python(Flask+Selenium), Firebase, JS/HTML/CSS, Figma, GitHub
