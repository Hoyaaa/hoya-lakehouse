# -*- coding: utf-8 -*-
import os
import csv
import time
import pandas as pd

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import (
    TimeoutException, ElementClickInterceptedException, NoSuchElementException
)
from webdriver_manager.chrome import ChromeDriverManager


# ===== 설정 =====
EXCEL_PATH = r"D:\재택근무\11월\11월_4주차\data\입찰공고번호.xlsx"
G2B_URL = "https://www.g2b.go.kr/"
TBODY_ID = "mf_wfm_container_tacBidPbancLst_contents_tab2_body_gridView1_body_tbody"
OUT_CSV = r"D:\재택근무\11월\11월_4주차\data\입찰공고_추출결과.csv"


# ===== 유틸 =====
def load_bid_numbers(path: str):
    print("3단계: 엑셀 로드 시작 →", path)
    df = pd.read_excel(path, dtype=str)  # 값 그대로 읽기
    col = next((c for c in df.columns if c in ["입찰공고번호", "공고번호"]), df.columns[0])
    nums = [str(v).strip() for v in df[col].tolist() if pd.notna(v) and str(v).strip()]
    print(f"3단계 완료: 공고번호 {len(nums)}건 적재 (예시 3건) → {nums[:3]}")
    return nums


def text_or_value(el):
    tag = el.tag_name.lower()
    return (el.get_attribute("value") or "").strip() if tag in ("input", "textarea") else el.text.strip()


def find_first_text(driver, xpaths):
    for xp in xpaths:
        try:
            el = driver.find_element(By.XPATH, xp)
            t = text_or_value(el)
            if t:
                return t
        except NoSuchElementException:
            continue
    return ""


def save_csv(path, rows, header):
    exists = os.path.isfile(path)
    with open(path, "a", newline="", encoding="utf-8-sig") as f:
        w = csv.DictWriter(f, fieldnames=header)
        if not exists:
            w.writeheader()
        for r in rows:
            w.writerow(r)
    print(f"7단계: CSV 저장 완료 → {path} (+{len(rows)}건)")


def get_first_visible_cell_by_header(driver, header_text: str, grid_id_part="gridList1", wait_sec=10):
    """
    헤더 텍스트로 th의 col_id를 찾고, 같은 col_id의 바디 셀들 중
    화면에 표시되는 첫 유효 텍스트를 반환한다.
    """
    # 1) 헤더(th) → col_id
    th = WebDriverWait(driver, wait_sec).until(
        EC.presence_of_element_located((
            By.XPATH,
            f"//th[contains(@id,'{grid_id_part}_column')][.//nobr[normalize-space()='{header_text}']]"
        ))
    )
    col_id = th.get_attribute("col_id") or th.get_attribute("header_id")

    # 2) 해당 컬럼의 모든 바디 nobr 수집 (행 번호 고정 X)
    nobrs = driver.find_elements(
        By.XPATH,
        f"//td[@col_id='{col_id}'][contains(@id,'{grid_id_part}_cell_')]//nobr"
    )

    # 3) 화면에 보이는 첫 유효 텍스트
    for nobr in nobrs:
        try:
            if nobr.is_displayed():
                txt = nobr.text.strip()
                if txt and txt != "-":
                    return txt
        except Exception:
            continue

    # 4) 백업: 해당 컬럼 내 날짜/시간 형태 텍스트
    backup = driver.find_elements(
        By.XPATH,
        f"//td[@col_id='{col_id}'][contains(@id,'{grid_id_part}_cell_')]//*[contains(text(),'/') or contains(text(),'-') or contains(text(),':')]"
    )
    for el in backup:
        try:
            if el.is_displayed():
                t = el.text.strip()
                if t:
                    return t
        except Exception:
            continue

    # 찾지 못하면 빈 문자열
    return ""


# ===== 팝업/네비게이션 =====
def dismiss_popup_if_present(driver, timeout=3):
    """안내 메시지 팝업이 있으면 닫는다. 닫았으면 True, 없으면 False."""
    try:
        box = WebDriverWait(driver, timeout).until(
            EC.visibility_of_element_located(
                (By.XPATH, "//*[contains(@class,'w2window') and contains(@class,'messagebox')]")
            )
        )
    except TimeoutException:
        return False  # 팝업 없음

    # 확인 버튼 우선
    for xp in [
        ".//input[@type='button' and @value='확인']",
        ".//button[@type='button' and (@title='확인' or contains(.,'확인'))]",
        ".//*[contains(@id,'_btnConfirm') and (self::input or self::button)]",
    ]:
        try:
            btn = box.find_element(By.XPATH, xp)
            driver.execute_script("arguments[0].click();", btn)
            print("팝업 처리: [확인] 버튼 클릭")
            time.sleep(0.2)
            return True
        except Exception:
            pass

    # 상단 X(닫기)
    try:
        close_btn = box.find_element(By.XPATH, ".//*[contains(@class,'w2window_close')]")
        driver.execute_script("arguments[0].click();", close_btn)
        print("팝업 처리: 닫기(X) 버튼 클릭")
        time.sleep(0.2)
        return True
    except Exception:
        pass

    print("⚠️ 팝업 발견했으나 닫기 요소 미검출")
    return False


