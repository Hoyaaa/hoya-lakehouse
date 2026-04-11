import os, glob, re
import pandas as pd

# ===== 0) 파일 경로 자동 탐색 =====
BASE_DIR = r"D:\재택근무\11월\11월_4주차\data"   # 폴더까지만 지정
# '입찰공고목록'으로 시작하고 .xls 또는 .xlsx 인 파일 모두 탐색
candidates = glob.glob(os.path.join(BASE_DIR, "입찰공고목록*.xls*"))

if not candidates:
    raise FileNotFoundError(f"폴더에 '입찰공고목록*.xls*' 파일을 찾지 못했습니다: {BASE_DIR}")

# 수정시간 기준 가장 최근 파일 선택
file_path = max(candidates, key=os.path.getmtime)
print(f"사용할 파일: {file_path}")

# ===== 1) 키워드 =====
exclude_keywords_text = """
제품 납품 시공 건축 시공 차량 급식 임차 구매 포장 조달 공사 운송 건립 수거 폐기물 증축
리모델링 구조물 공정 공법 철거 토목 공사 공장 출하 적재 하역 택배 운반 운송 물류 임대 버스 지하철
대관 구입 대금 사무기기 비품 폐기 폐 오염 하수 정화 청소 방역 분뇨 안전 위탁 병원 재개발 조명
의료 교통 주차 상수 감정 제설 상가 주행 회수 재활용 쓰레기 소각 매각 매입 코로나 반도체 터널
은행 보안 요금 분양 평가 유기물 회계 주택 방송 재해복구 소방시설 심사 콘크리트 수해복구 배수 집중호우
산불피해 건설 부대 수송 건설 여단 대대 병영 무기 도로 소방 운전면허
"""

def build_pattern(words: str) -> str:
    toks = [w.strip() for w in words.split() if w.strip()]
    toks = list(dict.fromkeys(toks))
    escaped = [re.escape(t) for t in toks]
    return "|".join(escaped)

def normalize_col(s: str) -> str:
    return re.sub(r"\s+", "", str(s or "")).strip()

def make_unique(cols):
    seen, out = {}, []
    for c in cols:
        if c not in seen:
            seen[c] = 0; out.append(c)
        else:
            seen[c] += 1; out.append(f"{c}_{seen[c]}")
    return out

def read_with_header_detection_any_sheet(path):
    """
    통합문서의 모든 시트를 돌며 '공고명' 또는 '제목'이 있는 행을 헤더로 인식해 DataFrame 반환.
    가장 먼저 발견된 시트를 사용.
    """
    xls = pd.ExcelFile(path, engine="openpyxl")
    for sh in xls.sheet_names:
        df0 = pd.read_excel(path, sheet_name=sh, header=None, dtype=str, engine="openpyxl")
        for i, row in df0.iterrows():
            row_norm = [normalize_col(x) for x in row.tolist()]
            if any("공고명" in x for x in row_norm) or any(x == "제목" for x in row_norm):
                cols = df0.iloc[i].astype(str).map(lambda x: x.strip())
                cols = [c if c != "nan" else "" for c in cols]
                cols_norm = [normalize_col(c) for c in cols]
                cols_final = make_unique(cols)
                df = df0.iloc[i+1:].copy()
                df.columns = cols_final
                colmap_norm = {cols_final[j]: cols_norm[j] for j in range(len(cols_final))}
                return df.reset_index(drop=True), colmap_norm, sh
    raise RuntimeError("어떤 시트에서도 헤더 행(공고명/제목)을 찾지 못했습니다.")

def find_announcement_col(colmap_norm):
    for orig, norm in colmap_norm.items():
        if "공고명" in norm or norm == "제목":
            return orig
    return None

# ===== 2) 읽기 + 헤더 자동탐지 =====
df, colmap_norm, used_sheet = read_with_header_detection_any_sheet(file_path)
print(f"시트: {used_sheet} | 원본 행: {len(df)}")

# ===== 3) 필터링 =====
col_gonggo = find_announcement_col(colmap_norm)
if not col_gonggo:
    raise KeyError("공고명(또는 제목) 컬럼을 찾지 못했습니다.")

exclude_pattern = build_pattern(exclude_keywords_text)
df[col_gonggo] = df[col_gonggo].astype(str)

df_filtered = df[~df[col_gonggo].str.contains(exclude_pattern, case=False, na=False)]
df_removed  = df[df[col_gonggo].str.contains(exclude_pattern, case=False, na=False)]

print(f"필터링 후: {len(df_filtered)}행 | 제외: {len(df_removed)}행")

# ===== 4) 저장 =====
out_dir = BASE_DIR
out_all      = os.path.join(out_dir, "입찰공고_전체데이터.xlsx")
out_keep     = os.path.join(out_dir, "입찰공고_필터링결과.xlsx")
out_removed  = os.path.join(out_dir, "입찰공고_제외된데이터.xlsx")

df.to_excel(out_all, index=False, engine="openpyxl")
df_filtered.to_excel(out_keep, index=False, engine="openpyxl")
df_removed.to_excel(out_removed, index=False, engine="openpyxl")

print("저장 완료 ⬇")
print(out_all)
print(out_keep)
print(out_removed)

