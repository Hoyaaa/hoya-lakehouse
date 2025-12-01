from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import time
import re
import firebase_admin
from firebase_admin import credentials, firestore
import os

# 🔐 Firebase Admin SDK 초기화
current_dir = os.path.dirname(__file__)
cred_path = os.path.join(current_dir, "firebase-adminsdk.json")
cred = credentials.Certificate(cred_path)
firebase_admin.initialize_app(cred)
db = firestore.client()

# 🧭 Selenium 설정
options = webdriver.ChromeOptions()
options.add_argument('--headless')
service = Service(ChromeDriverManager().install())
driver = webdriver.Chrome(service=service, options=options)

try:
    # 카카오채널 접속
    url = "https://pf.kakao.com/_fqhmxj/posts"
    driver.get(url)
    time.sleep(3)

    # 게시물 제목에서 주차 및 월 추출
    titles = driver.find_elements(By.CSS_SELECTOR, "strong.tit_card")
    latest_title = ""
    latest_anchor = None
    for title in titles:
        text = title.text.strip()
        if "학생식당 메뉴 안내" in text:
            latest_title = text
            latest_anchor = title.find_element(By.XPATH, "./ancestor::a[1]")
            break

    if not latest_anchor:
        raise Exception("❌ 게시물 찾기 실패")

    # 게시물 클릭 및 새 탭 전환
    latest_anchor.click()
    time.sleep(2)
    driver.switch_to.window(driver.window_handles[-1])
    time.sleep(2)

    post_text = driver.find_element(By.CSS_SELECTOR, "div.desc_card").text

    # 🍽️ 오늘의 메뉴 섹션만 추출
    menu_match = re.search(r"🍽️ 오늘의 메뉴(.*?)(?=\n\n|\Z)", post_text, re.DOTALL)
    if not menu_match:
        raise Exception("❌ '오늘의 메뉴' 섹션 없음")

    menu_section = menu_match.group(1).strip()

    # 요일별 데이터 정리
    days_kr = ["월요일", "화요일", "수요일", "목요일", "금요일"]
    days_en = ["Mon", "Tue", "Wed", "Thu", "Fri"]
    menu_data = {}

    for kr, en in zip(days_kr, days_en):
        pattern = rf"✔️\s*{kr}\s*:\s*(.*)"
        match = re.search(pattern, menu_section)
        menu = match.group(1).strip() if match else "메뉴 없음"
        menu_data[en] = menu

    # 🔑 문서 ID 만들기: 예시 → 4월 둘째 주 → 4th_2week_menu
    nth_week_match = re.search(r"(\d+)월\s+(\S+?)째 주", latest_title)
    if not nth_week_match:
        raise Exception("❌ 제목에서 주차 파싱 실패")

    month_num = nth_week_match.group(1)
    week_str = nth_week_match.group(2)
    week_dict = {"첫": "1", "둘": "2", "셋": "3", "넷": "4", "다섯": "5"}
    week_num = week_dict.get(week_str, "X")
    doc_id = f"{month_num}th_{week_num}week_menu"

    # 🔥 Firebase에 저장
    db.collection("today's_menu").document(doc_id).set(menu_data)

    print(f"✅ Firestore에 저장 완료: Document ID = {doc_id}")
    print("📦 저장 내용:", menu_data)

finally:
    driver.quit()