def go_back_to_list(driver, wait, list_handle):
    """상세에서 목록으로 복귀 (새 탭이면 닫고, 동일 탭이면 뒤로가기)"""
    if len(driver.window_handles) > 1 and driver.current_window_handle != list_handle:
        driver.close()
        driver.switch_to.window(list_handle)
    else:
        driver.back()
    WebDriverWait(driver, 15).until(EC.presence_of_element_located((By.ID, TBODY_ID)))
    time.sleep(0.3)


def ensure_on_list(driver, wait, list_handle):
    """뒤로가기 후 목록 tbody가 보이는지 보장."""
    try:
        WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.ID, TBODY_ID)))
        return
    except TimeoutException:
        pass

    # 목록 탭으로 전환 시도
    try:
        if list_handle in driver.window_handles and driver.current_window_handle != list_handle:
            driver.switch_to.window(list_handle)
    except Exception:
        pass

    try:
        WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.ID, TBODY_ID)))
        return
    except TimeoutException:
        print("⚠️ 목록 tbody 미검출 → 페이지 재진입 시도")
        go_to_bid_list(driver, wait)


# ===== 단계별 동작 =====
def go_to_bid_list(driver, wait):
    print("1단계: 웹 페이지 접속 시작")
    driver.get(G2B_URL)
    print(f"1단계 완료: 웹 페이지 접속 성공 → {G2B_URL}")

    actions = ActionChains(driver)
    print("2단계: 상단바 '입찰' 메뉴 이동")
    bid_btn = wait.until(EC.presence_of_element_located(
        (By.XPATH, "//span[normalize-space()='입찰']/ancestor::*[contains(@id,'btn_menuLvl1')][1]")
    ))
    actions.move_to_element(bid_btn).pause(0.2).perform()
    print("2단계: '입찰' 오버 완료")

    bid_list_btn = wait.until(EC.visibility_of_element_located(
        (By.XPATH, "//span[normalize-space()='입찰공고목록']/ancestor::*[contains(@id,'btn_menuLvl3')][1]")
    ))
    actions.move_to_element(bid_list_btn).pause(0.1).click().perform()
    print("2단계 완료: '입찰공고목록' 진입 중...")

    time.sleep(1)
    if len(driver.window_handles) > 1:
        driver.switch_to.window(driver.window_handles[-1])
        print("2단계: 새 탭 전환 완료 →", driver.current_url)
    else:
        print("2단계: 동일 탭 로딩 →", driver.current_url)


def search_bid_number(driver, wait, bid_no: str):
    """4단계: 공고번호 입력 후 '검색' 버튼 클릭."""
    print(f"4단계: 공고번호 입력 준비 → {bid_no}")
    input_id = "mf_wfm_container_tacBidPbancLst_contents_tab2_body_ibxBidPbancNo"
    search_btn_id = "mf_wfm_container_tacBidPbancLst_contents_tab2_body_btnS0004"

    input_el = wait.until(EC.element_to_be_clickable((By.ID, input_id)))
    input_el.clear()
    input_el.send_keys(bid_no)
    print("4단계: 공고번호 입력 완료")

    search_btn = wait.until(EC.element_to_be_clickable((By.ID, search_btn_id)))
    search_btn.click()
    print("4단계 완료: 검색 버튼 클릭")


def click_bid_title(driver, wait, bid_no: str):
    """5단계: 결과 테이블에서 해당 공고번호 행의 공고명 링크 클릭."""
    print(f"5단계: '{bid_no}' 행의 공고명 링크 클릭")
    xpath = (
        f"//tbody[@id='{TBODY_ID}']"
        f"//td[@col_id='bidPbancUntyNoOrd']/nobr[normalize-space()='{bid_no}']"
        f"/ancestor::tr[1]//td[@col_id='bidPbancNm']//a"
    )
    link = wait.until(EC.visibility_of_element_located((By.XPATH, xpath)))
    driver.execute_script("arguments[0].scrollIntoView({block:'center'});", link)
    try:
        wait.until(EC.element_to_be_clickable((By.XPATH, xpath)))
        link.click()
        print("5단계 완료: 공고명 링크 클릭 성공")
    except (ElementClickInterceptedException, TimeoutException):
        driver.execute_script("arguments[0].click();", link)
        print("5단계 완료: 공고명 링크 JS 클릭 성공")

    time.sleep(0.5)
    if len(driver.window_handles) > 1:
        driver.switch_to.window(driver.window_handles[-1])
        print("5단계: 새 탭 전환 →", driver.current_url)
    else:
        print("5단계: 동일 탭 이동 →", driver.current_url)


def parse_detail(driver):
    """상세 페이지에서 6개 항목 추출"""
    print("6단계: 상세 페이지 데이터 추출 시작")

    # 1) 입찰공고번호
    bid_no = find_first_text(driver, [
        "//input[@title='입찰공고번호']",
        "//td[@data-title='입찰공고번호']//*[self::input or self::span]"
    ])

    # 2) 공고기관
    org = find_first_text(driver, [
        "//input[@title='공고기관']",
        "//td[@data-title='공고기관']//*[self::input or self::span]"
    ])

    # 3) 공고명
    title = find_first_text(driver, [
        "//td[@data-title='공고명']//span[contains(@class,'w2textbox')]",
        "//td[@data-title='공고명']"
    ])

    # 4) 사업금액 (우선순위: 사업금액 → 배정예산 → 추정가격 → 요약버튼 내부텍스트)
    amount = find_first_text(driver, [
        "//td[@data-title='사업금액']//*[self::input or self::span]",
        "//td[@data-title='배정예산']//*[self::input or self::span]",
        "//td[@data-title='추정가격']//*[self::input or self::span]",
        "//*[contains(@class,'btn_summary')]//p[starts-with(normalize-space(),'배정예산')]"
    ])
    if "배정예산" in amount:
        t = amount.split("배정예산")[-1].replace(":", "").replace("원", "").strip()
        amount = t

    # 5) 시작일시 (기존 방식 유지)
    start_dt = find_first_text(driver, [
        "//td[@col_id='startDt']//nobr",
        "//td[contains(@id,'gridList1_cell_0_') and @col_id='startDt']//nobr"
    ])

    # 6) 종료일시: 헤더 텍스트 기반으로 동적 col_id → 표시되는 첫 유효 셀 값
    end_dt = get_first_visible_cell_by_header(driver, "종료일시", grid_id_part="gridList1")
    if not end_dt:
        # 백업 패턴
        end_dt = find_first_text(driver, [
            "//td[@col_id='endDt']//nobr",
            "//td[starts-with(@col_id,'end') or contains(@col_id,'End') or contains(@col_id,'endDt')]//nobr",
            "//*[contains(@class,'w2grid')]//td[.//nobr][contains(.,'/') or contains(.,':')]"
        ])

    record = {
        "입찰공고번호": bid_no,
        "공고기관": org,
        "공고명": title,
        "사업금액": amount,
        "시작일시": start_dt,
        "종료일시": end_dt,
    }
    print("6단계: 추출 결과 →", record)
    return record


# ===== 메인 =====
def main():
    service = Service(ChromeDriverManager().install())
    options = webdriver.ChromeOptions()
    options.add_argument("--start-maximized")
    driver = webdriver.Chrome(service=service, options=options)
    wait = WebDriverWait(driver, 15)

    # 1~2단계: 목록 페이지 진입
    go_to_bid_list(driver, wait)
    list_handle = driver.current_window_handle

    # 3단계: 엑셀 로드
    bid_numbers = load_bid_numbers(EXCEL_PATH)

    all_rows = []
    header = ["입찰공고번호", "공고기관", "공고명", "사업금액", "시작일시", "종료일시"]

    for idx, bid_no in enumerate(bid_numbers, start=1):
        print(f"\n=== 처리 {idx}/{len(bid_numbers)} : {bid_no} ===")

        # 4단계: 검색
        search_bid_number(driver, wait, bid_no)
        time.sleep(0.5)  # 결과 갱신 대기

        # 5단계: 공고명 클릭 → 상세 진입
        try:
            click_bid_title(driver, wait, bid_no)
        except TimeoutException:
            print(f"⚠️ '{bid_no}' 상세 진입 실패(행/링크 미검출). 다음 번호로 진행.")
            continue

        # 팝업 있으면 닫고 뒤로가기 → 다음 공고
        if dismiss_popup_if_present(driver, timeout=3):
            print("팝업 처리 완료 → 뒤로가기 진행")
            driver.back()
            ensure_on_list(driver, wait, list_handle)
            time.sleep(0.3)
            continue

        # 6단계: 상세 파싱
        try:
            row = parse_detail(driver)
            if not row.get("입찰공고번호"):
                print("⚠️ 상세에서 입찰공고번호 미검출 → 건너뜀")
            else:
                all_rows.append(row)
        except Exception as e:
            print("⚠️ 상세 파싱 중 오류:", e)

        # 목록 복귀
        try:
            go_back_to_list(driver, wait, list_handle)
        except Exception as e:
            print("⚠️ 목록 복귀 실패:", e)
            break

        time.sleep(0.3)

        # 중간 저장(10건마다)
        if idx % 10 == 0 and all_rows:
            save_csv(OUT_CSV, all_rows, header)
            all_rows.clear()

    # 남은 데이터 저장
    if all_rows:
        save_csv(OUT_CSV, all_rows, header)

    print("\n전체 처리 완료. 브라우저는 닫지 않고 유지됩니다.")


if __name__ == "__main__":
    main()
